package lk.sampath.casadminPortalms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.CreditFacilityTypeController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacility.CreditFacilityTypeDTO;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CreditFacilityTypeService;
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

@WebMvcTest(CreditFacilityTypeController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class CreditFacilityTypeControllerTest {

  @Value("${app.endpoint.saveCreditFacilityType}")
  private String saveCreditFacilityType;

  @Value("${app.endpoint.updateCreditFacilityTempType}")
  private String updateCreditFacilityTypeTemp;

  @Value("${app.endpoint.getCreditFacilityTempTypeById}")
  private String getCreditFacilityTypeTempById;

  @Value("${app.endpoint.creditFacilityTypeApproveReject}")
  private String approveRejectCreditFacilityType;

  @Value("${app.endpoint.getCreditFacilityType}")
  private String getCreditFacilityTypeId;

  @Value("${app.endpoint.getCreditFacilityTypeTempList}")
  private String getCreditFacilityTypeTempList;

  @Value("${app.endpoint.getCreditFacilityTypeMasterList}")
  private String getCreditFacilityTypeMasterList;

  @Value("${app.endpoint.deleteCreditFacilityTypeTemp}")
  private String deleteCreditFacilityTypeTemp;

  @Value("${app.endpoint.updateCreditFacilityMasterType}")
  private String updateCreditFacilityTypeMaster;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CreditFacilityTypeService creditFacilityTypeService;

  @MockitoBean private ModelMapper modelMapper;

  @Autowired private ObjectMapper objectMapper;

  private CreditFacilityTypeDTO tempCreditFacilityType;

  private CreditFacilityTypeDTO approvedCreditFacilityType;

  private ApproveRejectRQ approveRejectRQ;

  @InjectMocks private CreditFacilityTypeController creditFacilityTypeController;

  @BeforeEach
  public void setUp() {

    tempCreditFacilityType = new CreditFacilityTypeDTO();
    tempCreditFacilityType.setCreditFacilityTypeID(1);
    tempCreditFacilityType.setFacilityTypeName("Unit Testing");
    tempCreditFacilityType.setDescription("Unit Testing Description");
    tempCreditFacilityType.setApproveStatus(MasterDataApproveStatus.PENDING);

    approvedCreditFacilityType = new CreditFacilityTypeDTO();
    approvedCreditFacilityType.setCreditFacilityTypeID(1);
    approvedCreditFacilityType.setFacilityTypeName("Unit Testing");
    approvedCreditFacilityType.setDescription("Unit Testing Description");
    approvedCreditFacilityType.setApproveStatus(MasterDataApproveStatus.APPROVED);

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  /** saveCreditFacilityTypeTemp * */
  @Test
  void testSaveCreditFacilityTypeSuccess() throws Exception {

    StandardResponse<CreditFacilityTypeDTO> response =
        new StandardResponse<>(true, "Saved successfully", tempCreditFacilityType);

    when(creditFacilityTypeService.saveCreditFacilityTypeTemp(any(CreditFacilityTypeDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(saveCreditFacilityType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(tempCreditFacilityType)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.facilityTypeName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"));
  }

  @Test
  void testSaveCreditFacilityTypeBadRequest() throws Exception {
    CreditFacilityTypeDTO request = new CreditFacilityTypeDTO(); // Empty data

    when(creditFacilityTypeService.saveCreditFacilityTypeTemp(any(CreditFacilityTypeDTO.class)))
        .thenThrow(new ApiRequestException("Invalid data"));

    mockMvc
        .perform(
            post(saveCreditFacilityType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verify(creditFacilityTypeService, times(1))
        .saveCreditFacilityTypeTemp(any(CreditFacilityTypeDTO.class));
  }

  /** getCreditFacilityTypeTempById* */
  @Test
  void testGetCreditFacilityTypeTempById_Success() throws Exception {
    StandardResponse<CreditFacilityTypeDTO> response =
        new StandardResponse<>(true, "Success", tempCreditFacilityType);
    when(creditFacilityTypeService.findCreditFacilityTypeTempByID(1))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(getCreditFacilityTypeTempById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.response.facilityTypeName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.PENDING.name()));

    verify(creditFacilityTypeService, times(1)).findCreditFacilityTypeTempByID(1);
  }

  @Test
  void testGetCreditFacilityTypeTempById_NotFound() throws Exception {
    when(creditFacilityTypeService.findCreditFacilityTypeTempByID(1))
        .thenThrow(new ApiRequestException("Not found"));

    mockMvc
        .perform(get(getCreditFacilityTypeTempById, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()) // Expect HTTP 500 Internal Server Error
        .andExpect(jsonPath("$.message").value("Not found"));

    verify(creditFacilityTypeService, times(1)).findCreditFacilityTypeTempByID(1);
  }

  /** updateCreditFacilityTypeTemp* */
  @Test
  void testUpdateCreditFacilityTypeTemp_Success() throws Exception {
    StandardResponse<CreditFacilityTypeDTO> response =
        new StandardResponse<>(true, "Success", tempCreditFacilityType);

    when(creditFacilityTypeService.updateCreditFacilityTempType(
            eq(1), any(CreditFacilityTypeDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateCreditFacilityTypeTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempCreditFacilityType)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Success"))
        .andExpect(jsonPath("$.response.creditFacilityTypeID").value(1));

    verify(creditFacilityTypeService, times(1))
        .updateCreditFacilityTempType(eq(1), any(CreditFacilityTypeDTO.class));
  }

  @Test
  void testUpdateCreditFacilityTypeTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Update failed"))
        .when(creditFacilityTypeService)
        .updateCreditFacilityTempType(eq(1), any(CreditFacilityTypeDTO.class));

    mockMvc
        .perform(
            post(updateCreditFacilityTypeTemp, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempCreditFacilityType)))
        .andExpect(status().isBadRequest());

    verify(creditFacilityTypeService, times(1))
        .updateCreditFacilityTempType(eq(1), any(CreditFacilityTypeDTO.class));
  }

  /** approveRejectCreditFacilityTypeTemp* */
  @Test
  void testApproveRejectCreditFacilityType_Success() throws Exception {
    StandardResponse<CreditFacilityTypeDTO> response =
        new StandardResponse<>(true, "Success", approvedCreditFacilityType);

    when(creditFacilityTypeService.approveRejectCreditFacilityType(any(ApproveRejectRQ.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(approveRejectCreditFacilityType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(creditFacilityTypeService, times(1))
        .approveRejectCreditFacilityType(any(ApproveRejectRQ.class));
  }

  @Test
  void testApproveRejectCreditFacilityType_Failure() throws Exception {
    doThrow(new ApiRequestException("Approval failed"))
        .when(creditFacilityTypeService)
        .approveRejectCreditFacilityType(any(ApproveRejectRQ.class));

    mockMvc
        .perform(
            post(approveRejectCreditFacilityType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isBadRequest());

    verify(creditFacilityTypeService, times(1))
        .approveRejectCreditFacilityType(any(ApproveRejectRQ.class));
  }

  /** getCreditFacilityTypeMasterList* */
  @Test
  void testGetCreditFacilityTypeMasterList_Success() throws Exception {
    List<CreditFacilityTypeDTO> approvedCreditFacilityTypeList =
        List.of(approvedCreditFacilityType);
    StandardResponse<List<CreditFacilityTypeDTO>> response =
        new StandardResponse<>(true, "Success", approvedCreditFacilityTypeList);

    when(creditFacilityTypeService.searchCreditFacilityTypes())
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(getCreditFacilityTypeMasterList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].facilityTypeName").value("Unit Testing"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].description")
                .value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response[0].approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(creditFacilityTypeService, times(1)).searchCreditFacilityTypes();
  }

  @Test
  void testGetCreditFacilityTypeMasterList_ThrowsApiRequestException() throws Exception {
    when(creditFacilityTypeService.searchCreditFacilityTypes())
        .thenThrow(new ApiRequestException("Error retrieving documents"));

    mockMvc
        .perform(get(getCreditFacilityTypeMasterList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Error retrieving documents"));

    verify(creditFacilityTypeService, times(1)).searchCreditFacilityTypes();
  }

  /** getCreditFacilityTypeTempList* */
  @Test
  void testGetCreditFacilityTypeTempList_Success() throws Exception {
    List<CreditFacilityTypeDTO> tempCreditFacilityTypeList = List.of(tempCreditFacilityType);
    StandardResponse<List<CreditFacilityTypeDTO>> response =
        new StandardResponse<>(true, "Success", tempCreditFacilityTypeList);

    when(creditFacilityTypeService.findAllCreditFacilityTypeTempList())
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(getCreditFacilityTypeTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].facilityTypeName").value("Unit Testing"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.response[0].description")
                .value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response[0].approveStatus").value(MasterDataApproveStatus.PENDING.name()));

    verify(creditFacilityTypeService, times(1)).findAllCreditFacilityTypeTempList();
  }

  @Test
  void testGetCreditFacilityTypeTempList_ThrowsApiRequestException() throws Exception {
    when(creditFacilityTypeService.findAllCreditFacilityTypeTempList())
        .thenThrow(new ApiRequestException("Error retrieving documents"));

    mockMvc
        .perform(get(getCreditFacilityTypeTempList).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Error retrieving documents"));

    verify(creditFacilityTypeService, times(1)).findAllCreditFacilityTypeTempList();
  }

  /** getCreditFacilityTypeId* */
  @Test
  void testGetCreditFacilityTypeById_Success() throws Exception {
    StandardResponse<CreditFacilityTypeDTO> response =
        new StandardResponse<>(true, "Success", approvedCreditFacilityType);
    when(creditFacilityTypeService.findCreditFacilityTypeByID(1))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(get(getCreditFacilityTypeId, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.response.facilityTypeName").value("Unit Testing"))
        .andExpect(jsonPath("$.response.description").value("Unit Testing Description"))
        .andExpect(
            jsonPath("$.response.approveStatus").value(MasterDataApproveStatus.APPROVED.name()));

    verify(creditFacilityTypeService, times(1)).findCreditFacilityTypeByID(1);
  }

  @Test
  void testGetCreditFacilityTypeById_NotFound() throws Exception {
    when(creditFacilityTypeService.findCreditFacilityTypeByID(1))
        .thenThrow(new ApiRequestException("Not found"));

    mockMvc
        .perform(get(getCreditFacilityTypeId, 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Not found"));

    verify(creditFacilityTypeService, times(1)).findCreditFacilityTypeByID(1);
  }

  /** deleteCreditFacilityTypeTemp* */
  @Test
  void testDeleteCreditFacilityTypeTemp_Success() throws Exception {
    StandardResponse<Integer> response = new StandardResponse<>(true, "DELETED", 1);

    when(creditFacilityTypeService.deleteCreditFacilityTypeTemp(tempCreditFacilityType))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(deleteCreditFacilityTypeTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempCreditFacilityType)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("DELETED"))
        .andExpect(jsonPath("$.response").value(1));

    verify(creditFacilityTypeService, times(1))
        .deleteCreditFacilityTypeTemp(tempCreditFacilityType);
  }

  @Test
  void testDeleteCreditFacilityTypeTemp_Failure() throws Exception {
    doThrow(new ApiRequestException("Deletion failed"))
        .when(creditFacilityTypeService)
        .deleteCreditFacilityTypeTemp(tempCreditFacilityType);

    mockMvc
        .perform(
            post(deleteCreditFacilityTypeTemp)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempCreditFacilityType)))
        .andExpect(status().isBadRequest());

    verify(creditFacilityTypeService, times(1))
        .deleteCreditFacilityTypeTemp(tempCreditFacilityType);
  }

  /** updateCreditFacilityTypeMaster* */
  @Test
  void testUpdateCreditFacilityTypeMaster_Success() throws Exception {
    StandardResponse<CreditFacilityTypeDTO> response =
        new StandardResponse<>(true, "Success", tempCreditFacilityType);

    when(creditFacilityTypeService.updateApprovedCreditFacilityType(
            eq(1), any(CreditFacilityTypeDTO.class)))
        .thenReturn(ResponseEntity.ok(response));

    mockMvc
        .perform(
            post(updateCreditFacilityTypeMaster, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempCreditFacilityType)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Success"))
        .andExpect(jsonPath("$.response.creditFacilityTypeID").value(1));

    verify(creditFacilityTypeService, times(1))
        .updateApprovedCreditFacilityType(eq(1), any(CreditFacilityTypeDTO.class));
  }

  @Test
  void testUpdateCreditFacilityTypeMaster_Failure() throws Exception {
    doThrow(new ApiRequestException("Update failed"))
        .when(creditFacilityTypeService)
        .updateApprovedCreditFacilityType(eq(1), any(CreditFacilityTypeDTO.class));

    mockMvc
        .perform(
            post(updateCreditFacilityTypeMaster, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempCreditFacilityType)))
        .andExpect(status().isBadRequest());

    verify(creditFacilityTypeService, times(1))
        .updateApprovedCreditFacilityType(eq(1), any(CreditFacilityTypeDTO.class));
  }
}
