package lk.sampath.casadminportalms.entity.committee;

import jakarta.persistence.*;
import java.util.*;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CA_LEVEL_CONFIG")
public class CommitteeLevel extends UserTrackableEntity {

  @Id
  @Column(name = "LEVEL_CONFIG_ID", nullable = false, updatable = false)
  private Integer levelId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
  private Committee committee;

  @Column(name = "LEVEL_ID")
  private Integer level;

  @Column(name = "COMBINATION")
  private String combination;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private AppsConstants.Status status;

  @Enumerated(EnumType.STRING)
  @Column(name = "PATH_TYPE")
  private AppsConstants.CAPathType pathType;

  @Column(name = "USER_COUNT")
  private Integer userCount;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "committeeLevel")
  private List<LevelUser> levelUsers;

  public List<LevelUser> getLevelUsers() {
    if (this.levelUsers == null) {
      this.levelUsers = new ArrayList<>();
    }
    return levelUsers;
  }

  public Integer getLevelId() {
    if (this.levelId == null) {
      this.levelId = 0;
    }
    return levelId;
  }
}
