package sn.ssi.sigmap.service.mapper;


import sn.ssi.sigmap.domain.*;
import sn.ssi.sigmap.service.dto.PlanDePassationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanDePassation} and its DTO {@link PlanDePassationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlanDePassationMapper extends EntityMapper<PlanDePassationDTO, PlanDePassation> {



    default PlanDePassation fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlanDePassation planDePassation = new PlanDePassation();
        planDePassation.setId(id);
        return planDePassation;
    }
}
