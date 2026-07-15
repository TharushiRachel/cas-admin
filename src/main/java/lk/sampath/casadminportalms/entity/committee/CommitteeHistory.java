package lk.sampath.casadminportalms.entity.committee;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Data;

@Data
@Entity
@Table(name = "CASV3_COMMITTEE_AUD")
public class CommitteeHistory extends ApprovableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CASV3_COMMITTEE_AUD")
  @SequenceGenerator(
      name = "SEQ_CASV3_COMMITTEE_AUD",
      sequenceName = "SEQ_CASV3_COMMITTEE_AUD",
      allocationSize = 1)
  @Column(name = "COMMITTEE_AUD_ID", nullable = false, updatable = false)
  private Integer committeeAudId;

  @Column(name = "COMMITTEE_ID", nullable = false, updatable = false)
  private Integer committeeId;

  @Column(name = "COMMITTEE_NAME")
  private String committeeName;

  @Column(name = "DELEGATED_AUTHORITY_IN_BILLION")
  private BigDecimal delegatedAuthority;

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = CommitteeType.class)
  @JoinColumn(name = "COMMITTEE_TYPE_ID", referencedColumnName = "COMMITTEE_TYPE_ID")
  @JoinColumn(name = "COMMITTEE_TYPE", referencedColumnName = "COMMITTEE_TYPE")
  private CommitteeType committeeType;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private AppsConstants.Status status;

  @Column(name = "REVIEWER")
  private String reviewer;

  @Enumerated(EnumType.STRING)
  @Column(name = "CURRENT_PATH")
  private AppsConstants.CAPathType currentPath;

  @Enumerated(EnumType.STRING)
  @Column(name = "COMMITTEE_STATUS")
  private AppsConstants.RecordStatus committeeStatus;
}
