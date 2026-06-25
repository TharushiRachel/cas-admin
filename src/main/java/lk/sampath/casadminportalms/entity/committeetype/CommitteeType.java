package lk.sampath.casadminportalms.entity.committeetype;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Contains Entity to CommitteeType.
 */
@Entity
@Setter
@Getter
@Table(name = "CA_COMMITTEE_TYPE_CONFIG")
@ToString
public class CommitteeType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_COMMITTEE_TYPE")
    @SequenceGenerator(name = "SEQ_T_COMMITTEE_TYPE", sequenceName = "SEQ_T_COMMITTEE_TYPE", allocationSize = 1)
    @Column(name = "COMMITTEE_TYPE_ID")
    private Integer committeeTypeId;

    @Column(name = "COMMITTEE_TYPE")
    private String committeeType;

    @Column(name = "COMMITTEE_TYPE_DESCRIPTION")
    private String committeeTypeDescription;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_USER_DISPLAY_NAME")
    private String createdUserDisplayName;

    @Column(name = "AUTHORIZER_DISPLAY_NAME")
    private String authorizerDisplayName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "IS_SYSTEM")
    private Integer isSystem;

}
