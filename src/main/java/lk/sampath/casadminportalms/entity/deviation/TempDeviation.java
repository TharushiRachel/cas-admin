package lk.sampath.casadminportalms.entity.deviation;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "T_DEVIATIONS_TEMP")
@Getter
@Setter
@ToString
public class TempDeviation extends ApprovableEntity {

  @Id
  @Column(name = "DEVIATION_ID")
  private Integer deviationId;

  @Column(name = "PARENT_ID")
  private Integer parentId;

  @Column(name = "DEVIATION_TYPE")
  private String deviationType;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS")
  private String status;
}
