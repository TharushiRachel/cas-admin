package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DADesignationSaveRequest {

    /**
     * Optional. When provided, updates an existing designation row.
     */
    private Integer designationId;

    private String designationCode;
    private String designation;
    private String description;

    /**
     * COMMITTEE or INDIVIDUAL (same as headers split).
     */
    private String tableType;

    private Integer displayOrder;

    private List<DAColumnValueRequest> columnValues = new ArrayList<>();
}
