package lk.sampath.casadminportalms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.deviation.DeviationDTO;
import lk.sampath.casadminportalms.dto.deviation.DeviationTypeDTO;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.deviation.Deviation;
import lk.sampath.casadminportalms.entity.deviation.DeviationAud;
import lk.sampath.casadminportalms.entity.deviation.DeviationType;
import lk.sampath.casadminportalms.entity.deviation.TempDeviation;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.deviation.DeviationAudRepository;
import lk.sampath.casadminportalms.repository.deviation.DeviationRepository;
import lk.sampath.casadminportalms.repository.deviation.DeviationTempRepository;
import lk.sampath.casadminportalms.repository.deviation.DeviationTypeRepository;
import lk.sampath.casadminportalms.service.DeviationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class DeviationServiceImpl implements DeviationService {

  private final DeviationTypeRepository deviationTypeRepository;

  private final DeviationRepository deviationRepository;

  private final DeviationTempRepository deviationTempRepository;

  private final DeviationAudRepository deviationAudRepository;

  public DeviationServiceImpl(
      DeviationTypeRepository deviationTypeRepository,
      DeviationRepository deviationRepository,
      DeviationTempRepository deviationTempRepository,
      DeviationAudRepository deviationAudRepository) {
    this.deviationTypeRepository = deviationTypeRepository;
    this.deviationRepository = deviationRepository;
    this.deviationTempRepository = deviationTempRepository;
    this.deviationAudRepository = deviationAudRepository;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<DeviationTypeDTO>> saveDeviationType(
      DeviationTypeDTO deviationTypeDTO) throws ApiRequestException {
    log.info(
        "START: DeviationServiceImpl | saveDeviationType | Save Deviation Type: {}",
        deviationTypeDTO);

    try {
      Date now = new Date();

      DeviationType deviationType =
          (deviationTypeDTO.getDeviationTypeId() == null
                  || deviationTypeDTO.getDeviationTypeId() == 0)
              ? new DeviationType()
              : deviationTypeRepository
                  .findById(deviationTypeDTO.getDeviationTypeId())
                  .orElseThrow(
                      () ->
                          new ApiRequestException(
                              "Deviation Type not found with ID: "
                                  + deviationTypeDTO.getDeviationTypeId()));

      if (deviationType.getDeviationTypeId() == null) {
        deviationType.setCreatedDate(now);
      } else {
        deviationType.setLastModifiedDate(now);
      }

      deviationType.setDeviationType(deviationTypeDTO.getDeviationType());
      deviationType.setStatus(deviationTypeDTO.getStatus());

      log.info("Saving Deviation Type: {}", deviationType.getDeviationType());
      deviationTypeRepository.save(deviationType);

      DeviationTypeDTO savedDto = new DeviationTypeDTO(deviationType);
      StandardResponse<DeviationTypeDTO> response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedDto);

      log.info(
          "END: DeviationServiceImpl | saveDeviationType | Saved Deviation Type: {}", savedDto);
      return ResponseEntity.ok(response);

    } catch (ApiRequestException e) {
      throw e;
    } catch (Exception e) {
      log.error("ERROR: DeviationServiceImpl | saveDeviationType", e);
      throw new ApiRequestException("Failed to save deviation type");
    }
  }

  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  @Override
  public ResponseEntity<StandardResponse<List<DeviationTypeDTO>>> getAllDeviationTypes()
      throws ApiRequestException {

    log.info("START: DeviationServiceImpl | getAllDeviationTypes| Get All Deviation Types");
    List<DeviationTypeDTO> response = new ArrayList<>();

    try {
      List<DeviationType> types = deviationTypeRepository.findAll();
      if (!types.isEmpty()) {
        response = types.stream().map(DeviationTypeDTO::new).collect(Collectors.toList());
      }

      StandardResponse<List<DeviationTypeDTO>> deviationDtoList =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), response);
      return ResponseEntity.ok(deviationDtoList);

    } catch (Exception e) {
      log.info("ERROR: Get All Deviation Types: ", e);
      throw new ApiRequestException("Failed to get all deviation types");
    }
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<DeviationTypeDTO>> getDeviationTypeById(
      Integer deviationTypeId) throws ApiRequestException {
    DeviationType deviationType =
        deviationTypeRepository
            .findById(deviationTypeId)
            .orElseThrow(
                () ->
                    new ApiRequestException(
                        "Deviation Type not found with ID: " + deviationTypeId));

    DeviationTypeDTO deviationTypeDTO = new DeviationTypeDTO(deviationType);
    StandardResponse<DeviationTypeDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            deviationTypeDTO);
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<DeviationDTO>> saveOrUpdateDeviation(
      DeviationDTO deviationDTO) throws ApiRequestException {

    log.info("START: DeviationServiceImpl | saveDeviation | Save Deviation {}", deviationDTO);
    try {
      Date now = new Date();

      TempDeviation tempDeviation =
          (deviationDTO.getDeviationId() == null || deviationDTO.getDeviationId() == 0)
              ? new TempDeviation()
              : deviationTempRepository
                  .findById(deviationDTO.getDeviationId())
                  .orElseThrow(
                      () ->
                          new ApiRequestException(
                              "Deviation Type not found with ID: "
                                  + deviationDTO.getDeviationId()));

      if (deviationDTO.getDeviationId() == null || deviationDTO.getDeviationId() == 0) {
        tempDeviation.setCreatedDate(now);
        tempDeviation.setDeviationId(deviationTempRepository.getCurrentSequenceValue());
      } else {
        tempDeviation.setLastModifiedDate(now);
      }

      tempDeviation.setDeviationType(deviationDTO.getDeviationType());
      tempDeviation.setDescription(deviationDTO.getDescription());
      tempDeviation.setStatus(deviationDTO.getStatus());
      tempDeviation.setApproveStatus(deviationDTO.getApproveStatus());

      log.info("Saving Deviation: {}", tempDeviation.getDescription());
      deviationTempRepository.save(tempDeviation);

      DeviationDTO savedDto = new DeviationDTO(tempDeviation);
      StandardResponse<DeviationDTO> response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedDto);

      log.info("END: DeviationServiceImpl | saveDeviation | Saved Deviation: {}", savedDto);
      return ResponseEntity.ok(response);

    } catch (ApiRequestException e) {
      throw e;
    } catch (Exception e) {
      log.error("ERROR: DeviationServiceImpl | saveDeviationType", e);
      throw new ApiRequestException("Failed to save deviation type");
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<DeviationDTO>> approveOrRejectDeviation(
      ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
    log.info(
        "START: DeviationServiceImpl | approveOrRejectDeviation | Approve/Reject Deviation: {}",
        approveRejectRQ);

    if (approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null) {
      throw new ApiRequestException("Deviation ID is required for approval/rejection");
    }

    TempDeviation tempDeviation =
        deviationTempRepository
            .findById(approveRejectRQ.getApproveRejectDataID())
            .orElseThrow(
                () ->
                    new ApiRequestException(
                        "Deviation not found with ID: "
                            + approveRejectRQ.getApproveRejectDataID()));

    Optional<Deviation> existingDeviation =
        deviationRepository.findById(tempDeviation.getDeviationId());
    Deviation finalDeviation = existingDeviation.orElse(null);
    log.info("Existing Deviation: {}", finalDeviation);

    tempDeviation.setApprovedDate(new Date());
    tempDeviation.setApprovedBy(UserContext.getUsername());
    tempDeviation.setApproveStatus(approveRejectRQ.getApproveStatus());

    deviationTempRepository.save(tempDeviation);

    ResponseEntity<StandardResponse<DeviationDTO>> response;

    if (MasterDataApproveStatus.APPROVED.equals(approveRejectRQ.getApproveStatus())) {
      response = handleApproval(tempDeviation, finalDeviation);
    } else if (MasterDataApproveStatus.REJECTED.equals(approveRejectRQ.getApproveStatus())) {
      response = handleRejection(tempDeviation, finalDeviation);
    } else {
      throw new ApiRequestException("Invalid approval status");
    }

    log.info(
        "END: DeviationServiceImpl | approveOrRejectDeviation | Approve/Reject Deviation: {}",
        response);
    return response;
  }

  private ResponseEntity<StandardResponse<DeviationDTO>> handleRejection(
      TempDeviation tempDeviation, Deviation finalDeviation) {
    log.info(
        "DeviationServiceImpl | handleRejection | Handling rejection for Deviation ID: {}",
        tempDeviation.getDeviationId());

    saveDeviationAudit(tempDeviation);
    StandardResponse<DeviationDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            new DeviationDTO(tempDeviation));
    log.info(
        "DeviationServiceImpl | handleRejection | Rejected Deviation processed for Deviation ID: {}",
        tempDeviation.getDeviationId());
    return ResponseEntity.ok().body(response);
  }

  private ResponseEntity<StandardResponse<DeviationDTO>> handleApproval(
      TempDeviation tempDeviation, Deviation finalDeviation) {

    log.info(
        "DeviationServiceImpl | handleApproval | Handling approval for Deviation ID: {}",
        tempDeviation.getDeviationId());

    // Determine whether to update or create
    Deviation deviationToSave =
        (finalDeviation != null
                && finalDeviation.getDeviationId().equals(tempDeviation.getDeviationId()))
            ? finalDeviation
            : new Deviation();

    // Set ID only for new entity
    if (deviationToSave.getDeviationId() == null) {
      deviationToSave.setDeviationId(tempDeviation.getDeviationId());
    }

    // Map common fields
    mapTempToFinal(tempDeviation, deviationToSave);

    // Save
    Deviation savedDeviation = deviationRepository.save(deviationToSave);

    saveDeviationAudit(tempDeviation);
    // Delete temp record
    deviationTempRepository.delete(tempDeviation);

    log.info(
        "DeviationServiceImpl | handleApproval | Approved Deviation saved with ID: {}",
        savedDeviation.getDeviationId());

    // Build response
    return ResponseEntity.ok(
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            new DeviationDTO(savedDeviation)));
  }

  private void mapTempToFinal(TempDeviation source, Deviation target) {
    target.setDeviationType(source.getDeviationType());
    target.setDescription(source.getDescription());
    target.setStatus(source.getStatus());

    target.setCreatedDate(source.getCreatedDate());
    target.setCreatedBy(source.getCreatedBy());
    target.setModifiedBy(source.getModifiedBy());
    target.setLastModifiedDate(source.getLastModifiedDate());

    target.setApprovedBy(source.getApprovedBy());
    target.setApprovedDate(source.getApprovedDate());
    target.setApproveStatus(source.getApproveStatus());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<DeviationDTO>> updateApprovedDiversion(
      DeviationDTO deviationDTO) throws ApiRequestException {
    log.info(
        "START: DeviationServiceImpl | updateApprovedDiversion | Update Approved Deviation: {}",
        deviationDTO);

    Deviation existingDeviation =
        deviationRepository
            .findById(deviationDTO.getDeviationId())
            .orElseThrow(
                () ->
                    new ApiRequestException(
                        "Deviation not found with ID: " + deviationDTO.getDeviationId()));

    TempDeviation tempDeviation = mapToDiviationTemp(existingDeviation, deviationDTO);
    TempDeviation savedTemp = deviationTempRepository.saveAndFlush(tempDeviation);

    StandardResponse<DeviationDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            new DeviationDTO(savedTemp));
    log.info(
        "END: DeviationServiceImpl | updateApprovedDiversion | Updated Approved Deviation: {}",
        response);
    return ResponseEntity.ok(response);
  }

  private TempDeviation mapToDiviationTemp(Deviation existingDeviation, DeviationDTO deviationDTO) {

    TempDeviation tempDeviation = new TempDeviation();
    tempDeviation.setDeviationId(existingDeviation.getDeviationId());
    tempDeviation.setDeviationType(deviationDTO.getDeviationType());
    tempDeviation.setDescription(deviationDTO.getDescription());
    tempDeviation.setStatus(deviationDTO.getStatus());
    tempDeviation.setCreatedDate(existingDeviation.getCreatedDate());
    tempDeviation.setCreatedBy(existingDeviation.getCreatedBy());
    tempDeviation.setLastModifiedDate(new Date());
    tempDeviation.setModifiedBy(deviationDTO.getModifiedBy());
    tempDeviation.setApproveStatus(deviationDTO.getApproveStatus());
    tempDeviation.setApprovedDate(deviationDTO.getApprovedDate());
    tempDeviation.setApprovedBy(deviationDTO.getApprovedBy());
    return tempDeviation;
  }

  private void saveDeviationAudit(TempDeviation tempDeviation) {
    log.info(
        "DeviationServiceImpl | saveDeviationAudit | Saving audit for Deviation ID: {}",
        tempDeviation.getDeviationId());

    if (tempDeviation == null) return;

    DeviationAud deviationAud = new DeviationAud();
    deviationAud.setDeviationId(tempDeviation.getDeviationId());
    deviationAud.setDeviationType(tempDeviation.getDeviationType());
    deviationAud.setDescription(tempDeviation.getDescription());
    deviationAud.setStatus(tempDeviation.getStatus());
    deviationAud.setCreatedDate(tempDeviation.getCreatedDate());
    deviationAud.setCreatedBy(tempDeviation.getCreatedBy());
    deviationAud.setLastModifiedDate(tempDeviation.getLastModifiedDate());
    deviationAud.setModifiedBy(tempDeviation.getModifiedBy());
    deviationAud.setApprovedBy(tempDeviation.getApprovedBy());
    deviationAud.setApprovedDate(tempDeviation.getApprovedDate());
    deviationAud.setApproveStatus(tempDeviation.getApproveStatus());

    deviationAudRepository.save(deviationAud);
    log.info(
        "DeviationServiceImpl | saveDeviationAudit | Saved audit for Deviation ID: {}",
        tempDeviation.getDeviationId());
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<List<DeviationDTO>>> getAllDeviationTempList()
      throws ApiRequestException {
    log.info("START: DeviationServiceImpl | getAllDeviationTempList | Get All Deviation Temp List");
    List<TempDeviation> tempDeviationList = deviationTempRepository.findAll();
    List<DeviationDTO> deviationDTOList =
        tempDeviationList.stream().map(DeviationDTO::new).collect(Collectors.toList());
    StandardResponse<List<DeviationDTO>> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            deviationDTOList);
    log.info(
        "END: DeviationServiceImpl | getAllDeviationTempList | Get All Deviation Temp List {}",
        deviationDTOList.size());
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<DeviationDTO>> getDeviationTempById(Integer deviationId)
      throws ApiRequestException {
    log.info(
        "START: DeviationServiceImpl | getDeviationTempById | Get Deviation Temp By ID: {}",
        deviationId);
    TempDeviation tempDeviation =
        deviationTempRepository
            .findById(deviationId)
            .orElseThrow(
                () -> new ApiRequestException("Deviation Temp not found with ID: " + deviationId));
    DeviationDTO deviationDTO = new DeviationDTO(tempDeviation);
    StandardResponse<DeviationDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), deviationDTO);
    log.info(
        "END: DeviationServiceImpl | getDeviationTempById | Get Deviation Temp By ID: {}",
        deviationDTO);
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<List<DeviationDTO>>> getAllDeviationMasterList()
      throws ApiRequestException {
    log.info(
        "START: DeviationServiceImpl | getAllDeviationMasterList | Get All Deviation Master List");
    List<Deviation> deviationList = deviationRepository.findAll();
    List<DeviationDTO> deviationDTOList =
        deviationList.stream().map(DeviationDTO::new).collect(Collectors.toList());
    StandardResponse<List<DeviationDTO>> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            deviationDTOList);
    log.info(
        "END: DeviationServiceImpl | getAllDeviationMasterList | Get All Deviation Master List {}",
        deviationDTOList.size());
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<DeviationDTO>> getDeviationMasterById(Integer deviationId)
      throws ApiRequestException {
    log.info(
        "START: DeviationServiceImpl | getDeviationMasterById | Get Deviation Master By ID: {}",
        deviationId);
    Deviation deviation =
        deviationRepository
            .findById(deviationId)
            .orElseThrow(
                () ->
                    new ApiRequestException("Deviation Master not found with ID: " + deviationId));
    DeviationDTO deviationDTO = new DeviationDTO(deviation);
    StandardResponse<DeviationDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), deviationDTO);
    log.info(
        "END: DeviationServiceImpl | getDeviationMasterById | Get Deviation Master By ID: {}",
        deviationDTO);
    return ResponseEntity.ok().body(response);
  }
}
