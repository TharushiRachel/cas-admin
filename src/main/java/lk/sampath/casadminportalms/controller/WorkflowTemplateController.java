package lk.sampath.casadminportalms.controller;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.WorkflowTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflowTemplate")
public class WorkflowTemplateController {

  private final WorkflowTemplateService workflowTemplateService;

  @Autowired
  public WorkflowTemplateController(WorkflowTemplateService workflowTemplateService) {
    this.workflowTemplateService = workflowTemplateService;
  }

  @GetMapping("/getAllApprovedUPMGroups")
  public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> getAllApprovedUPMGroups()
      throws ApiRequestException {
    StandardResponse<List<UpmGroupDTO>> response =
        workflowTemplateService.getAllApprovedUPMGroups();
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/saveOrUpdateTempWorkflowTemplate")
  public ResponseEntity<StandardResponse<String>> saveOrUpdateTempWorkflowTemplate(
      @RequestBody WorkflowTemplateDTO request) throws ApiRequestException {
    StandardResponse<String> response =
        workflowTemplateService.saveOrUpdateTempWorkflowTemplate(request);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/getTempWorkflowTemplate")
  public ResponseEntity<StandardResponse<WorkflowTemplateResponse>> getTempWorkflowTemplate(
      @RequestHeader(name = "page", required = false) Integer headerPage,
      @RequestHeader(name = "size", required = false) Integer headerSize,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size)
      throws ApiRequestException {
    Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
    StandardResponse<WorkflowTemplateResponse> response =
        workflowTemplateService.getTempWorkflowTemplate(pageable);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/getWorkflowTemplate")
  public ResponseEntity<StandardResponse<Page<WorkflowTemplateDTO>>> getWorkflowTemplate(
      @RequestHeader(name = "page", required = false) Integer headerPage,
      @RequestHeader(name = "size", required = false) Integer headerSize,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size)
      throws ApiRequestException {
    Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
    StandardResponse<Page<WorkflowTemplateDTO>> response =
        workflowTemplateService.getWorkflowTemplate(pageable);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/authorizeWorkFlowTemp")
  public ResponseEntity<StandardResponse<Boolean>> authorizeWorkflowTemplateTemp(
      @RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
    StandardResponse<Boolean> response =
        workflowTemplateService.authorizeWorkflowTemplateTemp(approveRejectRQ);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/deleteWorkFlowTemp")
  public ResponseEntity<StandardResponse<Void>> deleteWorkFlowTempById(
      @RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
    ResponseEntity<StandardResponse<Void>> role =
        workflowTemplateService.deleteWorkFlowTempById(approveRejectRQ.getApproveRejectDataID());
    return ResponseEntity.ok().body(role.getBody());
  }
}
