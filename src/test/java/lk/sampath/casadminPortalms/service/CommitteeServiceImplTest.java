package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committee.CommitteeDTO;
import lk.sampath.casadminportalms.dto.committee.CommitteeLevelDTO;
import lk.sampath.casadminportalms.dto.committee.LevelUserDTO;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.entity.committee.CommitteeHistory;
import lk.sampath.casadminportalms.entity.committee.CommitteeTemp;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.committee.CommitteeAudRepository;
import lk.sampath.casadminportalms.repository.committee.CommitteeJdbc;
import lk.sampath.casadminportalms.repository.committee.CommitteeLevelAudRepository;
import lk.sampath.casadminportalms.repository.committee.CommitteeRepository;
import lk.sampath.casadminportalms.repository.committee.CommitteeTempRepository;
import lk.sampath.casadminportalms.repository.committee.LevelRepository;
import lk.sampath.casadminportalms.repository.committee.LevelTempRepository;
import lk.sampath.casadminportalms.repository.committee.LevelUserAudRepository;
import lk.sampath.casadminportalms.repository.committee.LevelUserRepository;
import lk.sampath.casadminportalms.repository.committee.LevelUserTempRepository;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolRepository;
import lk.sampath.casadminportalms.repository.committeetype.CommitteeTypeRepository;
import lk.sampath.casadminportalms.service.impl.CommitteeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CommitteeServiceImplTest {

  @Mock private CommitteeTempRepository committeeTempRepository;

  @Mock private CommitteeRepository committeeRepository;

  @Mock private CommitteeTypeRepository committeeTypeRepository;

  @Mock private LevelTempRepository levelTempRepository;

  @Mock private LevelUserTempRepository levelUserTempRepository;

  @Mock private CommitteePoolRepository committeePoolRepository;

  @Mock private LevelRepository levelRepository;

  @Mock private LevelUserRepository levelUserRepository;

  @Mock private CommitteeServiceImpl committeeService;

  @Mock private CommitteeAudRepository committeeAudRepository;

  @Mock private CommitteeLevelAudRepository committeeLevelAudRepository;

  @Mock private LevelUserAudRepository levelUserAudRepository;

  @Mock private CommitteeJdbc committeeJdbc;

  @InjectMocks private CommitteeServiceImpl committeeServiceImpl;

  private CommitteeDTO committeeDTO;

  private CommitteeTemp committeeTemp;

  private Committee committee;

  private CommitteeType committeeType;

  private CommitteePool committeePool;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    UserContext.setUsername("unit.test.user");
    UserContext.setDisplayName("Unit Test User");

    committeeType = new CommitteeType();
    committeeType.setCommitteeTypeId(1);
    committeeType.setCommitteeTypeName("CT1");
    committeeType.setCommitteeTypeDescription("Credit Committee Type");
    committeeType.setStatus(AppsConstants.Status.ACT);

    committeePool = new CommitteePool();
    committeePool.setUserId(10);
    committeePool.setUserName("user10");
    committeePool.setUserDisplayName("User Ten");
    committeePool.setUserStatus(AppsConstants.Status.ACT);

    committeeDTO = new CommitteeDTO();
    committeeDTO.setCommitteeId(0);
    committeeDTO.setCommitteeName("Credit Committee");
    committeeDTO.setDelegatedAuthority(BigDecimal.valueOf(100));
    committeeDTO.setStatus(AppsConstants.Status.ACT);
    committeeDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
    committeeDTO.setReviewer("reviewer1");
    committeeDTO.setCurrentPath(AppsConstants.CAPathType.REG);
    committeeDTO.setCommitteeTypeId(1);
    committeeDTO.setRecordStatus(AppsConstants.RecordStatus.NEW);
    committeeDTO.setCreatedBy("admin");
    committeeDTO.setCreatedDate(new Date());
    committeeDTO.setLevels(new ArrayList<>());

    committeeTemp = new CommitteeTemp();
    committeeTemp.setCommitteeId(5);
    committeeTemp.setCommitteeName("Existing Committee");
    committeeTemp.setDelegatedAuthority(BigDecimal.valueOf(50));
    committeeTemp.setStatus(AppsConstants.Status.INA);
    committeeTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
    committeeTemp.setReviewer("reviewer1");
    committeeTemp.setCurrentPath(AppsConstants.CAPathType.REG);
    committeeTemp.setCreatedBy("admin");
    committeeTemp.setCreatedDate(new Date());
    committeeTemp.setCommitteeLevels(new ArrayList<>());

    committee = new Committee();
    committee.setCommitteeId(5);
    committee.setCommitteeName("Existing Committee");
    committee.setDelegatedAuthority(BigDecimal.valueOf(50));
    committee.setStatus(AppsConstants.Status.ACT);
    committee.setApproveStatus(MasterDataApproveStatus.APPROVED);
    committee.setReviewer("reviewer1");
    committee.setCurrentPath(AppsConstants.CAPathType.REG);
    committee.setCreatedBy("admin");
    committee.setCreatedDate(new Date());
    committee.setCommitteeLevels(new ArrayList<>());
  }

  @AfterEach
  void tearDown() {
    UserContext.clear();
  }

  private CommitteeLevelDTO buildLevelDTO(
      Integer levelId, AppsConstants.RecordStatus recordStatus, List<LevelUserDTO> levelUsers) {
    CommitteeLevelDTO levelDTO = new CommitteeLevelDTO();
    levelDTO.setLevelId(levelId);
    levelDTO.setRecordStatus(recordStatus);
    levelDTO.setLevel(1);
    levelDTO.setCombination("AND");
    levelDTO.setStatus(AppsConstants.Status.ACT);
    levelDTO.setPathType(AppsConstants.CAPathType.REG);
    levelDTO.setUserCount(1);
    levelDTO.setCreatedBy("admin");
    levelDTO.setCreatedDate(new Date());
    levelDTO.setLevelUsers(levelUsers);
    return levelDTO;
  }

  private LevelUserDTO buildLevelUserDTO(
      Integer levelUserId, AppsConstants.RecordStatus recordStatus, Integer userId) {
    LevelUserDTO levelUserDTO = new LevelUserDTO();
    levelUserDTO.setLevelUserId(levelUserId);
    levelUserDTO.setRecordStatus(recordStatus);
    levelUserDTO.setUserId(userId);
    levelUserDTO.setCreatedBy("admin");
    levelUserDTO.setCreatedDate(new Date());
    return levelUserDTO;
  }

  /* ---------------------------- getTempCommittees ---------------------------- */

  @Test
  void getTempCommittees_WhenCommitteesExist_ReturnsSuccessResponse() throws ApiRequestException {
    CommitteeDTO dto1 = new CommitteeDTO();
    dto1.setCommitteeId(1);
    List<CommitteeDTO> committees = Arrays.asList(dto1, committeeDTO);
    when(committeeJdbc.getAllCommittees(true)).thenReturn(committees);

    ResponseEntity<StandardResponse<List<CommitteeDTO>>> response =
        committeeServiceImpl.getTempCommittees();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());
    assertEquals(2, ((List<?>) response.getBody().getResponse()).size());
    verify(committeeJdbc, times(1)).getAllCommittees(true);
  }

  @Test
  void getTempCommittees_WhenNoCommittees_ReturnsEmptyList() throws ApiRequestException {
    when(committeeJdbc.getAllCommittees(true)).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<List<CommitteeDTO>>> response =
        committeeServiceImpl.getTempCommittees();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(((List<?>) response.getBody().getResponse()).isEmpty());
    verify(committeeJdbc, times(1)).getAllCommittees(true);
  }

  @Test
  void getTempCommittees_WhenJdbcThrowsException_ThrowsApiRequestException() {
    when(committeeJdbc.getAllCommittees(true)).thenThrow(new RuntimeException("DB down"));

    ApiRequestException exception =
        assertThrows(ApiRequestException.class, committeeServiceImpl::getTempCommittees);

    assertEquals("An error occurred while fetching pending data.", exception.getMessage());
    verify(committeeJdbc, times(1)).getAllCommittees(true);
  }

  @Test
  void getTempCommittees_AlwaysUsesGenericMessage_RegardlessOfUnderlyingCause() {
    when(committeeJdbc.getAllCommittees(true))
        .thenThrow(new ApiRequestException("Some specific low level failure"));

    ApiRequestException exception =
        assertThrows(ApiRequestException.class, committeeServiceImpl::getTempCommittees);

    assertEquals("An error occurred while fetching pending data.", exception.getMessage());
  }

  @Test
  void getTempCommittees_QueriesJdbcWithTempFlagTrue() throws ApiRequestException {
    when(committeeJdbc.getAllCommittees(true)).thenReturn(Collections.singletonList(committeeDTO));

    committeeServiceImpl.getTempCommittees();

    verify(committeeJdbc, times(1)).getAllCommittees(true);
    verify(committeeJdbc, never()).getAllCommittees(false);
  }

  /* ---------------------------- getTempCommitteeById ---------------------------- */

  @Test
  void getTempCommitteeById_WhenFound_ReturnsSuccessResponse() throws ApiRequestException {
    CommitteeDTO found = new CommitteeDTO();
    found.setCommitteeId(5);
    when(committeeJdbc.getCommittee(5, true)).thenReturn(found);

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.getTempCommitteeById(5);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getSuccess());
    assertEquals(5, ((CommitteeDTO) response.getBody().getResponse()).getCommitteeId());
    verify(committeeJdbc, times(1)).getCommittee(5, true);
  }

  @Test
  void getTempCommitteeById_WhenIdIsNull_ThrowsApiRequestException() {
    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.getTempCommitteeById(null));

    assertEquals("Committee Id is empty.", exception.getMessage());
    verify(committeeJdbc, never()).getCommittee(any(), anyBoolean());
  }

  @Test
  void getTempCommitteeById_WhenNotFound_ThrowsApiRequestException() {
    CommitteeDTO notFound = new CommitteeDTO();
    when(committeeJdbc.getCommittee(99, true)).thenReturn(notFound);

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.getTempCommitteeById(99));

    assertEquals("Committee not found.", exception.getMessage());
    verify(committeeJdbc, times(1)).getCommittee(99, true);
  }

  @Test
  void getTempCommitteeById_WhenJdbcThrowsWithMessage_PropagatesMessage() {
    when(committeeJdbc.getCommittee(5, true)).thenThrow(new ApiRequestException("DB failure"));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.getTempCommitteeById(5));

    assertEquals("DB failure", exception.getMessage());
  }

  @Test
  void getTempCommitteeById_WhenJdbcThrowsWithEmptyMessage_UsesDefaultMessage() {
    when(committeeJdbc.getCommittee(5, true)).thenThrow(new ApiRequestException(""));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.getTempCommitteeById(5));

    assertEquals("An error occurred while fetching pending committee.", exception.getMessage());
  }

  /* ---------------------------- getCommittees ---------------------------- */

  @Test
  void getCommittees_WhenCommitteesExist_ReturnsSuccessResponse() throws ApiRequestException {
    List<CommitteeDTO> committees = Arrays.asList(committeeDTO);
    when(committeeJdbc.getAllCommittees(false)).thenReturn(committees);

    ResponseEntity<StandardResponse<List<CommitteeDTO>>> response =
        committeeServiceImpl.getCommittees();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getSuccess());
    assertEquals(1, ((List<?>) response.getBody().getResponse()).size());
    verify(committeeJdbc, times(1)).getAllCommittees(false);
  }

  @Test
  void getCommittees_WhenNoCommittees_ReturnsEmptyList() throws ApiRequestException {
    when(committeeJdbc.getAllCommittees(false)).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<List<CommitteeDTO>>> response =
        committeeServiceImpl.getCommittees();

    assertTrue(((List<?>) response.getBody().getResponse()).isEmpty());
    verify(committeeJdbc, times(1)).getAllCommittees(false);
  }

  @Test
  void getCommittees_WhenJdbcThrowsException_ThrowsApiRequestException() {
    when(committeeJdbc.getAllCommittees(false)).thenThrow(new RuntimeException("DB down"));

    ApiRequestException exception =
        assertThrows(ApiRequestException.class, committeeServiceImpl::getCommittees);

    assertEquals("An error occurred while fetching approved data.", exception.getMessage());
  }

  @Test
  void getCommittees_QueriesJdbcWithTempFlagFalse() throws ApiRequestException {
    when(committeeJdbc.getAllCommittees(false))
        .thenReturn(Collections.singletonList(committeeDTO));

    committeeServiceImpl.getCommittees();

    verify(committeeJdbc, times(1)).getAllCommittees(false);
    verify(committeeJdbc, never()).getAllCommittees(true);
  }

  @Test
  void getCommittees_WithMultipleCommittees_ReturnsAllOfThem() throws ApiRequestException {
    CommitteeDTO dto1 = new CommitteeDTO();
    dto1.setCommitteeId(1);
    CommitteeDTO dto2 = new CommitteeDTO();
    dto2.setCommitteeId(2);
    CommitteeDTO dto3 = new CommitteeDTO();
    dto3.setCommitteeId(3);
    when(committeeJdbc.getAllCommittees(false)).thenReturn(Arrays.asList(dto1, dto2, dto3));

    ResponseEntity<StandardResponse<List<CommitteeDTO>>> response =
        committeeServiceImpl.getCommittees();

    assertEquals(3, ((List<?>) response.getBody().getResponse()).size());
  }

  /* ---------------------------- getCommitteeById ---------------------------- */

  @Test
  void getCommitteeById_WhenFound_ReturnsSuccessResponse() throws ApiRequestException {
    CommitteeDTO found = new CommitteeDTO();
    found.setCommitteeId(5);
    when(committeeJdbc.getCommittee(5, false)).thenReturn(found);

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.getCommitteeById(5);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getSuccess());
    assertEquals(5, ((CommitteeDTO) response.getBody().getResponse()).getCommitteeId());
    verify(committeeJdbc, times(1)).getCommittee(5, false);
  }

  @Test
  void getCommitteeById_WhenIdIsNull_ThrowsApiRequestException() {
    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.getCommitteeById(null));

    assertEquals("Committee Id is empty.", exception.getMessage());
    verify(committeeJdbc, never()).getCommittee(any(), anyBoolean());
  }

  @Test
  void getCommitteeById_WhenNotFound_ThrowsApiRequestException() {
    CommitteeDTO notFound = new CommitteeDTO();
    when(committeeJdbc.getCommittee(99, false)).thenReturn(notFound);

    ApiRequestException exception =
        assertThrows(ApiRequestException.class, () -> committeeServiceImpl.getCommitteeById(99));

    assertEquals("Committee not found.", exception.getMessage());
  }

  @Test
  void getCommitteeById_WhenJdbcThrowsWithMessage_PropagatesMessage() {
    when(committeeJdbc.getCommittee(5, false)).thenThrow(new ApiRequestException("DB failure"));

    ApiRequestException exception =
        assertThrows(ApiRequestException.class, () -> committeeServiceImpl.getCommitteeById(5));

    assertEquals("DB failure", exception.getMessage());
  }

  @Test
  void getCommitteeById_WhenJdbcThrowsWithEmptyMessage_UsesDefaultMessage() {
    when(committeeJdbc.getCommittee(5, false)).thenThrow(new ApiRequestException(""));

    ApiRequestException exception =
        assertThrows(ApiRequestException.class, () -> committeeServiceImpl.getCommitteeById(5));

    assertEquals("An error occurred while fetching approved committee.", exception.getMessage());
  }

  /* ---------------------------- saveTempCommittee ---------------------------- */

  @Test
  void saveTempCommittee_WhenNewCommitteeNameAlreadyExists_ThrowsApiRequestException() {
    CommitteeDTO existing = new CommitteeDTO();
    existing.setCommitteeId(2);
    existing.setCommitteeName("Credit Committee");
    when(committeeJdbc.findCommitteeByCommitteeName("Credit Committee")).thenReturn(existing);
    when(committeeTempRepository.findById(0)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.saveTempCommittee(committeeDTO));

    assertEquals("The committee name is already exist.", exception.getMessage());
    verify(committeeTempRepository, never()).saveAndFlush(any(CommitteeTemp.class));
  }

  @Test
  void saveTempCommittee_WhenNewCommitteeWithNoConflict_SavesSuccessfully()
      throws ApiRequestException {
    when(committeeJdbc.findCommitteeByCommitteeName("Credit Committee")).thenReturn(null);
    when(committeeTempRepository.findById(0)).thenReturn(Optional.empty());
    when(committeeTypeRepository.findById(1)).thenReturn(Optional.of(committeeType));
    when(committeeTempRepository.getCurrentSequenceValue()).thenReturn(100);
    when(committeeTempRepository.saveAndFlush(any(CommitteeTemp.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.saveTempCommittee(committeeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getSuccess());

    CommitteeDTO saved = (CommitteeDTO) response.getBody().getResponse();
    assertEquals(100, saved.getCommitteeId());
    assertEquals("Credit Committee", saved.getCommitteeName());

    ArgumentCaptor<CommitteeTemp> captor = ArgumentCaptor.forClass(CommitteeTemp.class);
    verify(committeeTempRepository, times(1)).saveAndFlush(captor.capture());
    assertEquals(committeeType, captor.getValue().getCommitteeType());
  }

  @Test
  void saveTempCommittee_WhenCommitteeTypeNotFound_ThrowsApiRequestException() {
    when(committeeJdbc.findCommitteeByCommitteeName("Credit Committee")).thenReturn(null);
    when(committeeTempRepository.findById(0)).thenReturn(Optional.empty());
    when(committeeTypeRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.saveTempCommittee(committeeDTO));

    assertEquals("The committee type is not found.", exception.getMessage());
    verify(committeeTempRepository, never()).saveAndFlush(any(CommitteeTemp.class));
  }

  @Test
  void saveTempCommittee_WhenUpdatingExistingCommittee_SetsModifiedByAndDate()
      throws ApiRequestException {
    committeeDTO.setCommitteeId(10);
    committeeDTO.setCommitteeTypeId(0);
    when(committeeJdbc.findCommitteeByCommitteeName("Credit Committee")).thenReturn(null);
    when(committeeTempRepository.findById(10)).thenReturn(Optional.of(committeeTemp));
    when(committeeTempRepository.saveAndFlush(any(CommitteeTemp.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.saveTempCommittee(committeeDTO);

    assertNotNull(response);
    assertTrue(response.getBody().getSuccess());

    ArgumentCaptor<CommitteeTemp> captor = ArgumentCaptor.forClass(CommitteeTemp.class);
    verify(committeeTempRepository, times(1)).saveAndFlush(captor.capture());
    assertEquals("unit.test.user", captor.getValue().getModifiedBy());
    assertNotNull(captor.getValue().getLastModifiedDate());
  }

  @Test
  void saveTempCommittee_WhenLevelMarkedForDeletion_DeletesLevelAndSkipsMapping()
      throws ApiRequestException {
    committeeDTO.setCommitteeTypeId(0);
    CommitteeLevelDTO deletedLevel =
        buildLevelDTO(8, AppsConstants.RecordStatus.DELETE, new ArrayList<>());
    committeeDTO.setLevels(new ArrayList<>(List.of(deletedLevel)));

    when(committeeJdbc.findCommitteeByCommitteeName("Credit Committee")).thenReturn(null);
    when(committeeTempRepository.findById(0)).thenReturn(Optional.empty());
    when(committeeTempRepository.saveAndFlush(any(CommitteeTemp.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.saveTempCommittee(committeeDTO);

    assertTrue(response.getBody().getSuccess());
    verify(levelTempRepository, times(1)).deleteById(8);

    ArgumentCaptor<CommitteeTemp> captor = ArgumentCaptor.forClass(CommitteeTemp.class);
    verify(committeeTempRepository, times(1)).saveAndFlush(captor.capture());
    assertTrue(captor.getValue().getCommitteeLevels().isEmpty());
  }

  @Test
  void saveTempCommittee_WhenLevelUserPoolNotFound_ThrowsCommitteeLevelSaveFailedException() {
    committeeDTO.setCommitteeTypeId(0);
    LevelUserDTO levelUserDTO = buildLevelUserDTO(0, AppsConstants.RecordStatus.NEW, 99);
    CommitteeLevelDTO levelDTO =
        buildLevelDTO(0, AppsConstants.RecordStatus.NEW, new ArrayList<>(List.of(levelUserDTO)));
    committeeDTO.setLevels(new ArrayList<>(List.of(levelDTO)));

    when(committeeJdbc.findCommitteeByCommitteeName("Credit Committee")).thenReturn(null);
    when(committeeTempRepository.findById(0)).thenReturn(Optional.empty());
    when(levelTempRepository.findById(0)).thenReturn(Optional.empty());
    when(levelTempRepository.getCurrentSequenceValue()).thenReturn(50);
    when(committeePoolRepository.findByUserId(99)).thenReturn(null);

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.saveTempCommittee(committeeDTO));

    assertEquals("Committee level save has been failed.", exception.getMessage());
    verify(committeePoolRepository, times(1)).findByUserId(99);
    verify(committeeTempRepository, never()).saveAndFlush(any(CommitteeTemp.class));
  }

  @Test
  void saveTempCommittee_WhenLevelUserMarkedForDeletion_DeletesLevelUserAndSkipsPoolLookup()
      throws ApiRequestException {
    committeeDTO.setCommitteeTypeId(0);
    LevelUserDTO deletedLevelUser = buildLevelUserDTO(77, AppsConstants.RecordStatus.DELETE, 99);
    CommitteeLevelDTO levelDTO =
        buildLevelDTO(0, AppsConstants.RecordStatus.NEW, new ArrayList<>(List.of(deletedLevelUser)));
    committeeDTO.setLevels(new ArrayList<>(List.of(levelDTO)));

    when(committeeJdbc.findCommitteeByCommitteeName("Credit Committee")).thenReturn(null);
    when(committeeTempRepository.findById(0)).thenReturn(Optional.empty());
    when(levelTempRepository.findById(0)).thenReturn(Optional.empty());
    when(levelTempRepository.getCurrentSequenceValue()).thenReturn(50);
    when(committeeTempRepository.saveAndFlush(any(CommitteeTemp.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.saveTempCommittee(committeeDTO);

    assertTrue(response.getBody().getSuccess());
    verify(levelUserTempRepository, times(1)).deleteById(77);
    verify(committeePoolRepository, never()).findByUserId(any());

    ArgumentCaptor<CommitteeTemp> captor = ArgumentCaptor.forClass(CommitteeTemp.class);
    verify(committeeTempRepository, times(1)).saveAndFlush(captor.capture());
    assertEquals(1, captor.getValue().getCommitteeLevels().size());
    assertTrue(captor.getValue().getCommitteeLevels().get(0).getLevelUsers().isEmpty());
  }

  /* ---------------------------- approveRejectCommittee ---------------------------- */

  @Test
  void approveRejectCommittee_WhenApprovedAndActive_DelegatesToSaveCommittee()
      throws ApiRequestException {
    committeeDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);
    committeeDTO.setStatus(AppsConstants.Status.ACT);
    committeeDTO.setCommitteeId(5);
    when(committeeService.saveCommittee(committeeDTO)).thenReturn(committee);

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.approveRejectCommittee(committeeDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getSuccess());
    CommitteeDTO result = (CommitteeDTO) response.getBody().getResponse();
    assertEquals(5, result.getCommitteeId());
    verify(committeeService, times(1)).saveCommittee(committeeDTO);
  }

  @Test
  void approveRejectCommittee_WhenApprovedAndInactive_HandlesApprovedInactivePath()
      throws ApiRequestException {
    committeeDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);
    committeeDTO.setStatus(AppsConstants.Status.INA);
    committeeDTO.setCommitteeId(5);
    when(committeeTempRepository.findById(5)).thenReturn(Optional.of(committeeTemp));
    when(committeeRepository.findById(5)).thenReturn(Optional.of(committee));

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.approveRejectCommittee(committeeDTO);

    assertNotNull(response);
    assertTrue(response.getBody().getSuccess());
    verify(committeeRepository, times(1)).save(committee);
    verify(committeeAudRepository, times(1)).save(any(CommitteeHistory.class));
    assertEquals(MasterDataApproveStatus.APPROVED, committee.getApproveStatus());
    verify(committeeService, never()).saveCommittee(any());
  }

  @Test
  void approveRejectCommittee_WhenRejected_HandlesRejectedPath() throws ApiRequestException {
    committeeDTO.setApproveStatus(MasterDataApproveStatus.REJECTED);
    committeeDTO.setCommitteeId(5);
    when(committeeTempRepository.findById(5)).thenReturn(Optional.of(committeeTemp));

    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        committeeServiceImpl.approveRejectCommittee(committeeDTO);

    assertNotNull(response);
    assertTrue(response.getBody().getSuccess());
    verify(committeeTempRepository, times(1)).save(committeeTemp);
    verify(committeeAudRepository, times(1)).save(any(CommitteeHistory.class));
    assertEquals(MasterDataApproveStatus.REJECTED, committeeTemp.getApproveStatus());
    assertEquals(AppsConstants.RecordStatus.DRAFT, committeeTemp.getRecordStatus());
  }

  @Test
  void approveRejectCommittee_WhenApproveStatusUnknown_ThrowsApiRequestException() {
    committeeDTO.setApproveStatus(MasterDataApproveStatus.PENDING);

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> committeeServiceImpl.approveRejectCommittee(committeeDTO));

    assertEquals("Unknown approval status: PENDING", exception.getMessage());
  }

  @Test
  void approveRejectCommittee_WhenApprovedInactiveCommitteeTempNotFound_ThrowsException() {
    committeeDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);
    committeeDTO.setStatus(AppsConstants.Status.INA);
    committeeDTO.setCommitteeId(5);
    when(committeeTempRepository.findById(5)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> committeeServiceImpl.approveRejectCommittee(committeeDTO));

    assertEquals("Committee not found.", exception.getMessage());
  }

  @Test
  void approveRejectCommittee_WhenRejectedCommitteeTempNotFound_UsesDefaultMessage() {
    committeeDTO.setApproveStatus(MasterDataApproveStatus.REJECTED);
    committeeDTO.setCommitteeId(5);
    when(committeeTempRepository.findById(5)).thenReturn(Optional.empty());

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> committeeServiceImpl.approveRejectCommittee(committeeDTO));

    assertEquals("Committee authorization has been failed.", exception.getMessage());
  }

  /* ---------------------------- deleteCommitteeTemp ---------------------------- */

  @Test
  void deleteCommitteeTemp_WhenCommitteeExists_DeletesAssociatedRecords()
      throws ApiRequestException {
    when(committeeTempRepository.findById(5)).thenReturn(Optional.of(committeeTemp));

    StandardResponse<Boolean> response = committeeServiceImpl.deleteCommitteeTemp(5);

    assertNotNull(response);
    assertTrue(response.getSuccess());
    assertTrue((Boolean) response.getResponse());
    verify(levelUserTempRepository, times(1)).deleteByCommitteeTemp(committeeTemp);
    verify(levelTempRepository, times(1)).deleteByCommitteeTemp(committeeTemp);
    verify(committeeTempRepository, times(1)).deleteById(5);
  }

  @Test
  void deleteCommitteeTemp_WhenCommitteeNotFound_StillReturnsSuccess()
      throws ApiRequestException {
    when(committeeTempRepository.findById(99)).thenReturn(Optional.empty());

    StandardResponse<Boolean> response = committeeServiceImpl.deleteCommitteeTemp(99);

    assertNotNull(response);
    assertTrue(response.getSuccess());
    assertTrue((Boolean) response.getResponse());
    verify(levelUserTempRepository, never()).deleteByCommitteeTemp(any());
    verify(levelTempRepository, never()).deleteByCommitteeTemp(any());
    verify(committeeTempRepository, never()).deleteById(any());
  }

  @Test
  void deleteCommitteeTemp_WhenRepositoryThrowsException_ThrowsApiRequestException() {
    when(committeeTempRepository.findById(5)).thenThrow(new RuntimeException("DB error"));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.deleteCommitteeTemp(5));

    assertEquals("Remove temp committee has been failed.", exception.getMessage());
  }

  @Test
  void deleteCommitteeTemp_WhenSuccessful_ReturnsCorrectStandardResponseFields()
      throws ApiRequestException {
    when(committeeTempRepository.findById(5)).thenReturn(Optional.of(committeeTemp));

    StandardResponse<Boolean> response = committeeServiceImpl.deleteCommitteeTemp(5);

    assertEquals("Success", response.getMessage());
    assertTrue(response.getSuccess());
  }

  @Test
  void deleteCommitteeTemp_WhenDeletionOfChildRecordsFails_ThrowsApiRequestException() {
    when(committeeTempRepository.findById(5)).thenReturn(Optional.of(committeeTemp));
    doThrow(new RuntimeException("Constraint violation"))
        .when(levelUserTempRepository)
        .deleteByCommitteeTemp(committeeTemp);

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class, () -> committeeServiceImpl.deleteCommitteeTemp(5));

    assertEquals("Remove temp committee has been failed.", exception.getMessage());
  }

  /* ---------------------------- getCommitteeLevelsByCommittee ---------------------------- */

  @Test
  void getCommitteeLevelsByCommittee_WhenFound_ReturnsLevels() throws ApiRequestException {
    CommitteeLevelDTO levelDTO = buildLevelDTO(1, AppsConstants.RecordStatus.NEW, new ArrayList<>());
    CommitteeDTO found = new CommitteeDTO();
    found.setCommitteeId(5);
    found.setLevels(new ArrayList<>(List.of(levelDTO)));
    when(committeeJdbc.getCommittee(5, true)).thenReturn(found);

    ResponseEntity<StandardResponse<List<CommitteeLevelDTO>>> response =
        committeeServiceImpl.getCommitteeLevelsByCommittee(5);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getSuccess());
    assertEquals(1, ((List<?>) response.getBody().getResponse()).size());
  }

  @Test
  void getCommitteeLevelsByCommittee_WhenIdIsNull_ThrowsApiRequestException() {
    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> committeeServiceImpl.getCommitteeLevelsByCommittee(null));

    assertEquals("Committee Id is empty.", exception.getMessage());
  }

  @Test
  void getCommitteeLevelsByCommittee_WhenNotFound_ThrowsApiRequestException() {
    CommitteeDTO notFound = new CommitteeDTO();
    when(committeeJdbc.getCommittee(99, true)).thenReturn(notFound);

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> committeeServiceImpl.getCommitteeLevelsByCommittee(99));

    assertEquals("Committee not found.", exception.getMessage());
  }

  @Test
  void getCommitteeLevelsByCommittee_WhenJdbcThrowsWithMessage_PropagatesMessage() {
    when(committeeJdbc.getCommittee(5, true)).thenThrow(new ApiRequestException("DB failure"));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> committeeServiceImpl.getCommitteeLevelsByCommittee(5));

    assertEquals("DB failure", exception.getMessage());
  }

  @Test
  void getCommitteeLevelsByCommittee_WhenJdbcThrowsWithEmptyMessage_UsesDefaultMessage() {
    when(committeeJdbc.getCommittee(5, true)).thenThrow(new ApiRequestException(""));

    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> committeeServiceImpl.getCommitteeLevelsByCommittee(5));

    assertEquals(
        "An error occurred while fetching pending committee data.", exception.getMessage());
  }

  @Test
  void getCommitteeLevelsByCommittee_WhenNoLevelsExist_ReturnsEmptyList()
      throws ApiRequestException {
    CommitteeDTO found = new CommitteeDTO();
    found.setCommitteeId(5);
    found.setLevels(new ArrayList<>());
    when(committeeJdbc.getCommittee(5, true)).thenReturn(found);

    ResponseEntity<StandardResponse<List<CommitteeLevelDTO>>> response =
        committeeServiceImpl.getCommitteeLevelsByCommittee(5);

    assertTrue(((List<?>) response.getBody().getResponse()).isEmpty());
  }

  /* ---------------------------- saveMasterCommitteeHistory ---------------------------- */

  @Test
  void saveMasterCommitteeHistory_WithCompleteCommittee_MapsAllFieldsCorrectly() {
    committee.setCommitteeType(committeeType);
    committee.setApprovedBy("approver1");
    committee.setApprovedDate(new Date());
    committee.setModifiedBy("modifier1");
    committee.setLastModifiedDate(new Date());

    committeeServiceImpl.saveMasterCommitteeHistory(committee);

    ArgumentCaptor<CommitteeHistory> captor = ArgumentCaptor.forClass(CommitteeHistory.class);
    verify(committeeAudRepository, times(1)).save(captor.capture());

    CommitteeHistory history = captor.getValue();
    assertEquals(committee.getCommitteeId(), history.getCommitteeId());
    assertEquals(committee.getCommitteeName(), history.getCommitteeName());
    assertEquals(committee.getCommitteeType(), history.getCommitteeType());
    assertEquals(committee.getDelegatedAuthority(), history.getDelegatedAuthority());
    assertEquals(committee.getReviewer(), history.getReviewer());
    assertEquals(committee.getCurrentPath(), history.getCurrentPath());
    assertEquals(committee.getStatus(), history.getStatus());
    assertEquals(committee.getApproveStatus(), history.getApproveStatus());
    assertEquals(committee.getApprovedBy(), history.getApprovedBy());
    assertEquals(committee.getApprovedDate(), history.getApprovedDate());
    assertEquals(committee.getLastModifiedDate(), history.getLastModifiedDate());
    assertEquals(committee.getModifiedBy(), history.getModifiedBy());
    assertEquals(committee.getCreatedDate(), history.getCreatedDate());
    assertEquals(committee.getCreatedBy(), history.getCreatedBy());
  }

  @Test
  void saveMasterCommitteeHistory_WithNullCommitteeType_DoesNotThrowAndMapsNull() {
    committee.setCommitteeType(null);

    committeeServiceImpl.saveMasterCommitteeHistory(committee);

    ArgumentCaptor<CommitteeHistory> captor = ArgumentCaptor.forClass(CommitteeHistory.class);
    verify(committeeAudRepository, times(1)).save(captor.capture());
    assertNull(captor.getValue().getCommitteeType());
  }

  @Test
  void saveMasterCommitteeHistory_CallsAudRepositorySaveExactlyOnce() {
    committeeServiceImpl.saveMasterCommitteeHistory(committee);

    verify(committeeAudRepository, times(1)).save(any(CommitteeHistory.class));
    verify(committeeLevelAudRepository, never()).save(any());
    verify(levelUserAudRepository, never()).save(any());
  }

  @Test
  void saveMasterCommitteeHistory_WithRejectedApproveStatus_MapsApproveStatusCorrectly() {
    committee.setApproveStatus(MasterDataApproveStatus.REJECTED);

    committeeServiceImpl.saveMasterCommitteeHistory(committee);

    ArgumentCaptor<CommitteeHistory> captor = ArgumentCaptor.forClass(CommitteeHistory.class);
    verify(committeeAudRepository, times(1)).save(captor.capture());
    assertEquals(MasterDataApproveStatus.REJECTED, captor.getValue().getApproveStatus());
  }

  @Test
  void saveMasterCommitteeHistory_WithNullOptionalFields_DoesNotThrow() {
    committee.setReviewer(null);
    committee.setCurrentPath(null);
    committee.setApprovedBy(null);
    committee.setApprovedDate(null);
    committee.setModifiedBy(null);
    committee.setLastModifiedDate(null);

    assertDoesNotThrow(() -> committeeServiceImpl.saveMasterCommitteeHistory(committee));

    ArgumentCaptor<CommitteeHistory> captor = ArgumentCaptor.forClass(CommitteeHistory.class);
    verify(committeeAudRepository, times(1)).save(captor.capture());
    assertNull(captor.getValue().getReviewer());
    assertNull(captor.getValue().getApprovedBy());
  }
}
