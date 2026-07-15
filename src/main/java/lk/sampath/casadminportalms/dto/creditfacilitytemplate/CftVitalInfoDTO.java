package lk.sampath.casadminportalms.dto.creditfacilitytemplate;

import java.io.Serializable;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftVitalInfo;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftVitalInfoTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CftVitalInfoDTO implements Serializable {
  private Integer cftVitalInfoID;

  private Integer creditFacilityTemplateID;

  private String vitalInfoName;

  private AppsConstants.YesNo mandatory;

  private Integer displayOrder;

  private AppsConstants.Status status;

  private String recordStatus;

  public CftVitalInfoDTO() {}

  public CftVitalInfoDTO(CftVitalInfo cftVitalInfo) {
    this.cftVitalInfoID = cftVitalInfo.getCftVitalInfoID();
    this.creditFacilityTemplateID =
        cftVitalInfo.getCreditFacilityTemplate().getCreditFacilityTemplateID();
    this.vitalInfoName = cftVitalInfo.getVitalInfoName();
    this.mandatory = cftVitalInfo.getMandatory();
    this.displayOrder = cftVitalInfo.getDisplayOrder();
    this.status = cftVitalInfo.getStatus();
    this.recordStatus = cftVitalInfo.getRecordStatus();
  }

  public CftVitalInfoDTO(CftVitalInfoTemp cftVitalInfo) {
    this.cftVitalInfoID = cftVitalInfo.getCftVitalInfoID();
    this.creditFacilityTemplateID =
        cftVitalInfo.getCreditFacilityTemplate().getCreditFacilityTemplateID();
    this.vitalInfoName = cftVitalInfo.getVitalInfoName();
    this.mandatory = cftVitalInfo.getMandatory();
    this.displayOrder = cftVitalInfo.getDisplayOrder();
    this.status = cftVitalInfo.getStatus();
    this.recordStatus = cftVitalInfo.getRecordStatus();
  }
}
