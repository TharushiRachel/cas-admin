package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.userda.UserDaDTO;
import lk.sampath.casadminportalms.entity.userda.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.userda.UserDaAudRepository;
import lk.sampath.casadminportalms.repository.userda.UserDaRepository;
import lk.sampath.casadminportalms.repository.userda.UserDaTempRepository;
import lk.sampath.casadminportalms.service.UserDaService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Log4j2
public class UserDaServiceImpl implements UserDaService {
    
    private static final String USER_DA_WITH = "User Da with ";

    private static final String USER_DA_TEMP_WITH = "User Da with TEMP ";

    private static final String DOES_NOT_EXISTS = " does not exists";

    @Autowired
    private UserDaRepository userDaRepository;

    @Autowired
    private UserDaTempRepository userDaTempRepository;

    @Autowired
    private UserDaAudRepository userDaAudRepository;

    @Override
    public ResponseEntity<StandardResponse<List<UserDaDTO>>> findAllUserDaTempList(Pageable pageable) throws ApiRequestException {
        Page<UserDaTemp> userDaTempList = userDaTempRepository.findAll(pageable);
        StandardResponse<List<UserDaDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDaTempList);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<StandardResponse<UserDaDTO>> findUserDaTempByID(Integer userDaID) throws ApiRequestException {
        UserDaTemp userDaTemp = userDaTempRepository.findById(userDaID)
                .orElseThrow(() -> new ApiRequestException(USER_DA_TEMP_WITH + userDaID + DOES_NOT_EXISTS));
        StandardResponse<UserDaDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDaTemp);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<UserDaDTO>>> findAllApprovedUserDa(Pageable pageable) {
        Page<UserDa> userDaList = userDaRepository.findAll(pageable);
        StandardResponse<List<UserDaDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDaList);
        return  ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<UserDaDTO>> findApprovedUserDaById(Integer userDaID) throws ApiRequestException {
        UserDa userDa = userDaRepository.findById(userDaID).orElseThrow(() -> {
            throw new ApiRequestException(USER_DA_WITH + userDaID + DOES_NOT_EXISTS);
        });
        StandardResponse<UserDaDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDa);
        return ResponseEntity.ok().body(response);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UserDaDTO>> saveUserDaTemp(UserDaDTO userDaDTO) throws ApiRequestException {

        log.info("START: save UserDA :{}", userDaDTO);

        if (userDaDTO == null || userDaDTO.getUserName() == null || userDaDTO.getUserName().trim().isEmpty()) {
            throw new ApiRequestException("User cannot be empty or null.");
        }

        if (userDaDTO.getCleanAmount() != null && userDaDTO.getCleanAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiRequestException("Clean amount should be positive!");
        }

        Date date = new Date();

        UserDaTemp userDaTempSave = new UserDaTemp();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUserDaTemp.userDaTemp.userName.eq(userDaDTO.getUserName()));
        List<UserDaTemp> userDaTemps = (List<UserDaTemp>) userDaTempRepository.findAll(booleanBuilder);

        validateUserDaNameUniqueness(userDaDTO.getUserName(), null);

