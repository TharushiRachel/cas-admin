package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.querydsl.core.BooleanBuilder;
import java.util.*;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upcsection.UpcSectionDTO;
import lk.sampath.casadminportalms.entity.upcsection.UpcSection;
import lk.sampath.casadminportalms.entity.upcsection.UpcSectionAud;
import lk.sampath.casadminportalms.entity.upcsection.UpcSectionTemp;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionAudRepository;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionRepository;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionTempRepository;
import lk.sampath.casadminportalms.service.impl.UpcSectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UpcSectionServiceImplTest {

  @Mock private UpcSectionTempRepository upcSectionTempRepository;

  @Mock private UpcSectionRepository upcSectionRepository;

  @InjectMocks private UpcSectionServiceImpl upcSectionService;

  @Mock private UpcSectionAudRepository upcSectionAudRepository;

  private ApproveRejectRQ approveRejectRQ;

  private UpcSectionTemp upcSectionTemp;

  private UpcSection upcSection;

  private UpcSectionDTO upcSectionDTO;

  private Pageable pageable;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    pageable = PageRequest.of(0, 10);

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
    upcSection.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  /** Test cases for findAllUpcSectionTempList() * */
  @Test
  void testFindAllUpcSectionTempList_StatusCode() throws ApiRequestException {
    upcSectionTemp.setUpcSectionID(1);
    List<UpcSectionTemp> upcSectionList = Arrays.asList(upcSectionTemp);
    Page<UpcSectionTemp> upcSectionPage = new PageImpl<>(upcSectionList);

    Mockito.when(upcSectionTempRepository.findAll(pageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllUpcSectionTempList(pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testFindAllUpcSectionTempList_EmptyList() throws ApiRequestException {
    Page<UpcSectionTemp> upcSectionPage = new PageImpl<>(Collections.emptyList());

    Mockito.when(upcSectionTempRepository.findAll(pageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllUpcSectionTempList(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testFindAllUpcSectionTempList_NullData() throws ApiRequestException {
    Mockito.when(upcSectionTempRepository.findAll(pageable)).thenReturn(null);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllUpcSectionTempList(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertNull(response.getBody().getResponse());
  }

  @Test
  void testFindAllUpcSectionTempList_ResponseMessage() throws ApiRequestException {
    upcSectionTemp.setUpcSectionID(1);
    upcSectionTemp.setUpcSectionName("Test Section");
    List<UpcSectionTemp> upcSectionList = Arrays.asList(upcSectionTemp);
    Page<UpcSectionTemp> upcSectionPage = new PageImpl<>(upcSectionList);

    Mockito.when(upcSectionTempRepository.findAll(pageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllUpcSectionTempList(pageable);

    assertEquals("Success", response.getBody().getMessage());
  }

  @Test
  void testFindAllUpcSectionTempList_VerifiesRepositoryInvokedWithGivenPageable()
          throws ApiRequestException {
    Pageable customPageable = PageRequest.of(2, 5);
    Page<UpcSectionTemp> upcSectionPage = new PageImpl<>(Collections.emptyList());

    Mockito.when(upcSectionTempRepository.findAll(customPageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllUpcSectionTempList(customPageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
    verify(upcSectionTempRepository, times(1)).findAll(customPageable);
  }

  /** Test cases for findUpcSectionTempByID() * */
  @Test
  void testFindUpcSectionTempByID_Success() throws ApiRequestException {
    Integer upcSectionID = 1;
    upcSectionTemp.setUpcSectionID(upcSectionID);
    upcSectionTemp.setUpcSectionName("Test Section");
    upcSectionTemp.setUpcSectionDescription("Test Description");

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(upcSectionTemp));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.findUpcSectionTempByID(upcSectionID);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testFindUpcSectionTempByID_StatusCode() throws ApiRequestException {
    Integer upcSectionID = 1;
    upcSectionTemp.setUpcSectionID(upcSectionID);

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(upcSectionTemp));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.findUpcSectionTempByID(upcSectionID);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testFindUpcSectionTempByID_NotFound_ThrowsException() {
    Integer upcSectionID = 99;

    Mockito.when(upcSectionTempRepository.findById(upcSectionID)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.findUpcSectionTempByID(upcSectionID));

    assertEquals("UPC Section with TEMP 99 does not exists", exception.getMessage());
  }

  @Test
  void testFindUpcSectionTempByID_ResponseMessage() throws ApiRequestException {
    Integer upcSectionID = 1;
    upcSectionTemp.setUpcSectionID(upcSectionID);

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(upcSectionTemp));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.findUpcSectionTempByID(upcSectionID);

    assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
    assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
  }

  @Test
  void testFindUpcSectionTempByID_VerifiesRepositoryInteraction() throws ApiRequestException {
    Integer upcSectionID = 1;
    upcSectionTemp.setUpcSectionID(upcSectionID);

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(upcSectionTemp));

    upcSectionService.findUpcSectionTempByID(upcSectionID);

    verify(upcSectionTempRepository, times(1)).findById(upcSectionID);
  }

  /** Test cases for findAllApprovedUpcSection() * */
  @Test
  void testFindAllApprovedUpcSection_EmptyList() {
    // Arrange
    Page<UpcSection> upcSectionPage = new PageImpl<>(Collections.emptyList());

    // Mock repository behavior to return an empty page
    Mockito.when(upcSectionRepository.findAll(pageable)).thenReturn(upcSectionPage);

    // Act
    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllApprovedUpcSection(pageable);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testFindAllApprovedUpcSection_FilteringApproved() {
    // Arrange
    UpcSection approvedSection = new UpcSection();
    approvedSection.setUpcSectionID(1);
    approvedSection.setUpcSectionName("Approved Section");
    approvedSection.setApproveStatus(MasterDataApproveStatus.APPROVED);

    UpcSection pendingSection = new UpcSection();
    pendingSection.setUpcSectionID(2);
    pendingSection.setUpcSectionName("Pending Section");
    pendingSection.setApproveStatus(MasterDataApproveStatus.PENDING);

    List<UpcSection> upcSectionList = Arrays.asList(approvedSection, pendingSection);
    Page<UpcSection> upcSectionPage = new PageImpl<>(upcSectionList);

    Mockito.when(upcSectionRepository.findAll(pageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllApprovedUpcSection(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testFindAllApprovedUpcSection_ResponseMessage() {
    upcSection.setUpcSectionID(1);
    upcSection.setUpcSectionName("Approved Section");
    upcSection.setApproveStatus(MasterDataApproveStatus.APPROVED);

    List<UpcSection> upcSectionList = Arrays.asList(upcSection);
    Page<UpcSection> upcSectionPage = new PageImpl<>(upcSectionList);

    Mockito.when(upcSectionRepository.findAll(pageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllApprovedUpcSection(pageable);

    assertEquals("Success", response.getBody().getMessage());
  }

  @Test
  void testFindAllApprovedUpcSection_StatusCode() {
    upcSection.setUpcSectionID(1);
    upcSection.setUpcSectionName("Approved Section");
    upcSection.setApproveStatus(MasterDataApproveStatus.APPROVED);

    List<UpcSection> upcSectionList = Arrays.asList(upcSection);
    Page<UpcSection> upcSectionPage = new PageImpl<>(upcSectionList);

    Mockito.when(upcSectionRepository.findAll(pageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllApprovedUpcSection(pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testFindAllApprovedUpcSection_VerifiesRepositoryInvokedWithGivenPageable() {
    Pageable customPageable = PageRequest.of(1, 20);
    Page<UpcSection> upcSectionPage = new PageImpl<>(Collections.emptyList());

    Mockito.when(upcSectionRepository.findAll(customPageable)).thenReturn(upcSectionPage);

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> response =
            upcSectionService.findAllApprovedUpcSection(customPageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(upcSectionRepository, times(1)).findAll(customPageable);
  }

  /** Test cases for findApprovedUpcSectionByID() * */
  @Test
  void testFindApprovedUpcSectionByID_StatusCode() {
    Integer upcSectionID = 1;
    upcSection.setUpcSectionID(upcSectionID);
    upcSection.setUpcSectionName("Approved Section");
    upcSection.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.findApprovedUpcSectionByID(upcSectionID);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testFindApprovedUpcSectionByID_Success() {
    Integer upcSectionID = 1;
    upcSection.setUpcSectionID(upcSectionID);
    upcSection.setUpcSectionName("Approved Section");
    upcSection.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.findApprovedUpcSectionByID(upcSectionID);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testFindApprovedUpcSectionByID_ResponseStructure() {
    Integer upcSectionID = 1;
    upcSection.setUpcSectionID(upcSectionID);
    upcSection.setUpcSectionName("Approved Section");
    upcSection.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.findApprovedUpcSectionByID(upcSectionID);

    assertNotNull(response);
    assertNotNull(response.getBody());
  }

  @Test
  void testFindApprovedUpcSectionByID_NotFound_ThrowsException() {
    Integer upcSectionID = 99;

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.findApprovedUpcSectionByID(upcSectionID));

    assertEquals("UPC Section with 99 does not exists", exception.getMessage());
  }

  @Test
  void testFindApprovedUpcSectionByID_VerifiesRepositoryInteraction() {
    Integer upcSectionID = 1;
    upcSection.setUpcSectionID(upcSectionID);

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));

    upcSectionService.findApprovedUpcSectionByID(upcSectionID);

    verify(upcSectionRepository, times(1)).findById(upcSectionID);
  }

  /** Test cases for saveUpcSectionTemp() * */
  @Test
  void testSaveUpcSectionTemp_Success() {
    upcSectionDTO.setUpcSectionName("Test Section");
    upcSectionDTO.setUpcSectionDescription("Test Description");
    upcSectionDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
    upcSectionDTO.setCreatedBy("Tester");
    upcSectionDTO.setModifiedBy("Tester");

    upcSectionTemp.setUpcSectionID(1);
    upcSectionTemp.setUpcSectionName("Test Section");
    upcSectionTemp.setStatus(Status.ACT);

    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    Mockito.when(upcSectionTempRepository.getCurrentSequenceValue()).thenReturn(1);
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenReturn(upcSectionTemp);

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.saveUpcSectionTemp(upcSectionDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testSaveUpcSectionTemp_AlreadyExistsMessage() {
    upcSectionDTO.setUpcSectionName("Duplicate Section");

    UpcSectionTemp existingUpcSectionTemp = new UpcSectionTemp();
    existingUpcSectionTemp.setUpcSectionName("Duplicate Section");

    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.singletonList(existingUpcSectionTemp));

    try {
      upcSectionService.saveUpcSectionTemp(upcSectionDTO);
      fail("Expected ApiRequestException not thrown");
    } catch (ApiRequestException e) {
      assertEquals("Upc Section Temp Already Exists", e.getMessage());
    }
  }

  @Test
  void testSaveUpcSectionTemp_NullDTO_ThrowsException() {
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class, () -> upcSectionService.saveUpcSectionTemp(null));

    assertEquals("Upc Section name cannot be empty or null.", exception.getMessage());
    verify(upcSectionTempRepository, never()).saveAndFlush(any(UpcSectionTemp.class));
  }

  @Test
  void testSaveUpcSectionTemp_BlankName_ThrowsException() {
    upcSectionDTO.setUpcSectionName("   ");

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class, () -> upcSectionService.saveUpcSectionTemp(upcSectionDTO));

    assertEquals("Upc Section name cannot be empty or null.", exception.getMessage());
    verify(upcSectionTempRepository, never()).saveAndFlush(any(UpcSectionTemp.class));
  }

  @Test
  void testSaveUpcSectionTemp_NullName_ThrowsException() {
    upcSectionDTO.setUpcSectionName(null);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class, () -> upcSectionService.saveUpcSectionTemp(upcSectionDTO));

    assertEquals("Upc Section name cannot be empty or null.", exception.getMessage());
  }

  @Test
  void testSaveUpcSectionTemp_VerifiesSavedFieldsViaArgumentCaptor() {
    upcSectionDTO.setUpcSectionName("Captured Section");
    upcSectionDTO.setUpcSectionDescription("Captured Description");
    upcSectionDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
    upcSectionDTO.setCreatedBy("Tester");
    upcSectionDTO.setModifiedBy("Tester");
    upcSectionDTO.setStatus(Status.ACT);

    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    Mockito.when(upcSectionTempRepository.getCurrentSequenceValue()).thenReturn(5);
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.saveUpcSectionTemp(upcSectionDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    ArgumentCaptor<UpcSectionTemp> captor = ArgumentCaptor.forClass(UpcSectionTemp.class);
    verify(upcSectionTempRepository, times(1)).saveAndFlush(captor.capture());

    UpcSectionTemp captured = captor.getValue();
    assertEquals(5, captured.getUpcSectionID());
    assertEquals("Captured Section", captured.getUpcSectionName());
    assertEquals("Captured Description", captured.getUpcSectionDescription());
    assertEquals(Status.ACT, captured.getStatus());
    assertEquals("Tester", captured.getCreatedBy());
    assertEquals("Tester", captured.getModifiedBy());
  }

  /** Test cases for approveRejectUpcSection() * */
  @Test
  void testApproveRejectUpcSection_ApproveSuccess() {

    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    upcSectionTemp.setUpcSectionID(1);
    upcSectionTemp.setUpcSectionName("Test Section Temp");
    upcSectionTemp.setApproveStatus(MasterDataApproveStatus.PENDING);

    upcSection.setUpcSectionID(1);
    upcSection.setUpcSectionName("Test Section");

    Mockito.when(upcSectionTempRepository.findById(1)).thenReturn(Optional.of(upcSectionTemp));
    Mockito.when(upcSectionRepository.findById(1)).thenReturn(Optional.of(upcSection));
    Mockito.when(upcSectionRepository.saveAndFlush(Mockito.any(UpcSection.class)))
            .thenReturn(upcSection);
    Mockito.doNothing().when(upcSectionTempRepository).delete(upcSectionTemp);
    Mockito.when(upcSectionAudRepository.save(Mockito.any(UpcSectionAud.class))).thenReturn(null);

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approveRejectUpcSection(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testApproveRejectUpcSection_RejectSuccess() {
    approveRejectRQ.setApproveRejectDataID(2);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

    upcSectionTemp.setUpcSectionID(2);
    upcSectionTemp.setUpcSectionName("Test Section Temp");
    upcSectionTemp.setApproveStatus(MasterDataApproveStatus.PENDING);

    Mockito.when(upcSectionTempRepository.findById(2)).thenReturn(Optional.of(upcSectionTemp));
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenReturn(upcSectionTemp);
    Mockito.when(upcSectionAudRepository.save(Mockito.any(UpcSectionAud.class))).thenReturn(null);

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approveRejectUpcSection(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testApproveRejectUpcSection_NullRequest_ThrowsException() {
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class, () -> upcSectionService.approveRejectUpcSection(null));

    assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
  }

  @Test
  void testApproveRejectUpcSection_NullDataID_ThrowsException() {
    ApproveRejectRQ invalidRequest = new ApproveRejectRQ();

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.approveRejectUpcSection(invalidRequest));

    assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
  }

  @Test
  void testApproveRejectUpcSection_UpcSectionTempNotFound() {
    approveRejectRQ.setApproveRejectDataID(99);

    Mockito.when(upcSectionTempRepository.findById(99)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.approveRejectUpcSection(approveRejectRQ));

    assertEquals("UPC Section with TEMP 99 does not exists", exception.getMessage());
  }

  @Test
  void testApproveRejectUpcSection_UnknownStatus_ThrowsException() {
    approveRejectRQ.setApproveStatus(null);

    Mockito.when(upcSectionTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(upcSectionTemp));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.approveRejectUpcSection(approveRejectRQ));

    assertTrue(exception.getMessage().contains("Unknown approval status"));
  }

  /** Test cases for updateApprovedUpcSection() * */
  @Test
  void testUpdateApprovedUpcSection_Success() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(1);
    upcSectionDTO.setUpcSectionName("Updated Section");
    upcSectionDTO.setStatus(Status.ACT);
    upcSectionDTO.setCreatedDate(new Date());
    upcSectionDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);
    upcSectionDTO.setUpcSectionDescription("Updated Description");
    upcSectionDTO.setApprovedDate(new Date());
    upcSectionDTO.setApprovedBy("Admin");
    upcSectionDTO.setCreatedBy("Creator");
    upcSectionDTO.setModifiedBy("Modifier");

    upcSection.setUpcSectionID(1);
    upcSection.setUpcSectionName("Original Section");

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));
    Mockito.when(upcSectionTempRepository.exists(Mockito.any(BooleanBuilder.class)))
            .thenReturn(false);
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.updateApprovedUpcSection(upcSectionID, upcSectionDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testUpdateApprovedUpcSection_UpcSectionNotFound() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.updateApprovedUpcSection(upcSectionID, upcSectionDTO));
    assertEquals("UPC Section with 1 does not exists", exception.getMessage());
  }

  @Test
  void testUpdateApprovedUpcSection_DuplicateNameInTemp() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("Duplicate Name");

    upcSection.setUpcSectionID(upcSectionID);
    upcSection.setUpcSectionName("Existing Name"); // Set a valid name to avoid NPE

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));
    Mockito.when(upcSectionTempRepository.exists(Mockito.any(BooleanBuilder.class)))
            .thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.updateApprovedUpcSection(upcSectionID, upcSectionDTO));
    assertEquals(
            "UPC Section Name 'Duplicate Name' already exists in the system.", exception.getMessage());
  }

  @Test
  void testUpdateApprovedUpcSection_DuplicateNameInMaster() {
    // Arrange
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("Duplicate Name");

    upcSection.setUpcSectionID(upcSectionID);
    upcSection.setUpcSectionName("Existing Name"); // Prevent NullPointerException

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));
    Mockito.when(upcSectionTempRepository.exists(Mockito.any(BooleanBuilder.class)))
            .thenReturn(false);
    Mockito.when(upcSectionRepository.exists(Mockito.any(BooleanBuilder.class))).thenReturn(true);

    // Act & Assert
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.updateApprovedUpcSection(upcSectionID, upcSectionDTO));
    assertEquals(
            "UPC Section Name 'Duplicate Name' already exists in the system.", exception.getMessage());
  }

  @Test
  void testUpdateApprovedUpcSection_NotFound_DoesNotSaveTemp() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.updateApprovedUpcSection(upcSectionID, upcSectionDTO));

    assertEquals("UPC Section with 1 does not exists", exception.getMessage());
    verify(upcSectionTempRepository, never()).saveAndFlush(any(UpcSectionTemp.class));
  }

  @Test
  void testUpdateApprovedUpcSection_VerifiesMappedTempFieldsViaArgumentCaptor() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("Mapped Section");
    upcSectionDTO.setUpcSectionDescription("Mapped Description");
    upcSectionDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
    upcSectionDTO.setApprovedBy("Approver");
    upcSectionDTO.setCreatedBy("Creator");
    upcSectionDTO.setModifiedBy("Modifier");

    upcSection.setUpcSectionID(upcSectionID);
    upcSection.setUpcSectionName("Original Section");

    Mockito.when(upcSectionRepository.findById(upcSectionID)).thenReturn(Optional.of(upcSection));
    Mockito.when(upcSectionTempRepository.exists(Mockito.any(BooleanBuilder.class)))
            .thenReturn(false);
    Mockito.when(upcSectionRepository.exists(Mockito.any(BooleanBuilder.class)))
            .thenReturn(false);
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    upcSectionService.updateApprovedUpcSection(upcSectionID, upcSectionDTO);

    ArgumentCaptor<UpcSectionTemp> captor = ArgumentCaptor.forClass(UpcSectionTemp.class);
    verify(upcSectionTempRepository, times(1)).saveAndFlush(captor.capture());

    UpcSectionTemp captured = captor.getValue();
    assertEquals(upcSectionID, captured.getUpcSectionID());
    assertEquals("Mapped Section", captured.getUpcSectionName());
    assertEquals("Mapped Description", captured.getUpcSectionDescription());
    assertEquals(MasterDataApproveStatus.PENDING, captured.getApproveStatus());
    assertEquals("Approver", captured.getApprovedBy());
    assertEquals("Creator", captured.getCreatedBy());
  }

  /** Test cases for updateUpcSectionTemp() * */
  @Test
  void testUpdateUpcSectionTemp_Success() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("Updated Section");
    upcSectionDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
    upcSectionDTO.setUpcSectionDescription("Updated Description");
    upcSectionDTO.setApprovedDate(new Date());
    upcSectionDTO.setApprovedBy("Admin");
    upcSectionDTO.setCreatedBy("Creator");
    upcSectionDTO.setModifiedBy("Modifier");

    UpcSectionTemp existingUpcSectionTemp = new UpcSectionTemp();
    existingUpcSectionTemp.setUpcSectionID(upcSectionID);
    existingUpcSectionTemp.setUpcSectionName("Original Section");

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(existingUpcSectionTemp));
    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.updateUpcSectionTemp(upcSectionID, upcSectionDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testUpdateUpcSectionTemp_UpcSectionTempNotFound() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);

    Mockito.when(upcSectionTempRepository.findById(upcSectionID)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.updateUpcSectionTemp(upcSectionID, upcSectionDTO));
    assertEquals("UPC Section with TEMP 1 does not exists", exception.getMessage());
  }

  @Test
  void testUpdateUpcSectionTemp_DuplicateNameInTempRecords() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("Duplicate Name");

    UpcSectionTemp existingUpcSectionTemp = new UpcSectionTemp();
    existingUpcSectionTemp.setUpcSectionID(upcSectionID);
    existingUpcSectionTemp.setUpcSectionName("Original Section");

    UpcSectionTemp duplicateUpcSectionTemp = new UpcSectionTemp();
    duplicateUpcSectionTemp.setUpcSectionID(2);
    duplicateUpcSectionTemp.setUpcSectionName("Duplicate Name");

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(existingUpcSectionTemp));
    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.singletonList(duplicateUpcSectionTemp));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.updateUpcSectionTemp(upcSectionID, upcSectionDTO));
    assertEquals("UPC Section with TEMP Duplicate Name Already Exists", exception.getMessage());
  }

  @Test
  void testUpdateUpcSectionTemp_NoChangesInName() {

    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("Original Section");

    UpcSectionTemp existingUpcSectionTemp = new UpcSectionTemp();
    existingUpcSectionTemp.setUpcSectionID(upcSectionID);
    existingUpcSectionTemp.setUpcSectionName("Original Section");

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(existingUpcSectionTemp));
    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.updateUpcSectionTemp(upcSectionID, upcSectionDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testUpdateUpcSectionTemp_BlankNameUnchanged_ThrowsException() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("");

    UpcSectionTemp existingUpcSectionTemp = new UpcSectionTemp();
    existingUpcSectionTemp.setUpcSectionID(upcSectionID);
    existingUpcSectionTemp.setUpcSectionName("");

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(existingUpcSectionTemp));
    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> upcSectionService.updateUpcSectionTemp(upcSectionID, upcSectionDTO));

    assertEquals("Upc Section name cannot be empty or null.", exception.getMessage());
  }

  @Test
  void testUpdateUpcSectionTemp_VerifiesSavedFieldsViaArgumentCaptor() {
    Integer upcSectionID = 1;
    upcSectionDTO.setUpcSectionID(upcSectionID);
    upcSectionDTO.setUpcSectionName("Updated Section");
    upcSectionDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
    upcSectionDTO.setUpcSectionDescription("Updated Description");
    upcSectionDTO.setApprovedBy("Admin");
    upcSectionDTO.setCreatedBy("Creator");
    upcSectionDTO.setModifiedBy("Modifier");

    UpcSectionTemp existingUpcSectionTemp = new UpcSectionTemp();
    existingUpcSectionTemp.setUpcSectionID(upcSectionID);
    existingUpcSectionTemp.setUpcSectionName("Original Section");

    Mockito.when(upcSectionTempRepository.findById(upcSectionID))
            .thenReturn(Optional.of(existingUpcSectionTemp));
    Mockito.when(upcSectionTempRepository.findAll(Mockito.any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    Mockito.when(upcSectionTempRepository.saveAndFlush(Mockito.any(UpcSectionTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    upcSectionService.updateUpcSectionTemp(upcSectionID, upcSectionDTO);

    ArgumentCaptor<UpcSectionTemp> captor = ArgumentCaptor.forClass(UpcSectionTemp.class);
    verify(upcSectionTempRepository, times(1)).saveAndFlush(captor.capture());

    UpcSectionTemp captured = captor.getValue();
    assertEquals("Updated Section", captured.getUpcSectionName());
    assertEquals("Updated Description", captured.getUpcSectionDescription());
    assertEquals(MasterDataApproveStatus.PENDING, captured.getApproveStatus());
    assertEquals("Admin", captured.getApprovedBy());
    assertEquals("Creator", captured.getCreatedBy());
    assertEquals("Modifier", captured.getModifiedBy());
  }

  /** Test cases for deleteUpcSectionFormTemp() * */
  @Test
  void testDeleteUpcSectionFormTemp_Success() {
    Integer upcSectionID = 1;

    Mockito.doNothing().when(upcSectionTempRepository).deleteById(upcSectionID);

    ResponseEntity<StandardResponse<Void>> response =
            upcSectionService.deleteUpcSectionFormTemp(upcSectionID);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
    assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
  }

  @Test
  void testDeleteUpcSectionFormTemp_DatabaseException() {

    Integer upcSectionID = 1;

    Mockito.doThrow(new RuntimeException("Database Error"))
            .when(upcSectionTempRepository)
            .deleteById(upcSectionID);

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class, () -> upcSectionService.deleteUpcSectionFormTemp(upcSectionID));
    assertEquals("Database Error", exception.getMessage());
  }

  @Test
  void testDeleteUpcSectionFormTemp_ResponseStructure() {
    Integer upcSectionID = 1;

    Mockito.doNothing().when(upcSectionTempRepository).deleteById(upcSectionID);

    ResponseEntity<StandardResponse<Void>> response =
            upcSectionService.deleteUpcSectionFormTemp(upcSectionID);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(upcSectionID, response.getBody().getResponse());
  }

  @Test
  void testDeleteUpcSectionFormTemp_VerifiesRepositoryInteraction() {
    Integer upcSectionID = 5;

    Mockito.doNothing().when(upcSectionTempRepository).deleteById(upcSectionID);

    upcSectionService.deleteUpcSectionFormTemp(upcSectionID);

    verify(upcSectionTempRepository, times(1)).deleteById(upcSectionID);
  }

  @Test
  void testDeleteUpcSectionFormTemp_NullID_ThrowsException() {
    Mockito.doThrow(new IllegalArgumentException("The given id must not be null"))
            .when(upcSectionTempRepository)
            .deleteById(null);

    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> upcSectionService.deleteUpcSectionFormTemp(null));

    assertEquals("The given id must not be null", exception.getMessage());
  }

  @Test
  void testHandleApproval_CallsMapUpcSection_ForNewUser() throws ApiRequestException {
    when(upcSectionTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(upcSectionTemp));
    when(upcSectionRepository.findById(upcSectionTemp.getUpcSectionID()))
            .thenReturn(Optional.empty());
    when(upcSectionRepository.saveAndFlush(any(UpcSection.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approveRejectUpcSection(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upcSectionTempRepository, times(1)).delete(upcSectionTemp);
    verify(upcSectionRepository, times(1)).saveAndFlush(any(UpcSection.class));
  }

  @Test
  void testHandleApproval_CallsUpdateUpcSectionToMaster_ForExistingUser()
          throws ApiRequestException {
    when(upcSectionTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(upcSectionTemp));
    when(upcSectionRepository.findById(upcSectionTemp.getUpcSectionID()))
            .thenReturn(Optional.of(upcSection));
    when(upcSectionRepository.saveAndFlush(any(UpcSection.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approveRejectUpcSection(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upcSectionTempRepository, times(1)).delete(upcSectionTemp);
    verify(upcSectionRepository, times(1)).saveAndFlush(upcSection);
  }

  @Test
  void testApproveRejectUpcSection_RejectionSuccess_WithAudit() throws ApiRequestException {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

    when(upcSectionTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(upcSectionTemp));

    when(upcSectionAudRepository.save(any(UpcSectionAud.class)))
            .thenAnswer(
                    invocation -> {
                      UpcSectionAud audit = invocation.getArgument(0);
                      audit.setId(1);
                      return audit;
                    });

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approveRejectUpcSection(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upcSectionTempRepository, times(1)).findById(approveRejectRQ.getApproveRejectDataID());
    verify(upcSectionAudRepository, times(1)).save(any(UpcSectionAud.class));

    ArgumentCaptor<UpcSectionAud> auditCaptor = ArgumentCaptor.forClass(UpcSectionAud.class);
    verify(upcSectionAudRepository, times(1)).save(auditCaptor.capture());

    UpcSectionAud capturedAudit = auditCaptor.getValue();

    assertNotNull(capturedAudit);
    assertEquals(1, capturedAudit.getId());
    assertEquals(upcSectionTemp.getUpcSectionID(), capturedAudit.getUpcSectionID());
    assertEquals(upcSectionTemp.getStatus(), capturedAudit.getStatus());
    assertEquals(upcSectionTemp.getApproveStatus(), capturedAudit.getApproveStatus());
    assertEquals(
            upcSectionTemp.getUpcSectionDescription(), capturedAudit.getUpcSectionDescription());
    assertEquals(upcSectionTemp.getUpcSectionName(), capturedAudit.getUpcSectionName());
  }

  /** Test cases for approvedActiveList() * */
  @Test
  void testApprovedActiveList_Success() {
    UpcSection section = new UpcSection();
    section.setUpcSectionID(1);
    section.setUpcSectionName("Section One");
    section.setUpcSectionDescription("Description One");
    section.setStatus(Status.ACT);
    section.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Mockito.when(
                    upcSectionRepository.findByStatusAndApproveStatus(
                            Status.ACT, MasterDataApproveStatus.APPROVED))
            .thenReturn(Collections.singletonList(section));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approvedActiveList();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testApprovedActiveList_EmptyList() {
    Mockito.when(
                    upcSectionRepository.findByStatusAndApproveStatus(
                            Status.ACT, MasterDataApproveStatus.APPROVED))
            .thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approvedActiveList();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(Collections.emptyList(), response.getBody().getResponse());
  }

  @Test
  void testApprovedActiveList_VerifiesRepositoryCalledWithCorrectParameters() {
    Mockito.when(
                    upcSectionRepository.findByStatusAndApproveStatus(
                            Mockito.any(Status.class), Mockito.any(MasterDataApproveStatus.class)))
            .thenReturn(Collections.emptyList());

    upcSectionService.approvedActiveList();

    verify(upcSectionRepository, times(1))
            .findByStatusAndApproveStatus(Status.ACT, MasterDataApproveStatus.APPROVED);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testApprovedActiveList_MapsEntityFieldsToDTO() {
    UpcSection section = new UpcSection();
    section.setUpcSectionID(7);
    section.setUpcSectionName("Mapped Section");
    section.setUpcSectionDescription("Mapped Description");
    section.setStatus(Status.ACT);
    section.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Mockito.when(
                    upcSectionRepository.findByStatusAndApproveStatus(
                            Status.ACT, MasterDataApproveStatus.APPROVED))
            .thenReturn(Collections.singletonList(section));

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approvedActiveList();

    List<UpcSectionDTO> resultList = (List<UpcSectionDTO>) response.getBody().getResponse();

    assertEquals(1, resultList.size());
    assertEquals(7, resultList.get(0).getUpcSectionID());
    assertEquals("Mapped Section", resultList.get(0).getUpcSectionName());
    assertEquals("Mapped Description", resultList.get(0).getUpcSectionDescription());
  }

  @Test
  void testApprovedActiveList_ResponseMessageAndSuccessFlag() {
    Mockito.when(
                    upcSectionRepository.findByStatusAndApproveStatus(
                            Status.ACT, MasterDataApproveStatus.APPROVED))
            .thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<UpcSectionDTO>> response =
            upcSectionService.approvedActiveList();

    assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
    assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
  }
}