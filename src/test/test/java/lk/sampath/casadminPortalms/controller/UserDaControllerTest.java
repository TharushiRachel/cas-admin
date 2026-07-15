package lk.sampath.casadminPortalms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.UserDaController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.userda.UserDaDTO;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UserDaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

@WebMvcTest(UserDaController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class UserDaControllerTest {

  @Value("${app.endpoint.saveUserDa}")
  private String saveUserDa;

  @Value("${app.endpoint.updateUserDaTemp}")
  private String updateUserDaTemp;

  @Value("${app.endpoint.viewUserDaTempById}")
  private String viewUserDaTempById;

  @Value("${app.endpoint.userDaApproveReject}")
  private String approveRejectUserDa;

  @Value("${app.endpoint.viewUserDaById}")
  private String viewUserDaById;

  @Value("${app.endpoint.viewUserDaTempList}")
  private String viewUserDaTempList;

  @Value("${app.endpoint.viewUserDaList}")
  private String viewUserDaList;

  @Value("${app.endpoint.deleteUserDaTemp}")
  private String deleteUserDaTemp;

  @Value("${app.endpoint.updateUserDaMaster}")
  private String updateApprovedUserDa;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserDaService userDaService;

  @MockitoBean private ModelMapper modelMapper;

  @Autowired private ObjectMapper objectMapper;

  private UserDaDTO pendingUserDaDTO;

  private UserDaDTO approveUserDaDTO;

  private ApproveRejectRQ approveRejectRQ;

  @InjectMocks private UserDaController userDaController;

  @BeforeEach
  public void setUp() {

    pendingUserDaDTO = new UserDaDTO();
    pendingUserDaDTO.setUserDaID(1);
    pendingUserDaDTO.setUserName("Unit Testing");
    pendingUserDaDTO.setDescription("Unit Testing Description");
    pendingUserDaDTO.setApproveStatus(MasterDataApproveStatus.PENDING);

    approveUserDaDTO = new UserDaDTO();
    approveUserDaDTO.setUserName("Unit Testing");
    approveUserDaDTO.setDescription("Unit Testing Description");
    approveUserDaDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  /** viewAllUserDaTemp * */
  @Test
  void testViewAllUserDaTemp() throws Exception {
    List<UserDaDTO> userDaDTOList = List.of(pendingUserDaDTO);
    StandardResponse<List<UserDaDTO>> response =
        new StandardResponse<>(true, "Success", userDaDTOList);

    when(userDaService.findAllUserDaTempList()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUserDaTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.response[0].userName").value("Unit Testing"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].description")
                .value("Unit Testing Description"));

    verify(userDaService).findAllUserDaTempList();
  }

  @Test
  void testViewAllUserDaTemp_EmptyList() throws Exception {
    List<UserDaDTO> userDaDTOList = List.of();
    StandardResponse<List<UserDaDTO>> response =
        new StandardResponse<>(true, "Success", userDaDTOList);
    when(userDaService.findAllUserDaTempList()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUserDaTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response").isEmpty());

    verify(userDaService).findAllUserDaTempList();
  }

  @Test
  void testViewAllUserDaTemp_ThrowsApiRequestException() throws Exception {
    when(userDaService.findAllUserDaTempList())
        .thenThrow(new ApiRequestException("Error fetching userDa list"));

    mockMvc
        .perform(get(viewUserDaTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()) // Expect HTTP 500 Internal Server Error
        .andExpect(jsonPath("$.message").value("Error fetching userDa list"));

    verify(userDaService).findAllUserDaTempList();
  }

  /** viewUserDaTempById * */
  @Test
  void testViewSupportingDocTempById_Success() throws Exception {
    StandardResponse<UserDaDTO> response =
        new StandardResponse<>(true, "Success", pendingUserDaDTO);
    when(userDaService.findUserDaTempByID(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUserDaTempById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) // Expect HTTP 200 OK
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.response.userName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.PENDING.name()));

    verify(userDaService).findUserDaTempByID(1);
  }

  @Test
  void testViewUserDaTempById_UserDaNotFound() throws Exception {
    when(userDaService.findUserDaTempByID(1))
        .thenThrow(new ApiRequestException("UserDa not found"));

    mockMvc
        .perform(get(viewUserDaTempById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()) // Expect HTTP 500 Internal Server Error
        .andExpect(jsonPath("$.message").value("UserDa not found"));

    verify(userDaService).findUserDaTempByID(1);
  }

  /** getPagedUserDaData * */
  @Test
  void testViewAllUserDa_Success() throws Exception {
    List<UserDaDTO> userDaDTOList = List.of(approveUserDaDTO);
    StandardResponse<List<UserDaDTO>> response =
        new StandardResponse<>(true, "Success", userDaDTOList);

    when(userDaService.findAllApprovedUserDa()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(post(viewUserDaList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.response[0].userName").value("Unit Testing"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].description")
                .value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response[0].approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(userDaService).findAllApprovedUserDa();
  }

  @Test
  void testViewAllUserDa_ThrowsApiRequestException() throws Exception {
    when(userDaService.findAllApprovedUserDa())
        .thenThrow(new ApiRequestException("Error retrieving documents"));

    mockMvc
        .perform(post(viewUserDaList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()) // Expect HTTP 400 Internal Server Error
        .andExpect(jsonPath("$.message").value("Error retrieving documents"));

    verify(userDaService).findAllApprovedUserDa();
  }

  /** viewUserDaById * */
  @Test
  void testViewUserDaById_Success() throws Exception {
    StandardResponse<UserDaDTO> response =
        new StandardResponse<>(true, "Success", approveUserDaDTO);
    when(userDaService.findApprovedUserDaById(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewUserDaById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) // Expect HTTP 200 OK
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.response.userName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(userDaService).findApprovedUserDaById(1);
  }

  @Test
  void testViewUserDaById_UserDaNotFound() throws Exception {
    when(userDaService.findApprovedUserDaById(1))
        .thenThrow(new ApiRequestException("UserDa not found"));

    mockMvc
        .perform(get(viewUserDaById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()) // Expect HTTP 500 Internal Server Error
        .andExpect(jsonPath("$.message").value("UserDa not found"));

    verify(userDaService).findApprovedUserDaById(1);
  }

  /** saveUserDa * */
  @Test
  void testSaveUserDa_Success() throws Exception {
    StandardResponse<UserDaDTO> response =
        new StandardResponse<>(true, "Saved successfully", pendingUserDaDTO);

    when(userDaService.saveUserDaTemp(any(UserDaDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(saveUserDa)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUserDaDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.userName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"));

    verify(userDaService).saveUserDaTemp(any(UserDaDTO.class));
  }

  @Test
  void testSaveUserDa_Failure() throws Exception {
    UserDaDTO request = new UserDaDTO();
    doThrow(new ApiRequestException("Validation failed"))
        .when(userDaService)
        .saveUserDaTemp(any(UserDaDTO.class));

    mockMvc
        .perform(
            post(saveUserDa)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verify(userDaService).saveUserDaTemp(any(UserDaDTO.class));
  }

  /** approveRejectUserDa * */
  @Test
  void testApproveRejectUserDa_Success() throws Exception {
    StandardResponse<UserDaDTO> response =
        new StandardResponse<>(true, "APPROVED", approveUserDaDTO);
    when(userDaService.approveRejectUserDa(any(ApproveRejectRQ.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(approveRejectUserDa)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(MasterDataApproveStatus.APPROVED.name()));

    verify(userDaService).approveRejectUserDa(any(ApproveRejectRQ.class));
  }

  @Test
  void testApproveRejectUserDa_Failure() throws Exception {
    doThrow(new ApiRequestException("Approval failed"))
        .when(userDaService)
        .approveRejectUserDa(any(ApproveRejectRQ.class));

    mockMvc
        .perform(
            post(approveRejectUserDa)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isBadRequest());

    verify(userDaService).approveRejectUserDa(any(ApproveRejectRQ.class));
  }

  /** updateUserDaTemp * */
  @Test
  void testUpdateUserDaTemp_Success() throws Exception {
    StandardResponse<UserDaDTO> response =
        new StandardResponse<>(true, "Updated", pendingUserDaDTO);

    when(userDaService.updateUserDaTemp(eq(1), any(UserDaDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateUserDaTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUserDaDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated"));

    verify(userDaService).updateUserDaTemp(eq(1), any(UserDaDTO.class));
  }

  @Test
  void testUpdateUserDaTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Update failed"))
        .when(userDaService)
        .updateUserDaTemp(eq(1), any(UserDaDTO.class));

    mockMvc
        .perform(
            post(updateUserDaTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUserDaDTO)))
        .andExpect(status().isBadRequest());

    verify(userDaService).updateUserDaTemp(eq(1), any(UserDaDTO.class));
  }

  /** updateApprovedUserDa * */
  @Test
  void testUpdateApprovedUserDa_Success() throws Exception {
    StandardResponse<UserDaDTO> response =
        new StandardResponse<>(true, "Updated", approveUserDaDTO);

    when(userDaService.updateApprovedUserDa(eq(1), any(UserDaDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateApprovedUserDa, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveUserDaDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated"));

    verify(userDaService).updateApprovedUserDa(eq(1), any(UserDaDTO.class));
  }

  @Test
  void testUpdateApprovedUserDa_Failure() throws Exception {
    doThrow(new ApiRequestException("Update failed"))
        .when(userDaService)
        .updateApprovedUserDa(eq(1), any(UserDaDTO.class));

    mockMvc
        .perform(
            post(updateApprovedUserDa, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveUserDaDTO)))
        .andExpect(status().isBadRequest());

    verify(userDaService).updateApprovedUserDa(eq(1), any(UserDaDTO.class));
  }

  /** deleteUserDaTemp * */
  @Test
  void testDeleteUserDaTemp_Success() throws Exception {
    StandardResponse<Void> response = new StandardResponse<>(true, "DELETED", null);

    when(userDaService.deleteUserDaFromTemp(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(deleteUserDaTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUserDaDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("DELETED"));

    verify(userDaService).deleteUserDaFromTemp(1);
  }

  @Test
  void testDeleteUserDaTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Deletion failed")).when(userDaService).deleteUserDaFromTemp(1);

    mockMvc
        .perform(
            post(deleteUserDaTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingUserDaDTO)))
        .andExpect(status().isBadRequest());

    verify(userDaService).deleteUserDaFromTemp(1);
  }
}
