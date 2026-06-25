package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.entity.upmgroup.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupRepository;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupTempAudRepository;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupTempRepository;
import lk.sampath.casadminportalms.service.UpmGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UpmGroupServiceImpl implements UpmGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(UpmGroupServiceImpl.class);

    public static final String DOES_NOT_EXISTS = "Does not exists";

    public static final String NOT_NULL = "Upm group code name cannot be empty or null." ;

    @Autowired
    private UpmGroupRepository upmGroupRepository;
    @Autowired
    private UpmGroupTempRepository upmGroupTempRepository;
    @Autowired
    private UpmGroupTempAudRepository upmGroupTempAudRepository;

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupTempByID(Integer upmGroupID) throws ApiRequestException {
        UpmGroupTemp upmGroupTemp = upmGroupTempRepository.findById(upmGroupID)
                .orElseThrow(() -> new ApiRequestException("UPM Group temp with " + upmGroupID + DOES_NOT_EXISTS));
        StandardResponse<UpmGroupDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> findAllUpmGroupTempList(int page, int size) throws ApiRequestException {
        List<UpmGroupTemp> upmGroupTempList = upmGroupTempRepository.findAll(PageRequest.of(page, size)).getContent();
        StandardResponse<List<UpmGroupDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTempList);
        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupById(int upmGroupID) {
        UpmGroup upmGroup = upmGroupRepository.findById(upmGroupID).orElseThrow(() -> {
            throw new ApiRequestException("UPM group with " + upmGroupID + DOES_NOT_EXISTS);
        });

        StandardResponse<UpmGroupDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroup);
        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> searchUpmGroups(int page, int size) {
        List<UpmGroup> upmGroupList = upmGroupRepository.findAll(PageRequest.of(page, size)).getContent();

        StandardResponse<List<UpmGroupDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupList);
        return ResponseEntity.ok().body(response);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpmGroupDTO>> saveUPMGroupTemp(UpmGroupDTO upmGroupDTO) throws  ApiRequestException {

        LOG.info("START: Save UpmGroup :{}", upmGroupDTO);

        Date date = new Date();
        if (upmGroupDTO == null || upmGroupDTO.getGroupCode() == null || upmGroupDTO.getGroupCode().trim().isEmpty()) {
            throw new ApiRequestException(NOT_NULL);
        }

        UpmGroupTemp upmGroupTemp = new UpmGroupTemp();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUpmGroupTemp.upmGroupTemp.groupCode.eq(upmGroupDTO.getGroupCode()));
        List<UpmGroupTemp> upmGroupTemps = (List<UpmGroupTemp>) upmGroupTempRepository.findAll(booleanBuilder);

        validateTemplateNameUniqueness(upmGroupDTO.getGroupCode(), null);

        if(upmGroupTemps.isEmpty()){
            upmGroupTemp.setUpmGroupID(upmGroupTempRepository.getCurrentSequenceValue());
            upmGroupTemp.setStatus(upmGroupDTO.getStatus());
            upmGroupTemp.setCreatedDate(date);
            upmGroupTemp.setGroupCode(upmGroupDTO.getGroupCode());
            upmGroupTemp.setReferenceName(upmGroupDTO.getReferenceName());
            upmGroupTemp.setWorkFlowLevel(upmGroupDTO.getWorkFlowLevel());
            upmGroupTemp.setApproveStatus(upmGroupDTO.getApproveStatus());
            upmGroupTemp.setCreatedBy(upmGroupDTO.getCreatedBy());
            LOG.info("date :{}", upmGroupTemp);

            upmGroupTemp = upmGroupTempRepository.save(upmGroupTemp);
            LOG.info("SUCCESS: Saved UpmGroup with ID :{}", upmGroupTemp.getUpmGroupID());

        } else {
            throw new ApiRequestException("UPM Group Already Exists");
        }

        StandardResponse<UpmGroupDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpmGroupDTO>> updateUpmGroupTemp(Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException {

        UpmGroupTemp upmGroupTemp = upmGroupTempRepository.findById(upmGroupID).orElseThrow(() -> {
            throw new ApiRequestException("UPM Group Temp with ID " + upmGroupID + DOES_NOT_EXISTS);
        });

        Date date = new Date();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUpmGroupTemp.upmGroupTemp.groupCode.eq(upmGroupDTO.getGroupCode()));
        List<UpmGroupTemp> upmGroupTempList = (List<UpmGroupTemp>) upmGroupTempRepository.findAll(booleanBuilder);

        if(!upmGroupTemp.getGroupCode().equals(upmGroupDTO.getGroupCode())){
            validateTemplateNameUniqueness(upmGroupDTO.getGroupCode(), upmGroupID);
        } else if ( upmGroupDTO.getGroupCode() == null || upmGroupDTO.getGroupCode().trim().isEmpty()) {
            throw new ApiRequestException(NOT_NULL);
        }

        boolean existingTempList = upmGroupTempList.stream()
                        .anyMatch(temp -> temp.getGroupCode().equals(upmGroupDTO.getGroupCode()));

        if(existingTempList && !upmGroupTemp.getGroupCode().equals(upmGroupDTO.getGroupCode())){
            throw new ApiRequestException("UPM group temp with "+ upmGroupDTO.getGroupCode() + "Already Exists");
        }

        upmGroupTemp.setStatus(upmGroupDTO.getStatus());
        upmGroupTemp.setLastModifiedDate(date);
        upmGroupTemp.setModifiedBy(upmGroupDTO.getModifiedBy());
        upmGroupTemp.setGroupCode(upmGroupDTO.getGroupCode());
        upmGroupTemp.setReferenceName(upmGroupDTO.getReferenceName());
        upmGroupTemp.setWorkFlowLevel(upmGroupDTO.getWorkFlowLevel());
        upmGroupTemp.setApproveStatus(upmGroupDTO.getApproveStatus());

        upmGroupTempRepository.save(upmGroupTemp);
        LOG.info("END: Update UpmGroup :{}",upmGroupTemp);

        StandardResponse<UpmGroupDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpmGroupDTO>> approveRejectUpmGroup(ApproveRejectRQ approveRejectRQ) throws  ApiRequestException {

        if (approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null) {
            throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
        }

        UpmGroupTemp upmGroupTemp = upmGroupTempRepository.findById(approveRejectRQ.getApproveRejectDataID()).orElseThrow(() -> {
            throw new ApiRequestException("UPM group temp with" + approveRejectRQ.getApproveRejectDataID() + DOES_NOT_EXISTS);
        });
        LOG.info("upmGroupTemp :{}", upmGroupTemp);

        Optional<UpmGroup> optionalUpmGroup = upmGroupRepository.findById(upmGroupTemp.getUpmGroupID());
        UpmGroup findUpmGroup = optionalUpmGroup.orElse(null);

        upmGroupTemp.setApproveStatus(approveRejectRQ.getApproveStatus());
        upmGroupTemp.setApprovedDate(new Date());
        upmGroupTempRepository.save(upmGroupTemp);

        ResponseEntity<StandardResponse<UpmGroupDTO>> response;

        if (MasterDataApproveStatus.APPROVED.equals(approveRejectRQ.getApproveStatus())) {
            response = handleApproval(upmGroupTemp, findUpmGroup);
        } else if(MasterDataApproveStatus.REJECTED.equals(approveRejectRQ.getApproveStatus())){
            response = handleRejection(upmGroupTemp);
        } else {
            throw new ApiRequestException("Unknown approval status: " + approveRejectRQ.getApproveStatus());
        }

        return response;
    }

    private ResponseEntity<StandardResponse<UpmGroupDTO>>  handleRejection(UpmGroupTemp upmGroupTemp) {

        LOG.info("Handling rejection for UPM Group Temp ID: {}", upmGroupTemp.getUpmGroupID());
        saveUpmGroupAudit(upmGroupTemp);
        StandardResponse<UpmGroupDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<StandardResponse<UpmGroupDTO>> handleApproval(UpmGroupTemp upmGroupTemp, UpmGroup existingUpmGroup) {

        UpmGroup savedUpmGroup;

        if(existingUpmGroup != null && existingUpmGroup.getUpmGroupID().equals(upmGroupTemp.getUpmGroupID())){
            savedUpmGroup = updateUpmGroupToMaster(upmGroupTemp, existingUpmGroup);
        } else {
            savedUpmGroup = saveUpmGroupToMaster(upmGroupTemp);
        }

        saveUpmGroupAudit(upmGroupTemp);
        upmGroupTempRepository.delete(upmGroupTemp);

        StandardResponse<UpmGroupDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedUpmGroup);
        return ResponseEntity.ok().body(response);
    }

    private UpmGroup saveUpmGroupToMaster(UpmGroupTemp upmGroupTemp) {

        UpmGroup upmGroup = new UpmGroup();

        upmGroup.setUpmGroupID(upmGroupTemp.getUpmGroupID());
        upmGroup.setStatus(upmGroupTemp.getStatus());
        upmGroup.setCreatedDate(upmGroupTemp.getCreatedDate());
        upmGroup.setGroupCode(upmGroupTemp.getGroupCode());
        upmGroup.setApprovedDate(upmGroupTemp.getApprovedDate());
        upmGroup.setApprovedBy(upmGroupTemp.getApprovedBy());
        upmGroup.setApproveStatus(upmGroupTemp.getApproveStatus());
        upmGroup.setWorkFlowLevel(upmGroupTemp.getWorkFlowLevel());
        upmGroup.setCreatedBy(upmGroupTemp.getCreatedBy());
        upmGroup.setReferenceName(upmGroupTemp.getReferenceName());

        return upmGroupRepository.save(upmGroup);
    }

    private UpmGroup updateUpmGroupToMaster(UpmGroupTemp upmGroupTemp, UpmGroup existingUpmGroup) {

        UpmGroup upmGroup = (existingUpmGroup != null) ? existingUpmGroup : new UpmGroup();
        upmGroup.setUpmGroupID(upmGroupTemp.getUpmGroupID());
        upmGroup.setStatus(upmGroupTemp.getStatus());
        upmGroup.setCreatedDate(upmGroupTemp.getCreatedDate());
        upmGroup.setGroupCode(upmGroupTemp.getGroupCode());
        upmGroup.setApprovedDate(upmGroupTemp.getApprovedDate());
        upmGroup.setApprovedBy(upmGroupTemp.getApprovedBy());
        upmGroup.setApproveStatus(upmGroupTemp.getApproveStatus());
        upmGroup.setWorkFlowLevel(upmGroupTemp.getWorkFlowLevel());
        upmGroup.setCreatedBy(upmGroupTemp.getCreatedBy());
        upmGroup.setReferenceName(upmGroupTemp.getReferenceName());

        return upmGroupRepository.save(upmGroup);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UpmGroupDTO>> updateApprovedUpmGroup(Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException {

        LOG.info("START: Update Approved UPM Group :{}", upmGroupDTO);
        Date date = new Date();

        UpmGroup upmGroupDb = upmGroupRepository.findById(upmGroupID).orElseThrow( () -> {
            throw new ApiRequestException(" UPM group with " + upmGroupID + DOES_NOT_EXISTS);
        });

        LOG.info("START : GET upm group. {}",upmGroupDb);

        if(!upmGroupDb.getGroupCode().equals(upmGroupDTO.getGroupCode())){
            validateTemplateNameUniqueness(upmGroupDTO.getGroupCode(), upmGroupID);
        } else if ( upmGroupDTO.getGroupCode() == null || upmGroupDTO.getGroupCode().trim().isEmpty()) {
            throw new ApiRequestException(NOT_NULL);
        }

        UpmGroupTemp upmGroupTemp = new UpmGroupTemp();
        upmGroupTemp.setUpmGroupID(upmGroupDb.getUpmGroupID());
        upmGroupTemp.setStatus(upmGroupDTO.getStatus());
        upmGroupTemp.setGroupCode(upmGroupDTO.getGroupCode());
        upmGroupTemp.setReferenceName(upmGroupDTO.getReferenceName());
        upmGroupTemp.setWorkFlowLevel(upmGroupDTO.getWorkFlowLevel());
        upmGroupTemp.setLastModifiedDate(upmGroupDTO.getLastModifiedDate());
        upmGroupTemp.setModifiedBy(upmGroupDTO.getModifiedBy());
        upmGroupTemp.setApprovedDate(date);
        upmGroupTemp.setApprovedBy(upmGroupDTO.getApprovedBy());
        upmGroupTemp.setApproveStatus(upmGroupDTO.getApproveStatus());
        upmGroupTemp.setCreatedDate(upmGroupDTO.getCreatedDate());
        upmGroupTemp.setCreatedBy(upmGroupDTO.getCreatedBy());

        LOG.info("END : GET upm group {}",upmGroupTemp);
        upmGroupTempRepository.save(upmGroupTemp);

        StandardResponse<UpmGroupDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupTemp);
        return ResponseEntity.ok().body(response);
    }

    private void validateTemplateNameUniqueness(String groupCode, Integer upmGroupID) throws ApiRequestException {

        BooleanBuilder tempBuilder = new BooleanBuilder();
        tempBuilder.and(QUpmGroupTemp.upmGroupTemp.groupCode.eq(groupCode));
        if(upmGroupID != null) {
            tempBuilder.and(QUpmGroupTemp.upmGroupTemp.upmGroupID.ne(upmGroupID));
        }
        boolean existsInTemp = upmGroupTempRepository.exists(tempBuilder);

        BooleanBuilder masterBuilder = new BooleanBuilder();
        masterBuilder.and(QUpmGroup.upmGroup.groupCode.eq(groupCode));
        if(upmGroupID != null) {
            masterBuilder.and(QUpmGroup.upmGroup.upmGroupID.ne(upmGroupID));
        }
        boolean exitsInMaster = upmGroupRepository.exists(masterBuilder);

        if(existsInTemp || exitsInMaster) {
            throw new ApiRequestException("Group Code '" + groupCode + "' already exists in the system.");
        }


    }

    private void saveUpmGroupAudit(UpmGroupTemp upmGroupTemp){

        UpmGroupTempAud audit = new UpmGroupTempAud();
        audit.setUpmGroupID(upmGroupTemp.getUpmGroupID());
        audit.setStatus(upmGroupTemp.getStatus());
        audit.setCreatedDate(upmGroupTemp.getCreatedDate());
        audit.setGroupCode(upmGroupTemp.getGroupCode());
        audit.setApprovedDate(upmGroupTemp.getApprovedDate());
        audit.setApprovedBy(upmGroupTemp.getApprovedBy());
        audit.setApproveStatus(upmGroupTemp.getApproveStatus());
        audit.setWorkFlowLevel(upmGroupTemp.getWorkFlowLevel());
        audit.setCreatedBy(upmGroupTemp.getCreatedBy());
        audit.setReferenceName(upmGroupTemp.getReferenceName());

        upmGroupTempAudRepository.save(audit);
        LOG.info("Saved audit record for UPM Group ID: {}", upmGroupTemp.getUpmGroupID());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Void>> deleteUpmGroup(Integer upmGroupID) throws ApiRequestException {
        upmGroupTempRepository.deleteById(upmGroupID);
        StandardResponse<Void> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), upmGroupID);
        return ResponseEntity.ok().body(response);
    }
}
