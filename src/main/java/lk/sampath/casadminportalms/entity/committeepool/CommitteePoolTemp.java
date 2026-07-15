package lk.sampath.casadminportalms.entity.committeepool;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CA_POOL_CONFIG_TEMP")
public class CommitteePoolTemp extends ApprovableEntity {

  @Id
  @Column(name = "USER_ID")
  private Integer userId;

  @Column(name = "POOL_ID")
  private Integer poolId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "USER_DISPLAY_NAME")
  private String userDisplayName;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_STATUS")
  private AppsConstants.Status userStatus;

  @Column(name = "GROUP_CODE")
  private String groupCode;

  @Column(name = "REFERENCE_NAME")
  private String referenceName;
}
