package lk.sampath.casadminportalms.dto.creditfacilitytemplate;

import lk.sampath.casadminportalms.dto.facilitypaper.Formula;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class CreditFacilityTemplateDTO implements Serializable {

    private Integer creditFacilityTemplateID;

    private Integer creditFacilityTypeID;

    private String creditFacilityName;

    private String facilityTypeName;

    private String description;

    private BigDecimal maxFacilityAmount;

    private BigDecimal minFacilityAmount;

    private AppsConstants.YesNo showPurpose;

    private AppsConstants.YesNo showRepayment;

    private AppsConstants.YesNo showCondition;

    private AppsConstants.YesNo showRemark;

    private AppsConstants.YesNo showCalculator;

    private AppsConstants.YesNo showRentalData;

    private AppsConstants.Status status;

    private MasterDataApproveStatus approveStatus;

    private Date approvedDate;

    private String approvedBy;

    private Date createdDate;

    private String createdBy;

    private Date modifiedDate;

    private String modifiedBy;

    private String newFacilityEmail;

    private String existingFacilityEmail;

    private List<CftVitalInfoDTO> cftVitalInfoDTOList;

    private List<CftInterestRateDTO> cftInterestRateDTOList;

    private List<CftSupportingDocDTO> cftSupportingDocDTOList;

    private List<CftOtherFacilityInfoDTO> cftOtherFacilityInfoDTOList;

    private List<CftCustomFacilityInfoDTO> cftCustomFacilityInfoDTOList;

    private AppsConstants.YesNo isCftVitalInfoDTOListChange;
    private AppsConstants.YesNo isCftInterestRateDTOListChange;
    private AppsConstants.YesNo isCftSupportingDocDTOListChange;
    private AppsConstants.YesNo isCftOtherFacilityInfoDTOListChange;
    private AppsConstants.YesNo isCftCustomFacilityInfoDTOListChange;

    public CreditFacilityTemplateDTO() {
    }

    public CreditFacilityTemplateDTO(CreditFacilityTemplate creditFacilityTemplate) {
        this(creditFacilityTemplate, null);
    }

    public CreditFacilityTemplateDTO(CreditFacilityTemplate creditFacilityTemplate, List<Formula> formulaList) {
        this.creditFacilityTemplateID = creditFacilityTemplate.getCreditFacilityTemplateID();
        if (creditFacilityTemplate.getCreditFacilityType() != null) {
            this.creditFacilityTypeID = creditFacilityTemplate.getCreditFacilityType().getCreditFacilityTypeID();
            this.facilityTypeName = creditFacilityTemplate.getCreditFacilityType().getFacilityTypeName();
        }
        this.creditFacilityName = creditFacilityTemplate.getCreditFacilityName();
        this.description = creditFacilityTemplate.getDescription();
        this.maxFacilityAmount = creditFacilityTemplate.getMaxFacilityAmount();
        this.minFacilityAmount = creditFacilityTemplate.getMinFacilityAmount();
        this.showCondition = creditFacilityTemplate.getShowCondition();
        this.showPurpose = creditFacilityTemplate.getShowPurpose();
        this.showRemark = creditFacilityTemplate.getShowRemark();
        this.showRepayment = creditFacilityTemplate.getShowRepayment();
        this.showCalculator = creditFacilityTemplate.getShowCalculator();
        this.showRentalData = creditFacilityTemplate.getShowRentalData();
        this.status = creditFacilityTemplate.getStatus();
        this.newFacilityEmail = creditFacilityTemplate.getNewFacilityEmail();
        this.existingFacilityEmail = creditFacilityTemplate.getExistingFacilityEmail();

        if (creditFacilityTemplate.getApprovedDate() != null) {
            this.approvedDate = creditFacilityTemplate.getApprovedDate();
        }
        this.approvedBy = creditFacilityTemplate.getApprovedBy();
        this.approveStatus = creditFacilityTemplate.getApproveStatus();

        if (creditFacilityTemplate.getCreatedDate() != null) {
            this.createdDate = creditFacilityTemplate.getCreatedDate();
        }
        this.createdBy = creditFacilityTemplate.getCreatedBy();
        if (creditFacilityTemplate.getLastModifiedDate() != null) {
            this.modifiedDate = creditFacilityTemplate.getLastModifiedDate();
        }
        this.modifiedBy = creditFacilityTemplate.getModifiedBy();

        for (CftVitalInfo cftVitalInfo : creditFacilityTemplate.getCftVitalInfos()) {
            if (cftVitalInfo.getStatus() == AppsConstants.Status.ACT) {
                this.getCftVitalInfoDTOList().add(new CftVitalInfoDTO(cftVitalInfo));
            }
        }

        for(CftCustomFacilityInfo cftCustomFacilityInfo: creditFacilityTemplate.getCftCustomFacilityInfos()){
            if(cftCustomFacilityInfo.getStatus() == AppsConstants.Status.ACT){
                this.getCftCustomFacilityInfoDTOList().add(new CftCustomFacilityInfoDTO(cftCustomFacilityInfo));
            }
            this.getCftCustomFacilityInfoDTOList();
        }



        for (CftInterestRate cftInterestRate : creditFacilityTemplate.getCftInterestRates()) {
            if (cftInterestRate.getStatus() == AppsConstants.Status.ACT) {
                this.getCftInterestRateDTOList().add(new CftInterestRateDTO(cftInterestRate));
            }
        }

        for (CftSupportingDoc cftSupportingDoc : creditFacilityTemplate.getCftSupportingDocs()) {
            if (cftSupportingDoc.getStatus() == AppsConstants.Status.ACT) {
                this.getCftSupportingDocDTOList().add(new CftSupportingDocDTO(cftSupportingDoc));
            }
        }

        if (formulaList == null) {
            for (CftOtherFacilityInformation otherFacilityInfo : creditFacilityTemplate.getCftOtherFacilityInformations()) {
                if (otherFacilityInfo.getStatus() == AppsConstants.Status.ACT) {
                    this.getCftOtherFacilityInfoDTOList().add(new CftOtherFacilityInfoDTO(otherFacilityInfo));
                }
            }
        } else {
            for (CftOtherFacilityInformation otherFacilityInfo : creditFacilityTemplate.getCftOtherFacilityInformations()) {
                List<Formula> filteredFormula = formulaList.stream().filter(f -> f.getCode().equals(otherFacilityInfo.getOtherFacilityInfoCode())).collect(Collectors.toList());
                String outputCode = filteredFormula.size() > 0 ? filteredFormula.get(0).getOutputCode() : null;
                boolean currency = filteredFormula.size() > 0 ? filteredFormula.get(0).isCurrency() : false;
                boolean percentage = filteredFormula.size() > 0 ? filteredFormula.get(0).isPercentage() : false;
                if (otherFacilityInfo.getStatus() == AppsConstants.Status.ACT) {
                    this.getCftOtherFacilityInfoDTOList().add(new CftOtherFacilityInfoDTO(otherFacilityInfo, outputCode, currency, percentage));
                }
            }
        }


    }

    public CreditFacilityTemplateDTO(CreditFacilityTemplateTemp creditFacilityTemplate) {
        this(creditFacilityTemplate, null);
    }

    public CreditFacilityTemplateDTO(CreditFacilityTemplateTemp creditFacilityTemplate, List<Formula> formulaList) {
        this.creditFacilityTemplateID = creditFacilityTemplate.getCreditFacilityTemplateID();
        if (creditFacilityTemplate.getCreditFacilityType() != null) {
            this.creditFacilityTypeID = creditFacilityTemplate.getCreditFacilityType().getCreditFacilityTypeID();
            this.facilityTypeName = creditFacilityTemplate.getCreditFacilityName();
        }
        this.creditFacilityName = creditFacilityTemplate.getCreditFacilityName();
        this.description = creditFacilityTemplate.getDescription();
        this.maxFacilityAmount = creditFacilityTemplate.getMaxFacilityAmount();
        this.minFacilityAmount = creditFacilityTemplate.getMinFacilityAmount();
        this.showCondition = creditFacilityTemplate.getShowCondition();
        this.showPurpose = creditFacilityTemplate.getShowPurpose();
        this.showRemark = creditFacilityTemplate.getShowRemark();
        this.showRepayment = creditFacilityTemplate.getShowRepayment();
        this.showCalculator = creditFacilityTemplate.getShowCalculator();
        this.showRentalData = creditFacilityTemplate.getShowRentalData();
        this.status = creditFacilityTemplate.getStatus();
        this.newFacilityEmail = creditFacilityTemplate.getNewFacilityEmail();
        this.existingFacilityEmail = creditFacilityTemplate.getExistingFacilityEmail();

        if (creditFacilityTemplate.getApprovedDate() != null) {
            this.approvedDate = creditFacilityTemplate.getApprovedDate();
        }
        this.approvedBy = creditFacilityTemplate.getApprovedBy();
        this.approveStatus = creditFacilityTemplate.getApproveStatus();

        if (creditFacilityTemplate.getCreatedDate() != null) {
            this.createdDate = creditFacilityTemplate.getCreatedDate();
        }
        this.createdBy = creditFacilityTemplate.getCreatedBy();
        if (creditFacilityTemplate.getLastModifiedDate() != null) {
            this.modifiedDate = creditFacilityTemplate.getLastModifiedDate();
        }
        this.modifiedBy = creditFacilityTemplate.getModifiedBy();

        if(creditFacilityTemplate.getCftVitalInfos() != null && !creditFacilityTemplate.getCftVitalInfos().isEmpty()){
            for (CftVitalInfoTemp cftVitalInfo : creditFacilityTemplate.getCftVitalInfos()) {
                if (cftVitalInfo.getStatus() == AppsConstants.Status.ACT) {
                    this.getCftVitalInfoDTOList().add(new CftVitalInfoDTO(cftVitalInfo));
                }
            }
        }

        if(creditFacilityTemplate.getCftCustomFacilityInfos() != null && !creditFacilityTemplate.getCftCustomFacilityInfos().isEmpty()){
            for(CftCustomFacilityInfoTemp cftCustomFacilityInfo: creditFacilityTemplate.getCftCustomFacilityInfos()){
                if(cftCustomFacilityInfo.getStatus() == AppsConstants.Status.ACT){
                    this.getCftCustomFacilityInfoDTOList().add(new CftCustomFacilityInfoDTO(cftCustomFacilityInfo));
                }
            }
        }

        if(creditFacilityTemplate.getCftInterestRates() != null && !creditFacilityTemplate.getCftInterestRates().isEmpty()){
            for (CftInterestRateTemp cftInterestRate : creditFacilityTemplate.getCftInterestRates()) {
                if (cftInterestRate.getStatus() == AppsConstants.Status.ACT) {
                    this.getCftInterestRateDTOList().add(new CftInterestRateDTO(cftInterestRate));
                }
            }
        }

        if(creditFacilityTemplate.getCftSupportingDocs() != null && !creditFacilityTemplate.getCftSupportingDocs().isEmpty()){
            for (CftSupportingDocTemp cftSupportingDoc : creditFacilityTemplate.getCftSupportingDocs()) {
                if (cftSupportingDoc.getStatus() == AppsConstants.Status.ACT) {
                    this.getCftSupportingDocDTOList().add(new CftSupportingDocDTO(cftSupportingDoc));
                }
            }
        }

        if (formulaList == null) {
            if(creditFacilityTemplate.getCftOtherFacilityInformations() != null && !creditFacilityTemplate.getCftOtherFacilityInformations().isEmpty()){
                for (CftOtherFacilityInformationTemp otherFacilityInfo : creditFacilityTemplate.getCftOtherFacilityInformations()) {
                    if (otherFacilityInfo.getStatus() == AppsConstants.Status.ACT) {
                        this.getCftOtherFacilityInfoDTOList().add(new CftOtherFacilityInfoDTO(otherFacilityInfo));
                    }
                }
            }

        } else {
            for (CftOtherFacilityInformationTemp otherFacilityInfo : creditFacilityTemplate.getCftOtherFacilityInformations()) {
                List<Formula> filteredFormula = formulaList.stream().filter(f -> f.getCode().equals(otherFacilityInfo.getOtherFacilityInfoCode())).collect(Collectors.toList());
                String outputCode = filteredFormula.size() > 0 ? filteredFormula.get(0).getOutputCode() : null;
                boolean currency = filteredFormula.size() > 0 ? filteredFormula.get(0).isCurrency() : false;
                boolean percentage = filteredFormula.size() > 0 ? filteredFormula.get(0).isPercentage() : false;
                if (otherFacilityInfo.getStatus() == AppsConstants.Status.ACT) {
                    this.getCftOtherFacilityInfoDTOList().add(new CftOtherFacilityInfoDTO(otherFacilityInfo, outputCode, currency, percentage));
                }
            }
        }
    }

    public List<CftVitalInfoDTO> getCftVitalInfoDTOList() {
        if (cftVitalInfoDTOList == null) {
            cftVitalInfoDTOList = new ArrayList<>();
        }
        return cftVitalInfoDTOList;
    }

    public List<CftCustomFacilityInfoDTO> getCftCustomFacilityInfoDTOList() {
        if(cftCustomFacilityInfoDTOList == null){
            cftCustomFacilityInfoDTOList = new ArrayList<>();
        }
        return cftCustomFacilityInfoDTOList;
    }

    public List<CftInterestRateDTO> getCftInterestRateDTOList() {
        if (cftInterestRateDTOList == null) {
            cftInterestRateDTOList = new ArrayList<>();
        }
        return cftInterestRateDTOList;
    }

    public List<CftSupportingDocDTO> getCftSupportingDocDTOList() {
        if (cftSupportingDocDTOList == null) {
            cftSupportingDocDTOList = new ArrayList<>();
        }
        return cftSupportingDocDTOList;
    }

    public List<CftOtherFacilityInfoDTO> getCftOtherFacilityInfoDTOList() {
        if (this.cftOtherFacilityInfoDTOList == null) {
            cftOtherFacilityInfoDTOList = new ArrayList<>();
        }
        return cftOtherFacilityInfoDTOList;
    }
}
