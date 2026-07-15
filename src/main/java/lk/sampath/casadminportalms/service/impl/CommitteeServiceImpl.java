package lk.sampath.casadminportalms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committee.CommitteeDTO;
import lk.sampath.casadminportalms.dto.committee.CommitteeLevelDTO;
import lk.sampath.casadminportalms.dto.committee.LevelUserDTO;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.committee.*;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.committee.*;
import lk.sampath.casadminportalms.repository.committeepool.CommitteePoolRepository;
import lk.sampath.casadminportalms.repository.committeetype.CommitteeTypeRepository;
import lk.sampath.casadminportalms.service.CommitteeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class CommitteeServiceImpl implements CommitteeService {

  private final CommitteeTempRepository committeeTempRepository;

  private final CommitteeRepository committeeRepository;

  private final CommitteeTypeRepository committeeTypeRepository;

  private final CommitteeServiceImpl committeeService;

  private final LevelTempRepository levelTempRepository;

  private final LevelUserTempRepository levelUserTempRepository;

  private final CommitteePoolRepository committeePoolRepository;

  private final LevelRepository levelRepository;

  private final LevelUserRepository levelUserRepository;

  private final CommitteeAudRepository committeeAudRepository;

  private final CommitteeLevelAudRepository committeeLevelAudRepository;

  private final LevelUserAudRepository levelUserAudRepository;

  private final CommitteeJdbc committeeJdbc;

  public CommitteeServiceImpl(
      CommitteeTempRepository committeeTempRepository,
      CommitteeRepository committeeRepository,
      CommitteeTypeRepository committeeTypeRepository,
      LevelTempRepository levelTempRepository,
      LevelUserTempRepository levelUserTempRepository,
      CommitteePoolRepository committeePoolRepository,
      LevelRepository levelRepository,
      LevelUserRepository levelUserRepository,
      @Lazy CommitteeServiceImpl committeeService,
      CommitteeAudRepository committeeAudRepository,
      CommitteeLevelAudRepository committeeLevelAudRepository,
      LevelUserAudRepository levelUserAudRepository,
      CommitteeJdbc committeeJdbc) {
    this.committeeTempRepository = committeeTempRepository;
    this.committeeRepository = committeeRepository;
    this.committeeTypeRepository = committeeTypeRepository;
    this.committeeService = committeeService;
    this.levelTempRepository = levelTempRepository;
    this.levelUserTempRepository = levelUserTempRepository;
    this.committeePoolRepository = committeePoolRepository;
    this.levelRepository = levelRepository;
    this.levelUserRepository = levelUserRepository;
    this.committeeAudRepository = committeeAudRepository;
    this.committeeLevelAudRepository = committeeLevelAudRepository;
    this.levelUserAudRepository = levelUserAudRepository;
    this.committeeJdbc = committeeJdbc;
  }

  @Override
  public ResponseEntity<StandardResponse<List<CommitteeDTO>>> getTempCommittees()
      throws ApiRequestException {
    StandardResponse<List<CommitteeDTO>> response;
    try {
      List<CommitteeDTO> committees = committeeJdbc.getAllCommittees(true);
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), committees);

    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | getTempCommittees : ", e);
      throw new ApiRequestException("An error occurred while fetching pending data.");
    }
    return ResponseEntity.ok().body(response);
  }

  @Override
  public ResponseEntity<StandardResponse<CommitteeDTO>> getTempCommitteeById(Integer committeeId)
      throws ApiRequestException {

    StandardResponse<CommitteeDTO> response;

    try {
      if (committeeId != null) {
        CommitteeDTO committeeTemp = committeeJdbc.getCommittee(committeeId, true);

        if (committeeTemp.getCommitteeId() != 0) {
          response =
              new StandardResponse<>(
                  ErrorEnums.SUCCESS_CODE.getStatus(),
                  ErrorEnums.SUCCESS_CODE.getLabel(),
                  committeeTemp);
        } else {
          throw new ApiRequestException("Committee not found.");
        }
      } else {
        throw new ApiRequestException("Committee Id is empty.");
      }
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | getTempCommitteeById : ", e);
      String message =
          !e.getMessage().isEmpty()
              ? e.getMessage()
              : "An error occurred while fetching pending committee.";
      throw new ApiRequestException(message);
    }
    return ResponseEntity.ok().body(response);
  }

  @Override
  public ResponseEntity<StandardResponse<List<CommitteeDTO>>> getCommittees()
      throws ApiRequestException {
    StandardResponse<List<CommitteeDTO>> response;
    try {
      List<CommitteeDTO> committees = committeeJdbc.getAllCommittees(false);
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), committees);
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | getCommittees : ", e);
      throw new ApiRequestException("An error occurred while fetching approved data.");
    }
    return ResponseEntity.ok().body(response);
  }

  @Override
  public ResponseEntity<StandardResponse<CommitteeDTO>> getCommitteeById(Integer committeeId)
      throws ApiRequestException {
    StandardResponse<CommitteeDTO> response;

    try {
      if (committeeId != null) {
        CommitteeDTO committee = committeeJdbc.getCommittee(committeeId, false);

        if (committee.getCommitteeId() != 0) {
          response =
              new StandardResponse<>(
                  ErrorEnums.SUCCESS_CODE.getStatus(),
                  ErrorEnums.SUCCESS_CODE.getLabel(),
                  committee);
        } else {
          throw new ApiRequestException("Committee not found.");
        }
      } else {
        throw new ApiRequestException("Committee Id is empty.");
      }
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | getCommitteeById : ", e);
      String message =
          !e.getMessage().isEmpty()
              ? e.getMessage()
              : "An error occurred while fetching approved committee.";
      throw new ApiRequestException(message);
    }
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<CommitteeDTO>> saveTempCommittee(CommitteeDTO committeeDTO)
      throws ApiRequestException {

    StandardResponse<CommitteeDTO> response;
    try {
      CommitteeDTO committeeExit =
          committeeJdbc.findCommitteeByCommitteeName(committeeDTO.getCommitteeName());

      CommitteeTemp committeeTemp =
          committeeTempRepository
              .findById(committeeDTO.getCommitteeId())
              .orElse(new CommitteeTemp());

      if (committeeDTO.getCommitteeId() == 0 && committeeExit != null) {
        throw new ApiRequestException("The committee name is already exist.");
      } else {
        if (committeeDTO.getCommitteeTypeId() != 0) {
          CommitteeType committeeType =
              committeeTypeRepository
                  .findById(committeeDTO.getCommitteeTypeId())
                  .orElseThrow(() -> new ApiRequestException("The committee type is not found."));

          committeeTemp.setCommitteeType(committeeType);
        }

        committeeTemp.setCommitteeId(
            committeeDTO.getCommitteeId() == 0
                ? committeeTempRepository.getCurrentSequenceValue()
                : committeeDTO.getCommitteeId());
        committeeTemp.setCommitteeName(committeeDTO.getCommitteeName());
        committeeTemp.setDelegatedAuthority(committeeDTO.getDelegatedAuthority());
        committeeTemp.setStatus(committeeDTO.getStatus());
        committeeTemp.setReviewer(committeeDTO.getReviewer());
        committeeTemp.setCurrentPath(committeeDTO.getCurrentPath());
        committeeTemp.setRecordStatus(committeeDTO.getRecordStatus());
        committeeTemp.setApproveStatus(committeeDTO.getApproveStatus());
        committeeTemp.setCreatedBy(committeeDTO.getCreatedBy());
        committeeTemp.setCreatedDate(committeeDTO.getCreatedDate());

        if (committeeDTO.getCommitteeId() != null && committeeDTO.getCommitteeId() != 0) {
          committeeTemp.setModifiedBy(UserContext.getUsername());
          committeeTemp.setLastModifiedDate(new Date());
        }

        List<CommitteeLevelTemp> committeeLevels =
            this.mapTempCommitteeLevels(committeeDTO, committeeTemp);
        committeeTemp.setCommitteeLevels(committeeLevels);
        committeeTemp = committeeTempRepository.saveAndFlush(committeeTemp);
      }
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
              new CommitteeDTO(committeeTemp));

    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | saveTempCommittee: ", e);
      String message =
          !e.getMessage().isEmpty() ? e.getMessage() : "Committee saving has been failed.";
      throw new ApiRequestException(message);
    }
    return ResponseEntity.ok().body(response);
  }

  private List<CommitteeLevelTemp> mapTempCommitteeLevels(
      CommitteeDTO committeeDTO, CommitteeTemp committeeTemp) throws ApiRequestException {

    List<CommitteeLevelTemp> committeeLevels = new ArrayList<>();

    try {
      Date date = new Date();

      for (CommitteeLevelDTO committeeLevel : committeeDTO.getLevels()) {
        if (committeeLevel.getRecordStatus().equals(AppsConstants.RecordStatus.DELETE)) {
          levelTempRepository.deleteById(committeeLevel.getLevelId());
          continue;
        }

        CommitteeLevelTemp committeeLevelTemp =
            levelTempRepository
                .findById(committeeLevel.getLevelId())
                .orElse(new CommitteeLevelTemp());

        committeeLevelTemp.setLevelId(
            committeeLevel.getLevelId() == 0
                ? levelTempRepository.getCurrentSequenceValue()
                : committeeLevel.getLevelId());
        committeeLevelTemp.setCommitteeTemp(committeeTemp);
        committeeLevelTemp.setCombination(committeeLevel.getCombination());
        committeeLevelTemp.setLevel(committeeLevel.getLevel());
        committeeLevelTemp.setStatus(committeeLevel.getStatus());
        committeeLevelTemp.setPathType(committeeLevel.getPathType());
        committeeLevelTemp.setUserCount(committeeLevel.getUserCount());
        committeeLevelTemp.setCreatedBy(committeeLevel.getCreatedBy());
        committeeLevelTemp.setCreatedDate(committeeLevel.getCreatedDate());

        if (committeeLevel.getRecordStatus().equals(AppsConstants.RecordStatus.UPDATE)) {
          committeeLevelTemp.setModifiedBy(UserContext.getUsername());
          committeeLevelTemp.setLastModifiedDate(date);
        }

        List<LevelUserTemp> levelUsers = this.mapTempLevelUsers(committeeLevel, committeeLevelTemp);
        committeeLevelTemp.setLevelUsers(levelUsers);

        committeeLevels.add(committeeLevelTemp);
      }
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | mapTempCommitteeLevels : ", e);
      throw new ApiRequestException("Committee level save has been failed.");
    }

    return committeeLevels;
  }

  private List<LevelUserTemp> mapTempLevelUsers(
      CommitteeLevelDTO committeeLevelDTO, CommitteeLevelTemp committeeLevelTemp)
      throws ApiRequestException {

    List<LevelUserTemp> levelUsers = new ArrayList<>();
    try {
      for (LevelUserDTO levelUser : committeeLevelDTO.getLevelUsers()) {
        if (AppsConstants.RecordStatus.DELETE.equals(levelUser.getRecordStatus())) {
          levelUserTempRepository.deleteById(levelUser.getLevelUserId());
          continue;
        }

        CommitteePool committeePool = committeePoolRepository.findByUserId(levelUser.getUserId());
        if (committeePool == null) {
          throw new ApiRequestException("Pool user not found.");
        }

        LevelUserTemp levelUserTemp =
            levelUserTempRepository
                .findById(levelUser.getLevelUserId())
                .orElse(new LevelUserTemp());

        levelUserTemp.setLevelUserId(
            levelUser.getLevelUserId() == 0
                ? levelUserTempRepository.getCurrentSequenceValue()
                : levelUser.getLevelUserId());
        levelUserTemp.setCommitteeLevelTemp(committeeLevelTemp);
        levelUserTemp.setCommitteeTemp(committeeLevelTemp.getCommitteeTemp());
        levelUserTemp.setCommitteePool(committeePool);
        levelUserTemp.setCreatedBy(levelUser.getCreatedBy());
        levelUserTemp.setCreatedDate(levelUser.getCreatedDate());

        if (levelUser.getRecordStatus().equals(AppsConstants.RecordStatus.UPDATE)) {
          levelUserTemp.setModifiedBy(UserContext.getUsername());
          levelUserTemp.setLastModifiedDate(new Date());
        }
        levelUsers.add(levelUserTemp);
      }
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | mapTempLevelUsers : ", e);
      throw new ApiRequestException(e.getMessage() != null ? e.getMessage() : "");
    }

    return levelUsers;
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<CommitteeDTO>> approveRejectCommittee(
      CommitteeDTO committeeDTO) throws ApiRequestException {
    StandardResponse<CommitteeDTO> response;
    Committee committee = new Committee();
    try {

      if (committeeDTO.getApproveStatus().equals(MasterDataApproveStatus.APPROVED)) {
        if (committeeDTO.getStatus().equals(AppsConstants.Status.ACT)) {
          committee = this.committeeService.saveCommittee(committeeDTO);
        } else {
          handleApprovedINACommittee(committeeDTO.getCommitteeId());
        }
      } else if (committeeDTO.getApproveStatus().equals(MasterDataApproveStatus.REJECTED)) {
        handleRejectedCommittee(committeeDTO.getCommitteeId());
      } else {
        throw new ApiRequestException(
            "Unknown approval status: " + committeeDTO.getApproveStatus());
      }
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
              new CommitteeDTO(committee));
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | approveRejectCommittee : ", e);
      String message =
          !e.getMessage().isEmpty() ? e.getMessage() : "Committee authorization has been failed.";
      throw new ApiRequestException(message);
    }

    return ResponseEntity.ok().body(response);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public Committee saveCommittee(CommitteeDTO committeeDTO) throws ApiRequestException {
    Committee committee;
    Date date = new Date();
    try {
      CommitteeTemp committeeTemp =
          committeeTempRepository
              .findById(committeeDTO.getCommitteeId())
              .orElseThrow(() -> new ApiRequestException("The pending committee is not found."));

      // for audit
      committeeTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);
      committeeTemp.setApprovedBy(UserContext.getUsername());
      committeeTemp.setApprovedDate(date);

      committee =
          committeeRepository.findById(committeeDTO.getCommitteeId()).orElse(new Committee());
      committee.setCommitteeId(committeeTemp.getCommitteeId());
      committee.setCommitteeName(committeeTemp.getCommitteeName());
      committee.setCommitteeType(committeeTemp.getCommitteeType());
      committee.setDelegatedAuthority(committeeTemp.getDelegatedAuthority());
      committee.setReviewer(committeeTemp.getReviewer());
      committee.setCurrentPath(committeeTemp.getCurrentPath());
      committee.setStatus(committeeTemp.getStatus());
      committee.setApproveStatus(MasterDataApproveStatus.APPROVED);
      committee.setApprovedBy(UserContext.getUsername());
      committee.setApprovedDate(date);
      committee.setLastModifiedDate(committeeTemp.getLastModifiedDate());
      committee.setModifiedBy(committeeTemp.getModifiedBy());
      committee.setCreatedDate(committeeTemp.getCreatedDate());
      committee.setCreatedBy(committeeTemp.getCreatedBy());

      List<CommitteeLevel> committeeLevels = this.mapCommitteeLevels(committeeTemp, committee);
      committee.setCommitteeLevels(committeeLevels);

      committee = committeeRepository.saveAndFlush(committee);

      // delete temp committee
      this.committeeService.deleteCommitteeTemp(committeeDTO.getCommitteeId());

      // save to audit
      saveCommitteeHistory(committeeTemp, false);

    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | approveRejectCommittee | saveCommittee : ", e);
      String message =
          !e.getMessage().isEmpty() ? e.getMessage() : "Committee save has been failed.";
      throw new ApiRequestException(message);
    }

    return committee;
  }

  private List<CommitteeLevel> mapCommitteeLevels(CommitteeTemp committeeTemp, Committee committee)
      throws ApiRequestException {

    List<CommitteeLevel> committeeLevels = new ArrayList<>();

    try {

      int removeStatus = this.committeeJdbc.removeCommitteeLevel(committee.getCommitteeId());
      if (removeStatus == 0) {
        throw new ApiRequestException("Committee level save has been failed.");
      }
      for (CommitteeLevelTemp committeeLevelTemp : committeeTemp.getCommitteeLevels()) {

        CommitteeLevel committeeLevel = new CommitteeLevel();

        committeeLevel.setLevelId(committeeLevelTemp.getLevelId());
        committeeLevel.setCommittee(committee);
        committeeLevel.setCombination(committeeLevelTemp.getCombination());
        committeeLevel.setLevel(committeeLevelTemp.getLevel());
        committeeLevel.setStatus(committeeLevelTemp.getStatus());
        committeeLevel.setPathType(committeeLevelTemp.getPathType());
        committeeLevel.setUserCount(committeeLevelTemp.getUserCount());
        committeeLevel.setCreatedBy(committeeLevelTemp.getCreatedBy());
        committeeLevel.setCreatedDate(committeeLevelTemp.getCreatedDate());
        committeeLevel.setModifiedBy(committeeLevelTemp.getCreatedBy());
        committeeLevel.setLastModifiedDate(committeeLevelTemp.getLastModifiedDate());

        List<LevelUser> levelUsers = this.mapLevelUsers(committeeLevelTemp, committeeLevel);
        committeeLevel.setLevelUsers(levelUsers);

        committeeLevels.add(committeeLevel);

        // save to audit
        saveCommitteeLevelHistory(committeeLevelTemp, false);
      }
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | approveRejectCommittee | mapCommitteeLevels : ", e);
      throw new ApiRequestException("Committee level save has been failed.");
    }

    return committeeLevels;
  }

  private List<LevelUser> mapLevelUsers(
      CommitteeLevelTemp committeeLevelTemp, CommitteeLevel committeeLevel)
      throws ApiRequestException {
    List<LevelUser> levelUsers = new ArrayList<>();

    try {
      for (LevelUserTemp levelUserTemp : committeeLevelTemp.getLevelUsers()) {

        CommitteePool committeePool =
            committeePoolRepository.findByUserId(levelUserTemp.getCommitteePool().getUserId());
        if (committeePool == null) {
          throw new ApiRequestException("Pool user deactivated or not in the pool.");
        }

        LevelUser levelUser = new LevelUser();

        levelUser.setLevelUserId(levelUserTemp.getLevelUserId());
        levelUser.setCommitteeLevel(committeeLevel);
        levelUser.setCommittee(committeeLevel.getCommittee());
        levelUser.setCommitteePool(levelUserTemp.getCommitteePool());
        levelUser.setPathType(committeeLevel.getPathType());
        levelUser.setUserStatus(levelUserTemp.getUserStatus());
        levelUser.setStatus(levelUserTemp.getStatus());
        levelUser.setCreatedBy(levelUserTemp.getCreatedBy());
        levelUser.setCreatedDate(levelUserTemp.getCreatedDate());
        levelUser.setModifiedBy(levelUserTemp.getModifiedBy());
        levelUser.setLastModifiedDate(levelUserTemp.getLastModifiedDate());

        levelUsers.add(levelUser);

        // save to audit
        saveLevelUserHistory(levelUserTemp);
      }
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | approveRejectCommittee | mapLevelUsers : ", e);
      String message = !e.getMessage().isEmpty() ? e.getMessage() : "";
      throw new ApiRequestException(message);
    }

    return levelUsers;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public StandardResponse<Boolean> deleteCommitteeTemp(Integer committeeId)
      throws ApiRequestException {
    StandardResponse<Boolean> response;
    boolean success = false;
    try {
      committeeTempRepository
          .findById(committeeId)
          .ifPresent(
              tempCommittee -> {
                levelUserTempRepository.deleteByCommitteeTemp(tempCommittee);
                levelTempRepository.deleteByCommitteeTemp(tempCommittee);
                committeeTempRepository.deleteById(tempCommittee.getCommitteeId());
              });
      success = true;
    } catch (Exception e) {
      log.error("ERROR : Remove Temp Committee: ", e);
      throw new ApiRequestException("Remove temp committee has been failed.");
    }
    response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), success);

    return response;
  }

  @Override
  public ResponseEntity<StandardResponse<List<CommitteeLevelDTO>>> getCommitteeLevelsByCommittee(
      Integer committeeId) throws ApiRequestException {
    StandardResponse<List<CommitteeLevelDTO>> response;

    try {
      if (committeeId != null) {
        CommitteeDTO committeeTemp = committeeJdbc.getCommittee(committeeId, true);

        if (committeeTemp.getCommitteeId() != 0) {
          List<CommitteeLevelDTO> committeeLevels = committeeTemp.getLevels();
          response =
              new StandardResponse<>(
                  ErrorEnums.SUCCESS_CODE.getStatus(),
                  ErrorEnums.SUCCESS_CODE.getLabel(),
                  committeeLevels);
        } else {
          throw new ApiRequestException("Committee not found.");
        }
      } else {
        throw new ApiRequestException("Committee Id is empty.");
      }
    } catch (Exception e) {
      log.error("ERROR : CommitteeServiceImpl | getCommitteeLevelsByCommittee : ", e);
      String message =
          !e.getMessage().isEmpty()
              ? e.getMessage()
              : "An error occurred while fetching pending committee data.";
      throw new ApiRequestException(message);
    }
    return ResponseEntity.ok().body(response);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public void deleteCommittee(Integer committeeId) throws ApiRequestException {

    try {
      committeeRepository
          .findById(committeeId)
          .ifPresent(
              committee -> {
                levelUserRepository.deleteByCommittee(committee);
                levelRepository.deleteByCommittee(committee);
                committeeRepository.deleteById(committee.getCommitteeId());
              });
    } catch (Exception e) {
      log.error("ERROR : Remove Committee: ", e);
      throw new ApiRequestException("Remove committee has been failed.");
    }
  }

  private void saveCommitteeHistory(CommitteeTemp committee, boolean withLevels) {
    CommitteeHistory committeeHistory = new CommitteeHistory();

    committeeHistory.setCommitteeId(committee.getCommitteeId());
    committeeHistory.setCommitteeName(committee.getCommitteeName());
    committeeHistory.setCommitteeType(committee.getCommitteeType());
    committeeHistory.setDelegatedAuthority(committee.getDelegatedAuthority());
    committeeHistory.setReviewer(committee.getReviewer());
    committeeHistory.setCurrentPath(committee.getCurrentPath());
    committeeHistory.setStatus(committee.getStatus());
    committeeHistory.setApproveStatus(committee.getApproveStatus());
    committeeHistory.setApprovedBy(committee.getApprovedBy());
    committeeHistory.setApprovedDate(committee.getApprovedDate());
    committeeHistory.setLastModifiedDate(committee.getLastModifiedDate());
    committeeHistory.setModifiedBy(committee.getModifiedBy());
    committeeHistory.setCreatedDate(committee.getCreatedDate());
    committeeHistory.setCreatedBy(committee.getCreatedBy());
    committeeHistory.setCommitteeStatus(committee.getRecordStatus());

    committeeAudRepository.save(committeeHistory);

    if (withLevels) {
      mapLevelHistory(committee.getCommitteeLevels());
    }
  }

  private void mapLevelHistory(List<CommitteeLevelTemp> committeeLevels) {

    for (CommitteeLevelTemp committeeLevel : committeeLevels) {
      saveCommitteeLevelHistory(committeeLevel, true);
    }
  }

  private void mapLevelUserHistory(List<LevelUserTemp> levelUsers) {
    for (LevelUserTemp levelUser : levelUsers) {
      saveLevelUserHistory(levelUser);
    }
  }

  private void saveCommitteeLevelHistory(CommitteeLevelTemp committeeLevel, boolean withUsers) {
    CommitteeLevelHistory committeeLevelHistory = new CommitteeLevelHistory();

    committeeLevelHistory.setLevelId(committeeLevel.getLevelId());
    committeeLevelHistory.setCommittee(committeeLevel.getCommitteeTemp().getCommitteeId());
    committeeLevelHistory.setCombination(committeeLevel.getCombination());
    committeeLevelHistory.setLevel(committeeLevel.getLevel());
    committeeLevelHistory.setStatus(committeeLevel.getStatus());
    committeeLevelHistory.setPathType(committeeLevel.getPathType());
    committeeLevelHistory.setUserCount(committeeLevel.getUserCount());
    committeeLevelHistory.setCreatedBy(committeeLevel.getCreatedBy());
    committeeLevelHistory.setCreatedDate(committeeLevel.getCreatedDate());
    committeeLevelHistory.setModifiedBy(committeeLevel.getCreatedBy());
    committeeLevelHistory.setLastModifiedDate(committeeLevel.getLastModifiedDate());

    committeeLevelAudRepository.save(committeeLevelHistory);
    if (withUsers) {
      mapLevelUserHistory(committeeLevel.getLevelUsers());
    }
  }

  private void saveLevelUserHistory(LevelUserTemp levelUser) {

    LevelUserHistory levelUserHistory = new LevelUserHistory();

    levelUserHistory.setLevelUserId(levelUser.getLevelUserId());
    levelUserHistory.setCommittee(levelUser.getCommitteeTemp().getCommitteeId());
    levelUserHistory.setCommitteeLevel(levelUser.getCommitteeLevelTemp().getLevelId());
    levelUserHistory.setStatus(levelUser.getStatus());
    levelUserHistory.setPathType(levelUser.getCommitteeLevelTemp().getPathType());
    levelUserHistory.setCommitteePool(levelUser.getCommitteePool());
    levelUserHistory.setUserStatus(levelUser.getUserStatus());
    levelUserHistory.setStatus(levelUser.getStatus());
    levelUserHistory.setCreatedBy(levelUser.getCreatedBy());
    levelUserHistory.setCreatedDate(levelUser.getCreatedDate());
    levelUserHistory.setModifiedBy(levelUser.getCreatedBy());
    levelUserHistory.setLastModifiedDate(levelUser.getLastModifiedDate());

    levelUserAudRepository.save(levelUserHistory);
  }

  private void handleApprovedINACommittee(Integer committeeId) throws ApiRequestException {

    CommitteeTemp committeeTemp =
        committeeTempRepository
            .findById(committeeId)
            .orElseThrow(() -> new ApiRequestException("Committee not found."));
    committeeTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);
    committeeTemp.setApprovedBy(UserContext.getUsername());
    committeeTemp.setApprovedDate(new Date());

    Committee committee =
        committeeRepository
            .findById(committeeId)
            .orElseThrow(() -> new ApiRequestException("Committee not found."));
    committee.setApproveStatus(MasterDataApproveStatus.APPROVED);
    committee.setApprovedBy(UserContext.getUsername());
    committee.setApprovedDate(new Date());
    committee.setModifiedBy(committeeTemp.getModifiedBy());
    committee.setLastModifiedDate(committeeTemp.getLastModifiedDate());

    committeeRepository.save(committee);

    // save to audit
    saveCommitteeHistory(committeeTemp, true);
  }

  private void handleRejectedCommittee(Integer committeeId) throws ApiRequestException {
    CommitteeTemp committeeTemp =
        committeeTempRepository
            .findById(committeeId)
            .orElseThrow(() -> new ApiRequestException(""));
    committeeTemp.setApproveStatus(MasterDataApproveStatus.REJECTED);
    committeeTemp.setApprovedBy(UserContext.getUsername());
    committeeTemp.setApprovedDate(new Date());
    committeeTemp.setRecordStatus(AppsConstants.RecordStatus.DRAFT);

    committeeTempRepository.save(committeeTemp);

    // save to audit
    saveCommitteeHistory(committeeTemp, true);
  }

  @Override
  public void saveMasterCommitteeHistory(Committee committee) {
    CommitteeHistory committeeHistory = new CommitteeHistory();

    committeeHistory.setCommitteeId(committee.getCommitteeId());
    committeeHistory.setCommitteeName(committee.getCommitteeName());
    committeeHistory.setCommitteeType(committee.getCommitteeType());
    committeeHistory.setDelegatedAuthority(committee.getDelegatedAuthority());
    committeeHistory.setReviewer(committee.getReviewer());
    committeeHistory.setCurrentPath(committee.getCurrentPath());
    committeeHistory.setStatus(committee.getStatus());
    committeeHistory.setApproveStatus(committee.getApproveStatus());
    committeeHistory.setApprovedBy(committee.getApprovedBy());
    committeeHistory.setApprovedDate(committee.getApprovedDate());
    committeeHistory.setLastModifiedDate(committee.getLastModifiedDate());
    committeeHistory.setModifiedBy(committee.getModifiedBy());
    committeeHistory.setCreatedDate(committee.getCreatedDate());
    committeeHistory.setCreatedBy(committee.getCreatedBy());

    committeeAudRepository.save(committeeHistory);
  }
}
