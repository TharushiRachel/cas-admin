package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

@Data
public class DALimitValueRequest {

    private Integer columnId;
    private Double riskValue;
    private String riskRating;
}
