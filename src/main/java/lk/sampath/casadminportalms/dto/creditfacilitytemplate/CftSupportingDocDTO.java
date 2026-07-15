package lk.sampath.casadminportalms.dto.creditfacilitytemplate;

import java.io.Serializable;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftSupportingDoc;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftSupportingDocTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CftSupportingDocDTO implements Serializable {

  private Integer cftSupportingDocID;

  private Integer creditFacilityTemplateID;

  private Integer supportingDocID;

  private String documentName;

  private AppsConstants.YesNo mandatory;

  private MasterDataApproveStatus approveStatus;

  private AppsConstants.Status status;

  private String recordStatus;

  public CftSupportingDocDTO() {}

  public CftSupportingDocDTO(CftSupportingDoc cftSupportingDoc) {

    this.cftSupportingDocID = cftSupportingDoc.getCftSupportingDocID();
    this.creditFacilityTemplateID =
        cftSupportingDoc.getCreditFacilityTemplate().getCreditFacilityTemplateID();
    this.supportingDocID = cftSupportingDoc.getSupportingDoc().getSupportingDocID();
    this.documentName = cftSupportingDoc.getSupportingDoc().getDocumentName();
    this.mandatory = cftSupportingDoc.getMandatory();
    // this.approveStatus = cftSupportingDoc.getApproveStatus();
    this.status = cftSupportingDoc.getStatus();
    this.recordStatus = cftSupportingDoc.getRecordStatus();
  }

  public CftSupportingDocDTO(CftSupportingDocTemp cftSupportingDoc) {

    this.cftSupportingDocID = cftSupportingDoc.getCftSupportingDocID();
    this.creditFacilityTemplateID =
        cftSupportingDoc.getCreditFacilityTemplate().getCreditFacilityTemplateID();
    this.supportingDocID = cftSupportingDoc.getSupportingDoc().getSupportingDocID();
    this.documentName = cftSupportingDoc.getSupportingDoc().getDocumentName();
    this.mandatory = cftSupportingDoc.getMandatory();
    // this.approveStatus = cftSupportingDoc.getApproveStatus();
    this.status = cftSupportingDoc.getStatus();
    this.recordStatus = cftSupportingDoc.getRecordStatus();
  }
}
