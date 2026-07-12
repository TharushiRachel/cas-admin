package lk.sampath.casadminportalms.entity.committeepool;

import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "CA_POOL_CONFIG")
public class CommitteePool extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMMITTEE_USER_POOL")
    @SequenceGenerator(name = "SEQ_COMMITTEE_USER_POOL", sequenceName = "SEQ_COMMITTEE_USER_POOL", allocationSize = 1)
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "POOL_ID")
    private Integer poolId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_DISPLAY_NAME")
    private String userDisplayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_STATUS")
    private AppsConstants.Status userStatus;

    @Column(name = "GROUP_CODE")
    private String groupCode;

    @Column(name = "REFERENCE_NAME")
    private String referenceName;

}
