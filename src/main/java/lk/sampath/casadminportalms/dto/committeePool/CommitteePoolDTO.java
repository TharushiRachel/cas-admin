package lk.sampath.casadminportalms.dto.committeepool;

import java.util.Date;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Data;

@Data
public class CommitteePoolDTO {

  private Integer committeePoolId;

  private String userName;

  private String userDisplayName;

  private String designation;

  private String workClass;

  private AppsConstants.Status status;

  private Integer userId;

  private Date createdDate;

  private String createdBy;

  private Date lastModifiedDate;

  private String modifiedBy;

  private MasterDataApproveStatus approveStatus;

  private Date approvedDate;

  private String approvedBy;

  private Integer parentRecId;

  public CommitteePoolDTO() {}

  public CommitteePoolDTO(CommitteePool committeePool) {
    this.committeePoolId = committeePool.getPoolId();
    this.userName = committeePool.getUserName();
    this.userDisplayName = committeePool.getUserDisplayName();
    this.designation = committeePool.getReferenceName();
    this.workClass = committeePool.getGroupCode();
    this.userId = committeePool.getUserId();
    this.status = committeePool.getUserStatus();
    this.createdDate = committeePool.getCreatedDate();
    this.createdBy = committeePool.getCreatedBy();
    this.lastModifiedDate = committeePool.getLastModifiedDate();
    this.modifiedBy = committeePool.getModifiedBy();
    this.approveStatus = committeePool.getApproveStatus();
    this.approvedDate = committeePool.getApprovedDate();
    this.approvedBy = committeePool.getApprovedBy();
  }

  public CommitteePoolDTO(CommitteePoolTemp committeePoolTemp) {
    this.committeePoolId = committeePoolTemp.getPoolId();
    this.userName = committeePoolTemp.getUserName();
    this.userDisplayName = committeePoolTemp.getUserDisplayName();
    this.designation = committeePoolTemp.getReferenceName();
    this.workClass = committeePoolTemp.getGroupCode();
    this.userId = committeePoolTemp.getUserId();
    this.status = committeePoolTemp.getUserStatus();
    this.createdDate = committeePoolTemp.getCreatedDate();
    this.createdBy = committeePoolTemp.getCreatedBy();
    this.lastModifiedDate = committeePoolTemp.getLastModifiedDate();
    this.modifiedBy = committeePoolTemp.getModifiedBy();
    this.approveStatus = committeePoolTemp.getApproveStatus();
    this.approvedDate = committeePoolTemp.getApprovedDate();
    this.approvedBy = committeePoolTemp.getApprovedBy();
  }
}
