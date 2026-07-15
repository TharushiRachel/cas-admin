package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
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
@Table(name = "T_CREDIT_FACILITY_TEMPLATE")
public class CreditFacilityTemplate extends ApprovableEntity {

  @Id
  @Column(name = "CREDIT_FACILITY_TEMPLATE_ID")
  private Integer creditFacilityTemplateID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CREDIT_FACILITY_TYPE_ID")
  private CreditFacilityType creditFacilityType;

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

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL},
      mappedBy = "creditFacilityTemplate")
  private Set<CftVitalInfo> cftVitalInfos;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL},
      mappedBy = "creditFacilityTemplate")
  private Set<CftInterestRate> cftInterestRates;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL},
      mappedBy = "creditFacilityTemplate")
  private Set<CftSupportingDoc> cftSupportingDocs;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL},
      mappedBy = "creditFacilityTemplate")
  private Set<CftOtherFacilityInformation> cftOtherFacilityInformations;

  //    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy =
  // "creditFacilityTemplate")
  //    private Set<FPDocumentElement> fPDocumentElements;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL},
      mappedBy = "creditFacilityTemplate")
  private Set<CftCustomFacilityInfo> cftCustomFacilityInfos;
}
