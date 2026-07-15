package lk.sampath.casadminportalms.entity.committee;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CASV3_LEVEL_AUD")
public class CommitteeLevelHistory extends UserTrackableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CASV3_LEVEL_AUD")
  @SequenceGenerator(
      name = "SEQ_CASV3_LEVEL_AUD",
      sequenceName = "SEQ_CASV3_LEVEL_AUD",
      allocationSize = 1)
  @Column(name = "LEVEL_AUD_ID")
  private Integer levelAudId;

  @Column(name = "LEVEL_CONFIG_ID")
  private Integer levelId;

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

  @Column(name = "COMMITTEE_ID")
  private Integer committee;
}
