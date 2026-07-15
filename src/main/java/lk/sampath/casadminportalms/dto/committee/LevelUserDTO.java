package lk.sampath.casadminportalms.dto.committee;

import java.util.Date;
import lk.sampath.casadminportalms.entity.committee.LevelUser;
import lk.sampath.casadminportalms.entity.committee.LevelUserTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LevelUserDTO {

  private Integer levelUserId;

  private Integer parentUserId;

  private Integer levelId;

  private Integer committeeId;

  private Integer userId;

  private String userName;

  private String userDisplayName;

  private AppsConstants.RecordStatus recordStatus;

  private Date createdDate;

  private String createdBy;

  public LevelUserDTO(LevelUserTemp levelUserTemp) {
    this.levelUserId = levelUserTemp.getLevelUserId();
    this.levelId = levelUserTemp.getCommitteeLevelTemp().getLevelId();
    this.committeeId = levelUserTemp.getCommitteeTemp().getCommitteeId();
    this.userId = levelUserTemp.getCommitteePool().getUserId();
    this.userName = levelUserTemp.getCommitteePool().getUserName();
    this.userDisplayName = levelUserTemp.getCommitteePool().getUserDisplayName();
    this.createdBy = levelUserTemp.getCreatedBy();
    this.createdDate = levelUserTemp.getCreatedDate();
  }

  public LevelUserDTO(LevelUser levelUser) {
    this.levelUserId = levelUser.getLevelUserId();
    this.levelId = levelUser.getCommitteeLevel().getLevelId();
    this.committeeId = levelUser.getCommittee().getCommitteeId();
    this.userId = levelUser.getCommitteePool().getUserId();
    this.userName = levelUser.getCommitteePool().getUserName();
    this.userDisplayName = levelUser.getCommitteePool().getUserDisplayName();
    this.createdBy = levelUser.getCreatedBy();
    this.createdDate = levelUser.getCreatedDate();
  }

  public Integer getLevelUserId() {
    if (this.levelUserId == null) {
      this.levelUserId = 0;
    }
    return levelUserId;
  }

  public Integer getParentUserId() {
    if (this.parentUserId == null) {
      this.parentUserId = 0;
    }
    return parentUserId;
  }
}
