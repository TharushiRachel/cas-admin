package lk.sampath.casadminportalms.dto.role;

import lombok.Data;

@Data
public class RolePrivilegeDTO {

  private Integer roleId;
  private String roleName;
  private String privilegeName;
  private String privilegeCode;
  private String privilegeCategory;

  public RolePrivilegeDTO(
      Integer roleId,
      String roleName,
      String privilegeName,
      String privilegeCode,
      String privilegeCategory) {
    this.roleId = roleId;
    this.roleName = roleName;
    this.privilegeName = privilegeName;
    this.privilegeCode = privilegeCode;
    this.privilegeCategory = privilegeCategory;
  }
}
