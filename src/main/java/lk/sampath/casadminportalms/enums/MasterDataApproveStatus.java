package lk.sampath.casadminportalms.enums;

import org.springframework.util.StringUtils;

public enum MasterDataApproveStatus {
  DRAFT("Draft", "DRAFT"),
  PENDING("Pending", "PENDING"),
  APPROVED("Approved", "APPROVED"),
  REJECTED("Rejected", "REJECTED"),
  DELETED("Deleted", "DELETED"),
  PENDING_RMV("Remove Pending", "PENDING_RMV");

  private String label;

  private String value;

  MasterDataApproveStatus(String label, String value) {
    this.label = label;
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public String getValue() {
    return value;
  }

  public static MasterDataApproveStatus getEnum(String value) {
    for (MasterDataApproveStatus clusterStatus : MasterDataApproveStatus.values()) {
      if (clusterStatus.getValue().equalsIgnoreCase(value)) {
        return clusterStatus;
      }
    }
    return null;
  }

  public static MasterDataApproveStatus resolveApproveStatus(String statusStr) {
    MasterDataApproveStatus matchingStatus = null;
    if (!StringUtils.isEmpty(statusStr)) {
      matchingStatus = MasterDataApproveStatus.valueOf(statusStr.trim());
    }

    return matchingStatus;
  }
}
