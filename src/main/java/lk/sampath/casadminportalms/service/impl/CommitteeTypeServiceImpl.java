package lk.sampath.casadminportalms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import lk.sampath.casadminportalms.service.CommitteeTypeService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class CommitteeTypeServiceImpl implements CommitteeTypeService {

  private static final Logger LOG = LoggerFactory.getLogger(CommitteeTypeServiceImpl.class);

  private final CommitteeTypeRepository committeeTypeRepository;

  private final CommitteeTypeAudRepository committeeTypeAudRepository;

  @Autowired
  public CommitteeTypeServiceImpl(
      CommitteeTypeRepository committeeTypeRepository,
      CommitteeTypeAudRepository committeeTypeAudRepository) {
    this.committeeTypeRepository = committeeTypeRepository;
    this.committeeTypeAudRepository = committeeTypeAudRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> getCommitteeTypes()
      throws ApiRequestException {
    try {
      List<CommitteeType> committeeTypeList = this.committeeTypeRepository.findAll();
      List<CommitteeTypeDTO> committeeTypes = committeeTypeList.stream().map(CommitteeTypeDTO :: new).toList();

      StandardResponse<List<CommitteeTypeDTO>> response =
              new StandardResponse<>(
                      ErrorEnums.SUCCESS_CODE.getStatus(),
                      ErrorEnums.SUCCESS_CODE.getLabel(),
                      committeeTypes);
      return ResponseEntity.ok().body(response);
    } catch (Exception e){
      log.error("Error : CommitteeTypeServiceImpl | getCommitteeTypes : ", e);
      throw new ApiRequestException("Failed to retrieve committee types");
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> saveCommitteeType(
      CommitteeTypeDTO request) throws ApiRequestException {

    StandardResponse<List<CommitteeTypeDTO>> response;
    List<CommitteeType> committeeTypeList;

    try {
      List<CommitteeType> duplicateCommitteeType =
          this.committeeTypeRepository.findByCommitteeType(
              request.getCommitteeType().trim().toLowerCase());

      if (!duplicateCommitteeType.isEmpty()) {
        throw new ApiRequestException("Committee Type is already exists.");
      }

      CommitteeType committeeType = new CommitteeType();
      committeeType.setCommitteeTypeName(request.getCommitteeType());
      committeeType.setCommitteeTypeDescription(request.getCommitteeTypeName());
      committeeType.setStatus(AppsConstants.Status.ACT);
      committeeType.setIsSystem(request.getIsSystem());
      committeeType.setCreatedUserDisplayName(UserContext.getDisplayName());
      committeeType.setApprovedBy(UserContext.getUsername());
      committeeType.setApprovedDate(new Date());
      committeeType.setApproveStatus(MasterDataApproveStatus.APPROVED);

      committeeType = this.committeeTypeRepository.save(committeeType);

      // save to audit
      saveCommitteeTypeAud(committeeType);

      committeeTypeList = this.committeeTypeRepository.findAll();
      List<CommitteeTypeDTO> committeeTypes = committeeTypeList.stream().map(CommitteeTypeDTO :: new).toList();
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
                  committeeTypes);

    } catch (ApiRequestException e) {
      LOG.error("ERROR : Committee type saving failed: ", e);
      String message =
          !e.getMessage().isEmpty() ? e.getMessage() : "Committee Type saving has been failed.";
      throw new ApiRequestException(message);
    }

    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> updateCommitteeType(
      CommitteeTypeDTO request) throws ApiRequestException {
    StandardResponse<List<CommitteeTypeDTO>> response;
    List<CommitteeType> committeeTypeList;
    try {
      CommitteeType committeeType =
          this.committeeTypeRepository
              .findById(request.getCommitteeTypeId())
              .orElseThrow(
                  () ->
                      new ApiRequestException(
                          "Committee Type with ID "
                              + request.getCommitteeTypeId()
                              + " does not exist"));

      committeeType.setCommitteeTypeName(request.getCommitteeType());
      committeeType.setCommitteeTypeDescription(request.getCommitteeTypeName());
      committeeType.setStatus(request.getStatus());
      committeeType.setIsSystem(request.getIsSystem());
      committeeType.setModifiedBy(UserContext.getUsername());
      committeeType.setLastModifiedDate(new Date());
      committeeType.setApprovedBy(UserContext.getUsername());
      committeeType.setApprovedDate(new Date());
      committeeType.setApproveStatus(MasterDataApproveStatus.APPROVED);

      committeeType = this.committeeTypeRepository.save(committeeType);

      // save to audit
      saveCommitteeTypeAud(committeeType);

      committeeTypeList = this.committeeTypeRepository.findAll();
      List<CommitteeTypeDTO> committeeTypes = committeeTypeList.stream().map(CommitteeTypeDTO :: new).toList();
      response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
                  committeeTypes);

    } catch (ApiRequestException e) {
      LOG.error("ERROR : Committee type update failed: ", e);
      String message =
          !e.getMessage().isEmpty() ? e.getMessage() : "Committee Type update has been failed.";
      throw new ApiRequestException(message);
    }
    return ResponseEntity.ok().body(response);
  }

  private void saveCommitteeTypeAud(CommitteeType committeeType) {

    CommitteeTypeAud committeeTypeAud = new CommitteeTypeAud();
    committeeTypeAud.setCommitteeTypeId(committeeType.getCommitteeTypeId());
    committeeTypeAud.setCommitteeType(committeeType.getCommitteeTypeName());
    committeeTypeAud.setCommitteeTypeName(committeeType.getCommitteeTypeDescription());
    committeeTypeAud.setStatus(committeeType.getStatus());
    committeeTypeAud.setIsSystem(committeeType.getIsSystem());
    committeeTypeAud.setCreatedUserDisplayName(committeeType.getCreatedUserDisplayName());
    committeeTypeAud.setModifiedBy(committeeType.getModifiedBy());
    committeeTypeAud.setLastModifiedDate(committeeType.getLastModifiedDate());
    committeeTypeAud.setCreatedBy(committeeType.getCreatedBy());
    committeeTypeAud.setCreatedDate(committeeType.getCreatedDate());
    committeeTypeAud.setApprovedBy(committeeType.getApprovedBy());
    committeeTypeAud.setApprovedDate(committeeType.getApprovedDate());
    committeeTypeAud.setApproveStatus(committeeType.getApproveStatus());

    this.committeeTypeAudRepository.save(committeeTypeAud);
    log.info("Saved audit record for Committee Type ID: {}", committeeType.getCommitteeTypeId());
  }
}
