package lk.sampath.casadminportalms.entity.role;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_ROLE_PRIVILEGE_AUD")
public class RolePrivilegeAud implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_ROLE_PRIVILEGE_AUD")
    @SequenceGenerator(name="SEQ_T_ROLE_PRIVILEGE_AUD",sequenceName = "SEQ_T_ROLE_PRIVILEGE_AUD", allocationSize = 1)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ROLE_AUD_ID", nullable = false)
    private RoleAud role;

    @ManyToOne
    @JoinColumn(name = "PRIVILEGE_ID", nullable = false)
    private Privilege privilege;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTION_DATE", nullable = false)
    private Date actionDate;

}
