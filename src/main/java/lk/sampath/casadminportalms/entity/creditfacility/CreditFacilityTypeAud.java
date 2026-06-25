package lk.sampath.casadminportalms.entity.creditfacility;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;


/**
 *
 *
 * @author yomesh
 */

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_CREDIT_FACILITY_TYPE_AUD")

public class CreditFacilityTypeAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_UPM_GROUP_AUD")
    @SequenceGenerator(name = "SEQ_T_UPM_GROUP_AUD", sequenceName = "SEQ_T_UPM_GROUP_AUD", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CREDIT_FACILITY_TYPE_ID")
    private Integer creditFacilityTypeID;

    @Column(name = "FACILITY_TYPE_NAME")
    private String facilityTypeName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

}
