package lk.sampath.casadminportalms.entity.upmgroup;


import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_UPM_GROUP_TEMP")
@ToString
public class UpmGroupTemp extends ApprovableEntity {

    @Id
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
