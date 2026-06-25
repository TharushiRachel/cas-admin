package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.entity.role.PrivilegeCategory;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/getSystemPrivileges")
    public ResponseEntity<StandardResponse<List<PrivilegeCategory>>> findAllPrivilegeCategories(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws  ApiRequestException{
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<PrivilegeCategory>>> privilegeCategories = roleService.findAllPrivilegeCategories(pageable);
        return ResponseEntity.ok().body(privilegeCategories.getBody());
    }

    @GetMapping("/roleTemp/{roleID}")
    public ResponseEntity<StandardResponse<Object>> viewTempRoleById(@PathVariable Integer roleID) throws  ApiRequestException {
        ResponseEntity<StandardResponse<Object>> role = roleService.findRolesTempByID(roleID);
        return ResponseEntity.ok().body(role.getBody());
    }

    @GetMapping("/roleTempList")
    public ResponseEntity<StandardResponse<List<RoleDTO>>> viewTempRoleList(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<RoleDTO>>> role = roleService.findAllRolesTempList(pageable);
        return ResponseEntity.ok().body(role.getBody());
    }

    @GetMapping("/searchRoles")
    public ResponseEntity<StandardResponse<List<RoleDTO>>> listRole(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws  ApiRequestException{
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<RoleDTO>>> roles = roleService.findAllApprovedRoles(pageable);
        return ResponseEntity.ok().body(roles.getBody());
    }

    @GetMapping("/{roleID}")
    public ResponseEntity<StandardResponse<Object>> viewRoleById(@PathVariable Integer roleID) throws ApiRequestException {
        ResponseEntity<StandardResponse<Object>> role = roleService.findApprovedRoleById(roleID);
        return ResponseEntity.ok().body(role.getBody());
    }

    @PostMapping
    public ResponseEntity<StandardResponse<Object>> saveRole(@Validated @RequestBody RoleDTO request) throws  ApiRequestException{
        ResponseEntity<StandardResponse<Object>> role = roleService.saveRoleTemp(request);
        return ResponseEntity.ok().body(role.getBody());
    }

    @PostMapping("/updateTempRole/{roleID}")
    public ResponseEntity<StandardResponse<Object>> updateRole(@PathVariable  Integer roleID, @Validated @RequestBody RoleDTO request) throws  ApiRequestException{
        ResponseEntity<StandardResponse<Object>> role = roleService.updateRoleTemp(roleID, request);
        return ResponseEntity.ok().body(role.getBody());
    }

    @PostMapping("/approvedRejectRole")
    public ResponseEntity<StandardResponse<Object>> approveRejectRole(@Validated @RequestBody ApproveRejectRQ approveRejectRQ) throws  ApiRequestException{
        ResponseEntity<StandardResponse<Object>> role = roleService.approveRejectRole(approveRejectRQ);
        return ResponseEntity.ok().body(role.getBody());
    }

    @PostMapping("/updateApprovedRole/{roleID}")
    public ResponseEntity<StandardResponse<Object>> updateApprovedRole(@PathVariable  Integer roleID, @Validated @RequestBody RoleDTO request) throws  ApiRequestException{
        ResponseEntity<StandardResponse<Object>> role = roleService.updateApprovedRole(roleID, request);
        return ResponseEntity.ok().body(role.getBody());
    }

    @PostMapping("/deleteRoleTemp")
    public ResponseEntity<StandardResponse<Void>> deleteRoleTempById(@RequestBody  RoleDTO roleDTO) throws  ApiRequestException{
        ResponseEntity<StandardResponse<Void>> role = roleService.deleteRoleTempById(roleDTO.getRoleID());
        return ResponseEntity.ok().body(role.getBody());
    }
}
