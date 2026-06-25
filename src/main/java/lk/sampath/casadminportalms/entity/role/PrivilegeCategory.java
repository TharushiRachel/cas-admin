package lk.sampath.casadminportalms.entity.role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "T_PRIVILEGE_CATEGORY")
@NoArgsConstructor
public class PrivilegeCategory implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PRIVILEGE_CATEGORY")
    @SequenceGenerator(name = "SEQ_T_PRIVILEGE_CATEGORY", sequenceName = "SEQ_T_PRIVILEGE_CATEGORY", allocationSize = 1)
    @Column(name = "PRIVILEGE_CATEGORY_ID")
    private Integer privilegeCategoryID;

    @Column(name = "CATEGORY")
    private String category;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},  mappedBy = "privilegeCategory")
    private List<Privilege> privileges;
}
