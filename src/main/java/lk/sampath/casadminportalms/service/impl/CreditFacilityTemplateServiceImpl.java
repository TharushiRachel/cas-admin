package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.dto.userSession.UserContext;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDoc;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeRepository;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocRepository;
import lk.sampath.casadminportalms.service.CreditFacilityTemplateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
public class CreditFacilityTemplateServiceImpl implements CreditFacilityTemplateService {

    private static final String NOT_FOUND_RECORD_MSG = "Not Found";

    private static final String DOES_NOT_EXISTS = "Does Not Exists";

    private final CreditFacilityTemplateRepository creditFacilityTemplateRepository;

    private final CreditFacilityTemplateTempRepository creditFacilityTemplateTempRepository;

    private final CreditFacilityTypeRepository creditFacilityTypeDao;
   
    private final CreditFacilityTemplateAudRepo creditFacilityTemplateAudRepo;
   
    private final CftInterestRateRepository cftInterestRateRepository;

    private final CftInterestRateTempRepository cftInterestRateTempRepository;

    private final CftVitalInfoTempRepository cftVitalInfoTempRepository;
   
    private final CftVitalInfoRepository cftVitalInfoRepository;

    private final CftCustomFacilityInfoTempRepository cftCustomFacilityInfoTempRepository;

    private final CftCustomFacilityInfoRepository cftCustomFacilityInfoRepository;

    private final CftSupportingDocRepository cftSupportingDocRepository;

    private final CftSupportingDocTempRepository cftSupportingDocTempRepository;

    private final SupportingDocRepository supportingDocRepository;
   
    private final CftOtherFacilityInfoRepository otherFacilityInfoRepository;
   
    private final CftOtherFacilityInfoTempRepository otherFacilityInfoTempRepository;

    private final CftInterestRateAudRepo cftInterestRateAudRepo;

    private final CftVitalInfoAudRepo cftVitalInfoAudRepo;

    private final CftCustomFacilityInfoAudRepo cftCustomFacilityInfoAudRepo;

    private final CftSupportingDocAudRepo cftSupportingDocAudRepo;

    private final CftOtherFacilityInfoAudRepo cftOtherFacilityInfoAudRepo;

