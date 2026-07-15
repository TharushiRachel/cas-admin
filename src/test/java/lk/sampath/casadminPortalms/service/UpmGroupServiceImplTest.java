package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.querydsl.core.BooleanBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroupTemp;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroupTempAud;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupRepository;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupTempAudRepository;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupTempRepository;
import lk.sampath.casadminportalms.service.impl.UpmGroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UpmGroupServiceImplTest {

  @Mock private UpmGroupTempRepository upmGroupTempRepository;

  @Mock private UpmGroupRepository upmGroupRepository;

  @Mock private UpmGroupTempAudRepository upmGroupTempAudRepository;

  @InjectMocks private UpmGroupServiceImpl upmGroupService;

  private UpmGroupTemp upmGroupTemp;

  private UpmGroupDTO upmGroupDTO;

  private UpmGroup upmGroup;

  private ApproveRejectRQ approveRejectRQ;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    upmGroupTemp = new UpmGroupTemp();
    upmGroupTemp.setUpmGroupID(1);
    upmGroupTemp.setGroupCode("TEST_CODE");
    upmGroupTemp.setReferenceName("Test Reference");
    upmGroupTemp.setWorkFlowLevel(1);
    upmGroupTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
    upmGroupTemp.setCreatedBy("Admin");
    upmGroupTemp.setModifiedBy("User1");

    upmGroup = new UpmGroup();
    upmGroup.setUpmGroupID(1);
    upmGroup.setGroupCode("TEST_CODE");
    upmGroup.setReferenceName("Test Reference");
    upmGroup.setWorkFlowLevel(1);
    upmGroup.setApproveStatus(MasterDataApproveStatus.APPROVED);
    upmGroup.setCreatedBy("Admin");
    upmGroup.setModifiedBy("User1");

    upmGroupDTO = new UpmGroupDTO();
    upmGroupDTO.setUpmGroupID(1);
    upmGroupDTO.setGroupCode("Old Unit Testing");
    upmGroupDTO.setReferenceName("Test Reference");
    upmGroupDTO.setWorkFlowLevel(1);
    upmGroupDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);
    upmGroupDTO.setCreatedBy("Admin");
    upmGroupDTO.setModifiedBy("User1");

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  /** findAllUpmGroupTempList * */
  @Test
  void testFindUpmGroupTempList_Success() throws Exception {
    List<UpmGroupTemp> upmGroupTempList = Arrays.asList(upmGroupTemp);
    when(upmGroupTempRepository.findAll()).thenReturn(upmGroupTempList);

    ResponseEntity<StandardResponse<List<UpmGroupDTO>>> response =
        upmGroupService.findAllUpmGroupTempList();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upmGroupTempRepository, times(1)).findAll();
  }

  @Test
  void testFindUpmGroupTempList_EmptyList() throws Exception {
    List<UpmGroupTemp> upmGroupTempList = Collections.emptyList();
    when(upmGroupTempRepository.findAll()).thenReturn(upmGroupTempList);

    ResponseEntity<StandardResponse<List<UpmGroupDTO>>> response =
        upmGroupService.findAllUpmGroupTempList();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upmGroupTempRepository, times(1)).findAll();
  }

  /** findUpmGroupTempByID * */
  @Test
  void testFindUpmGroupTempById_Success() throws ApiRequestException {
    when(upmGroupTempRepository.findById(upmGroupTemp.getUpmGroupID()))
        .thenReturn(Optional.of(upmGroupTemp));

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.findUpmGroupTempByID(upmGroupTemp.getUpmGroupID());

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upmGroupTempRepository, times(1)).findById(upmGroupTemp.getUpmGroupID());
  }

  @Test
  void testFindUpmGroupTempById_NotFound() {
    when(upmGroupTempRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.findUpmGroupTempByID((2));
            });

    assertEquals("UPM Group temp with 2Does not exists", exception.getMessage());

    verify(upmGroupTempRepository, times(1)).findById(2);
  }

  /** searchUpmGroups * */
  @Test
  void testFindAllApprovedUpmGroup_Success() {
    List<UpmGroup> upmGroupList = Arrays.asList(upmGroup);
    when(upmGroupRepository.findAll()).thenReturn(upmGroupList);

    ResponseEntity<StandardResponse<List<UpmGroupDTO>>> response =
        upmGroupService.searchUpmGroups();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upmGroupRepository, times(1)).findAll();
  }

  @Test
  void testFindAllApprovedUpmGroup_EmptyList() throws ApiRequestException {
    List<UpmGroup> upmGroupList = Collections.emptyList();
    when(upmGroupRepository.findAll()).thenReturn(upmGroupList);

    ResponseEntity<StandardResponse<List<UpmGroupDTO>>> response =
        upmGroupService.searchUpmGroups();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upmGroupRepository, times(1)).findAll();
  }

  /** findUpmGroupById * */
  @Test
  void testFindApprovedUpmGroupById_Success() throws ApiRequestException {
    when(upmGroupRepository.findById(upmGroup.getUpmGroupID())).thenReturn(Optional.of(upmGroup));

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.findUpmGroupById(upmGroup.getUpmGroupID());

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upmGroupRepository, times(1)).findById(upmGroup.getUpmGroupID());
  }

  @Test
  void testFindApprovedUpmGroupById_NotFound() {
    when(upmGroupRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.findUpmGroupById(2);
            });

    assertEquals("UPM group with 2Does not exists", exception.getMessage());

    verify(upmGroupRepository, times(1)).findById(2);
  }

  /** saveUPMGroupTemp * */
  @Test
  void testSaveUpmGroupTemp_Success() throws ApiRequestException {
    when(upmGroupTempRepository.findAll(any(BooleanBuilder.class)))
        .thenReturn(Collections.emptyList());
    when(upmGroupTempRepository.getCurrentSequenceValue()).thenReturn(1);
    when(upmGroupTempRepository.save(any(UpmGroupTemp.class))).thenReturn(upmGroupTemp);

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.saveUPMGroupTemp(upmGroupDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    UpmGroupTemp savedUser = (UpmGroupTemp) response.getBody().getResponse();
    assertEquals(upmGroupTemp.getGroupCode(), savedUser.getGroupCode());

    verify(upmGroupTempRepository, times(1)).findAll(any(BooleanBuilder.class));
    verify(upmGroupTempRepository, times(1)).save(any(UpmGroupTemp.class));
  }

  @Test
  void testSaveUpmGroupTemp_UserAlreadyExists() throws ApiRequestException {
    when(upmGroupTempRepository.findAll(any(BooleanBuilder.class)))
        .thenReturn(List.of(upmGroupTemp));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.saveUPMGroupTemp(upmGroupDTO);
            });

    assertEquals("UPM Group Already Exists", exception.getMessage());

    verify(upmGroupTempRepository, times(1)).findAll(any(BooleanBuilder.class));
    verify(upmGroupTempRepository, never()).saveAndFlush(any(UpmGroupTemp.class));
  }

  /** approveRejectUpmGroup * */
  @Test
  void testApproveRejectUpmGroup_Success() throws ApiRequestException {
    when(upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
        .thenReturn(Optional.of(upmGroupTemp));
    when(upmGroupRepository.findById(upmGroupTemp.getUpmGroupID()))
        .thenReturn(Optional.of(upmGroup));

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.approveRejectUpmGroup(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upmGroupTempRepository, times(1)).findById(approveRejectRQ.getApproveRejectDataID());
    verify(upmGroupTempRepository, times(1)).save(upmGroupTemp);
  }

  /** approveRejectUpmGroup - Reject Path * */
  @Test
  void testApproveRejectUpmGroup_RejectSuccess() throws ApiRequestException {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);
    when(upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
        .thenReturn(Optional.of(upmGroupTemp));

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.approveRejectUpmGroup(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upmGroupTempRepository, times(1)).findById(approveRejectRQ.getApproveRejectDataID());
    verify(upmGroupTempRepository, times(1)).save(upmGroupTemp);
  }

  /** approveRejectUpmGroup - Invalid Request * */
  @Test
  void testApproveRejectUpmGroup_InvalidRequest() {
    ApproveRejectRQ invalidRequest = new ApproveRejectRQ();
    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.approveRejectUpmGroup(invalidRequest);
            });

    assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
  }

  /** approveRejectUpmGroup - UpmGroup Not Found * */
  @Test
  void testApproveRejectUpmGroup_NotFound() {
    when(upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
        .thenReturn(Optional.empty());
    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.approveRejectUpmGroup(approveRejectRQ);
            });
    assertEquals("UPM group temp with1Does not exists", exception.getMessage());
  }

  /** approveRejectUpmGroup - Unknown Status * */
  @Test
  void testApproveRejectUpmGroup_UnknownStatus() {
    approveRejectRQ.setApproveStatus(null);
    when(upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
        .thenReturn(Optional.of(upmGroupTemp));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.approveRejectUpmGroup(approveRejectRQ);
            });

    assertTrue(exception.getMessage().contains("Unknown approval status"));
  }

  @Test
  void testHandleApproval_CallsMapUpmGroup_ForNewUser() throws ApiRequestException {
    when(upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
        .thenReturn(Optional.of(upmGroupTemp));
    when(upmGroupRepository.findById(upmGroupTemp.getUpmGroupID())).thenReturn(Optional.empty());
    when(upmGroupRepository.save(any(UpmGroup.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.approveRejectUpmGroup(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upmGroupTempRepository, times(1)).delete(upmGroupTemp);
    verify(upmGroupRepository, times(1)).save(any(UpmGroup.class));
  }

  @Test
  void testHandleApproval_CallsUpdateUpmGroupToMaster_ForExistingUser() throws ApiRequestException {
    when(upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
        .thenReturn(Optional.of(upmGroupTemp));
    when(upmGroupRepository.findById(upmGroupTemp.getUpmGroupID()))
        .thenReturn(Optional.of(upmGroup));
    when(upmGroupRepository.save(any(UpmGroup.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.approveRejectUpmGroup(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upmGroupTempRepository, times(1)).delete(upmGroupTemp);
    verify(upmGroupRepository, times(1)).save(upmGroup);
  }

  /** updateUpmGroupTemp * */
  @Test
  void testUpdateUpmGroupTemp_Success() throws ApiRequestException {
    when(upmGroupTempRepository.findById(1)).thenReturn(Optional.of(upmGroupTemp));
    when(upmGroupTempRepository.findAll(any(BooleanBuilder.class)))
        .thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.updateUpmGroupTemp(1, upmGroupDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upmGroupTempRepository, times(1)).save(upmGroupTemp);
  }

  /** updateUpmGroupTemp - UpmGroup Not Found * */
  @Test
  void testUpdateUpmGroupTemp_UpmGroupTempNotFound() {
    when(upmGroupTempRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.updateUpmGroupTemp(1, upmGroupDTO);
            });

    assertEquals("UPM Group Temp with ID 1Does not exists", exception.getMessage());
  }

  @Test
  void testUpdateUpmGroup_DocumentNameConflict() {
    UpmGroupTemp conflictingUser = new UpmGroupTemp();
    conflictingUser.setGroupCode(upmGroupDTO.getGroupCode());
    conflictingUser.setUpmGroupID(2);
    when(upmGroupTempRepository.findById(1)).thenReturn(Optional.of(upmGroupTemp));
    when(upmGroupTempRepository.findAll(any(BooleanBuilder.class)))
        .thenReturn(List.of(conflictingUser));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.updateUpmGroupTemp(1, upmGroupDTO);
            });

    assertEquals("UPM group temp with Old Unit TestingAlready Exists", exception.getMessage());
  }

  @Test
  void testUpdateApprovedUpmGroup_Success() throws ApiRequestException {
    when(upmGroupRepository.findById(upmGroupDTO.getUpmGroupID()))
        .thenReturn(Optional.of(upmGroup));
    when(upmGroupTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(upmGroupRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(upmGroupTempRepository.save(any(UpmGroupTemp.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.updateApprovedUpmGroup(1, upmGroupDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(upmGroupTempRepository, times(1)).save(any(UpmGroupTemp.class));
  }

  /** updateApprovedUpmGroup - upm group Not Found * */
  @Test
  void testUpdateApprovedUpmGroup_NotFound() {
    when(upmGroupRepository.findById(upmGroupDTO.getUpmGroupID())).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.updateApprovedUpmGroup(1, upmGroupDTO);
            });

    assertEquals(" UPM group with 1Does not exists", exception.getMessage());
  }

  /** updateApprovedUpmGroup - Group code Conflict in Temporary Records * */
  @Test
  void testUpdateApprovedUpmGroup_DocumentNameConflictInTemp() {
    when(upmGroupRepository.findById(upmGroupDTO.getUpmGroupID()))
        .thenReturn(Optional.of(upmGroup));
    when(upmGroupTempRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.updateApprovedUpmGroup(1, upmGroupDTO);
            });

    assertEquals(
        "Group Code 'Old Unit Testing' already exists in the system.", exception.getMessage());
  }

  /** updateApprovedUpmGroup - Group code Conflict in Master Records * */
  @Test
  void testUpdateApprovedUpmGroup_DocumentNameConflictsInMaster() {
    when(upmGroupRepository.findById(upmGroupDTO.getUpmGroupID()))
        .thenReturn(Optional.of(upmGroup));
    when(upmGroupTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(upmGroupRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              upmGroupService.updateApprovedUpmGroup(1, upmGroupDTO);
            });

    assertEquals(
        "Group Code 'Old Unit Testing' already exists in the system.", exception.getMessage());
  }

  /** delete UpmGroupTemp * */
  @Test
  void testDeleteUpmGroupFromTemp_Success() throws ApiRequestException {
    doNothing().when(upmGroupTempRepository).deleteById(upmGroupDTO.getUpmGroupID());

    ResponseEntity<StandardResponse<Void>> response = upmGroupService.deleteUpmGroup(1);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getResponse());

    verify(upmGroupTempRepository, times(1)).deleteById(1);
  }

  @Test
  void testApproveRejectUpmGroup_RejectionSuccess_WithAudit() throws ApiRequestException {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

    when(upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
        .thenReturn(Optional.of(upmGroupTemp));

    when(upmGroupTempAudRepository.save(any(UpmGroupTempAud.class)))
        .thenAnswer(
            invocation -> {
              UpmGroupTempAud audit = invocation.getArgument(0);
              audit.setRev(1);
              audit.setRevType(1);
              return audit;
            });

    ResponseEntity<StandardResponse<UpmGroupDTO>> response =
        upmGroupService.approveRejectUpmGroup(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(upmGroupTempRepository, times(1)).findById(approveRejectRQ.getApproveRejectDataID());
    verify(upmGroupTempAudRepository, times(1)).save(any(UpmGroupTempAud.class));

    ArgumentCaptor<UpmGroupTempAud> auditCaptor = ArgumentCaptor.forClass(UpmGroupTempAud.class);
    verify(upmGroupTempAudRepository, times(1)).save(auditCaptor.capture());

    UpmGroupTempAud capturedAudit = auditCaptor.getValue();

    assertNotNull(capturedAudit);
    assertEquals(1, capturedAudit.getRev());
    assertEquals(1, capturedAudit.getRevType());
    assertEquals(upmGroupTemp.getUpmGroupID(), capturedAudit.getUpmGroupID());
    assertEquals(upmGroupTemp.getStatus(), capturedAudit.getStatus());
    assertEquals(upmGroupTemp.getApproveStatus(), capturedAudit.getApproveStatus());
    assertEquals(upmGroupTemp.getReferenceName(), capturedAudit.getReferenceName());
    assertEquals(upmGroupTemp.getGroupCode(), capturedAudit.getGroupCode());
    assertEquals(upmGroupTemp.getWorkFlowLevel(), capturedAudit.getWorkFlowLevel());
  }
}
