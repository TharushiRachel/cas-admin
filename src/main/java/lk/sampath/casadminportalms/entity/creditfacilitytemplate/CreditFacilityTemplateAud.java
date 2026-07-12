package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "CASV3_T_CREDIT_FACILITY_TEMPLATE_AUD")
public class CreditFacilityTemplateAud extends ApprovableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_CREDIT_FACILITY_TEMPLATE_AUD")
    @SequenceGenerator(name = "CASV3_SEQ_CREDIT_FACILITY_TEMPLATE_AUD", sequenceName = "CASV3_SEQ_CREDIT_FACILITY_TEMPLATE_AUD", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CREDIT_FACILITY_TEMPLATE_ID")
    private Integer creditFacilityTemplateID;

    @Column(name = "CREDIT_FACILITY_TYPE_ID")
    private Integer creditFacilityType;

    @Column(name = "CREDIT_FACILITY_NAME")
    private String creditFacilityName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MAXIMUM_FACILITY_AMOUNT")
    private BigDecimal maxFacilityAmount;

    @Column(name = "MINIMUM_FACILITY_AMOUNT")
    private BigDecimal minFacilityAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_PURPOSE")
    private AppsConstants.YesNo showPurpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_REPAYMENT")
    private AppsConstants.YesNo showRepayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_CONDITION")
    private AppsConstants.YesNo showCondition;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_REMARK")
    private AppsConstants.YesNo showRemark;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_CALCULATOR")
    private AppsConstants.YesNo showCalculator;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_RENTAL_DATA")
    private AppsConstants.YesNo showRentalData;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppsConstants.Status status;

    @Column(name = "NEW_FACILITY_EMAIL")
    private String newFacilityEmail;

    @Column(name = "EXISTING_FACILITY_EMAIL")
    private String existingFacilityEmail;
}
