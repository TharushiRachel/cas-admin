package lk.sampath.casadminportalms.service;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface WorkflowTemplateService {

  StandardResponse<List<UpmGroupDTO>> getAllApprovedUPMGroups() throws ApiRequestException;

  StandardResponse<String> saveOrUpdateTempWorkflowTemplate(
      WorkflowTemplateDTO workflowTemplateCreateRequest) throws ApiRequestException;

  StandardResponse<WorkflowTemplateResponse> getTempWorkflowTemplate(Pageable pageable)
      throws ApiRequestException;

  StandardResponse<Page<WorkflowTemplateDTO>> getWorkflowTemplate(Pageable pageable)
      throws ApiRequestException;

  StandardResponse<Boolean> authorizeWorkflowTemplateTemp(ApproveRejectRQ approveRejectRQ)
      throws ApiRequestException;

  ResponseEntity<StandardResponse<Void>> deleteWorkFlowTempById(Integer approveRejectDataID)
      throws ApiRequestException;
}
