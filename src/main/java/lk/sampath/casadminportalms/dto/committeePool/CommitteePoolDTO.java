package lk.sampath.casadminportalms.dto.committeePool;

import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolTemp;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

import java.util.Date;

@Data
public class CommitteePoolDTO {

    private Integer committeePoolId;

    private String userName;

    private String userDisplayName;

    private String designation;

    private String workClass;

    private Status status;

    private Integer userId;

    private Date createdDate;

    private String createdBy;

    private Date lastModifiedDate;

    private String modifiedBy;

    private MasterDataApproveStatus approveStatus;

    private Date approvedDate;

    private String approvedBy;

    public CommitteePoolDTO(){}

    public CommitteePoolDTO(CommitteePool committeePool){
        this.committeePoolId = committeePool.getCommitteePoolId();
        this.userName = committeePool.getUserName();
        this.userDisplayName = committeePool.getUserDisplayName();
        this.designation = committeePool.getDesignation();
        this.workClass = committeePool.getWorkClass();
        this.userId = committeePool.getUserId();
        this.status = committeePool.getStatus();
        this.createdDate = committeePool.getCreatedDate();
        this.createdBy = committeePool.getCreatedBy();
        this.lastModifiedDate = committeePool.getLastModifiedDate();
        this.modifiedBy = committeePool.getModifiedBy();
        this.approveStatus = committeePool.getApproveStatus();
        this.approvedDate = committeePool.getApprovedDate();
        this.approvedBy = committeePool.getApprovedBy();
    }

    public CommitteePoolDTO(CommitteePoolTemp committeePoolTemp){
        this.committeePoolId = committeePoolTemp.getCommitteePoolId();
        this.userName = committeePoolTemp.getUserName();
        this.userDisplayName = committeePoolTemp.getUserDisplayName();
        this.designation = committeePoolTemp.getDesignation();
        this.workClass = committeePoolTemp.getWorkClass();
        this.userId = committeePoolTemp.getUserId();
        this.status = committeePoolTemp.getStatus();
        this.createdDate = committeePoolTemp.getCreatedDate();
        this.createdBy = committeePoolTemp.getCreatedBy();
        this.lastModifiedDate = committeePoolTemp.getLastModifiedDate();
        this.modifiedBy = committeePoolTemp.getModifiedBy();
        this.approveStatus = committeePoolTemp.getApproveStatus();
        this.approvedDate = committeePoolTemp.getApprovedDate();
        this.approvedBy = committeePoolTemp.getApprovedBy();
    }
}
