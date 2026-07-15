package lk.sampath.casadminportalms.dto.dadesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import lk.sampath.casadminportalms.entity.daDesignation.DALimitTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DALimitRecordDTO {

    private Integer daLimitsId;
    private Integer designationId;
    private Integer columnId;
    private Double riskValue;
    private String riskRating;
    private String isCommittee;
    private AppsConstants.Status status;
    private MasterDataApproveStatus approveStatus;
    private String authorizerDisplayName;

    public DALimitRecordDTO(DALimit entity) {
        if (entity == null) {
            return;
        }
        this.daLimitsId = entity.getDaLimitsId();
        this.designationId = entity.getDesignationId();
        this.columnId = entity.getColumnId();
        this.riskValue = entity.getRiskValue();
        this.riskRating = entity.getRiskRating();
        this.isCommittee = entity.getIsCommittee();
        this.status = entity.getStatus();
        this.approveStatus = entity.getApproveStatus();
        this.authorizerDisplayName = entity.getAuthorizerDisplayName();
    }

    public DALimitRecordDTO(DALimitTemp entity) {
        if (entity == null) {
            return;
        }
        this.daLimitsId = entity.getDaLimitsId();
        this.designationId = entity.getDesignation() != null ? entity.getDesignation().getId() : null;
        this.columnId = entity.getColumnId();
        this.riskValue = entity.getRiskValue();
        this.riskRating = entity.getRiskRating();
        this.isCommittee = entity.getIsCommittee();
        this.status = entity.getStatus();
        this.approveStatus = entity.getApproveStatus();
        this.authorizerDisplayName = entity.getAuthorizerDisplayName();
    }
}