    public CreditFacilityTemplateServiceImpl(CreditFacilityTemplateRepository creditFacilityTemplateRepository, CreditFacilityTemplateTempRepository creditFacilityTemplateTempRepository, CreditFacilityTypeRepository creditFacilityTypeDao, CreditFacilityTemplateAudRepo creditFacilityTemplateAudRepo, CftInterestRateRepository cftInterestRateRepository, CftInterestRateTempRepository cftInterestRateTempRepository, CftVitalInfoTempRepository cftVitalInfoTempRepository, CftVitalInfoRepository cftVitalInfoRepository, CftCustomFacilityInfoTempRepository cftCustomFacilityInfoTempRepository, CftCustomFacilityInfoRepository cftCustomFacilityInfoRepository, CftSupportingDocRepository cftSupportingDocRepository, CftSupportingDocTempRepository cftSupportingDocTempRepository, SupportingDocRepository supportingDocRepository, CftOtherFacilityInfoRepository otherFacilityInfoRepository, CftOtherFacilityInfoTempRepository otherFacilityInfoTempRepository, CftInterestRateAudRepo cftInterestRateAudRepo, CftVitalInfoAudRepo cftVitalInfoAudRepo, CftCustomFacilityInfoAudRepo cftCustomFacilityInfoAudRepo, CftSupportingDocAudRepo cftSupportingDocAudRepo, CftOtherFacilityInfoAudRepo cftOtherFacilityInfoAudRepo) {
        this.creditFacilityTemplateRepository = creditFacilityTemplateRepository;
        this.creditFacilityTemplateTempRepository = creditFacilityTemplateTempRepository;
        this.creditFacilityTypeDao = creditFacilityTypeDao;
        this.creditFacilityTemplateAudRepo = creditFacilityTemplateAudRepo;
        this.cftInterestRateRepository = cftInterestRateRepository;
        this.cftInterestRateTempRepository = cftInterestRateTempRepository;
        this.cftVitalInfoTempRepository = cftVitalInfoTempRepository;
        this.cftVitalInfoRepository = cftVitalInfoRepository;
        this.cftCustomFacilityInfoTempRepository = cftCustomFacilityInfoTempRepository;
        this.cftCustomFacilityInfoRepository = cftCustomFacilityInfoRepository;
        this.cftSupportingDocRepository = cftSupportingDocRepository;
        this.cftSupportingDocTempRepository = cftSupportingDocTempRepository;
        this.supportingDocRepository = supportingDocRepository;
        this.otherFacilityInfoRepository = otherFacilityInfoRepository;
        this.otherFacilityInfoTempRepository = otherFacilityInfoTempRepository;
        this.cftInterestRateAudRepo = cftInterestRateAudRepo;
        this.cftVitalInfoAudRepo = cftVitalInfoAudRepo;
        this.cftCustomFacilityInfoAudRepo = cftCustomFacilityInfoAudRepo;
        this.cftSupportingDocAudRepo = cftSupportingDocAudRepo;
        this.cftOtherFacilityInfoAudRepo = cftOtherFacilityInfoAudRepo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Page<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplatesTemp(Pageable pageable) {

        Page<CreditFacilityTemplateTemp> creditFacilityTemplateTempList = creditFacilityTemplateTempRepository.findAll(pageable);
        Page<CreditFacilityTemplateDTO> pagedCreditFacilityTemplateDTOList = creditFacilityTemplateTempList.map(CreditFacilityTemplateDTO::new);

        StandardResponse<Page<CreditFacilityTemplateDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), pagedCreditFacilityTemplateDTOList);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateTempByID(Integer creditFacilityTemplateID) {

        CreditFacilityTemplateTemp creditFacilityTemplateTemp = creditFacilityTemplateTempRepository.findById(creditFacilityTemplateID).orElseThrow(() -> {
            throw new ApiRequestException("Credit Facility Template Temp with" + creditFacilityTemplateID + DOES_NOT_EXISTS);
        });

        CreditFacilityTemplateDTO creditFacilityTemplateDTO = new CreditFacilityTemplateDTO(creditFacilityTemplateTemp);

        StandardResponse<CreditFacilityTemplateDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Page<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplates(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        // Always stable ordering
        Sort idSort = Sort.by(Sort.Direction.ASC, "creditFacilityTemplateID");

        // 1) Priority set: ACT + APPROVED
        BooleanBuilder priorityPredicate = new BooleanBuilder();
        priorityPredicate.and(QCreditFacilityTemplate.creditFacilityTemplate.status.eq(AppsConstants.Status.ACT));
        priorityPredicate.and(QCreditFacilityTemplate.creditFacilityTemplate.approveStatus.eq(MasterDataApproveStatus.APPROVED));

        List<CreditFacilityTemplate> priorityAll =
                (List<CreditFacilityTemplate>) creditFacilityTemplateRepository.findAll(priorityPredicate, idSort);

        long priorityCount = priorityAll.size();
        int start = page * size;
        int end = Math.min(start + size, (int) priorityCount);

        List<CreditFacilityTemplate> pageContent = new ArrayList<>();

        // 2) Fill current page from priority first
        if (start < priorityCount) {
            pageContent.addAll(priorityAll.subList(start, end));
        }

        // 3) Fill remaining slots from non-priority
        int remaining = size - pageContent.size();
        if (remaining > 0) {
            BooleanBuilder nonPriorityPredicate = new BooleanBuilder();
            nonPriorityPredicate.and(
                    QCreditFacilityTemplate.creditFacilityTemplate.status.ne(AppsConstants.Status.ACT)
                            .or(QCreditFacilityTemplate.creditFacilityTemplate.approveStatus.ne(MasterDataApproveStatus.APPROVED))
            );

            int nonPriorityOffset = Math.max(0, start - (int) priorityCount);
            Pageable nonPriorityPageable = PageRequest.of(nonPriorityOffset / size, size, idSort);

            Page<CreditFacilityTemplate> nonPriorityPage =
                    creditFacilityTemplateRepository.findAll(nonPriorityPredicate, nonPriorityPageable);

            List<CreditFacilityTemplate> nonPriorityList = nonPriorityPage.getContent();
            if (!nonPriorityList.isEmpty()) {
                pageContent.addAll(nonPriorityList.subList(0, Math.min(remaining, nonPriorityList.size())));
            }
        }

        // 4) Total elements for proper page metadata
        long totalElements = creditFacilityTemplateRepository.count();

        Page<CreditFacilityTemplate> finalPage =
                new org.springframework.data.domain.PageImpl<>(pageContent, pageable, totalElements);

        Page<CreditFacilityTemplateDTO> dtoPage = finalPage.map(CreditFacilityTemplateDTO::new);

        StandardResponse<Page<CreditFacilityTemplateDTO>> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), dtoPage);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateByID(Integer creditFacilityTemplateID) {

        CreditFacilityTemplate creditFacilityTemplate = creditFacilityTemplateRepository.findById(creditFacilityTemplateID).orElseThrow(() -> {
            throw new ApiRequestException("Credit Facility Template  with" + creditFacilityTemplateID + DOES_NOT_EXISTS);
        });

        CreditFacilityTemplateDTO creditFacilityTemplateDTO = new CreditFacilityTemplateDTO(creditFacilityTemplate);

        StandardResponse<CreditFacilityTemplateDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> saveCreditFacilityTemplateTemp(CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {

        log.info("START: saveCreditFacilityTemplateTemp :{}", creditFacilityTemplateDTO);
        CreditFacilityTemplateTemp creditFacilityTemplateTemp;
        CreditFacilityTemplateTemp newCreditFacilityTemplateTemp = new CreditFacilityTemplateTemp();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QCreditFacilityTemplateTemp.creditFacilityTemplateTemp.creditFacilityName.eq(creditFacilityTemplateDTO.getCreditFacilityName()));
        List<CreditFacilityTemplateTemp> creditFacilityTemplateTempList = (List<CreditFacilityTemplateTemp>) creditFacilityTemplateTempRepository.findAll(booleanBuilder);

        BooleanBuilder booleanBuilderForMaster = new BooleanBuilder();
        booleanBuilderForMaster.and(QCreditFacilityTemplate.creditFacilityTemplate.creditFacilityName.eq(creditFacilityTemplateDTO.getCreditFacilityName()));
        List<CreditFacilityTemplate> creditFacilityTemplateList = (List<CreditFacilityTemplate>) creditFacilityTemplateRepository.findAll(booleanBuilderForMaster);

        if(creditFacilityTemplateTempList.isEmpty() && creditFacilityTemplateList.isEmpty()){

            Integer nextId = creditFacilityTemplateTempRepository.getNextSequenceValue();
            creditFacilityTemplateTemp = createCreditFacilityTemplateTempObj(creditFacilityTemplateDTO, true);

            Set<CftInterestRateTemp> cftInterestRateTempList = saveCtfInterestRateTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
            Set<CftVitalInfoTemp> cftVitalInfoTempList = saveCftVitalInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
            Set<CftCustomFacilityInfoTemp> customFacilityInfoTempList = saveCftCustomFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
            Set<CftSupportingDocTemp> cftSupportingDocTempList = saveCftSupportingDocTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
            Set<CftOtherFacilityInformationTemp> cftOtherFacilityInformationTempList  = saveCftOtherFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

            creditFacilityTemplateTemp.setCftInterestRates(cftInterestRateTempList);
            creditFacilityTemplateTemp.setCftVitalInfos(cftVitalInfoTempList);
            creditFacilityTemplateTemp.setCftCustomFacilityInfos(customFacilityInfoTempList);
            creditFacilityTemplateTemp.setCftSupportingDocs(cftSupportingDocTempList);
            creditFacilityTemplateTemp.setCftOtherFacilityInformations(cftOtherFacilityInformationTempList);

            creditFacilityTemplateTemp.setLastModifiedDate(new Date());

            creditFacilityTemplateTemp.setCreditFacilityTemplateID(nextId);
            newCreditFacilityTemplateTemp = creditFacilityTemplateTempRepository.saveAndFlush(creditFacilityTemplateTemp);

        }
        else if(!creditFacilityTemplateTempList.isEmpty()){
            throw new ApiRequestException("Credit Facility Template Name Already Exists in Temp Table");
        }
        else if(!creditFacilityTemplateList.isEmpty()){
            throw new ApiRequestException("Credit Facility Template Name Already Exists in Master Table");
        }
        else if(creditFacilityTemplateDTO.getCreditFacilityName() == null || creditFacilityTemplateDTO.getCreditFacilityName().isEmpty()){
            throw new ApiRequestException("Credit Facility Template Name cannot be empty or null.");
        }

        CreditFacilityTemplateDTO facilityTemplateDTO = new CreditFacilityTemplateDTO(newCreditFacilityTemplateTemp);
        StandardResponse<CreditFacilityTemplateDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), facilityTemplateDTO);
        return ResponseEntity.ok().body(response);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplateTemp(Integer creditFacilityTemplateID, CreditFacilityTemplateDTO creditFacilityTemplateDTO){

        CreditFacilityTemplateTemp newCreditFacilityTemplateTemp;

        CreditFacilityTemplateTemp existCreditFacilityTemplateTemp = creditFacilityTemplateTempRepository.findById(creditFacilityTemplateID).
                orElseThrow(() -> new ApiRequestException(NOT_FOUND_RECORD_MSG));

        Optional<CreditFacilityTemplate> optionalCreditFacilityTemplate = creditFacilityTemplateRepository.findById(creditFacilityTemplateID);

        if(!existCreditFacilityTemplateTemp.getCreditFacilityName().equals(creditFacilityTemplateDTO.getCreditFacilityName())){
            validateRoleNameUniqueness(creditFacilityTemplateDTO.getCreditFacilityName());

            CreditFacilityTemplateTemp finalExistCreditFacilityTemplateTemp = existCreditFacilityTemplateTemp;

            optionalCreditFacilityTemplate.ifPresent(creditFacilityTemplate -> {
                if(!creditFacilityTemplate.getCreditFacilityTemplateID().equals(finalExistCreditFacilityTemplateTemp.getCreditFacilityTemplateID())){
                    validateRoleNameUniqueness(creditFacilityTemplateDTO.getCreditFacilityName());
                }
            });
        }

        existCreditFacilityTemplateTemp.setCreditFacilityName(creditFacilityTemplateDTO.getCreditFacilityName());
        existCreditFacilityTemplateTemp.setDescription(creditFacilityTemplateDTO.getDescription());
        existCreditFacilityTemplateTemp.setMaxFacilityAmount(creditFacilityTemplateDTO.getMaxFacilityAmount());
        existCreditFacilityTemplateTemp.setMinFacilityAmount(creditFacilityTemplateDTO.getMinFacilityAmount());
        existCreditFacilityTemplateTemp.setShowCondition(creditFacilityTemplateDTO.getShowCondition());
        existCreditFacilityTemplateTemp.setShowPurpose(creditFacilityTemplateDTO.getShowPurpose());
        existCreditFacilityTemplateTemp.setShowRemark(creditFacilityTemplateDTO.getShowRemark());
        existCreditFacilityTemplateTemp.setShowCalculator(creditFacilityTemplateDTO.getShowCalculator());
        existCreditFacilityTemplateTemp.setShowRentalData(creditFacilityTemplateDTO.getShowRentalData());
        existCreditFacilityTemplateTemp.setShowRepayment(creditFacilityTemplateDTO.getShowRepayment());
        existCreditFacilityTemplateTemp.setStatus(creditFacilityTemplateDTO.getStatus());//draft
        existCreditFacilityTemplateTemp.setNewFacilityEmail(creditFacilityTemplateDTO.getNewFacilityEmail());
        existCreditFacilityTemplateTemp.setExistingFacilityEmail(creditFacilityTemplateDTO.getExistingFacilityEmail());
        existCreditFacilityTemplateTemp.setApproveStatus(creditFacilityTemplateDTO.getApproveStatus());

        if (creditFacilityTemplateDTO.getCreditFacilityTypeID() != null) {
            CreditFacilityType creditFacilityType = creditFacilityTypeDao.findById(creditFacilityTemplateDTO.getCreditFacilityTypeID()).orElseThrow(() ->
                    new ApiRequestException("Credit Facility Type with ID " + creditFacilityTemplateDTO.getCreditFacilityTypeID() + " does not exist."));

            existCreditFacilityTemplateTemp.setCreditFacilityType(creditFacilityType);
        }

        Set<CftInterestRateTemp> cftInterestRateTempList = saveCtfInterestRateTemp(creditFacilityTemplateDTO, existCreditFacilityTemplateTemp);
        Set<CftVitalInfoTemp> cftVitalInfoTempList = saveCftVitalInfoTemp(creditFacilityTemplateDTO, existCreditFacilityTemplateTemp);
        Set<CftCustomFacilityInfoTemp> customFacilityInfoTempList = saveCftCustomFacilityInfoTemp(creditFacilityTemplateDTO, existCreditFacilityTemplateTemp);
        Set<CftSupportingDocTemp> cftSupportingDocTempList = saveCftSupportingDocTemp(creditFacilityTemplateDTO, existCreditFacilityTemplateTemp);
        Set<CftOtherFacilityInformationTemp> cftOtherFacilityInformationTempList  = saveCftOtherFacilityInfoTemp(creditFacilityTemplateDTO, existCreditFacilityTemplateTemp);

        existCreditFacilityTemplateTemp.setCftInterestRates(cftInterestRateTempList);
        existCreditFacilityTemplateTemp.setCftVitalInfos(cftVitalInfoTempList);
        existCreditFacilityTemplateTemp.setCftCustomFacilityInfos(customFacilityInfoTempList);
        existCreditFacilityTemplateTemp.setCftSupportingDocs(cftSupportingDocTempList);
        existCreditFacilityTemplateTemp.setCftOtherFacilityInformations(cftOtherFacilityInformationTempList);

        newCreditFacilityTemplateTemp = creditFacilityTemplateTempRepository.saveAndFlush(existCreditFacilityTemplateTemp);

        CreditFacilityTemplateDTO facilityTemplateDTO = new CreditFacilityTemplateDTO(newCreditFacilityTemplateTemp);
        StandardResponse<CreditFacilityTemplateDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), facilityTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    public CreditFacilityTemplateTemp createCreditFacilityTemplateTempObj(CreditFacilityTemplateDTO creditFacilityTemplateDTO, Boolean isNew){
        Date date = new Date();
        CreditFacilityTemplateTemp creditFacilityTemplateTemp = new CreditFacilityTemplateTemp();

        if (isNew){
            creditFacilityTemplateTemp.setCreatedDate(date);
        }else {
            creditFacilityTemplateTemp.setCreatedDate(creditFacilityTemplateTemp.getCreatedDate());
        }

        creditFacilityTemplateTemp.setApproveStatus(creditFacilityTemplateDTO.getApproveStatus());
        creditFacilityTemplateTemp.setCreditFacilityName(creditFacilityTemplateDTO.getCreditFacilityName());
        creditFacilityTemplateTemp.setDescription(creditFacilityTemplateDTO.getDescription());
        creditFacilityTemplateTemp.setMaxFacilityAmount(creditFacilityTemplateDTO.getMaxFacilityAmount());
        creditFacilityTemplateTemp.setMinFacilityAmount(creditFacilityTemplateDTO.getMinFacilityAmount());
        creditFacilityTemplateTemp.setShowCondition(creditFacilityTemplateDTO.getShowCondition());
        creditFacilityTemplateTemp.setShowPurpose(creditFacilityTemplateDTO.getShowPurpose());
        creditFacilityTemplateTemp.setShowRemark(creditFacilityTemplateDTO.getShowRemark());
        creditFacilityTemplateTemp.setShowCalculator(creditFacilityTemplateDTO.getShowCalculator());
        creditFacilityTemplateTemp.setShowRentalData(creditFacilityTemplateDTO.getShowRentalData());
        creditFacilityTemplateTemp.setShowRepayment(creditFacilityTemplateDTO.getShowRepayment());
        creditFacilityTemplateTemp.setStatus(creditFacilityTemplateDTO.getStatus());
        creditFacilityTemplateTemp.setNewFacilityEmail(creditFacilityTemplateDTO.getNewFacilityEmail());
        creditFacilityTemplateTemp.setExistingFacilityEmail(creditFacilityTemplateDTO.getExistingFacilityEmail());

        if (creditFacilityTemplateDTO.getCreditFacilityTypeID() != null) {
            CreditFacilityType creditFacilityType = creditFacilityTypeDao.findById(creditFacilityTemplateDTO.getCreditFacilityTypeID()).orElseThrow(() ->
                    new ApiRequestException("Credit Facility Type Id with " + creditFacilityTemplateDTO.getCreditFacilityTypeID() + " does not exist")
            );
            creditFacilityTemplateTemp.setCreditFacilityType(creditFacilityType);
        }

        return creditFacilityTemplateTemp;
    }



