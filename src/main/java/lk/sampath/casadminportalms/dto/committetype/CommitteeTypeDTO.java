package lk.sampath.casadminportalms.dto.committetype;

import java.io.Serializable;
import java.util.Date;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Data;

@Data
public class CommitteeTypeDTO implements Serializable {

  private Integer committeeTypeId;

  private String committeeType;

  private String committeeTypeName;

  private String createdUserDisplayName;

  private AppsConstants.Status status;

  private Date createdDate;

  private String createdBy;

  private Date modifiedDate;

  private String modifiedBy;

  private Integer isSystem;

  public CommitteeTypeDTO() {}

  public CommitteeTypeDTO(CommitteeType committeeTypeE) {
    this.committeeTypeId = committeeTypeE.getCommitteeTypeId();
    this.committeeType = committeeTypeE.getCommitteeTypeName();
    this.committeeTypeName = committeeTypeE.getCommitteeTypeDescription();
    this.createdUserDisplayName = committeeTypeE.getCreatedUserDisplayName();
    this.status = committeeTypeE.getStatus();
    this.createdDate = committeeTypeE.getCreatedDate();
    this.createdBy = committeeTypeE.getCreatedBy();
    this.modifiedDate = committeeTypeE.getLastModifiedDate();
    this.modifiedBy = committeeTypeE.getModifiedBy();
    this.isSystem = committeeTypeE.getIsSystem();
  }
}
