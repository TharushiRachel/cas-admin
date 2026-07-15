package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.supportingdoc.SupportingDocDTO;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.supportingdoc.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocRepository;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocTempAudRepository;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocTempRepository;
import lk.sampath.casadminportalms.service.SupportingDocService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class SupportingDocServiceImpl implements SupportingDocService {

  private static final String SUPPORTING_DOC_WITH = "Supporting Doc with ";

  private static final String SUPPORTING_DOC_TEMP_WITH = "Supporting Doc Temp with ";
  private static final String DOES_NOT_EXISTS = "does not exists";
  private static final String ALREADY_EXISTS = "already exists";

  private static final String EMPTY_NULL = "Supporting Doc name cannot be empty or null";

  private final SupportingDocRepository supportingDocRepository;

  private final SupportingDocTempRepository supportingDocTempRepository;

  private final SupportingDocTempAudRepository supportingDocTempAudRepository;

  public SupportingDocServiceImpl(
      SupportingDocRepository supportingDocRepository,
      SupportingDocTempRepository supportingDocTempRepository,
      SupportingDocTempAudRepository supportingDocTempAudRepository) {
    this.supportingDocRepository = supportingDocRepository;
    this.supportingDocTempRepository = supportingDocTempRepository;
    this.supportingDocTempAudRepository = supportingDocTempAudRepository;
  }

  @Override
  public ResponseEntity<StandardResponse<List<SupportingDocDTO>>> findAllSupportingDocTempList(
      Pageable pageable) throws ApiRequestException {
    Page<SupportingDocTemp> supportingDocTempList = supportingDocTempRepository.findAll(pageable);
    StandardResponse<List<SupportingDocDTO>> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocTempList);
    return ResponseEntity.ok().body(response);
  }

  @Override
  public ResponseEntity<StandardResponse<SupportingDocDTO>> findSupportingDocTempById(
      Integer supportingDocID) throws ApiRequestException {
    SupportingDocTemp supportingDocTemp =
        supportingDocTempRepository
            .findById(supportingDocID)
            .orElseThrow(
                () ->
                    new ApiRequestException(
                        SUPPORTING_DOC_WITH + supportingDocID + DOES_NOT_EXISTS));
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocTemp);
    return ResponseEntity.ok().body(response);
  }

  @Override
  public ResponseEntity<StandardResponse<List<SupportingDocDTO>>> searchSupportingDocGroups(
      Pageable pageable) throws ApiRequestException {
    Page<SupportingDoc> supportingDocList = supportingDocRepository.findAll(pageable);
    StandardResponse<List<SupportingDocDTO>> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocList);

    return ResponseEntity.ok().body(response);
  }

  @Transactional(readOnly = true)
  public ResponseEntity<StandardResponse<SupportingDocDTO>> findSupportingDocById(
      Integer supportingDocID) throws ApiRequestException {
    SupportingDoc supportingDoc =
        supportingDocRepository
            .findById(supportingDocID)
            .orElseThrow(
                () ->
                    new ApiRequestException(
                        SUPPORTING_DOC_WITH + supportingDocID + DOES_NOT_EXISTS));
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), supportingDoc);
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<SupportingDocDTO>> saveSupportingDocTemp(
      SupportingDocDTO supportingDocDTO) throws ApiRequestException {

    log.info("START: save Supporting Document :{}", supportingDocDTO);

    if (supportingDocDTO == null
        || supportingDocDTO.getDocumentName() == null
        || supportingDocDTO.getDocumentName().trim().isEmpty()) {
      throw new ApiRequestException(EMPTY_NULL);
    }

    SupportingDocTemp supportingDocTempSave = new SupportingDocTemp();
    Date date = new Date();
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(
        QSupportingDocTemp.supportingDocTemp.documentName.eq(supportingDocDTO.getDocumentName()));
    List<SupportingDocTemp> supportingDocTemps =
        (List<SupportingDocTemp>) supportingDocTempRepository.findAll(booleanBuilder);

    validateSupportingDocNameUniqueness(supportingDocDTO.getDocumentName(), null);

    if (supportingDocTemps.isEmpty()) {
      supportingDocTempSave.setSupportingDocID(
          supportingDocTempRepository.getCurrentSequenceValue());
      supportingDocTempSave.setStatus(supportingDocDTO.getStatus());
      supportingDocTempSave.setCreatedDate(date);
      supportingDocTempSave.setLastModifiedDate(date);
      supportingDocTempSave.setApproveStatus(supportingDocDTO.getApproveStatus());
      supportingDocTempSave.setDocumentName(supportingDocDTO.getDocumentName());
      supportingDocTempSave.setDescription(supportingDocDTO.getDescription());
      supportingDocTempSave.setSupportDocumentType(supportingDocDTO.getSupportDocumentType());
      log.info(SUPPORTING_DOC_WITH, supportingDocTempSave);

      supportingDocTempSave = supportingDocTempRepository.saveAndFlush(supportingDocTempSave);
      log.info(
          "SUCCESS: Saved Supporting Doc with ID :{}", supportingDocTempSave.getSupportingDocID());

    } else {
      throw new ApiRequestException("Supporting Document Already Exists");
    }
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocTempSave);
    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<SupportingDocDTO>> approveRejectSupportingDoc(
      ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
    log.info("START: Approve/Reject Supporting Document: {}", approveRejectRQ);

    if (approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null) {
      throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
    }

    SupportingDocTemp supportingDocTemp =
        supportingDocTempRepository
            .findById(approveRejectRQ.getApproveRejectDataID())
            .orElseThrow(
                () -> {
                  throw new ApiRequestException(
                      SUPPORTING_DOC_WITH
                          + approveRejectRQ.getApproveRejectDataID()
                          + DOES_NOT_EXISTS);
                });

    Optional<SupportingDoc> optionalSupportingDoc =
        supportingDocRepository.findById(supportingDocTemp.getSupportingDocID());
    SupportingDoc findSupportingDoc = optionalSupportingDoc.orElse(null);
    log.info("Find Supporting Document: {}", findSupportingDoc);

    supportingDocTemp.setApprovedDate(new Date());
    supportingDocTemp.setApproveStatus(approveRejectRQ.getApproveStatus());
    supportingDocTemp.setApprovedBy(UserContext.getUsername());
    supportingDocTempRepository.saveAndFlush(supportingDocTemp);

    ResponseEntity<StandardResponse<SupportingDocDTO>> response;

    if (MasterDataApproveStatus.APPROVED.equals(approveRejectRQ.getApproveStatus())) {
      response = handleApproval(supportingDocTemp, findSupportingDoc);
    } else if (MasterDataApproveStatus.REJECTED.equals(approveRejectRQ.getApproveStatus())) {
      response = handleRejection(supportingDocTemp);

    } else {
      throw new ApiRequestException(
          "Unknown approval status: " + approveRejectRQ.getApproveStatus());
    }
    return response;
  }

  private ResponseEntity<StandardResponse<SupportingDocDTO>> handleApproval(
      SupportingDocTemp supportingDocTemp, SupportingDoc supportingDoc) {
    SupportingDoc savedSupportingDoc;

    if (supportingDoc != null
        && supportingDoc.getSupportingDocID().equals(supportingDocTemp.getSupportingDocID())) {
      savedSupportingDoc = updateSupportingDocToMaster(supportingDocTemp, supportingDoc);
    } else {
      savedSupportingDoc = mapSupportingDoc(supportingDocTemp, null);
    }

    insertToAuditTable(supportingDocTemp);

    supportingDocTempRepository.delete(supportingDocTemp);
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            savedSupportingDoc);
    return ResponseEntity.ok().body(response);
  }

  private ResponseEntity<StandardResponse<SupportingDocDTO>> handleRejection(
      SupportingDocTemp supportingDocTemp) {
    log.info(
        "Handling rejection for Supporting Doc Temp ID: {}",
        supportingDocTemp.getSupportingDocID());

    insertToAuditTable(supportingDocTemp);
    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocTemp);
    return ResponseEntity.ok().body(response);
  }

  private SupportingDoc updateSupportingDocToMaster(
      SupportingDocTemp supportingDocTemp, SupportingDoc existingSupportingDoc) {

    SupportingDoc supportingDoc =
        (existingSupportingDoc != null) ? existingSupportingDoc : new SupportingDoc();
    supportingDoc.setSupportingDocID(supportingDocTemp.getSupportingDocID());
    supportingDoc.setDocumentName(supportingDocTemp.getDocumentName());
    supportingDoc.setSupportDocumentType(supportingDocTemp.getSupportDocumentType());
    supportingDoc.setDescription(supportingDocTemp.getDescription());
    supportingDoc.setStatus(supportingDocTemp.getStatus());
    supportingDoc.setCreatedDate(supportingDocTemp.getCreatedDate());
    supportingDoc.setModifiedBy(supportingDocTemp.getModifiedBy());
    supportingDoc.setLastModifiedDate(new Date());
    supportingDoc.setApproveStatus(supportingDocTemp.getApproveStatus());
    supportingDoc.setApprovedBy(supportingDocTemp.getApprovedBy());
    supportingDoc.setApprovedDate(supportingDocTemp.getApprovedDate());
    supportingDoc.setCreatedBy(supportingDocTemp.getCreatedBy());

    return supportingDocRepository.saveAndFlush(supportingDoc);
  }

  private SupportingDoc mapSupportingDoc(
      SupportingDocTemp supportingDocTemp, SupportingDoc existingSupportingDoc) {

    SupportingDoc supportingDoc =
        (existingSupportingDoc != null) ? existingSupportingDoc : new SupportingDoc();

    supportingDoc.setSupportingDocID(supportingDocTemp.getSupportingDocID());
    supportingDoc.setDocumentName(supportingDocTemp.getDocumentName());
    supportingDoc.setSupportDocumentType(supportingDocTemp.getSupportDocumentType());
    supportingDoc.setDescription(supportingDocTemp.getDescription());
    supportingDoc.setStatus(supportingDocTemp.getStatus());
    supportingDoc.setCreatedDate(supportingDocTemp.getCreatedDate());
    supportingDoc.setModifiedBy(supportingDocTemp.getModifiedBy());
    supportingDoc.setLastModifiedDate(supportingDocTemp.getLastModifiedDate());
    supportingDoc.setApproveStatus(supportingDocTemp.getApproveStatus());
    supportingDoc.setApprovedBy(supportingDocTemp.getApprovedBy());
    supportingDoc.setApprovedDate(supportingDocTemp.getApprovedDate());
    supportingDoc.setCreatedBy(supportingDocTemp.getCreatedBy());

    supportingDocRepository.saveAndFlush(supportingDoc);

    return supportingDoc;
  }

  private void insertToAuditTable(SupportingDocTemp supportingDocTemp) {
    if (supportingDocTemp == null) return;

    SupportingDocAud supportingDocAud = new SupportingDocAud();

    supportingDocAud.setSupportingDocID(supportingDocTemp.getSupportingDocID());
    supportingDocAud.setDocumentName(supportingDocTemp.getDocumentName());
    supportingDocAud.setDescription(supportingDocTemp.getDescription());
    supportingDocAud.setSupportDocumentType(supportingDocTemp.getSupportDocumentType());
    supportingDocAud.setStatus(supportingDocTemp.getStatus());
    supportingDocAud.setApproveStatus(supportingDocTemp.getApproveStatus());
    supportingDocAud.setApprovedDate(supportingDocTemp.getApprovedDate());
    supportingDocAud.setApprovedBy(supportingDocTemp.getApprovedBy());
    supportingDocAud.setCreatedDate(supportingDocTemp.getCreatedDate());
    supportingDocAud.setCreatedBy(supportingDocTemp.getCreatedBy());
    supportingDocAud.setLastModifiedDate(supportingDocTemp.getLastModifiedDate());
    supportingDocAud.setModifiedBy(supportingDocTemp.getModifiedBy());

    supportingDocTempAudRepository.save(supportingDocAud);
    log.info(
        "Saved audit record for Supporting Document ID: {}", supportingDocAud.getSupportingDocID());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<SupportingDocDTO>> updateSupportingDocTemp(
      Integer supportingDocID, SupportingDocDTO supportingDocDTO) throws ApiRequestException {

    SupportingDocTemp supportingDocTemp =
        supportingDocTempRepository
            .findById(supportingDocID)
            .orElseThrow(
                () -> {
                  throw new ApiRequestException(
                      SUPPORTING_DOC_WITH + supportingDocID + DOES_NOT_EXISTS);
                });

    Date date = new Date();

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(
        QSupportingDocTemp.supportingDocTemp.documentName.eq(supportingDocDTO.getDocumentName()));
    List<SupportingDocTemp> supportingDocTempList =
        (List<SupportingDocTemp>) supportingDocTempRepository.findAll(booleanBuilder);

    if (supportingDocDTO.getDocumentName() == null
        || supportingDocDTO.getDocumentName().trim().isEmpty()) {
      throw new ApiRequestException(EMPTY_NULL);
    }

    validateSupportingDocNameUniqueness(supportingDocDTO.getDocumentName(), supportingDocID);

    boolean existingTempList =
        supportingDocTempList.stream()
            .anyMatch(temp -> temp.getDocumentName().equals(supportingDocDTO.getDocumentName()));

    if (existingTempList
        && !supportingDocTemp.getDocumentName().equals(supportingDocDTO.getDocumentName())) {
      throw new ApiRequestException(
          SUPPORTING_DOC_TEMP_WITH + supportingDocDTO.getDocumentName() + ALREADY_EXISTS);
    }
    supportingDocTemp.setDocumentName(supportingDocDTO.getDocumentName());
    supportingDocTemp.setSupportDocumentType(supportingDocDTO.getSupportDocumentType());
    supportingDocTemp.setStatus(supportingDocDTO.getStatus());
    supportingDocTemp.setDescription(supportingDocDTO.getDescription());
    supportingDocTemp.setApprovedBy(supportingDocDTO.getApprovedBy());
    supportingDocTemp.setApprovedDate(supportingDocDTO.getApprovedDate());
    supportingDocTemp.setApproveStatus(supportingDocDTO.getApproveStatus());
    supportingDocTemp.setModifiedBy(supportingDocDTO.getModifiedBy());
    supportingDocTemp.setLastModifiedDate(date);
    supportingDocTemp.setCreatedDate(supportingDocDTO.getCreatedDate());
    supportingDocTemp.setCreatedBy(supportingDocDTO.getCreatedBy());

    log.info("Updated Supporting Document Temp : {}", supportingDocTemp);

    supportingDocTempRepository.save(supportingDocTemp);

    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocTemp);

    return ResponseEntity.ok().body(response);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<SupportingDocDTO>> updateApprovedSupportingDoc(
      Integer supportingDocID, SupportingDocDTO supportingDocDTO) throws ApiRequestException {
    log.info("START: Update Supporting Document :{}", supportingDocDTO);

    SupportingDoc supportingDoc =
        supportingDocRepository
            .findById(supportingDocID)
            .orElseThrow(
                () -> {
                  throw new ApiRequestException(
                      SUPPORTING_DOC_WITH + supportingDocID + DOES_NOT_EXISTS);
                });

    log.info("START : GET SupportingDoc. {}", supportingDoc);
    if (!supportingDoc.getDocumentName().equals(supportingDocDTO.getDocumentName())) {
      validateSupportingDocNameUniqueness(supportingDocDTO.getDocumentName(), supportingDocID);
    } else if (supportingDocDTO.getDocumentName() == null
        || supportingDocDTO.getDocumentName().trim().isEmpty()) {
      throw new ApiRequestException(EMPTY_NULL);
    }

    SupportingDocTemp supportingDocTemp = mapToSupportingDocTemp(supportingDoc, supportingDocDTO);

    log.info("END : GET SupportingDoc {}", supportingDocTemp);
    supportingDocTemp = supportingDocTempRepository.saveAndFlush(supportingDocTemp);

    StandardResponse<SupportingDocDTO> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocTemp);
    return ResponseEntity.ok().body(response);
  }

  private void validateSupportingDocNameUniqueness(String documentName, Integer supportingDocID)
      throws ApiRequestException {
    // Check for duplicates in temp table
    BooleanBuilder tempBuilder = new BooleanBuilder();
    tempBuilder.and(QSupportingDocTemp.supportingDocTemp.documentName.eq(documentName));
    if (supportingDocID != null) {
      tempBuilder.and(QSupportingDocTemp.supportingDocTemp.supportingDocID.ne(supportingDocID));
    }
    boolean existsInTemp = supportingDocTempRepository.exists(tempBuilder);

    // Check for duplicates in master table
    BooleanBuilder masterBuilder = new BooleanBuilder();
    masterBuilder.and(QSupportingDoc.supportingDoc.documentName.eq(documentName));
    if (supportingDocID != null) {
      masterBuilder.and(QSupportingDoc.supportingDoc.supportingDocID.ne(supportingDocID));
    }
    boolean existsInMaster = supportingDocRepository.exists(masterBuilder);

    // Throw error if duplicate found
    if (existsInTemp || existsInMaster) {
      throw new ApiRequestException(
          "Document name '" + documentName + "' already exists in the system.");
    }
  }

  private SupportingDocTemp mapToSupportingDocTemp(
      SupportingDoc supportingDoc, SupportingDocDTO supportingDocDTO) {

    SupportingDocTemp supportingDocTemp = new SupportingDocTemp();

    Date date = new Date();
    supportingDocTemp.setSupportingDocID(supportingDoc.getSupportingDocID());
    supportingDocTemp.setDocumentName(supportingDocDTO.getDocumentName());
    supportingDocTemp.setSupportDocumentType(supportingDocDTO.getSupportDocumentType());
    supportingDocTemp.setDescription(supportingDocDTO.getDescription());
    supportingDocTemp.setStatus(supportingDocDTO.getStatus());
    supportingDocTemp.setCreatedBy(supportingDoc.getCreatedBy());
    supportingDocTemp.setCreatedDate(supportingDoc.getCreatedDate());
    supportingDocTemp.setApproveStatus(supportingDocDTO.getApproveStatus());
    supportingDocTemp.setApprovedBy(supportingDoc.getApprovedBy());
    supportingDocTemp.setApprovedDate(supportingDoc.getApprovedDate());
    supportingDocTemp.setModifiedBy(supportingDoc.getModifiedBy());
    supportingDocTemp.setLastModifiedDate(date);

    return supportingDocTemp;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public ResponseEntity<StandardResponse<Void>> deleteSupportingDocTemp(Integer supportingDocID)
      throws ApiRequestException {
    supportingDocTempRepository.deleteById(supportingDocID);
    StandardResponse<Void> response =
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            supportingDocID);
    return ResponseEntity.ok().body(response);
  }
}