    private CreditFacilityTemplateAud insertToAuditTable(CreditFacilityTemplateTemp creditFacilityTemplateTemp){
        CreditFacilityTemplateAud creditFacilityTemplateAud = new CreditFacilityTemplateAud();

        creditFacilityTemplateAud.setCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());
        creditFacilityTemplateAud.setCreditFacilityType(creditFacilityTemplateTemp.getCreditFacilityType().getCreditFacilityTypeID());
        creditFacilityTemplateAud.setApproveStatus(creditFacilityTemplateTemp.getApproveStatus());
        creditFacilityTemplateAud.setCreditFacilityName(creditFacilityTemplateTemp.getCreditFacilityName());
        creditFacilityTemplateAud.setDescription(creditFacilityTemplateTemp.getDescription());
        creditFacilityTemplateAud.setMaxFacilityAmount(creditFacilityTemplateTemp.getMaxFacilityAmount());
        creditFacilityTemplateAud.setMinFacilityAmount(creditFacilityTemplateTemp.getMinFacilityAmount());
        creditFacilityTemplateAud.setShowCondition(creditFacilityTemplateTemp.getShowCondition());
        creditFacilityTemplateAud.setShowPurpose(creditFacilityTemplateTemp.getShowPurpose());
        creditFacilityTemplateAud.setShowRemark(creditFacilityTemplateTemp.getShowRemark());
        creditFacilityTemplateAud.setShowCalculator(creditFacilityTemplateTemp.getShowCalculator());
        creditFacilityTemplateAud.setShowRentalData(creditFacilityTemplateTemp.getShowRentalData());
        creditFacilityTemplateAud.setShowRepayment(creditFacilityTemplateTemp.getShowRepayment());
        creditFacilityTemplateAud.setStatus(creditFacilityTemplateTemp.getStatus());
        creditFacilityTemplateAud.setExistingFacilityEmail(creditFacilityTemplateTemp.getExistingFacilityEmail());
        creditFacilityTemplateAud.setNewFacilityEmail(creditFacilityTemplateTemp.getNewFacilityEmail());

