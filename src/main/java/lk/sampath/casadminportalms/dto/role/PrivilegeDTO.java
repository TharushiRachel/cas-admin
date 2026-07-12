package lk.sampath.casadminportalms.dto.role;

import lk.sampath.casadminportalms.entity.role.Privilege;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

@Data
public class PrivilegeDTO {

    private Integer privilegeID;

    private String privilegeName;

    private String code;

    private String description;

    private Integer privilegeCategoryID;

    private Status status;

    public PrivilegeDTO() {
    }
}
