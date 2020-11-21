package sn.ssi.sigmap.gateway.responserewriting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.post.SendResponseFilter;
import springfox.documentation.swagger2.web.Swagger2Controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Zuul filter to rewrite micro-services Swagger URL Base Path.
 */
public class SwaggerBasePathRewritingFilter extends SendResponseFilter {

    private final Logger log = LoggerFactory.getLogger(SwaggerBasePathRewritingFilter.class);

    private ObjectMapper mapper = new ObjectMapper();

    public SwaggerBasePathRewritingFilter() {
        super(new ZuulProperties());
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    /**
     * Filter requests to micro-services Swagger docs.
     */
    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getRequest().getRequestURI().endsWith(Swagger2Controller.DEFAULT_URL);
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();

        context.getResponse().setCharacterEncoding("UTF-8");

        String rewrittenResponse = rewriteBasePath(context);
        if (context.getResponseGZipped()) {
            try {
                context.setResponseDataStream(new ByteArrayInputStream(gzipData(rewrittenResponse)));
            } catch (IOException e) {
                log.error("Swagger-docs filter error", e);
            }
        } else {
            context.setResponseBody(rewrittenResponse);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private String rewriteBasePath(RequestContext context) {
        InputStream responseDataStream = context.getResponseDataStream();
        String requestUri = RequestContext.getCurrentContext().getRequest().getRequestURI();
        try {
            if (context.getResponseGZipped()) {
                responseDataStream = new GZIPInputStream(context.getResponseDataStream());
            }
            String response = IOUtils.toString(responseDataStream, StandardCharsets.UTF_8);
            if (response != null) {
                LinkedHashMap<String, Object> map = this.mapper.readValue(response, LinkedHashMap.class);

                String basePath = requestUri.replace(Swagger2Controller.DEFAULT_URL, "");
                map.put("basePath", basePath);
                log.debug("Swagger-docs: rewritten Base URL with correct micro-service route: {}", basePath);
                return mapper.writeValueAsString(map);
            }
        } catch (IOException e) {
            log.error("Swagger-docs filter error", e);
        }
        return null;
    }

    public static byte[] gzipData(String content) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter gzip = new PrintWriter(new GZIPOutputStream(bos));
        gzip.print(content);
        gzip.flush();
        gzip.close();
        return bos.toByteArray();
    }
}