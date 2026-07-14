package lk.sampath.casadminportalms.entity.workflowtemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;
import java.util.Set;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_WORK_FLOW_TEMPLATE")
@Builder
@AllArgsConstructor
public class WorkflowTemplate extends ApprovableEntity {

    @Id
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

    @Version
    @Column(name = "VERSION")
    protected Integer version;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "workFlowTemplate")
    private Set<WorkflowTemplateData> workFlowTemplateDataSet;

}
