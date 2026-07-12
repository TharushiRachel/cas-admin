package lk.sampath.casadminportalms.dto.role;

import lombok.Data;

@Data
public class UpmRolePrivilegeDTO {

    private String code;

    private String allowedSolIds;

    private String restrictedSolIds;
}
