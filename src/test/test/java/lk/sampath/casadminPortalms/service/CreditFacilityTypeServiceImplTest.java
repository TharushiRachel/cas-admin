package lk.sampath.casadminPortalms.service;

import static lk.sampath.casadminportalms.service.impl.CreditFacilityTypeServiceImpl.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacility.CreditFacilityTypeDTO;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityTypeAud;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityTypeTemp;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeAudRepository;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeRepository;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeTempRepository;
import lk.sampath.casadminportalms.service.impl.CreditFacilityTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;

/**
 * @author yomesh
 */
@ExtendWith(MockitoExtension.class)
class CreditFacilityTypeServiceImplTest {

  @Mock private CreditFacilityTypeTempRepository creditFacilityTypeTempRepository;

  @Mock private CreditFacilityTypeRepository creditFacilityTypeRepository;

  @Mock private CreditFacilityTypeAudRepository creditFacilityTypeAudRepository;

  @Mock private TaskScheduler taskScheduler;

  @Mock private ModelMapper modelMapper;

  @InjectMocks private CreditFacilityTypeServiceImpl creditFacilityTypeService;

  private CreditFacilityTypeTemp creditFacilityTypeTemp;
  private CreditFacilityType creditFacilityType;

  private CreditFacilityTypeDTO tempCreditFacilityTypeDTO;
  private CreditFacilityTypeAud audit;

  private CreditFacilityTypeDTO approvedCreditFacilityTypeDTO;

  private ApproveRejectRQ approveRejectRQ;

  @BeforeEach
  void setup() {

    tempCreditFacilityTypeDTO = new CreditFacilityTypeDTO();
    tempCreditFacilityTypeDTO.setCreditFacilityTypeID(1);
    tempCreditFacilityTypeDTO.setFacilityTypeName("Temp DTO");
    tempCreditFacilityTypeDTO.setDescription("Unit Testing Description");
    tempCreditFacilityTypeDTO.setApproveStatus(MasterDataApproveStatus.PENDING);

    approvedCreditFacilityTypeDTO = new CreditFacilityTypeDTO();
    approvedCreditFacilityTypeDTO.setCreditFacilityTypeID(1);
    approvedCreditFacilityTypeDTO.setFacilityTypeName("Approved dto");
    approvedCreditFacilityTypeDTO.setDescription("Unit Testing Description");
    approvedCreditFacilityTypeDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    creditFacilityTypeTemp = new CreditFacilityTypeTemp();
    creditFacilityTypeTemp.setCreditFacilityTypeID(30);
    creditFacilityTypeTemp.setFacilityTypeName("Loan");
    creditFacilityTypeTemp.setDescription("Loan for Temp");
    creditFacilityTypeTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
    creditFacilityTypeTemp.setStatus(Status.ACT);

    creditFacilityType = new CreditFacilityType();
    creditFacilityType.setCreditFacilityTypeID(30);
    creditFacilityType.setFacilityTypeName("Loan");
    creditFacilityType.setDescription("Loan");
    creditFacilityType.setApproveStatus(MasterDataApproveStatus.APPROVED);

    audit = new CreditFacilityTypeAud();
    audit.setCreditFacilityTypeID(creditFacilityTypeTemp.getCreditFacilityTypeID());
    audit.setFacilityTypeName(creditFacilityTypeTemp.getFacilityTypeName());
  }

  /** Test cases for save Credit Facility Type Temp */
  @Test
  void testSaveCreditFacilityTypeTemp_Success() throws ApiRequestException {

    when(creditFacilityTypeTempRepository.getCurrentSequenceValue()).thenReturn(2);
    when(creditFacilityTypeTempRepository.save(any(CreditFacilityTypeTemp.class)))
            .thenReturn(new CreditFacilityTypeTemp());
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName(
            tempCreditFacilityTypeDTO.getFacilityTypeName()))
            .thenReturn(false);
    when(creditFacilityTypeRepository.existsByFacilityTypeName(
            tempCreditFacilityTypeDTO.getFacilityTypeName()))
            .thenReturn(false);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.saveCreditFacilityTypeTemp(tempCreditFacilityTypeDTO);

