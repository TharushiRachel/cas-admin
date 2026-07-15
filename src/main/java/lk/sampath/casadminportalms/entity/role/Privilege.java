package lk.sampath.casadminportalms.entity.role;

import jakarta.persistence.*;
import java.io.Serializable;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "T_PRIVILEGE")
@NoArgsConstructor
public class Privilege implements Serializable {
  private static final long serialVersionUID = 2405172041950251807L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PRIVILEGE")
  @SequenceGenerator(name = "SEQ_T_PRIVILEGE", sequenceName = "SEQ_T_PRIVILEGE", allocationSize = 1)
  @Column(name = "PRIVILEGE_ID")
  private Integer privilegeID;

  @Column(name = "PRIVILEGE_NAME")
  private String privilegeName;

  @Column(name = "PRIVILEGE_CODE")
  private String code;

  @Column(name = "BEHAVIOUR_DESCRIPTION")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRIVILEGE_CATEGORY_ID", referencedColumnName = "PRIVILEGE_CATEGORY_ID")
  private PrivilegeCategory privilegeCategory;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Status status;

  public Privilege(Integer privilegeID) {}
}
