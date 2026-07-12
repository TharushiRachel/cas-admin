package lk.sampath.casadminportalms.entity.role;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_ROLE_AUD")
public class RoleAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_ROLES_AUD")
    @SequenceGenerator(name="SEQ_T_ROLES_AUD",sequenceName = "SEQ_T_ROLES_AUD",allocationSize = 1)
    @Column(name = "ROLE_AUD_ID")
    private Integer id;

    @Column(name = "ROLE_ID", nullable = false, updatable = false)
    private Integer roleID;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "UPM_PRIVILAGE_CODE")
    private String upmPrivilegeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<RolePrivilegeAud> rolePrivilegeAudSet;
}
