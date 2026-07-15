package lk.sampath.casadminportalms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolDTO;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolResp;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolHistory;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.committee.CommitteeRepository;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolHistoryRepository;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolJdbc;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolRepository;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolTempRepository;
import lk.sampath.casadminportalms.service.CommitteePoolService;
import lk.sampath.casadminportalms.service.CommitteeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class CommitteePoolServiceImpl implements CommitteePoolService {

  private final CommitteePoolRepository committeePoolRepository;

  private final CommitteePoolTempRepository committeePoolTempRepository;

  private final CommitteePoolHistoryRepository committeePoolHistoryRepository;

  private final CommitteeRepository committeeRepository;

  private final CommitteePoolJdbc committeePoolJdbc;

  private final CommitteeService committeeService;

  public CommitteePoolServiceImpl(
      CommitteePoolRepository committeePoolRepository,
      CommitteePoolTempRepository committeePoolTempRepository,
      CommitteePoolHistoryRepository committeePoolHistoryRepository,
      CommitteeRepository committeeRepository,
      CommitteePoolJdbc committeePoolJdbc,
      CommitteeService committeeService) {
    this.committeePoolRepository = committeePoolRepository;
    this.committeePoolTempRepository = committeePoolTempRepository;
    this.committeePoolHistoryRepository = committeePoolHistoryRepository;
    this.committeeRepository = committeeRepository;
    this.committeePoolJdbc = committeePoolJdbc;
    this.committeeService = committeeService;
  }

  @Override
  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool()
      throws ApiRequestException {
    StandardResponse<List<CommitteePoolDTO>> response;
    try {
      List<CommitteePoolDTO> committeePoolTemps = committeePoolJdbc.findAllCommitteePoolTempList();
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
              committeePoolTemps);
    } catch (Exception e) {
      log.error("ERROR : Get All Temp Pool Users: ", e);
      throw new ApiRequestException("An error occurred.");
    }

    return ResponseEntity.ok().body(response);
  }

  @Override
  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool()
      throws ApiRequestException {
    StandardResponse<List<CommitteePoolDTO>> response;
    try {
      List<CommitteePoolDTO> committeePools = committeePoolJdbc.findAllCommitteePoolList();
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
              committeePools);
    } catch (Exception e) {
      log.error("ERROR : Get All Pool Users: ", e);
      throw new ApiRequestException("An error occurred.");
    }
    return ResponseEntity.ok().body(response);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> saveCommitteePoolUsers(
      List<CommitteePoolDTO> committeePoolUsers) throws ApiRequestException {
    StandardResponse<List<CommitteePoolDTO>> response;
    try {
      Date date = new Date();
      List<CommitteePoolDTO> prevUsers = committeePoolJdbc.findAllCommitteePoolList();

      for (CommitteePoolDTO committeePoolUser : committeePoolUsers) {
        CommitteePool committeePool = new CommitteePool();

        committeePool.setPoolId(committeePoolUser.getCommitteePoolId());
        committeePool.setUserName(committeePoolUser.getUserName());
        committeePool.setReferenceName(committeePoolUser.getDesignation());
        committeePool.setGroupCode(committeePoolUser.getWorkClass());
        committeePool.setUserDisplayName(committeePoolUser.getUserDisplayName());
        committeePool.setUserStatus(AppsConstants.Status.ACT);
        committeePool.setApproveStatus(MasterDataApproveStatus.APPROVED);
        committeePool.setApprovedDate(date);
        committeePool.setApprovedBy(UserContext.getUsername());

        committeePool = committeePoolRepository.save(committeePool);

        prevUsers.add(new CommitteePoolDTO(committeePool));

        // save to history
        saveCommitteePoolHistory(new CommitteePoolDTO(committeePool));
      }
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), prevUsers);
    } catch (Exception e) {
      log.error("ERROR : Save Pool Users: ", e);
      throw new ApiRequestException("Pool user(s) adding has been failed.");
    }
    return ResponseEntity.ok().body(response);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<CommitteePoolResp>> saveTempCommitteePoolUser(
      CommitteePoolDTO committeePoolUser) throws ApiRequestException {

    StandardResponse<CommitteePoolResp> response;
    try {
      Date date = new Date();
      CommitteePoolTemp committeePool = new CommitteePoolTemp();

      committeePool.setUserId(committeePoolUser.getUserId());
      committeePool.setPoolId(committeePoolUser.getCommitteePoolId());
      committeePool.setUserName(committeePoolUser.getUserName());
      committeePool.setReferenceName(committeePoolUser.getDesignation());
      committeePool.setGroupCode(committeePoolUser.getWorkClass());
      committeePool.setUserDisplayName(committeePoolUser.getUserDisplayName());
      committeePool.setCreatedDate(committeePoolUser.getCreatedDate());
      committeePool.setCreatedBy(committeePoolUser.getCreatedBy());
      committeePool.setUserStatus(committeePoolUser.getStatus());
      committeePool.setLastModifiedDate(date);
      committeePool.setModifiedBy(UserContext.getUsername());

      if (committeePoolUser.getStatus().equals(AppsConstants.Status.RMV)) {
        committeePool.setApproveStatus(MasterDataApproveStatus.PENDING_RMV);
      } else {
        committeePool.setApproveStatus(MasterDataApproveStatus.PENDING);
      }

      committeePoolTempRepository.saveAndFlush(committeePool);

      CommitteePoolResp committeePoolResp = prepareCommitteePoolResponse();
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
              committeePoolResp);
    } catch (Exception e) {
      log.error("ERROR : Save Temp Pool Users: ", e);
      throw new ApiRequestException("Pool user adding has been failed.");
    }
    return ResponseEntity.ok().body(response);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<CommitteePoolResp>> approveRejectPoolUser(
      CommitteePoolDTO committeePoolUser) throws ApiRequestException {

    StandardResponse<CommitteePoolResp> response;
    try {

      CommitteePool committeePool;
      if (committeePoolRepository.findById(committeePoolUser.getUserId()).isPresent()) {
        CommitteePoolTemp committeePoolTemp =
            committeePoolTempRepository.getReferenceById(committeePoolUser.getUserId());
        committeePool = committeePoolRepository.getReferenceById(committeePoolUser.getUserId());

        if (committeePoolUser.getApproveStatus() == MasterDataApproveStatus.APPROVED) {
          if (committeePoolTemp.getUserStatus().equals(AppsConstants.Status.RMV)) {
            processUserRemoval(committeePoolTemp);
          } else {
            updateCommitteePool(committeePool, committeePoolTemp, committeePoolUser);
          }
        } else {
          updateHistoryWithPoolTemp(committeePoolTemp, committeePoolUser.getApproveStatus());
        }

        // remove temp record
        committeePoolTempRepository.deleteById(committeePoolUser.getUserId());
        committeePoolTempRepository.flush();
      } else {
        throw new ApiRequestException("Committee pool user not found.");
      }

      CommitteePoolResp committeePoolResp = prepareCommitteePoolResponse();
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
              committeePoolResp);
    } catch (Exception e) {
      log.error(
          "ERROR : CommitteePoolServiceImpl | approveRejectPoolUser| Approve or Reject Pool Users: ",
          e);
      String message =
          !e.getMessage().isEmpty() ? e.getMessage() : "Pool user approve/reject has been failed.";
      throw new ApiRequestException(message);
    }
    return ResponseEntity.ok().body(response);
  }

  public void saveCommitteePoolHistory(CommitteePoolDTO committeePoolUser)
      throws ApiRequestException {

    CommitteePoolHistory committeePool = new CommitteePoolHistory();

    committeePool.setUserId(committeePoolUser.getUserId());
    committeePool.setPoolId(committeePoolUser.getCommitteePoolId());
    committeePool.setUserName(committeePoolUser.getUserName());
    committeePool.setDesignation(committeePoolUser.getDesignation());
    committeePool.setWorkClass(committeePoolUser.getWorkClass());
    committeePool.setUserDisplayName(committeePoolUser.getUserDisplayName());
    committeePool.setStatus(committeePoolUser.getStatus());
    committeePool.setCreatedDate(committeePoolUser.getCreatedDate());
    committeePool.setCreatedBy(committeePoolUser.getCreatedBy());
    committeePool.setLastModifiedDate(committeePoolUser.getLastModifiedDate());
    committeePool.setModifiedBy(committeePoolUser.getModifiedBy());
    committeePool.setApproveStatus(committeePoolUser.getApproveStatus());
    committeePool.setApprovedBy(committeePoolUser.getApprovedBy());
    committeePool.setApprovedDate(committeePoolUser.getApprovedDate());

    committeePoolHistoryRepository.save(committeePool);
    log.info("Saved audit record for User ID: {}", committeePool.getUserId());
  }

  private CommitteePoolResp prepareCommitteePoolResponse() {
    CommitteePoolResp committeePoolResp = new CommitteePoolResp();

    List<CommitteePoolDTO> tempList = committeePoolJdbc.findAllCommitteePoolTempList();
    List<CommitteePoolDTO> masterList = committeePoolJdbc.findAllCommitteePoolList();
    committeePoolResp.setCommitteePoolTempList(tempList);
    committeePoolResp.setCommitteePoolList(masterList);

    return committeePoolResp;
  }

  private void updateCommitteePool(
      CommitteePool committeePool,
      CommitteePoolTemp committeePoolTemp,
      CommitteePoolDTO committeePoolUser) {
    Date date = new Date();

    committeePool.setUserStatus(committeePoolUser.getStatus());
    committeePool.setApproveStatus(committeePoolUser.getApproveStatus());
    committeePool.setApprovedDate(date);
    committeePool.setApprovedBy(UserContext.getUsername());
    committeePool.setModifiedBy(committeePoolTemp.getModifiedBy());
    committeePool.setLastModifiedDate(committeePoolTemp.getLastModifiedDate());
    committeePool = committeePoolRepository.saveAndFlush(committeePool);

    // save to history
    this.saveCommitteePoolHistory(new CommitteePoolDTO(committeePool));

    if (committeePool.getGroupCode().equals("80")) {
      if (committeePool.getUserStatus().equals(AppsConstants.Status.ACT)) {
        changeCommitteeCurrentPath(committeePool, AppsConstants.CAPathType.REG);
      } else {
        changeCommitteeCurrentPath(committeePool, AppsConstants.CAPathType.ALT);
      }
    }
  }

  private void updateHistoryWithPoolTemp(
      CommitteePoolTemp committeePoolTemp, MasterDataApproveStatus status) {
    Date date = new Date();

    committeePoolTemp.setApproveStatus(status);
    committeePoolTemp.setApprovedDate(date);
    committeePoolTemp.setApprovedBy(UserContext.getUsername());

    // save to history
    this.saveCommitteePoolHistory(new CommitteePoolDTO(committeePoolTemp));
  }

  private void processUserRemoval(CommitteePoolTemp committeePoolTemp) throws ApiRequestException {
    List<Committee> committees =
        committeePoolJdbc.getCommitteesByUserName(committeePoolTemp.getUserName());
    if (committees.isEmpty()) {
      committeePoolRepository.deleteById(committeePoolTemp.getUserId());
      updateHistoryWithPoolTemp(committeePoolTemp, MasterDataApproveStatus.APPROVED);
    } else {
      List<String> nameList = new ArrayList<>();
      for (Committee committee : committees) {
        nameList.add(committee.getCommitteeName());
      }
      String committeeNames = String.join(", ", nameList);

      log.error("ERROR : Pool user exist in committee: {}", committeePoolTemp.getUserName());
      throw new ApiRequestException(
          "This pool user is a member of the committee. These are the committee(s): "
              + committeeNames);
    }
  }

  private void changeCommitteeCurrentPath(
      CommitteePool committeePool, AppsConstants.CAPathType path) {
    List<Committee> existingCommittees =
        committeePoolJdbc.getCommitteesByUserName(committeePool.getUserName());
    if (!existingCommittees.isEmpty()) {
      for (Committee existingCommittee : existingCommittees) {
        Committee committee =
            committeeRepository.getReferenceById(existingCommittee.getCommitteeId());
        committee.setCurrentPath(path);
        committee.setModifiedBy(UserContext.getUsername());
        committee.setLastModifiedDate(new Date());

        committeeRepository.saveAndFlush(committee);

        if (path.equals(AppsConstants.CAPathType.ALT)) {
          int returnCode =
              committeePoolJdbc.changeCommitteePaperCurrentLevel(
                  existingCommittee.getCommitteeId(), path);

          log.info(
              "Response : CommitteePoolServiceImpl | changeCommitteeCurrentPath {}", returnCode);
        }

        // save to audit
        this.committeeService.saveMasterCommitteeHistory(committee);
      }
    }
  }
}
