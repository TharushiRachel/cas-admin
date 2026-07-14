package lk.sampath.casadminportalms.entity.workflowtemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lombok.*;


@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_WORK_FLOW_TEMPLATE_DATA_AUD")
@ToString
@AllArgsConstructor
public class WorkflowTemplateDataAud{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_T_WORKFLOW_DATA_AUD")
    @SequenceGenerator(name = "CASV3_SEQ_T_WORKFLOW_DATA_AUD", sequenceName = "CASV3_SEQ_T_WORKFLOW_DATA_AUD", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORK_FLOW_TEMPLATE_DATA_ID")
    private Integer workFlowTemplateDataID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_UPM_GROUP_ID")
    private UpmGroup nextUPMGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS_UPM_GROUP_ID")
    private UpmGroup previousUPMGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPM_GROUP_ID")
    private UpmGroup upmGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_FLOW_TEMPLATE_ID")
    private WorkflowTemplate workFlowTemplate;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;
}
