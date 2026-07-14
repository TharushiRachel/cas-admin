package lk.sampath.casadminportalms.entity.workflowtemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@Table(name = "CASV3_T_WORK_FLOW_TEMPLATE_DATA_TEMP")
public class WorkflowTemplateDataTemp {

    @Id
    @Column(name = "WORK_FLOW_TEMPLATE_DATA_TEMP_ID")
    private Integer workFlowTemplateTempDataId;

    @ManyToOne
    @JoinColumn(name = "WORK_FLOW_TEMPLATE_ID")
    private WorkflowTemplateTemp workflowTemplateTemp;

    @ManyToOne
    @JoinColumn(name = "UPM_GROUP_ID")
    private UpmGroup upmGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_UPM_GROUP_ID")
    private UpmGroup nextUPMGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS_UPM_GROUP_ID")
    private UpmGroup previousUPMGroup;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;
}
