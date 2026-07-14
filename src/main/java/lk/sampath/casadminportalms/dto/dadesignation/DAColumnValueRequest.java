package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

@Data
public class DAColumnValueRequest {

    /**
     * Leaf column key from headers response (BORROWER RISK GRADING.subId).
     */
    private Integer subId;

    private Double riskValue;
}
