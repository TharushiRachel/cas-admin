package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DADesignationRequest {

    private Integer designationId;
    private String designation;
    private String designationCode;
    private String description;
    private Integer displayOrder;
    private String isCommittee;
    private String tableType;
    private String status;
    private List<DALimitValueRequest> limits = new ArrayList<>();
}
