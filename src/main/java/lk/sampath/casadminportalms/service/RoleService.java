package lk.sampath.casadminportalms.service;


import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.entity.role.Privilege;
import lk.sampath.casadminportalms.entity.role.PrivilegeCategory;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {

    ResponseEntity<StandardResponse<List<PrivilegeCategory>>> findAllPrivilegeCategories(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> findRolesTempByID(Integer roleID) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<RoleDTO>>> findAllRolesTempList(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<RoleDTO>>> findAllApprovedRoles(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> findApprovedRoleById(int roleID) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> saveRoleTemp(RoleDTO RoleDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> updateRoleTemp(Integer roleID, RoleDTO roleDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> approveRejectRole(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> updateApprovedRole(Integer roleID, RoleDTO roleDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<Privilege>>> findAllPrivileges(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteRoleTempById(int roleID) throws ApiRequestException;
}
