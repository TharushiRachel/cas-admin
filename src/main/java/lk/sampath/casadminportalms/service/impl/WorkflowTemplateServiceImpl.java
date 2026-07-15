package lk.sampath.casadminportalms.service.impl;

import java.util.*;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkFlowTemplateDataDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lk.sampath.casadminportalms.entity.workflowtemplate.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupRepository;
import lk.sampath.casadminportalms.repository.workflowtemplate.*;
import lk.sampath.casadminportalms.service.WorkflowTemplateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class WorkflowTemplateServiceImpl implements WorkflowTemplateService {

  private final WorkflowTemplateRepository workflowTemplateRepository;
  private final WorkflowTemplateDataRepository workflowTemplateDataRepository;
  private final WorkflowTemplateTempRepository workflowTemplateTempRepository;
  private final WorkflowTemplateDataTempRepository workflowTemplateDataTempRepository;
  private final WorkflowTemplateAudRepository workflowTemplateAudRepository;
  private final WorkflowTemplateDataAudRepository workflowTemplateDataAudRepository;
  private final UpmGroupRepository upmGroupRepository;

  @Autowired
  public WorkflowTemplateServiceImpl(
      WorkflowTemplateRepository workflowTemplateRepository,
      WorkflowTemplateTempRepository workflowTemplateTempRepository,
      WorkflowTemplateDataTempRepository workflowTemplateDataTempRepository,
      WorkflowTemplateDataRepository workflowTemplateDataRepository,
      UpmGroupRepository upmGroupRepository,
      WorkflowTemplateAudRepository workflowTemplateAudRepository,
      WorkflowTemplateDataAudRepository workflowTemplateDataAudRepository) {
    this.workflowTemplateRepository = workflowTemplateRepository;
    this.workflowTemplateTempRepository = workflowTemplateTempRepository;
    this.workflowTemplateDataTempRepository = workflowTemplateDataTempRepository;
    this.workflowTemplateDataRepository = workflowTemplateDataRepository;
    this.workflowTemplateAudRepository = workflowTemplateAudRepository;
    this.workflowTemplateDataAudRepository = workflowTemplateDataAudRepository;
    this.upmGroupRepository = upmGroupRepository;
  }

  public WorkflowTemplateTemp workflowTemplateMapper(WorkflowTemplateDTO workflowTemplateDTO) {
    return WorkflowTemplateTemp.builder()
        .workFlowTemplateId(workflowTemplateDTO.getWorkFlowTemplateId())
        .workFlowTemplateName(workflowTemplateDTO.getWorkFlowTemplateName())
        .code(workflowTemplateDTO.getCode())
        .description(workflowTemplateDTO.getDescription())
        .status(workflowTemplateDTO.getStatus())
        .build();
  }

  public WorkflowTemplateDataTemp workflowTemplateMapper(
      WorkFlowTemplateDataDTO workFlowTemplateDataDTO, Integer id) {
    WorkflowTemplateTemp workFlowTemplate = new WorkflowTemplateTemp();
    UpmGroup upmGroup = new UpmGroup();
    UpmGroup nextUpmGroup = null;
    UpmGroup perviousUpmGroup = null;
    if (workFlowTemplateDataDTO.getWorkFlowTemplateId() != null) {
      workFlowTemplate =
          workflowTemplateTempRepository.getReferenceById(
              workFlowTemplateDataDTO.getWorkFlowTemplateId());
    } else {
      workFlowTemplate.setWorkFlowTemplateId(id);
    }
    if (workFlowTemplateDataDTO.getUpmGroupID() != null) {
      upmGroup = upmGroupRepository.getReferenceById(workFlowTemplateDataDTO.getUpmGroupID());
    }
    if (workFlowTemplateDataDTO.getNextUPMGroupId() != null) {
      nextUpmGroup =
          upmGroupRepository.getReferenceById(workFlowTemplateDataDTO.getNextUPMGroupId());
    }
    if (workFlowTemplateDataDTO.getPreviousUPMGroupId() != null) {
      perviousUpmGroup =
          upmGroupRepository.getReferenceById(workFlowTemplateDataDTO.getPreviousUPMGroupId());
    }
    return WorkflowTemplateDataTemp.builder()
        .workFlowTemplateTempDataId(workFlowTemplateDataDTO.getWorkFlowTemplateDataId())
        .workflowTemplateTemp(workFlowTemplate)
        .upmGroup(upmGroup)
        .nextUPMGroup(nextUpmGroup)
        .previousUPMGroup(perviousUpmGroup)
        .displayOrder(workFlowTemplateDataDTO.getDisplayOrder())
        .build();
  }

  public WorkflowTemplate workflowTemplateMapper(WorkflowTemplateTemp workflowTemplateTemp) {
    return WorkflowTemplate.builder()
        .workFlowTemplateId(workflowTemplateTemp.getWorkFlowTemplateId())
        .workFlowTemplateName(workflowTemplateTemp.getWorkFlowTemplateName())
        .code(workflowTemplateTemp.getCode())
        .description(workflowTemplateTemp.getDescription())
        .status(workflowTemplateTemp.getStatus())
        .build();
  }

  @Override
  public StandardResponse<List<UpmGroupDTO>> getAllApprovedUPMGroups() {
    List<UpmGroupDTO> result = new ArrayList<>();
    List<UpmGroup> approvedUPMGroup = upmGroupRepository.findAllApprovedUpmGroups();
    if (!approvedUPMGroup.isEmpty()) {
      approvedUPMGroup.forEach(
          rec -> {
            UpmGroupDTO upmGroupDTO = new UpmGroupDTO();
            BeanUtils.copyProperties(rec, upmGroupDTO);
            result.add(upmGroupDTO);
          });
    }
    return new StandardResponse<>(
        ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), result);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
  public StandardResponse<String> saveOrUpdateTempWorkflowTemplate(WorkflowTemplateDTO request)
      throws ApiRequestException {
    try {
      log.info("START saveOrUpdateTempWorkflowTemplate : {}", request);

      if (request == null
          || request.getWorkFlowTemplateName() == null
          || request.getWorkFlowTemplateName().trim().isEmpty()
          || request.getCode() == null
          || request.getCode().trim().isEmpty()
          || request.getWorkFlowTemplateDataDTOList() == null
          || request.getWorkFlowTemplateDataDTOList().isEmpty()) {
        throw new ApiRequestException("Template Name, Template Code and UPM Level are required.");
      }

      WorkflowTemplateTemp result = null;
      Date date = new Date();
      Integer duplicateCount =
          workflowTemplateTempRepository.getWorkflowTempCountByName(
              request.getWorkFlowTemplateName());
      boolean isNewWorkFlowTemplate = request.getWorkFlowTemplateId() == null;
      Integer nextId = workflowTemplateTempRepository.getNextSequenceValue();

      List<WorkflowTemplateDataTemp> workflowTemplateDataTempList = new ArrayList<>();

      WorkflowTemplateTemp workflowTemplateTemp = workflowTemplateMapper(request);
      workflowTemplateTemp.setCreatedDate(date);
      workflowTemplateTemp.setApproveStatus(MasterDataApproveStatus.PENDING);

      if (isNewWorkFlowTemplate) {
        workflowTemplateTemp.setWorkFlowTemplateId(nextId);
        if (duplicateCount > 0) {
          throw new ApiRequestException("Already Found");
        }
      } else {
        workflowTemplateTemp.setWorkFlowTemplateId(request.getWorkFlowTemplateId());
      }

      if (!request.getWorkFlowTemplateDataDTOList().isEmpty()) {
        request
            .getWorkFlowTemplateDataDTOList()
            .forEach(
                rec -> {
                  boolean isNewWFData =
                      request.getWorkFlowTemplateId() == null
                          || rec.getWorkFlowTemplateDataId() == null;
                  if (isNewWFData && !rec.isRemoved()) {
                    Integer nextDataId = workflowTemplateDataTempRepository.getNextSequenceValue();
                    WorkflowTemplateDataTemp workflowTemplateDataTemp =
                        workflowTemplateMapper(rec, nextId);
                    workflowTemplateDataTemp.setWorkFlowTemplateTempDataId(nextDataId);
                    workflowTemplateDataTemp.setWorkflowTemplateTemp(workflowTemplateTemp);
                    //
                    // workflowTemplateDataTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
                    workflowTemplateDataTempList.add(workflowTemplateDataTemp);
                  }
                });
      }
      workflowTemplateTemp.setWorkFlowTemplateDataSet(workflowTemplateDataTempList);
      workflowTemplateTempRepository.saveAndFlush(workflowTemplateTemp);
      return new StandardResponse<>(
          ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), result);
    } catch (Exception e) {
      log.error("Error in saveOrUpdateTempWorkflowTemplate : {}", e);
      throw e;
    }
  }

  @Override
  public StandardResponse<Boolean> authorizeWorkflowTemplateTemp(ApproveRejectRQ approveRejectRQ)
      throws ApiRequestException {
    try {
      Date date = new Date();

      WorkflowTemplateTemp workflowTemplateTemp =
          workflowTemplateTempRepository
              .findById(approveRejectRQ.getApproveRejectDataID())
              .orElseThrow(() -> new ApiRequestException("Not Found"));

      if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.APPROVED)) {
        Integer nextId = workflowTemplateRepository.getNextSequenceValue();
        WorkflowTemplate workflowTemplate = workflowTemplateMapper(workflowTemplateTemp);
        workflowTemplate.setCreatedDate(date);
        workflowTemplate.setApproveStatus(approveRejectRQ.getApproveStatus());
        workflowTemplate.setApprovedDate(date);
        workflowTemplate.setApprovedBy("User");
        workflowTemplate.setWorkFlowTemplateId(nextId);
        workflowTemplate.setWorkFlowTemplateDataSet(
            getTempWorkflowTemplateData(workflowTemplateTemp, workflowTemplate));

        workflowTemplateRepository.saveAndFlush(workflowTemplate);
        // audit (parent, child)
        insertToAuditTableFromMaster(workflowTemplate);
        // delete temp
        workflowTemplateTempRepository.delete(workflowTemplateTemp);
      } else if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.REJECTED)) {
        workflowTemplateTemp.setApproveStatus(MasterDataApproveStatus.REJECTED);
        workflowTemplateTempRepository.saveAndFlush(workflowTemplateTemp);
      }
      log.info("END: approveCreditFacilityTemplateDTO :{}", approveRejectRQ);
      return new StandardResponse<>(
          ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), true);
    } catch (Exception e) {
      log.error("ERROR : error in authorizeWorkflowTemplateTemp :{}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResponseEntity<StandardResponse<Void>> deleteWorkFlowTempById(Integer approveRejectDataID)
      throws ApiRequestException {
    try {
      workflowTemplateTempRepository.deleteById(approveRejectDataID);
      StandardResponse<Void> response =
          new StandardResponse<>(
              ErrorEnums.SUCCESS_CODE.getStatus(),
              ErrorEnums.SUCCESS_CODE.getLabel(),
              approveRejectDataID);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      log.error("ERROR : error in deleteWorkFlowTempById :{}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public StandardResponse<WorkflowTemplateResponse> getTempWorkflowTemplate(Pageable pageable)
      throws ApiRequestException {
    try {
      WorkflowTemplateResponse result = new WorkflowTemplateResponse();
      List<WorkflowTemplateDTO> workflowTemplateTempList = new ArrayList<>();
      Page<WorkflowTemplateTemp> pageDetails =
          this.workflowTemplateTempRepository.findAllWorkflowTemplateTemp(pageable);

      if (pageDetails.hasContent()) {
        pageDetails
            .getContent()
            .forEach(
                rec -> {
                  List<WorkFlowTemplateDataDTO> childArray = new ArrayList<>();
                  List<WorkflowTemplateDataTemp> workflowTemplateTempDataList =
                      this.workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(
                          rec.getWorkFlowTemplateId());

                  if (!workflowTemplateTempDataList.isEmpty()) {
                    workflowTemplateTempDataList.forEach(
                        recChild -> {
                          WorkFlowTemplateDataDTO workFlowTemplateDataDTO =
                              WorkFlowTemplateDataDTO.builder()
                                  .workFlowTemplateDataId(recChild.getWorkFlowTemplateTempDataId())
                                  .workFlowTemplateId(rec.getWorkFlowTemplateId())
                                  .upmGroup(recChild.getUpmGroup())
                                  .nextUPMGroup(recChild.getNextUPMGroup())
                                  .previousUPMGroup(recChild.getPreviousUPMGroup())
                                  .displayOrder(recChild.getDisplayOrder())
                                  .workflowTemplateTemp(null)
                                  .build();
                          childArray.add(workFlowTemplateDataDTO);
                        });
                  }

                  WorkflowTemplateDTO workflowTemplateDTO =
                      WorkflowTemplateDTO.builder()
                          .workFlowTemplateId(rec.getWorkFlowTemplateId())
                          .workFlowTemplateName(rec.getWorkFlowTemplateName())
                          .code(rec.getCode())
                          .description(rec.getDescription())
                          .status(rec.getStatus())
                          .approveStatus(rec.getApproveStatus())
                          .workFlowTemplateDataDTOList(childArray)
                          .build();

                  workflowTemplateTempList.add(workflowTemplateDTO);
                });
      }

      result.setDataList(workflowTemplateTempList);
      result.setCount(pageDetails.getTotalElements());
      return new StandardResponse<>(
          ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), result);
    } catch (Exception e) {
      log.error("ERROR : in getTempWorkflowTemplate : {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public StandardResponse<Page<WorkflowTemplateDTO>> getWorkflowTemplate(Pageable pageable)
      throws ApiRequestException {
    try {
      WorkflowTemplateResponse result = new WorkflowTemplateResponse();
      List<WorkflowTemplateDTO> workflowTemplateTempList = new ArrayList<>();

      Page<WorkflowTemplate> pageDetails =
          this.workflowTemplateRepository.findAllWorkflowTemplate(pageable);

      if (pageDetails.hasContent()) {
        pageDetails
            .getContent()
            .forEach(
                rec -> {
                  List<WorkFlowTemplateDataDTO> childArray = new ArrayList<>();
                  List<WorkflowTemplateData> workflowTemplateTempDataList =
                      this.workflowTemplateDataRepository.findAllWorkflowTemplateData(
                          rec.getWorkFlowTemplateId());

                  if (!workflowTemplateTempDataList.isEmpty()) {
                    workflowTemplateTempDataList.forEach(
                        recChild -> {
                          WorkFlowTemplateDataDTO workFlowTemplateDataDTO =
                              WorkFlowTemplateDataDTO.builder()
                                  .workFlowTemplateDataId(recChild.getWorkFlowTemplateDataID())
                                  .workFlowTemplateId(rec.getWorkFlowTemplateId())
                                  .upmGroup(recChild.getUpmGroup())
                                  .nextUPMGroup(recChild.getNextUPMGroup())
                                  .previousUPMGroup(recChild.getPreviousUPMGroup())
                                  .displayOrder(recChild.getDisplayOrder())
                                  .workflowTemplateTemp(null)
                                  .build();
                          childArray.add(workFlowTemplateDataDTO);
                        });
                  }
                  WorkflowTemplateDTO workflowTemplateDTO =
                      WorkflowTemplateDTO.builder()
                          .workFlowTemplateId(rec.getWorkFlowTemplateId())
                          .workFlowTemplateName(rec.getWorkFlowTemplateName())
                          .code(rec.getCode())
                          .description(rec.getDescription())
                          .status(rec.getStatus())
                          .approveStatus(rec.getApproveStatus())
                          .workFlowTemplateDataDTOList(childArray)
                          .build();

                  workflowTemplateTempList.add(workflowTemplateDTO);
                });
      }
      Page<WorkflowTemplateDTO> dtoPage =
          new PageImpl<>(workflowTemplateTempList, pageable, pageDetails.getTotalElements());
      return new StandardResponse<>(
          ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), dtoPage);
    } catch (Exception e) {
      log.error("ERROR in getWorkflowTemplate :{}", e.getMessage());
      throw e;
    }
  }

  public Set<WorkflowTemplateData> getTempWorkflowTemplateData(
      WorkflowTemplateTemp workflowTemplateTemp, WorkflowTemplate workflowTemplate) {
    try {
      Set<WorkflowTemplateData> workflowTemplateDataSet = new HashSet<>();

      if (!workflowTemplateTemp.getWorkFlowTemplateDataSet().isEmpty()) {
        for (WorkflowTemplateDataTemp workflowTemplateDataTemp :
            workflowTemplateTemp.getWorkFlowTemplateDataSet()) {
          WorkflowTemplateData workflowTemplateData = new WorkflowTemplateData();
          Integer nextId = workflowTemplateDataTempRepository.getNextSequenceValue();
          workflowTemplateData.setWorkFlowTemplateDataID(nextId);
          workflowTemplateData.setWorkFlowTemplate(workflowTemplate);
          workflowTemplateData.setUpmGroup(workflowTemplateDataTemp.getUpmGroup());
          workflowTemplateData.setNextUPMGroup(workflowTemplateDataTemp.getNextUPMGroup());
          workflowTemplateData.setPreviousUPMGroup(workflowTemplateDataTemp.getPreviousUPMGroup());
          workflowTemplateData.setDisplayOrder(workflowTemplateDataTemp.getDisplayOrder());
          workflowTemplateDataSet.add(workflowTemplateData);
        }
      }
      return workflowTemplateDataSet;
    } catch (Exception e) {
      log.error("ERROR in getTempWorkflowTemplateData :{}", e.getMessage());
      throw e;
    }
  }

  public void insertToAuditTableFromMaster(WorkflowTemplate workflowTemplate) {
    try {
      WorkflowTemplateAud workflowTemplateAud = new WorkflowTemplateAud();
      workflowTemplateAud.setWorkFlowTemplateId(workflowTemplate.getWorkFlowTemplateId());
      workflowTemplateAud.setWorkFlowTemplateName(workflowTemplate.getWorkFlowTemplateName());
      workflowTemplateAud.setCode(workflowTemplate.getCode());
      workflowTemplateAud.setStatus(workflowTemplate.getStatus());
      workflowTemplateAud.setDescription(workflowTemplate.getDescription());
      workflowTemplateAudRepository.saveAndFlush(workflowTemplateAud);

      if (!workflowTemplate.getWorkFlowTemplateDataSet().isEmpty()) {
        for (WorkflowTemplateData workflowTemplateData :
            workflowTemplate.getWorkFlowTemplateDataSet()) {
          WorkflowTemplateDataAud workflowTemplateDataAud = new WorkflowTemplateDataAud();
          workflowTemplateDataAud.setWorkFlowTemplateDataID(
              workflowTemplateData.getWorkFlowTemplateDataID());
          workflowTemplateDataAud.setWorkFlowTemplate(workflowTemplateData.getWorkFlowTemplate());
          workflowTemplateDataAud.setUpmGroup(workflowTemplateData.getUpmGroup());
          workflowTemplateDataAud.setNextUPMGroup(workflowTemplateData.getNextUPMGroup());
          workflowTemplateDataAud.setPreviousUPMGroup(workflowTemplateData.getPreviousUPMGroup());
          workflowTemplateDataAud.setDisplayOrder(workflowTemplateData.getDisplayOrder());
          workflowTemplateDataAudRepository.saveAndFlush(workflowTemplateDataAud);
        }
      }
    } catch (Exception e) {
      log.error("ERROR in insertToAuditTableFromMaster :{}", e.getMessage());
      throw e;
    }
  }
}
