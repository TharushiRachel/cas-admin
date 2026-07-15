package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "CASV3_T_CFT_VITAL_INFO_AUD")
public class CftVitalInfoAud extends UserTrackableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_CFT_VITAL_INFO_AUD")
  @SequenceGenerator(
      name = "CASV3_SEQ_CFT_VITAL_INFO_AUD",
      sequenceName = "CASV3_SEQ_CFT_VITAL_INFO_AUD",
      allocationSize = 1)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "CFT_VITAL_INFO_ID")
  private Integer cftVitalInfoID;

  @Column(name = "CREDIT_FACILITY_TEMPLATE_ID")
  private Integer creditFacilityTemplate;

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
