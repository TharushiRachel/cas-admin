package lk.sampath.casadminportalms.entity.daDesignation;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "DA_LIMITS")
public class DALimit extends ApprovableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DA_LIMITS")
    @SequenceGenerator(name = "SEQ_DA_LIMITS", sequenceName = "SEQ_DA_LIMITS", allocationSize = 1)
    @Column(name = "DA_LIMITS_ID")
    private Integer daLimitsId;

    @Column(name = "DESIGNATION_ID")
    private Integer designationId;

    @Column(name = "COLUMN_ID")
    private Integer columnId;

    @Column(name = "RISK_VALUE")
    private Double riskValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppsConstants.Status status;

    @Column(name = "AUTHORIZER_DISPLAY_NAME")
    private String authorizerDisplayName;

    @Column(name = "RISK_RATING")
    private String riskRating;

    @Column(name = "IS_COMMITTEE")
    private String isCommittee;


}
