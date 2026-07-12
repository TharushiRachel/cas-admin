package lk.sampath.casadminportalms.entity.creditfacilitytemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "CASV3_T_CFT_SUPPORTING_DOC_AUD")
@RequiredArgsConstructor
@AllArgsConstructor
public class CftSupportingDocAud  extends UserTrackableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_CFT_SUPPORTING_DOC")
    @SequenceGenerator(name = "CASV3_SEQ_CFT_SUPPORTING_DOC", sequenceName = "CASV3_SEQ_CFT_SUPPORTING_DOC", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CFT_SUPPORTING_DOC_ID")
    private Integer cftSupportingDocID;


    @Column(name = "CREDIT_FACILITY_TEMPLATE_ID")
    private Integer creditFacilityTemplate;


    @Column(name = "SUPPORTING_DOC_ID")
    private Integer supportingDoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "MANDATORY")
    private AppsConstants.YesNo mandatory;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppsConstants.Status status;

    @Column(name = "RECORD_STATUS")
    private String recordStatus;
}
