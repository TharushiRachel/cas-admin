package lk.sampath.casadminportalms.entity.committeepool;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CASV3_CA_POOL_CONFIG_AUD")
public class CommitteePoolHistory extends ApprovableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CASV3_CA_POOL_CONFIG_AUD")
  @SequenceGenerator(
      name = "SEQ_CASV3_CA_POOL_CONFIG_AUD",
      sequenceName = "SEQ_CASV3_CA_POOL_CONFIG_AUD",
      allocationSize = 1)
  @Column(name = "COMMITTEE_POOL_AUD_ID")
  private Integer committeePoolAudId;

  @Column(name = "POOL_ID")
  private Integer poolId;

  @Column(name = "USER_ID")
  private Integer userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "USER_DISPLAY_NAME")
  private String userDisplayName;

  @Column(name = "REFERENCE_NAME")
  private String designation;

  @Column(name = "GROUP_CODE")
  private String workClass;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_STATUS")
  private AppsConstants.Status status;
}
