package lk.sampath.casadminportalms.dto.common;

import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Data;
import java.io.Serializable;

@Data
public class ApproveRejectRQ implements Serializable {

    private Integer approveRejectDataID;

    private MasterDataApproveStatus approveStatus;
}
