package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "CASV3_T_CFT_CUSTOM_FACILITY_INFO_AUD")
@RequiredArgsConstructor
@AllArgsConstructor
public class CftCustomFacilityInfoAud  extends UserTrackableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_CFT_CUSTOM_FACILITY_INFO_AUD")
    @SequenceGenerator(name = "CASV3_SEQ_CFT_CUSTOM_FACILITY_INFO_AUD", sequenceName = "CASV3_SEQ_CFT_CUSTOM_FACILITY_INFO_AUD", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CUSTOM_FACILITY_INFO_ID")
    private Integer cftCustomFacilityInfoID;

    @Column(name = "CREDIT_FACILITY_TEMPLATE_ID")
    private Integer creditFacilityTemplate;

    @Column(name = "CUSTOM_FACILITY_INFO_NAME")
    private String customFacilityInfoName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CUSTOM_FACILITY_INFO_CODE")
    private String customFacilityInfoCode;

    @Column(name = "FIELD_TYPE")
    private String fieldType;

    @Enumerated(EnumType.STRING)
    @Column(name = "MANDATORY")
    private AppsConstants.YesNo mandatory;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppsConstants.Status status;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "RECORD_STATUS")
    private String recordStatus;

}