        return creditFacilityTemplateAudRepo.saveAndFlush(creditFacilityTemplateAud);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> authorizeCreditFacilityTemplate(ApproveRejectRQ approveRejectRQ) throws ApiRequestException {

        log.info("START: approveCreditFacilityTemplateDTO :{}", approveRejectRQ);

        Date date = new Date();

        if(approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null){
            throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
        }

        CreditFacilityTemplateTemp creditFacilityTemplateTemp = creditFacilityTemplateTempRepository.findById(approveRejectRQ.getApproveRejectDataID()).
                orElseThrow(() -> {throw new ApiRequestException(NOT_FOUND_RECORD_MSG);});

        Optional<CreditFacilityTemplate> optionalCreditFacilityTemplate = creditFacilityTemplateRepository.findById(creditFacilityTemplateTemp.getCreditFacilityTemplateID());
        CreditFacilityTemplate facilityTemplate = optionalCreditFacilityTemplate.orElse(null);

        creditFacilityTemplateTemp.setApprovedDate(date);
        creditFacilityTemplateTemp.setApproveStatus(approveRejectRQ.getApproveStatus());
        creditFacilityTemplateTemp.setApprovedBy(UserContext.getUsername());

        CreditFacilityTemplateTemp newCft = creditFacilityTemplateTempRepository.saveAndFlush(creditFacilityTemplateTemp);

        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response;

        if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.APPROVED)){
            response = handleApproval(newCft, facilityTemplate);
        }else if(approveRejectRQ.getApproveStatus() == MasterDataApproveStatus.REJECTED){
            response = handleRejection(newCft);
        } else {
            throw new ApiRequestException("Unknown approval status: "+ approveRejectRQ.getApproveStatus());
        }

        log.info("END: approveCreditFacilityTemplateDTO :{}", approveRejectRQ);
        return response;
    }

    private ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> handleApproval(CreditFacilityTemplateTemp creditFacilityTemplateTemp, CreditFacilityTemplate facilityTemplate) {

        CreditFacilityTemplateDTO creditFacilityTemplateDTO;

        if(facilityTemplate != null && facilityTemplate.getCreditFacilityTemplateID().equals(creditFacilityTemplateTemp.getCreditFacilityTemplateID())){
            creditFacilityTemplateDTO = updateCreditFacilityTemplateToMaster(creditFacilityTemplateTemp, facilityTemplate);
        } else {
            creditFacilityTemplateDTO = createCreditFacilityTemplateObj(creditFacilityTemplateTemp);
        }

        insertToAuditTable(creditFacilityTemplateTemp);
        creditFacilityTemplateTempRepository.delete(creditFacilityTemplateTemp);

        StandardResponse<CreditFacilityTemplateDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> handleRejection(CreditFacilityTemplateTemp newCft) {

        insertToAuditTable(newCft);

        CreditFacilityTemplateDTO creditFacilityTemplateDTO = new CreditFacilityTemplateDTO(newCft);
        StandardResponse<CreditFacilityTemplateDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    private CreditFacilityTemplateDTO createCreditFacilityTemplateObj(CreditFacilityTemplateTemp creditFacilityTemplateTemp){

        CreditFacilityTemplate creditFacilityTemplate = new CreditFacilityTemplate();

        creditFacilityTemplate.setCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());
        creditFacilityTemplate.setCreditFacilityName(creditFacilityTemplateTemp.getCreditFacilityName());
        creditFacilityTemplate.setCreditFacilityType(creditFacilityTemplateTemp.getCreditFacilityType());
        creditFacilityTemplate.setDescription(creditFacilityTemplateTemp.getDescription());
        creditFacilityTemplate.setMaxFacilityAmount(creditFacilityTemplateTemp.getMaxFacilityAmount());
        creditFacilityTemplate.setMinFacilityAmount(creditFacilityTemplateTemp.getMinFacilityAmount());
        creditFacilityTemplate.setStatus(creditFacilityTemplateTemp.getStatus());
        creditFacilityTemplate.setApprovedDate(creditFacilityTemplateTemp.getApprovedDate());
        creditFacilityTemplate.setShowPurpose(creditFacilityTemplateTemp.getShowPurpose());
        creditFacilityTemplate.setShowRepayment(creditFacilityTemplateTemp.getShowRepayment());
        creditFacilityTemplate.setShowCondition(creditFacilityTemplateTemp.getShowCondition());
        creditFacilityTemplate.setShowRemark(creditFacilityTemplateTemp.getShowRemark());
        creditFacilityTemplate.setShowRemark(creditFacilityTemplateTemp.getShowRemark());
        creditFacilityTemplate.setShowRentalData(creditFacilityTemplateTemp.getShowRentalData());
        creditFacilityTemplate.setShowCalculator(creditFacilityTemplateTemp.getShowCalculator());
        creditFacilityTemplate.setNewFacilityEmail(creditFacilityTemplateTemp.getNewFacilityEmail());
        creditFacilityTemplate.setExistingFacilityEmail(creditFacilityTemplateTemp.getExistingFacilityEmail());
        creditFacilityTemplate.setCreatedDate(creditFacilityTemplateTemp.getCreatedDate());
        creditFacilityTemplate.setApproveStatus(creditFacilityTemplateTemp.getApproveStatus());

       Set<CftInterestRate> cftInterestRateList = authorizeCftInterestRate(creditFacilityTemplateTemp.getCftInterestRates(), creditFacilityTemplate);
       creditFacilityTemplate.setCftInterestRates(cftInterestRateList);

       Set<CftVitalInfo> cftVitalInfoSet =  authorizeCftVitalInfo(creditFacilityTemplateTemp.getCftVitalInfos(), creditFacilityTemplate);
       creditFacilityTemplate.setCftVitalInfos(cftVitalInfoSet);

       Set<CftSupportingDoc> cftSupportingDocSet = authorizeCftSupportingDoc(creditFacilityTemplateTemp.getCftSupportingDocs(), creditFacilityTemplate);
       creditFacilityTemplate.setCftSupportingDocs(cftSupportingDocSet);

       Set<CftOtherFacilityInformation> otherFacilityInformationSet = authorizeCftOtherFacilityInformation(creditFacilityTemplateTemp.getCftOtherFacilityInformations(), creditFacilityTemplate);
       creditFacilityTemplate.setCftOtherFacilityInformations(otherFacilityInformationSet);

       Set<CftCustomFacilityInfo> cftCustomFacilityInfoSet = authorizeCftCustomFacilityInfo(creditFacilityTemplateTemp.getCftCustomFacilityInfos(), creditFacilityTemplate);
       creditFacilityTemplate.setCftCustomFacilityInfos(cftCustomFacilityInfoSet);

        creditFacilityTemplateRepository.save(creditFacilityTemplate);

       return new CreditFacilityTemplateDTO(creditFacilityTemplate);
    }

    private CreditFacilityTemplateDTO updateCreditFacilityTemplateToMaster(CreditFacilityTemplateTemp creditFacilityTemplateTemp, CreditFacilityTemplate facilityTemplate) {

        CreditFacilityTemplate creditFacilityTemplate = (facilityTemplate != null) ? facilityTemplate : new CreditFacilityTemplate();

        creditFacilityTemplate.setCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());
        creditFacilityTemplate.setCreditFacilityName(creditFacilityTemplateTemp.getCreditFacilityName());
        creditFacilityTemplate.setCreditFacilityType(creditFacilityTemplateTemp.getCreditFacilityType());
        creditFacilityTemplate.setDescription(creditFacilityTemplateTemp.getDescription());
        creditFacilityTemplate.setMaxFacilityAmount(creditFacilityTemplateTemp.getMaxFacilityAmount());
        creditFacilityTemplate.setMinFacilityAmount(creditFacilityTemplateTemp.getMinFacilityAmount());
        creditFacilityTemplate.setStatus(creditFacilityTemplateTemp.getStatus());
        creditFacilityTemplate.setApprovedDate(creditFacilityTemplateTemp.getApprovedDate());
        creditFacilityTemplate.setShowPurpose(creditFacilityTemplateTemp.getShowPurpose());
        creditFacilityTemplate.setShowRepayment(creditFacilityTemplateTemp.getShowRepayment());
        creditFacilityTemplate.setShowCondition(creditFacilityTemplateTemp.getShowCondition());
        creditFacilityTemplate.setShowRemark(creditFacilityTemplateTemp.getShowRemark());
        creditFacilityTemplate.setShowRemark(creditFacilityTemplateTemp.getShowRemark());
        creditFacilityTemplate.setShowRentalData(creditFacilityTemplateTemp.getShowRentalData());
        creditFacilityTemplate.setShowCalculator(creditFacilityTemplateTemp.getShowCalculator());
        creditFacilityTemplate.setNewFacilityEmail(creditFacilityTemplateTemp.getNewFacilityEmail());
        creditFacilityTemplate.setExistingFacilityEmail(creditFacilityTemplateTemp.getExistingFacilityEmail());
        creditFacilityTemplate.setCreatedDate(creditFacilityTemplateTemp.getCreatedDate());
        creditFacilityTemplate.setCreatedBy(creditFacilityTemplateTemp.getCreatedBy());
        creditFacilityTemplate.setLastModifiedDate(creditFacilityTemplateTemp.getLastModifiedDate());
        creditFacilityTemplate.setModifiedBy(creditFacilityTemplateTemp.getModifiedBy());
        creditFacilityTemplate.setApproveStatus(creditFacilityTemplateTemp.getApproveStatus());

        List<CftVitalInfo> cftVitalInfoList = cftVitalInfoRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplate.getCreditFacilityTemplateID());
        cftVitalInfoRepository.deleteAll(cftVitalInfoList);
        Set<CftVitalInfo> cftVitalInfoSet =  authorizeCftVitalInfo(creditFacilityTemplateTemp.getCftVitalInfos(), creditFacilityTemplate);
        creditFacilityTemplate.setCftVitalInfos(cftVitalInfoSet);

        List<CftInterestRate> cftInterestRateList = cftInterestRateRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplate.getCreditFacilityTemplateID());
        cftInterestRateRepository.deleteAll(cftInterestRateList);
        Set<CftInterestRate> cftInterestRateSet = authorizeCftInterestRate(creditFacilityTemplateTemp.getCftInterestRates(), creditFacilityTemplate);
        creditFacilityTemplate.setCftInterestRates(cftInterestRateSet);

        List<CftSupportingDoc> cftSupportingDocList = cftSupportingDocRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplate.getCreditFacilityTemplateID());
        cftSupportingDocRepository.deleteAll(cftSupportingDocList);
        Set<CftSupportingDoc> cftSupportingDocSet = authorizeCftSupportingDoc(creditFacilityTemplateTemp.getCftSupportingDocs(), creditFacilityTemplate);
        creditFacilityTemplate.setCftSupportingDocs(cftSupportingDocSet);

        List<CftOtherFacilityInformation> cftOtherFacilityInformationList = otherFacilityInfoRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplate.getCreditFacilityTemplateID());
        otherFacilityInfoRepository.deleteAll(cftOtherFacilityInformationList);
        Set<CftOtherFacilityInformation> otherFacilityInformationSet = authorizeCftOtherFacilityInformation(creditFacilityTemplateTemp.getCftOtherFacilityInformations(), creditFacilityTemplate);
        creditFacilityTemplate.setCftOtherFacilityInformations(otherFacilityInformationSet);

        List<CftCustomFacilityInfo> cftCustomFacilityInfoList = cftCustomFacilityInfoRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplate.getCreditFacilityTemplateID());
        cftCustomFacilityInfoRepository.deleteAll(cftCustomFacilityInfoList);
        Set<CftCustomFacilityInfo> cftCustomFacilityInfoSet = authorizeCftCustomFacilityInfo(creditFacilityTemplateTemp.getCftCustomFacilityInfos(), creditFacilityTemplate);
        creditFacilityTemplate.setCftCustomFacilityInfos(cftCustomFacilityInfoSet);

        creditFacilityTemplateRepository.save(creditFacilityTemplate);

        return new CreditFacilityTemplateDTO(creditFacilityTemplate);
    }

    private Set<CftVitalInfo> authorizeCftVitalInfo(Set<CftVitalInfoTemp> cftVitalInfoTemps, CreditFacilityTemplate creditFacilityTemplate){

        Set<CftVitalInfo> cftVitalInfoSet = new HashSet<>();

            for (CftVitalInfoTemp cftVitalInfoTemp: cftVitalInfoTemps) {
                CftVitalInfo vitalInfo = new CftVitalInfo();

                vitalInfo.setCftVitalInfoID(cftVitalInfoTemp.getCftVitalInfoID());
                vitalInfo.setCreditFacilityTemplate(creditFacilityTemplate);
                vitalInfo.setVitalInfoName(cftVitalInfoTemp.getVitalInfoName());
                vitalInfo.setMandatory(cftVitalInfoTemp.getMandatory());
                vitalInfo.setDisplayOrder(cftVitalInfoTemp.getDisplayOrder());
                vitalInfo.setStatus(cftVitalInfoTemp.getStatus());
                vitalInfo.setRecordStatus(cftVitalInfoTemp.getRecordStatus());

                cftVitalInfoSet.add(vitalInfo);

                insertToCftVitalInfoAuditTable(vitalInfo, null);

            }


        return cftVitalInfoSet;
    }

    private Set<CftInterestRate> authorizeCftInterestRate(Set<CftInterestRateTemp> cftInterestRateTemps, CreditFacilityTemplate creditFacilityTemplate){

        Set<CftInterestRate> cftInterestRateDTOList = new HashSet<>();

            for (CftInterestRateTemp interestRateTemp: cftInterestRateTemps) {
                CftInterestRate cftInterestRate = new CftInterestRate();

                cftInterestRate.setCftInterestRateID(interestRateTemp.getCftInterestRateID());
                cftInterestRate.setCreditFacilityTemplate(creditFacilityTemplate);
                cftInterestRate.setRateName(interestRateTemp.getRateName());
                cftInterestRate.setRateCode(interestRateTemp.getRateCode());
                cftInterestRate.setValue(interestRateTemp.getValue());
                cftInterestRate.setIsDefault(interestRateTemp.getIsDefault());
                cftInterestRate.setStatus(interestRateTemp.getStatus());
                cftInterestRate.setInterestRatingSubCategory(interestRateTemp.getInterestRatingSubCategory());
                cftInterestRate.setIsEditable(interestRateTemp.getIsEditable());

                cftInterestRate.setRecordStatus(interestRateTemp.getRecordStatus());

                cftInterestRateDTOList.add(cftInterestRate);

                insertToCftInterestRateAuditTable(cftInterestRate, null);
        }
        return cftInterestRateDTOList;
    }

    private Set<CftSupportingDoc> authorizeCftSupportingDoc(Set<CftSupportingDocTemp> cftSupportingDocTemps, CreditFacilityTemplate creditFacilityTemplate) {

        Set<CftSupportingDoc> cftSupportingDocSet = new HashSet<>();

        for (CftSupportingDocTemp cftSupportingDocTemp : cftSupportingDocTemps) {

            CftSupportingDoc cftSupportingDoc = new CftSupportingDoc();

            cftSupportingDoc.setCftSupportingDocID(cftSupportingDocTemp.getCftSupportingDocID());
            cftSupportingDoc.setCreditFacilityTemplate(creditFacilityTemplate);
            cftSupportingDoc.setSupportingDoc(cftSupportingDocTemp.getSupportingDoc());
            cftSupportingDoc.setMandatory(cftSupportingDocTemp.getMandatory());
            cftSupportingDoc.setStatus(cftSupportingDocTemp.getStatus());

            cftSupportingDoc.setRecordStatus(cftSupportingDocTemp.getRecordStatus());

            cftSupportingDocSet.add(cftSupportingDoc);

            insertToCftSupportingDocAuditTable(cftSupportingDoc, null);
        }

        return cftSupportingDocSet;
    }

    private Set<CftOtherFacilityInformation> authorizeCftOtherFacilityInformation(Set<CftOtherFacilityInformationTemp> cftOtherFacilityInformationTemps, CreditFacilityTemplate creditFacilityTemplate) {

        Set<CftOtherFacilityInformation> otherFacilityInformationSet = new HashSet<>();

        for (CftOtherFacilityInformationTemp cftOtherFacilityInformationTemp : cftOtherFacilityInformationTemps) {
            CftOtherFacilityInformation cftOtherFacilityInformation = new CftOtherFacilityInformation();

            cftOtherFacilityInformation.setCftOtherFacilityInfoID(cftOtherFacilityInformationTemp.getCftOtherFacilityInfoID());
            cftOtherFacilityInformation.setCreditFacilityTemplate(creditFacilityTemplate);
            cftOtherFacilityInformation.setOtherFacilityInfoName(cftOtherFacilityInformationTemp.getOtherFacilityInfoName());
            cftOtherFacilityInformation.setDescription(cftOtherFacilityInformationTemp.getDescription());
            cftOtherFacilityInformation.setOtherFacilityInfoCode(cftOtherFacilityInformationTemp.getOtherFacilityInfoCode());
            cftOtherFacilityInformation.setOtherFacilityInfoFieldType(cftOtherFacilityInformationTemp.getOtherFacilityInfoFieldType());
            cftOtherFacilityInformation.setDefaultValue(cftOtherFacilityInformationTemp.getDefaultValue());
            cftOtherFacilityInformation.setDisplayOrder(cftOtherFacilityInformationTemp.getDisplayOrder());
            cftOtherFacilityInformation.setMandatory(cftOtherFacilityInformationTemp.getMandatory());
            cftOtherFacilityInformation.setStatus(cftOtherFacilityInformationTemp.getStatus());
            cftOtherFacilityInformation.setRecordStatus(cftOtherFacilityInformationTemp.getRecordStatus());

            otherFacilityInformationSet.add(cftOtherFacilityInformation);

            insertToCftOtherFacilityInfoAuditTable(cftOtherFacilityInformation, null);
            }

        return otherFacilityInformationSet;

    }

    private Set<CftCustomFacilityInfo> authorizeCftCustomFacilityInfo(Set<CftCustomFacilityInfoTemp> cftCustomFacilityInfoTemps, CreditFacilityTemplate creditFacilityTemplate) {

        Set<CftCustomFacilityInfo> cftCustomFacilityInfoSet = new HashSet<>();

        for (CftCustomFacilityInfoTemp cftCustomFacilityInfoTemp : cftCustomFacilityInfoTemps) {
            CftCustomFacilityInfo cftCustomFacilityInfo = new CftCustomFacilityInfo();

            cftCustomFacilityInfo.setCftCustomFacilityInfoID(cftCustomFacilityInfoTemp.getCftCustomFacilityInfoID());
            cftCustomFacilityInfo.setCreditFacilityTemplate(creditFacilityTemplate);
            cftCustomFacilityInfo.setCustomFacilityInfoName(cftCustomFacilityInfoTemp.getCustomFacilityInfoName());
            cftCustomFacilityInfo.setDescription(cftCustomFacilityInfoTemp.getDescription());
            cftCustomFacilityInfo.setCustomFacilityInfoCode(cftCustomFacilityInfoTemp.getCustomFacilityInfoCode());
            cftCustomFacilityInfo.setFieldType(cftCustomFacilityInfoTemp.getFieldType());
            cftCustomFacilityInfo.setMandatory(cftCustomFacilityInfoTemp.getMandatory());
            cftCustomFacilityInfo.setStatus(cftCustomFacilityInfoTemp.getStatus());
            cftCustomFacilityInfo.setDisplayOrder(cftCustomFacilityInfoTemp.getDisplayOrder());
            cftCustomFacilityInfo.setRecordStatus(cftCustomFacilityInfoTemp.getRecordStatus());

            cftCustomFacilityInfoSet.add(cftCustomFacilityInfo);

            insertToCftCustomFacilityInfoAuditTable(cftCustomFacilityInfo, null);
            }

        return cftCustomFacilityInfoSet;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplate(Integer creditFacilityTemplateID, CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {

        CreditFacilityTemplateTemp creditFacilityTemplateTemp;

        CreditFacilityTemplate creditFacilityTemplate = creditFacilityTemplateRepository.findById(creditFacilityTemplateID).orElseThrow( () -> {
            throw new ApiRequestException("ROLE_WITH_ID" + creditFacilityTemplateID + DOES_NOT_EXISTS);
        });

        //create temp , save
        creditFacilityTemplateTemp = createCreditFacilityTemplateTempObj(creditFacilityTemplateDTO, false);
        creditFacilityTemplateTemp.setApproveStatus(creditFacilityTemplateDTO.getApproveStatus());
        creditFacilityTemplateTemp.setCreditFacilityTemplateID(creditFacilityTemplate.getCreditFacilityTemplateID());

        Set<CftInterestRateTemp> cftInterestRateTempList = saveCtfInterestRateTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
        Set<CftVitalInfoTemp> cftVitalInfoTempList = saveCftVitalInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
        Set<CftCustomFacilityInfoTemp> customFacilityInfoTempList = saveCftCustomFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
        Set<CftSupportingDocTemp> cftSupportingDocTempList = saveCftSupportingDocTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);
        Set<CftOtherFacilityInformationTemp> cftOtherFacilityInformationTempList = saveCftOtherFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        creditFacilityTemplateTemp.setCftInterestRates(cftInterestRateTempList);
        creditFacilityTemplateTemp.setCftVitalInfos(cftVitalInfoTempList);
        creditFacilityTemplateTemp.setCftCustomFacilityInfos(customFacilityInfoTempList);
        creditFacilityTemplateTemp.setCftSupportingDocs(cftSupportingDocTempList);
        creditFacilityTemplateTemp.setCftOtherFacilityInformations(cftOtherFacilityInformationTempList);

        CreditFacilityTemplateTemp newCreditFacilityTemplateTemp = creditFacilityTemplateTempRepository.saveAndFlush(creditFacilityTemplateTemp);

        CreditFacilityTemplateDTO facilityTemplateDTO = new CreditFacilityTemplateDTO(newCreditFacilityTemplateTemp);

        StandardResponse<CreditFacilityTemplateDTO> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), facilityTemplateDTO);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Void>> deleteCreditFacilityTemplateTemp(Integer creditFacilityTemplateID) {

        CreditFacilityTemplateTemp existCreditFacilityTemplateTemp = creditFacilityTemplateTempRepository.findById(creditFacilityTemplateID).
                orElseThrow(() -> {throw new ApiRequestException(NOT_FOUND_RECORD_MSG);});

        if(existCreditFacilityTemplateTemp != null){
            creditFacilityTemplateTempRepository.delete(existCreditFacilityTemplateTemp);
        }

        StandardResponse<Void> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), creditFacilityTemplateID);
        return ResponseEntity.ok().body(response);
    }

    public Set<CftInterestRateTemp> saveCtfInterestRateTemp(CreditFacilityTemplateDTO creditFacilityTemplateDTO, CreditFacilityTemplateTemp creditFacilityTemplateTemp ){
        Set<CftInterestRateTemp> cftInterestRateTempList = new HashSet<>();

        if(creditFacilityTemplateDTO.getIsCftInterestRateDTOListChange().equals(AppsConstants.YesNo.Y)){

            List<CftInterestRateTemp> existingCftInterestRateTempList = cftInterestRateTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());

            if(!existingCftInterestRateTempList.isEmpty()){
                cftInterestRateTempRepository.deleteAll(existingCftInterestRateTempList);
            }

            for (CftInterestRateDTO cftInterestRateDTO : creditFacilityTemplateDTO.getCftInterestRateDTOList()) {

                    CftInterestRateTemp interestRateTemp = new CftInterestRateTemp();
                    Integer nextId = cftInterestRateTempRepository.getNextSequenceValue();
                    interestRateTemp.setCftInterestRateID(nextId);

                    interestRateTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
                    interestRateTemp.setRateName(cftInterestRateDTO.getRateName());
                    interestRateTemp.setRateCode(cftInterestRateDTO.getRateCode());
                    interestRateTemp.setIsDefault(cftInterestRateDTO.getIsDefault());
                    interestRateTemp.setValue(cftInterestRateDTO.getValue());
                    interestRateTemp.setStatus(cftInterestRateDTO.getStatus());
                    interestRateTemp.setInterestRatingSubCategory(cftInterestRateDTO.getInterestRatingSubCategory());
                    interestRateTemp.setIsEditable(cftInterestRateDTO.getIsEditable());
                    interestRateTemp.setRecordStatus(cftInterestRateDTO.getRecordStatus());

                    cftInterestRateTempList.add(interestRateTemp);
            }
        }

        return cftInterestRateTempList;

    }

    public Boolean insertToCftInterestRateAuditTable(CftInterestRate cftInterestRate, CftInterestRateTemp cftInterestRateTemp){

        CftInterestRateAud cftInterestRateAud = new CftInterestRateAud();

        if (cftInterestRateTemp != null && cftInterestRate == null){
            cftInterestRateAud.setCftInterestRateID(cftInterestRateTemp.getCftInterestRateID());
            cftInterestRateAud.setCreditFacilityTemplate(cftInterestRateTemp.getCreditFacilityTemplate() != null ?
                    cftInterestRateTemp.getCreditFacilityTemplate().getCreditFacilityTemplateID() : null);
            cftInterestRateAud.setRateName(cftInterestRateTemp.getRateName());
            cftInterestRateAud.setRateCode(cftInterestRateTemp.getRateCode());
            cftInterestRateAud.setValue(cftInterestRateTemp.getValue());
            cftInterestRateAud.setIsDefault(cftInterestRateTemp.getIsDefault());
            cftInterestRateAud.setStatus(cftInterestRateTemp.getStatus());
            cftInterestRateAud.setInterestRatingSubCategory(cftInterestRateTemp.getInterestRatingSubCategory());
            cftInterestRateAud.setIsEditable(cftInterestRateTemp.getIsEditable());
            cftInterestRateAud.setCreatedDate(cftInterestRateTemp.getCreatedDate());
            cftInterestRateAud.setLastModifiedDate(cftInterestRateTemp.getLastModifiedDate());
            cftInterestRateAud.setRecordStatus(cftInterestRateTemp.getRecordStatus());
        }else {
            cftInterestRateAud.setCftInterestRateID(cftInterestRate.getCftInterestRateID());
            cftInterestRateAud.setCreditFacilityTemplate(cftInterestRate.getCreditFacilityTemplate() != null ?
                    cftInterestRate.getCreditFacilityTemplate().getCreditFacilityTemplateID() : null);
            cftInterestRateAud.setRateName(cftInterestRate.getRateName());
            cftInterestRateAud.setRateCode(cftInterestRate.getRateCode());
            cftInterestRateAud.setValue(cftInterestRate.getValue());
            cftInterestRateAud.setIsDefault(cftInterestRate.getIsDefault());
            cftInterestRateAud.setStatus(cftInterestRate.getStatus());
            cftInterestRateAud.setInterestRatingSubCategory(cftInterestRate.getInterestRatingSubCategory());
            cftInterestRateAud.setIsEditable(cftInterestRate.getIsEditable());
            cftInterestRateAud.setCreatedDate(cftInterestRate.getCreatedDate());
            cftInterestRateAud.setLastModifiedDate(cftInterestRate.getLastModifiedDate());
            cftInterestRateAud.setRecordStatus(cftInterestRate.getRecordStatus());
        }

        cftInterestRateAud.setId(null);

          cftInterestRateAudRepo.saveAndFlush(cftInterestRateAud);

        return true ;
    }

    public Set<CftVitalInfoTemp> saveCftVitalInfoTemp(CreditFacilityTemplateDTO creditFacilityTemplateDTO, CreditFacilityTemplateTemp creditFacilityTemplateTemp){
        Date date = new Date();

        Set<CftVitalInfoTemp> cftVitalInfoTempList = new HashSet<>();

        if(creditFacilityTemplateDTO.getIsCftVitalInfoDTOListChange().equals(AppsConstants.YesNo.Y)){

            List<CftVitalInfoTemp> existingCftVitalInfoTempList = cftVitalInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());

            if(!existingCftVitalInfoTempList.isEmpty()){
                cftVitalInfoTempRepository.deleteAll(existingCftVitalInfoTempList);
            }

            for (CftVitalInfoDTO cftVitalInfoDTO : creditFacilityTemplateDTO.getCftVitalInfoDTOList()) {

                    CftVitalInfoTemp cftVitalInfoTemp = new CftVitalInfoTemp();

                    Integer nextId = cftVitalInfoTempRepository.getNextSequenceValue();
                    cftVitalInfoTemp.setCftVitalInfoID(nextId);
                    cftVitalInfoTemp.setCreatedDate(date);
                    cftVitalInfoTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
                    cftVitalInfoTemp.setVitalInfoName(cftVitalInfoDTO.getVitalInfoName());
                    cftVitalInfoTemp.setMandatory(cftVitalInfoDTO.getMandatory());
                    cftVitalInfoTemp.setDisplayOrder(cftVitalInfoDTO.getDisplayOrder());
                    cftVitalInfoTemp.setStatus(cftVitalInfoDTO.getStatus());
                    cftVitalInfoTemp.setRecordStatus(cftVitalInfoDTO.getRecordStatus());

                    cftVitalInfoTempList.add(cftVitalInfoTemp);
            }
        }


        return cftVitalInfoTempList;
    }

    public Boolean insertToCftVitalInfoAuditTable(CftVitalInfo cftVitalInfo, CftVitalInfoTemp cftVitalInfoTemp ){

        CftVitalInfoAud cftVitalInfoAud = new CftVitalInfoAud();

        if(cftVitalInfoTemp != null && cftVitalInfo == null){
            cftVitalInfoAud.setCftVitalInfoID(cftVitalInfoTemp.getCftVitalInfoID());
            cftVitalInfoAud.setCreditFacilityTemplate(cftVitalInfoTemp.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftVitalInfoAud.setVitalInfoName(cftVitalInfoTemp.getVitalInfoName());
            cftVitalInfoAud.setMandatory(cftVitalInfoTemp.getMandatory());
            cftVitalInfoAud.setDisplayOrder(cftVitalInfoTemp.getDisplayOrder());
            cftVitalInfoAud.setStatus(cftVitalInfoTemp.getStatus());
            cftVitalInfoAud.setCreatedDate(cftVitalInfoTemp.getCreatedDate());
            cftVitalInfoAud.setLastModifiedDate(cftVitalInfoTemp.getLastModifiedDate());
            cftVitalInfoAud.setRecordStatus(cftVitalInfoTemp.getRecordStatus());
        }else {
            cftVitalInfoAud.setCftVitalInfoID(cftVitalInfo.getCftVitalInfoID());
            cftVitalInfoAud.setCreditFacilityTemplate(cftVitalInfo.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftVitalInfoAud.setVitalInfoName(cftVitalInfo.getVitalInfoName());
            cftVitalInfoAud.setMandatory(cftVitalInfo.getMandatory());
            cftVitalInfoAud.setDisplayOrder(cftVitalInfo.getDisplayOrder());
            cftVitalInfoAud.setStatus(cftVitalInfo.getStatus());
            cftVitalInfoAud.setCreatedDate(cftVitalInfo.getCreatedDate());
            cftVitalInfoAud.setLastModifiedDate(cftVitalInfo.getLastModifiedDate());
            cftVitalInfoAud.setRecordStatus(cftVitalInfo.getRecordStatus());
        }

        cftVitalInfoAud.setId(null);

        cftVitalInfoAudRepo.saveAndFlush(cftVitalInfoAud);

        return true ;
    }

    public Set<CftCustomFacilityInfoTemp> saveCftCustomFacilityInfoTemp(CreditFacilityTemplateDTO creditFacilityTemplateDTO, CreditFacilityTemplateTemp creditFacilityTemplateTemp){
        Date date = new Date();
        Set<CftCustomFacilityInfoTemp> customFacilityInfoTempList = new HashSet<>();

        if(creditFacilityTemplateDTO.getIsCftCustomFacilityInfoDTOListChange().equals(AppsConstants.YesNo.Y)) {

            List<CftCustomFacilityInfoTemp> existingCftCustomFacilityInfoTempList = cftCustomFacilityInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());

            if(!existingCftCustomFacilityInfoTempList.isEmpty()){
                cftCustomFacilityInfoTempRepository.deleteAll(existingCftCustomFacilityInfoTempList);
            }

            for (CftCustomFacilityInfoDTO cftCustomFacilityInfoDTO : creditFacilityTemplateDTO.getCftCustomFacilityInfoDTOList()) {

                CftCustomFacilityInfoTemp cftCustomFacilityInfoTemp = new CftCustomFacilityInfoTemp();

                Integer nextId = cftCustomFacilityInfoTempRepository.getNextSequenceValue();
                cftCustomFacilityInfoTemp.setCftCustomFacilityInfoID(nextId);
                cftCustomFacilityInfoTemp.setCreatedDate(date);

                cftCustomFacilityInfoTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
                cftCustomFacilityInfoTemp.setCustomFacilityInfoName(cftCustomFacilityInfoDTO.getCustomFacilityInfoName());
                cftCustomFacilityInfoTemp.setCustomFacilityInfoCode(cftCustomFacilityInfoDTO.getCustomFacilityInfoCode());
                cftCustomFacilityInfoTemp.setDescription(cftCustomFacilityInfoDTO.getDescription());
                cftCustomFacilityInfoTemp.setFieldType(cftCustomFacilityInfoDTO.getFieldType());
                cftCustomFacilityInfoTemp.setMandatory(cftCustomFacilityInfoDTO.getMandatory());
                cftCustomFacilityInfoTemp.setStatus(cftCustomFacilityInfoDTO.getStatus());
                cftCustomFacilityInfoTemp.setDisplayOrder(cftCustomFacilityInfoDTO.getDisplayOrder());
                cftCustomFacilityInfoTemp.setRecordStatus(cftCustomFacilityInfoDTO.getRecordStatus());

                customFacilityInfoTempList.add(cftCustomFacilityInfoTemp);
            }
        }
        return customFacilityInfoTempList;
    }

    public Boolean insertToCftCustomFacilityInfoAuditTable(CftCustomFacilityInfo cftCustomFacilityInfo, CftCustomFacilityInfoTemp cftCustomFacilityInfoTemp){

        CftCustomFacilityInfoAud cftVitalInfoAud = new CftCustomFacilityInfoAud();

        if (cftCustomFacilityInfoTemp != null && cftCustomFacilityInfo == null){
            cftVitalInfoAud.setCftCustomFacilityInfoID(cftCustomFacilityInfoTemp.getCftCustomFacilityInfoID());
            cftVitalInfoAud.setCreditFacilityTemplate(cftCustomFacilityInfoTemp.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftVitalInfoAud.setCustomFacilityInfoName(cftCustomFacilityInfoTemp.getCustomFacilityInfoName());
            cftVitalInfoAud.setDescription(cftCustomFacilityInfoTemp.getDescription());
            cftVitalInfoAud.setCustomFacilityInfoCode(cftCustomFacilityInfoTemp.getCustomFacilityInfoCode());
            cftVitalInfoAud.setFieldType(cftCustomFacilityInfoTemp.getFieldType());
            cftVitalInfoAud.setMandatory(cftCustomFacilityInfoTemp.getMandatory());
            cftVitalInfoAud.setStatus(cftCustomFacilityInfoTemp.getStatus());
            cftVitalInfoAud.setDisplayOrder(cftCustomFacilityInfoTemp.getDisplayOrder());
            cftVitalInfoAud.setCreatedDate(cftCustomFacilityInfoTemp.getCreatedDate());
            cftVitalInfoAud.setLastModifiedDate(cftCustomFacilityInfoTemp.getLastModifiedDate());
            cftVitalInfoAud.setRecordStatus(cftCustomFacilityInfoTemp.getRecordStatus());
        }else {
            cftVitalInfoAud.setCftCustomFacilityInfoID(cftCustomFacilityInfo.getCftCustomFacilityInfoID());
            cftVitalInfoAud.setCreditFacilityTemplate(cftCustomFacilityInfo.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftVitalInfoAud.setCustomFacilityInfoName(cftCustomFacilityInfo.getCustomFacilityInfoName());
            cftVitalInfoAud.setDescription(cftCustomFacilityInfo.getDescription());
            cftVitalInfoAud.setCustomFacilityInfoCode(cftCustomFacilityInfo.getCustomFacilityInfoCode());
            cftVitalInfoAud.setFieldType(cftCustomFacilityInfo.getFieldType());
            cftVitalInfoAud.setMandatory(cftCustomFacilityInfo.getMandatory());
            cftVitalInfoAud.setStatus(cftCustomFacilityInfo.getStatus());
            cftVitalInfoAud.setDisplayOrder(cftCustomFacilityInfo.getDisplayOrder());
            cftVitalInfoAud.setCreatedDate(cftCustomFacilityInfo.getCreatedDate());
            cftVitalInfoAud.setLastModifiedDate(cftCustomFacilityInfo.getLastModifiedDate());
            cftVitalInfoAud.setRecordStatus(cftCustomFacilityInfo.getRecordStatus());
        }

        cftVitalInfoAud.setId(null);

        cftCustomFacilityInfoAudRepo.saveAndFlush(cftVitalInfoAud);
        return true ;
    }

    public Set<CftSupportingDocTemp> saveCftSupportingDocTemp(CreditFacilityTemplateDTO creditFacilityTemplateDTO, CreditFacilityTemplateTemp creditFacilityTemplateTemp){
        Date date = new Date();
        Set<CftSupportingDocTemp> cftSupportingDocTempList = new HashSet<>();
        if(creditFacilityTemplateDTO.getIsCftSupportingDocDTOListChange().equals(AppsConstants.YesNo.Y)){

            List<CftSupportingDocTemp> existingCftSupportingDocTempList = cftSupportingDocTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());

            if(!existingCftSupportingDocTempList.isEmpty()){
                cftSupportingDocTempRepository.deleteAll(existingCftSupportingDocTempList);
            }

            for (CftSupportingDocDTO cftSupportingDocDTO : creditFacilityTemplateDTO.getCftSupportingDocDTOList()) {

                SupportingDoc supportingDoc = supportingDocRepository.findById(cftSupportingDocDTO.getSupportingDocID()).orElseThrow(() ->
                        new ApiRequestException("Supporting Doc with ID " + cftSupportingDocDTO.getSupportingDocID() + " does not exist."));


                CftSupportingDocTemp cftSupportingDocTemp = new CftSupportingDocTemp();
                Integer nextId = cftSupportingDocTempRepository.getNextSequenceValue();
                cftSupportingDocTemp.setCreatedDate(date);
                cftSupportingDocTemp.setCftSupportingDocID(nextId);
                cftSupportingDocTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
                cftSupportingDocTemp.setMandatory(cftSupportingDocDTO.getMandatory());
                cftSupportingDocTemp.setSupportingDoc(supportingDoc);
                cftSupportingDocTemp.setStatus(cftSupportingDocDTO.getStatus());
                cftSupportingDocTemp.setRecordStatus(cftSupportingDocDTO.getRecordStatus());

                cftSupportingDocTempList.add(cftSupportingDocTemp);

            }
        }

        return cftSupportingDocTempList;
    }

    public Boolean insertToCftSupportingDocAuditTable(CftSupportingDoc cftSupportingDoc, CftSupportingDocTemp cftSupportingDocTemp){

        CftSupportingDocAud cftSupportingDocAud = new CftSupportingDocAud();

        if (cftSupportingDocTemp != null && cftSupportingDoc == null){
            cftSupportingDocAud.setCftSupportingDocID(cftSupportingDocTemp.getCftSupportingDocID());
            cftSupportingDocAud.setCreditFacilityTemplate(cftSupportingDocTemp.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftSupportingDocAud.setSupportingDoc(cftSupportingDocTemp.getSupportingDoc().getSupportingDocID());
            cftSupportingDocAud.setMandatory(cftSupportingDocTemp.getMandatory());
            cftSupportingDocAud.setStatus(cftSupportingDocTemp.getStatus());
            cftSupportingDocAud.setCreatedDate(cftSupportingDocTemp.getCreatedDate());
            cftSupportingDocAud.setLastModifiedDate(cftSupportingDocTemp.getLastModifiedDate());
            cftSupportingDocAud.setRecordStatus(cftSupportingDocTemp.getRecordStatus());
        }else {
            cftSupportingDocAud.setCftSupportingDocID(cftSupportingDoc.getCftSupportingDocID());
            cftSupportingDocAud.setCreditFacilityTemplate(cftSupportingDoc.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftSupportingDocAud.setSupportingDoc(cftSupportingDoc.getSupportingDoc().getSupportingDocID());
            cftSupportingDocAud.setMandatory(cftSupportingDoc.getMandatory());
            cftSupportingDocAud.setStatus(cftSupportingDoc.getStatus());
            cftSupportingDocAud.setCreatedDate(cftSupportingDoc.getCreatedDate());
            cftSupportingDocAud.setLastModifiedDate(cftSupportingDoc.getLastModifiedDate());
            cftSupportingDocAud.setRecordStatus(cftSupportingDoc.getRecordStatus());
        }

        cftSupportingDocAud.setId(null);

        cftSupportingDocAudRepo.saveAndFlush(cftSupportingDocAud);

        return true ;
    }

    public Set<CftOtherFacilityInformationTemp> saveCftOtherFacilityInfoTemp(CreditFacilityTemplateDTO creditFacilityTemplateDTO, CreditFacilityTemplateTemp creditFacilityTemplateTemp){
        Date date = new Date();
        Set<CftOtherFacilityInformationTemp> cftOtherFacilityInformationTempList = new HashSet<>();

        if(creditFacilityTemplateDTO.getIsCftOtherFacilityInfoDTOListChange().equals(AppsConstants.YesNo.Y)){

            List<CftOtherFacilityInformationTemp> existingCftOtherFacilityInformationTempList = otherFacilityInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(creditFacilityTemplateTemp.getCreditFacilityTemplateID());

            if(!existingCftOtherFacilityInformationTempList.isEmpty()){
                otherFacilityInfoTempRepository.deleteAll(existingCftOtherFacilityInformationTempList);
            }

            for (CftOtherFacilityInfoDTO facilityInfoDTO : creditFacilityTemplateDTO.getCftOtherFacilityInfoDTOList()) {

                CftOtherFacilityInformationTemp cftOtherFacilityInformationTemp = new CftOtherFacilityInformationTemp();

                    Integer nextId = otherFacilityInfoTempRepository.getNextSequenceValue();
                    cftOtherFacilityInformationTemp.setCreatedDate(date);
                    cftOtherFacilityInformationTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
                    cftOtherFacilityInformationTemp.setCftOtherFacilityInfoID(nextId);
                    cftOtherFacilityInformationTemp.setOtherFacilityInfoCode(facilityInfoDTO.getOtherFacilityInfoCode());
                    cftOtherFacilityInformationTemp.setOtherFacilityInfoName(facilityInfoDTO.getOtherFacilityInfoName());
                    cftOtherFacilityInformationTemp.setDescription(facilityInfoDTO.getDescription());
                    cftOtherFacilityInformationTemp.setOtherFacilityInfoFieldType(facilityInfoDTO.getOtherFacilityInfoFieldType());
                    cftOtherFacilityInformationTemp.setDefaultValue(facilityInfoDTO.getDefaultValue());
                    cftOtherFacilityInformationTemp.setDisplayOrder(facilityInfoDTO.getDisplayOrder());
                    cftOtherFacilityInformationTemp.setMandatory(facilityInfoDTO.getMandatory());
                    cftOtherFacilityInformationTemp.setStatus(facilityInfoDTO.getStatus());
                    cftOtherFacilityInformationTemp.setRecordStatus(facilityInfoDTO.getRecordStatus());

                    cftOtherFacilityInformationTempList.add(cftOtherFacilityInformationTemp);

            }
        }

        return cftOtherFacilityInformationTempList;
    }

    public Boolean insertToCftOtherFacilityInfoAuditTable(CftOtherFacilityInformation cftOtherFacilityInformation, CftOtherFacilityInformationTemp cftOtherFacilityInformationTemp){

        CftOtherFacilityInformationAud cftOtherFacilityInformationAud = new CftOtherFacilityInformationAud();

        if (cftOtherFacilityInformationTemp != null && cftOtherFacilityInformation == null){
            cftOtherFacilityInformationAud.setCftOtherFacilityInfoID(cftOtherFacilityInformationTemp.getCftOtherFacilityInfoID());
            cftOtherFacilityInformationAud.setCreditFacilityTemplate(cftOtherFacilityInformationTemp.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftOtherFacilityInformationAud.setOtherFacilityInfoName(cftOtherFacilityInformationTemp.getOtherFacilityInfoName());
            cftOtherFacilityInformationAud.setDescription(cftOtherFacilityInformationTemp.getDescription());
            cftOtherFacilityInformationAud.setOtherFacilityInfoCode(cftOtherFacilityInformationTemp.getOtherFacilityInfoCode());
            cftOtherFacilityInformationAud.setOtherFacilityInfoFieldType(cftOtherFacilityInformationTemp.getOtherFacilityInfoFieldType());
            cftOtherFacilityInformationAud.setDefaultValue(cftOtherFacilityInformationTemp.getDefaultValue());
            cftOtherFacilityInformationAud.setDisplayOrder(cftOtherFacilityInformationTemp.getDisplayOrder());
            cftOtherFacilityInformationAud.setMandatory(cftOtherFacilityInformationTemp.getMandatory());
            cftOtherFacilityInformationAud.setStatus(cftOtherFacilityInformationTemp.getStatus());
            cftOtherFacilityInformationAud.setCreatedDate(cftOtherFacilityInformationTemp.getCreatedDate());
            cftOtherFacilityInformationAud.setLastModifiedDate(cftOtherFacilityInformationTemp.getLastModifiedDate());
            cftOtherFacilityInformationAud.setRecordStatus(cftOtherFacilityInformationTemp.getRecordStatus());
        }
        else {
            cftOtherFacilityInformationAud.setCftOtherFacilityInfoID(cftOtherFacilityInformation.getCftOtherFacilityInfoID());
            cftOtherFacilityInformationAud.setCreditFacilityTemplate(cftOtherFacilityInformation.getCreditFacilityTemplate().getCreditFacilityTemplateID());
            cftOtherFacilityInformationAud.setOtherFacilityInfoName(cftOtherFacilityInformation.getOtherFacilityInfoName());
            cftOtherFacilityInformationAud.setDescription(cftOtherFacilityInformation.getDescription());
            cftOtherFacilityInformationAud.setOtherFacilityInfoCode(cftOtherFacilityInformation.getOtherFacilityInfoCode());
            cftOtherFacilityInformationAud.setOtherFacilityInfoFieldType(cftOtherFacilityInformation.getOtherFacilityInfoFieldType());
            cftOtherFacilityInformationAud.setDefaultValue(cftOtherFacilityInformation.getDefaultValue());
            cftOtherFacilityInformationAud.setDisplayOrder(cftOtherFacilityInformation.getDisplayOrder());
            cftOtherFacilityInformationAud.setMandatory(cftOtherFacilityInformation.getMandatory());
            cftOtherFacilityInformationAud.setStatus(cftOtherFacilityInformation.getStatus());
            cftOtherFacilityInformationAud.setCreatedDate(cftOtherFacilityInformation.getCreatedDate());
            cftOtherFacilityInformationAud.setLastModifiedDate(cftOtherFacilityInformation.getLastModifiedDate());
            cftOtherFacilityInformationAud.setRecordStatus(cftOtherFacilityInformation.getRecordStatus());
        }

        cftOtherFacilityInformationAud.setId(null);

        cftOtherFacilityInfoAudRepo.saveAndFlush(cftOtherFacilityInformationAud);
        return true ;
    }


    public void validateRoleNameUniqueness(String roleName) throws ApiRequestException{

        BooleanBuilder tempQuery = new BooleanBuilder(QCreditFacilityTemplateTemp.creditFacilityTemplateTemp.creditFacilityName.eq(roleName));
        if (creditFacilityTemplateTempRepository.exists(tempQuery)) {
            throw new ApiRequestException("Credit Facility Template Name Already Exists in Temporary Records");
        }

        BooleanBuilder masterQuery = new BooleanBuilder(QCreditFacilityTemplate.creditFacilityTemplate.creditFacilityName.eq(roleName));
        if (creditFacilityTemplateRepository.exists(masterQuery)) {
            throw new ApiRequestException("Credit Facility Template Name Already Exists in Master Records");
        }
    }

}
