package lk.sampath.casadminportalms.dto.workflowtemplate;

import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateTemp;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Builder
@Data
public class WorkFlowTemplateDataDTO implements Serializable {

    private Integer workFlowTemplateDataID;
    private Integer workFlowTemplateId;
    private Integer upmGroupID;
    private WorkflowTemplateTemp workflowTemplateTemp;
    private UpmGroup upmGroup;
    private UpmGroup nextUPMGroup;
    private Integer nextUPMGroupID;
    private UpmGroup previousUPMGroup;
    private Integer previousUPMGroupID;
    private Integer displayOrder;
    private boolean removed;

}
