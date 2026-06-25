package lk.sampath.casadminportalms.controller;

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
public class WorkflowTemplateController {

    private final WorkflowTemplateService workflowTemplateService;

    @Autowired
    public WorkflowTemplateController(WorkflowTemplateService workflowTemplateService) {
        this.workflowTemplateService = workflowTemplateService;
    }

    @GetMapping("${app.endpoint.getAllApprovedUPMGroups}")
    public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> getAllApprovedUPMGroups(Pageable pageable)
            throws ApiRequestException {
        StandardResponse<List<UpmGroupDTO>> response = workflowTemplateService.getAllApprovedUPMGroups(pageable);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("${app.endpoint.saveOrUpdateTempWorkflowTemplate}")
    public ResponseEntity<StandardResponse<String>> saveOrUpdateTempWorkflowTemplate(@RequestBody WorkflowTemplateDTO request)
            throws ApiRequestException {
        StandardResponse<String> response = workflowTemplateService.saveOrUpdateTempWorkflowTemplate(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("${app.endpoint.getTempWorkflowTemplate}")
    public ResponseEntity<StandardResponse<WorkflowTemplateResponse>> getTempWorkflowTemplate(Pageable pageable) throws ApiRequestException {
        StandardResponse<WorkflowTemplateResponse> response = workflowTemplateService.getTempWorkflowTemplate(pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("${app.endpoint.getWorkflowTemplate}")
    public ResponseEntity<StandardResponse<List<WorkflowTemplateDTO>>> getWorkflowTemplate(Pageable pageable) throws ApiRequestException {
        StandardResponse<List<WorkflowTemplateDTO>> response = workflowTemplateService.getWorkflowTemplate(pageable);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("${app.endpoint.authorizeWorkFlowTemp}")
    public ResponseEntity<StandardResponse<Boolean>> authorizeWorkflowTemplateTemp(@RequestBody ApproveRejectRQ approveRejectRQ)
            throws ApiRequestException {
        StandardResponse<Boolean> response = workflowTemplateService.authorizeWorkflowTemplateTemp(approveRejectRQ);
        return ResponseEntity.ok().body(response);
    }
}
