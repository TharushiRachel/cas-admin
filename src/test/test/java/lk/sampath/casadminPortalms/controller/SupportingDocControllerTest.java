package lk.sampath.casadminPortalms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.SupportingDocController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.supportingdoc.SupportingDocDTO;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.SupportingDocService;
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

@WebMvcTest(SupportingDocController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class SupportingDocControllerTest {

  @Value("${app.endpoint.saveSupportingDoc}")
  private String saveSupportingDoc;

  @Value("${app.endpoint.updateSupportingDocTemp}")
  private String updateSupportingDocTemp;

  @Value("${app.endpoint.supportingDocTempViewById}")
  private String supportingDocTempViewById;

  @Value("${app.endpoint.supportingDocApproveReject}")
  private String approveRejectSupportingDoc;

  @Value("${app.endpoint.viewSupportingDocById}")
  private String viewSupportingDocById;

  @Value("${app.endpoint.viewSupportingDocTempList}")
  private String viewSupportingDocTempList;

  @Value("${app.endpoint.viewSupportingDocList}")
  private String viewSupportingDocList;

  @Value("${app.endpoint.deleteSupportingDocTemp}")
  private String deleteSupportingDocTemp;

  @Value("${app.endpoint.updateApprovedSupportingDoc}")
  private String updateApprovedSupportingDoc;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private SupportingDocService supportingDocService;

  @MockitoBean private ModelMapper modelMapper;

  @Autowired private ObjectMapper objectMapper;

  private SupportingDocDTO pendingSupportingDoc;

  private SupportingDocDTO approvedSupportingDoc;

  private ApproveRejectRQ approveRejectRQ;

  @InjectMocks private SupportingDocController supportingDocController;

  @BeforeEach
  public void setUp() {
    pendingSupportingDoc = new SupportingDocDTO();
    pendingSupportingDoc.setSupportingDocID(1);
    pendingSupportingDoc.setDocumentName("Unit Testing");
    pendingSupportingDoc.setDescription("Unit Testing Description");
    pendingSupportingDoc.setApproveStatus(MasterDataApproveStatus.PENDING);

    approvedSupportingDoc = new SupportingDocDTO();
    approvedSupportingDoc.setSupportingDocID(1);
    approvedSupportingDoc.setDocumentName("Unit Testing");
    approvedSupportingDoc.setDescription("Unit Testing Description");
    approvedSupportingDoc.setApproveStatus(MasterDataApproveStatus.APPROVED);

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  /** viewAllSupportingDocsTemp * */
  @Test
  void testViewAllSupportingDocTemp() throws Exception {
    List<SupportingDocDTO> supportingDocList = List.of(pendingSupportingDoc);
    StandardResponse<List<SupportingDocDTO>> response =
        new StandardResponse<>(true, "Success", supportingDocList);
    when(supportingDocService.findAllSupportingDocTempList())
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewSupportingDocTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].documentName").value("Unit Testing"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].description")
                .value("Unit Testing Description"));

    verify(supportingDocService, times(1)).findAllSupportingDocTempList();
  }

  @Test
  void testViewAllSupportingDocTemp_EmptyList() throws Exception {
    List<SupportingDocDTO> supportingDocDTOS = List.of();
    StandardResponse<List<SupportingDocDTO>> response =
        new StandardResponse<>(true, "Success", supportingDocDTOS);
    when(supportingDocService.findAllSupportingDocTempList())
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewSupportingDocTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response").isEmpty());

    verify(supportingDocService, times(1)).findAllSupportingDocTempList();
  }

  @Test
  void testViewAllSupportingDocTemp_ThrowsApiRequestException() throws Exception {
    when(supportingDocService.findAllSupportingDocTempList())
        .thenThrow(new ApiRequestException("Error fetching supportingDoc list"));

    mockMvc
        .perform(get(viewSupportingDocTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Error fetching supportingDoc list"));

    verify(supportingDocService, times(1)).findAllSupportingDocTempList();
  }

  /** viewSupportingDocTempById * */
  @Test
  void testViewSupportingDocTempById_Success() throws Exception {
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(true, "Success", pendingSupportingDoc);
    when(supportingDocService.findSupportingDocTempById(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(supportingDocTempViewById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.documentName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.PENDING.name()));

    verify(supportingDocService, times(1)).findSupportingDocTempById(1);
  }

  @Test
  void testViewSupportingDocTempById_DocumentNotFound() throws Exception {
    when(supportingDocService.findSupportingDocTempById(1))
        .thenThrow(new ApiRequestException("SupportingDoc not found"));

    mockMvc
        .perform(get(supportingDocTempViewById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("SupportingDoc not found"));

    verify(supportingDocService, times(1)).findSupportingDocTempById(1);
  }

  /** getApprovedSupportingDocData * */
  @Test
  void testViewAllSupportingDoc_Success() throws Exception {
    List<SupportingDocDTO> supportingDocDTOList = List.of(approvedSupportingDoc);
    StandardResponse<List<SupportingDocDTO>> response =
        new StandardResponse<>(true, "Success", supportingDocDTOList);

    when(supportingDocService.searchSupportingDocGroups()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewSupportingDocList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response[0].documentName").value("Unit Testing"))
        .andExpect(jsonPath("$.response[0].description").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response[0].approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(supportingDocService, times(1)).searchSupportingDocGroups();
  }

  @Test
  void testViewSupportingDoc_ThrowsApiRequestException() throws Exception {
    when(supportingDocService.searchSupportingDocGroups())
        .thenThrow(new ApiRequestException("Error retrieving document"));

    mockMvc
        .perform(get(viewSupportingDocList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Error retrieving document"));

    verify(supportingDocService, times(1)).searchSupportingDocGroups();
  }

  /** viewSupportingDocById * */
  @Test
  void testViewSupportingDocId_Success() throws Exception {
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(true, "Success", approvedSupportingDoc);
    when(supportingDocService.findSupportingDocById(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(viewSupportingDocById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.documentName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(supportingDocService, times(1)).findSupportingDocById(1);
  }

  @Test
  void testViewSupportingDocById_SupportingDocNotFound() throws Exception {
    when(supportingDocService.findSupportingDocById(1))
        .thenThrow(new ApiRequestException("SupportingDoc not found"));

    mockMvc
        .perform(get(viewSupportingDocById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("SupportingDoc not found"));

    verify(supportingDocService, times(1)).findSupportingDocById(1);
  }

  /** saveSupportingDoc * */
  @Test
  void testSaveSupporting_Success() throws Exception {
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(true, "Saved Successfully", pendingSupportingDoc);

    when(supportingDocService.saveSupportingDocTemp(any(SupportingDocDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(saveSupportingDoc)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingSupportingDoc)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.documentName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"));

    verify(supportingDocService, times(1)).saveSupportingDocTemp(any(SupportingDocDTO.class));
  }

  @Test
  void testSupportingDocSave_Failure() throws Exception {
    SupportingDocDTO request = new SupportingDocDTO();
    doThrow(new ApiRequestException("Validation failed"))
        .when(supportingDocService)
        .saveSupportingDocTemp(any(SupportingDocDTO.class));

    mockMvc
        .perform(
            post(saveSupportingDoc)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verify(supportingDocService, times(1)).saveSupportingDocTemp(any(SupportingDocDTO.class));
  }

  /** approveRejectSupportingDoc * */
  @Test
  void testApproveRejectSupportingDoc_Success() throws Exception {
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(true, "APPROVED", approvedSupportingDoc);
    when(supportingDocService.approveRejectSupportingDoc(any(ApproveRejectRQ.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(approveRejectSupportingDoc)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(MasterDataApproveStatus.APPROVED.name()));

    verify(supportingDocService, times(1)).approveRejectSupportingDoc(any(ApproveRejectRQ.class));
  }

  @Test
  void testApproveRejectSupportingDoc_Failure() throws Exception {
    doThrow(new ApiRequestException("Approval failed"))
        .when(supportingDocService)
        .approveRejectSupportingDoc(any(ApproveRejectRQ.class));

    mockMvc
        .perform(
            post(approveRejectSupportingDoc)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isBadRequest());

    verify(supportingDocService, times(1)).approveRejectSupportingDoc(any(ApproveRejectRQ.class));
  }

  /** updateSupportingDocTemp * */
  @Test
  void testUpdateSupportingDocTemp_Success() throws Exception {
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(true, "Updated", pendingSupportingDoc);

    when(supportingDocService.updateSupportingDocTemp(eq(1), any(SupportingDocDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateSupportingDocTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingSupportingDoc)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated"));

    verify(supportingDocService, times(1))
        .updateSupportingDocTemp(eq(1), any(SupportingDocDTO.class));
  }

  @Test
  void testUpdateSupportingDocTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Update failed"))
        .when(supportingDocService)
        .updateSupportingDocTemp(eq(1), any(SupportingDocDTO.class));

    mockMvc
        .perform(
            post(updateSupportingDocTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingSupportingDoc)))
        .andExpect(status().isBadRequest());

    verify(supportingDocService, times(1))
        .updateSupportingDocTemp(eq(1), any(SupportingDocDTO.class));
  }

  /** updateApprovedSupportingDoc * */
  @Test
  void testUpdateApprovedSupportingDoc_Success() throws Exception {
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(true, "Updated", approvedSupportingDoc);

    when(supportingDocService.updateApprovedSupportingDoc(eq(1), any(SupportingDocDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateApprovedSupportingDoc, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approvedSupportingDoc)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated"));

    verify(supportingDocService, times(1))
        .updateApprovedSupportingDoc(eq(1), any(SupportingDocDTO.class));
  }

  @Test
  void testUpdateApprovedSupportingDoc_Failure() throws Exception {
    doThrow(new ApiRequestException("Update Failed"))
        .when(supportingDocService)
        .updateApprovedSupportingDoc(eq(1), any(SupportingDocDTO.class));

    mockMvc
        .perform(
            post(updateApprovedSupportingDoc, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approvedSupportingDoc)))
        .andExpect(status().isBadRequest());

    verify(supportingDocService, times(1))
        .updateApprovedSupportingDoc(eq(1), any(SupportingDocDTO.class));
  }

  /** deleteSupportingDocTemp * */
  @Test
  void testDeleteSupportingDocTemp_Success() throws Exception {
    StandardResponse<Void> response = new StandardResponse<>(true, "DELETED", null);

    when(supportingDocService.deleteSupportingDocTemp(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(deleteSupportingDocTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingSupportingDoc)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("DELETED"));

    verify(supportingDocService, times(1)).deleteSupportingDocTemp(1);
  }

  @Test
  void testDeleteSupportingDocTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Deletion Failed"))
        .when(supportingDocService)
        .deleteSupportingDocTemp(1);

    mockMvc
        .perform(
            post(deleteSupportingDocTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pendingSupportingDoc)))
        .andExpect(status().isBadRequest());

    verify(supportingDocService, times(1)).deleteSupportingDocTemp(1);
  }
}
