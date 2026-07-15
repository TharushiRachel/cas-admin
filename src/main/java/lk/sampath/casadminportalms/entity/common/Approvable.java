package lk.sampath.casadminportalms.entity.common;

import java.io.Serializable;
import java.util.Date;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;

public interface Approvable extends Serializable {

  public MasterDataApproveStatus getApproveStatus();

  public void setApproveStatus(MasterDataApproveStatus approveStatus);

  public Date getApprovedDate();

  public void setApprovedDate(Date approvedDate);

  public String getApprovedBy();

  public void setApprovedBy(String approvedBy);

  public Integer getParentRecordID();

  public void setParentRecordID(Integer parentRecordID);
}
