package lk.sampath.casadminportalms.entity.upctemplate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_UPC_TEMPLATE_DATA_AUD")
public class UpcTemplateDataAud {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_UPC_TEMPLATE_DATA_AUD")
  @SequenceGenerator(
      name = "CASV3_SEQ_UPC_TEMPLATE_DATA_AUD",
      sequenceName = "CASV3_SEQ_UPC_TEMPLATE_DATA_AUD",
      allocationSize = 1)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "UPC_TEMPLATE_DATA_ID")
  private Integer upcTemplateDataID;

  @Column(name = "UPC_TEMPLATE_ID")
  private Integer upcTemplateID;

  @Column(name = "UPC_SECTION_ID")
  private Integer upcSectionID;

  @Column(name = "PARENT_SECTION_ID")
  private Integer parentSectionID;

  @Column(name = "SECTION_LEVEL")
  private Integer sectionLevel;

  @Column(name = "DISPLAY_ORDER")
  private Integer displayOrder;
}
