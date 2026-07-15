package lk.sampath.casadminportalms.entity.deviation;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CASV3_T_DEVIATIONS_AUDIT")
@Getter
@Setter
public class DeviationAud extends ApprovableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_DEVIATIONS_AUD")
  @SequenceGenerator(
      name = "CASV3_SEQ_DEVIATIONS_AUD",
      sequenceName = "CASV3_SEQ_DEVIATIONS_AUD",
      allocationSize = 1)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "DEVIATION_ID")
  private Integer deviationId;

  @Column(name = "DEVIATION_TYPE")
  private String deviationType;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS")
  private String status;
}
