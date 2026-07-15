package lk.sampath.casadminportalms.entity.committeetype;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@Table(name = "CASV3_CA_COMMITTEE_TYPE_CONFIG_AUD")
@ToString
public class CommitteeTypeAud extends ApprovableEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "SEQ_CASV3_CA_COMMITTEE_TYPE_CONFIG_AUD")
  @SequenceGenerator(
      name = "SEQ_CASV3_CA_COMMITTEE_TYPE_CONFIG_AUD",
      sequenceName = "SEQ_CASV3_CA_COMMITTEE_TYPE_CONFIG_AUD",
      allocationSize = 1)
  @Column(name = "COMMITTEE_TYPE_AUD_ID")
  private Integer committeeTypeAudId;

  @Column(name = "COMMITTEE_TYPE_ID")
  private Integer committeeTypeId;

  @Column(name = "COMMITTEE_TYPE")
  private String committeeType;

  @Column(name = "COMMITTEE_TYPE_NAME")
  private String committeeTypeName;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private AppsConstants.Status status;

  @Column(name = "IS_SYSTEM")
  private Integer isSystem;

  @Column(name = "CREATED_USER_DISPLAY_NAME")
  private String createdUserDisplayName;
}
