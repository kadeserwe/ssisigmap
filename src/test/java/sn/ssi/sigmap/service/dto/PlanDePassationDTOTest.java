package sn.ssi.sigmap.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.ssi.sigmap.web.rest.TestUtil;

public class PlanDePassationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanDePassationDTO.class);
        PlanDePassationDTO planDePassationDTO1 = new PlanDePassationDTO();
        planDePassationDTO1.setId(1L);
        PlanDePassationDTO planDePassationDTO2 = new PlanDePassationDTO();
        assertThat(planDePassationDTO1).isNotEqualTo(planDePassationDTO2);
        planDePassationDTO2.setId(planDePassationDTO1.getId());
        assertThat(planDePassationDTO1).isEqualTo(planDePassationDTO2);
        planDePassationDTO2.setId(2L);
        assertThat(planDePassationDTO1).isNotEqualTo(planDePassationDTO2);
        planDePassationDTO1.setId(null);
        assertThat(planDePassationDTO1).isNotEqualTo(planDePassationDTO2);
    }
}
