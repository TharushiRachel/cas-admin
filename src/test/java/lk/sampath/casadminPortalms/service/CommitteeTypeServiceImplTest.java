package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeTypeAud;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.committeetype.CommitteeTypeAudRepository;
import lk.sampath.casadminportalms.repository.committeetype.CommitteeTypeRepository;
import lk.sampath.casadminportalms.service.impl.CommitteeTypeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CommitteeTypeServiceImplTest {

  @Mock CommitteeTypeRepository committeeTypeRepository;

  @Mock CommitteeTypeAudRepository committeeTypeAudRepository;

  @InjectMocks CommitteeTypeServiceImpl committeeTypeServiceImpl;

  private CommitteeTypeDTO committeeTypeDTO;

  private CommitteeType committeeType;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    UserContext.setUsername("unit.test.user");
    UserContext.setDisplayName("Unit Test User");

    committeeTypeDTO = new CommitteeTypeDTO();
    committeeTypeDTO.setCommitteeTypeId(1);
    committeeTypeDTO.setCommitteeType("BCC");
    committeeTypeDTO.setCommitteeTypeName("Board Credit Committee");
    committeeTypeDTO.setStatus(AppsConstants.Status.ACT);
    committeeTypeDTO.setIsSystem(0);

    committeeType = new CommitteeType();
    committeeType.setCommitteeTypeId(1);
    committeeType.setCommitteeTypeName("BCC");
    committeeType.setCommitteeTypeDescription("Board Credit Committee");
    committeeType.setStatus(AppsConstants.Status.ACT);
    committeeType.setIsSystem(0);
    committeeType.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  @AfterEach
  void tearDown() {
    UserContext.clear();
  }

  /** getCommitteeTypes * */
  @Test
  void testGetCommitteeTypes_Success_ReturnsMappedList() {
    when(committeeTypeRepository.findAll()).thenReturn(List.of(committeeType));

    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeServiceImpl.getCommitteeTypes();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());
    assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

    @SuppressWarnings("unchecked")
    List<CommitteeTypeDTO> resultList = (List<CommitteeTypeDTO>) response.getBody().getResponse();
    assertEquals(1, resultList.size());
    assertEquals(committeeType.getCommitteeTypeId(), resultList.get(0).getCommitteeTypeId());
    assertEquals(committeeType.getCommitteeTypeName(), resultList.get(0).getCommitteeType());

    verify(committeeTypeRepository, times(1)).findAll();
  }

  @Test
  void testGetCommitteeTypes_EmptyList_ReturnsEmptyResponse() {
    when(committeeTypeRepository.findAll()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeServiceImpl.getCommitteeTypes();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    @SuppressWarnings("unchecked")
    List<CommitteeTypeDTO> resultList = (List<CommitteeTypeDTO>) response.getBody().getResponse();
    assertTrue(resultList.isEmpty());

    verify(committeeTypeRepository, times(1)).findAll();
  }

  @Test
  void testGetCommitteeTypes_MultipleRecords_ReturnsAllMapped() {
    CommitteeType second = new CommitteeType();
    second.setCommitteeTypeId(2);
    second.setCommitteeTypeName("MCC");
    second.setCommitteeTypeDescription("Management Credit Committee");
    second.setStatus(AppsConstants.Status.ACT);
    second.setIsSystem(0);

    when(committeeTypeRepository.findAll()).thenReturn(List.of(committeeType, second));

    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeServiceImpl.getCommitteeTypes();

    assertNotNull(response.getBody());
    @SuppressWarnings("unchecked")
    List<CommitteeTypeDTO> resultList = (List<CommitteeTypeDTO>) response.getBody().getResponse();
    assertEquals(2, resultList.size());
    assertEquals("BCC", resultList.get(0).getCommitteeType());
    assertEquals("MCC", resultList.get(1).getCommitteeType());

    verify(committeeTypeRepository, times(1)).findAll();
  }

  @Test
  void testGetCommitteeTypes_RepositoryThrowsException_WrapsInApiRequestException() {
    when(committeeTypeRepository.findAll()).thenThrow(new RuntimeException("DB is down"));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              committeeTypeServiceImpl.getCommitteeTypes();
            });

    assertEquals("Failed to retrieve committee types", exception.getMessage());

    verify(committeeTypeRepository, times(1)).findAll();
  }

  @Test
  void testGetCommitteeTypes_MapsAllDtoFieldsCorrectly() {
    committeeType.setCreatedUserDisplayName("Jane Doe");

    when(committeeTypeRepository.findAll()).thenReturn(List.of(committeeType));

    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeServiceImpl.getCommitteeTypes();

    @SuppressWarnings("unchecked")
    List<CommitteeTypeDTO> resultList = (List<CommitteeTypeDTO>) response.getBody().getResponse();
    CommitteeTypeDTO dto = resultList.get(0);

    assertEquals(committeeType.getCommitteeTypeId(), dto.getCommitteeTypeId());
    assertEquals(committeeType.getCommitteeTypeName(), dto.getCommitteeType());
    assertEquals(committeeType.getCommitteeTypeDescription(), dto.getCommitteeTypeName());
    assertEquals(committeeType.getCreatedUserDisplayName(), dto.getCreatedUserDisplayName());
    assertEquals(committeeType.getStatus(), dto.getStatus());
    assertEquals(committeeType.getIsSystem(), dto.getIsSystem());
  }

  /** saveCommitteeType * */
  @Test
  void testSaveCommitteeType_Success_ReturnsSuccessResponseWithAllCommitteeTypes() {
    when(committeeTypeRepository.findByCommitteeType("bcc")).thenReturn(Collections.emptyList());
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(committeeTypeRepository.findAll()).thenReturn(List.of(committeeType));

    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());

    verify(committeeTypeRepository, times(1)).findByCommitteeType("bcc");
    verify(committeeTypeRepository, times(1)).save(any(CommitteeType.class));
    verify(committeeTypeAudRepository, times(1)).save(any(CommitteeTypeAud.class));
  }

  @Test
  void testSaveCommitteeType_DuplicateExists_ThrowsApiRequestException() {
    when(committeeTypeRepository.findByCommitteeType("bcc")).thenReturn(List.of(committeeType));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);
            });

    assertEquals("Committee Type is already exists.", exception.getMessage());

    verify(committeeTypeRepository, never()).save(any(CommitteeType.class));
    verify(committeeTypeAudRepository, never()).save(any(CommitteeTypeAud.class));
  }

  @Test
  void testSaveCommitteeType_TrimsAndLowercasesCommitteeTypeForDuplicateCheck() {
    committeeTypeDTO.setCommitteeType("  BCC  ");

    when(committeeTypeRepository.findByCommitteeType("bcc")).thenReturn(Collections.emptyList());
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(committeeTypeRepository.findAll()).thenReturn(List.of(committeeType));

    committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);

    verify(committeeTypeRepository, times(1)).findByCommitteeType("bcc");
  }

  @Test
  void testSaveCommitteeType_CapturesCorrectFieldsOnSavedEntity() {
    when(committeeTypeRepository.findByCommitteeType("bcc")).thenReturn(Collections.emptyList());
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(committeeTypeRepository.findAll()).thenReturn(Collections.emptyList());

    committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);

    ArgumentCaptor<CommitteeType> captor = ArgumentCaptor.forClass(CommitteeType.class);
    verify(committeeTypeRepository, times(1)).save(captor.capture());
    CommitteeType captured = captor.getValue();

    assertEquals(committeeTypeDTO.getCommitteeType(), captured.getCommitteeTypeName());
    assertEquals(committeeTypeDTO.getCommitteeTypeName(), captured.getCommitteeTypeDescription());
    assertEquals(AppsConstants.Status.ACT, captured.getStatus());
    assertEquals(committeeTypeDTO.getIsSystem(), captured.getIsSystem());
    assertEquals(MasterDataApproveStatus.APPROVED, captured.getApproveStatus());
  }

  @Test
  void testSaveCommitteeType_SavesAuditRecordWithMatchingData() {
    when(committeeTypeRepository.findByCommitteeType("bcc")).thenReturn(Collections.emptyList());
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(
            invocation -> {
              CommitteeType saved = invocation.getArgument(0);
              saved.setCommitteeTypeId(5);
              return saved;
            });
    when(committeeTypeRepository.findAll()).thenReturn(Collections.emptyList());

    committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);

    ArgumentCaptor<CommitteeTypeAud> auditCaptor = ArgumentCaptor.forClass(CommitteeTypeAud.class);
    verify(committeeTypeAudRepository, times(1)).save(auditCaptor.capture());
    CommitteeTypeAud capturedAudit = auditCaptor.getValue();

    assertEquals(5, capturedAudit.getCommitteeTypeId());
    assertEquals(committeeTypeDTO.getCommitteeType(), capturedAudit.getCommitteeType());
    assertEquals(committeeTypeDTO.getCommitteeTypeName(), capturedAudit.getCommitteeTypeName());
    assertEquals(AppsConstants.Status.ACT, capturedAudit.getStatus());
    assertEquals(committeeTypeDTO.getIsSystem(), capturedAudit.getIsSystem());
    assertEquals(MasterDataApproveStatus.APPROVED, capturedAudit.getApproveStatus());
  }

  @Test
  void testSaveCommitteeType_RepositorySaveThrowsApiRequestExceptionWithEmptyMessage_UsesFallbackMessage() {
    when(committeeTypeRepository.findByCommitteeType("bcc")).thenReturn(Collections.emptyList());
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenThrow(new ApiRequestException(""));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);
            });

    assertEquals("Committee Type saving has been failed.", exception.getMessage());

    verify(committeeTypeAudRepository, never()).save(any(CommitteeTypeAud.class));
  }

  /** updateCommitteeType * */
  @Test
  void testUpdateCommitteeType_Success_ReturnsUpdatedList() {
    when(committeeTypeRepository.findById(1)).thenReturn(Optional.of(committeeType));
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(committeeTypeRepository.findAll()).thenReturn(List.of(committeeType));

    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeServiceImpl.updateCommitteeType(committeeTypeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());

    verify(committeeTypeRepository, times(1)).findById(1);
    verify(committeeTypeRepository, times(1)).save(any(CommitteeType.class));
    verify(committeeTypeAudRepository, times(1)).save(any(CommitteeTypeAud.class));
  }

  @Test
  void testUpdateCommitteeType_NotFound_ThrowsApiRequestException() {
    committeeTypeDTO.setCommitteeTypeId(99);
    when(committeeTypeRepository.findById(99)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              committeeTypeServiceImpl.updateCommitteeType(committeeTypeDTO);
            });

    assertEquals("Committee Type with ID 99 does not exist", exception.getMessage());

    verify(committeeTypeRepository, never()).save(any(CommitteeType.class));
    verify(committeeTypeAudRepository, never()).save(any(CommitteeTypeAud.class));
  }

  @Test
  void testUpdateCommitteeType_CapturesUpdatedFieldsOnEntity() {
    committeeTypeDTO.setCommitteeType("Updated Name");
    committeeTypeDTO.setCommitteeTypeName("Updated Description");
    committeeTypeDTO.setStatus(AppsConstants.Status.INA);
    committeeTypeDTO.setIsSystem(1);

    when(committeeTypeRepository.findById(1)).thenReturn(Optional.of(committeeType));
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(committeeTypeRepository.findAll()).thenReturn(Collections.emptyList());

    committeeTypeServiceImpl.updateCommitteeType(committeeTypeDTO);

    ArgumentCaptor<CommitteeType> captor = ArgumentCaptor.forClass(CommitteeType.class);
    verify(committeeTypeRepository, times(1)).save(captor.capture());
    CommitteeType captured = captor.getValue();

    assertEquals("Updated Name", captured.getCommitteeTypeName());
    assertEquals("Updated Description", captured.getCommitteeTypeDescription());
    assertEquals(AppsConstants.Status.INA, captured.getStatus());
    assertEquals(1, captured.getIsSystem());
    assertEquals(MasterDataApproveStatus.APPROVED, captured.getApproveStatus());
  }

  @Test
  void testUpdateCommitteeType_SavesAuditRecordReflectingUpdatedEntity() {
    committeeTypeDTO.setCommitteeType("Renamed BCC");

    when(committeeTypeRepository.findById(1)).thenReturn(Optional.of(committeeType));
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(committeeTypeRepository.findAll()).thenReturn(Collections.emptyList());

    committeeTypeServiceImpl.updateCommitteeType(committeeTypeDTO);

    ArgumentCaptor<CommitteeTypeAud> auditCaptor = ArgumentCaptor.forClass(CommitteeTypeAud.class);
    verify(committeeTypeAudRepository, times(1)).save(auditCaptor.capture());
    CommitteeTypeAud capturedAudit = auditCaptor.getValue();

    assertEquals(1, capturedAudit.getCommitteeTypeId());
    assertEquals("Renamed BCC", capturedAudit.getCommitteeType());
    assertEquals(MasterDataApproveStatus.APPROVED, capturedAudit.getApproveStatus());
  }

  @Test
  void testUpdateCommitteeType_PreservesStatusFromRequestDirectly() {
    committeeTypeDTO.setStatus(AppsConstants.Status.RMV);

    when(committeeTypeRepository.findById(1)).thenReturn(Optional.of(committeeType));
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(committeeTypeRepository.findAll()).thenReturn(Collections.emptyList());

    committeeTypeServiceImpl.updateCommitteeType(committeeTypeDTO);

    ArgumentCaptor<CommitteeType> captor = ArgumentCaptor.forClass(CommitteeType.class);
    verify(committeeTypeRepository, times(1)).save(captor.capture());

    assertEquals(AppsConstants.Status.RMV, captor.getValue().getStatus());
  }

  @Test
  void testUpdateCommitteeType_SaveThrowsApiRequestExceptionWithEmptyMessage_UsesFallbackMessage() {
    when(committeeTypeRepository.findById(1)).thenReturn(Optional.of(committeeType));
    when(committeeTypeRepository.save(any(CommitteeType.class)))
        .thenThrow(new ApiRequestException(""));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              committeeTypeServiceImpl.updateCommitteeType(committeeTypeDTO);
            });

    assertEquals("Committee Type update has been failed.", exception.getMessage());

    verify(committeeTypeAudRepository, never()).save(any(CommitteeTypeAud.class));
  }
}
