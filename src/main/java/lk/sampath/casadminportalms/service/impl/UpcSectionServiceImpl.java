package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upcsection.UpcSectionDTO;
import lk.sampath.casadminportalms.dto.userSession.UserContext;
import lk.sampath.casadminportalms.entity.upcsection.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionAudRepository;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionRepository;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionTempRepository;
import lk.sampath.casadminportalms.service.UpcSectionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *  *
 *  *
 *  * @author chamika
 *  */

@Service
@Transactional
@Log4j2
public class UpcSectionServiceImpl implements UpcSectionService {
    
    private static final String UPC_SECTION_WITH = "UPC Section with ";

    private static final String UPC_SECTION_TEMP_WITH = "UPC Section with TEMP ";

    private static final String DOES_NOT_EXISTS = " does not exists";

    private static final String EMPTY_NULL = "Upc Section name cannot be empty or null.";

    private final UpcSectionRepository upcSectionRepository;

    private final UpcSectionTempRepository upcSectionTempRepository;

    private final UpcSectionAudRepository upcSectionAudRepository;

    public UpcSectionServiceImpl(UpcSectionRepository upcSectionRepository, UpcSectionTempRepository upcSectionTempRepository, UpcSectionAudRepository upcSectionAudRepository) {
        this.upcSectionRepository = upcSectionRepository;
        this.upcSectionTempRepository = upcSectionTempRepository;
        this.upcSectionAudRepository = upcSectionAudRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<UpcSectionDTO>>> findAllUpcSectionTempList(Pageable pageable) throws ApiRequestException {
        log.info("START: UpcSectionServiceImpl | findAllUpcSectionTempList with pageable: {}", pageable);
        Page<UpcSectionTemp> upcSectionList = upcSectionTempRepository.findAll(pageable);
        StandardResponse<List<UpcSectionDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionList);
        log.info("END: UpcSectionServiceImpl | findAllUpcSectionTempList with pageable: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> findUpcSectionTempByID(Integer upcSectionID) throws ApiRequestException {
        log.info("START: UpcSectionServiceImpl | findUpcSectionTempByID with upcSectionID: {}", upcSectionID);
        UpcSectionTemp upcSectionTemp = upcSectionTempRepository.findById(upcSectionID)
                .orElseThrow(() -> new ApiRequestException(UPC_SECTION_TEMP_WITH + upcSectionID + DOES_NOT_EXISTS));
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionTemp);
        log.info("END: UpcSectionServiceImpl | findUpcSectionTempByID with upcSectionID: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<UpcSectionDTO>>> findAllApprovedUpcSection(Pageable pageable) {
        log.info("START: findAllApprovedUpcSection with pageable: {}", pageable);
        Page<UpcSection> upcSectionList = upcSectionRepository.findAll(pageable);
        StandardResponse<List<UpcSectionDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionList);
        log.info("END: findAllApprovedUpcSection with pageable: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> findApprovedUpcSectionByID(Integer upcSectionID) throws ApiRequestException {
        log.info("START: findApprovedUpcSectionByID with upcSectionID: {}", upcSectionID);
        UpcSection upcSection = upcSectionRepository.findById(upcSectionID).orElseThrow(() -> {
            throw new ApiRequestException(UPC_SECTION_WITH + upcSectionID + DOES_NOT_EXISTS);
        });
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSection);
        log.info("END: findApprovedUpcSectionByID with upcSectionID: {}", response);
        return ResponseEntity.ok().body(response);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> saveUpcSectionTemp(UpcSectionDTO upcSectionDTO) throws ApiRequestException {
        log.info("START: UpcSectionServiceImpl | saveUpcSectionTemp with upcSectionDTO: {}", upcSectionDTO);

        if (upcSectionDTO == null || upcSectionDTO.getUpcSectionName() == null || upcSectionDTO.getUpcSectionName().trim().isEmpty()) {
            throw new ApiRequestException(EMPTY_NULL);
        }
        Date date = new Date();
        UpcSectionTemp upcSectionTemp = new UpcSectionTemp();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUpcSectionTemp.upcSectionTemp.upcSectionName.eq(upcSectionDTO.getUpcSectionName()));
        List<UpcSectionTemp> upcSectionTemps = (List<UpcSectionTemp>) upcSectionTempRepository.findAll(booleanBuilder);
        log.info("Checking for existing Upc Section Temp with name: {}", upcSectionTemps.size());

        validateUpcSectionNameUniqueness(upcSectionDTO.getUpcSectionName(), null);

        if (upcSectionTemps.isEmpty()) {
            log.info("Saving new Upc Section Temp with name: {}", upcSectionDTO.getUpcSectionName());

            upcSectionTemp.setUpcSectionID(upcSectionTempRepository.getCurrentSequenceValue());
            upcSectionTemp.setStatus(upcSectionDTO.getStatus());
            upcSectionTemp.setCreatedDate(date);
            upcSectionTemp.setUpcSectionName(upcSectionDTO.getUpcSectionName());
            upcSectionTemp.setApprovedBy(upcSectionDTO.getApprovedBy());
            upcSectionTemp.setUpcSectionDescription(upcSectionDTO.getUpcSectionDescription());
            upcSectionTemp.setApprovedDate(upcSectionDTO.getApprovedDate());
            upcSectionTemp.setCreatedBy(upcSectionDTO.getCreatedBy());
            upcSectionTemp.setModifiedBy(upcSectionDTO.getModifiedBy());
            upcSectionTemp.setApproveStatus(upcSectionDTO.getApproveStatus());
            log.info(UPC_SECTION_WITH, upcSectionDTO);

            upcSectionTemp = upcSectionTempRepository.saveAndFlush(upcSectionTemp);
            log.info("SUCCESS: Saved Upc Section with ID : {}", upcSectionTemp.getUpcSectionID());

        } else {
            throw new ApiRequestException("Upc Section Temp Already Exists");
        }
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionTemp);

