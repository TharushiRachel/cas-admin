package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkFlowTemplateDataDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplate;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateData;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateDataTemp;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateTemp;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upmgroup.UpmGroupRepository;
import lk.sampath.casadminportalms.repository.workflowtemplate.*;
import lk.sampath.casadminportalms.service.impl.WorkflowTemplateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class WorkflowTemplateServiceImplTest {

  @Mock WorkflowTemplateRepository workflowTemplateRepository;

  @Mock WorkflowTemplateDataRepository workflowTemplateDataRepository;

  @Mock WorkflowTemplateTempRepository workflowTemplateTempRepository;

  @Mock WorkflowTemplateDataTempRepository workflowTemplateDataTempRepository;

  @Mock WorkflowTemplateAudRepository workflowTemplateAudRepository;

  @Mock WorkflowTemplateDataAudRepository workflowTemplateDataAudRepository;

  @Mock UpmGroupRepository upmGroupRepository;

  @InjectMocks WorkflowTemplateServiceImpl workflowTemplateServiceImpl;

  @Mock WorkflowTemplateDTO newWorkflowTemplateDTO;

  @Mock WorkflowTemplateDTO existingWorkflowTemplateDTO;

  @Mock UpmGroup upmGroup;

  @Mock UpmGroupDTO upmGroupDTO;

  @Mock WorkFlowTemplateDataDTO workFlowTemplateDataDTO;

  @Mock WorkflowTemplateTemp workflowTemplateTemp;
  @Mock ApproveRejectRQ approveRejectRQ;

  @Mock WorkflowTemplateDataTemp workflowTemplateDataTemp;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    newWorkflowTemplateDTO =
        WorkflowTemplateDTO.builder()
            .workFlowTemplateId(null)
            .workFlowTemplateName("New Template")
            .code("")
            .description("")
            .workFlowTemplateDataDTOList(new ArrayList<>())
            .build();

    existingWorkflowTemplateDTO =
        WorkflowTemplateDTO.builder()
            .workFlowTemplateId(1)
            .workFlowTemplateName("Existing Template")
            .code("")
            .description("")
            .workFlowTemplateDataDTOList(new ArrayList<>())
            .build();

    workFlowTemplateDataDTO =
        WorkFlowTemplateDataDTO.builder()
            .workFlowTemplateDataID(1)
            .workFlowTemplateId(1)
            .upmGroupID(1)
            .nextUPMGroup(null)
            .upmGroup(null)
            .previousUPMGroup(null)
            .previousUPMGroupID(1)
            .displayOrder(1)
            .removed(false)
            .build();

    upmGroup = new UpmGroup();
    upmGroup.setUpmGroupID(1);
    upmGroup.setGroupCode("A");
    upmGroup.setReferenceName("A");
    upmGroup.setWorkFlowLevel(10);
    upmGroup.setStatus(Status.ACT);

    upmGroupDTO = new UpmGroupDTO();
    upmGroupDTO.setUpmGroupID(1);
    upmGroupDTO.setGroupCode("A");
    upmGroupDTO.setReferenceName("A");
    upmGroupDTO.setWorkFlowLevel(10);
    upmGroupDTO.setStatus(Status.ACT);

    workflowTemplateDataTemp =
        WorkflowTemplateDataTemp.builder()
            .workFlowTemplateTempDataId(1)
            .workflowTemplateTemp(null)
            .upmGroup(null)
            .nextUPMGroup(null)
            .previousUPMGroup(null)
            .displayOrder(1)
            .build();

    workflowTemplateTemp =
        WorkflowTemplateTemp.builder()
            .workFlowTemplateId(1)
            .workFlowTemplateName("Template")
            .code("")
            .description("")
            .status(Status.ACT)
            .workFlowTemplateDataSet(List.of(workflowTemplateDataTemp))
            .build();

    approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.PENDING);
  }

  @Test
  void testGetAllApprovedUPMGroups() {
    List<UpmGroup> mockList = List.of(new UpmGroup(), new UpmGroup());
    List<UpmGroupDTO> mockCommiteeTypeList = List.of(new UpmGroupDTO(), new UpmGroupDTO());
    when(upmGroupRepository.findAllApprovedUpmGroups()).thenReturn(mockList);
    StandardResponse<List<UpmGroupDTO>> response =
        workflowTemplateServiceImpl.getAllApprovedUPMGroups();
    verify(upmGroupRepository, times(1)).findAllApprovedUpmGroups();
    assertNotNull(response);
    assertEquals(
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(),
            ErrorEnums.SUCCESS_CODE.getLabel(),
            mockCommiteeTypeList),
        response);
  }

  @Test
  void testSaveOrUpdateTempWorkflowTemplate() {
    newWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of(workFlowTemplateDataDTO));
    when(workflowTemplateTempRepository.getWorkflowTempCountByName("BCC")).thenReturn(0);
    when(workflowTemplateTempRepository.getNextSequenceValue()).thenReturn(1001);
    workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(newWorkflowTemplateDTO);

    newWorkflowTemplateDTO.setWorkFlowTemplateId(null);
    newWorkflowTemplateDTO.setWorkFlowTemplateName("BCC");

    when(workflowTemplateTempRepository.getWorkflowTempCountByName("BCC")).thenReturn(1);
    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(newWorkflowTemplateDTO);
            });
    assertEquals("Already Found", exception.getMessage());
    newWorkflowTemplateDTO.setWorkFlowTemplateId(1);
    newWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of());
    workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(newWorkflowTemplateDTO);

    newWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of(workFlowTemplateDataDTO));
    workFlowTemplateDataDTO.setWorkFlowTemplateDataID(null);
    workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(newWorkflowTemplateDTO);
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp() {
    Date date = new Date();
    when(workflowTemplateTempRepository.findById(10))
        .thenReturn(Optional.ofNullable(workflowTemplateTemp));
    ApiRequestException exception =
        assertThrows(
            ApiRequestException.class,
            () -> {
              workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);
            });
    assertEquals("Not Found", exception.getMessage());

    when(workflowTemplateTempRepository.findById(1))
        .thenReturn(Optional.ofNullable(workflowTemplateTemp));
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.PENDING);
    StandardResponse<Boolean> response =
        workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);
    assertEquals(
        new StandardResponse<>(
            ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), true),
        response);

    when(workflowTemplateRepository.getNextSequenceValue()).thenReturn(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    WorkflowTemplate workflowTemplate =
        workflowTemplateServiceImpl.workflowTemplateMapper(workflowTemplateTemp);
    workflowTemplate.setCreatedDate(date);
    workflowTemplate.setApproveStatus(approveRejectRQ.getApproveStatus());
    workflowTemplate.setApprovedDate(date);
    workflowTemplate.setApprovedBy("User");
    workflowTemplate.setWorkFlowTemplateId(1);
    workflowTemplate.setWorkFlowTemplateDataSet(
        workflowTemplateServiceImpl.getTempWorkflowTemplateData(workflowTemplateTemp));

    workflowTemplateServiceImpl.insertToAuditTableFromMaster(workflowTemplate);
    workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);

    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);
    workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);
  }

  @Test
  void testGetTempWorkflowTemplate() throws ApiRequestException {
    // Setup variables
    int pageNo = 0;
    int pageSize = 10;
    Pageable pageable = PageRequest.of(pageNo, pageSize);

    // Case 1: When there is content in the repository
    WorkflowTemplateTemp workflowTemplateTemp = new WorkflowTemplateTemp();
    workflowTemplateTemp.setWorkFlowTemplateId(1);
    workflowTemplateTemp.setWorkFlowTemplateName("Test Template");
    workflowTemplateTemp.setCode("T001");
    workflowTemplateTemp.setDescription("Test description");
    workflowTemplateTemp.setStatus(Status.ACT);
    workflowTemplateTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

    List<WorkflowTemplateTemp> templateList = new ArrayList<>();
    templateList.add(workflowTemplateTemp);
    Page<WorkflowTemplateTemp> pageWithContent = Mockito.mock(Page.class);
    Mockito.when(pageWithContent.hasContent()).thenReturn(true);
    Mockito.when(pageWithContent.getContent()).thenReturn(templateList);
    Mockito.when(workflowTemplateTempRepository.findAllWorkflowTemplateTemp(pageable))
        .thenReturn(pageWithContent);

    // Child data mock
    WorkflowTemplateDataTemp childData = new WorkflowTemplateDataTemp();
    childData.setWorkFlowTemplateTempDataId(1);
    childData.setUpmGroup(null);
    childData.setNextUPMGroup(null);
    childData.setPreviousUPMGroup(null);
    childData.setDisplayOrder(1);

    List<WorkflowTemplateDataTemp> childDataList = new ArrayList<>();
    childDataList.add(childData);

    assertNotNull(childDataList);
    assertEquals(1, childDataList.size());

    Mockito.when(
            workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(
                (int) Mockito.anyLong()))
        .thenReturn(childDataList);

    workflowTemplateServiceImpl.getTempWorkflowTemplate(pageNo, pageSize);
  }

  @Test
  void testGetWorkflowTemplate() throws ApiRequestException {
    // Mock data
    WorkflowTemplate template = new WorkflowTemplate();
    template.setWorkFlowTemplateId(1);
    template.setWorkFlowTemplateName("Template 1");
    template.setCode("T1");
    template.setDescription("Description 1");
    template.setStatus(Status.ACT);
    template.setApproveStatus(MasterDataApproveStatus.APPROVED);

    WorkflowTemplateData templateData = new WorkflowTemplateData();
    templateData.setWorkFlowTemplateDataID(100);
    templateData.setUpmGroup(null);
    templateData.setNextUPMGroup(null);
    templateData.setPreviousUPMGroup(null);
    templateData.setDisplayOrder(1);

    // Mock behavior
    Page<WorkflowTemplate> page = new PageImpl<>(Collections.singletonList(template));
    when(workflowTemplateRepository.findAllWorkflowTemplate(any(Pageable.class))).thenReturn(page);
    when(workflowTemplateDataRepository.findAllWorkflowTemplateData(1))
        .thenReturn(Collections.singletonList(templateData));

    // Call the method
    StandardResponse<List<WorkflowTemplateDTO>> response =
        workflowTemplateServiceImpl.getWorkflowTemplate(0, 10);

    // Assertions
    assertNotNull(response);
    assertEquals(true, response.getSuccess());
    assertNotNull(response.getResponse());

    // Verify repository interactions
    verify(workflowTemplateRepository, times(1)).findAllWorkflowTemplate(any(Pageable.class));

    when(workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(1))
        .thenReturn(List.of(workflowTemplateDataTemp));
    Pageable pageable = PageRequest.of(0, 10);

    Page<WorkflowTemplateTemp> pageWithContent = Mockito.mock(Page.class);
    Mockito.when(pageWithContent.hasContent()).thenReturn(true);
    when(pageWithContent.getContent()).thenReturn(List.of());

    Mockito.when(workflowTemplateTempRepository.findAllWorkflowTemplateTemp(pageable))
        .thenReturn(pageWithContent);
    workflowTemplateServiceImpl.getWorkflowTemplate(0, 10);
  }
}
