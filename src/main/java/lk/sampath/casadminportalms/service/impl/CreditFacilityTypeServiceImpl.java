package lk.sampath.casadminportalms.service.impl;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacility.CreditFacilityTypeDTO;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityTypeAud;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityTypeTemp;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeAudRepository;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeRepository;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeTempRepository;
import lk.sampath.casadminportalms.service.CreditFacilityTypeService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 *
 * @author yomesh
 */

@Service
public class CreditFacilityTypeServiceImpl implements CreditFacilityTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CreditFacilityTypeServiceImpl.class);

    public static final String CREDIT_FACILITY_TYPE_WITH = "Credit Facility Type with ID ";

    public static final String DOES_NOT_EXISTS = " does not exists";
    public static final String ALL_READY_EXISTS = " all ready exists";
    public static final String APPROVE_REJECT_FAIL ="Approve Reject Action failed ";
    public static final String TEMP_TABLE =" in temporary table ";
    public static final String MASTER_TABLE =" in Master Table";

    private final ModelMapper modelMapper;
    private final CreditFacilityTypeRepository creditFacilityTypeRepository;
    private final CreditFacilityTypeTempRepository creditFacilityTypeTempRepository;
    private final CreditFacilityTypeAudRepository creditFacilityTypeAudRepository;

    @Autowired
    public CreditFacilityTypeServiceImpl(
            ModelMapper modelMapper,
            CreditFacilityTypeRepository creditFacilityTypeRepository,
            CreditFacilityTypeTempRepository creditFacilityTypeTempRepository,
            CreditFacilityTypeAudRepository creditFacilityTypeAudRepository) {
        this.modelMapper = modelMapper;
        this.creditFacilityTypeRepository = creditFacilityTypeRepository;
        this.creditFacilityTypeTempRepository = creditFacilityTypeTempRepository;

        this.creditFacilityTypeAudRepository = creditFacilityTypeAudRepository;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    @Override
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> saveCreditFacilityTypeTemp(CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException {

        LOG.info("START: Save Credit Facility type :{}", creditFacilityTypeTempDTO);
        CreditFacilityTypeTemp creditFacilityTypeTemp = new CreditFacilityTypeTemp();
        Date date = new Date();
        StandardResponse<CreditFacilityTypeDTO> response;

        try {
            if (StringUtils.isEmpty(creditFacilityTypeTempDTO.getFacilityTypeName())) {
                LOG.info("END: Facility Type Name is empty or null. Throwing ApiRequestException.");

                throw new ApiRequestException("Facility Type Name Cannot be null or empty");
            }

            validateCreditFacilityTypeNameUniquenessAdd(creditFacilityTypeTempDTO);

            creditFacilityTypeTemp.setCreditFacilityTypeID(creditFacilityTypeTempRepository.getCurrentSequenceValue());
            creditFacilityTypeTemp.setFacilityTypeName(creditFacilityTypeTempDTO.getFacilityTypeName());
            creditFacilityTypeTemp.setDescription(creditFacilityTypeTempDTO.getDescription());
            creditFacilityTypeTemp.setStatus(creditFacilityTypeTempDTO.getStatus());
            creditFacilityTypeTemp.setApproveStatus(creditFacilityTypeTempDTO.getApproveStatus());
            creditFacilityTypeTemp.setCreatedBy(creditFacilityTypeTempDTO.getCreatedBy());
            creditFacilityTypeTemp.setCreatedDate(date);
            LOG.info("Converted Credit Facility type to Save :{}", creditFacilityTypeTemp);
                creditFacilityTypeTempRepository.save(creditFacilityTypeTemp);
            CreditFacilityTypeDTO creditFacilityTypeTempDto = new CreditFacilityTypeDTO(creditFacilityTypeTemp);
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTypeTempDto);
        }

        catch (Exception exception){

            LOG.info("END: In Adding Credit facility type threw the exception  with message :{}", exception.getMessage());
           if (exception instanceof ApiRequestException){
               throw exception;
           }
           else {

               throw new ApiRequestException("Unable to Add Credit Facility Type");
           }
        }

        LOG.info("END: Response Body in Save Credit Facility type :{}", response.getResponse());
        return ResponseEntity.ok().body(response);
    }

    private void validateCreditFacilityTypeNameUniquenessAdd (CreditFacilityTypeDTO creditFacilityTypeTempDTO ) throws ApiRequestException {
        LOG.info("START: validateCreditFacilityTypeNameUniquenessAdd function in Save Credit Facility type :{}", creditFacilityTypeTempDTO);
        if (creditFacilityTypeTempRepository.existsByFacilityTypeName(creditFacilityTypeTempDTO.getFacilityTypeName())) {
            LOG.info("END: validateCreditFacilityTypeNameUniquenessAdd function in Save Credit Facility type threw excpetion for facility name:{}", creditFacilityTypeTempDTO.getFacilityTypeName());
                throw new ApiRequestException(CREDIT_FACILITY_TYPE_WITH+creditFacilityTypeTempDTO.getFacilityTypeName() +ALL_READY_EXISTS + TEMP_TABLE);
        }

        if (creditFacilityTypeRepository.existsByFacilityTypeName(creditFacilityTypeTempDTO.getFacilityTypeName()) ) {
            LOG.info("END: validateCreditFacilityTypeNameUniquenessAdd function in Save Credit Facility type threw excpetion for facility name:{}", creditFacilityTypeTempDTO.getFacilityTypeName());
            throw new ApiRequestException(CREDIT_FACILITY_TYPE_WITH+creditFacilityTypeTempDTO.getFacilityTypeName() +ALL_READY_EXISTS + MASTER_TABLE);
        }

    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> findCreditFacilityTypeTempByID(Integer creditFacilityTypeID) throws ApiRequestException {
        LOG.info("START: Find Credit Facility type from temp by ID :{}", creditFacilityTypeID);
        StandardResponse<CreditFacilityTypeDTO> response;
        try {
            CreditFacilityTypeTemp creditFacilityTypeTemp = creditFacilityTypeTempRepository.findById(creditFacilityTypeID).orElseThrow(() ->
                    new ApiRequestException(CREDIT_FACILITY_TYPE_WITH + creditFacilityTypeID + DOES_NOT_EXISTS));

            CreditFacilityTypeDTO creditFacilityTypeTempDTO = new CreditFacilityTypeDTO(creditFacilityTypeTemp);
            LOG.info("Find Credit Facility type from temp by ID :{}", creditFacilityTypeTempDTO);
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTypeTempDTO);
        }

        catch (Exception exception){
            LOG.info("END: In find by id from temp threw the exception with message :{}", exception.getMessage());
            if (exception instanceof ApiRequestException){
                throw exception;
            }
            else {
                throw new ApiRequestException("Unable to Fetch Credit Facility Type");
            }
        }

        LOG.info("END: Find Credit Facility type from temp by ID :{}", creditFacilityTypeID);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateCreditFacilityTempType(Integer creditFacilityTypeID, CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException {

        LOG.info("START: Update Credit Facility type in Temp :{}, with ID :{}", creditFacilityTypeTempDTO, creditFacilityTypeID);
        CreditFacilityTypeTemp creditFacilityTypeTemp ;

        try {
            if (StringUtils.isEmpty(creditFacilityTypeTempDTO.getFacilityTypeName())) {
                throw new ApiRequestException("Facility Type Name Cannot be null");
            }
            creditFacilityTypeTemp = creditFacilityTypeTempRepository.findById(creditFacilityTypeID).orElseThrow(() ->
                    new ApiRequestException("Credit Facility type with ID " + creditFacilityTypeID + " does not exist")
            );

            Date date = new Date();

            validateCreditFacilityTypeNameUniquenessTemp(creditFacilityTypeTemp, creditFacilityTypeTempDTO);

            creditFacilityTypeTemp.setStatus(creditFacilityTypeTempDTO.getStatus());
            creditFacilityTypeTemp.setLastModifiedDate(date);
            creditFacilityTypeTemp.setModifiedBy(creditFacilityTypeTempDTO.getModifiedBy());
            creditFacilityTypeTemp.setFacilityTypeName(creditFacilityTypeTempDTO.getFacilityTypeName());
            creditFacilityTypeTemp.setDescription(creditFacilityTypeTempDTO.getDescription());
            creditFacilityTypeTemp.setApproveStatus(creditFacilityTypeTempDTO.getApproveStatus());


                creditFacilityTypeTempRepository.save(creditFacilityTypeTemp);

        }

        catch (Exception exception){
            LOG.info("END: In update credit facility type in temp threw the exception with message :{}", exception.getMessage());
            if (exception instanceof ApiRequestException){
                throw exception;
            }
            else {
                throw new ApiRequestException("Unable to Update Credit Facility Type");

            }

        }

        StandardResponse<CreditFacilityTypeDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), new CreditFacilityTypeDTO(creditFacilityTypeTemp));
        LOG.info("END:  Update Credit Facility type in Temp :{}, with ID :{}", creditFacilityTypeTempDTO, creditFacilityTypeID);
        return ResponseEntity.ok().body(response);
    }

    private void validateCreditFacilityTypeNameUniquenessTemp (CreditFacilityTypeTemp creditFacilityTypeTemp,CreditFacilityTypeDTO creditFacilityTypeTempDTO ) throws ApiRequestException {

        LOG.info("START: validateCreditFacilityTypeNameUniquenessTemp in update temp :{}, with ID :{}", creditFacilityTypeTempDTO, creditFacilityTypeTemp.getCreditFacilityTypeID());

        if(!creditFacilityTypeTemp.getFacilityTypeName().equals(creditFacilityTypeTempDTO.getFacilityTypeName())

                && creditFacilityTypeTempRepository.existsByFacilityTypeName(creditFacilityTypeTempDTO.getFacilityTypeName())) {

                throw new ApiRequestException("Credit Facility Type Name Already in Temporary Records");
            }

            CreditFacilityType creditFacilityTypeMaster = creditFacilityTypeRepository.findCreditFacilityTypeByName(creditFacilityTypeTempDTO.getFacilityTypeName());
            if(creditFacilityTypeMaster != null

                    && !Objects.equals(creditFacilityTypeMaster.getCreditFacilityTypeID(), creditFacilityTypeTemp.getCreditFacilityTypeID())) {

                    throw new ApiRequestException("Credit Facility Type Name Already in Master Records");
                }

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> approveRejectCreditFacilityType(ApproveRejectRQ approveRejectRQ) throws ApiRequestException {

        LOG.info("START: Approve Reject Credit Facility Type :{}", approveRejectRQ);
        CreditFacilityTypeTemp creditFacilityTypeTemp;
        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> response = null;
        try {
            if (approveRejectRQ == null ||

                    approveRejectRQ.getApproveRejectDataID() == null) {

                throw new ApiRequestException("Invalid ApproveRejectRQ for creditfacilitytype: CreditFacilityTypeID cannot be null");
            }

            Date date = new Date();


             creditFacilityTypeTemp = creditFacilityTypeTempRepository.findById(approveRejectRQ.getApproveRejectDataID()).orElseThrow(() ->

                     new ApiRequestException(CREDIT_FACILITY_TYPE_WITH + approveRejectRQ.getApproveRejectDataID() + DOES_NOT_EXISTS));

            Optional<CreditFacilityType> optionalCreditFacilityType = creditFacilityTypeRepository.findById(creditFacilityTypeTemp.getCreditFacilityTypeID());
            CreditFacilityType findCreditFacilityType = optionalCreditFacilityType.orElse(null);


            creditFacilityTypeTemp.setApproveStatus(approveRejectRQ.getApproveStatus());

            creditFacilityTypeTemp.setApprovedDate(date);


                creditFacilityTypeTempRepository.save(creditFacilityTypeTemp);



            if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.APPROVED)) {

                    response = handleApproval(creditFacilityTypeTemp, findCreditFacilityType);

            }
            else
                if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.REJECTED)) {

                    response = handleRejection(creditFacilityTypeTemp);

                }
                else{
                    LOG.info("END: Invalid approval status: {}", approveRejectRQ.getApproveStatus());

                    throw new ApiRequestException("Not a valid approval status");
                }

            LOG.info("SUCCESS: Moved Credit Facility Type Temp with ID :{} to Credit Facility Type with ID :{}", creditFacilityTypeTemp.getCreditFacilityTypeID(), approveRejectRQ.getApproveRejectDataID());
        }
        catch (Exception exception){
            LOG.info("END: In approve reject credit facility type threw the exception with message :{}", exception.getMessage());
            if (exception instanceof ApiRequestException){
                throw exception;
            }
            else {
                throw new ApiRequestException(APPROVE_REJECT_FAIL);

            }

        }

        LOG.info("END:  approve reject Credit Facility type in Temp with ID :{}", approveRejectRQ.getApproveRejectDataID() );
        return response;
    }


    private ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> handleApproval(CreditFacilityTypeTemp creditFacilityTypeTemp, CreditFacilityType existingCreditFacilityType) {
        LOG.info("START: Handling approval for Credit Facility Type Temp with ID: {}", creditFacilityTypeTemp.getCreditFacilityTypeID());

        CreditFacilityType savedCreditFacilityType;
        try {
            if(existingCreditFacilityType != null &&

                    existingCreditFacilityType.getCreditFacilityTypeID().equals(creditFacilityTypeTemp.getCreditFacilityTypeID())){

                savedCreditFacilityType = updateCreditFacilityTypeToMaster(creditFacilityTypeTemp, existingCreditFacilityType);

            } else {
                savedCreditFacilityType = saveCreditFacilityTypeToMaster(creditFacilityTypeTemp);
            }

            LOG.info("Saving audit information for Credit Facility Type Temp with ID: {}", creditFacilityTypeTemp.getCreditFacilityTypeID());

            saveCreditFacilityTypeAudit(creditFacilityTypeTemp);

            creditFacilityTypeTempRepository.delete(creditFacilityTypeTemp);

        }
        catch (Exception e) {
            LOG.info("Error occurred while handling approval for Credit Facility Type Temp with ID: {}", creditFacilityTypeTemp.getCreditFacilityTypeID(), e);

            throw new ApiRequestException(APPROVE_REJECT_FAIL);
        }


        StandardResponse<CreditFacilityTypeDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), new CreditFacilityTypeDTO( savedCreditFacilityType));
        LOG.info("END: Returning response for Credit Facility Type with ID: {}", savedCreditFacilityType.getCreditFacilityTypeID());

        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<StandardResponse<CreditFacilityTypeDTO>>  handleRejection(CreditFacilityTypeTemp creditFacilityTypeTemp) {

        LOG.info("Handling rejection for UPM Group Temp ID: {}", creditFacilityTypeTemp.getFacilityTypeName());
        StandardResponse<CreditFacilityTypeDTO> response;
        try {
            saveCreditFacilityTypeAudit(creditFacilityTypeTemp);
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), new CreditFacilityTypeDTO (creditFacilityTypeTemp));

        }
        catch (Exception e) {
            LOG.info("Error occurred while handling rejection for Credit Facility Type Temp with ID: {}", creditFacilityTypeTemp.getCreditFacilityTypeID(), e);

            throw new ApiRequestException(APPROVE_REJECT_FAIL);
        }
        LOG.info("END: Rejection handled for Credit Facility Type Temp with ID: {}", creditFacilityTypeTemp.getCreditFacilityTypeID());

        return ResponseEntity.ok().body(response);
    }

    private CreditFacilityType updateCreditFacilityTypeToMaster(CreditFacilityTypeTemp creditFacilityTypeTemp, CreditFacilityType existingCreditFacilityType) {

        Date date = new Date();

        existingCreditFacilityType.setCreditFacilityTypeID(creditFacilityTypeTemp.getCreditFacilityTypeID());
        existingCreditFacilityType.setStatus(creditFacilityTypeTemp.getStatus());
        existingCreditFacilityType.setCreatedDate(creditFacilityTypeTemp.getCreatedDate());
        existingCreditFacilityType.setApproveStatus(creditFacilityTypeTemp.getApproveStatus());
        existingCreditFacilityType.setFacilityTypeName(creditFacilityTypeTemp.getFacilityTypeName());
        existingCreditFacilityType.setDescription(creditFacilityTypeTemp.getDescription());
        existingCreditFacilityType.setApprovedBy(creditFacilityTypeTemp.getApprovedBy());
        existingCreditFacilityType.setCreatedBy(creditFacilityTypeTemp.getCreatedBy());
        existingCreditFacilityType.setApprovedDate(creditFacilityTypeTemp.getApprovedDate());
        existingCreditFacilityType.setLastModifiedDate(date);
        existingCreditFacilityType.setModifiedBy(creditFacilityTypeTemp.getModifiedBy());



        return creditFacilityTypeRepository.save(existingCreditFacilityType);
    }

    private CreditFacilityType saveCreditFacilityTypeToMaster(CreditFacilityTypeTemp creditFacilityTypeTemp) {

        CreditFacilityType creditFacilityType =  new CreditFacilityType();
        Date date = new Date();

        creditFacilityType.setCreditFacilityTypeID(creditFacilityTypeTemp.getCreditFacilityTypeID());
        creditFacilityType.setStatus(creditFacilityTypeTemp.getStatus());
        creditFacilityType.setCreatedDate(creditFacilityTypeTemp.getCreatedDate());
        creditFacilityType.setApproveStatus(creditFacilityTypeTemp.getApproveStatus());
        creditFacilityType.setFacilityTypeName(creditFacilityTypeTemp.getFacilityTypeName());
        creditFacilityType.setDescription(creditFacilityTypeTemp.getDescription());
        creditFacilityType.setApprovedBy(creditFacilityTypeTemp.getApprovedBy());
        creditFacilityType.setCreatedBy(creditFacilityTypeTemp.getCreatedBy());
        creditFacilityType.setApprovedDate(creditFacilityTypeTemp.getApprovedDate());
        creditFacilityType.setLastModifiedDate(date);
        creditFacilityType.setModifiedBy(creditFacilityTypeTemp.getModifiedBy());


        return creditFacilityTypeRepository.save(creditFacilityType);
    }

    private void saveCreditFacilityTypeAudit(CreditFacilityTypeTemp creditFacilityTypeTemp){

        CreditFacilityTypeAud audit = new CreditFacilityTypeAud();
        audit.setCreditFacilityTypeID(creditFacilityTypeTemp.getCreditFacilityTypeID());
        audit.setStatus(Status.ACT);
        audit.setCreatedDate(creditFacilityTypeTemp.getCreatedDate());
        audit.setApproveStatus(creditFacilityTypeTemp.getApproveStatus());
        audit.setFacilityTypeName(creditFacilityTypeTemp.getFacilityTypeName());
        audit.setDescription(creditFacilityTypeTemp.getDescription());
        audit.setApprovedBy(creditFacilityTypeTemp.getApprovedBy());
        audit.setCreatedBy(creditFacilityTypeTemp.getCreatedBy());
        audit.setApprovedDate(creditFacilityTypeTemp.getApprovedDate());
        audit.setLastModifiedDate(creditFacilityTypeTemp.getLastModifiedDate());
        audit.setModifiedBy(creditFacilityTypeTemp.getModifiedBy());

        creditFacilityTypeAudRepository.save(audit);

        LOG.info("Saved audit record for UPM Group ID: {}", creditFacilityTypeTemp.getCreditFacilityTypeID());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateApprovedCreditFacilityType(Integer creditFacilityTypeID, CreditFacilityTypeDTO creditFacilityTypeDTO) throws ApiRequestException{

        LOG.info("START : Update approved Credit Facility Type {}", creditFacilityTypeDTO);
        CreditFacilityType creditFacilityTypeDb;
        CreditFacilityTypeTemp creditFacilityTypeTemp = new CreditFacilityTypeTemp();
        try {
            if (StringUtils.isEmpty(creditFacilityTypeDTO.getFacilityTypeName())) {
                LOG.info("END : Update approved Credit Facility Type threw error");
                throw new ApiRequestException("Facility Type Name Cannot be null");
            }
             creditFacilityTypeDb = creditFacilityTypeRepository.findById(creditFacilityTypeID).orElseThrow(() ->
                    new ApiRequestException(CREDIT_FACILITY_TYPE_WITH + creditFacilityTypeDTO.getCreditFacilityTypeID() + DOES_NOT_EXISTS));
            validateCreditFacilityTypeByIdApprovedTypes(creditFacilityTypeDb);

            validateCreditFacilityTypeNameUniqueness(creditFacilityTypeDb, creditFacilityTypeDTO);

            Date date = new Date();


            creditFacilityTypeTemp.setCreditFacilityTypeID(creditFacilityTypeDb.getCreditFacilityTypeID());
            creditFacilityTypeTemp.setLastModifiedDate(date);
            creditFacilityTypeTemp.setModifiedBy(creditFacilityTypeDTO.getModifiedBy());
            creditFacilityTypeTemp.setStatus(creditFacilityTypeDTO.getStatus());
            creditFacilityTypeTemp.setApproveStatus(creditFacilityTypeDTO.getApproveStatus());
            creditFacilityTypeTemp.setFacilityTypeName(creditFacilityTypeDTO.getFacilityTypeName());
            creditFacilityTypeTemp.setDescription(creditFacilityTypeDTO.getDescription());
            creditFacilityTypeTemp.setCreatedBy(creditFacilityTypeDb.getCreatedBy());
            creditFacilityTypeTemp.setCreatedDate(creditFacilityTypeDb.getCreatedDate());
            LOG.info(" GET Updated Credit Facility Type from Temp {}", creditFacilityTypeTemp);
                creditFacilityTypeTempRepository.save(creditFacilityTypeTemp);

        }

        catch (Exception exception){
            LOG.info("END: In update approved credit facility type threw the exception with message :{}", exception.getMessage());
            if (exception instanceof ApiRequestException){
                throw exception;
            }
            else {
                throw new ApiRequestException("Unable to Update Credit Facility Type");

            }


        }

        LOG.info("END : Updated Credit Facility Type from Temp {}", creditFacilityTypeDb);


        StandardResponse<CreditFacilityTypeDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), new CreditFacilityTypeDTO (creditFacilityTypeTemp));
        return ResponseEntity.ok().body(response);
    }

    private void validateCreditFacilityTypeByIdApprovedTypes(CreditFacilityType creditFacilityTypeDb){
        if (creditFacilityTypeTempRepository.existsByCreditFacilityTypeID(creditFacilityTypeDb.getCreditFacilityTypeID())) {
            throw new ApiRequestException("Credit Facility Type Already in Temporary Records");
        }
    }
    private void validateCreditFacilityTypeNameUniqueness (CreditFacilityType creditFacilityTypeDb,CreditFacilityTypeDTO creditFacilityTypeDTO ) throws ApiRequestException {

        if (creditFacilityTypeTempRepository.existsByFacilityTypeName(creditFacilityTypeDTO.getFacilityTypeName())) {
            throw new ApiRequestException("Credit Facility Type Name Already in Temporary Records");
        }
        if(!creditFacilityTypeDb.getFacilityTypeName().equals(creditFacilityTypeDTO.getFacilityTypeName())
                && creditFacilityTypeRepository.existsByFacilityTypeName(creditFacilityTypeDTO.getFacilityTypeName())) {
                throw new ApiRequestException("Credit Facility Type Name Already in Master Records");
            }

    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> searchCreditFacilityTypes() throws ApiRequestException {
        LOG.info("START : Search Credit Facility Type List  in Master ");
        List<CreditFacilityType> creditFacilityTypes = new ArrayList<>();
        List<CreditFacilityTypeDTO> creditFacilityTypeDTOList;
        try {

                creditFacilityTypes = creditFacilityTypeRepository.findAllApprovedCreditFacilityTypes();

            creditFacilityTypeDTOList = creditFacilityTypes.stream().map(creditFacilityType -> modelMapper.map(creditFacilityType, CreditFacilityTypeDTO.class)).toList();
        }

        catch (Exception exception){
            LOG.info("END: Failed to fetch Credit Facility Types with error: {}", exception.getMessage(), exception);

            throw new ApiRequestException("Unable to Fetch Credit Facility Types");

        }
        StandardResponse<List<CreditFacilityTypeDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTypeDTOList);
        LOG.info("END :  Search Credit Facility Type List in Master ");
        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> findCreditFacilityTypeByID(Integer creditFacilityTypeID) throws ApiRequestException {
        LOG.info("START : GET Credit Facility Types in Master with ID : {} ",creditFacilityTypeID);
        CreditFacilityType creditFacilityType;
        CreditFacilityTypeDTO creditFacilityTypeDTO;
        try {
            creditFacilityType = creditFacilityTypeRepository.findById(creditFacilityTypeID).orElseThrow(() ->
                    new ApiRequestException(CREDIT_FACILITY_TYPE_WITH + creditFacilityTypeID + DOES_NOT_EXISTS));
             creditFacilityTypeDTO = modelMapper.map(creditFacilityType, CreditFacilityTypeDTO.class);

        }

        catch (Exception exception){
            LOG.info("END: Credit Facility Types in Master threw the exception with message :{}", exception.getMessage());
            if (exception instanceof ApiRequestException){
                throw exception;
            }
            else {
                throw new ApiRequestException("Unable to Fetch Credit Facility Type");
            }


        }
        StandardResponse<CreditFacilityTypeDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTypeDTO);
        LOG.info("END : GET Credit Facility Types in Master with ID : {} ",creditFacilityTypeID);
        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> findAllCreditFacilityTypeTempList() throws ApiRequestException {
        LOG.info("START : GET Credit Facility Types List in Temp ");
        List<CreditFacilityTypeTemp> creditFacilityTypeTempList;
        List<CreditFacilityTypeDTO> creditFacilityTypeTempListDTO = new ArrayList<>();
        try {


             creditFacilityTypeTempList = creditFacilityTypeTempRepository.findAll();
            LOG.info(" Fetched Credit Facility Types List in Temp : {}", creditFacilityTypeTempList);
             if (!creditFacilityTypeTempList.isEmpty()){
                 for(CreditFacilityTypeTemp creditType : creditFacilityTypeTempList){

                     creditFacilityTypeTempListDTO.add( new CreditFacilityTypeDTO(creditType));
                 }
             }

        }
        catch (Exception exception){
            LOG.info("END: Credit Facility Types in Master threw an exception with message: {}", exception.getMessage(), exception);

            throw new ApiRequestException("Unable to Fetch Credit Facility Types");

        }
        StandardResponse<List<CreditFacilityTypeDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTypeTempListDTO);
        LOG.info("END : GET Credit Facility Types List in Temp ");
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Integer>> deleteCreditFacilityTypeTemp( CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException {

        LOG.info("START : Delete Credit Facility Type with ID {} ",creditFacilityTypeTempDTO);
        CreditFacilityTypeTemp creditFacilityTypeTemp;
        try {
             creditFacilityTypeTemp = creditFacilityTypeTempRepository.findById(creditFacilityTypeTempDTO.getCreditFacilityTypeID()).orElseThrow(() ->
                    new ApiRequestException(CREDIT_FACILITY_TYPE_WITH + creditFacilityTypeTempDTO.getCreditFacilityTypeID() + DOES_NOT_EXISTS)
            );

                creditFacilityTypeTempRepository.deleteById(creditFacilityTypeTemp.getCreditFacilityTypeID());

        }

        catch (Exception exception){
            LOG.info("END: Credit Facility Types Delete threw the exception with message :{}", exception.getMessage());
            if (exception instanceof ApiRequestException){
                throw exception;
            }
            else {
                throw new ApiRequestException("Deletion failed");
            }


        }
        StandardResponse<Integer> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTypeTemp.getCreditFacilityTypeID());
        LOG.info("END : Delete Credit Facility Type with ID {} ",creditFacilityTypeTempDTO.getCreditFacilityTypeID());
        return ResponseEntity.ok().body(response);
    }


}
