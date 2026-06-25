package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.InterestRatingSubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "T_CFT_INTEREST_RATE_AUD")
public class CftInterestRateAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CFT_INTEREST_RATE_AUD")
    @SequenceGenerator(name = "SEQ_T_CFT_INTEREST_RATE_AUD", sequenceName = "SEQ_T_CFT_INTEREST_RATE_AUD", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CFT_INTEREST_RATE_ID")
    private Integer cftInterestRateID;

    @Column(name = "CREDIT_FACILITY_TEMPLATE_ID")
    private Integer creditFacilityTemplate;

    @Column(name = "RATE_NAME")
    private String rateName;

    @Column(name = "RATE_CODE")
    private String rateCode;

    @Column(name = "VALUE")
    private Double value;

    @Enumerated(EnumType.STRING)
    @Column(name = "IS_DEFAULT")
    private AppsConstants.YesNo isDefault;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppsConstants.Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "INTEREST_RATING_SUB_CATEGORY")
    private InterestRatingSubCategory interestRatingSubCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "IS_EDITABLE")
    private AppsConstants.YesNo isEditable;

    @Column(name = "RECORD_STATUS")
    private String recordStatus;

}
