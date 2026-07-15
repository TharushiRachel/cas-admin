package lk.sampath.casadminportalms.dto.common;

import java.io.Serializable;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Data;

@Data
public class ApproveRejectRQ implements Serializable {

  private Integer approveRejectDataID;
  private MasterDataApproveStatus approveStatus;
}
