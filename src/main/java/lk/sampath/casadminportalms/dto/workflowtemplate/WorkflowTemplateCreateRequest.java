package lk.sampath.casadminportalms.dto.workflowtemplate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Data;

@Data
public class WorkflowTemplateCreateRequest implements Serializable {
  private Integer workFlowTemplateID;

  private String workFlowTemplateName;

  private String code;

  private String description;

  private AppsConstants.Status status;

  private MasterDataApproveStatus approveStatus;

  private String approvedDateStr;

  private String approvedBy;

  private Date createdDateStr;

  private String createdBy;

  private String modifiedDateStr;

  private String modifiedBy;

  private List<WorkFlowTemplateDataDTO> workFlowTemplateDataDTOList;
}
