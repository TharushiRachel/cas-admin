package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.WorkflowTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/workflowTemplate")
public class WorkflowTemplateController {

    private final WorkflowTemplateService workflowTemplateService;

    @Autowired
    public WorkflowTemplateController(WorkflowTemplateService workflowTemplateService) {
        this.workflowTemplateService = workflowTemplateService;
    }

    @GetMapping("/getAllApprovedUPMGroups")
    public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> getAllApprovedUPMGroups(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
            throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        StandardResponse<List<UpmGroupDTO>> response = workflowTemplateService.getAllApprovedUPMGroups(pageable);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/saveOrUpdateTempWorkflowTemplate")
    public ResponseEntity<StandardResponse<String>> saveOrUpdateTempWorkflowTemplate(@RequestBody WorkflowTemplateDTO request)
            throws ApiRequestException {
        StandardResponse<String> response = workflowTemplateService.saveOrUpdateTempWorkflowTemplate(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getTempWorkflowTemplate")
    public ResponseEntity<StandardResponse<WorkflowTemplateResponse>> getTempWorkflowTemplate(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        StandardResponse<WorkflowTemplateResponse> response = workflowTemplateService.getTempWorkflowTemplate(pageable);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/getWorkflowTemplate")
    public ResponseEntity<StandardResponse<List<WorkflowTemplateDTO>>> getWorkflowTemplate(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        StandardResponse<List<WorkflowTemplateDTO>> response = workflowTemplateService.getWorkflowTemplate(page, size);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/authorizeWorkFlowTemp")
    public ResponseEntity<StandardResponse<Boolean>> authorizeWorkflowTemplateTemp(@RequestBody ApproveRejectRQ approveRejectRQ)
            throws ApiRequestException {
        StandardResponse<Boolean> response = workflowTemplateService.authorizeWorkflowTemplateTemp(approveRejectRQ);
        return ResponseEntity.ok().body(response);
    }
}
