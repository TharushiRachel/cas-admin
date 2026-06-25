package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import java.util.List;

public interface WorkflowTemplateService  {

    default StandardResponse<List<UpmGroupDTO>> getAllApprovedUPMGroups() throws ApiRequestException {
        return getAllApprovedUPMGroups(0, 10);
    }
    StandardResponse<List<UpmGroupDTO>> getAllApprovedUPMGroups(int page, int size) throws ApiRequestException;

    StandardResponse<String> saveOrUpdateTempWorkflowTemplate(WorkflowTemplateDTO workflowTemplateCreateRequest) throws ApiRequestException;

    StandardResponse<WorkflowTemplateResponse> getTempWorkflowTemplate(int pageNo, int pageSize) throws ApiRequestException;

    StandardResponse<List<WorkflowTemplateDTO>> getWorkflowTemplate(int pageNo,int pageSize)throws ApiRequestException;

    StandardResponse<Boolean>  authorizeWorkflowTemplateTemp(ApproveRejectRQ approveRejectRQ)throws ApiRequestException;
}
