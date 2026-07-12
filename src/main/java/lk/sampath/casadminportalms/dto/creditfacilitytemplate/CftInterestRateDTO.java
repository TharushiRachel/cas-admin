package lk.sampath.casadminportalms.dto.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftInterestRate;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftInterestRateTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.InterestRatingSubCategory;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class CftInterestRateDTO implements Serializable {
    private Integer cftInterestRateID = 0;

    private Integer creditFacilityTemplateID;

    private String rateName;

    private String rateCode;

    private Double value;

    private AppsConstants.YesNo isDefault;

    private AppsConstants.Status status;

    private MasterDataApproveStatus approveStatus;

    private Date approvedDate;

    private String approvedBy;

    private InterestRatingSubCategory interestRatingSubCategory;

    private AppsConstants.YesNo isEditable;

    private String recordStatus;

    public CftInterestRateDTO() {
    }

    public CftInterestRateDTO(CftInterestRate cftInterestRate) {

        this.cftInterestRateID = cftInterestRate.getCftInterestRateID();
        this.creditFacilityTemplateID = cftInterestRate.getCreditFacilityTemplate().getCreditFacilityTemplateID();
        this.rateName = cftInterestRate.getRateName();
        this.rateCode = cftInterestRate.getRateCode();
        this.value = cftInterestRate.getValue();
        this.isDefault = cftInterestRate.getIsDefault();
        //this.approveStatus = cftInterestRate.getApproveStatus();
        this.status = cftInterestRate.getStatus();

//        if (cftInterestRate.getApprovedDate() != null) {
//            this.approvedDate = cftInterestRate.getApprovedDate();
//        }
//        this.approveStatus = cftInterestRate.getApproveStatus();
//        this.approvedBy = cftInterestRate.getApprovedBy();
        this.interestRatingSubCategory = cftInterestRate.getInterestRatingSubCategory();
        this.isEditable = cftInterestRate.getIsEditable();
        this.recordStatus = cftInterestRate.getRecordStatus();
    }

    public CftInterestRateDTO(CftInterestRateTemp cftInterestRate) {

        this.cftInterestRateID = cftInterestRate.getCftInterestRateID();
        this.creditFacilityTemplateID = cftInterestRate.getCreditFacilityTemplate().getCreditFacilityTemplateID();
        this.rateName = cftInterestRate.getRateName();
        this.rateCode = cftInterestRate.getRateCode();
        this.value = cftInterestRate.getValue();
        this.isDefault = cftInterestRate.getIsDefault();
        this.status = cftInterestRate.getStatus();

//        if (cftInterestRate.getApprovedDate() != null) {
//            this.approvedDate = cftInterestRate.getApprovedDate();
//        }
//        this.approveStatus = cftInterestRate.getApproveStatus();
//        this.approvedBy = cftInterestRate.getApprovedBy();
        this.interestRatingSubCategory = cftInterestRate.getInterestRatingSubCategory();
        this.isEditable = cftInterestRate.getIsEditable();
        this.recordStatus = cftInterestRate.getRecordStatus();
    }

}
