package lk.sampath.casadminportalms.dto.role;

import lk.sampath.casadminportalms.entity.role.Privilege;
import lk.sampath.casadminportalms.entity.role.Role;
import lk.sampath.casadminportalms.entity.role.RoleTemp;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class RoleDTO {

    private Integer roleID;

    private String roleName;

    private String upmPrivilegeCode;

    private Status status;

    private List<Integer> privileges;

    private List<Integer> addedPrivileges;

    private List<Integer> deletedPrivileges;

    private MasterDataApproveStatus approveStatus;

    private Date approvedDate;

    private String approvedBy;

    private Date createdDate;

    private String createdBy;

    private Date lastModifiedDate;

    private String modifiedBy;

    public RoleDTO() {
    }

    public RoleDTO(Role role) {
        this.roleID = role.getRoleID();
        this.roleName = role.getRoleName();
        this.upmPrivilegeCode = role.getUpmPrivilegeCode();
        this.status = role.getStatus();
        this.privileges = role.getPrivileges().stream()
                .map(Privilege::getPrivilegeID)
                .toList();
        this.approveStatus = role.getApproveStatus();
        this.createdDate = role.getCreatedDate();
        this.createdBy = role.getCreatedBy();
        this.modifiedBy = role.getModifiedBy();
        this.lastModifiedDate = role.getLastModifiedDate();
        this.approvedBy = role.getApprovedBy();
        this.approvedDate = role.getApprovedDate();
    }

    public RoleDTO(RoleTemp roleTemp) {
        this.roleID = roleTemp.getRoleID();
        this.roleName = roleTemp.getRoleName();
        this.upmPrivilegeCode = roleTemp.getUpmPrivilegeCode();
        this.status = roleTemp.getStatus();
        this.privileges = roleTemp.getPrivileges().stream()
                .map(Privilege::getPrivilegeID)
                .toList();
        this.approveStatus = roleTemp.getApproveStatus();
        this.createdDate = roleTemp.getCreatedDate();
        this.createdBy = roleTemp.getCreatedBy();
        this.modifiedBy = roleTemp.getModifiedBy();
        this.lastModifiedDate = roleTemp.getLastModifiedDate();
        this.approvedBy = roleTemp.getApprovedBy();
        this.approvedDate = roleTemp.getApprovedDate();
    }
}
