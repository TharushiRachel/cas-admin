package lk.sampath.casadminportalms.entity.workflowtemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_WORK_FLOW_TEMPLATE_TEMP")
@AllArgsConstructor
@Setter
@Builder(toBuilder = true)
public class WorkflowTemplateTemp extends ApprovableEntity {

    @Id
    @Column(name = "WORK_FLOW_TEMPLATE_ID", nullable = false)
    private Integer workFlowTemplateId;

    @Column(name = "WORK_FLOW_TEMPLATE_NAME", length = 255)
    private String workFlowTemplateName;

    @Column(name = "CODE", length = 255)
    private String code;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @Version
    @Column(name = "VERSION")
    protected Integer version;

    @OneToMany(mappedBy = "workflowTemplateTemp",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkflowTemplateDataTemp> workFlowTemplateDataSet;

}
