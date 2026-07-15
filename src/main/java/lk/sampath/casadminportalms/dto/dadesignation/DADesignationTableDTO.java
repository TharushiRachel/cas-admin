package lk.sampath.casadminportalms.dto.dadesignation;

import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DADesignationTableDTO {

    private Integer designationId;
    private String designationCode;
    private String designation;
    private String description;
    private String tableType;
    private String isCommittee;
    private Integer displayOrder;
    private Status status;
    private MasterDataApproveStatus approveStatus;

    private Map<Integer, Double> tableValues = new LinkedHashMap<>();
}