        log.info("END: UpcSectionServiceImpl | saveUpcSectionTemp with response: {}", response);
        return ResponseEntity.ok().body(response);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> approveRejectUpcSection(ApproveRejectRQ approveRejectRQ) throws ApiRequestException {

        log.info("START: UpcSectionServiceImpl | approveRejectUpcSection with ApproveRejectRQ: {}", approveRejectRQ);
        if (approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null) {
            throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
        }

        UpcSectionTemp upcSectionTemp = upcSectionTempRepository.findById(approveRejectRQ.getApproveRejectDataID())
                .orElseThrow(() -> new ApiRequestException(UPC_SECTION_TEMP_WITH + approveRejectRQ.getApproveRejectDataID() + DOES_NOT_EXISTS));

        Optional<UpcSection> optionalUpcSection = upcSectionRepository.findById(upcSectionTemp.getUpcSectionID());
        UpcSection findUpcSection = optionalUpcSection.orElse(null);
        log.info("find Upc Section : {}", findUpcSection);

        upcSectionTemp.setApprovedDate(new Date());
        upcSectionTemp.setApproveStatus(approveRejectRQ.getApproveStatus());
        upcSectionTemp.setApprovedBy(UserContext.getUsername());

        upcSectionTempRepository.saveAndFlush(upcSectionTemp);

        ResponseEntity<StandardResponse<UpcSectionDTO>> response;

        if (MasterDataApproveStatus.APPROVED.equals(approveRejectRQ.getApproveStatus())) {
            response = handleApproval(upcSectionTemp, findUpcSection);
        } else if (MasterDataApproveStatus.REJECTED.equals(approveRejectRQ.getApproveStatus())) {
            response = handleRejection(upcSectionTemp);
        } else {
            throw new ApiRequestException("Unknown approval status: " + approveRejectRQ.getApproveStatus());
        }
        log.info("END: UpcSectionServiceImpl | approveRejectUpcSection with response: {}", response);
        return response;
    }

