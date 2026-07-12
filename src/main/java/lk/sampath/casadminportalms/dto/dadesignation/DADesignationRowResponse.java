package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DADesignationRowResponse {

    private Integer designationId;
    private String designation;
    private String designationCode;
    private String description;
    private Integer displayOrder;
    private String isCommittee;
    private String status;
    private String approveStatus;

    /**
     * Leaf header id (columnId) -> risk value for the cell.
     */
    private Map<String, Double> values = new LinkedHashMap<>();
}
