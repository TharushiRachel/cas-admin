package lk.sampath.casadminportalms.dto.supportingdoc;

import java.util.Date;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

@Data
public class SupportingDocDTO {

  private Integer supportingDocID;

  private String documentName;

  private String description;

  private String supportDocumentType;

  private Status status;

  private MasterDataApproveStatus approveStatus;

  private Date approvedDate;

  private String approvedBy;

  private Date createdDate;

  private String createdBy;

  private Date lastModifiedDate;

  private String modifiedBy;
}
