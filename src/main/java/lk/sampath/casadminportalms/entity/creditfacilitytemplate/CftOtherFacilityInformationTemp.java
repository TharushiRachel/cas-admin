package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.InputFieldValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "CASV3_T_CFT_OTHER_FACILITY_INFO_TEMP")
@RequiredArgsConstructor
@AllArgsConstructor
public class CftOtherFacilityInformationTemp  extends UserTrackableEntity {


    @Id
    @Column(name = "OTHER_FACILITY_INFO_ID")
    private Integer cftOtherFacilityInfoID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDIT_FACILITY_TEMPLATE_ID", referencedColumnName = "CREDIT_FACILITY_TEMPLATE_ID")
    private CreditFacilityTemplateTemp creditFacilityTemplate;

    @Column(name = "OTHER_FACILITY_INFO_NAME")
    private String otherFacilityInfoName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OTHER_FACILITY_INFO_CODE")
    private String otherFacilityInfoCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "FIELD_TYPE")
    private InputFieldValueType otherFacilityInfoFieldType;

    @Column(name = "DEFAULT_VALUE")
    private String defaultValue;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "MANDATORY")
    private AppsConstants.YesNo mandatory;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppsConstants.Status status;

    @Column(name = "RECORD_STATUS")
    private String recordStatus;
}
