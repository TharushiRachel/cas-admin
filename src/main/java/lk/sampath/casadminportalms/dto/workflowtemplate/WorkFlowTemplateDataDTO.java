package lk.sampath.casadminportalms.dto.workflowtemplate;

import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateTemp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WorkFlowTemplateDataDTO implements Serializable {

    private Integer workFlowTemplateDataId;
    private Integer workFlowTemplateId;
    private Integer upmGroupID;
    private WorkflowTemplateTemp workflowTemplateTemp;
    private UpmGroup upmGroup;
    private UpmGroup nextUPMGroup;
    private Integer nextUPMGroupId;
    private UpmGroup previousUPMGroup;
    private Integer previousUPMGroupId;
    private Integer displayOrder;
    private boolean removed;

}
