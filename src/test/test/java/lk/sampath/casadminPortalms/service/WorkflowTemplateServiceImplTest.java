package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkFlowTemplateDataDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplate;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateAud;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
                    .code("T-NEW")
                    .description("")
                    .workFlowTemplateDataDTOList(new ArrayList<>())
                    .build();

    existingWorkflowTemplateDTO =
            WorkflowTemplateDTO.builder()
                    .workFlowTemplateId(1)
                    .workFlowTemplateName("Existing Template")
                    .code("T-EXIST")
                    .description("")
                    .workFlowTemplateDataDTOList(new ArrayList<>())
                    .build();

    workFlowTemplateDataDTO =
            WorkFlowTemplateDataDTO.builder()
                    .workFlowTemplateDataId(1)
                    .workFlowTemplateId(1)
                    .upmGroupID(1)
                    .nextUPMGroup(null)
                    .upmGroup(null)
                    .previousUPMGroup(null)
                    .previousUPMGroupId(1)
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
  void testGetAllApprovedUPMGroups_EmptyList_ReturnsEmptyResult() {
    when(upmGroupRepository.findAllApprovedUpmGroups()).thenReturn(Collections.emptyList());

    StandardResponse<List<UpmGroupDTO>> response =
            workflowTemplateServiceImpl.getAllApprovedUPMGroups();

    assertNotNull(response);
    assertEquals(true, response.getSuccess());
    assertNotNull(response.getResponse());
    assertTrue(((List<UpmGroupDTO>) response.getResponse()).isEmpty());
    verify(upmGroupRepository, times(1)).findAllApprovedUpmGroups();
  }

  @Test
  void testGetAllApprovedUPMGroups_MapsFieldsCorrectly() {
    when(upmGroupRepository.findAllApprovedUpmGroups()).thenReturn(List.of(upmGroup));

    StandardResponse<List<UpmGroupDTO>> response =
            workflowTemplateServiceImpl.getAllApprovedUPMGroups();

    assertNotNull(response);
    List<UpmGroupDTO> mappedList = (List<UpmGroupDTO>) response.getResponse();
    assertEquals(1, mappedList.size());
    UpmGroupDTO mapped = mappedList.get(0);
    assertEquals(upmGroup.getUpmGroupID(), mapped.getUpmGroupID());
    assertEquals(upmGroup.getGroupCode(), mapped.getGroupCode());
    assertEquals(upmGroup.getReferenceName(), mapped.getReferenceName());
    assertEquals(upmGroup.getWorkFlowLevel(), mapped.getWorkFlowLevel());
    assertEquals(upmGroup.getStatus(), mapped.getStatus());
  }

  @Test
  void testGetAllApprovedUPMGroups_MultipleGroups_PreservesCount() {
    UpmGroup secondGroup = new UpmGroup();
    secondGroup.setUpmGroupID(2);
    secondGroup.setGroupCode("B");
    secondGroup.setReferenceName("B");
    secondGroup.setWorkFlowLevel(20);
    secondGroup.setStatus(Status.ACT);

    when(upmGroupRepository.findAllApprovedUpmGroups()).thenReturn(List.of(upmGroup, secondGroup));

    StandardResponse<List<UpmGroupDTO>> response =
            workflowTemplateServiceImpl.getAllApprovedUPMGroups();

    List<UpmGroupDTO> mappedList = (List<UpmGroupDTO>) response.getResponse();
    assertEquals(2, mappedList.size());
    assertEquals(1, mappedList.get(0).getUpmGroupID());
    assertEquals(2, mappedList.get(1).getUpmGroupID());
  }

  @Test
  void testGetAllApprovedUPMGroups_VerifiesRepositoryInteraction() {
    when(upmGroupRepository.findAllApprovedUpmGroups()).thenReturn(List.of(upmGroup));

    workflowTemplateServiceImpl.getAllApprovedUPMGroups();

    verify(upmGroupRepository, times(1)).findAllApprovedUpmGroups();
    verifyNoMoreInteractions(upmGroupRepository);
  }

  @Test
  void testGetAllApprovedUPMGroups_ReturnsSuccessStatusAndMessage() {
    when(upmGroupRepository.findAllApprovedUpmGroups()).thenReturn(List.of(upmGroup));

    StandardResponse<List<UpmGroupDTO>> response =
            workflowTemplateServiceImpl.getAllApprovedUPMGroups();

    assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getSuccess());
    assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getMessage());
  }

  @Test
  void testSaveOrUpdateTempWorkflowTemplate() {
    newWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of(workFlowTemplateDataDTO));
    when(workflowTemplateTempRepository.getWorkflowTempCountByName("New Template")).thenReturn(0);
    when(workflowTemplateTempRepository.getNextSequenceValue()).thenReturn(1001);
    when(workflowTemplateDataTempRepository.getNextSequenceValue()).thenReturn(2001);

    StandardResponse<String> response =
            workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(newWorkflowTemplateDTO);
    assertNotNull(response);
    assertEquals(true, response.getSuccess());
    verify(workflowTemplateTempRepository, times(1)).saveAndFlush(any(WorkflowTemplateTemp.class));

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
    ApiRequestException emptyDataException =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(newWorkflowTemplateDTO);
                    });
    assertEquals(
            "Template Name, Template Code and UPM Level are required.",
            emptyDataException.getMessage());

    newWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of(workFlowTemplateDataDTO));
    workFlowTemplateDataDTO.setWorkFlowTemplateDataId(null);
    StandardResponse<String> response2 =
            workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(newWorkflowTemplateDTO);
    assertNotNull(response2);
  }

  @Test
  void testSaveOrUpdateTempWorkflowTemplate_NullRequest_ThrowsException() {
    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(null));
    assertEquals(
            "Template Name, Template Code and UPM Level are required.", exception.getMessage());
    verify(workflowTemplateTempRepository, never()).saveAndFlush(any(WorkflowTemplateTemp.class));
  }

  @Test
  void testSaveOrUpdateTempWorkflowTemplate_BlankTemplateName_ThrowsException() {
    newWorkflowTemplateDTO.setWorkFlowTemplateName("   ");
    newWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of(workFlowTemplateDataDTO));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(
                                    newWorkflowTemplateDTO));
    assertEquals(
            "Template Name, Template Code and UPM Level are required.", exception.getMessage());
  }

  @Test
  void testSaveOrUpdateTempWorkflowTemplate_BlankCode_ThrowsException() {
    newWorkflowTemplateDTO.setCode("   ");
    newWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of(workFlowTemplateDataDTO));

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(
                                    newWorkflowTemplateDTO));
    assertEquals(
            "Template Name, Template Code and UPM Level are required.", exception.getMessage());
    verify(workflowTemplateTempRepository, never()).saveAndFlush(any(WorkflowTemplateTemp.class));
  }

  @Test
  void testSaveOrUpdateTempWorkflowTemplate_UpdateExisting_UsesProvidedIdNotNextSequence() {
    existingWorkflowTemplateDTO.setWorkFlowTemplateDataDTOList(List.of(workFlowTemplateDataDTO));
    when(workflowTemplateTempRepository.getWorkflowTempCountByName("Existing Template"))
            .thenReturn(0);
    when(workflowTemplateTempRepository.getNextSequenceValue()).thenReturn(9999);

    ArgumentCaptor<WorkflowTemplateTemp> captor =
            ArgumentCaptor.forClass(WorkflowTemplateTemp.class);

    workflowTemplateServiceImpl.saveOrUpdateTempWorkflowTemplate(existingWorkflowTemplateDTO);

    verify(workflowTemplateTempRepository, times(1)).saveAndFlush(captor.capture());
    assertEquals(1, captor.getValue().getWorkFlowTemplateId());
    assertEquals(MasterDataApproveStatus.PENDING, captor.getValue().getApproveStatus());
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp() {
    Date date = new Date();
    when(workflowTemplateTempRepository.findById(10)).thenReturn(Optional.empty());
    RuntimeException notFoundException =
            assertThrows(
                    RuntimeException.class,
                    () -> {
                      approveRejectRQ.setApproveRejectDataID(10);
                      workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);
                    });
    assertTrue(notFoundException.getMessage().contains("Not Found"));

    approveRejectRQ.setApproveRejectDataID(1);
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
            workflowTemplateServiceImpl.getTempWorkflowTemplateData(
                    workflowTemplateTemp, workflowTemplate));

    workflowTemplateServiceImpl.insertToAuditTableFromMaster(workflowTemplate);
    workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);

    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);
    workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp_NotFound_ThrowsWrappedRuntimeException() {
    approveRejectRQ.setApproveRejectDataID(99);
    when(workflowTemplateTempRepository.findById(99)).thenReturn(Optional.empty());

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ));

    assertTrue(exception.getCause() instanceof ApiRequestException);
    assertEquals("Not Found", exception.getCause().getMessage());
    verify(workflowTemplateTempRepository, never()).saveAndFlush(any(WorkflowTemplateTemp.class));
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp_Approved_SavesToMasterAndDeletesTemp() {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
    when(workflowTemplateTempRepository.findById(1)).thenReturn(Optional.of(workflowTemplateTemp));
    when(workflowTemplateRepository.getNextSequenceValue()).thenReturn(500);
    when(workflowTemplateDataTempRepository.getNextSequenceValue()).thenReturn(700);

    ArgumentCaptor<WorkflowTemplate> captor = ArgumentCaptor.forClass(WorkflowTemplate.class);

    StandardResponse<Boolean> response =
            workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);

    assertEquals(true, response.getResponse());
    verify(workflowTemplateRepository, times(1)).saveAndFlush(captor.capture());
    assertEquals(500, captor.getValue().getWorkFlowTemplateId());
    assertEquals(MasterDataApproveStatus.APPROVED, captor.getValue().getApproveStatus());
    verify(workflowTemplateAudRepository, times(1)).saveAndFlush(any(WorkflowTemplateAud.class));
    verify(workflowTemplateTempRepository, times(1)).delete(workflowTemplateTemp);
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp_Rejected_UpdatesTempStatusOnly() {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);
    when(workflowTemplateTempRepository.findById(1)).thenReturn(Optional.of(workflowTemplateTemp));

    StandardResponse<Boolean> response =
            workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);

    assertEquals(true, response.getResponse());
    assertEquals(MasterDataApproveStatus.REJECTED, workflowTemplateTemp.getApproveStatus());
    verify(workflowTemplateTempRepository, times(1)).saveAndFlush(workflowTemplateTemp);
    verify(workflowTemplateTempRepository, never()).delete(any(WorkflowTemplateTemp.class));
    verify(workflowTemplateRepository, never()).saveAndFlush(any(WorkflowTemplate.class));
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp_NullApproveStatus_ThrowsWrappedException() {
    approveRejectRQ.setApproveStatus(null);
    when(workflowTemplateTempRepository.findById(1)).thenReturn(Optional.of(workflowTemplateTemp));

    assertThrows(
            RuntimeException.class,
            () -> workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ));

    verify(workflowTemplateTempRepository, never()).saveAndFlush(any(WorkflowTemplateTemp.class));
    verify(workflowTemplateTempRepository, never()).delete(any(WorkflowTemplateTemp.class));
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp_PendingStatus_TakesNoBranchAction() {
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.PENDING);
    when(workflowTemplateTempRepository.findById(1)).thenReturn(Optional.of(workflowTemplateTemp));

    StandardResponse<Boolean> response =
            workflowTemplateServiceImpl.authorizeWorkflowTemplateTemp(approveRejectRQ);

    assertEquals(true, response.getResponse());
    verify(workflowTemplateTempRepository, never()).saveAndFlush(any(WorkflowTemplateTemp.class));
    verify(workflowTemplateTempRepository, never()).delete(any(WorkflowTemplateTemp.class));
    verify(workflowTemplateRepository, never()).saveAndFlush(any(WorkflowTemplate.class));
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

    Mockito.when(workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(anyInt()))
            .thenReturn(childDataList);

    StandardResponse<WorkflowTemplateResponse> response =
            workflowTemplateServiceImpl.getTempWorkflowTemplate(pageable);

    assertNotNull(response);
    assertEquals(true, response.getSuccess());
    WorkflowTemplateResponse body = (WorkflowTemplateResponse) response.getResponse();
    assertNotNull(body);
    assertEquals(1, body.getDataList().size());
    assertEquals(1, body.getDataList().get(0).getWorkFlowTemplateDataDTOList().size());
    verify(workflowTemplateTempRepository, times(1)).findAllWorkflowTemplateTemp(pageable);
  }

  @Test
  void testGetTempWorkflowTemplate_NoContent_ReturnsEmptyDataList() throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    Page<WorkflowTemplateTemp> emptyPage = Mockito.mock(Page.class);
    when(emptyPage.hasContent()).thenReturn(false);
    when(emptyPage.getTotalElements()).thenReturn(0L);
    when(workflowTemplateTempRepository.findAllWorkflowTemplateTemp(pageable))
            .thenReturn(emptyPage);

    StandardResponse<WorkflowTemplateResponse> response =
            workflowTemplateServiceImpl.getTempWorkflowTemplate(pageable);

    WorkflowTemplateResponse body = (WorkflowTemplateResponse) response.getResponse();
    assertNotNull(body);
    assertTrue(body.getDataList().isEmpty());
    assertEquals(0L, body.getCount());
    verify(workflowTemplateDataTempRepository, never()).findAllTempWorkflowTemplateData(anyInt());
  }

  @Test
  void testGetTempWorkflowTemplate_ContentWithNoChildData_ReturnsEmptyChildArray()
          throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    WorkflowTemplateTemp template = new WorkflowTemplateTemp();
    template.setWorkFlowTemplateId(5);
    template.setWorkFlowTemplateName("No Child Template");
    template.setCode("NCT");
    template.setStatus(Status.ACT);
    template.setApproveStatus(MasterDataApproveStatus.PENDING);

    Page<WorkflowTemplateTemp> pageWithContent = Mockito.mock(Page.class);
    when(pageWithContent.hasContent()).thenReturn(true);
    when(pageWithContent.getContent()).thenReturn(List.of(template));
    when(pageWithContent.getTotalElements()).thenReturn(1L);
    when(workflowTemplateTempRepository.findAllWorkflowTemplateTemp(pageable))
            .thenReturn(pageWithContent);
    when(workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(5))
            .thenReturn(Collections.emptyList());

    StandardResponse<WorkflowTemplateResponse> response =
            workflowTemplateServiceImpl.getTempWorkflowTemplate(pageable);

    WorkflowTemplateResponse body = (WorkflowTemplateResponse) response.getResponse();
    assertEquals(1, body.getDataList().size());
    assertTrue(body.getDataList().get(0).getWorkFlowTemplateDataDTOList().isEmpty());
  }

  @Test
  void testGetTempWorkflowTemplate_MultipleTemplatesWithChildren_MapsAllFields()
          throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    WorkflowTemplateTemp template1 = new WorkflowTemplateTemp();
    template1.setWorkFlowTemplateId(1);
    template1.setWorkFlowTemplateName("Template One");
    template1.setCode("T1");
    template1.setStatus(Status.ACT);
    template1.setApproveStatus(MasterDataApproveStatus.PENDING);

    WorkflowTemplateTemp template2 = new WorkflowTemplateTemp();
    template2.setWorkFlowTemplateId(2);
    template2.setWorkFlowTemplateName("Template Two");
    template2.setCode("T2");
    template2.setStatus(Status.ACT);
    template2.setApproveStatus(MasterDataApproveStatus.PENDING);

    Page<WorkflowTemplateTemp> pageWithContent = Mockito.mock(Page.class);
    when(pageWithContent.hasContent()).thenReturn(true);
    when(pageWithContent.getContent()).thenReturn(List.of(template1, template2));
    when(pageWithContent.getTotalElements()).thenReturn(2L);
    when(workflowTemplateTempRepository.findAllWorkflowTemplateTemp(pageable))
            .thenReturn(pageWithContent);
    when(workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(1))
            .thenReturn(List.of(workflowTemplateDataTemp));
    when(workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(2))
            .thenReturn(Collections.emptyList());

    StandardResponse<WorkflowTemplateResponse> response =
            workflowTemplateServiceImpl.getTempWorkflowTemplate(pageable);

    WorkflowTemplateResponse body = (WorkflowTemplateResponse) response.getResponse();
    assertEquals(2, body.getDataList().size());
    assertEquals("Template One", body.getDataList().get(0).getWorkFlowTemplateName());
    assertEquals(1, body.getDataList().get(0).getWorkFlowTemplateDataDTOList().size());
    assertEquals("Template Two", body.getDataList().get(1).getWorkFlowTemplateName());
    assertTrue(body.getDataList().get(1).getWorkFlowTemplateDataDTOList().isEmpty());
  }

  @Test
  void testGetTempWorkflowTemplate_VerifiesTotalCountMatchesPage() throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    Page<WorkflowTemplateTemp> pageWithContent = Mockito.mock(Page.class);
    when(pageWithContent.hasContent()).thenReturn(true);
    when(pageWithContent.getContent()).thenReturn(List.of(workflowTemplateTemp));
    when(pageWithContent.getTotalElements()).thenReturn(42L);
    when(workflowTemplateTempRepository.findAllWorkflowTemplateTemp(pageable))
            .thenReturn(pageWithContent);
    when(workflowTemplateDataTempRepository.findAllTempWorkflowTemplateData(1))
            .thenReturn(Collections.emptyList());

    StandardResponse<WorkflowTemplateResponse> response =
            workflowTemplateServiceImpl.getTempWorkflowTemplate(pageable);

    WorkflowTemplateResponse body = (WorkflowTemplateResponse) response.getResponse();
    assertEquals(42L, body.getCount());
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
    Pageable pageable = PageRequest.of(0, 10);
    Page<WorkflowTemplate> page = new PageImpl<>(Collections.singletonList(template));
    when(workflowTemplateRepository.findAllWorkflowTemplate(pageable)).thenReturn(page);
    when(workflowTemplateDataRepository.findAllWorkflowTemplateData(1))
            .thenReturn(Collections.singletonList(templateData));

    // Call the method
    StandardResponse<Page<WorkflowTemplateDTO>> response =
            workflowTemplateServiceImpl.getWorkflowTemplate(pageable);

    // Assertions
    assertNotNull(response);
    assertEquals(true, response.getSuccess());
    assertNotNull(response.getResponse());
    Page<WorkflowTemplateDTO> dtoPage = (Page<WorkflowTemplateDTO>) response.getResponse();
    assertEquals(1, dtoPage.getContent().size());
    assertEquals(1, dtoPage.getContent().get(0).getWorkFlowTemplateDataDTOList().size());

    // Verify repository interactions
    verify(workflowTemplateRepository, times(1)).findAllWorkflowTemplate(pageable);
    verify(workflowTemplateDataRepository, times(1)).findAllWorkflowTemplateData(1);
  }

  @Test
  void testGetWorkflowTemplate_NoContent_ReturnsEmptyPage() throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    Page<WorkflowTemplate> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
    when(workflowTemplateRepository.findAllWorkflowTemplate(pageable)).thenReturn(emptyPage);

    StandardResponse<Page<WorkflowTemplateDTO>> response =
            workflowTemplateServiceImpl.getWorkflowTemplate(pageable);

    Page<WorkflowTemplateDTO> dtoPage = (Page<WorkflowTemplateDTO>) response.getResponse();
    assertTrue(dtoPage.getContent().isEmpty());
    assertEquals(0, dtoPage.getTotalElements());
    verify(workflowTemplateDataRepository, never()).findAllWorkflowTemplateData(anyInt());
  }

  @Test
  void testGetWorkflowTemplate_ContentWithNoChildData_ReturnsEmptyChildArray()
          throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    WorkflowTemplate template = new WorkflowTemplate();
    template.setWorkFlowTemplateId(7);
    template.setWorkFlowTemplateName("No Child Master");
    template.setCode("NCM");
    template.setStatus(Status.ACT);
    template.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Page<WorkflowTemplate> page =
            new PageImpl<>(Collections.singletonList(template), pageable, 1);
    when(workflowTemplateRepository.findAllWorkflowTemplate(pageable)).thenReturn(page);
    when(workflowTemplateDataRepository.findAllWorkflowTemplateData(7))
            .thenReturn(Collections.emptyList());

    StandardResponse<Page<WorkflowTemplateDTO>> response =
            workflowTemplateServiceImpl.getWorkflowTemplate(pageable);

    Page<WorkflowTemplateDTO> dtoPage = (Page<WorkflowTemplateDTO>) response.getResponse();
    assertEquals(1, dtoPage.getContent().size());
    assertTrue(dtoPage.getContent().get(0).getWorkFlowTemplateDataDTOList().isEmpty());
  }

  @Test
  void testGetWorkflowTemplate_MultipleTemplates_MapsAllFieldsCorrectly()
          throws ApiRequestException {
    Pageable pageable = PageRequest.of(0, 10);
    WorkflowTemplate template1 = new WorkflowTemplate();
    template1.setWorkFlowTemplateId(1);
    template1.setWorkFlowTemplateName("Master One");
    template1.setCode("M1");
    template1.setStatus(Status.ACT);
    template1.setApproveStatus(MasterDataApproveStatus.APPROVED);

    WorkflowTemplate template2 = new WorkflowTemplate();
    template2.setWorkFlowTemplateId(2);
    template2.setWorkFlowTemplateName("Master Two");
    template2.setCode("M2");
    template2.setStatus(Status.ACT);
    template2.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Page<WorkflowTemplate> page =
            new PageImpl<>(List.of(template1, template2), pageable, 2);
    when(workflowTemplateRepository.findAllWorkflowTemplate(pageable)).thenReturn(page);
    when(workflowTemplateDataRepository.findAllWorkflowTemplateData(1))
            .thenReturn(Collections.emptyList());
    when(workflowTemplateDataRepository.findAllWorkflowTemplateData(2))
            .thenReturn(Collections.emptyList());

    StandardResponse<Page<WorkflowTemplateDTO>> response =
            workflowTemplateServiceImpl.getWorkflowTemplate(pageable);

    Page<WorkflowTemplateDTO> dtoPage = (Page<WorkflowTemplateDTO>) response.getResponse();
    assertEquals(2, dtoPage.getContent().size());
    assertEquals("Master One", dtoPage.getContent().get(0).getWorkFlowTemplateName());
    assertEquals("Master Two", dtoPage.getContent().get(1).getWorkFlowTemplateName());
  }

  @Test
  void testGetWorkflowTemplate_VerifiesPageableAndTotalElements() throws ApiRequestException {
    Pageable pageable = PageRequest.of(2, 5);
    WorkflowTemplate template = new WorkflowTemplate();
    template.setWorkFlowTemplateId(1);
    template.setWorkFlowTemplateName("Template");
    template.setCode("T1");
    template.setStatus(Status.ACT);
    template.setApproveStatus(MasterDataApproveStatus.APPROVED);

    Page<WorkflowTemplate> page = new PageImpl<>(List.of(template), pageable, 11);
    when(workflowTemplateRepository.findAllWorkflowTemplate(pageable)).thenReturn(page);
    when(workflowTemplateDataRepository.findAllWorkflowTemplateData(1))
            .thenReturn(Collections.emptyList());

    StandardResponse<Page<WorkflowTemplateDTO>> response =
            workflowTemplateServiceImpl.getWorkflowTemplate(pageable);

    Page<WorkflowTemplateDTO> dtoPage = (Page<WorkflowTemplateDTO>) response.getResponse();
    assertEquals(11, dtoPage.getTotalElements());
    assertEquals(2, dtoPage.getNumber());
    verify(workflowTemplateRepository, times(1)).findAllWorkflowTemplate(pageable);
  }

  @Test
  void testDeleteWorkFlowTempById_Success() throws ApiRequestException {
    doNothing().when(workflowTemplateTempRepository).deleteById(1);

    ResponseEntity<StandardResponse<Void>> response =
            workflowTemplateServiceImpl.deleteWorkFlowTempById(1);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(true, response.getBody().getSuccess());
    assertEquals(1, response.getBody().getResponse());
    verify(workflowTemplateTempRepository, times(1)).deleteById(1);
  }

  @Test
  void testDeleteWorkFlowTempById_VerifiesRepositoryCalledWithCorrectId()
          throws ApiRequestException {
    doNothing().when(workflowTemplateTempRepository).deleteById(42);

    workflowTemplateServiceImpl.deleteWorkFlowTempById(42);

    ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(workflowTemplateTempRepository, times(1)).deleteById(idCaptor.capture());
    assertEquals(42, idCaptor.getValue());
  }

  @Test
  void testDeleteWorkFlowTempById_RepositoryThrowsException_WrapsInRuntimeException() {
    doThrow(new RuntimeException("DB error"))
            .when(workflowTemplateTempRepository)
            .deleteById(5);

    RuntimeException exception =
            assertThrows(
                    RuntimeException.class,
                    () -> workflowTemplateServiceImpl.deleteWorkFlowTempById(5));

    assertNotNull(exception.getCause());
    assertEquals("DB error", exception.getCause().getMessage());
  }

  @Test
  void testDeleteWorkFlowTempById_NullId_StillInvokesRepository() throws ApiRequestException {
    doNothing().when(workflowTemplateTempRepository).deleteById(null);

    ResponseEntity<StandardResponse<Void>> response =
            workflowTemplateServiceImpl.deleteWorkFlowTempById(null);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(workflowTemplateTempRepository, times(1)).deleteById(null);
  }

  @Test
  void testDeleteWorkFlowTempById_ReturnsSuccessMessage() throws ApiRequestException {
    doNothing().when(workflowTemplateTempRepository).deleteById(9);

    ResponseEntity<StandardResponse<Void>> response =
            workflowTemplateServiceImpl.deleteWorkFlowTempById(9);

    assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
    assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
  }
}