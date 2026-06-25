package lk.sampath.casadminportalms.entity.role;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;
import java.util.Set;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_ROLE")
@ToString
public class Role extends ApprovableEntity {

    @Id
    @Column(name = "ROLE_ID")
    private Integer roleID;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "UPM_PRIVILAGE_CODE")
    private String upmPrivilegeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "T_ROLE_PRIVILEGE",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "PRIVILEGE_ID")})
    private Set<Privilege> privileges;
}
