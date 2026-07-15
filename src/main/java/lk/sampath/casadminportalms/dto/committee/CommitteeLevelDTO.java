package lk.sampath.casadminportalms.dto.committee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.sampath.casadminportalms.entity.committee.CommitteeLevel;
import lk.sampath.casadminportalms.entity.committee.CommitteeLevelTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommitteeLevelDTO {

  private Integer levelId;

  private Integer parentLevelId;

  private Integer level;

  private String combination;

  private Integer userCount;

  private AppsConstants.CAPathType pathType;

  private Integer committeeId;

  private AppsConstants.Status status;

  private Date createdDate;

  private String createdBy;

  private Date lastModifiedDate;

  private String modifiedBy;

  private AppsConstants.RecordStatus recordStatus;

  private List<LevelUserDTO> levelUsers;

  public CommitteeLevelDTO(CommitteeLevelTemp committeeLevelTemp) {
    this.levelId = committeeLevelTemp.getLevelId();
    this.committeeId = committeeLevelTemp.getCommitteeTemp().getCommitteeId();
    this.level = committeeLevelTemp.getLevel();
    this.pathType = committeeLevelTemp.getPathType();
    this.combination = committeeLevelTemp.getCombination();
    this.userCount = committeeLevelTemp.getUserCount();
    this.createdBy = committeeLevelTemp.getCreatedBy();
    this.createdDate = committeeLevelTemp.getCreatedDate();

    if (committeeLevelTemp.getLevelUsers() != null) {
      this.levelUsers = committeeLevelTemp.getLevelUsers().stream().map(LevelUserDTO::new).toList();
    } else {
      this.levelUsers = new ArrayList<>();
    }
  }

  public CommitteeLevelDTO(CommitteeLevel committeeLevel) {
    this.levelId = committeeLevel.getLevelId();
    this.committeeId = committeeLevel.getCommittee().getCommitteeId();
    this.level = committeeLevel.getLevel();
    this.pathType = committeeLevel.getPathType();
    this.combination = committeeLevel.getCombination();
    this.userCount = committeeLevel.getUserCount();
    this.createdBy = committeeLevel.getCreatedBy();
    this.createdDate = committeeLevel.getCreatedDate();

    if (committeeLevel.getLevelUsers() != null) {
      this.levelUsers = committeeLevel.getLevelUsers().stream().map(LevelUserDTO::new).toList();
    } else {
      this.levelUsers = new ArrayList<>();
    }
  }

  public Integer getLevelId() {
    if (this.levelId == null) {
      this.levelId = 0;
    }
    return levelId;
  }

  public Integer getParentLevelId() {
    if (this.parentLevelId == null) {
      this.parentLevelId = 0;
    }
    return parentLevelId;
  }

  public List<LevelUserDTO> getLevelUsers() {
    if (this.levelUsers == null) {
      this.levelUsers = new ArrayList<>();
    }
    return levelUsers;
  }
}
