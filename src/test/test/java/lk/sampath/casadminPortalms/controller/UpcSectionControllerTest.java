package lk.sampath.casadminPortalms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.UpcSectionController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upcsection.UpcSectionDTO;
import lk.sampath.casadminportalms.entity.upcsection.UpcSection;
import lk.sampath.casadminportalms.entity.upcsection.UpcSectionTemp;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UpcSectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UpcSectionController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class UpcSectionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UpcSectionService upcSectionService;

  @Autowired private ObjectMapper objectMapper;

  private ApproveRejectRQ approveRejectRQ;

  private UpcSectionTemp upcSectionTemp;

  private UpcSection upcSection;

  private UpcSectionDTO upcSectionDTO;

  @BeforeEach
  public void setUp() {

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    upcSectionTemp = new UpcSectionTemp();
    upcSectionTemp.setUpcSectionID(1);
    upcSectionTemp.setUpcSectionName("Unit Testing");
    upcSectionTemp.setApproveStatus(MasterDataApproveStatus.PENDING);

    upcSectionDTO = new UpcSectionDTO();
    upcSectionDTO.setUpcSectionID(1);
    upcSectionDTO.setUpcSectionName("Unit Testing");
    upcSectionDTO.setApproveStatus(MasterDataApproveStatus.PENDING);

    upcSection = new UpcSection();
    upcSection.setUpcSectionID(1);
    upcSection.setUpcSectionName("Unit Testing");
    upcSection.setUpcSectionDescription("Unit Testing Description");
    upcSection.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  @Test
  void testViewAllUpcSectionTemp() throws Exception {
    List<UpcSectionDTO> mockResponse = new ArrayList<>();
    mockResponse.add(new UpcSectionDTO());
    StandardResponse<List<UpcSectionDTO>> response =
        new StandardResponse<>(true, "Success", mockResponse);

    when(upcSectionService.findAllUpcSectionTempList()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/upcSection/upcSectionTemp"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response").isArray());

    verify(upcSectionService, times(1)).findAllUpcSectionTempList();
  }

  @Test
  void testViewUpcSectionTempById() throws Exception {
    int mockId = 1;
    UpcSectionDTO mockData = new UpcSectionDTO();
    mockData.setUpcSectionID(mockId);
    StandardResponse<UpcSectionDTO> response = new StandardResponse<>(true, "Success", mockData);

    when(upcSectionService.findUpcSectionTempByID(mockId)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/upcSection/upcSectionTemp/" + mockId))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.response.upcSectionID").value(mockId)); // Validate the specific field

    verify(upcSectionService, times(1)).findUpcSectionTempByID(mockId);
  }

  @Test
  void testGetPagedUpcSectionData() throws Exception {
    List<UpcSectionDTO> mockResponse = new ArrayList<>();
    mockResponse.add(new UpcSectionDTO());
    StandardResponse<List<UpcSectionDTO>> response =
        new StandardResponse<>(true, "Success", mockResponse);

    when(upcSectionService.findAllApprovedUpcSection()).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/upcSection/upcSectionList"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response").isArray());

    verify(upcSectionService, times(1)).findAllApprovedUpcSection();
  }

  @Test
  void testSaveUpcSection() throws Exception {
    UpcSectionDTO mockRequest = new UpcSectionDTO();
    UpcSectionDTO mockResponse = new UpcSectionDTO();
    mockResponse.setUpcSectionID(1);
    StandardResponse<UpcSectionDTO> response =
        new StandardResponse<>(true, "Success", mockResponse);

    when(upcSectionService.saveUpcSectionTemp(any())).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/upcSection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.upcSectionID").value(1));

    verify(upcSectionService, times(1)).saveUpcSectionTemp(Mockito.any());
  }

  @Test
  void testApproveRejectUpcSection() throws Exception {
    ApproveRejectRQ mockRequest = new ApproveRejectRQ();
    UpcSectionDTO mockResponse = new UpcSectionDTO();
    StandardResponse<UpcSectionDTO> response =
        new StandardResponse<>(true, "Success", mockResponse);

    Mockito.when(upcSectionService.approveRejectUpcSection(Mockito.any()))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/upcSection/approveRejectUpcSection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isOk());

    Mockito.verify(upcSectionService, times(1)).approveRejectUpcSection(Mockito.any());
  }

  @Test
  void testUpdateUpcSectionTemp() throws Exception {
    int mockId = 1;
    UpcSectionDTO mockRequest = new UpcSectionDTO();
    UpcSectionDTO mockResponse = new UpcSectionDTO();
    mockResponse.setUpcSectionID(mockId);
    StandardResponse<UpcSectionDTO> response =
        new StandardResponse<>(true, "Success", mockResponse);

    Mockito.when(upcSectionService.updateUpcSectionTemp(Mockito.eq(mockId), Mockito.any()))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/upcSection/updateUpcSectionTemp/" + mockId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.upcSectionID").value(mockId));

    Mockito.verify(upcSectionService, times(1))
        .updateUpcSectionTemp(Mockito.eq(mockId), Mockito.any());
  }

  @Test
  void testDeleteUpcSectionTemp() throws Exception {
    UpcSectionDTO mockRequest = new UpcSectionDTO();
    mockRequest.setUpcSectionID(1);

    Mockito.when(upcSectionService.deleteUpcSectionFormTemp(mockRequest.getUpcSectionID()))
        .thenReturn(
            ResponseEntity.ok(
                new StandardResponse<>(true, "Success", mockRequest.getUpcSectionID())));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/upcSection/upcSectionTemp/deleteUpcSectionTemp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isOk());

    Mockito.verify(upcSectionService, times(1))
        .deleteUpcSectionFormTemp(mockRequest.getUpcSectionID());
  }

  @Test
  void testViewUpcSectionById_Success() throws Exception {
    int mockId = 1;
    StandardResponse<UpcSectionDTO> response = new StandardResponse<>(true, "Success", upcSection);
    when(upcSectionService.findApprovedUpcSectionByID(1)).thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get("/api/upcSection/" + mockId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) // Expect HTTP 200 OK
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.response.upcSectionName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.upcSectionDescription").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(upcSectionService, times(1)).findApprovedUpcSectionByID(1);
  }

  @Test
  void testViewUpcSectionById_UserDaNotFound() throws Exception {
    int mockId = 1;
    when(upcSectionService.findApprovedUpcSectionByID(1))
        .thenThrow(new ApiRequestException("Upc Section not found"));

    mockMvc
        .perform(get("/api/upcSection/" + mockId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()) // Expect HTTP 500 Internal Server Error
        .andExpect(jsonPath("$.message").value("Upc Section not found"));

    verify(upcSectionService, times(1)).findApprovedUpcSectionByID(1);
  }

  @Test
  void testUpdateApprovedUpcSection_Success() throws Exception {
    StandardResponse<UpcSectionDTO> response = new StandardResponse<>(true, "Updated", upcSection);

    when(upcSectionService.updateApprovedUpcSection(eq(1), any(UpcSectionDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post("/api/upcSection/updateUpcSectionMaster/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(upcSection)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated"));

    verify(upcSectionService, times(1)).updateApprovedUpcSection(eq(1), any(UpcSectionDTO.class));
  }

  @Test
  void testUpdateApprovedUpcSection_Failure() throws Exception {
    doThrow(new ApiRequestException("Update failed"))
        .when(upcSectionService)
        .updateApprovedUpcSection(eq(1), any(UpcSectionDTO.class));

    mockMvc
        .perform(
            post("/api/upcSection/updateUpcSectionMaster/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(upcSection)))
        .andExpect(status().isBadRequest());

    verify(upcSectionService, times(1)).updateApprovedUpcSection(eq(1), any(UpcSectionDTO.class));
  }
}
