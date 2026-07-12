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
@Table(name = "CASV3_T_CREDIT_FACILITY_TYPE_TEMP")

@ToString
public class CreditFacilityTypeTemp extends ApprovableEntity {

    @Id
    @Column(name = "CREDIT_FACILITY_TYPE_ID", nullable = false, updatable = false)
    private Integer creditFacilityTypeID;

    @Column(name = "FACILITY_TYPE_NAME")
    private String facilityTypeName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

}
