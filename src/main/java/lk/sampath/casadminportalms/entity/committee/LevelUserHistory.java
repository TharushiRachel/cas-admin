package lk.sampath.casadminportalms.entity.committee;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Data;

@Data
@Entity
@Table(name = "CASV3_LEVEL_USER_AUD")
public class LevelUserHistory extends UserTrackableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CASV3_LEVEL_USER_AUD")
  @SequenceGenerator(
      name = "SEQ_CASV3_LEVEL_USER_AUD",
      sequenceName = "SEQ_CASV3_LEVEL_USER_AUD",
      allocationSize = 1)
  @Column(name = "USER_AUD_ID")
  private Integer levelUserAudId;

  @Column(name = "USER_CONFIG_ID")
  private Integer levelUserId;

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

  @Column(name = "COMMITTEE_ID")
  private Integer committee;

  @Column(name = "LEVEL_CONFIG_ID")
  private Integer committeeLevel;
}
