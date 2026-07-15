package lk.sampath.casadminPortalms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lk.sampath.casadminportalms.controller.WorkflowTemplateController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.service.WorkflowTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WorkflowTemplateController.class)
@ContextConfiguration(classes = WorkflowTemplateController.class)
class WorkflowTemplateControllerTest {

  @MockitoBean WorkflowTemplateService workflowTemplateService;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private WorkflowTemplateDTO workflowTemplateDTO;

  private int pageNo;
  private int pageSize;

  @BeforeEach
  public void setUp() {
    workflowTemplateDTO =
        WorkflowTemplateDTO.builder()
            .workFlowTemplateId(1)
            .workFlowTemplateName("")
            .code("")
            .description("")
            .build();
    pageNo = 0;
    pageSize = 5;
  }

  @Test
  void testGetAllApprovedUPMGroups() throws Exception {
    List<UpmGroupDTO> mockList = List.of(new UpmGroupDTO(), new UpmGroupDTO());
    StandardResponse<List<UpmGroupDTO>> response =
        new StandardResponse<>(true, "Success", mockList);
    when(workflowTemplateService.getAllApprovedUPMGroups()).thenReturn(response);

    mockMvc
        .perform(get("/api/workflowTemplate/getAllApprovedUPMGroups"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response").isArray());
    verify(workflowTemplateService).getAllApprovedUPMGroups();
  }

  @Test
  void testSaveOrUpdateTempWorkflowTemplate() throws Exception {
    StandardResponse<String> response =
        new StandardResponse<>(true, "Success", workflowTemplateDTO);
    when(workflowTemplateService.saveOrUpdateTempWorkflowTemplate(workflowTemplateDTO))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/api/workflowTemplate/saveOrUpdateTempWorkflowTemplate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workflowTemplateDTO)))
        .andExpect(status().isOk());
    verify(workflowTemplateService).saveOrUpdateTempWorkflowTemplate(workflowTemplateDTO);
  }

  @Test
  void testGetTempWorkflowTemplate() throws Exception {
    List<WorkflowTemplateDTO> mockList = List.of(workflowTemplateDTO, workflowTemplateDTO);
    WorkflowTemplateResponse workflowTemplateResponse = new WorkflowTemplateResponse();
    workflowTemplateResponse.setDataList(mockList);
    workflowTemplateResponse.setCount(2);
    StandardResponse<WorkflowTemplateResponse> response =
        new StandardResponse<>(true, "Success", workflowTemplateResponse);

    when(workflowTemplateService.getTempWorkflowTemplate(pageNo, pageSize)).thenReturn(response);

    mockMvc
        .perform(
            get("/api/workflowTemplate/getTempWorkflowTemplate")
                .param("pageNo", "0")
                .param("pageSize", "5"))
        .andExpect(status().isOk());
    verify(workflowTemplateService).getTempWorkflowTemplate(pageNo, pageSize);
  }

  @Test
  void testGetWorkflowTemplate() throws Exception {
    List<WorkflowTemplateDTO> mockList = List.of(workflowTemplateDTO, workflowTemplateDTO);
    StandardResponse<List<WorkflowTemplateDTO>> response =
        new StandardResponse<>(true, "Success", mockList);
    when(workflowTemplateService.getWorkflowTemplate(pageNo, pageSize)).thenReturn(response);

    mockMvc
        .perform(
            get("/api/workflowTemplate/getWorkflowTemplate")
                .param("pageNo", "0")
                .param("pageSize", "5"))
        .andExpect(status().isOk());
    verify(workflowTemplateService).getWorkflowTemplate(pageNo, pageSize);
  }

  @Test
  void testAuthorizeWorkflowTemplateTemp() throws Exception {
    ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.valueOf("PENDING"));
    approveRejectRQ.setApproveRejectDataID(1);

    StandardResponse<Boolean> response = new StandardResponse<>(true, "Success", true);
    when(workflowTemplateService.authorizeWorkflowTemplateTemp(approveRejectRQ))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/api/workflowTemplate/authorizeWorkFlowTemp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approveRejectRQ)))
        .andExpect(status().isOk());
    verify(workflowTemplateService).authorizeWorkflowTemplateTemp(approveRejectRQ);
  }
}
