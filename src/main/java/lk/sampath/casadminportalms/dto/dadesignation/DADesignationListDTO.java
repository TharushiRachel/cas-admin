package lk.sampath.casadminportalms.dto.dadesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DADesignationData;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DADesignationListDTO {

    private Integer designationId;
    private String designationCode;
    private String designation;
    private String description;
    private Integer displayOrder;
    private String isCommittee;
    private Status status;
    private MasterDataApproveStatus approveStatus;

    public DADesignationListDTO(DADesignationData entity) {
        this(entity, entity != null ? entity.getIsCommittee() : null);
    }

    public DADesignationListDTO(DADesignationData entity, String isCommittee) {
        if (entity == null) {
            return;
        }
        this.designationId = entity.getId();
        this.designationCode = entity.getDesignationCode();
        this.designation = entity.getDesignation();
        this.description = entity.getDescription();
        this.displayOrder = entity.getDisplayOrder();
        this.isCommittee = isCommittee;
        this.status = entity.getStatus();
        this.approveStatus = entity.getApproveStatus();
    }
}
