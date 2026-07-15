package lk.sampath.casadminportalms.entity.workflowtemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import lombok.*;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "T_WORK_FLOW_TEMPLATE_DATA")
public class WorkflowTemplateData {

  @Id
  @Column(name = "WORK_FLOW_TEMPLATE_DATA_ID")
  private Integer workFlowTemplateDataID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "NEXT_UPM_GROUP_ID")
  private UpmGroup nextUPMGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PREVIOUS_UPM_GROUP_ID")
  private UpmGroup previousUPMGroup;

  @ManyToOne
  @JoinColumn(name = "UPM_GROUP_ID")
  private UpmGroup upmGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "WORK_FLOW_TEMPLATE_ID")
  private WorkflowTemplate workFlowTemplate;

  @Column(name = "DISPLAY_ORDER")
  private Integer displayOrder;
}
