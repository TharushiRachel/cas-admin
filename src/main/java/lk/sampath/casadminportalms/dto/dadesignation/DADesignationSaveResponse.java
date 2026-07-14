package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DADesignationSaveResponse {

    private Integer designationId;
    private String designationCode;
    private String designation;
    private String description;
    private String tableType;
    private String isCommittee;
    private Integer displayOrder;
    private String status;

    private Map<String, Double> values = new LinkedHashMap<>();
}
