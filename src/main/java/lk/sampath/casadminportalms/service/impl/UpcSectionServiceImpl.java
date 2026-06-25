package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upcsection.UpcSectionDTO;
import lk.sampath.casadminportalms.entity.upcsection.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionAudRepository;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionRepository;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionTempRepository;
import lk.sampath.casadminportalms.service.UpcSectionService;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class UpcSectionServiceImpl implements UpcSectionService {

    private static final Logger LOG = LoggerFactory.getLogger(UpcSectionServiceImpl.class);

    private static final String UPC_SECTION_WITH = "UPC Section with ";

    private static final String UPC_SECTION_TEMP_WITH = "UPC Section with TEMP ";

    private static final String DOES_NOT_EXISTS = " does not exists";

    private static final String EMPTY_NULL = "Upc Section name cannot be empty or null.";

    @Autowired
    private UpcSectionRepository upcSectionRepository;

    @Autowired
    private UpcSectionTempRepository upcSectionTempRepository;

    @Autowired
    private UpcSectionAudRepository upcSectionAudRepository;


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<UpcSectionDTO>>> findAllUpcSectionTempList(int page, int size) throws ApiRequestException {
        List<UpcSectionTemp> upcSectionList = upcSectionTempRepository.findAll(PageRequest.of(page, size)).getContent();
        StandardResponse<List<UpcSectionDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionList);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> findUpcSectionTempByID(Integer upcSectionID) throws ApiRequestException {
        UpcSectionTemp upcSectionTemp = upcSectionTempRepository.findById(upcSectionID)
                .orElseThrow(() -> new ApiRequestException(UPC_SECTION_TEMP_WITH + upcSectionID + DOES_NOT_EXISTS));
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionTemp);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<Page<UpcSectionDTO>>> findAllApprovedUpcSection(Pageable pageable) {
        Page<UpcSection> upcSectionList = upcSectionRepository.findAll(pageable);
        StandardResponse<Page<UpcSectionDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionList);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> findApprovedUpcSectionByID(Integer upcSectionID) throws ApiRequestException {
        UpcSection upcSection = upcSectionRepository.findById(upcSectionID).orElseThrow(() -> {
            throw new ApiRequestException(UPC_SECTION_WITH + upcSectionID + DOES_NOT_EXISTS);
        });
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSection);
        return ResponseEntity.ok().body(response);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> saveUpcSectionTemp(UpcSectionDTO upcSectionDTO) throws ApiRequestException {
        LOG.info("START: save Upc Section Temp :{}", upcSectionDTO);

        if (upcSectionDTO == null || upcSectionDTO.getUpcSectionName() == null || upcSectionDTO.getUpcSectionName().trim().isEmpty()) {
            throw new ApiRequestException(EMPTY_NULL);
        }
        Date date = new Date();
        UpcSectionTemp upcSectionTemp = new UpcSectionTemp();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUpcSectionTemp.upcSectionTemp.upcSectionName.eq(upcSectionDTO.getUpcSectionName()));
        List<UpcSectionTemp> upcSectionTemps = (List<UpcSectionTemp>) upcSectionTempRepository.findAll(booleanBuilder);

        validateUpcSectionNameUniqueness(upcSectionDTO.getUpcSectionName(), null);

        if (upcSectionTemps.isEmpty()) {
            upcSectionTemp.setUpcSectionID(upcSectionTempRepository.getCurrentSequenceValue());
            upcSectionTemp.setStatus(upcSectionDTO.getStatus());
            upcSectionTemp.setCreatedDate(date);
            upcSectionTemp.setLastModifiedDate(date);
            upcSectionTemp.setUpcSectionName(upcSectionDTO.getUpcSectionName());
            upcSectionTemp.setApprovedBy(upcSectionDTO.getApprovedBy());
            upcSectionTemp.setUpcSectionDescription(upcSectionDTO.getUpcSectionDescription());
            upcSectionTemp.setApprovedDate(upcSectionDTO.getApprovedDate());
            upcSectionTemp.setCreatedBy(upcSectionDTO.getCreatedBy());
            upcSectionTemp.setModifiedBy(upcSectionDTO.getModifiedBy());
            upcSectionTemp.setApproveStatus(upcSectionDTO.getApproveStatus());
            LOG.info(UPC_SECTION_WITH, upcSectionDTO);

            upcSectionTemp = upcSectionTempRepository.saveAndFlush(upcSectionTemp);
            LOG.info("SUCCESS: Saved Upc Section with ID : {}", upcSectionTemp.getUpcSectionID());

        } else {
            throw new ApiRequestException("Upc Section Temp Already Exists");
        }
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionTemp);
        return ResponseEntity.ok().body(response);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> approveRejectUpcSection(ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        if (approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null) {
            throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
        }

        UpcSectionTemp upcSectionTemp = upcSectionTempRepository.findById(approveRejectRQ.getApproveRejectDataID())
                .orElseThrow(() -> new ApiRequestException(UPC_SECTION_TEMP_WITH + approveRejectRQ.getApproveRejectDataID() + DOES_NOT_EXISTS));

        Optional<UpcSection> optionalUpcSection = upcSectionRepository.findById(upcSectionTemp.getUpcSectionID());
        UpcSection findUpcSection = optionalUpcSection.orElse(null);
        LOG.info("find Upc Section : {}", findUpcSection);

        upcSectionTemp.setApprovedDate(new Date());
        upcSectionTemp.setApproveStatus(approveRejectRQ.getApproveStatus());

        upcSectionTempRepository.saveAndFlush(upcSectionTemp);

        ResponseEntity<StandardResponse<UpcSectionDTO>> response;

        if (MasterDataApproveStatus.APPROVED.equals(approveRejectRQ.getApproveStatus())) {
            response = handleApproval(upcSectionTemp, findUpcSection);
        } else if (MasterDataApproveStatus.REJECTED.equals(approveRejectRQ.getApproveStatus())) {
            response = handleRejection(upcSectionTemp);
        } else {
            throw new ApiRequestException("Unknown approval status: " + approveRejectRQ.getApproveStatus());
        }
        return response;
    }

    private ResponseEntity<StandardResponse<UpcSectionDTO>> handleApproval(UpcSectionTemp temp, UpcSection existingUpcSection) {
        UpcSection savedUpcSection;

        if (existingUpcSection != null && existingUpcSection.getUpcSectionID().equals(temp.getUpcSectionID())) {
            savedUpcSection = updateUpcSectionToMaster(temp, existingUpcSection);
        } else {
            savedUpcSection = mapUpcSection(temp, null);
        }

        saveUpcSectionAudit(temp);
        upcSectionTempRepository.delete(temp);
        LOG.info("Handling Approval for supporting doc temp ID: {} ", savedUpcSection.getUpcSectionID());
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedUpcSection);
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<StandardResponse<UpcSectionDTO>> handleRejection(UpcSectionTemp temp) {
        LOG.info("Handling rejection for UPC Section Temp ID: {} ", temp.getUpcSectionID());

        saveUpcSectionAudit(temp);
        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), temp);
        return ResponseEntity.ok().body(response);
    }

    private UpcSection updateUpcSectionToMaster(UpcSectionTemp upcSectionTemp, UpcSection upcSection) {

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


        return upcSectionRepository.saveAndFlush(upcSection);
    }

    private UpcSection mapUpcSection(UpcSectionTemp upcSectionTemp, UpcSection existingUpcSection) {
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

        return upcSection;
    }

    private void saveUpcSectionAudit(UpcSectionTemp temp) {
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
        LOG.info("saved audit record for UPC Section ID: {}", temp.getUpcSectionID());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> updateUpcSectionTemp(Integer upcSectionID, UpcSectionDTO upcSectionDTO) throws ApiRequestException {

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

        LOG.info("END: Update Upc Section: {}", upcSectionDb);

        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionDb);

        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpcSectionDTO>> updateApprovedUpcSection(Integer upcSectionID, UpcSectionDTO upcSectionDTO) throws ApiRequestException {
        LOG.info("START: Update UPC Section :{}", upcSectionDTO);

        UpcSection upcSectionDb = upcSectionRepository.findById(upcSectionID).orElseThrow(() -> {
            throw new ApiRequestException(UPC_SECTION_WITH + upcSectionID + DOES_NOT_EXISTS);
        });

        if (!upcSectionDb.getUpcSectionName().equals(upcSectionDTO.getUpcSectionName())) {
            validateUpcSectionNameUniqueness(upcSectionDTO.getUpcSectionName(), upcSectionID);
        } else if (upcSectionDTO.getUpcSectionName() == null || upcSectionDTO.getUpcSectionName().trim().isEmpty()) {
            throw new ApiRequestException(EMPTY_NULL);
        }

        UpcSectionTemp upcSectionTemp = mapToUpcSectionTemp(upcSectionDb, upcSectionDTO);

        LOG.info("END : GET UpcSection {}", upcSectionTemp);
        upcSectionTemp = upcSectionTempRepository.saveAndFlush(upcSectionTemp);

        StandardResponse<UpcSectionDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionTemp);
        return ResponseEntity.ok().body(response);
    }

    private void validateUpcSectionNameUniqueness(String upcSectionName, Integer upcSectionID) throws ApiRequestException {
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
        upcSectionTempRepository.deleteById(upcSectionID);
        StandardResponse<Void> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcSectionID);
        return ResponseEntity.ok().body(response);
    }
}
