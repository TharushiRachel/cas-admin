package lk.sampath.casadminportalms.entity.userda;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table(name = "T_USER_DA")
@ToString
@RequiredArgsConstructor

public class UserDa extends ApprovableEntity  {

    @Id
    @Column(name = "USER_DA_ID")
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
