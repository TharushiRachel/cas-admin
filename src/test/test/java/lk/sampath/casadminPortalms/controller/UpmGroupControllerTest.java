package lk.sampath.casadminPortalms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.UpmGroupController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UpmGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UpmGroupController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class UpmGroupControllerTest {

  @Value("${app.endpoint.upmGroupCreate}")
  private String saveUpmGroup;

  @Value("${app.endpoint.upmGroupTempUpdate}")
  private String updateUpmGroupTemp;

  @Value("${app.endpoint.upmGroupTempViewById}")
  private String upmGroupTempViewById;

  @Value("${app.endpoint.upmGroupApproveReject}")
  private String approveRejectUpmGroup;

  @Value("${app.endpoint.upmGroupView}")
  private String viewUpmGroupById;

  @Value("${app.endpoint.upmGroupTempList}")
  private String viewUpmGroupTempList;

  @Value("${app.endpoint.upmGroupList}")
  private String viewUpmGroupList;

  @Value("${app.endpoint.deleteUpmGroup}")
  private String deleteUpmGroupTemp;

  @Value("${app.endpoint.updateUpmGroup}")
  private String updateApprovedUpmGroup;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UpmGroupService upmGroupService;

  @MockitoBean private ModelMapper modelMapper;

  @Autowired private ObjectMapper objectMapper;

  private UpmGroupDTO pendingUpmGroup;

  private UpmGroupDTO approvedUpmGroup;

  private ApproveRejectRQ approveRejectRQ;

  @BeforeEach
  public void setUp() {

    pendingUpmGroup = new UpmGroupDTO();
    pendingUpmGroup.setUpmGroupID(1);
    pendingUpmGroup.setGroupCode("Unit Testing");
    pendingUpmGroup.setReferenceName("Unit Testing Reference");
    pendingUpmGroup.setApproveStatus(MasterDataApproveStatus.PENDING);

    approvedUpmGroup = new UpmGroupDTO();
    approvedUpmGroup.setUpmGroupID(1);
    approvedUpmGroup.setGroupCode("Unit Testing");
    approvedUpmGroup.setReferenceName("Unit Testing Reference");
    approvedUpmGroup.setApproveStatus(MasterDataApproveStatus.APPROVED);

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  /** listUpmGroupTemp * */
  @Test
  void testViewAllUpmGroupTemp() throws Exception {
    List<UpmGroupDTO> upmGroupList = List.of(pendingUpmGroup);
    StandardResponse<List<UpmGroupDTO>> response =
        new StandardResponse<>(true, "Success", upmGroupList);
    when(upmGroupService.findAllUpmGroupTempList()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUpmGroupTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.response[0].groupCode").value("Unit Testing"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].referenceName")
                .value("Unit Testing Reference"));

    verify(upmGroupService).findAllUpmGroupTempList();
  }

  @Test
  void testViewAllUpmGroupTemp_EmptyList() throws Exception {
    List<UpmGroupDTO> supportingDocDTOS = List.of();
    StandardResponse<List<UpmGroupDTO>> response =
        new StandardResponse<>(true, "Success", supportingDocDTOS);
    when(upmGroupService.findAllUpmGroupTempList()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUpmGroupTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response").isEmpty());

    verify(upmGroupService).findAllUpmGroupTempList();
  }

  /** viewUpmGroupTempByID * */
  @Test
  void testViewUpmGroupTempById_Success() throws Exception {
    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(true, "Success", pendingUpmGroup);
    when(upmGroupService.findUpmGroupTempByID(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(upmGroupTempViewById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.groupCode").value("Unit Testing"))
        .andExpect(jsonPath("$.response.referenceName").value("Unit Testing Reference"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.PENDING.name()));

    verify(upmGroupService).findUpmGroupTempByID(1);
  }

  @Test
  void testViewUpmGroupTempById_GroupCodeNotFound() throws Exception {
    when(upmGroupService.findUpmGroupTempByID(1))
        .thenThrow(new ApiRequestException("Upm Group Code not found"));

    mockMvc
        .perform(get(upmGroupTempViewById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Upm Group Code not found"));

    verify(upmGroupService).findUpmGroupTempByID(1);
  }

  /** getPagedUpmGroupData * */
  @Test
  void testViewAllUpmGroup_Success() throws Exception {
    List<UpmGroupDTO> upmGroupDTOList = List.of(approvedUpmGroup);
    StandardResponse<List<UpmGroupDTO>> response =
        new StandardResponse<>(true, "Success", upmGroupDTOList);

    when(upmGroupService.searchUpmGroups()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUpmGroupList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response[0].groupCode").value("Unit Testing"))
        .andExpect(jsonPath("$.response[0].referenceName").value("Unit Testing Reference"))
        .andExpect(
            jsonPath("$.response[0].approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(upmGroupService).searchUpmGroups();
  }

  @Test
  void testViewAllUpmGroup_ThrowsApiRequestException() throws Exception {
    when(upmGroupService.searchUpmGroups())
        .thenThrow(new ApiRequestException("Error retrieving document"));

    mockMvc
        .perform(get(viewUpmGroupList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Error retrieving document"));

    verify(upmGroupService).searchUpmGroups();
  }

  /** viewUpmGroupById * */
  @Test
  void testViewUpmGroupId_Success() throws Exception {
    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(true, "Success", approvedUpmGroup);
    when(upmGroupService.findUpmGroupById(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUpmGroupById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.groupCode").value("Unit Testing"))
        .andExpect(jsonPath("$.response.referenceName").value("Unit Testing Reference"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(upmGroupService).findUpmGroupById(1);
  }

  @Test
  void testViewUpmGroupById_UpmGroupCodeNotFound() throws Exception {
    when(upmGroupService.findUpmGroupById(1))
        .thenThrow(new ApiRequestException("UpmGroup not found"));

    mockMvc
        .perform(get(viewUpmGroupById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("UpmGroup not found"));

    verify(upmGroupService).findUpmGroupById(1);
  }

  /** saveUPMGroup * */
  @Test
  void testSaveUpmGroup_Success() throws Exception {
    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(true, "Saved Successfully", pendingUpmGroup);

    when(upmGroupService.saveUPMGroupTemp(any(UpmGroupDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(saveUpmGroup)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUpmGroup)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.groupCode").value("Unit Testing"))
        .andExpect(jsonPath("$.response.referenceName").value("Unit Testing Reference"));

    verify(upmGroupService).saveUPMGroupTemp(any(UpmGroupDTO.class));
  }

  @Test
  void testUpmGroupSaved_Failure() throws Exception {
    UpmGroupDTO request = new UpmGroupDTO();
    doThrow(new ApiRequestException("Validation failed"))
        .when(upmGroupService)
        .saveUPMGroupTemp(any(UpmGroupDTO.class));

    mockMvc
        .perform(
            post(saveUpmGroup)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verify(upmGroupService).saveUPMGroupTemp(any(UpmGroupDTO.class));
  }

  /** approveRejectUpmGroup * */
  @Test
  void testApproveRejectUpmGroup_Success() throws Exception {
    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(true, "APPROVED", approveRejectRQ);
    when(upmGroupService.approveRejectUpmGroup(any(ApproveRejectRQ.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(approveRejectUpmGroup)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(MasterDataApproveStatus.APPROVED.name()));

    verify(upmGroupService).approveRejectUpmGroup(any(ApproveRejectRQ.class));
  }

  @Test
  void testApproveRejectUpmGroup_Failure() throws Exception {
    doThrow(new ApiRequestException("Approval failed"))
        .when(upmGroupService)
        .approveRejectUpmGroup(any(ApproveRejectRQ.class));

    mockMvc
        .perform(
            post(approveRejectUpmGroup)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isBadRequest());

    verify(upmGroupService).approveRejectUpmGroup(any(ApproveRejectRQ.class));
  }

  /** updateUpmGroupTemp * */
  @Test
  void testUpdateUpmGroupTemp_Success() throws Exception {
    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(true, "Updated", pendingUpmGroup);

    when(upmGroupService.updateUpmGroupTemp(eq(1), any(UpmGroupDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateUpmGroupTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUpmGroup)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated"));

    verify(upmGroupService).updateUpmGroupTemp(eq(1), any(UpmGroupDTO.class));
  }

  @Test
  void testUpdateUpmGroupTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Update failed"))
        .when(upmGroupService)
        .updateUpmGroupTemp(eq(1), any(UpmGroupDTO.class));

    mockMvc
        .perform(
            post(updateUpmGroupTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUpmGroup)))
        .andExpect(status().isBadRequest());

    verify(upmGroupService).updateUpmGroupTemp(eq(1), any(UpmGroupDTO.class));
  }

  /** updateApprovedUpmGroup * */
  @Test
  void testUpdateApprovedUpmGroup_Success() throws Exception {
    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(true, "Updated", approvedUpmGroup);

    when(upmGroupService.updateApprovedUpmGroup(eq(1), any(UpmGroupDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateApprovedUpmGroup, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approvedUpmGroup)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated"));

    verify(upmGroupService).updateApprovedUpmGroup(eq(1), any(UpmGroupDTO.class));
  }

  @Test
  void testUpdateApprovedUpmGroup_Failure() throws Exception {
    doThrow(new ApiRequestException("Update Failed"))
        .when(upmGroupService)
        .updateApprovedUpmGroup(eq(1), any(UpmGroupDTO.class));

    mockMvc
        .perform(
            post(updateApprovedUpmGroup, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approvedUpmGroup)))
        .andExpect(status().isBadRequest());

    verify(upmGroupService).updateApprovedUpmGroup(eq(1), any(UpmGroupDTO.class));
  }

  /** deleteUpmGroup * */
  @Test
  void testDeleteUpmGroupTemp_Success() throws Exception {
    StandardResponse<Void> response = new StandardResponse<>(true, "DELETED", null);

    when(upmGroupService.deleteUpmGroup(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(deleteUpmGroupTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUpmGroup)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("DELETED"));

    verify(upmGroupService).deleteUpmGroup(1);
  }

  @Test
  void testDeleteUpmGroupTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Deletion Failed")).when(upmGroupService).deleteUpmGroup(1);

    mockMvc
        .perform(
            post(deleteUpmGroupTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUpmGroup)))
        .andExpect(status().isBadRequest());

    verify(upmGroupService).deleteUpmGroup(1);
  }
}
