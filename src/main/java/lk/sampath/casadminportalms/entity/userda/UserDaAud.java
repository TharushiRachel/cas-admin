package lk.sampath.casadminportalms.entity.userda;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_USER_DA_AUD")
public class UserDaAud extends ApprovableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_USER_DA_AUD")
  @SequenceGenerator(
      name = "CASV3_SEQ_USER_DA_AUD",
      sequenceName = "CASV3_SEQ_USER_DA_AUD",
      allocationSize = 1)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "USER_DA_ID", nullable = false, updatable = false)
  private Integer userDaID;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "MAX_AMOUNT")
  private BigDecimal maxAmount;

  @Column(name = "DESCRIPTION")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Status status;

  @Column(name = "CLEAN_AMOUNT")
  private BigDecimal cleanAmount;
}
