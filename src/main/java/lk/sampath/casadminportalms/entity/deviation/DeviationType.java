package lk.sampath.casadminportalms.entity.deviation;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.UserTrackableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "T_DEVIATION_TYPE")
@Getter
@Setter
public class DeviationType extends UserTrackableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_DEVIATION_TYPE")
    @SequenceGenerator(name = "SEQ_T_DEVIATION_TYPE", sequenceName = "SEQ_T_DEVIATION_TYPE", allocationSize = 1)
    @Column(name = "DEVIATION_TYPE_ID")
    private Integer deviationTypeId;

    @Column(name = "DEVIATION_TYPE")
    private String deviationType;

    @Column(name = "STATUS")
    private String status;

}
