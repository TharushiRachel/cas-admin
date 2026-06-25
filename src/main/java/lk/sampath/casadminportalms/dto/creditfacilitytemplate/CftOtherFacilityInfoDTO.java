package lk.sampath.casadminportalms.dto.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformation;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformationTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.InputFieldValueType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class CftOtherFacilityInfoDTO implements Serializable {

    private Integer cftOtherFacilityInfoID;

    private Integer creditFacilityTemplateID;

    private String otherFacilityInfoName;

    private String description;

    private String otherFacilityInfoCode;

    private InputFieldValueType otherFacilityInfoFieldType;

    private String defaultValue;

    private Integer displayOrder;

    private AppsConstants.YesNo mandatory;

    private AppsConstants.Status status;

    private String recordStatus;

    private String outputCode;

    private boolean currency;

    private boolean percentage;


    public CftOtherFacilityInfoDTO() {
    }

    public CftOtherFacilityInfoDTO(CftOtherFacilityInformation cftOtherFacilityInformation) {
        this(cftOtherFacilityInformation, null, false, false);
    }

    public CftOtherFacilityInfoDTO(CftOtherFacilityInformation cftOtherFacilityInformation, String outputCode, boolean currency, boolean percentage) {
        this.cftOtherFacilityInfoID = cftOtherFacilityInformation.getCftOtherFacilityInfoID();
        this.creditFacilityTemplateID = cftOtherFacilityInformation.getCreditFacilityTemplate().getCreditFacilityTemplateID();
        this.otherFacilityInfoName = cftOtherFacilityInformation.getOtherFacilityInfoName();
        this.description = cftOtherFacilityInformation.getDescription();
        this.otherFacilityInfoCode = cftOtherFacilityInformation.getOtherFacilityInfoCode();
        this.otherFacilityInfoFieldType = cftOtherFacilityInformation.getOtherFacilityInfoFieldType();
        this.defaultValue = cftOtherFacilityInformation.getDefaultValue();
        this.displayOrder = cftOtherFacilityInformation.getDisplayOrder();
        this.mandatory = cftOtherFacilityInformation.getMandatory();
        this.status = cftOtherFacilityInformation.getStatus();
        this.outputCode = outputCode;
        this.currency = currency;
        this.percentage = percentage;
        this.recordStatus = cftOtherFacilityInformation.getRecordStatus();
    }

    public CftOtherFacilityInfoDTO(CftOtherFacilityInformationTemp cftOtherFacilityInformation) {
        this(cftOtherFacilityInformation, null, false, false);
    }

    public CftOtherFacilityInfoDTO(CftOtherFacilityInformationTemp cftOtherFacilityInformation, String outputCode, boolean currency, boolean percentage) {
        this.cftOtherFacilityInfoID = cftOtherFacilityInformation.getCftOtherFacilityInfoID();
        this.creditFacilityTemplateID = cftOtherFacilityInformation.getCreditFacilityTemplate().getCreditFacilityTemplateID();
        this.otherFacilityInfoName = cftOtherFacilityInformation.getOtherFacilityInfoName();
        this.description = cftOtherFacilityInformation.getDescription();
        this.otherFacilityInfoCode = cftOtherFacilityInformation.getOtherFacilityInfoCode();
        this.otherFacilityInfoFieldType = cftOtherFacilityInformation.getOtherFacilityInfoFieldType();
        this.defaultValue = cftOtherFacilityInformation.getDefaultValue();
        this.displayOrder = cftOtherFacilityInformation.getDisplayOrder();
        this.mandatory = cftOtherFacilityInformation.getMandatory();
        this.status = cftOtherFacilityInformation.getStatus();
        this.outputCode = outputCode;
        this.currency = currency;
        this.percentage = percentage;
        this.recordStatus = cftOtherFacilityInformation.getRecordStatus();
    }

}
