package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DADesignationSaveRequest {

    private Integer designationId;
    private String designationCode;
    private String designation;
    private String description;
    private Integer displayOrder;

    private List<DAColumnValueRequest> columnValues = new ArrayList<>();
}
