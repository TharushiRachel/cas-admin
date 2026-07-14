package lk.sampath.casadminportalms.enums;

import org.springframework.util.StringUtils;

public enum DaTableType {

    COMMITTEE("Committee", "C"),
    INDIVIDUAL("Individual", "I");

    private String label;
    private String value;

    DaTableType(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static DaTableType getEnum(String value) {
        for (DaTableType clusterStatus : DaTableType.values()) {
            if (clusterStatus.getValue().equalsIgnoreCase(value)) {
                return clusterStatus;
            }
        }
        return null;
    }

    public static DaTableType resolveStatus(String statusStr) {
        DaTableType matchingStatus = null;
        if (!StringUtils.isEmpty(statusStr)) {
            matchingStatus = DaTableType.valueOf(statusStr.trim());
        }

        return matchingStatus;
    }
}
