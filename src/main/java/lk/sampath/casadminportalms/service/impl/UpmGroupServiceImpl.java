package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.upmgroup.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupJdbc;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupRepository;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupTempAudRepository;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupTempRepository;
import lk.sampath.casadminportalms.service.UpmGroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class UpmGroupServiceImpl implements UpmGroupService {

  public static final String DOES_NOT_EXISTS = "Does not exists";

  public static final String NOT_NULL = "Upm group code name cannot be empty or null.";

  private final UpmGroupRepository upmGroupRepository;

  private final UpmGroupTempRepository upmGroupTempRepository;

  private final UpmGroupTempAudRepository upmGroupTempAudRepository;

  private final UpmGroupJdbc upmGroupJdbc;

  public UpmGroupServiceImpl(
      UpmGroupRepository upmGroupRepository,
      UpmGroupTempRepository upmGroupTempRepository,
      UpmGroupTempAudRepository upmGroupTempAudRepository,
      UpmGroupJdbc upmGroupJdbc) {
    this.upmGroupRepository = upmGroupRepository;
    this.upmGroupTempRepository = upmGroupTempRepository;
    this.upmGroupTempAudRepository = upmGroupTempAudRepository;
    this.upmGroupJdbc = upmGroupJdbc;
  }

  @Transactional(readOnly = true)
  @Override
  public ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupTempByID(Integer upmGroupID)
      throws ApiRequestException {
    UpmGroupDTO upmGroupTemp = upmGroupJdbc.findUpmGroupTempById(upmGroupID);
    if (upmGroupTemp == null) {
      throw new ApiRequestException("UPM Group temp with " + upmGroupID + DOES_NOT_EXISTS);
    }

    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
    return ResponseEntity.ok().body(response);
  }

  @Transactional(readOnly = true)
  @Override
  public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> findAllUpmGroupTempList()
      throws ApiRequestException {
    List<UpmGroupDTO> upmGroupTempList = upmGroupJdbc.findAllUpmGroupTempList();
    StandardResponse<List<UpmGroupDTO>> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            upmGroupTempList);
    return ResponseEntity.ok().body(response);
  }

  @Transactional(readOnly = true)
  @Override
  public ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupById(int upmGroupID) {
    UpmGroupDTO upmGroup = upmGroupJdbc.findUpmGroupById(upmGroupID);
    if (upmGroup == null) {
      throw new ApiRequestException("UPM Group with " + upmGroupID + DOES_NOT_EXISTS);
    }

    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroup);
    return ResponseEntity.ok().body(response);
  }

  @Transactional(readOnly = true)
  @Override
  public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> searchUpmGroups() {
    List<UpmGroupDTO> upmGroupList = upmGroupJdbc.findAllUpmGroupList();
    StandardResponse<List<UpmGroupDTO>> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupList);
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<UpmGroupDTO>> saveUPMGroupTemp(UpmGroupDTO upmGroupDTO)
      throws ApiRequestException {

    log.info("START: Save UpmGroup :{}", upmGroupDTO);
    if (upmGroupDTO == null
        || upmGroupDTO.getGroupCode() == null
        || upmGroupDTO.getGroupCode().trim().isEmpty()) {
      throw new ApiRequestException(NOT_NULL);
    }

    UpmGroupTemp upmGroupTemp = new UpmGroupTemp();

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QUpmGroupTemp.upmGroupTemp.groupCode.eq(upmGroupDTO.getGroupCode()));
    List<UpmGroupTemp> upmGroupTemps =
        (List<UpmGroupTemp>) upmGroupTempRepository.findAll(booleanBuilder);

    validateTemplateNameUniqueness(upmGroupDTO.getGroupCode(), null);

    if (upmGroupTemps.isEmpty()) {
      upmGroupTemp.setUpmGroupID(upmGroupTempRepository.getCurrentSequenceValue());
      upmGroupTemp.setStatus(upmGroupDTO.getStatus());
      upmGroupTemp.setGroupCode(upmGroupDTO.getGroupCode());
      upmGroupTemp.setReferenceName(upmGroupDTO.getReferenceName());
      upmGroupTemp.setWorkFlowLevel(upmGroupDTO.getWorkFlowLevel());
      upmGroupTemp.setApproveStatus(upmGroupDTO.getApproveStatus());

      upmGroupTemp = upmGroupTempRepository.save(upmGroupTemp);
      log.info("SUCCESS: Saved UpmGroup with ID :{}", upmGroupTemp.getUpmGroupID());

    } else {
      throw new ApiRequestException("UPM Group Already Exists");
    }

    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<UpmGroupDTO>> updateUpmGroupTemp(
      Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException {

    UpmGroupTemp upmGroupTemp =
        upmGroupTempRepository
            .findById(upmGroupID)
            .orElseThrow(
                () ->
                    new ApiRequestException(
                        "UPM Group Temp with ID " + upmGroupID + DOES_NOT_EXISTS));

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QUpmGroupTemp.upmGroupTemp.groupCode.eq(upmGroupDTO.getGroupCode()));
    List<UpmGroupTemp> upmGroupTempList =
        (List<UpmGroupTemp>) upmGroupTempRepository.findAll(booleanBuilder);

    if (!upmGroupTemp.getGroupCode().equals(upmGroupDTO.getGroupCode())) {
      validateTemplateNameUniqueness(upmGroupDTO.getGroupCode(), upmGroupID);
    } else if (upmGroupDTO.getGroupCode().trim().isEmpty()) {
      throw new ApiRequestException(NOT_NULL);
    }

    boolean existingTempList =
        upmGroupTempList.stream()
            .anyMatch(temp -> temp.getGroupCode().equals(upmGroupDTO.getGroupCode()));

    if (existingTempList && !upmGroupTemp.getGroupCode().equals(upmGroupDTO.getGroupCode())) {
      throw new ApiRequestException(
          "UPM group temp with " + upmGroupDTO.getGroupCode() + "Already Exists");
    }

    upmGroupTemp.setStatus(upmGroupDTO.getStatus());
    upmGroupTemp.setGroupCode(upmGroupDTO.getGroupCode());
    upmGroupTemp.setReferenceName(upmGroupDTO.getReferenceName());
    upmGroupTemp.setWorkFlowLevel(upmGroupDTO.getWorkFlowLevel());
    upmGroupTemp.setApproveStatus(upmGroupDTO.getApproveStatus());

    upmGroupTempRepository.save(upmGroupTemp);
    log.info("END: Update UpmGroup :{}", upmGroupTemp);

    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<UpmGroupDTO>> approveRejectUpmGroup(
      ApproveRejectRQ approveRejectRQ) throws ApiRequestException {

    if (approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null) {
      throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
    }

    UpmGroupTemp upmGroupTemp =
        upmGroupTempRepository
            .findById(approveRejectRQ.getApproveRejectDataID())
            .orElseThrow(
                () ->
                    new ApiRequestException(
                        "UPM group temp with"
                            + approveRejectRQ.getApproveRejectDataID()
                            + DOES_NOT_EXISTS));
    log.info("upmGroupTemp :{}", upmGroupTemp);

    Optional<UpmGroup> optionalUpmGroup = upmGroupRepository.findById(upmGroupTemp.getUpmGroupID());
    UpmGroup findUpmGroup = optionalUpmGroup.orElse(null);

    ResponseEntity<StandardResponse<UpmGroupDTO>> response;

    if (MasterDataApproveStatus.APPROVED.equals(approveRejectRQ.getApproveStatus())) {
      response = handleApproval(upmGroupTemp, findUpmGroup);
    } else if (MasterDataApproveStatus.REJECTED.equals(approveRejectRQ.getApproveStatus())) {

      upmGroupTemp.setApproveStatus(approveRejectRQ.getApproveStatus());
      upmGroupTemp.setApprovedBy(UserContext.getUsername());
      upmGroupTemp.setApprovedDate(new Date());

      upmGroupTempRepository.save(upmGroupTemp);
      response = handleRejection(upmGroupTemp);
    } else {
      throw new ApiRequestException(
          "Unknown approval status: " + approveRejectRQ.getApproveStatus());
    }

    return response;
  }

  private ResponseEntity<StandardResponse<UpmGroupDTO>> handleRejection(UpmGroupTemp upmGroupTemp) {

    log.info("Handling rejection for UPM Group Temp ID: {}", upmGroupTemp.getUpmGroupID());
    Date rejectedDate = new Date();
    saveUpmGroupAudit(upmGroupTemp, rejectedDate, MasterDataApproveStatus.REJECTED);
    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
    return ResponseEntity.ok().body(response);
  }

  private ResponseEntity<StandardResponse<UpmGroupDTO>> handleApproval(
      UpmGroupTemp upmGroupTemp, UpmGroup existingUpmGroup) {

    UpmGroup savedUpmGroup;
    Date approvedDate = new Date();

    if (existingUpmGroup != null
        && existingUpmGroup.getUpmGroupID().equals(upmGroupTemp.getUpmGroupID())) {
      savedUpmGroup = updateUpmGroupToMaster(upmGroupTemp, existingUpmGroup, approvedDate);
    } else {
      savedUpmGroup = saveUpmGroupToMaster(upmGroupTemp, approvedDate);
    }

    saveUpmGroupAudit(upmGroupTemp, approvedDate, MasterDataApproveStatus.APPROVED);
    upmGroupTempRepository.delete(upmGroupTemp);

    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedUpmGroup);
    return ResponseEntity.ok().body(response);
  }

  private UpmGroup saveUpmGroupToMaster(UpmGroupTemp upmGroupTemp, Date approvedDate) {

    UpmGroup upmGroup = new UpmGroup();

    upmGroup.setUpmGroupID(upmGroupTemp.getUpmGroupID());
    upmGroup.setStatus(upmGroupTemp.getStatus());
    upmGroup.setCreatedDate(upmGroupTemp.getCreatedDate());
    upmGroup.setGroupCode(upmGroupTemp.getGroupCode());
    upmGroup.setWorkFlowLevel(upmGroupTemp.getWorkFlowLevel());
    upmGroup.setCreatedBy(upmGroupTemp.getCreatedBy());
    upmGroup.setCreatedDate(upmGroupTemp.getCreatedDate());
    upmGroup.setModifiedBy(upmGroupTemp.getModifiedBy());
    upmGroup.setLastModifiedDate(upmGroupTemp.getLastModifiedDate());
    upmGroup.setApprovedDate(approvedDate);
    upmGroup.setApprovedBy(UserContext.getUsername());
    upmGroup.setApproveStatus(MasterDataApproveStatus.APPROVED);

    return upmGroupRepository.save(upmGroup);
  }

  private UpmGroup updateUpmGroupToMaster(
      UpmGroupTemp upmGroupTemp, UpmGroup existingUpmGroup, Date approvedDate) {

    UpmGroup upmGroup = (existingUpmGroup != null) ? existingUpmGroup : new UpmGroup();
    upmGroup.setUpmGroupID(upmGroupTemp.getUpmGroupID());
    upmGroup.setStatus(upmGroupTemp.getStatus());
    upmGroup.setCreatedDate(upmGroupTemp.getCreatedDate());
    upmGroup.setGroupCode(upmGroupTemp.getGroupCode());
    upmGroup.setWorkFlowLevel(upmGroupTemp.getWorkFlowLevel());
    upmGroup.setCreatedBy(upmGroupTemp.getCreatedBy());
    upmGroup.setReferenceName(upmGroupTemp.getReferenceName());
    upmGroup.setModifiedBy(upmGroupTemp.getModifiedBy());
    upmGroup.setLastModifiedDate(upmGroupTemp.getLastModifiedDate());
    upmGroup.setApprovedDate(approvedDate);
    upmGroup.setApprovedBy(UserContext.getUsername());
    upmGroup.setApproveStatus(MasterDataApproveStatus.APPROVED);

    return upmGroupRepository.save(upmGroup);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<UpmGroupDTO>> updateApprovedUpmGroup(
      Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException {

    log.info("START: Update Approved UPM Group :{}", upmGroupDTO);

    UpmGroupDTO upmGroupDb = upmGroupJdbc.findUpmGroupById(upmGroupID);
    if (upmGroupDb == null) {
      throw new ApiRequestException(" UPM group with " + upmGroupID + DOES_NOT_EXISTS);
    }

    log.info("START : GET upm group. {}", upmGroupDb);

    if (!upmGroupDb.getGroupCode().equals(upmGroupDTO.getGroupCode())) {
      validateTemplateNameUniqueness(upmGroupDTO.getGroupCode(), upmGroupID);
    } else if (upmGroupDTO.getGroupCode().trim().isEmpty()) {
      throw new ApiRequestException(NOT_NULL);
    }

    UpmGroupTemp upmGroupTemp = new UpmGroupTemp();
    upmGroupTemp.setUpmGroupID(upmGroupDb.getUpmGroupID());
    upmGroupTemp.setStatus(upmGroupDTO.getStatus());
    upmGroupTemp.setGroupCode(upmGroupDTO.getGroupCode());
    upmGroupTemp.setReferenceName(upmGroupDTO.getReferenceName());
    upmGroupTemp.setWorkFlowLevel(upmGroupDTO.getWorkFlowLevel());
    upmGroupTemp.setApproveStatus(upmGroupDTO.getApproveStatus());
    upmGroupTemp.setCreatedBy(upmGroupDb.getCreatedBy());
    upmGroupTemp.setCreatedDate(upmGroupDb.getCreatedDate());
    upmGroupTemp.setModifiedBy(UserContext.getUsername());
    upmGroupTemp.setLastModifiedDate(new Date());

    log.info("END : GET upm group {}", upmGroupTemp);
    upmGroupTempRepository.save(upmGroupTemp);

    StandardResponse<UpmGroupDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
    return ResponseEntity.ok().body(response);
  }

  private void validateTemplateNameUniqueness(String groupCode, Integer upmGroupID)
      throws ApiRequestException {

    BooleanBuilder tempBuilder = new BooleanBuilder();
    tempBuilder.and(QUpmGroupTemp.upmGroupTemp.groupCode.eq(groupCode));
    if (upmGroupID != null) {
      tempBuilder.and(QUpmGroupTemp.upmGroupTemp.upmGroupID.ne(upmGroupID));
    }
    boolean existsInTemp = upmGroupTempRepository.exists(tempBuilder);

    BooleanBuilder masterBuilder = new BooleanBuilder();
    masterBuilder.and(QUpmGroup.upmGroup.groupCode.eq(groupCode));
    if (upmGroupID != null) {
      masterBuilder.and(QUpmGroup.upmGroup.upmGroupID.ne(upmGroupID));
    }
    boolean exitsInMaster = upmGroupRepository.exists(masterBuilder);

    if (existsInTemp || exitsInMaster) {
      throw new ApiRequestException("Group Code '" + groupCode + "' already exists in the system.");
    }
  }

  private void saveUpmGroupAudit(
      UpmGroupTemp upmGroupTemp, Date actionDate, MasterDataApproveStatus approveStatus) {

    UpmGroupTempAud audit = new UpmGroupTempAud();
    audit.setUpmGroupID(upmGroupTemp.getUpmGroupID());
    audit.setReferenceName(upmGroupTemp.getReferenceName());
    audit.setGroupCode(upmGroupTemp.getGroupCode());
    audit.setWorkFlowLevel(upmGroupTemp.getWorkFlowLevel());
    audit.setStatus(upmGroupTemp.getStatus());
    audit.setCreatedDate(upmGroupTemp.getCreatedDate());
    audit.setCreatedBy(upmGroupTemp.getCreatedBy());
    audit.setModifiedBy(upmGroupTemp.getModifiedBy());
    audit.setLastModifiedDate(upmGroupTemp.getLastModifiedDate());
    audit.setApprovedDate(actionDate);
    audit.setApprovedBy(UserContext.getUsername());
    audit.setApproveStatus(approveStatus);

    upmGroupTempAudRepository.save(audit);
    log.info("Saved audit record for UPM Group ID: {}", upmGroupTemp.getUpmGroupID());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<Void>> deleteUpmGroup(Integer upmGroupID)
      throws ApiRequestException {
    upmGroupTempRepository.deleteById(upmGroupID);
    StandardResponse<Void> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupID);
    return ResponseEntity.ok().body(response);
  }

  @Override
  public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> findAllActiveUpmGroups()
      throws ApiRequestException {
    List<UpmGroupDTO> upmGroupList = upmGroupJdbc.findAllActiveUpmGroups();
    StandardResponse<List<UpmGroupDTO>> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupList);
    return ResponseEntity.ok().body(response);
  }
}
