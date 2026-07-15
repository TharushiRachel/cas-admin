package lk.sampath.casadminportalms.dto.creditfacility;

import java.util.Date;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityTypeTemp;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

@Data
public class CreditFacilityTypeDTO {

  private Integer creditFacilityTypeID;

  private String facilityTypeName;

  private String description;

  private Status status;

  private MasterDataApproveStatus approveStatus;

  private Date approvedDate;

  private String approvedBy;

  private Date createdDate;

  private String createdBy;

  private Date lastModifiedDate;

  private String modifiedBy;

  public CreditFacilityTypeDTO() {}

  public CreditFacilityTypeDTO(CreditFacilityType creditFacilityType) {
    this.creditFacilityTypeID = creditFacilityType.getCreditFacilityTypeID();
    this.facilityTypeName = creditFacilityType.getFacilityTypeName();
    this.description = creditFacilityType.getDescription();
    this.status = creditFacilityType.getStatus();
    this.approveStatus = creditFacilityType.getApproveStatus();
    this.approvedDate = creditFacilityType.getApprovedDate();
    this.approvedBy = creditFacilityType.getApprovedBy();
    this.createdDate = creditFacilityType.getCreatedDate();
    this.createdBy = creditFacilityType.getCreatedBy();
    this.lastModifiedDate = creditFacilityType.getLastModifiedDate();
    this.modifiedBy = creditFacilityType.getModifiedBy();
  }

  public CreditFacilityTypeDTO(CreditFacilityTypeTemp creditFacilityType) {
    this.creditFacilityTypeID = creditFacilityType.getCreditFacilityTypeID();
    this.facilityTypeName = creditFacilityType.getFacilityTypeName();
    this.description = creditFacilityType.getDescription();
    this.status = creditFacilityType.getStatus();
    this.approveStatus = creditFacilityType.getApproveStatus();
    this.approvedDate = creditFacilityType.getApprovedDate();
    this.approvedBy = creditFacilityType.getApprovedBy();
    this.createdDate = creditFacilityType.getCreatedDate();
    this.createdBy = creditFacilityType.getCreatedBy();
    this.lastModifiedDate = creditFacilityType.getLastModifiedDate();
    this.modifiedBy = creditFacilityType.getModifiedBy();
  }
}
