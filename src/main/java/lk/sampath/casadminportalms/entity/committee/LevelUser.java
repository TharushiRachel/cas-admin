package lk.sampath.casadminportalms.entity.committee;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CA_USER_CONFIG")
public class LevelUser extends UserTrackableEntity {

  @Id
  @Column(name = "USER_CONFIG_ID", nullable = false, updatable = false)
  private Integer levelUserId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
  private Committee committee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LEVEL_CONFIG_ID", referencedColumnName = "LEVEL_CONFIG_ID")
  private CommitteeLevel committeeLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_NAME", referencedColumnName = "USER_NAME")
  private CommitteePool committeePool;

  @Enumerated(EnumType.STRING)
  @Column(name = "PATH_TYPE")
  private AppsConstants.CAPathType pathType;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_STATUS")
  private AppsConstants.Status userStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private AppsConstants.Status status;

  public Integer getLevelUserId() {
    if (this.levelUserId == null) {
      this.levelUserId = 0;
    }
    return levelUserId;
  }
}
