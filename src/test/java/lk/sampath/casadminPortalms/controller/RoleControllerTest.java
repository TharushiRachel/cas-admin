package lk.sampath.casadminPortalms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.RoleController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.entity.role.PrivilegeCategory;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RoleController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class RoleControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private RoleServiceImpl roleService;

  @Test
  void testFindAllPrivilegeCategories() throws Exception {

    List<PrivilegeCategory> mockPrivilegeCategories = Arrays.asList(new PrivilegeCategory());
    StandardResponse<List<PrivilegeCategory>> response =
        new StandardResponse<>(true, "Success", mockPrivilegeCategories);
    when(roleService.findAllPrivilegeCategories()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/role/getSystemPrivileges"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void testViewTempRoleById() throws Exception {

    Integer mockRoleID = 1;
    StandardResponse<Object> response = new StandardResponse<>(true, "Success", new RoleDTO());
    when(roleService.findRolesTempByID(mockRoleID)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/role/roleTemp/{roleID}", mockRoleID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Success"));
  }

  @Test
  void testViewTempRoleList() throws Exception {

    List<RoleDTO> mockRoleList = Arrays.asList(new RoleDTO());
    StandardResponse<List<RoleDTO>> response =
        new StandardResponse<>(true, "Success", mockRoleList);
    when(roleService.findAllRolesTempList()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/role/roleTempList"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Success"));
  }

  @Test
  void testViewRoleById() throws Exception {

    Integer mockRoleID = 1;
    StandardResponse<Object> response = new StandardResponse<>(true, "Success", new RoleDTO());
    when(roleService.findApprovedRoleById(mockRoleID)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/role/{roleID}", mockRoleID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Success"));
  }

  @Test
  void testViewRoleList() throws Exception {

    List<RoleDTO> mockRoleList = Arrays.asList(new RoleDTO());
    StandardResponse<List<RoleDTO>> response =
        new StandardResponse<>(true, "Success", mockRoleList);
    when(roleService.findAllApprovedRoles()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/role/searchRoles"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Success"));
  }

  @Test
  void testSaveRole_Success() throws Exception {
    // Arrange
    RoleDTO request = new RoleDTO();
    request.setRoleName("Admin");
    request.setCreatedBy("TestUser");

    RoleDTO savedRole = new RoleDTO();
    savedRole.setRoleID(1);
    savedRole.setRoleName("Admin");
    savedRole.setCreatedBy("TestUser");

    StandardResponse<Object> response =
        new StandardResponse<>(true, "Role saved successfully", savedRole);
    when(roleService.saveRoleTemp(any(RoleDTO.class))).thenReturn(ResponseEntity.ok(response));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Role saved successfully"));
  }

  @Test
  void testSaveRole_InvalidInput() throws Exception {
    // Arrange
    RoleDTO request = new RoleDTO(); // Invalid because required fields are missing

    when(roleService.saveRoleTemp(any(RoleDTO.class)))
        .thenThrow(new ApiRequestException("Role name cannot be null"));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist()) // No success key in error response
        .andExpect(jsonPath("$.message").value("Role name cannot be null"));
  }

  @Test
  void testUpdateRole_Success() throws Exception {
    // Arrange
    Integer roleID = 1;
    RoleDTO request = new RoleDTO();
    request.setRoleName("UpdatedRole");
    request.setModifiedBy("TestUser");

    RoleDTO updatedRole = new RoleDTO();
    updatedRole.setRoleID(1);
    updatedRole.setRoleName("UpdatedRole");
    updatedRole.setModifiedBy("TestUser");

    StandardResponse<Object> response =
        new StandardResponse<>(true, "Role updated successfully", updatedRole);
    when(roleService.updateRoleTemp(eq(roleID), any(RoleDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/updateTempRole/{roleID}", roleID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Role updated successfully"));
  }

  @Test
  void testUpdateRole_NotFound() throws Exception {
    // Arrange
    Integer roleID = 99; // Role ID that doesn't exist
    RoleDTO request = new RoleDTO();
    request.setRoleName("NonExistentRole");
    request.setModifiedBy("TestUser");

    when(roleService.updateRoleTemp(eq(roleID), any(RoleDTO.class)))
        .thenThrow(new ApiRequestException("Role with ID 99 does not exist"));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/updateTempRole/{roleID}", roleID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist()) // No success key in error response
        .andExpect(jsonPath("$.message").value("Role with ID 99 does not exist"));
  }

  @Test
  void testUpdateRole_InvalidInput() throws Exception {

    Integer roleID = 1;
    RoleDTO request = new RoleDTO();

    when(roleService.updateRoleTemp(eq(roleID), any(RoleDTO.class)))
        .thenThrow(new ApiRequestException("Role name cannot be null or empty"));

    mockMvc
        .perform(
            post("/api/role/updateTempRole/{roleID}", roleID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist())
        .andExpect(jsonPath("$.message").value("Role name cannot be null or empty"));
  }

  @Test
  void testApproveRejectRole_Success() throws Exception {
    // Arrange
    ApproveRejectRQ request = new ApproveRejectRQ();
    request.setApproveRejectDataID(1);
    request.setApproveStatus(MasterDataApproveStatus.APPROVED);

    StandardResponse<Object> response =
        new StandardResponse<>(true, "Role approved successfully", null);
    when(roleService.approveRejectRole(any(ApproveRejectRQ.class)))
        .thenReturn(ResponseEntity.ok(response));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/approvedRejectRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Role approved successfully"));
  }

  @Test
  void testApproveRejectRole_InvalidInput() throws Exception {
    // Arrange
    ApproveRejectRQ request = new ApproveRejectRQ(); // Invalid because required fields are missing

    when(roleService.approveRejectRole(any(ApproveRejectRQ.class)))
        .thenThrow(new ApiRequestException("Invalid approval or rejection request"));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/approvedRejectRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist())
        .andExpect(jsonPath("$.message").value("Invalid approval or rejection request"));
  }

  @Test
  void testApproveRejectRole_RoleNotFound() throws Exception {
    // Arrange
    ApproveRejectRQ request = new ApproveRejectRQ();
    request.setApproveRejectDataID(99);
    request.setApproveStatus(MasterDataApproveStatus.APPROVED);

    when(roleService.approveRejectRole(any(ApproveRejectRQ.class)))
        .thenThrow(new ApiRequestException("Role with ID 99 does not exist"));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/approvedRejectRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist())
        .andExpect(jsonPath("$.message").value("Role with ID 99 does not exist"));
  }

  @Test
  void testUpdateApprovedRole_Success() throws Exception {
    // Arrange
    Integer roleID = 1;
    RoleDTO request = new RoleDTO();
    request.setRoleName("Admin");

    StandardResponse<Object> response =
        new StandardResponse<>(true, "Role updated successfully", null);
    when(roleService.updateApprovedRole(eq(roleID), any(RoleDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/updateApprovedRole/{roleID}", roleID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Role updated successfully"));
  }

  @Test
  void testUpdateApprovedRole_RoleNotFound() throws Exception {
    // Arrange
    Integer roleID = 99; // Non-existent role ID
    RoleDTO request = new RoleDTO();
    request.setRoleName("Admin");

    when(roleService.updateApprovedRole(eq(roleID), any(RoleDTO.class)))
        .thenThrow(new ApiRequestException("Role with ID 99 does not exist"));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/updateApprovedRole/{roleID}", roleID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist())
        .andExpect(jsonPath("$.message").value("Role with ID 99 does not exist"));
  }

  @Test
  void testUpdateApprovedRole_InvalidInput() throws Exception {
    // Arrange
    Integer roleID = 1;
    RoleDTO request = new RoleDTO(); // Missing required fields (e.g., roleName)

    when(roleService.updateApprovedRole(eq(roleID), any(RoleDTO.class)))
        .thenThrow(new ApiRequestException("Invalid role details"));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/updateApprovedRole/{roleID}", roleID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist())
        .andExpect(jsonPath("$.message").value("Invalid role details"));
  }

  @Test
  void testDeleteRoleTempById_Success() throws Exception {
    // Arrange
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setRoleID(1);

    StandardResponse<Void> response =
        new StandardResponse<>(true, "Role deleted successfully", null);
    when(roleService.deleteRoleTempById(roleDTO.getRoleID()))
        .thenReturn(ResponseEntity.ok(response));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/deleteRoleTemp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(roleDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Role deleted successfully"));
  }

  @Test
  void testDeleteRoleTempById_RoleNotFound() throws Exception {
    // Arrange
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setRoleID(99); // Non-existent role ID

    when(roleService.deleteRoleTempById(roleDTO.getRoleID()))
        .thenThrow(new ApiRequestException("Role with ID 99 does not exist"));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/role/deleteRoleTemp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(roleDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").doesNotExist())
        .andExpect(jsonPath("$.message").value("Role with ID 99 does not exist"));
  }
}
