package lk.sampath.casadminportalms.entity.deviation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_DEVIATIONS")
@Getter
@Setter
public class Deviation extends ApprovableEntity {

  @Id
  @Column(name = "DEVIATION_ID")
  private Integer deviationId;

  @Column(name = "DEVIATION_TYPE")
  private String deviationType;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS")
  private String status;

  //    @Temporal(TemporalType.TIMESTAMP)
  //    @Column(name = "CREATED_DATE", updatable = false)
  //    private Date createdDate;
  //
  //    @Column(name = "CREATED_BY", updatable = false)
  //    private String createdBy;
  //
  //    @Temporal(TemporalType.TIMESTAMP)
  //    @Column(name = "MODIFIED_DATE", updatable = false)
  //    private Date modifiedDate;
  //
  //    @Column(name = "MODIFIED_BY", updatable = false)
  //    private String modifiedBy;
  //
  //    @Temporal(TemporalType.TIMESTAMP)
  //    @Column(name = "AUTHORIZED_DATE", updatable = false)
  //    private Date athorizedDate;
  //
  //    @Column(name = "AUTHORIZED_BY", updatable = false)
  //    private String authorizedBy;

}