    CreditFacilityTypeDTO responseDto =
            (CreditFacilityTypeDTO) Objects.requireNonNull(response.getBody()).getResponse();
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    assertEquals(
            tempCreditFacilityTypeDTO.getFacilityTypeName(), responseDto.getFacilityTypeName());
  }

  @Test
  void testSaveCreditFacilityTypeTemp_Failure_NameEmpty() {

    CreditFacilityTypeDTO tempCreditFacilityTypeDTOWithOutName = tempCreditFacilityTypeDTO;
    tempCreditFacilityTypeDTOWithOutName.setFacilityTypeName(null);
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.saveCreditFacilityTypeTemp(
                                    tempCreditFacilityTypeDTOWithOutName));

    assertEquals("Facility Type Name Cannot be null or empty", exception.getMessage());
  }

  @Test
  void testSaveCreditFacilityTypeTemp_Failure_NameAlreadyExistsInTemp() {

    when(creditFacilityTypeTempRepository.existsByFacilityTypeName(
            tempCreditFacilityTypeDTO.getFacilityTypeName()))
            .thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.saveCreditFacilityTypeTemp(tempCreditFacilityTypeDTO));
    String expectedMessage =
            (CREDIT_FACILITY_TYPE_WITH
                    + tempCreditFacilityTypeDTO.getFacilityTypeName()
                    + ALL_READY_EXISTS
                    + TEMP_TABLE);
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testSaveCreditFacilityTypeTemp_Failure_NameAlreadyExistsInMaster() {

    when(creditFacilityTypeTempRepository.existsByFacilityTypeName(
            tempCreditFacilityTypeDTO.getFacilityTypeName()))
            .thenReturn(false);
    when(creditFacilityTypeRepository.existsByFacilityTypeName(
            tempCreditFacilityTypeDTO.getFacilityTypeName()))
            .thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.saveCreditFacilityTypeTemp(tempCreditFacilityTypeDTO));

    String expectedMessage =
            (CREDIT_FACILITY_TYPE_WITH
                    + tempCreditFacilityTypeDTO.getFacilityTypeName()
                    + ALL_READY_EXISTS
                    + MASTER_TABLE);
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testSaveCreditFacilityTypeTemp_Failure_UnhandledException() {
    when(creditFacilityTypeTempRepository.getCurrentSequenceValue())
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.saveCreditFacilityTypeTemp(tempCreditFacilityTypeDTO));

    assertEquals("Unable to Add Credit Facility Type", exception.getMessage());
  }

  /** Test cases for find Credit Facility Type Temp By ID */
  @Test
  void testFindCreditFacilityTypeTempByID_Success() throws ApiRequestException {

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.findCreditFacilityTypeTempByID(1);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());
    assertNotNull(response.getBody().getResponse());
    CreditFacilityTypeDTO responseDto = (CreditFacilityTypeDTO) response.getBody().getResponse();
    assertEquals(creditFacilityTypeTemp.getFacilityTypeName(), responseDto.getFacilityTypeName());
  }

  @Test
  void testFindCreditFacilityTypeTempByID_NotFound() {

    when(creditFacilityTypeTempRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.findCreditFacilityTypeTempByID(2));

    assertEquals((CREDIT_FACILITY_TYPE_WITH + "2" + DOES_NOT_EXISTS), exception.getMessage());
  }

  @Test
  void testFindCreditFacilityTypeTempByID_UnhandledException() {

    when(creditFacilityTypeTempRepository.findById(anyInt()))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.findCreditFacilityTypeTempByID(1));

    assertEquals("Unable to Fetch Credit Facility Type", exception.getMessage());
  }

  @Test
  void testFindCreditFacilityTypeTempByID_Success_VerifiesAllMappedFields()
          throws ApiRequestException {

    when(creditFacilityTypeTempRepository.findById(30))
            .thenReturn(Optional.of(creditFacilityTypeTemp));

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.findCreditFacilityTypeTempByID(30);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    CreditFacilityTypeDTO responseDto =
            (CreditFacilityTypeDTO) Objects.requireNonNull(response.getBody()).getResponse();
    assertEquals(
            creditFacilityTypeTemp.getCreditFacilityTypeID(), responseDto.getCreditFacilityTypeID());
    assertEquals(creditFacilityTypeTemp.getDescription(), responseDto.getDescription());
    assertEquals(creditFacilityTypeTemp.getApproveStatus(), responseDto.getApproveStatus());
    assertEquals(creditFacilityTypeTemp.getStatus(), responseDto.getStatus());

    verify(creditFacilityTypeTempRepository).findById(30);
  }

  @Test
  void testFindCreditFacilityTypeTempByID_WithDifferentValidID() throws ApiRequestException {

    CreditFacilityTypeTemp anotherTemp = new CreditFacilityTypeTemp();
    anotherTemp.setCreditFacilityTypeID(99);
    anotherTemp.setFacilityTypeName("Overdraft");
    anotherTemp.setDescription("Overdraft Temp Description");
    anotherTemp.setApproveStatus(MasterDataApproveStatus.PENDING);

    when(creditFacilityTypeTempRepository.findById(99)).thenReturn(Optional.of(anotherTemp));

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.findCreditFacilityTypeTempByID(99);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    CreditFacilityTypeDTO responseDto =
            (CreditFacilityTypeDTO) Objects.requireNonNull(response.getBody()).getResponse();
    assertEquals("Overdraft", responseDto.getFacilityTypeName());
    assertEquals(99, responseDto.getCreditFacilityTypeID());
    verify(creditFacilityTypeTempRepository, never()).findById(30);
  }

  /** Test cases for update Credit Facility Type Temp */
  @Test
  void testUpdateCreditFacilityTempType_Success() throws ApiRequestException {

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(false);
    when(creditFacilityTypeRepository.findCreditFacilityTypeByName("Temp DTO")).thenReturn(null);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.updateCreditFacilityTempType(1, tempCreditFacilityTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());

    CreditFacilityTypeDTO updatedDTO = (CreditFacilityTypeDTO) response.getBody().getResponse();
    assertNotNull(updatedDTO);
    assertEquals("Temp DTO", updatedDTO.getFacilityTypeName());
  }

  @Test
  void testUpdateCreditFacilityTempType_Failure_NameEmpty() {

    tempCreditFacilityTypeDTO.setFacilityTypeName("");

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateCreditFacilityTempType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Facility Type Name Cannot be null", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTempType_Failure_NotFound() {

    when(creditFacilityTypeTempRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateCreditFacilityTempType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility type with ID 1 does not exist", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTempType_Failure_NameInTemporaryRecords() {

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateCreditFacilityTempType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility Type Name Already in Temporary Records", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTempType_Success_NameInTemporaryRecordsUpdatedName() {

    creditFacilityTypeTemp.setFacilityTypeName("Temp DTO");

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(true);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.updateCreditFacilityTempType(1, tempCreditFacilityTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());

    CreditFacilityTypeDTO updatedDTO = (CreditFacilityTypeDTO) response.getBody().getResponse();
    assertNotNull(updatedDTO);
    assertEquals("Temp DTO", updatedDTO.getFacilityTypeName());
  }

  @Test
  void testUpdateCreditFacilityTempType_Failure_NameInMasterRecords() {

    CreditFacilityType masterRecord = new CreditFacilityType();
    masterRecord.setCreditFacilityTypeID(2);
    masterRecord.setFacilityTypeName("Temp DTO");

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(false);
    when(creditFacilityTypeRepository.findCreditFacilityTypeByName("Temp DTO"))
            .thenReturn(masterRecord);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateCreditFacilityTempType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility Type Name Already in Master Records", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTempType_Success_NameInMasterRecordsIdNotEqual() {

    CreditFacilityType masterRecord = new CreditFacilityType();
    masterRecord.setCreditFacilityTypeID(30);
    masterRecord.setFacilityTypeName("Temp DTO");

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(false);
    when(creditFacilityTypeRepository.findCreditFacilityTypeByName("Temp DTO"))
            .thenReturn(masterRecord);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.updateCreditFacilityTempType(1, tempCreditFacilityTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());

    CreditFacilityTypeDTO updatedDTO = (CreditFacilityTypeDTO) response.getBody().getResponse();
    assertNotNull(updatedDTO);
    assertEquals("Temp DTO", updatedDTO.getFacilityTypeName());
  }

  @Test
  void testUpdateCreditFacilityTempType_Failure_UnhandledException() {

    when(creditFacilityTypeTempRepository.findById(1))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateCreditFacilityTempType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Unable to Update Credit Facility Type", exception.getMessage());
  }

  /** Test cases for Approve Reject Credit Facility Type Temp */
  @Test
  void testApproveRejectCreditFacilityType_Success_ApproveNew() {

    creditFacilityType.setCreditFacilityTypeID(1);
    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTypeRepository.save(any(CreditFacilityType.class)))
            .thenReturn(creditFacilityType);
    when(creditFacilityTypeAudRepository.save(any(CreditFacilityTypeAud.class))).thenReturn(audit);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());
    assertNotNull(response.getBody().getResponse());

    CreditFacilityTypeDTO resDTO = (CreditFacilityTypeDTO) (response.getBody().getResponse());
    assertEquals("Loan", resDTO.getFacilityTypeName());
    verify(creditFacilityTypeTempRepository).delete(creditFacilityTypeTemp);
  }

  @Test
  void testApproveRejectCreditFacilityType_Success_ApproveNewIdNotEqual() {
    creditFacilityType.setCreditFacilityTypeID(2);
    creditFacilityType.setApproveStatus(MasterDataApproveStatus.APPROVED);

    approveRejectRQ.setApproveRejectDataID(1);
    creditFacilityTypeTemp.setCreditFacilityTypeID(1);
    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeRepository.findById(1))
            .thenReturn(Optional.ofNullable(creditFacilityType));
    when(creditFacilityTypeRepository.save(any(CreditFacilityType.class)))
            .thenReturn(creditFacilityType);
    when(creditFacilityTypeAudRepository.save(any(CreditFacilityTypeAud.class))).thenReturn(audit);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    assertNotNull(response.getBody().getResponse());

    CreditFacilityTypeDTO resDTO = (CreditFacilityTypeDTO) (response.getBody().getResponse());
    assertEquals("Loan", resDTO.getFacilityTypeName());
    verify(creditFacilityTypeTempRepository).delete(creditFacilityTypeTemp);
  }

  @Test
  void testApproveRejectCreditFacilityType_Success_ApproveExsisting() {
    creditFacilityType.setCreditFacilityTypeID(1);
    creditFacilityType.setApproveStatus(MasterDataApproveStatus.APPROVED);

    creditFacilityType.setCreditFacilityTypeID(1);
    approveRejectRQ.setApproveRejectDataID(1);
    creditFacilityTypeTemp.setCreditFacilityTypeID(1);
    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeRepository.findById(1))
            .thenReturn(Optional.ofNullable(creditFacilityType));
    when(creditFacilityTypeRepository.save(any(CreditFacilityType.class)))
            .thenReturn(creditFacilityType);
    when(creditFacilityTypeAudRepository.save(any(CreditFacilityTypeAud.class))).thenReturn(audit);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    assertNotNull(response.getBody().getResponse());

    CreditFacilityTypeDTO resDTO = (CreditFacilityTypeDTO) (response.getBody().getResponse());
    assertEquals("Loan", resDTO.getFacilityTypeName());
    verify(creditFacilityTypeTempRepository).delete(creditFacilityTypeTemp);
  }

  @Test
  void testApproveRejectCreditFacilityType_Success_Reject() {

    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeAudRepository.save(any(CreditFacilityTypeAud.class)))
            .thenAnswer(
                    invocation -> {
                      audit = invocation.getArgument(0);
                      audit.setId(1);
                      return audit;
                    });

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    assertNotNull(response.getBody().getResponse());
    CreditFacilityTypeDTO resDTO = (CreditFacilityTypeDTO) (response.getBody().getResponse());
    assertEquals("Loan", resDTO.getFacilityTypeName());

    verify(creditFacilityTypeAudRepository).save(any(CreditFacilityTypeAud.class));

    ArgumentCaptor<CreditFacilityTypeAud> auditCaptor =
            ArgumentCaptor.forClass(CreditFacilityTypeAud.class);
    verify(creditFacilityTypeAudRepository).save(auditCaptor.capture());

    CreditFacilityTypeAud capturedAudit = auditCaptor.getValue();

    assertNotNull(capturedAudit);
    assertEquals(1, capturedAudit.getId());
    assertEquals(
            creditFacilityTypeTemp.getCreditFacilityTypeID(), capturedAudit.getCreditFacilityTypeID());
    assertEquals(creditFacilityTypeTemp.getStatus(), capturedAudit.getStatus());
    assertEquals(creditFacilityTypeTemp.getApproveStatus(), capturedAudit.getApproveStatus());
    assertEquals(creditFacilityTypeTemp.getFacilityTypeName(), capturedAudit.getFacilityTypeName());
    assertEquals(creditFacilityTypeTemp.getDescription(), capturedAudit.getDescription());
  }

  @Test
  void testApproveRejectCreditFacilityType_InvalidRequest() {

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.approveRejectCreditFacilityType(null));

    assertEquals(
            "Invalid ApproveRejectRQ for creditfacilitytype: CreditFacilityTypeID cannot be null",
            exception.getMessage());
  }

  @Test
  void testApproveRejectCreditFacilityType_InvalidRequestWithApproveStatus() {

    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.DRAFT);
    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ));

    assertEquals("Not a valid approval status", exception.getMessage());
  }

  @Test
  void testApproveRejectCreditFacilityType_InvalidRequestIdNull() {

    approveRejectRQ.setApproveRejectDataID(null);
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ));

    assertEquals(
            "Invalid ApproveRejectRQ for creditfacilitytype: CreditFacilityTypeID cannot be null",
            exception.getMessage());
  }

  @Test
  void testApproveRejectCreditFacilityType_RecordNotFound() {

    when(creditFacilityTypeTempRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ));

    assertEquals("Credit Facility Type with ID 1 does not exists", exception.getMessage());
  }

  @Test
  void testApproveRejectCreditFacilityType_UnhandledException() {

    when(creditFacilityTypeTempRepository.findById(1))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ));

    assertEquals(APPROVE_REJECT_FAIL, exception.getMessage());
  }

  @Test
  void testApproveRejectCreditFacilityType_Failure_ApproveNew() {

    creditFacilityType.setCreditFacilityTypeID(1);
    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTypeRepository.save(any(CreditFacilityType.class)))
            .thenReturn(creditFacilityType);

    when(creditFacilityTypeAudRepository.save(any(CreditFacilityTypeAud.class)))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ));

    assertEquals(APPROVE_REJECT_FAIL, exception.getMessage());
  }

  @Test
  void testApproveRejectCreditFacilityType_Failure_Reject() {

    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));
    when(creditFacilityTypeAudRepository.save(any(CreditFacilityTypeAud.class)))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ));

    assertEquals(APPROVE_REJECT_FAIL, exception.getMessage());
  }

  /** Test cases for update approved Credit Facility Type Temp */
  @Test
  void testUpdateApprovedCreditFacilityType_Success() throws ApiRequestException {

    creditFacilityType.setCreditFacilityTypeID(1);

    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));
    when(creditFacilityTypeTempRepository.existsByCreditFacilityTypeID(1)).thenReturn(false);
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(false);
    when(creditFacilityTypeRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(false);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.updateApprovedCreditFacilityType(1, tempCreditFacilityTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    assertNotNull(response.getBody().getResponse());

    CreditFacilityTypeDTO resDTO = (CreditFacilityTypeDTO) (response.getBody().getResponse());

    assertEquals("Temp DTO", resDTO.getFacilityTypeName());
  }

  @Test
  void testUpdateApprovedCreditFacilityType_Failure_NameEmpty() {

    tempCreditFacilityTypeDTO.setFacilityTypeName("");

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateApprovedCreditFacilityType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Facility Type Name Cannot be null", exception.getMessage());
  }

  @Test
  void testUpdateApprovedCreditFacilityType_Failure_RecordNotFound() {

    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateApprovedCreditFacilityType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility Type with ID 1 does not exists", exception.getMessage());
  }

  @Test
  void testUpdateApprovedCreditFacilityType_Failure_RecordInTemp() {

    creditFacilityType.setCreditFacilityTypeID(1);
    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));
    when(creditFacilityTypeTempRepository.existsByCreditFacilityTypeID(1)).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateApprovedCreditFacilityType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility Type Already in Temporary Records", exception.getMessage());
  }

  @Test
  void testUpdateApprovedCreditFacilityType_Failure_NameInTempRecords() {

    creditFacilityType.setFacilityTypeName(tempCreditFacilityTypeDTO.getFacilityTypeName());
    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateApprovedCreditFacilityType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility Type Name Already in Temporary Records", exception.getMessage());
  }

  @Test
  void testUpdateApprovedCreditFacilityType_Failure_NameInMasterRecords() {

    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(false);
    when(creditFacilityTypeRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateApprovedCreditFacilityType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility Type Name Already in Master Records", exception.getMessage());
  }

  @Test
  void testUpdateApprovedCreditFacilityType_Failure_UnhandledException() {

    when(creditFacilityTypeRepository.findById(1))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.updateApprovedCreditFacilityType(
                                    1, tempCreditFacilityTypeDTO));

    assertEquals("Unable to Update Credit Facility Type", exception.getMessage());
  }

  @Test
  void testUpdateApprovedCreditFacilityType_Success_SameName() throws ApiRequestException {

    creditFacilityType.setCreditFacilityTypeID(1);
    creditFacilityType.setFacilityTypeName(tempCreditFacilityTypeDTO.getFacilityTypeName());
    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));
    when(creditFacilityTypeTempRepository.existsByCreditFacilityTypeID(1)).thenReturn(false);
    when(creditFacilityTypeTempRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(false);
    when(creditFacilityTypeRepository.existsByFacilityTypeName("Temp DTO")).thenReturn(true);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.updateApprovedCreditFacilityType(1, tempCreditFacilityTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    assertNotNull(response.getBody().getResponse());

    CreditFacilityTypeDTO resDTO = (CreditFacilityTypeDTO) (response.getBody().getResponse());

    assertEquals("Temp DTO", resDTO.getFacilityTypeName());
  }

  /** Test cases for Search Credit Facility Type list */
  @Test
  void testSearchCreditFacilityTypes_Success() throws ApiRequestException {

    Pageable pageable = PageRequest.of(0, 10);
    Page<CreditFacilityType> creditFacilityTypePage =
            new PageImpl<>(Collections.singletonList(creditFacilityType));
    when(creditFacilityTypeRepository.findAll(pageable)).thenReturn(creditFacilityTypePage);

    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> response =
            creditFacilityTypeService.searchCreditFacilityTypes(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());

    Page<CreditFacilityType> responsePage =
            (Page<CreditFacilityType>) response.getBody().getResponse();

    assertNotNull(responsePage);
    assertEquals("Loan", responsePage.getContent().get(0).getFacilityTypeName());
  }

  @Test
  void testSearchCreditFacilityTypes_EmptyResult() throws ApiRequestException {

    Pageable pageable = PageRequest.of(0, 10);
    when(creditFacilityTypeRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(new ArrayList<>()));

    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> response =
            creditFacilityTypeService.searchCreditFacilityTypes(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    Page<CreditFacilityType> responsePage =
            (Page<CreditFacilityType>) response.getBody().getResponse();
    assertEquals(0, responsePage.getContent().size());
  }

  @Test
  void testSearchCreditFacilityTypes_UnhandledException() {

    Pageable pageable = PageRequest.of(0, 10);
    when(creditFacilityTypeRepository.findAll(pageable))
            .thenThrow(new RuntimeException("Database error"));

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> creditFacilityTypeService.searchCreditFacilityTypes(pageable));

    assertEquals("Database error", exception.getMessage());
  }

  @Test
  void testSearchCreditFacilityTypes_VerifiesRepositoryCalledWithGivenPageable()
          throws ApiRequestException {

    Pageable pageable = PageRequest.of(2, 5);
    Page<CreditFacilityType> creditFacilityTypePage =
            new PageImpl<>(Collections.singletonList(creditFacilityType));
    when(creditFacilityTypeRepository.findAll(pageable)).thenReturn(creditFacilityTypePage);

    creditFacilityTypeService.searchCreditFacilityTypes(pageable);

    verify(creditFacilityTypeRepository).findAll(pageable);
  }

  @Test
  void testSearchCreditFacilityTypes_MultipleItems() throws ApiRequestException {

    CreditFacilityType secondType = new CreditFacilityType();
    secondType.setCreditFacilityTypeID(31);
    secondType.setFacilityTypeName("Overdraft");
    secondType.setDescription("Overdraft Master");

    Pageable pageable = PageRequest.of(0, 10);
    Page<CreditFacilityType> creditFacilityTypePage =
            new PageImpl<>(Arrays.asList(creditFacilityType, secondType));
    when(creditFacilityTypeRepository.findAll(pageable)).thenReturn(creditFacilityTypePage);

    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> response =
            creditFacilityTypeService.searchCreditFacilityTypes(pageable);

    Page<CreditFacilityType> responsePage =
            (Page<CreditFacilityType>) Objects.requireNonNull(response.getBody()).getResponse();

    assertEquals(2, responsePage.getContent().size());
    assertEquals("Overdraft", responsePage.getContent().get(1).getFacilityTypeName());
  }

  /** Test cases for Search Credit Facility Type by ID */
  @Test
  void testFindCreditFacilityTypeByID_Success() throws ApiRequestException {

    creditFacilityType.setCreditFacilityTypeID(1);
    CreditFacilityTypeDTO expectedDTO = new CreditFacilityTypeDTO(creditFacilityType);
    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));
    when(modelMapper.map(creditFacilityType, CreditFacilityTypeDTO.class)).thenReturn(expectedDTO);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.findCreditFacilityTypeByID(1);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());
    CreditFacilityTypeDTO responseDTO = (CreditFacilityTypeDTO) response.getBody().getResponse();
    assertEquals(creditFacilityType.getFacilityTypeName(), responseDTO.getFacilityTypeName());
  }

  @Test
  void testFindCreditFacilityTypeByID_NotFound() {

    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.findCreditFacilityTypeByID(1));

    assertEquals("Credit Facility Type with ID 1 does not exists", exception.getMessage());
  }

  @Test
  void testFindCreditFacilityTypeByID_UnhandledException() {

    when(creditFacilityTypeRepository.findById(1))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.findCreditFacilityTypeByID(1));

    assertEquals("Unable to Fetch Credit Facility Type", exception.getMessage());
  }

  @Test
  void testFindCreditFacilityTypeByID_VerifiesModelMapperInvokedWithCorrectArguments()
          throws ApiRequestException {

    creditFacilityType.setCreditFacilityTypeID(30);
    CreditFacilityTypeDTO expectedDTO = new CreditFacilityTypeDTO(creditFacilityType);
    when(creditFacilityTypeRepository.findById(30)).thenReturn(Optional.of(creditFacilityType));
    when(modelMapper.map(creditFacilityType, CreditFacilityTypeDTO.class)).thenReturn(expectedDTO);

    creditFacilityTypeService.findCreditFacilityTypeByID(30);

    verify(creditFacilityTypeRepository).findById(30);
    verify(modelMapper).map(creditFacilityType, CreditFacilityTypeDTO.class);
  }

  @Test
  void testFindCreditFacilityTypeByID_WithDifferentID() throws ApiRequestException {

    CreditFacilityType anotherType = new CreditFacilityType();
    anotherType.setCreditFacilityTypeID(42);
    anotherType.setFacilityTypeName("Term Loan");
    anotherType.setDescription("Term Loan Master Record");

    CreditFacilityTypeDTO expectedDTO = new CreditFacilityTypeDTO(anotherType);
    when(creditFacilityTypeRepository.findById(42)).thenReturn(Optional.of(anotherType));
    when(modelMapper.map(anotherType, CreditFacilityTypeDTO.class)).thenReturn(expectedDTO);

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response =
            creditFacilityTypeService.findCreditFacilityTypeByID(42);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    CreditFacilityTypeDTO responseDTO =
            (CreditFacilityTypeDTO) Objects.requireNonNull(response.getBody()).getResponse();
    assertEquals("Term Loan", responseDTO.getFacilityTypeName());
    assertEquals(42, responseDTO.getCreditFacilityTypeID());
  }

  /** Test cases for Search Credit Facility Type List in Temp */
  @Test
  void testFindAllCreditFacilityTypeTempList_Success() throws ApiRequestException {

    Pageable pageable = PageRequest.of(0, 10);
    Page<CreditFacilityTypeTemp> creditFacilityTypeTempPage =
            new PageImpl<>(Collections.singletonList(creditFacilityTypeTemp));

    when(creditFacilityTypeTempRepository.findAll(pageable))
            .thenReturn(creditFacilityTypeTempPage);

    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> response =
            creditFacilityTypeService.findAllCreditFacilityTypeTempList(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());

    Page<CreditFacilityTypeTemp> responsePage =
            (Page<CreditFacilityTypeTemp>) response.getBody().getResponse();
    assertEquals("Loan", responsePage.getContent().get(0).getFacilityTypeName());
  }

  @Test
  void testFindAllCreditFacilityTypeTempList_EmptyResult() throws ApiRequestException {

    Pageable pageable = PageRequest.of(0, 10);
    when(creditFacilityTypeTempRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(new ArrayList<>()));

    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> response =
            creditFacilityTypeService.findAllCreditFacilityTypeTempList(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    Page<CreditFacilityTypeTemp> responsePage =
            (Page<CreditFacilityTypeTemp>) response.getBody().getResponse();
    assertEquals(0, responsePage.getContent().size());
  }

  @Test
  void testFindAllCreditFacilityTypeTempList_UnhandledException() {

    Pageable pageable = PageRequest.of(0, 10);
    when(creditFacilityTypeTempRepository.findAll(pageable))
            .thenThrow(new RuntimeException("Database error"));

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> creditFacilityTypeService.findAllCreditFacilityTypeTempList(pageable));

    assertEquals("Database error", exception.getMessage());
  }

  @Test
  void testFindAllCreditFacilityTypeTempList_VerifiesRepositoryCalledWithGivenPageable()
          throws ApiRequestException {

    Pageable pageable = PageRequest.of(1, 20);
    Page<CreditFacilityTypeTemp> creditFacilityTypeTempPage =
            new PageImpl<>(Collections.singletonList(creditFacilityTypeTemp));

    when(creditFacilityTypeTempRepository.findAll(pageable))
            .thenReturn(creditFacilityTypeTempPage);

    creditFacilityTypeService.findAllCreditFacilityTypeTempList(pageable);

    verify(creditFacilityTypeTempRepository).findAll(pageable);
  }

  @Test
  void testFindAllCreditFacilityTypeTempList_MultipleItems() throws ApiRequestException {

    CreditFacilityTypeTemp secondTemp = new CreditFacilityTypeTemp();
    secondTemp.setCreditFacilityTypeID(31);
    secondTemp.setFacilityTypeName("Overdraft Temp");
    secondTemp.setDescription("Overdraft Temp Description");

    Pageable pageable = PageRequest.of(0, 10);
    Page<CreditFacilityTypeTemp> creditFacilityTypeTempPage =
            new PageImpl<>(Arrays.asList(creditFacilityTypeTemp, secondTemp));

    when(creditFacilityTypeTempRepository.findAll(pageable))
            .thenReturn(creditFacilityTypeTempPage);

    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> response =
            creditFacilityTypeService.findAllCreditFacilityTypeTempList(pageable);

    Page<CreditFacilityTypeTemp> responsePage =
            (Page<CreditFacilityTypeTemp>) Objects.requireNonNull(response.getBody()).getResponse();

    assertEquals(2, responsePage.getContent().size());
    assertEquals("Overdraft Temp", responsePage.getContent().get(1).getFacilityTypeName());
  }

  /** Test cases for Delete Credit Facility Type */
  @Test
  void testDeleteCreditFacilityTypeTemp_Success() throws ApiRequestException {

    creditFacilityTypeTemp.setCreditFacilityTypeID(1);
    when(creditFacilityTypeTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTypeTemp));

    ResponseEntity<StandardResponse<Integer>> response =
            creditFacilityTypeService.deleteCreditFacilityTypeTemp(tempCreditFacilityTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody().getMessage());
    Integer responseDTO = (Integer) response.getBody().getResponse();
    assertEquals(1, responseDTO);

    verify(creditFacilityTypeTempRepository).deleteById(1);
  }

  @Test
  void testDeleteCreditFacilityTypeTemp_NotFound() {

    when(creditFacilityTypeTempRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.deleteCreditFacilityTypeTemp(tempCreditFacilityTypeDTO));

    assertEquals("Credit Facility Type with ID 1 does not exists", exception.getMessage());

    verify(creditFacilityTypeTempRepository, never()).deleteById(anyInt());
  }

  @Test
  void testDeleteCreditFacilityTypeTemp_UnhandledException() {

    when(creditFacilityTypeTempRepository.findById(1))
            .thenThrow(new RuntimeException("Database error"));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityTypeService.deleteCreditFacilityTypeTemp(tempCreditFacilityTypeDTO));

    assertEquals("Deletion failed", exception.getMessage());

    verify(creditFacilityTypeTempRepository, never()).deleteById(anyInt());
  }

  @Test
  void testDeleteCreditFacilityTypeTemp_VerifyCorrectIdUsedForDeletion()
          throws ApiRequestException {

    CreditFacilityTypeTemp anotherTemp = new CreditFacilityTypeTemp();
    anotherTemp.setCreditFacilityTypeID(77);
    anotherTemp.setFacilityTypeName("Bridge Loan");

    CreditFacilityTypeDTO deleteDto = new CreditFacilityTypeDTO();
    deleteDto.setCreditFacilityTypeID(77);

    when(creditFacilityTypeTempRepository.findById(77)).thenReturn(Optional.of(anotherTemp));

    ResponseEntity<StandardResponse<Integer>> response =
            creditFacilityTypeService.deleteCreditFacilityTypeTemp(deleteDto);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Integer responseId = (Integer) Objects.requireNonNull(response.getBody()).getResponse();
    assertEquals(77, responseId);

    verify(creditFacilityTypeTempRepository).deleteById(77);
    verify(creditFacilityTypeTempRepository, never()).deleteById(1);
  }

  @Test
  void testDeleteCreditFacilityTypeTemp_NullDTO_ThrowsApiRequestException() {

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityTypeService.deleteCreditFacilityTypeTemp(null));

    assertEquals("Deletion failed", exception.getMessage());

    verify(creditFacilityTypeTempRepository, never()).deleteById(anyInt());
  }
}