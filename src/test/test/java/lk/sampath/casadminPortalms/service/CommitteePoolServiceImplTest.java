package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolDTO;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolResp;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolHistory;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.committee.CommitteeRepository;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolHistoryRepository;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolJdbc;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolRepository;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolTempRepository;
import lk.sampath.casadminportalms.service.CommitteeService;
import lk.sampath.casadminportalms.service.impl.CommitteePoolServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CommitteePoolServiceImplTest {

    @Mock private CommitteePoolRepository committeePoolRepository;

    @Mock private CommitteePoolTempRepository committeePoolTempRepository;

    @Mock private CommitteePoolHistoryRepository committeePoolHistoryRepository;

    @Mock private CommitteeRepository committeeRepository;

    @Mock private CommitteePoolJdbc committeePoolJdbc;

    @Mock private CommitteeService committeeService;

    @InjectMocks private CommitteePoolServiceImpl committeePoolService;

    private CommitteePoolDTO committeePoolDTO;

    private CommitteePool committeePool;

    private CommitteePoolTemp committeePoolTemp;

    private Committee committee;

    @BeforeEach
    void setup() {
        UserContext.setUsername("unit.test.user");

        Date date = new Date();

        committeePoolDTO = new CommitteePoolDTO();
        committeePoolDTO.setCommitteePoolId(100);
        committeePoolDTO.setUserId(1);
        committeePoolDTO.setUserName("jane.doe");
        committeePoolDTO.setUserDisplayName("Jane Doe");
        committeePoolDTO.setDesignation("Officer");
        committeePoolDTO.setWorkClass("10");
        committeePoolDTO.setStatus(AppsConstants.Status.ACT);
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);
        committeePoolDTO.setCreatedBy("creator");
        committeePoolDTO.setCreatedDate(date);
        committeePoolDTO.setModifiedBy("modifier");
        committeePoolDTO.setLastModifiedDate(date);

        committeePool = new CommitteePool();
        committeePool.setUserId(1);
        committeePool.setPoolId(100);
        committeePool.setUserName("jane.doe");
        committeePool.setUserDisplayName("Jane Doe");
        committeePool.setReferenceName("Officer");
        committeePool.setGroupCode("10");
        committeePool.setUserStatus(AppsConstants.Status.ACT);
        committeePool.setApproveStatus(MasterDataApproveStatus.APPROVED);

        committeePoolTemp = new CommitteePoolTemp();
        committeePoolTemp.setUserId(1);
        committeePoolTemp.setPoolId(100);
        committeePoolTemp.setUserName("jane.doe");
        committeePoolTemp.setUserDisplayName("Jane Doe");
        committeePoolTemp.setReferenceName("Officer");
        committeePoolTemp.setGroupCode("10");
        committeePoolTemp.setUserStatus(AppsConstants.Status.ACT);
        committeePoolTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
        committeePoolTemp.setModifiedBy("modifier");
        committeePoolTemp.setLastModifiedDate(date);

        committee = new Committee();
        committee.setCommitteeId(500);
        committee.setCommitteeName("Board Credit Committee");

        when(committeePoolJdbc.findAllCommitteePoolTempList()).thenReturn(new ArrayList<>());
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    /** getTempCommitteePool * */
    @Test
    void getTempCommitteePool_whenUsersExist_shouldReturnSuccessResponse() {
        List<CommitteePoolDTO> tempList = Arrays.asList(committeePoolDTO);
        when(committeePoolJdbc.findAllCommitteePoolTempList()).thenReturn(tempList);

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.getTempCommitteePool();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(tempList, response.getBody().getResponse());

        verify(committeePoolJdbc).findAllCommitteePoolTempList();
    }

    @Test
    void getTempCommitteePool_whenNoUsers_shouldReturnEmptyList() {
        when(committeePoolJdbc.findAllCommitteePoolTempList()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.getTempCommitteePool();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Collections.emptyList(), response.getBody().getResponse());

        verify(committeePoolJdbc).findAllCommitteePoolTempList();
    }

    @Test
    void getTempCommitteePool_whenMultipleUsersExist_shouldReturnAllOfThem() {
        CommitteePoolDTO secondDto = new CommitteePoolDTO();
        secondDto.setCommitteePoolId(200);
        secondDto.setUserName("second.user");
        List<CommitteePoolDTO> tempList = Arrays.asList(committeePoolDTO, secondDto);
        when(committeePoolJdbc.findAllCommitteePoolTempList()).thenReturn(tempList);

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.getTempCommitteePool();

        assertNotNull(response);
        List<?> resultList = (List<?>) response.getBody().getResponse();
        assertEquals(2, resultList.size());
    }

    @Test
    void getTempCommitteePool_whenRepositoryThrows_shouldWrapInApiRequestException() {
        when(committeePoolJdbc.findAllCommitteePoolTempList())
                .thenThrow(new RuntimeException("DB connection lost"));

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class, () -> committeePoolService.getTempCommitteePool());

        assertEquals("An error occurred.", exception.getMessage());
    }

    @Test
    void getTempCommitteePool_shouldNotTouchOtherRepositories() {
        committeePoolService.getTempCommitteePool();

        verifyNoInteractions(committeePoolRepository);
        verifyNoInteractions(committeePoolTempRepository);
        verifyNoInteractions(committeePoolHistoryRepository);
        verify(committeePoolJdbc, never()).findAllCommitteePoolList();
    }

    /** getCommitteePool * */
    @Test
    void getCommitteePool_whenUsersExist_shouldReturnSuccessResponse() {
        List<CommitteePoolDTO> poolList = Arrays.asList(committeePoolDTO);
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(poolList);

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.getCommitteePool();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(poolList, response.getBody().getResponse());

        verify(committeePoolJdbc).findAllCommitteePoolList();
    }

    @Test
    void getCommitteePool_whenNoUsers_shouldReturnEmptyList() {
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.getCommitteePool();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody().getResponse());
    }

    @Test
    void getCommitteePool_whenMultipleUsersExist_shouldReturnAllOfThem() {
        CommitteePoolDTO secondDto = new CommitteePoolDTO();
        secondDto.setCommitteePoolId(300);
        secondDto.setUserName("third.user");
        List<CommitteePoolDTO> poolList = Arrays.asList(committeePoolDTO, secondDto);
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(poolList);

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.getCommitteePool();

        List<?> resultList = (List<?>) response.getBody().getResponse();
        assertEquals(2, resultList.size());
    }

    @Test
    void getCommitteePool_whenRepositoryThrows_shouldWrapInApiRequestException() {
        when(committeePoolJdbc.findAllCommitteePoolList())
                .thenThrow(new RuntimeException("DB connection lost"));

        ApiRequestException exception =
                assertThrows(ApiRequestException.class, () -> committeePoolService.getCommitteePool());

        assertEquals("An error occurred.", exception.getMessage());
    }

    @Test
    void getCommitteePool_shouldNotTouchOtherRepositories() {
        committeePoolService.getCommitteePool();

        verifyNoInteractions(committeePoolRepository);
        verifyNoInteractions(committeePoolTempRepository);
        verifyNoInteractions(committeePoolHistoryRepository);
        verify(committeePoolJdbc, never()).findAllCommitteePoolTempList();
    }

    /** saveCommitteePoolUsers * */
    @Test
    void saveCommitteePoolUsers_whenSingleUser_shouldSaveAndReturnUpdatedList() {
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(new ArrayList<>());
        when(committeePoolRepository.save(any(CommitteePool.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.saveCommitteePoolUsers(List.of(committeePoolDTO));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<?> resultList = (List<?>) response.getBody().getResponse();
        assertEquals(1, resultList.size());

        verify(committeePoolRepository).save(any(CommitteePool.class));
        verify(committeePoolHistoryRepository).save(any(CommitteePoolHistory.class));
    }

    @Test
    void saveCommitteePoolUsers_whenMultipleUsers_shouldSaveEachOne() {
        CommitteePoolDTO secondDto = new CommitteePoolDTO();
        secondDto.setCommitteePoolId(200);
        secondDto.setUserName("second.user");
        secondDto.setDesignation("Manager");
        secondDto.setWorkClass("20");

        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(new ArrayList<>());
        when(committeePoolRepository.save(any(CommitteePool.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.saveCommitteePoolUsers(List.of(committeePoolDTO, secondDto));

        assertNotNull(response);
        List<?> resultList = (List<?>) response.getBody().getResponse();
        assertEquals(2, resultList.size());

        verify(committeePoolRepository, times(2)).save(any(CommitteePool.class));
        verify(committeePoolHistoryRepository, times(2)).save(any(CommitteePoolHistory.class));
    }

    @Test
    void saveCommitteePoolUsers_whenEmptyList_shouldReturnExistingListWithoutSaving() {
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(new ArrayList<>());

        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
                committeePoolService.saveCommitteePoolUsers(Collections.emptyList());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> resultList = (List<?>) response.getBody().getResponse();
        assertTrue(resultList.isEmpty());

        verify(committeePoolRepository, never()).save(any(CommitteePool.class));
        verify(committeePoolHistoryRepository, never()).save(any(CommitteePoolHistory.class));
    }

    @Test
    void saveCommitteePoolUsers_whenRepositoryThrows_shouldWrapInApiRequestException() {
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(new ArrayList<>());
        when(committeePoolRepository.save(any(CommitteePool.class)))
                .thenThrow(new RuntimeException("save failed"));

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> committeePoolService.saveCommitteePoolUsers(List.of(committeePoolDTO)));

        assertEquals("Pool user(s) adding has been failed.", exception.getMessage());
    }

    @Test
    void saveCommitteePoolUsers_shouldMapDtoFieldsAndForceApprovedStatus() {
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(new ArrayList<>());
        when(committeePoolRepository.save(any(CommitteePool.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        committeePoolService.saveCommitteePoolUsers(List.of(committeePoolDTO));

        ArgumentCaptor<CommitteePool> captor = ArgumentCaptor.forClass(CommitteePool.class);
        verify(committeePoolRepository).save(captor.capture());

        CommitteePool saved = captor.getValue();
        assertEquals(committeePoolDTO.getCommitteePoolId(), saved.getPoolId());
        assertEquals(committeePoolDTO.getUserName(), saved.getUserName());
        assertEquals(committeePoolDTO.getDesignation(), saved.getReferenceName());
        assertEquals(committeePoolDTO.getWorkClass(), saved.getGroupCode());
        assertEquals(committeePoolDTO.getUserDisplayName(), saved.getUserDisplayName());
        assertEquals(AppsConstants.Status.ACT, saved.getUserStatus());
        assertEquals(MasterDataApproveStatus.APPROVED, saved.getApproveStatus());
        assertEquals("unit.test.user", saved.getApprovedBy());
        assertNotNull(saved.getApprovedDate());
    }

    @Test
    void saveCommitteePoolUsers_shouldFetchExistingPoolListOnlyOnce() {
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(new ArrayList<>());
        when(committeePoolRepository.save(any(CommitteePool.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        committeePoolService.saveCommitteePoolUsers(List.of(committeePoolDTO));

        verify(committeePoolJdbc).findAllCommitteePoolList();
    }

    /** saveTempCommitteePoolUser * */
    @Test
    void saveTempCommitteePoolUser_whenStatusActive_shouldSetApproveStatusPending() {
        committeePoolDTO.setStatus(AppsConstants.Status.ACT);
        when(committeePoolTempRepository.saveAndFlush(any(CommitteePoolTemp.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<CommitteePoolResp>> response =
                committeePoolService.saveTempCommitteePoolUser(committeePoolDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ArgumentCaptor<CommitteePoolTemp> captor = ArgumentCaptor.forClass(CommitteePoolTemp.class);
        verify(committeePoolTempRepository).saveAndFlush(captor.capture());
        assertEquals(MasterDataApproveStatus.PENDING, captor.getValue().getApproveStatus());
    }

    @Test
    void saveTempCommitteePoolUser_whenStatusRemove_shouldSetApproveStatusPendingRmv() {
        committeePoolDTO.setStatus(AppsConstants.Status.RMV);
        when(committeePoolTempRepository.saveAndFlush(any(CommitteePoolTemp.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        committeePoolService.saveTempCommitteePoolUser(committeePoolDTO);

        ArgumentCaptor<CommitteePoolTemp> captor = ArgumentCaptor.forClass(CommitteePoolTemp.class);
        verify(committeePoolTempRepository).saveAndFlush(captor.capture());
        assertEquals(MasterDataApproveStatus.PENDING_RMV, captor.getValue().getApproveStatus());
    }

    @Test
    void saveTempCommitteePoolUser_whenRepositoryThrows_shouldWrapInApiRequestException() {
        when(committeePoolTempRepository.saveAndFlush(any(CommitteePoolTemp.class)))
                .thenThrow(new RuntimeException("save failed"));

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> committeePoolService.saveTempCommitteePoolUser(committeePoolDTO));

        assertEquals("Pool user adding has been failed.", exception.getMessage());
    }

    @Test
    void saveTempCommitteePoolUser_whenStatusIsNull_shouldThrowApiRequestException() {
        committeePoolDTO.setStatus(null);

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> committeePoolService.saveTempCommitteePoolUser(committeePoolDTO));

        assertEquals("Pool user adding has been failed.", exception.getMessage());
        verify(committeePoolTempRepository, never()).saveAndFlush(any(CommitteePoolTemp.class));
    }

    @Test
    void saveTempCommitteePoolUser_shouldReturnCommitteePoolRespWithBothLists() {
        List<CommitteePoolDTO> tempList = Arrays.asList(committeePoolDTO);
        List<CommitteePoolDTO> masterList = Arrays.asList(committeePoolDTO);
        when(committeePoolJdbc.findAllCommitteePoolTempList()).thenReturn(tempList);
        when(committeePoolJdbc.findAllCommitteePoolList()).thenReturn(masterList);
        when(committeePoolTempRepository.saveAndFlush(any(CommitteePoolTemp.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<CommitteePoolResp>> response =
                committeePoolService.saveTempCommitteePoolUser(committeePoolDTO);

        CommitteePoolResp resp = (CommitteePoolResp) response.getBody().getResponse();
        assertEquals(tempList, resp.getCommitteePoolTempList());
        assertEquals(masterList, resp.getCommitteePoolList());

        verify(committeePoolJdbc).findAllCommitteePoolTempList();
        verify(committeePoolJdbc).findAllCommitteePoolList();
    }

    @Test
    void saveTempCommitteePoolUser_shouldMapAllDtoFieldsOntoTempEntity() {
        when(committeePoolTempRepository.saveAndFlush(any(CommitteePoolTemp.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        committeePoolService.saveTempCommitteePoolUser(committeePoolDTO);

        ArgumentCaptor<CommitteePoolTemp> captor = ArgumentCaptor.forClass(CommitteePoolTemp.class);
        verify(committeePoolTempRepository).saveAndFlush(captor.capture());

        CommitteePoolTemp saved = captor.getValue();
        assertEquals(committeePoolDTO.getUserId(), saved.getUserId());
        assertEquals(committeePoolDTO.getCommitteePoolId(), saved.getPoolId());
        assertEquals(committeePoolDTO.getUserName(), saved.getUserName());
        assertEquals(committeePoolDTO.getDesignation(), saved.getReferenceName());
        assertEquals(committeePoolDTO.getWorkClass(), saved.getGroupCode());
        assertEquals(committeePoolDTO.getUserDisplayName(), saved.getUserDisplayName());
        assertEquals(committeePoolDTO.getStatus(), saved.getUserStatus());
        assertEquals("unit.test.user", saved.getModifiedBy());
        assertNotNull(saved.getLastModifiedDate());
    }

    /** approveRejectPoolUser * */
    @Test
    void approveRejectPoolUser_whenUserNotFound_shouldThrowApiRequestException() {
        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.empty());

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> committeePoolService.approveRejectPoolUser(committeePoolDTO));

        assertEquals("Committee pool user not found.", exception.getMessage());
        verify(committeePoolTempRepository, never()).deleteById(any());
    }

    @Test
    void approveRejectPoolUser_whenApprovedAndRemovalWithNoCommittees_shouldDeleteUser() {
        committeePoolTemp.setUserStatus(AppsConstants.Status.RMV);
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);

        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.of(committeePool));
        when(committeePoolTempRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePoolTemp);
        when(committeePoolRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePool);
        when(committeePoolJdbc.getCommitteesByUserName(committeePoolTemp.getUserName()))
                .thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<CommitteePoolResp>> response =
                committeePoolService.approveRejectPoolUser(committeePoolDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(committeePoolRepository).deleteById(committeePoolTemp.getUserId());
        verify(committeePoolHistoryRepository).save(any(CommitteePoolHistory.class));
        verify(committeePoolTempRepository).deleteById(committeePoolDTO.getUserId());
        verify(committeePoolTempRepository).flush();
        verify(committeePoolRepository, never()).saveAndFlush(any(CommitteePool.class));
    }

    @Test
    void approveRejectPoolUser_whenApprovedRemovalButUserInCommittee_shouldThrowWithCommitteeNames() {
        committeePoolTemp.setUserStatus(AppsConstants.Status.RMV);
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);

        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.of(committeePool));
        when(committeePoolTempRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePoolTemp);
        when(committeePoolRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePool);
        when(committeePoolJdbc.getCommitteesByUserName(committeePoolTemp.getUserName()))
                .thenReturn(List.of(committee));

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> committeePoolService.approveRejectPoolUser(committeePoolDTO));

        assertEquals(
                "This pool user is a member of the committee. These are the committee(s): "
                        + "Board Credit Committee",
                exception.getMessage());

        verify(committeePoolRepository, never()).deleteById(any());
        verify(committeePoolTempRepository, never()).deleteById(any());
    }

    @Test
    void approveRejectPoolUser_whenApprovedAndNotRemoval_shouldUpdateMasterRecord() {
        committeePoolTemp.setUserStatus(AppsConstants.Status.ACT);
        committeePool.setGroupCode("10");
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);

        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.of(committeePool));
        when(committeePoolTempRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePoolTemp);
        when(committeePoolRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePool);
        when(committeePoolRepository.saveAndFlush(any(CommitteePool.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        committeePoolService.approveRejectPoolUser(committeePoolDTO);

        verify(committeePoolRepository).saveAndFlush(any(CommitteePool.class));
        verify(committeePoolHistoryRepository).save(any(CommitteePoolHistory.class));
        verifyNoInteractions(committeeRepository);
        verifyNoInteractions(committeeService);
    }

    @Test
    void approveRejectPoolUser_whenApprovedActiveInSpecialGroup_shouldChangePathToRegular() {
        committeePoolTemp.setUserStatus(AppsConstants.Status.ACT);
        committeePool.setGroupCode("80");
        committeePool.setUserName("jane.doe");
        committeePoolDTO.setStatus(AppsConstants.Status.ACT);
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);

        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.of(committeePool));
        when(committeePoolTempRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePoolTemp);
        when(committeePoolRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePool);
        when(committeePoolRepository.saveAndFlush(any(CommitteePool.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(committeePoolJdbc.getCommitteesByUserName("jane.doe")).thenReturn(List.of(committee));
        when(committeeRepository.getReferenceById(committee.getCommitteeId())).thenReturn(committee);
        when(committeeRepository.saveAndFlush(any(Committee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        committeePoolService.approveRejectPoolUser(committeePoolDTO);

        ArgumentCaptor<Committee> captor = ArgumentCaptor.forClass(Committee.class);
        verify(committeeRepository).saveAndFlush(captor.capture());
        assertEquals(AppsConstants.CAPathType.REG, captor.getValue().getCurrentPath());
        verify(committeeService).saveMasterCommitteeHistory(any(Committee.class));
        verify(committeePoolJdbc, never()).changeCommitteePaperCurrentLevel(any(), any());
    }

    @Test
    void approveRejectPoolUser_whenApprovedInactiveInSpecialGroup_shouldChangePathToAlternative() {
        committeePoolTemp.setUserStatus(AppsConstants.Status.ACT);
        committeePool.setGroupCode("80");
        committeePool.setUserName("jane.doe");
        committeePoolDTO.setStatus(AppsConstants.Status.INA);
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);

        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.of(committeePool));
        when(committeePoolTempRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePoolTemp);
        when(committeePoolRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePool);
        when(committeePoolRepository.saveAndFlush(any(CommitteePool.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(committeePoolJdbc.getCommitteesByUserName("jane.doe")).thenReturn(List.of(committee));
        when(committeeRepository.getReferenceById(committee.getCommitteeId())).thenReturn(committee);
        when(committeeRepository.saveAndFlush(any(Committee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(committeePoolJdbc.changeCommitteePaperCurrentLevel(
                committee.getCommitteeId(), AppsConstants.CAPathType.ALT))
                .thenReturn(1);

        committeePoolService.approveRejectPoolUser(committeePoolDTO);

        ArgumentCaptor<Committee> captor = ArgumentCaptor.forClass(Committee.class);
        verify(committeeRepository).saveAndFlush(captor.capture());
        assertEquals(AppsConstants.CAPathType.ALT, captor.getValue().getCurrentPath());
        verify(committeePoolJdbc)
                .changeCommitteePaperCurrentLevel(committee.getCommitteeId(), AppsConstants.CAPathType.ALT);
        verify(committeeService).saveMasterCommitteeHistory(any(Committee.class));
    }

    @Test
    void approveRejectPoolUser_whenRejected_shouldUpdateTempHistoryOnlyAndNotMaster() {
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.REJECTED);

        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.of(committeePool));
        when(committeePoolTempRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePoolTemp);
        when(committeePoolRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePool);

        ResponseEntity<StandardResponse<CommitteePoolResp>> response =
                committeePoolService.approveRejectPoolUser(committeePoolDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ArgumentCaptor<CommitteePoolHistory> captor =
                ArgumentCaptor.forClass(CommitteePoolHistory.class);
        verify(committeePoolHistoryRepository).save(captor.capture());
        assertEquals(MasterDataApproveStatus.REJECTED, captor.getValue().getApproveStatus());
        assertEquals("unit.test.user", captor.getValue().getApprovedBy());
        assertNotNull(captor.getValue().getApprovedDate());

        verify(committeePoolRepository, never()).saveAndFlush(any(CommitteePool.class));
        verify(committeePoolTempRepository).deleteById(committeePoolDTO.getUserId());
        verify(committeePoolTempRepository).flush();
    }

    @Test
    void approveRejectPoolUser_shouldAlwaysDeleteTempRecordAfterProcessing() {
        committeePoolDTO.setApproveStatus(MasterDataApproveStatus.REJECTED);

        when(committeePoolRepository.findById(committeePoolDTO.getUserId()))
                .thenReturn(Optional.of(committeePool));
        when(committeePoolTempRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePoolTemp);
        when(committeePoolRepository.getReferenceById(committeePoolDTO.getUserId()))
                .thenReturn(committeePool);

        committeePoolService.approveRejectPoolUser(committeePoolDTO);

        verify(committeePoolTempRepository).deleteById(committeePoolDTO.getUserId());
        verify(committeePoolTempRepository).flush();
    }
}