package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "T_CFT_VITAL_INFO")
@EntityListeners(AuditingEntityListener.class)
public class CftVitalInfo extends UserTrackableEntity {

  @Id
  @Column(name = "CFT_VITAL_INFO_ID")
  private Integer cftVitalInfoID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CREDIT_FACILITY_TEMPLATE_ID")
  private CreditFacilityTemplate creditFacilityTemplate;

  @Column(name = "VITAL_INFO_NAME")
  private String vitalInfoName;

  @Enumerated(EnumType.STRING)
  @Column(name = "MANDATORY")
  private AppsConstants.YesNo mandatory;

  @Column(name = "DISPLAY_ORDER")
  private Integer displayOrder;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private AppsConstants.Status status;

  @Column(name = "RECORD_STATUS")
  private String recordStatus;
}
