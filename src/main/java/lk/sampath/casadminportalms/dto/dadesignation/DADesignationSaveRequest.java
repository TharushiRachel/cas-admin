package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DADesignationSaveRequest {

    /**
     * Required. Stored as DESIGNATION_ID on DA_LIMITS_TEMP (no DA_DESIGNATION write).
     */
    private Integer designationId;

    /**
     * Optional metadata for API response only (not persisted on DA_LIMITS_TEMP).
     */
    private String designationCode;
    private String designation;
    private String description;
    private Integer displayOrder;

    private List<DAColumnValueRequest> columnValues = new ArrayList<>();
}
