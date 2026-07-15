package lk.sampath.casadminportalms.entity.committee;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "CA_COMMITTEE_CONFIG")
public class Committee extends ApprovableEntity {

  @Id
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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "committee")
  private List<CommitteeLevel> committeeLevels;

  public List<CommitteeLevel> getCommitteeLevels() {
    if (this.committeeLevels == null) {
      this.committeeLevels = new ArrayList<>();
    }
    return committeeLevels;
  }

  public Integer getCommitteeId() {
    if (this.committeeId == null) {
      this.committeeId = 0;
    }
    return committeeId;
  }
}
