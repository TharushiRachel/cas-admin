package lk.sampath.casadminportalms.dto.workflowtemplate;

import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;
import java.util.List;

@Data
public class WorkflowTemplateRequest {

    private Integer workFlowTemplateID;

    private String workFlowTemplateName;

    private String code;

    private String description;

    private Status status;

    private List<MasterDataApproveStatus> approveStatusList;

    private String approvedDateStr;
}
