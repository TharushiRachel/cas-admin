package lk.sampath.casadminportalms.entity.upmgroup;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_UPM_GROUP_TEMP_AUD")
public class UpmGroupTempAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_UPM_GROUP_AUD")
    @SequenceGenerator(name = "SEQ_T_UPM_GROUP_AUD", sequenceName = "SEQ_T_UPM_GROUP_AUD", allocationSize = 1)
    @Column(name = "REV", nullable = false, updatable = false)
    private Integer rev;

    @Column(name = "REVTYPE", nullable = false, updatable = false)
    private Integer revType;

    @Column(name = "UPM_GROUP_ID", nullable = false, updatable = false)
    private Integer upmGroupID;

    @Column(name = "GROUP_CODE")
    private String groupCode;

    @Column(name = "REFERENCE_NAME")
    private String referenceName;

    @Column(name = "WORK_FLOW_LEVEL")
    private Integer workFlowLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;
}