        if(userDaTemps.isEmpty()){
            userDaTempSave.setUserDaID(userDaTempRepository.getCurrentSequenceValue());
            userDaTempSave.setStatus(userDaDTO.getStatus());
            userDaTempSave.setCreatedDate(date);
            userDaTempSave.setLastModifiedDate(date);
            userDaTempSave.setApproveStatus(userDaDTO.getApproveStatus());
            userDaTempSave.setUserName(userDaDTO.getUserName());
            userDaTempSave.setMaxAmount(userDaDTO.getMaxAmount());
            userDaTempSave.setDescription(userDaDTO.getDescription());
            userDaTempSave.setApprovedDate(userDaDTO.getApprovedDate());
            userDaTempSave.setApprovedBy(userDaDTO.getApprovedBy());
            userDaTempSave.setCreatedBy(userDaDTO.getCreatedBy());
            userDaTempSave.setModifiedBy(userDaDTO.getModifiedBy());
            userDaTempSave.setCleanAmount(userDaDTO.getCleanAmount());
            log.info(USER_DA_TEMP_WITH, userDaTempSave);

            userDaTempSave = userDaTempRepository.saveAndFlush(userDaTempSave);
            log.info("SUCCESS: Saved User DA with ID : {}", userDaTempSave.getUserDaID());
        } else {
            throw new ApiRequestException("UserDA Already Exists");
        }
        StandardResponse<UserDaDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDaTempSave);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UserDaDTO>> approveRejectUserDa(ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        if(approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null){
            throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
        }

        UserDaTemp userDaTemp = userDaTempRepository.findById(approveRejectRQ.getApproveRejectDataID()).orElseThrow(() -> {
            throw new ApiRequestException(USER_DA_TEMP_WITH + approveRejectRQ.getApproveRejectDataID() + DOES_NOT_EXISTS);
        });
        Date date = new Date();
        Optional<UserDa> optionalUserDa = userDaRepository.findById(userDaTemp.getUserDaID());
        UserDa findUserDa = optionalUserDa.orElse(null);
        log.info("findUserDa :{}", findUserDa);

        userDaTemp.setApprovedDate(date);
        userDaTemp.setApproveStatus(approveRejectRQ.getApproveStatus());

        userDaTempRepository.save(userDaTemp);

        ResponseEntity<StandardResponse<UserDaDTO>> response;

        if (MasterDataApproveStatus.APPROVED.equals(approveRejectRQ.getApproveStatus())) {
            response = handleApproval(userDaTemp, findUserDa);
        } else if (MasterDataApproveStatus.REJECTED.equals(approveRejectRQ.getApproveStatus())) {
            response = handleRejection(userDaTemp);
        } else {
            throw new ApiRequestException("Unknown approval status: " + approveRejectRQ.getApproveStatus());
        }

        return response;
    }

    private ResponseEntity<StandardResponse<UserDaDTO>> handleApproval(UserDaTemp temp, UserDa existingUserDa){
        UserDa savedUserDa;
        if(existingUserDa != null && existingUserDa.getUserDaID().equals(temp.getUserDaID())){
            savedUserDa = updateUserDaToMaster(temp, existingUserDa);
        } else {
            savedUserDa = mapUserDa(temp, null);
        }

        saveUserDaAudit(temp);
        userDaTempRepository.delete(temp);
        StandardResponse<UserDaDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedUserDa);
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<StandardResponse<UserDaDTO>> handleRejection(UserDaTemp temp){
        log.info("Handling rejection for User Da Temp ID: {} ", temp.getUserDaID());
        saveUserDaAudit(temp);
        StandardResponse<UserDaDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), temp);
        return ResponseEntity.ok().body(response);
    }

    private UserDa updateUserDaToMaster(UserDaTemp userDaTemp, UserDa userDa){
        Date date = new Date();
        userDa.setStatus(userDaTemp.getStatus());
        userDa.setCreatedDate(userDaTemp.getCreatedDate());
        userDa.setLastModifiedDate(date);
        userDa.setApproveStatus(userDaTemp.getApproveStatus());
        userDa.setUserName(userDaTemp.getUserName());
        userDa.setMaxAmount(userDaTemp.getMaxAmount());
        userDa.setDescription(userDaTemp.getDescription());
        userDa.setApprovedDate(userDaTemp.getApprovedDate());
        userDa.setApprovedBy(userDaTemp.getApprovedBy());
        userDa.setCreatedBy(userDaTemp.getCreatedBy());
        userDa.setModifiedBy(userDaTemp.getModifiedBy());
        userDa.setCleanAmount(userDaTemp.getCleanAmount());

        return userDaRepository.saveAndFlush(userDa);
    }

    private UserDa mapUserDa(UserDaTemp userDaTemp, UserDa exisitingUserDa){
        UserDa userDa = (exisitingUserDa != null) ? exisitingUserDa : new UserDa();
        Date date = new Date();

        userDa.setUserDaID(userDaTemp.getUserDaID());
        userDa.setStatus(userDaTemp.getStatus());
        userDa.setCreatedDate(userDaTemp.getCreatedDate());
        userDa.setLastModifiedDate(date);
        userDa.setApproveStatus(userDaTemp.getApproveStatus());
        userDa.setUserName(userDaTemp.getUserName());
        userDa.setMaxAmount(userDaTemp.getMaxAmount());
        userDa.setDescription(userDaTemp.getDescription());
        userDa.setApprovedDate(userDaTemp.getApprovedDate());
        userDa.setApprovedBy(userDaTemp.getApprovedBy());
        userDa.setCreatedBy(userDaTemp.getCreatedBy());
        userDa.setModifiedBy(userDaTemp.getModifiedBy());


        return userDaRepository.saveAndFlush(userDa);
    }

    private void saveUserDaAudit(UserDaTemp temp){
        if(temp == null) return;

        UserDaAud audit = new UserDaAud();
        audit.setUserDaID(temp.getUserDaID());
        audit.setStatus(temp.getStatus());
        audit.setCreatedDate(temp.getCreatedDate());
        audit.setLastModifiedDate(temp.getLastModifiedDate());
        audit.setApproveStatus(temp.getApproveStatus());
        audit.setUserName(temp.getUserName());
        audit.setMaxAmount(temp.getMaxAmount());
        audit.setDescription(temp.getDescription());
        audit.setApprovedDate(temp.getApprovedDate());
        audit.setApprovedBy(temp.getApprovedBy());
        audit.setCreatedBy(temp.getCreatedBy());
        audit.setModifiedBy(temp.getModifiedBy());
        audit.setCleanAmount(temp.getCleanAmount());
        userDaAudRepository.save(audit);
        log.info("saved audit record for User Da ID: {}", temp.getUserDaID());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UserDaDTO>> updateUserDaTemp(Integer userDaID, UserDaDTO userDaDTO) throws ApiRequestException {

        UserDaTemp userDaDb = userDaTempRepository.findById(userDaID).orElseThrow(() -> {
            throw new ApiRequestException(USER_DA_TEMP_WITH + userDaID + DOES_NOT_EXISTS);
        });

        if (userDaDTO.getCleanAmount() != null && userDaDTO.getCleanAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiRequestException("Clean amount should be positive!");
        }
        Date date = new Date();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QUserDaTemp.userDaTemp.userName.eq(userDaDTO.getUserName()));
        List<UserDaTemp> userDaTempList = (List<UserDaTemp>) userDaTempRepository.findAll(booleanBuilder);

        if(userDaDTO.getUserName() == null || userDaDTO.getUserName().trim().isEmpty()){
            throw new ApiRequestException("User name cannot be empty or null.");
        }

        validateUserDaNameUniqueness(userDaDTO.getUserName(), userDaID);

        // Use a stream to check if any element in the list matches the provided string
        boolean existingTempList = userDaTempList.stream()
                        .anyMatch(temp -> temp.getUserName().equals(userDaDTO.getUserName()));
        if(existingTempList &&
        !userDaDb.getUserName().equals(userDaDTO.getUserName())){
            throw new ApiRequestException(USER_DA_TEMP_WITH + userDaDTO.getUserName() + " Already Exists");
        }

            userDaDb.setUserName(userDaDTO.getUserName());
            userDaDb.setDescription(userDaDTO.getDescription());
            userDaDb.setMaxAmount(userDaDTO.getMaxAmount());
            userDaDb.setStatus(userDaDTO.getStatus());
            userDaDb.setApproveStatus(userDaDTO.getApproveStatus());
            userDaDb.setModifiedBy(userDaDTO.getModifiedBy());
            userDaDb.setApprovedDate(userDaDTO.getApprovedDate());
            userDaDb.setApprovedBy(userDaDTO.getApprovedBy());
            userDaDb.setCreatedDate(userDaDTO.getCreatedDate());
            userDaDb.setCreatedBy(userDaDTO.getCreatedBy());
            userDaDb.setLastModifiedDate(date);
            userDaDb.setCleanAmount(userDaDTO.getCleanAmount());
            userDaTempRepository.save(userDaDb);

            log.info("END: Update UserDa: {}", userDaDb);

            StandardResponse<UserDaDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDaDb);
            return ResponseEntity.ok().body(response);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<UserDaDTO>> updateApprovedUserDa(Integer userDaID, UserDaDTO userDaDTO) throws ApiRequestException{
        log.info("START: Update UPC Section :{}", userDaDTO);

        UserDa userDaDb = userDaRepository.findById(userDaID).orElseThrow(() -> {
            throw new ApiRequestException(USER_DA_WITH + userDaID + DOES_NOT_EXISTS);
        });

        if (userDaDTO.getCleanAmount() != null && userDaDTO.getCleanAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiRequestException("Clean amount should be positive!");
        }

        log.info("START : GET UserDa. {}", userDaDb);

        if(!userDaDb.getUserName().equals(userDaDTO.getUserName())){
            validateUserDaNameUniqueness(userDaDTO.getUserName(), userDaID);
        } else if (userDaDTO.getUserName() == null || userDaDTO.getUserName().trim().isEmpty()) {
            throw new ApiRequestException("User name cannot be empty or null.");
        }

        UserDaTemp userDaTemp = mapToUserDaTemp(userDaDb, userDaDTO);

        log.info("END : GET UserDa {}", userDaTemp);
        userDaTemp = userDaTempRepository.saveAndFlush(userDaTemp);

        StandardResponse<UserDaDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDaTemp);
        return ResponseEntity.ok().body(response);

    }

    private void validateUserDaNameUniqueness(String userDaName, Integer userDaID) throws ApiRequestException {

        BooleanBuilder tempBuilder = new BooleanBuilder();
        tempBuilder.and(QUserDaTemp.userDaTemp.userName.eq(userDaName));
        if(userDaID != null) {
            tempBuilder.and(QUserDaTemp.userDaTemp.userDaID.ne(userDaID));
        }
        boolean existsInTemp = userDaTempRepository.exists(tempBuilder);

        BooleanBuilder masterBuilder = new BooleanBuilder();
        masterBuilder.and(QUserDa.userDa.userName.eq(userDaName));

        if(userDaID != null){
            masterBuilder.and(QUserDa.userDa.userDaID.ne(userDaID));
        }

        boolean existsInMaster = userDaRepository.exists(masterBuilder);

        if (existsInTemp || existsInMaster) {
            throw new ApiRequestException("User name '" + userDaName + "' already exists in the system.");
        }

    }

    private UserDaTemp mapToUserDaTemp(UserDa userDa, UserDaDTO userDaDTO){
        Date date = new Date();
        UserDaTemp userDaTemp = new UserDaTemp();

        userDaTemp.setUserDaID(userDa.getUserDaID());
        userDaTemp.setUserName(userDaDTO.getUserName());
        userDaTemp.setDescription(userDaDTO.getDescription());
        userDaTemp.setMaxAmount(userDaDTO.getMaxAmount());
        userDaTemp.setStatus(userDaDTO.getStatus());
        userDaTemp.setApproveStatus(userDaDTO.getApproveStatus());
        userDaTemp.setModifiedBy(userDa.getModifiedBy());
        userDaTemp.setApprovedDate(userDaDTO.getApprovedDate());
        userDaTemp.setApprovedBy(userDaDTO.getApprovedBy());
        userDaTemp.setCreatedDate(userDa.getCreatedDate());
        userDaTemp.setCreatedBy(userDa.getCreatedBy());
        userDaTemp.setLastModifiedDate(date);
        userDaTemp.setCleanAmount(userDaDTO.getCleanAmount());
        return userDaTemp;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Void>> deleteUserDaFromTemp(Integer userDaID) throws ApiRequestException{
        userDaTempRepository.deleteById(userDaID);
        StandardResponse<Void> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), userDaID);
        return ResponseEntity.ok().body(response);
    }

}

