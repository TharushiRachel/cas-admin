package lk.sampath.casadminportalms.entity.userda;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_USER_DA_AUD")
public class UserDaAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_USER_DA_AUD")
    @SequenceGenerator(name="SEQ_T_USER_DA_AUD",sequenceName = "SEQ_T_USER_DA_AUD",allocationSize = 1)
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
}
