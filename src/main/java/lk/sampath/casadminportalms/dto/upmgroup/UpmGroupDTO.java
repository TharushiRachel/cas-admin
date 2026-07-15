package lk.sampath.casadminportalms.dto.upmgroup;

import java.io.Serializable;
import java.util.Date;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

@Data
public class UpmGroupDTO implements Serializable {

  private Integer upmGroupID;

  private String groupCode;

  private String referenceName;

  private Integer workFlowLevel;

  private Status status;

  private MasterDataApproveStatus approveStatus;

  private Date approvedDate;

  private String approvedBy;

  private Date createdDate;

  private String createdBy;

  private Date lastModifiedDate;

  private String modifiedBy;
}
