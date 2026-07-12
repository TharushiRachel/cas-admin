package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "CASV3_T_CFT_CUSTOM_FACILITY_INFO_TEMP")
@RequiredArgsConstructor
@AllArgsConstructor
public class CftCustomFacilityInfoTemp  extends UserTrackableEntity {

    @Id
    @Column(name = "CUSTOM_FACILITY_INFO_ID")
    private Integer cftCustomFacilityInfoID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDIT_FACILITY_TEMPLATE_ID")
    private CreditFacilityTemplateTemp creditFacilityTemplate;

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
