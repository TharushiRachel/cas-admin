package lk.sampath.casadminportalms.dto.workflowtemplate;

import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WorkflowTemplateDTO implements Serializable{

    private Integer workFlowTemplateId;
    private String workFlowTemplateName;
    private String code;
    private String description;
    private Status status;
    private Date approvedDate;
    private String approvedBy;
    private Long parentRecID;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
    private MasterDataApproveStatus approveStatus;
    private List<WorkFlowTemplateDataDTO> workFlowTemplateDataDTOList;
}
