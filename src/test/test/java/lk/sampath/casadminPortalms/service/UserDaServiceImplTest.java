package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.querydsl.core.BooleanBuilder;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.userda.UserDaDTO;
import lk.sampath.casadminportalms.entity.userda.UserDa;
import lk.sampath.casadminportalms.entity.userda.UserDaAud;
import lk.sampath.casadminportalms.entity.userda.UserDaTemp;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.userda.UserDaAudRepository;
import lk.sampath.casadminportalms.repository.userda.UserDaRepository;
import lk.sampath.casadminportalms.repository.userda.UserDaTempRepository;
import lk.sampath.casadminportalms.service.impl.UserDaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserDaServiceImplTest {

  @Mock private UserDaTempRepository userDaTempRepository;

  @Mock private UserDaRepository userDaRepository;

  @Mock private UserDaAudRepository userDaAudRepository;

  @InjectMocks private UserDaServiceImpl userDaService;

  private UserDaTemp userDaTemp;

  private UserDa userDa;

  private UserDaDTO userDaDTO;

  private ApproveRejectRQ approveRejectRQ;

  @BeforeEach
  void setup() {

    userDaTemp = new UserDaTemp();
    userDaTemp.setUserDaID(1);
    userDaTemp.setUserName("Unit Testing");
    userDaTemp.setDescription("Unit Testing Description Temp");
    userDaTemp.setMaxAmount(BigDecimal.valueOf(123));
    userDaTemp.setApproveStatus(MasterDataApproveStatus.PENDING);

    userDa = new UserDa();
    userDa.setUserDaID(1);
    userDa.setUserName("Unit Testing");
    userDa.setDescription("Unit Testing Description");
    userDa.setMaxAmount(BigDecimal.valueOf(123));
    userDa.setApproveStatus(MasterDataApproveStatus.APPROVED);

    userDaDTO = new UserDaDTO();
    userDaDTO.setUserDaID(1);
    userDaDTO.setUserName("Old Unit Testing");
    userDaDTO.setDescription("Old Unit Testing Description");
    userDaDTO.setMaxAmount(BigDecimal.valueOf(123));
    userDaDTO.setStatus(Status.INA);

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
  }

  /** findAllUserDaTempList * */
  @Test
  void testFindAllUserDaTempList_Success() {
    List<UserDaTemp> userDaTempList = Arrays.asList(userDaTemp);
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserDaTemp> page = new PageImpl<>(userDaTempList, pageable, userDaTempList.size());

    when(userDaTempRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllUserDaTempList(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(userDaTempRepository).findAll(pageable);
  }

  @Test
  void testFindAllUserDaTempList_EmptyList() throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserDaTemp> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
    when(userDaTempRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllUserDaTempList(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(userDaTempRepository).findAll(pageable);
  }

  @Test
  void testFindAllUserDaTempList_MultipleRecords() throws ApiRequestException {
    UserDaTemp secondTemp = new UserDaTemp();
    secondTemp.setUserDaID(2);
    secondTemp.setUserName("Second User");

    Pageable pageable = PageRequest.of(0, 10);
    List<UserDaTemp> userDaTempList = Arrays.asList(userDaTemp, secondTemp);
    Page<UserDaTemp> page = new PageImpl<>(userDaTempList, pageable, userDaTempList.size());

    when(userDaTempRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllUserDaTempList(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody().getResponse());
    assertEquals(2, ((Page<UserDaTemp>) response.getBody().getResponse()).getContent().size());

    verify(userDaTempRepository).findAll(pageable);
  }

  @Test
  void testFindAllUserDaTempList_VerifiesPageableArgumentPassedThrough() throws ApiRequestException {
    Pageable pageable = PageRequest.of(2, 5);
    Page<UserDaTemp> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
    when(userDaTempRepository.findAll(any(Pageable.class))).thenReturn(page);

    userDaService.findAllUserDaTempList(pageable);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(userDaTempRepository).findAll(pageableCaptor.capture());
    assertEquals(pageable, pageableCaptor.getValue());
  }

  @Test
  void testFindAllUserDaTempList_ResponseFieldsMatchSuccessCode() throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserDaTemp> page = new PageImpl<>(Arrays.asList(userDaTemp), pageable, 1);
    when(userDaTempRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllUserDaTempList(pageable);

    assertTrue(response.getBody().getSuccess());
    assertEquals("Success", response.getBody().getMessage());
  }

  /** findUserDaTempByID * */
  @Test
  void testFindUserDaTempById_Success() throws ApiRequestException {
    when(userDaTempRepository.findById(userDaTemp.getUserDaID()))
            .thenReturn(Optional.of(userDaTemp));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.findUserDaTempByID(userDaTemp.getUserDaID());

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(userDaTempRepository).findById(userDaTemp.getUserDaID());
  }

  @Test
  void testFindUserDaTempById_NotFound() {
    when(userDaTempRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.findUserDaTempByID(2);
                    });

    assertEquals("User Da with TEMP 2 does not exists", exception.getMessage());

    verify(userDaTempRepository).findById(2);
  }

  @Test
  void testFindUserDaTempById_ReturnsCorrectData() throws ApiRequestException {
    when(userDaTempRepository.findById(userDaTemp.getUserDaID()))
            .thenReturn(Optional.of(userDaTemp));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.findUserDaTempByID(userDaTemp.getUserDaID());

    UserDaTemp result = (UserDaTemp) response.getBody().getResponse();
    assertEquals(userDaTemp.getUserName(), result.getUserName());
    assertEquals(userDaTemp.getDescription(), result.getDescription());
    assertEquals(userDaTemp.getMaxAmount(), result.getMaxAmount());
  }

  @Test
  void testFindUserDaTempById_NotFound_DifferentId() {
    when(userDaTempRepository.findById(99)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.findUserDaTempByID(99);
                    });

    assertEquals("User Da with TEMP 99 does not exists", exception.getMessage());
    verify(userDaTempRepository).findById(99);
  }

  @Test
  void testFindUserDaTempById_DoesNotInteractWithOtherRepositories() throws ApiRequestException {
    when(userDaTempRepository.findById(userDaTemp.getUserDaID()))
            .thenReturn(Optional.of(userDaTemp));

    userDaService.findUserDaTempByID(userDaTemp.getUserDaID());

    verifyNoInteractions(userDaRepository);
    verifyNoInteractions(userDaAudRepository);
  }

  /** findAllApprovedUserDa * */
  @Test
  void testFindAllApprovedUserDaList_Success() {
    List<UserDa> userDaList = Arrays.asList(userDa);
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserDa> page = new PageImpl<>(userDaList, pageable, userDaList.size());

    when(userDaRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllApprovedUserDa(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(userDaRepository).findAll(pageable);
  }

  @Test
  void testFindAllApprovedUserDaList_EmptyList() throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserDa> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
    when(userDaRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllApprovedUserDa(pageable);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(userDaRepository).findAll(pageable);
  }

  @Test
  void testFindAllApprovedUserDaList_MultipleRecords() throws ApiRequestException {
    UserDa secondUserDa = new UserDa();
    secondUserDa.setUserDaID(2);
    secondUserDa.setUserName("Second Approved User");

    Pageable pageable = PageRequest.of(0, 10);
    List<UserDa> userDaList = Arrays.asList(userDa, secondUserDa);
    Page<UserDa> page = new PageImpl<>(userDaList, pageable, userDaList.size());

    when(userDaRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllApprovedUserDa(pageable);

    assertNotNull(response.getBody().getResponse());
    assertEquals(2, ((Page<UserDa>) response.getBody().getResponse()).getContent().size());

    verify(userDaRepository).findAll(pageable);
  }

  @Test
  void testFindAllApprovedUserDaList_VerifiesPageableArgumentPassedThrough()
          throws ApiRequestException {
    Pageable pageable = PageRequest.of(1, 20);
    Page<UserDa> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
    when(userDaRepository.findAll(any(Pageable.class))).thenReturn(page);

    userDaService.findAllApprovedUserDa(pageable);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(userDaRepository).findAll(pageableCaptor.capture());
    assertEquals(pageable, pageableCaptor.getValue());
  }

  @Test
  void testFindAllApprovedUserDaList_ResponseFieldsMatchSuccessCode() throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserDa> page = new PageImpl<>(Arrays.asList(userDa), pageable, 1);
    when(userDaRepository.findAll(pageable)).thenReturn(page);

    ResponseEntity<StandardResponse<List<UserDaDTO>>> response =
            userDaService.findAllApprovedUserDa(pageable);

    assertTrue(response.getBody().getSuccess());
    assertEquals("Success", response.getBody().getMessage());
  }

  /** findApprovedUserDaById * */
  @Test
  void testFindApprovedUserDaById_Success() throws ApiRequestException {
    when(userDaRepository.findById(userDa.getUserDaID())).thenReturn(Optional.of(userDa));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.findApprovedUserDaById(userDa.getUserDaID());

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(userDaRepository).findById(userDa.getUserDaID());
  }

  @Test
  void testFindApprovedUserDaById_NotFound() {
    when(userDaRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.findApprovedUserDaById(2);
                    });

    assertEquals("User Da with 2 does not exists", exception.getMessage());

    verify(userDaRepository).findById(2);
  }

  @Test
  void testFindApprovedUserDaById_ReturnsCorrectData() throws ApiRequestException {
    when(userDaRepository.findById(userDa.getUserDaID())).thenReturn(Optional.of(userDa));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.findApprovedUserDaById(userDa.getUserDaID());

    UserDa result = (UserDa) response.getBody().getResponse();
    assertEquals(userDa.getUserName(), result.getUserName());
    assertEquals(userDa.getDescription(), result.getDescription());
    assertEquals(userDa.getApproveStatus(), result.getApproveStatus());
  }

  @Test
  void testFindApprovedUserDaById_NotFound_DifferentId() {
    when(userDaRepository.findById(50)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.findApprovedUserDaById(50);
                    });

    assertEquals("User Da with 50 does not exists", exception.getMessage());
    verify(userDaRepository).findById(50);
  }

  @Test
  void testFindApprovedUserDaById_DoesNotInteractWithOtherRepositories()
          throws ApiRequestException {
    when(userDaRepository.findById(userDa.getUserDaID())).thenReturn(Optional.of(userDa));

    userDaService.findApprovedUserDaById(userDa.getUserDaID());

    verifyNoInteractions(userDaTempRepository);
    verifyNoInteractions(userDaAudRepository);
  }

  /** saveUserDaTemp * */
  @Test
  void testSaveUserDaTemp_Success() throws ApiRequestException {
    when(userDaTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    when(userDaTempRepository.getCurrentSequenceValue()).thenReturn(1);
    when(userDaTempRepository.saveAndFlush(any(UserDaTemp.class))).thenReturn(userDaTemp);

    ResponseEntity<StandardResponse<UserDaDTO>> response = userDaService.saveUserDaTemp(userDaDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    // Cast response object and verify fields
    UserDaTemp savedUser = (UserDaTemp) response.getBody().getResponse();
    assertEquals(userDaTemp.getUserName(), savedUser.getUserName());

    verify(userDaTempRepository).findAll(any(BooleanBuilder.class));
    verify(userDaTempRepository).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testSaveUserDaTemp_UserAlreadyExists() {
    when(userDaTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(List.of(userDaTemp));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.saveUserDaTemp(userDaDTO);
                    });

    assertEquals("UserDA Already Exists", exception.getMessage());

    verify(userDaTempRepository).findAll(any(BooleanBuilder.class));
    verify(userDaTempRepository, never()).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testSaveUserDaTemp_NullDTO() {
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.saveUserDaTemp(null);
                    });

    assertEquals("User cannot be empty or null.", exception.getMessage());
    verify(userDaTempRepository, never()).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testSaveUserDaTemp_BlankUserName() {
    userDaDTO.setUserName("   ");

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.saveUserDaTemp(userDaDTO);
                    });

    assertEquals("User cannot be empty or null.", exception.getMessage());
    verify(userDaTempRepository, never()).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testSaveUserDaTemp_NegativeCleanAmount() {
    userDaDTO.setCleanAmount(BigDecimal.valueOf(-1));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.saveUserDaTemp(userDaDTO);
                    });

    assertEquals("Clean amount should be positive!", exception.getMessage());
    verify(userDaTempRepository, never()).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testSaveUserDaTemp_UserNameAlreadyExistsInMaster() {
    when(userDaTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    when(userDaTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(userDaRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.saveUserDaTemp(userDaDTO);
                    });

    assertEquals(
            "User name '" + userDaDTO.getUserName() + "' already exists in the system.",
            exception.getMessage());
    verify(userDaTempRepository, never()).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testSaveUserDaTemp_SavesWithSuppliedFields() throws ApiRequestException {
    when(userDaTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());
    when(userDaTempRepository.getCurrentSequenceValue()).thenReturn(5);
    when(userDaTempRepository.saveAndFlush(any(UserDaTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UserDaDTO>> response = userDaService.saveUserDaTemp(userDaDTO);

    ArgumentCaptor<UserDaTemp> captor = ArgumentCaptor.forClass(UserDaTemp.class);
    verify(userDaTempRepository).saveAndFlush(captor.capture());

    UserDaTemp saved = captor.getValue();
    assertEquals(5, saved.getUserDaID());
    assertEquals(userDaDTO.getUserName(), saved.getUserName());
    assertEquals(userDaDTO.getDescription(), saved.getDescription());
    assertEquals(userDaDTO.getMaxAmount(), saved.getMaxAmount());
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /** approveRejectUserDa * */
  @Test
  void testApproveRejectUserDa_ApprovalSuccess() throws ApiRequestException {
    when(userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(userDaTemp));
    when(userDaRepository.findById(userDaTemp.getUserDaID())).thenReturn(Optional.of(userDa));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.approveRejectUserDa(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(userDaTempRepository).findById(approveRejectRQ.getApproveRejectDataID());
    verify(userDaTempRepository).save(userDaTemp);
  }

  /** approveRejectUserDa - Reject Path * */
  @Test
  void testApproveRejectUserDa_RejectionSuccess() throws ApiRequestException {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);
    when(userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(userDaTemp));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.approveRejectUserDa(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(userDaTempRepository).findById(approveRejectRQ.getApproveRejectDataID());
    verify(userDaTempRepository).save(userDaTemp);
  }

  /** approveRejectUserDa - Invalid Request * */
  @Test
  void testApproveRejectUserDa_InvalidRequest() {
    ApproveRejectRQ invalidRequest = new ApproveRejectRQ();
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.approveRejectUserDa(invalidRequest);
                    });
    assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
  }

  /** approveRejectUserDa - UserDaTemp Not Found * */
  @Test
  void testApproveRejectUserDa_UserDaTempNotFound() {
    when(userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.empty());
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.approveRejectUserDa(approveRejectRQ);
                    });
    assertEquals("User Da with TEMP 1 does not exists", exception.getMessage());
  }

  /** approveRejectUserDa - Unknown Status * */
  @Test
  void testApproveRejectUserDa_UnknownStatus() {
    approveRejectRQ.setApproveStatus(null);
    when(userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(userDaTemp));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.approveRejectUserDa(approveRejectRQ);
                    });
    assertTrue(exception.getMessage().contains("Unknown approval status"));
  }

  @Test
  void testHandleApproval_CallsMapUserDa_ForNewUser() throws ApiRequestException {
    when(userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(userDaTemp));
    when(userDaRepository.findById(userDaTemp.getUserDaID())).thenReturn(Optional.empty());
    when(userDaRepository.saveAndFlush(any(UserDa.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.approveRejectUserDa(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(userDaTempRepository).delete(userDaTemp);
    verify(userDaRepository).saveAndFlush(any(UserDa.class));
  }

  @Test
  void testHandleApproval_CallsUpdateUserDaToMaster_ForExistingUser() throws ApiRequestException {
    when(userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(userDaTemp));
    when(userDaRepository.findById(userDaTemp.getUserDaID())).thenReturn(Optional.of(userDa));
    when(userDaRepository.saveAndFlush(any(UserDa.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.approveRejectUserDa(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(userDaTempRepository).delete(userDaTemp);
    verify(userDaRepository).saveAndFlush(userDa);
  }

  /** updateUserDaTemp * */
  @Test
  void testUpdateUserDaTemp_Success() throws ApiRequestException {
    when(userDaTempRepository.findById(1)).thenReturn(Optional.of(userDaTemp));
    when(userDaTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.updateUserDaTemp(1, userDaDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(userDaTempRepository).save(userDaTemp);
  }

  /** updateUserDaTemp - UserDaTemp Not Found * */
  @Test
  void testUpdateUserDaTemp_UserDaTempNotFound() {
    when(userDaTempRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateUserDaTemp(1, userDaDTO);
                    });
    assertEquals("User Da with TEMP 1 does not exists", exception.getMessage());
  }

  @Test
  void testUpdateUserDaTemp_UserNameConflict() {
    UserDaTemp conflictingUser = new UserDaTemp();
    conflictingUser.setUserName(userDaDTO.getUserName());
    conflictingUser.setUserDaID(2);
    when(userDaTempRepository.findById(1)).thenReturn(Optional.of(userDaTemp));
    when(userDaTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(List.of(conflictingUser));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateUserDaTemp(1, userDaDTO);
                    });
    assertEquals("User Da with TEMP Old Unit Testing Already Exists", exception.getMessage());
  }

  @Test
  void testUpdateUserDaTemp_NegativeCleanAmount() {
    when(userDaTempRepository.findById(1)).thenReturn(Optional.of(userDaTemp));
    userDaDTO.setCleanAmount(BigDecimal.valueOf(-10));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateUserDaTemp(1, userDaDTO);
                    });

    assertEquals("Clean amount should be positive!", exception.getMessage());
    verify(userDaTempRepository, never()).save(any(UserDaTemp.class));
  }

  @Test
  void testUpdateUserDaTemp_BlankUserName() {
    userDaDTO.setUserName("   ");
    when(userDaTempRepository.findById(1)).thenReturn(Optional.of(userDaTemp));
    when(userDaTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(Collections.emptyList());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateUserDaTemp(1, userDaDTO);
                    });

    assertEquals("User name cannot be empty or null.", exception.getMessage());
    verify(userDaTempRepository, never()).save(any(UserDaTemp.class));
  }

  /** updateApprovedUserDa * */
  @Test
  void testUpdateApprovedUserDa_Success() throws ApiRequestException {
    when(userDaRepository.findById(userDaDTO.getUserDaID())).thenReturn(Optional.of(userDa));
    when(userDaTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(userDaRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(userDaTempRepository.saveAndFlush(any(UserDaTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.updateApprovedUserDa(1, userDaDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(userDaTempRepository).saveAndFlush(any(UserDaTemp.class));
  }

  /** updateApprovedUserDa - UserDa Not Found * */
  @Test
  void testUpdateApprovedUserDa_UserDaNotFound() {
    when(userDaRepository.findById(userDaDTO.getUserDaID())).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateApprovedUserDa(1, userDaDTO);
                    });
    assertEquals("User Da with 1 does not exists", exception.getMessage());
  }

  /** updateApprovedUserDa - UserName Conflict in Temporary Records * */
  @Test
  void testUpdateApprovedUserDa_UserNameConflictInTemp() {
    when(userDaRepository.findById(userDaDTO.getUserDaID())).thenReturn(Optional.of(userDa));
    when(userDaTempRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateApprovedUserDa(1, userDaDTO);
                    });
    assertEquals(
            "User name 'Old Unit Testing' already exists in the system.", exception.getMessage());
  }

  /** updateApprovedUserDa - UserName Conflict in Master Records * */
  @Test
  void testUpdateApprovedUserDa_UserNameConflictInMaster() {
    when(userDaRepository.findById(userDaDTO.getUserDaID())).thenReturn(Optional.of(userDa));
    when(userDaTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(userDaRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateApprovedUserDa(1, userDaDTO);
                    });
    assertEquals(
            "User name 'Old Unit Testing' already exists in the system.", exception.getMessage());
  }

  @Test
  void testUpdateApprovedUserDa_NegativeCleanAmount() {
    when(userDaRepository.findById(userDaDTO.getUserDaID())).thenReturn(Optional.of(userDa));
    userDaDTO.setCleanAmount(BigDecimal.valueOf(-5));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      userDaService.updateApprovedUserDa(1, userDaDTO);
                    });

    assertEquals("Clean amount should be positive!", exception.getMessage());
    verify(userDaTempRepository, never()).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testUpdateApprovedUserDa_SameUserNameSkipsUniquenessValidation() throws ApiRequestException {
    userDaDTO.setUserName(userDa.getUserName());
    when(userDaRepository.findById(userDaDTO.getUserDaID())).thenReturn(Optional.of(userDa));
    when(userDaTempRepository.saveAndFlush(any(UserDaTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.updateApprovedUserDa(1, userDaDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(userDaTempRepository, never()).exists(any(BooleanBuilder.class));
    verify(userDaRepository, never()).exists(any(BooleanBuilder.class));
    verify(userDaTempRepository).saveAndFlush(any(UserDaTemp.class));
  }

  @Test
  void testUpdateApprovedUserDa_MapsAllFieldsToTemp() throws ApiRequestException {
    when(userDaRepository.findById(userDaDTO.getUserDaID())).thenReturn(Optional.of(userDa));
    when(userDaTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(userDaRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(userDaTempRepository.saveAndFlush(any(UserDaTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    userDaService.updateApprovedUserDa(1, userDaDTO);

    ArgumentCaptor<UserDaTemp> captor = ArgumentCaptor.forClass(UserDaTemp.class);
    verify(userDaTempRepository).saveAndFlush(captor.capture());

    UserDaTemp mapped = captor.getValue();
    assertEquals(userDa.getUserDaID(), mapped.getUserDaID());
    assertEquals(userDaDTO.getUserName(), mapped.getUserName());
    assertEquals(userDaDTO.getDescription(), mapped.getDescription());
    assertEquals(userDaDTO.getMaxAmount(), mapped.getMaxAmount());
  }

  /** deleteUserDaFromTemp * */
  @Test
  void testDeleteUserDaFromTemp_Success() throws ApiRequestException {
    doNothing().when(userDaTempRepository).deleteById(1);

    ResponseEntity<StandardResponse<Void>> response = userDaService.deleteUserDaFromTemp(1);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getResponse());

    verify(userDaTempRepository).deleteById(1);
  }

  @Test
  void testDeleteUserDaFromTemp_VerifiesCorrectIdPassed() throws ApiRequestException {
    doNothing().when(userDaTempRepository).deleteById(7);

    ResponseEntity<StandardResponse<Void>> response = userDaService.deleteUserDaFromTemp(7);

    assertEquals(7, response.getBody().getResponse());
    ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(userDaTempRepository).deleteById(idCaptor.capture());
    assertEquals(7, idCaptor.getValue());
  }

  @Test
  void testDeleteUserDaFromTemp_DifferentId() throws ApiRequestException {
    doNothing().when(userDaTempRepository).deleteById(99);

    ResponseEntity<StandardResponse<Void>> response = userDaService.deleteUserDaFromTemp(99);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(99, response.getBody().getResponse());
    verify(userDaTempRepository).deleteById(99);
  }

  @Test
  void testDeleteUserDaFromTemp_RepositoryThrowsException_PropagatesException() {
    doThrow(new RuntimeException("Delete failed")).when(userDaTempRepository).deleteById(1);

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> {
                      userDaService.deleteUserDaFromTemp(1);
                    });

    assertEquals("Delete failed", exception.getMessage());
    verify(userDaTempRepository).deleteById(1);
  }

  @Test
  void testDeleteUserDaFromTemp_ResponseFieldsMatchSuccessCode() throws ApiRequestException {
    doNothing().when(userDaTempRepository).deleteById(1);

    ResponseEntity<StandardResponse<Void>> response = userDaService.deleteUserDaFromTemp(1);

    assertTrue(response.getBody().getSuccess());
    assertEquals("Success", response.getBody().getMessage());
  }

  @Test
  void testDeleteUserDaFromTemp_DoesNotInteractWithOtherRepositories() throws ApiRequestException {
    doNothing().when(userDaTempRepository).deleteById(1);

    userDaService.deleteUserDaFromTemp(1);

    verifyNoInteractions(userDaRepository);
    verifyNoInteractions(userDaAudRepository);
  }

  @Test
  void testApproveRejectUserDa_RejectionSuccess_WithAudit() throws ApiRequestException {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

    when(userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
            .thenReturn(Optional.of(userDaTemp));
    when(userDaAudRepository.save(any(UserDaAud.class)))
            .thenAnswer(
                    invocation -> {
                      UserDaAud audit = invocation.getArgument(0);
                      audit.setId(1);
                      return audit;
                    });

    ResponseEntity<StandardResponse<UserDaDTO>> response =
            userDaService.approveRejectUserDa(approveRejectRQ);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    verify(userDaTempRepository).findById(approveRejectRQ.getApproveRejectDataID());
    verify(userDaAudRepository).save(any(UserDaAud.class));

    ArgumentCaptor<UserDaAud> auditCaptor = ArgumentCaptor.forClass(UserDaAud.class);
    verify(userDaAudRepository).save(auditCaptor.capture());

    UserDaAud capturedAudit = auditCaptor.getValue();

    assertNotNull(capturedAudit);
    assertEquals(1, capturedAudit.getId());
    assertEquals(userDaTemp.getUserDaID(), capturedAudit.getUserDaID());
    assertEquals(userDaTemp.getStatus(), capturedAudit.getStatus());
    assertEquals(userDaTemp.getApproveStatus(), capturedAudit.getApproveStatus());
    assertEquals(userDaTemp.getUserName(), capturedAudit.getUserName());
    assertEquals(userDaTemp.getMaxAmount(), capturedAudit.getMaxAmount());
    assertEquals(userDaTemp.getDescription(), capturedAudit.getDescription());
  }
}