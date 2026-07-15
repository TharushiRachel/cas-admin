package lk.sampath.casadminportalms.entity.upcsection;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_UPC_SECTION_TEMP")
public class UpcSectionTemp extends ApprovableEntity {

  @Id
  @Column(name = "UPC_SECTION_ID", nullable = false, updatable = false)
  private Integer upcSectionID;

  @Column(name = "UPC_SECTION_NAME")
  private String upcSectionName;

  @Column(name = "UPC_SECTION_DESCRIPTION")
  private String upcSectionDescription;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Status status;
}
