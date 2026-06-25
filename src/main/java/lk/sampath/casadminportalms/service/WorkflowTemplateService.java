package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface WorkflowTemplateService  {

    StandardResponse<List<UpmGroupDTO>> getAllApprovedUPMGroups(Pageable pageable) throws ApiRequestException;

    StandardResponse<String> saveOrUpdateTempWorkflowTemplate(WorkflowTemplateDTO workflowTemplateCreateRequest) throws ApiRequestException;

    StandardResponse<WorkflowTemplateResponse> getTempWorkflowTemplate(Pageable pageable) throws ApiRequestException;

    StandardResponse<List<WorkflowTemplateDTO>> getWorkflowTemplate(Pageable pageable)throws ApiRequestException;

    StandardResponse<Boolean>  authorizeWorkflowTemplateTemp(ApproveRejectRQ approveRejectRQ)throws ApiRequestException;
}
