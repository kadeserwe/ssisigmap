package sn.ssi.sigmap.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class PlanDePassationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanDePassation.class);
        PlanDePassation planDePassation1 = new PlanDePassation();
        planDePassation1.setId(1L);
        PlanDePassation planDePassation2 = new PlanDePassation();
        planDePassation2.setId(planDePassation1.getId());
        assertThat(planDePassation1).isEqualTo(planDePassation2);
        planDePassation2.setId(2L);
        assertThat(planDePassation1).isNotEqualTo(planDePassation2);
        planDePassation1.setId(null);
        assertThat(planDePassation1).isNotEqualTo(planDePassation2);
    }
}
