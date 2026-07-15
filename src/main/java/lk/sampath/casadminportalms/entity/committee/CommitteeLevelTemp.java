package lk.sampath.casadminportalms.entity.committee;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CA_LEVEL_CONFIG_TEMP")
public class CommitteeLevelTemp extends UserTrackableEntity {

  @Id
  @Column(name = "LEVEL_CONFIG_ID", nullable = false, updatable = false)
  private Integer levelId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
  private CommitteeTemp committeeTemp;

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

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "committeeLevelTemp",
      orphanRemoval = true)
  private List<LevelUserTemp> levelUsers;

  public List<LevelUserTemp> getLevelUsers() {
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