    private ResponseEntity<StandardResponse<UpcSectionDTO>> handleApproval(UpcSectionTemp temp, UpcSection existingUpcSection) {
        log.info("UpcSectionServiceImpl | Handling approval for UPC Section Temp ID: {} ", temp.getUpcSectionID());
        UpcSection savedUpcSection;

        if (existingUpcSection != null && existingUpcSection.getUpcSectionID().equals(temp.getUpcSectionID())) {
            savedUpcSection = updateUpcSectionToMaster(temp, existingUpcSection);
        } else {
            savedUpcSection = mapUpcSection(temp, null);
        }

        saveUpcSectionAudit(temp);
        upcSectionTempRepository.delete(temp);
        log.info("Handling Approval for supporting doc temp ID: {} ", savedUpcSection.getUpcSectionID());
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedUpcSection);
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<StandardResponse<UpcSectionDTO>> handleRejection(UpcSectionTemp temp) {
        log.info("Handling rejection for UPC Section Temp ID: {} ", temp.getUpcSectionID());

        saveUpcSectionAudit(temp);
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), temp);
        return ResponseEntity.ok().body(response);
    }

    private UpcSection updateUpcSectionToMaster(UpcSectionTemp upcSectionTemp, UpcSection upcSection) {

        log.info("UpcSectionServiceImpl | Updating existing UPC Section ID: {} to master with data from temp ID: {}", upcSection.getUpcSectionID(), upcSectionTemp.getUpcSectionID());
        Date date = new Date();
        upcSection.setStatus(upcSectionTemp.getStatus());
        upcSection.setCreatedDate(date);
        upcSection.setLastModifiedDate(date);
        upcSection.setApproveStatus(upcSectionTemp.getApproveStatus());
        upcSection.setUpcSectionDescription(upcSectionTemp.getUpcSectionDescription());
        upcSection.setUpcSectionName(upcSectionTemp.getUpcSectionName());
        upcSection.setApprovedDate(upcSectionTemp.getApprovedDate());
        upcSection.setApprovedBy(upcSectionTemp.getApprovedBy());
        upcSection.setApproveStatus(upcSectionTemp.getApproveStatus());
        upcSection.setCreatedBy(upcSectionTemp.getCreatedBy());
        upcSection.setModifiedBy(upcSectionTemp.getModifiedBy());

        log.info("UpcSectionServiceImpl | Updated existing UPC Section ID: {} to master with data from temp ID: {}", upcSection.getUpcSectionID(), upcSectionTemp.getUpcSectionID());
        return upcSectionRepository.saveAndFlush(upcSection);
    }

    private UpcSection mapUpcSection(UpcSectionTemp upcSectionTemp, UpcSection existingUpcSection) {
        log.info("UpcSectionServiceImpl | Mapping UPC Section Temp ID: {} to master. Existing UPC Section: {}", upcSectionTemp.getUpcSectionID(), existingUpcSection);
        UpcSection upcSection = (existingUpcSection != null) ? existingUpcSection : new UpcSection();
        Date date = new Date();

        upcSection.setUpcSectionID(upcSectionTemp.getUpcSectionID());
        upcSection.setUpcSectionName(upcSectionTemp.getUpcSectionName());
        upcSection.setUpcSectionDescription(upcSectionTemp.getUpcSectionDescription());
        upcSection.setStatus(upcSectionTemp.getStatus());
        upcSection.setApprovedDate(upcSectionTemp.getApprovedDate());
        upcSection.setApprovedBy(upcSectionTemp.getApprovedBy());
        upcSection.setApproveStatus(upcSectionTemp.getApproveStatus());
        upcSection.setCreatedBy(upcSectionTemp.getCreatedBy());
        upcSection.setModifiedBy(upcSectionTemp.getModifiedBy());
        upcSection.setCreatedDate(date);
        upcSection.setLastModifiedDate(date);

        upcSectionRepository.saveAndFlush(upcSection);

        log.info("UpcSectionServiceImpl | Mapped UPC Section Temp ID: {} to master. Resulting UPC Section ID: {}", upcSectionTemp.getUpcSectionID(), upcSection.getUpcSectionID());
        return upcSection;
    }

    private void saveUpcSectionAudit(UpcSectionTemp temp) {

        log.info("UpcSectionServiceImpl | saveUpcSectionAudit Temp ID: {}", temp.getUpcSectionID());
        if (temp == null) return;

        UpcSectionAud audit = new UpcSectionAud();
        audit.setUpcSectionID(temp.getUpcSectionID());
        audit.setUpcSectionName(temp.getUpcSectionName());
        audit.setUpcSectionDescription(temp.getUpcSectionDescription());
        audit.setStatus(temp.getStatus());
        audit.setApproveStatus(temp.getApproveStatus());
        audit.setApprovedDate(temp.getApprovedDate());
        audit.setApprovedBy(temp.getApprovedBy());
        audit.setCreatedBy(temp.getCreatedBy());
        audit.setCreatedDate(temp.getCreatedDate());
        audit.setModifiedBy(temp.getModifiedBy());
        audit.setLastModifiedDate(temp.getLastModifiedDate());

        upcSectionAudRepository.save(audit);
        log.info("UpcSectionServiceImpl | saveUpcSectionAudit | saved audit record for UPC Section ID: {}", temp.getUpcSectionID());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> updateUpcSectionTemp(Integer upcSectionID, UpcSectionDTO upcSectionDTO) throws ApiRequestException {
        log.info("START: UpcSectionServiceImpl | updateUpcSectionTemp | Update Upc Section: {}", upcSectionDTO);
        Date date = new Date();

        UpcSectionTemp upcSectionDb = upcSectionTempRepository.findById(upcSectionID).orElseThrow(() -> {
            throw new ApiRequestException(UPC_SECTION_TEMP_WITH + upcSectionID + DOES_NOT_EXISTS);
        });

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUpcSectionTemp.upcSectionTemp.upcSectionName.eq(upcSectionDTO.getUpcSectionName()));
        List<UpcSectionTemp> upcSectionTempList = (List<UpcSectionTemp>) upcSectionTempRepository.findAll(booleanBuilder);

        if (!upcSectionDb.getUpcSectionName().equals(upcSectionDTO.getUpcSectionName())) {
            validateUpcSectionNameUniqueness(upcSectionDTO.getUpcSectionName(), upcSectionID);
        } else if (upcSectionDTO.getUpcSectionName() == null || upcSectionDTO.getUpcSectionName().trim().isEmpty()) {
            throw new ApiRequestException(EMPTY_NULL);
        }

        // Use a stream to check if any element in the list matches the provided string
        boolean existsInTempList = upcSectionTempList.stream()
                .anyMatch(temp -> temp.getUpcSectionName().equals(upcSectionDTO.getUpcSectionName()));

        if (existsInTempList &&
                !upcSectionDb.getUpcSectionName().equals(upcSectionDTO.getUpcSectionName())) {
            throw new ApiRequestException(UPC_SECTION_TEMP_WITH + upcSectionDTO.getUpcSectionName() + " Already Exists");
        }

        upcSectionDb.setStatus(upcSectionDTO.getStatus());
        upcSectionDb.setCreatedDate(date);
        upcSectionDb.setLastModifiedDate(date);
        upcSectionDb.setApproveStatus(upcSectionDTO.getApproveStatus());
        upcSectionDb.setUpcSectionDescription(upcSectionDTO.getUpcSectionDescription());
        upcSectionDb.setUpcSectionName(upcSectionDTO.getUpcSectionName());
        upcSectionDb.setApprovedDate(upcSectionDTO.getApprovedDate());
        upcSectionDb.setApprovedBy(upcSectionDTO.getApprovedBy());
        upcSectionDb.setCreatedBy(upcSectionDTO.getCreatedBy());
        upcSectionDb.setModifiedBy(upcSectionDTO.getModifiedBy());

        upcSectionTempRepository.saveAndFlush(upcSectionDb);

        log.info("END: Update Upc Section: {}", upcSectionDb);

        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionDb);

        log.info("END: UpcSectionServiceImpl | updateUpcSectionTemp | Update Upc Section response: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> updateApprovedUpcSection(Integer upcSectionID, UpcSectionDTO upcSectionDTO) throws ApiRequestException {
        log.info("START: UpcSectionServiceImpl | updateApprovedUpcSection | Update UPC Section :{}", upcSectionDTO);

        UpcSection upcSectionDb = upcSectionRepository.findById(upcSectionID).orElseThrow(() -> {
            throw new ApiRequestException(UPC_SECTION_WITH + upcSectionID + DOES_NOT_EXISTS);
        });

        if (!upcSectionDb.getUpcSectionName().equals(upcSectionDTO.getUpcSectionName())) {
            validateUpcSectionNameUniqueness(upcSectionDTO.getUpcSectionName(), upcSectionID);
        } else if (upcSectionDTO.getUpcSectionName() == null || upcSectionDTO.getUpcSectionName().trim().isEmpty()) {
            throw new ApiRequestException(EMPTY_NULL);
        }

        UpcSectionTemp upcSectionTemp = mapToUpcSectionTemp(upcSectionDb, upcSectionDTO);

        log.info("END : GET UpcSection {}", upcSectionTemp);
        upcSectionTemp = upcSectionTempRepository.saveAndFlush(upcSectionTemp);

        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionTemp);

        log.info("END: UpcSectionServiceImpl | updateApprovedUpcSection | Update UPC Section response: {}", response);
        return ResponseEntity.ok().body(response);
    }

    private void validateUpcSectionNameUniqueness(String upcSectionName, Integer upcSectionID) throws ApiRequestException {

        log.info("UpcSectionServiceImpl | validateUpcSectionNameUniqueness | Validating uniqueness of UPC Section Name: {} with ID: {}", upcSectionName, upcSectionID);
        BooleanBuilder tempBuilder = new BooleanBuilder();
        tempBuilder.and(QUpcSectionTemp.upcSectionTemp.upcSectionName.eq(upcSectionName));
        if (upcSectionID != null) {
            tempBuilder.and(QUpcSectionTemp.upcSectionTemp.upcSectionID.ne(upcSectionID));
        }
        boolean existsInTemp = upcSectionTempRepository.exists(tempBuilder);

        BooleanBuilder masterBuilder = new BooleanBuilder();
        masterBuilder.and(QUpcSection.upcSection.upcSectionName.eq(upcSectionName));
        if (upcSectionID != null) {
            masterBuilder.and(QUpcSection.upcSection.upcSectionID.ne(upcSectionID));
        }
        boolean existsInMaster = upcSectionRepository.exists(masterBuilder);

        if (existsInTemp || existsInMaster) {
            throw new ApiRequestException("UPC Section Name '" + upcSectionName + "' already exists in the system.");
        }

        log.info("UpcSectionServiceImpl | validateUpcSectionNameUniqueness | UPC Section Name: {} is unique.", upcSectionName);
    }

    private UpcSectionTemp mapToUpcSectionTemp(UpcSection upcSection, UpcSectionDTO upcSectionDTO) {
        Date date = new Date();
        UpcSectionTemp upcSectionTemp = new UpcSectionTemp();

        upcSectionTemp.setUpcSectionID(upcSection.getUpcSectionID());
        upcSectionTemp.setStatus(upcSectionDTO.getStatus());
        upcSectionTemp.setCreatedDate(upcSectionDTO.getCreatedDate());
        upcSectionTemp.setApproveStatus(upcSectionDTO.getApproveStatus());
        upcSectionTemp.setUpcSectionDescription(upcSectionDTO.getUpcSectionDescription());
        upcSectionTemp.setUpcSectionName(upcSectionDTO.getUpcSectionName());
        upcSectionTemp.setApprovedDate(upcSectionDTO.getApprovedDate());
        upcSectionTemp.setApprovedBy(upcSectionDTO.getApprovedBy());
        upcSectionTemp.setCreatedBy(upcSectionDTO.getCreatedBy());

        upcSection.setModifiedBy(upcSectionDTO.getModifiedBy());
        upcSection.setLastModifiedDate(date);

        return upcSectionTemp;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Void>> deleteUpcSectionFormTemp(Integer upcSectionID) throws ApiRequestException {
        log.info("START: UpcSectionServiceImpl | deleteUpcSectionFormTemp | Deleting UPC Section Temp with ID: {}", upcSectionID);
        upcSectionTempRepository.deleteById(upcSectionID);
        StandardResponse<Void> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionID);
        log.info("END: UpcSectionServiceImpl | deleteUpcSectionFormTemp | Deleted UPC Section Temp with ID: {}", upcSectionID);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> approvedActiveList() throws ApiRequestException {
        log.info("START: UpcSectionServiceImpl | approvedActiveList | Fetching all approved and active UPC Sections");
        List<UpcSection> activeApprovedSections = upcSectionRepository.findByStatusAndApproveStatus(Status.ACT, MasterDataApproveStatus.APPROVED);

        List<UpcSectionDTO> resultList = activeApprovedSections.stream()
                .map(upcSection -> {
                    UpcSectionDTO dto = new UpcSectionDTO();
                    dto.setUpcSectionID(upcSection.getUpcSectionID());
                    dto.setUpcSectionName(upcSection.getUpcSectionName());
                    dto.setUpcSectionDescription(upcSection.getUpcSectionDescription());
                    return dto;
                })
                .toList();
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), resultList);
        return ResponseEntity.ok().body(response);
    }
}
