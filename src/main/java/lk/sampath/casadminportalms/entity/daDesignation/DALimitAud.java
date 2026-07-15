package lk.sampath.casadminportalms.entity.daDesignation;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CASV3_T_DA_LIMIT_AUD")
public class DALimitAud extends ApprovableEntity {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DA_LIMITS_ID", nullable = false, updatable = false)
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