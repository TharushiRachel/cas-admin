package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDTO;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDataDTO;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateResponse;
import lk.sampath.casadminportalms.dto.userSession.UserContext;
import lk.sampath.casadminportalms.entity.upcsection.UpcSection;
import lk.sampath.casadminportalms.entity.upctemplate.*;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionRepository;
import lk.sampath.casadminportalms.repository.upctemplate.*;
import lk.sampath.casadminportalms.service.UpcTemplateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Log4j2
public class UpcTemplateServiceImpl implements UpcTemplateService {
    
    private static final String DOES_NOT_EXIST = "does not exist";

    private static final String UPC_TEMPLATE_WITH = "UPC Template with";


    private static final String UPC_TEMPLATE_WITH_ID = "UPC Template with ID ";

    private final UpcTemplateTempRepository upcTemplateTempRepository;

    private final UpcTemplateDataTempRepository upcTemplateDataTempRepository;

    private final UpcSectionRepository upcSectionRepository;

    private final UpcTemplateRepository upcTemplateRepository;

    private final UpcTemplateDataRepository upcTemplateDataRepository;

    private final UpcTemplateAudRepository upcTemplateAudRepository;

    private final UpcTemplateDataAudRepository upcTemplateDataAudRepository;

    public UpcTemplateServiceImpl(UpcTemplateTempRepository upcTemplateTempRepository, UpcTemplateDataTempRepository upcTemplateDataTempRepository, UpcSectionRepository upcSectionRepository, UpcTemplateRepository upcTemplateRepository, UpcTemplateDataRepository upcTemplateDataRepository, UpcTemplateAudRepository upcTemplateAudRepository, UpcTemplateDataAudRepository upcTemplateDataAudRepository) {
        this.upcTemplateTempRepository = upcTemplateTempRepository;
        this.upcTemplateDataTempRepository = upcTemplateDataTempRepository;
        this.upcSectionRepository = upcSectionRepository;
        this.upcTemplateRepository = upcTemplateRepository;
        this.upcTemplateDataRepository = upcTemplateDataRepository;
        this.upcTemplateAudRepository = upcTemplateAudRepository;
        this.upcTemplateDataAudRepository = upcTemplateDataAudRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<List<UpcTemplateResponse>>> findAllUpcTemplateTempList(Pageable pageable) throws ApiRequestException {

        log.info("START: Find All Upc Template Temp List ");
        Page<UpcTemplateResponse> upcTemplateTempList = upcTemplateTempRepository.findAllTemplates(pageable);
        log.info(" Fetched All Upc Template Temp List : {} ",upcTemplateTempList);
        StandardResponse<List<UpcTemplateResponse>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateTempList);
        log.info("END: Find All Upc Template Temp List :{}", response.getResponse());
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> findUpcTemplateTempById(Integer upcTemplateID) throws ApiRequestException {
        log.info("START: Find Upc Template Temp By ID with UPC Template ID: {}", upcTemplateID);
        UpcTemplateTemp upcTemplateTemp = upcTemplateTempRepository.findById(upcTemplateID).orElseThrow(() ->
                new ApiRequestException(UPC_TEMPLATE_WITH + upcTemplateID + DOES_NOT_EXIST)
        );
        log.info("Fetched Upc Template Temp: {}", upcTemplateTemp);
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO(upcTemplateTemp);

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateDTO);
        log.info("END: Find Upc Template Temp By ID: {} with Response: {}", upcTemplateID, response.getResponse());
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<List<UpcTemplateResponse>>> findAllApprovedUpcTemplates(Pageable pageable) throws ApiRequestException {
        log.info("START: Find All Approved Upc Templates");
        Page<UpcTemplateResponse> upcTemplateList = upcTemplateRepository.findAllTemplates(pageable);
        log.info("Converted Upc Templates to DTOs: {}", upcTemplateList);
        StandardResponse<List<UpcTemplateResponse>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateList);

        log.info("END: Find All Approved Upc Templates with Response: {}", response.getResponse());
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> findApprovedUpcTemplateById(Integer upcTemplateID) throws ApiRequestException {
        log.info("START: Find Approved Upc Template By ID with UPC Template ID: {}", upcTemplateID);
        UpcTemplate upcTemplate = upcTemplateRepository.findById(upcTemplateID).orElseThrow(() ->
                new ApiRequestException(UPC_TEMPLATE_WITH + upcTemplateID + DOES_NOT_EXIST)
        );
        log.info("Fetched Approved Upc Template: {}", upcTemplate);
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO(upcTemplate);

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateDTO);
        log.info("END: Find Approved Upc Template By ID with UPC Template ID: {}. Response: {}", upcTemplateID, response.getResponse());
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> saveUpcTemplate(UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
        log.info("START: Save Upm Template :{}", upcTemplateDTO);

        Date date = new Date();
        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUpcTemplateTemp.upcTemplateTemp.templateName.eq(upcTemplateDTO.getTemplateName()));
        List<UpcTemplateTemp> upcTemplateTempList = (List<UpcTemplateTemp>) upcTemplateTempRepository.findAll(booleanBuilder);
        log.info("Checked for Existing Template. Found {} existing templates with name: {}", upcTemplateTempList.size(), upcTemplateDTO.getTemplateName());

        validateTemplateNameUniqueness(upcTemplateDTO.getTemplateName(), null);


        if(upcTemplateTempList.isEmpty()) {
            upcTemplateTemp.setUpcTemplateID(upcTemplateTempRepository.getCurrentSequenceValue());
            upcTemplateTemp.setTemplateName(upcTemplateDTO.getTemplateName());
            upcTemplateTemp.setUpcLabel(upcTemplateDTO.getUpcLabel());
            upcTemplateTemp.setDescription(upcTemplateDTO.getDescription());
            upcTemplateTemp.setUpcLabelFontColor(upcTemplateDTO.getUpcLabelFontColor());
            upcTemplateTemp.setUpcLabelBackgroundColor(upcTemplateDTO.getUpcLabelBackgroundColor());
            upcTemplateTemp.setStatus(upcTemplateDTO.getStatus());
            upcTemplateTemp.setCreatedDate(date);
            upcTemplateTemp.setApproveStatus(upcTemplateDTO.getApproveStatus());

            if (upcTemplateDTO.getUpcTemplateDataDTOList() != null) {
                List<UpcTemplateDataTemp> upcTemplateDataTempList = saveOrUpdateUpcTemplateData(upcTemplateDTO, upcTemplateTemp);
                upcTemplateTemp.setUpcTemplateDataTempList(upcTemplateDataTempList);
                log.info("Saved or Updated Upc Template Data: {}", upcTemplateDataTempList);
            }

            upcTemplateTemp = upcTemplateTempRepository.saveAndFlush(upcTemplateTemp);
            log.info("Successfully saved Upc Template with ID: {}", upcTemplateTemp.getUpcTemplateID());
        } else {
            log.warn("Upc Template with name '{}' already exists", upcTemplateDTO.getTemplateName());
            throw new ApiRequestException("Upc Section Template Already Exists");
        }

        UpcTemplateDTO upcTemplate = new UpcTemplateDTO(upcTemplateTemp);
        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplate);
        log.info("END: Save Upc Template successful. Response: {}", response.getResponse());
        return ResponseEntity.ok().body(response);
    }

    public List<UpcTemplateDataTemp> saveOrUpdateUpcTemplateData(UpcTemplateDTO upcTemplateDTO, UpcTemplateTemp upcTemplateTemp) {
        log.info("START: save Or Update Upc Template Data :{}", upcTemplateDTO);
        List<UpcTemplateDataTemp> newDataTemps = new ArrayList<>();

        if (upcTemplateDTO.getIsModified().equals(AppsConstants.YesNo.Y)) {
            List<UpcTemplateDataTemp> existingDataTemps = upcTemplateDataTempRepository.findByUpcTemplateTempUpcTemplateID(upcTemplateTemp.getUpcTemplateID());
            log.info("Existing UpcTemplateDataTemps to be deleted: {}", existingDataTemps);

            if (!existingDataTemps.isEmpty()) {
                upcTemplateDataTempRepository.deleteAll(existingDataTemps);
                log.info("Deleted obsolete UpcTemplateDataTemps.");
            }

            for (UpcTemplateDataDTO upcTemplateDataDTO : upcTemplateDTO.getUpcTemplateDataDTOList()) {
                UpcTemplateDataTemp upcTemplateDataTemp = createUpcTemplateDataTemp(upcTemplateTemp, upcTemplateDataDTO);
                newDataTemps.add(upcTemplateDataTemp);
            }

            log.info("Saving new UpcTemplateDataTemps: {}", newDataTemps);
        } else {

            for (UpcTemplateDataDTO upcTemplateDataDTO : upcTemplateDTO.getUpcTemplateDataDTOList()) {
                UpcTemplateDataTemp upcTemplateDataTemp = createUpcTemplateDataTemp(upcTemplateTemp, upcTemplateDataDTO);
                newDataTemps.add(upcTemplateDataTemp);
            }

            log.info("Saving UpcTemplateDataTemps without deletion: {}", newDataTemps);
        }

        return newDataTemps;
    }

    private UpcTemplateDataTemp createUpcTemplateDataTemp(UpcTemplateTemp upcTemplateTemp, UpcTemplateDataDTO upcTemplateDataDTO) {
        UpcSection upcSection = upcSectionRepository.findById(upcTemplateDataDTO.getUpcSectionID())
                .orElseThrow(() -> new ApiRequestException("Upc section with ID "
                        + upcTemplateDataDTO.getUpcSectionID() + " does not exist."));

        UpcTemplateDataTemp upcTemplateDataTemp = new UpcTemplateDataTemp();
        upcTemplateDataTemp.setUpcTemplateDataID(upcTemplateDataTempRepository.getCurrentSequenceValue());
        upcTemplateDataTemp.setUpcTemplateTemp(upcTemplateTemp);
        upcTemplateDataTemp.setUpcSection(upcSection);
        upcTemplateDataTemp.setDisplayOrder(upcTemplateDataDTO.getDisplayOrder());
        upcTemplateDataTemp.setParentSectionID(upcTemplateDataDTO.getParentSectionID());
        upcTemplateDataTemp.setSectionLevel(upcTemplateDataDTO.getSectionLevel());

        return upcTemplateDataTemp;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> updateUpcTemplateTemp(Integer upcTemplateID, UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
        log.info("START: Update UpcTemplateTemp for ID: {}", upcTemplateID);
        Date date = new Date();

        UpcTemplateTemp upcTemplateTemp = upcTemplateTempRepository.findById(upcTemplateID).orElseThrow(() ->
                new ApiRequestException(UPC_TEMPLATE_WITH + upcTemplateID + DOES_NOT_EXIST)
        );
        log.info("Fetched UpcTemplateTemp to be updated: {}", upcTemplateTemp);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUpcTemplateTemp.upcTemplateTemp.templateName.eq(upcTemplateDTO.getTemplateName()));


        if(upcTemplateDTO.getTemplateName() == null || upcTemplateDTO.getTemplateName().trim().isEmpty()){
            log.info(" Upc Template name is null or empty. Template ID: {}", upcTemplateID);
            throw new ApiRequestException("Upc Template cannot be empty or null");
        }
        log.info("Validating uniqueness for template name: {}", upcTemplateDTO.getTemplateName());
        validateTemplateNameUniqueness(upcTemplateDTO.getTemplateName(), upcTemplateID);

        upcTemplateTemp.setTemplateName(upcTemplateDTO.getTemplateName());
        upcTemplateTemp.setUpcLabel(upcTemplateDTO.getUpcLabel());
        upcTemplateTemp.setDescription(upcTemplateDTO.getDescription());
        upcTemplateTemp.setUpcLabelFontColor(upcTemplateDTO.getUpcLabelFontColor());
        upcTemplateTemp.setUpcLabelBackgroundColor(upcTemplateDTO.getUpcLabelBackgroundColor());
        upcTemplateTemp.setStatus(upcTemplateDTO.getStatus());
        upcTemplateTemp.setLastModifiedDate(date);
        upcTemplateTemp.setApproveStatus(upcTemplateDTO.getApproveStatus());
        log.info("Updated fields for UpcTemplateTemp: {}", upcTemplateTemp);
        if (upcTemplateDTO.getUpcTemplateDataDTOList() != null && upcTemplateDTO.getIsModified().equals(AppsConstants.YesNo.Y)) {
            List<UpcTemplateDataTemp> upcTemplateDataTempList = saveOrUpdateUpcTemplateData(upcTemplateDTO, upcTemplateTemp);
            upcTemplateTemp.setUpcTemplateDataTempList(upcTemplateDataTempList);
        }

        upcTemplateTemp = upcTemplateTempRepository.saveAndFlush(upcTemplateTemp);

        UpcTemplateDTO upcTemplate = new UpcTemplateDTO(upcTemplateTemp);

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplate);
        log.info("END: Response body after updating UpcTemplateTemp: {}", response.getResponse());

        return ResponseEntity.ok().body(response);

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> approveRejectUpcTemplate(ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        log.info("START: Approve/Reject UPC Template: {}", approveRejectRQ);

        if (approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null) {
            log.info("ERROR: Invalid ApproveRejectRQ: DataID cannot be null");
            throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
        }

        UpcTemplateTemp upcTemplateTemp = upcTemplateTempRepository.findById(approveRejectRQ.getApproveRejectDataID())
                .orElseThrow(() -> new ApiRequestException(
                        UPC_TEMPLATE_WITH_ID + approveRejectRQ.getApproveRejectDataID() + DOES_NOT_EXIST));
        log.info("Fetched UpcTemplateTemp for approval/rejection: {}", upcTemplateTemp);

        Optional<UpcTemplate> optionalUpcTemplate = upcTemplateRepository.findById(upcTemplateTemp.getUpcTemplateID());
        UpcTemplate findUpcTemplate = optionalUpcTemplate.orElse(null);

        upcTemplateTemp.setApprovedDate(new Date());
        upcTemplateTemp.setApproveStatus(approveRejectRQ.getApproveStatus());
        upcTemplateTemp.setApprovedBy(UserContext.getUsername());
        UpcTemplateTemp savedTemp = upcTemplateTempRepository.saveAndFlush(upcTemplateTemp);
        log.info("Saved updated UpcTemplateTemp: {}", savedTemp);
        ResponseEntity<StandardResponse<Object>> response;

        if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.APPROVED)) {
            response = handleApproval(upcTemplateTemp, findUpcTemplate);
        } else if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.REJECTED)) {
            response = handleRejection(savedTemp);
        } else {
            throw new ApiRequestException("Unknown approval status: " + approveRejectRQ.getApproveStatus());
        }

        log.info("END: Approve/Reject UPC Template Response: {}", response.getBody());
        return response;
    }

    private ResponseEntity<StandardResponse<Object>> handleApproval(UpcTemplateTemp temp, UpcTemplate existingTemplate) {
        UpcTemplate savedTemplate;

        if (existingTemplate != null && existingTemplate.getUpcTemplateID().equals(temp.getUpcTemplateID())) {
            savedTemplate = updateUpcTemplateToMaster(temp, existingTemplate);
        } else {
            savedTemplate = mapUpcTemplate(temp, null);
        }

        saveUpcTemplateAudit(temp);
        upcTemplateTempRepository.delete(temp);

        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO(savedTemplate);

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<StandardResponse<Object>> handleRejection(UpcTemplateTemp temp) {
        log.info("Handling rejection for UPC Template Temp ID: {}", temp.getUpcTemplateID());

        saveUpcTemplateAudit(temp);
        if (temp.getUpcTemplateDataTempList() != null) {
            saveUpcTemplateDataAudit(temp.getUpcTemplateDataTempList());
        }

        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO(temp);

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    private UpcTemplate mapUpcTemplate(UpcTemplateTemp upcTemplateTemp, UpcTemplate existingUpcTemplate) {

        UpcTemplate upcTemplate = (existingUpcTemplate != null) ? existingUpcTemplate : new UpcTemplate();

        upcTemplate.setUpcTemplateID(upcTemplateTemp.getUpcTemplateID());
        upcTemplate.setTemplateName(upcTemplateTemp.getTemplateName());
        upcTemplate.setUpcLabel(upcTemplateTemp.getUpcLabel());
        upcTemplate.setDescription(upcTemplateTemp.getDescription());
        upcTemplate.setUpcLabelFontColor(upcTemplateTemp.getUpcLabelFontColor());
        upcTemplate.setUpcLabelBackgroundColor(upcTemplateTemp.getUpcLabelBackgroundColor());
        upcTemplate.setStatus(upcTemplateTemp.getStatus());
        upcTemplate.setModifiedBy(upcTemplateTemp.getModifiedBy());
        upcTemplate.setLastModifiedDate(upcTemplateTemp.getLastModifiedDate());
        upcTemplate.setCreatedDate(upcTemplateTemp.getCreatedDate());
        upcTemplate.setCreatedBy(upcTemplateTemp.getCreatedBy());
        upcTemplate.setApprovedBy(upcTemplateTemp.getApprovedBy());
        upcTemplate.setApprovedDate(upcTemplateTemp.getApprovedDate());
        upcTemplate.setApproveStatus(upcTemplateTemp.getApproveStatus());

        upcTemplateRepository.saveAndFlush(upcTemplate);

        if (upcTemplateTemp.getUpcTemplateDataTempList() != null) {
            List<UpcTemplateData> upcTemplateDataList = saveOrUpdateMasterTemplateData(upcTemplateTemp, upcTemplate);
            upcTemplate.setUpcTemplateDataList(upcTemplateDataList);
        }

        return upcTemplate;
    }

    public UpcTemplate updateUpcTemplateToMaster(UpcTemplateTemp upcTemplateTemp, UpcTemplate upcTemplate) {

        upcTemplate.setTemplateName(upcTemplateTemp.getTemplateName());
        upcTemplate.setUpcLabel(upcTemplateTemp.getUpcLabel());
        upcTemplate.setDescription(upcTemplateTemp.getDescription());
        upcTemplate.setUpcLabelFontColor(upcTemplateTemp.getUpcLabelFontColor());
        upcTemplate.setUpcLabelBackgroundColor(upcTemplateTemp.getUpcLabelBackgroundColor());
        upcTemplate.setStatus(upcTemplateTemp.getStatus());
        upcTemplate.setModifiedBy(upcTemplateTemp.getModifiedBy());
        upcTemplate.setLastModifiedDate(new Date());
        upcTemplate.setApprovedBy(upcTemplateTemp.getApprovedBy());
        upcTemplate.setApprovedDate(upcTemplateTemp.getApprovedDate());
        upcTemplate.setApproveStatus(upcTemplateTemp.getApproveStatus());

        UpcTemplate updatedUpcTemplate = upcTemplateRepository.saveAndFlush(upcTemplate);

        if (upcTemplateTemp.getUpcTemplateDataTempList() != null) {
            List<UpcTemplateData> updatedTemplateDataList = saveOrUpdateMasterTemplateData(upcTemplateTemp, updatedUpcTemplate);
            updatedUpcTemplate.setUpcTemplateDataList(updatedTemplateDataList);
        }

        return updatedUpcTemplate;
    }

    private void saveUpcTemplateAudit(UpcTemplateTemp temp) {
        if (temp == null) return;

        UpcTemplateAud audit = new UpcTemplateAud();
        audit.setUpcTemplateID(temp.getUpcTemplateID());
        audit.setTemplateName(temp.getTemplateName());
        audit.setUpcLabel(temp.getUpcLabel());
        audit.setDescription(temp.getDescription());
        audit.setUpcLabelFontColor(temp.getUpcLabelFontColor());
        audit.setUpcLabelBackgroundColor(temp.getUpcLabelBackgroundColor());
        audit.setStatus(temp.getStatus());
        audit.setApproveStatus(temp.getApproveStatus());
        audit.setApprovedDate(temp.getApprovedDate());
        audit.setCreatedBy(temp.getCreatedBy());
        audit.setCreatedDate(temp.getCreatedDate());
        audit.setModifiedBy(temp.getModifiedBy());
        audit.setLastModifiedDate(temp.getLastModifiedDate());

        upcTemplateAudRepository.save(audit);
        log.info("Saved audit record for UPC Template ID: {}", temp.getUpcTemplateID());
    }

    private void saveUpcTemplateDataAudit(List<UpcTemplateDataTemp> dataTempList) {
        if (dataTempList == null || dataTempList.isEmpty()) {
            log.warn("No data records found for audit.");
            return;
        }

        List<UpcTemplateDataAud> audits = dataTempList.stream().map(temp -> {
            UpcTemplateDataAud audit = new UpcTemplateDataAud();
            audit.setUpcTemplateDataID(temp.getUpcTemplateDataID());
            audit.setUpcTemplateID(temp.getUpcTemplateTemp().getUpcTemplateID());
            audit.setUpcSectionID(temp.getUpcSection().getUpcSectionID());
            audit.setParentSectionID(temp.getParentSectionID());
            audit.setSectionLevel(temp.getSectionLevel());
            audit.setDisplayOrder(temp.getDisplayOrder());
            return audit;
        }).toList();

        upcTemplateDataAudRepository.saveAll(audits);
        log.info("Saved {} data audit records.", audits.size());
    }

    public List<UpcTemplateData> saveOrUpdateMasterTemplateData(UpcTemplateTemp temp, UpcTemplate master) {

        if (temp == null || master == null || temp.getUpcTemplateDataTempList() == null) {
            log.info("END: Input is invalid. Temp: {}, Master: {}, Template Data Temp List is null.", temp, master);
            return Collections.emptyList();
        }

        List<UpcTemplateData> upcTemplateDataList = upcTemplateDataRepository.findByUpcTemplateUpcTemplateID(master.getUpcTemplateID());
        if(!upcTemplateDataList.isEmpty()){
            upcTemplateDataRepository.deleteAll(upcTemplateDataList);
        }

        List<UpcTemplateData> masterData = temp.getUpcTemplateDataTempList().stream().map(tempData -> {
            UpcTemplateData data = new UpcTemplateData();
            data.setUpcTemplateDataID(tempData.getUpcTemplateDataID());
            data.setUpcTemplate(master);
            data.setUpcSection(tempData.getUpcSection());
            data.setDisplayOrder(tempData.getDisplayOrder());
            data.setParentSectionID(tempData.getParentSectionID());
            data.setSectionLevel(tempData.getSectionLevel());
            return data;
        }).toList();

        List<UpcTemplateData> savedData = upcTemplateDataRepository.saveAll(masterData);
        log.info("Saved master template data for template ID: {}", master.getUpcTemplateID());

        saveUpcTemplateDataAudit(temp.getUpcTemplateDataTempList());
        upcTemplateDataTempRepository.deleteAll(temp.getUpcTemplateDataTempList());
        log.info("END: Save/Update Master Template Data for Master Template ID: {}", master.getUpcTemplateID());

        return savedData;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> updateApprovedUpcTemplate(Integer upcTemplateID, UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {

        log.info("START: Update UPC Template :{}", upcTemplateDTO);

        UpcTemplate upcTemplate = upcTemplateRepository.findById(upcTemplateID).orElseThrow(() ->
                new ApiRequestException(UPC_TEMPLATE_WITH_ID + upcTemplateID + DOES_NOT_EXIST)
        );
        log.info("Fetched existing UPC Template: {}", upcTemplate);
        if(!upcTemplate.getTemplateName().equals(upcTemplateDTO.getTemplateName())){
            validateTemplateNameUniqueness(upcTemplateDTO.getTemplateName(), upcTemplateID);
        } else if (upcTemplateDTO.getTemplateName() == null || upcTemplateDTO.getTemplateName().trim().isEmpty()) {
            log.info("Template name is null or empty. Throwing ApiRequestException.");
            throw new ApiRequestException("Upc Template cannot be empty or null");

        }

        UpcTemplateTemp upcTemplateTemp = mapToUpcTemplateTemp(upcTemplate, upcTemplateDTO);
        upcTemplateTemp = upcTemplateTempRepository.saveAndFlush(upcTemplateTemp);
        log.info("Saved updated UpcTemplateTemp: {}", upcTemplateTemp);
        if (upcTemplateDTO.getUpcTemplateDataDTOList() != null) {
            List<UpcTemplateDataTemp> upcTemplateDataTempList = updateUpcTemplateTempFromMaster(upcTemplateDTO, upcTemplateTemp);
            upcTemplateTemp.setUpcTemplateDataTempList(upcTemplateDataTempList);
        }

        UpcTemplateDTO upcTemplateUpdate = new UpcTemplateDTO(upcTemplateTemp);

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateUpdate);
        log.info("END: Update Approved UPC Template response :{}.",response.getResponse() );
        return ResponseEntity.ok().body(response);
    }

    private void validateTemplateNameUniqueness(String templateName, Integer upcTemplateID) throws ApiRequestException {

        BooleanBuilder tempBuilder = new BooleanBuilder();
        tempBuilder.and(QUpcTemplateTemp.upcTemplateTemp.templateName.eq(templateName));
        if(upcTemplateID != null) {
            tempBuilder.and(QUpcTemplateTemp.upcTemplateTemp.upcTemplateID.ne(upcTemplateID));
        }
        boolean existsInTemp = upcTemplateTempRepository.exists(tempBuilder);

        BooleanBuilder masterBuilder = new BooleanBuilder();
        masterBuilder.and(QUpcTemplate.upcTemplate.templateName.eq(templateName));

        if(upcTemplateID != null){
            masterBuilder.and(QUpcTemplate.upcTemplate.upcTemplateID.ne(upcTemplateID));
        }

        boolean existsInMaster = upcTemplateRepository.exists(masterBuilder);

        if (existsInTemp || existsInMaster) {
            throw new ApiRequestException("User name '" + templateName + "' already exists in the system.");
        }
    }

    private UpcTemplateTemp mapToUpcTemplateTemp(UpcTemplate upcTemplate, UpcTemplateDTO upcTemplateDTO) {
        Date date = new Date();
        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();

        upcTemplateTemp.setUpcTemplateID(upcTemplate.getUpcTemplateID());
        upcTemplateTemp.setTemplateName(upcTemplateDTO.getTemplateName());
        upcTemplateTemp.setUpcLabel(upcTemplateDTO.getUpcLabel());
        upcTemplateTemp.setDescription(upcTemplateDTO.getDescription());
        upcTemplateTemp.setUpcLabelFontColor(upcTemplateDTO.getUpcLabelFontColor());
        upcTemplateTemp.setUpcLabelBackgroundColor(upcTemplateDTO.getUpcLabelBackgroundColor());
        upcTemplateTemp.setStatus(upcTemplateDTO.getStatus());
        upcTemplateTemp.setCreatedBy(upcTemplate.getCreatedBy());
        upcTemplateTemp.setCreatedDate(upcTemplate.getCreatedDate());
        upcTemplateTemp.setApproveStatus(upcTemplateDTO.getApproveStatus());

        upcTemplate.setModifiedBy(upcTemplateDTO.getModifiedBy());
        upcTemplate.setLastModifiedDate(date);

        return upcTemplateTemp;
    }

    private List<UpcTemplateDataTemp> updateUpcTemplateTempFromMaster(UpcTemplateDTO upcTemplateDTO, UpcTemplateTemp upcTemplateTemp) {
        List<UpcTemplateDataTemp> upcTemplateDataTempList = new ArrayList<>();

        for (UpcTemplateDataDTO dataDTO : upcTemplateDTO.getUpcTemplateDataDTOList()) {
            UpcSection upcSection = upcSectionRepository.findById(dataDTO.getUpcSectionID()).orElseThrow(() ->
                    new ApiRequestException("UPC Section with ID " + dataDTO.getUpcSectionID() + DOES_NOT_EXIST)
            );

            UpcTemplateDataTemp dataTemp = new UpcTemplateDataTemp();
            dataTemp.setUpcTemplateDataID(upcTemplateDataTempRepository.getCurrentSequenceValue());
            dataTemp.setUpcTemplateTemp(upcTemplateTemp);
            dataTemp.setUpcSection(upcSection);
            dataTemp.setDisplayOrder(dataDTO.getDisplayOrder());
            dataTemp.setParentSectionID(dataDTO.getParentSectionID());
            dataTemp.setSectionLevel(dataDTO.getSectionLevel());

            upcTemplateDataTempList.add(dataTemp);
        }

        upcTemplateDataTempList = upcTemplateDataTempRepository.saveAll(upcTemplateDataTempList);
        log.info("Saved {} UPC Template Data Temps", upcTemplateDataTempList.size());

        return upcTemplateDataTempList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Void>> deleteUpcTemplateFromTemp(Integer upcTemplateID) throws ApiRequestException {
        log.info("START: Delete UPC Template from Temp with ID: {}", upcTemplateID);
        if (!upcTemplateTempRepository.existsById(upcTemplateID)) {
            log.info("END : UPC Template with ID: {} does not exist. Throwing ApiRequestException.", upcTemplateID);

            throw new ApiRequestException(UPC_TEMPLATE_WITH_ID + upcTemplateID + " does not exist");
        }

        upcTemplateDataTempRepository.deleteByUpcTemplateTempUpcTemplateID(upcTemplateID);
        upcTemplateTempRepository.deleteById(upcTemplateID);

        StandardResponse<Void> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upcTemplateID);
        log.info("END: Successfully deleted UPC Template from Temp with ID: {}", upcTemplateID);
        return ResponseEntity.ok().body(response);
    }
}
