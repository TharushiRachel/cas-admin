package lk.sampath.casadminportalms.entity.committeetype;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

/**
 * Contains Entity to CommitteeType.
 */
@Entity
@Setter
@Getter
@Table(name = "CA_COMMITTEE_TYPE_CONFIG")
@ToString
public class CommitteeType  extends UserTrackableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CA_COMMITTEE_TYPE_CONFIG")
    @SequenceGenerator(name = "SEQ_CA_COMMITTEE_TYPE_CONFIG", sequenceName = "SEQ_CA_COMMITTEE_TYPE_CONFIG", allocationSize = 1)
    @Column(name = "COMMITTEE_TYPE_ID")
    private Integer committeeTypeId;

    @Column(name = "COMMITTEE_TYPE")
    private String committeeType;

    @Column(name = "COMMITTEE_TYPE_NAME")
    private String committeeTypeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppsConstants.Status status;

    @Column(name = "IS_SYSTEM")
    private Integer isSystem;

    @Column(name = "CREATED_USER_DISPLAY_NAME")
    private String createdUserDisplayName;

}
