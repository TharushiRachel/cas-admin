package lk.sampath.casadminportalms.enums;

public enum ErrorEnums {
  SUCCESS_CODE("Success", "200", true),
  FAILED_CODE("Failed", "500", false);
  private String label;
  private String code;

  private Boolean status;

  ErrorEnums(String label, String code, Boolean status) {
    this.label = label;
    this.code = code;
    this.status = status;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }
}
