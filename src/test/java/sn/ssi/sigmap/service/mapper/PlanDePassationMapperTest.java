package sn.ssi.sigmap.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PlanDePassationMapperTest {

    private PlanDePassationMapper planDePassationMapper;

    @BeforeEach
    public void setUp() {
        planDePassationMapper = new PlanDePassationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(planDePassationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(planDePassationMapper.fromId(null)).isNull();
    }
}
