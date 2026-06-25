package lk.sampath.casadminportalms.dto.workflowtemplate;

import lombok.Data;
import java.util.List;

@Data
public class WorkflowTemplateResponse {

    private long count;
    private List<WorkflowTemplateDTO> dataList;
}
