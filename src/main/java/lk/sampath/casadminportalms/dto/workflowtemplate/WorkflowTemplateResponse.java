package lk.sampath.casadminportalms.dto.workflowtemplate;

import java.util.List;
import lombok.Data;

@Data
public class WorkflowTemplateResponse {

  private long count;
  private List<WorkflowTemplateDTO> dataList;
}
