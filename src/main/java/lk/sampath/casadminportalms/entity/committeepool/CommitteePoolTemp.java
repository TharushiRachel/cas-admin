package lk.sampath.casadminportalms.entity.committeepool;

import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "T_COMMITTEE_POOL_TEMP")
public class CommitteePoolTemp extends ApprovableEntity {

    @Id
    @Column(name = "COMMITTEE_POOL_ID")
    private Integer committeePoolId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_DISPLAY_NAME")
    private String userDisplayName;

    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "WORK_CLASS")
    private String workClass;

    @Column(name = "STATUS")
    private Status status;}
