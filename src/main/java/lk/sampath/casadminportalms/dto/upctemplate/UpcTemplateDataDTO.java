package lk.sampath.casadminportalms.dto.upctemplate;

import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateData;
import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateDataTemp;
import lombok.Data;

@Data
public class UpcTemplateDataDTO {

  private Integer upcTemplateDataID;

  private Integer upcTemplateID;

  private Integer upcSectionID;

  private Integer parentSectionID;

  private Integer sectionLevel;

  private Integer displayOrder;

  private String upcSectionName;

  public UpcTemplateDataDTO() {}

  public UpcTemplateDataDTO(UpcTemplateDataTemp upcTemplateData) {
    this.upcTemplateDataID = upcTemplateData.getUpcTemplateDataID();
    this.upcTemplateID = upcTemplateData.getUpcTemplateTemp().getUpcTemplateID();
    this.upcSectionID = upcTemplateData.getUpcSection().getUpcSectionID();
    this.displayOrder = upcTemplateData.getDisplayOrder();
    this.parentSectionID = upcTemplateData.getParentSectionID();
    this.sectionLevel = upcTemplateData.getSectionLevel();
    this.upcSectionName = upcTemplateData.getUpcSection().getUpcSectionName();
  }

  public UpcTemplateDataDTO(UpcTemplateData upcTemplateData) {
    this.upcTemplateDataID = upcTemplateData.getUpcTemplateDataID();
    this.upcTemplateID = upcTemplateData.getUpcTemplate().getUpcTemplateID();
    this.upcSectionID = upcTemplateData.getUpcSection().getUpcSectionID();
    this.displayOrder = upcTemplateData.getDisplayOrder();
    this.parentSectionID = upcTemplateData.getParentSectionID();
    this.sectionLevel = upcTemplateData.getSectionLevel();
    this.upcSectionName = upcTemplateData.getUpcSection().getUpcSectionName();
  }
}
