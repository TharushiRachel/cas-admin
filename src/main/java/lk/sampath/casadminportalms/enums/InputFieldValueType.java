package lk.sampath.casadminportalms.enums;

public enum InputFieldValueType {
    TEXT("Text"),
    NUMBER("Number"),
    CURRENCY("CURRENCY"),
    DATE("Date"),
    PERCENTAGE("Percentage");

    private String description;

    InputFieldValueType(String description) {
        this.description = description;
    }

    public static InputFieldValueType resolve(String status) {
        InputFieldValueType result = null;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(status)) {
            for (InputFieldValueType inputFieldValueType : InputFieldValueType.values()) {
                if (inputFieldValueType.name().equals(status.toUpperCase())) {
                    result = inputFieldValueType;
                    break;
                }
            }
        }
        return result;
    }

    public String getDescription() {
        return description;
    }
}
