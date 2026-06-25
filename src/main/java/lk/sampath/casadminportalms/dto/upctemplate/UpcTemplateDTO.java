package lk.sampath.casadminportalms.dto.upctemplate;

import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplate;
import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateData;
import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateDataTemp;
import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Data
public class UpcTemplateDTO {

    private Integer upcTemplateID;

    private String templateName;

    private String description;

    private Status status;

    private String upcLabel;

    private String upcLabelBackgroundColor;

    private String upcLabelFontColor;

    private MasterDataApproveStatus approveStatus;

    private Date approvedDate;

    private String approvedBy;

    private Date createdDate;

    private String createdBy;

    private Date lastModifiedDate;

    private String modifiedBy;

    private AppsConstants.YesNo isModified;

    private List<UpcTemplateDataDTO> upcTemplateDataDTOList;

    public UpcTemplateDTO() {
    }

    public UpcTemplateDTO(UpcTemplateTemp upcTemplate) {

        this.upcTemplateID = upcTemplate.getUpcTemplateID();
        this.templateName = upcTemplate.getTemplateName();
        this.description = upcTemplate.getDescription();
        this.approveStatus = upcTemplate.getApproveStatus();
        this.status = upcTemplate.getStatus();
        this.upcLabel = upcTemplate.getUpcLabel();
        this.upcLabelBackgroundColor = upcTemplate.getUpcLabelBackgroundColor();
        this.upcLabelFontColor = upcTemplate.getUpcLabelFontColor();
        if (upcTemplate.getApprovedDate() != null) {
            this.approvedDate = upcTemplate.getApprovedDate();
        }
        this.approvedBy = upcTemplate.getApprovedBy();
        if (upcTemplate.getCreatedDate() != null) {
            this.createdDate = upcTemplate.getCreatedDate();
        }
        this.createdBy = upcTemplate.getCreatedBy();
        if (upcTemplate.getLastModifiedDate() != null) {
            this.lastModifiedDate = upcTemplate.getLastModifiedDate();
        }
        this.modifiedBy = upcTemplate.getModifiedBy();

        if (upcTemplate.getUpcTemplateDataTempList() != null && !upcTemplate.getUpcTemplateDataTempList().isEmpty()) {
            this.upcTemplateDataDTOList = new ArrayList<>();
            for (UpcTemplateDataTemp templateData : upcTemplate.getUpcTemplateDataTempList()) {
                this.upcTemplateDataDTOList.add(new UpcTemplateDataDTO(templateData));
            }
            this.upcTemplateDataDTOList.sort(Comparator.comparingInt(UpcTemplateDataDTO::getDisplayOrder));
        } else {
            this.upcTemplateDataDTOList = new ArrayList<>();
        }
    }

    public UpcTemplateDTO(UpcTemplate upcTemplate) {

        this.upcTemplateID = upcTemplate.getUpcTemplateID();
        this.templateName = upcTemplate.getTemplateName();
        this.description = upcTemplate.getDescription();
        this.approveStatus = upcTemplate.getApproveStatus();
        this.status = upcTemplate.getStatus();
        this.upcLabel = upcTemplate.getUpcLabel();
        this.upcLabelBackgroundColor = upcTemplate.getUpcLabelBackgroundColor();
        this.upcLabelFontColor = upcTemplate.getUpcLabelFontColor();
        if (upcTemplate.getApprovedDate() != null) {
            this.approvedDate = upcTemplate.getApprovedDate();
        }
        this.approvedBy = upcTemplate.getApprovedBy();
        if (upcTemplate.getCreatedDate() != null) {
            this.createdDate = upcTemplate.getCreatedDate();
        }
        this.createdBy = upcTemplate.getCreatedBy();
        if (upcTemplate.getLastModifiedDate() != null) {
            this.lastModifiedDate = upcTemplate.getLastModifiedDate();
        }
        this.modifiedBy = upcTemplate.getModifiedBy();

        if (upcTemplate.getUpcTemplateDataList() != null && !upcTemplate.getUpcTemplateDataList().isEmpty()) {
            this.upcTemplateDataDTOList = new ArrayList<>();
            for (UpcTemplateData templateData : upcTemplate.getUpcTemplateDataList()) {
                this.upcTemplateDataDTOList.add(new UpcTemplateDataDTO(templateData));
            }
            this.upcTemplateDataDTOList.sort(Comparator.comparingInt(UpcTemplateDataDTO::getDisplayOrder));
        } else {
            this.upcTemplateDataDTOList = new ArrayList<>();
        }
    }
}
