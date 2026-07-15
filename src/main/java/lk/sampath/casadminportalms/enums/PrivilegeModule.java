package lk.sampath.casadminportalms.enums;

public enum PrivilegeModule {
  WEB("Web"),
  MOBILE("Mobile");

  private String description;

  PrivilegeModule(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
