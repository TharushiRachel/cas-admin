package lk.sampath.casadminportalms.entity.workflowtemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;


@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_WORKFLOW_TEMPLATE_AUD")
@ToString
@AllArgsConstructor
public class WorkflowTemplateAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_WORKFLOW_AUD")
    @SequenceGenerator(name = "SEQ_T_WORKFLOW_AUD", sequenceName = "SEQ_T_WORKFLOW_AUD", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer Id;

    @Column(name = "WORK_FLOW_TEMPLATE_ID", nullable = false)
    private Integer workFlowTemplateId;

    @Column(name = "WORK_FLOW_TEMPLATE_NAME")
    private String workFlowTemplateName;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;
}
