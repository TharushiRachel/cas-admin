package lk.sampath.casadminportalms.dto.committee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.entity.committee.CommitteeTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Data;

@Data
public class CommitteeDTO {

  private Integer committeeId;

  private Integer parentCommitteeId;

  private String committeeName;

  private BigDecimal delegatedAuthority;

  private AppsConstants.Status status;

  private MasterDataApproveStatus approveStatus;

  private Date approvedDate;

  private String approvedBy;

  private Date createdDate;

  private String createdBy;

  private Date lastModifiedDate;

  private String modifiedBy;

  private String reviewer;

  private AppsConstants.CAPathType currentPath;

  private Integer committeeTypeId;

  private String committeeType;

  private String committeeTypeName;

  private AppsConstants.RecordStatus recordStatus;

  private List<CommitteeLevelDTO> levels;

  private String reviewerDisplayName;

  public CommitteeDTO() {}

  public CommitteeDTO(CommitteeTemp committeeTemp) {
    this.committeeId = committeeTemp.getCommitteeId();
    this.committeeName = committeeTemp.getCommitteeName();
    this.delegatedAuthority = committeeTemp.getDelegatedAuthority();
    this.status = committeeTemp.getStatus();
    this.createdDate = committeeTemp.getCreatedDate();
    this.createdBy = committeeTemp.getCreatedBy();
    this.currentPath = committeeTemp.getCurrentPath();
    this.recordStatus = committeeTemp.getRecordStatus();
    this.approveStatus = committeeTemp.getApproveStatus();
    if (committeeTemp.getCommitteeType() != null) {
      this.committeeTypeId = committeeTemp.getCommitteeType().getCommitteeTypeId();
      this.committeeType = committeeTemp.getCommitteeType().getCommitteeTypeName();
      this.committeeTypeName = committeeTemp.getCommitteeType().getCommitteeTypeDescription();
    }
    if (committeeTemp.getCommitteeLevels() != null) {
      this.levels =
          committeeTemp.getCommitteeLevels().stream().map(CommitteeLevelDTO::new).toList();
    } else {
      this.levels = new ArrayList<>();
    }
  }

  public CommitteeDTO(Committee committee) {
    this.committeeId = committee.getCommitteeId();
    this.committeeName = committee.getCommitteeName();
    this.delegatedAuthority = committee.getDelegatedAuthority();
    this.status = committee.getStatus();
    this.approveStatus = committee.getApproveStatus();
    this.approvedDate = committee.getApprovedDate();
    this.approvedBy = committee.getCommitteeName();
    this.createdDate = committee.getCreatedDate();
    this.createdBy = committee.getCreatedBy();
    this.lastModifiedDate = committee.getLastModifiedDate();
    this.modifiedBy = committee.getModifiedBy();
    this.currentPath = committee.getCurrentPath();
    if (committee.getCommitteeType() != null) {
      this.committeeTypeId = committee.getCommitteeType().getCommitteeTypeId();
      this.committeeType = committee.getCommitteeType().getCommitteeTypeName();
      this.committeeTypeName = committee.getCommitteeType().getCommitteeTypeDescription();
    }

    if (committee.getCommitteeLevels() != null) {
      this.levels = committee.getCommitteeLevels().stream().map(CommitteeLevelDTO::new).toList();
    } else {
      this.levels = new ArrayList<>();
    }
  }

  public Integer getCommitteeId() {
    if (this.committeeId == null) {
      this.committeeId = 0;
    }
    return committeeId;
  }

  public Integer getParentCommitteeId() {
    if (this.parentCommitteeId == null) {
      this.parentCommitteeId = 0;
    }
    return parentCommitteeId;
  }

  public List<CommitteeLevelDTO> getLevels() {
    if (this.levels == null) {
      this.levels = new ArrayList<>();
    }
    return levels;
  }
}
