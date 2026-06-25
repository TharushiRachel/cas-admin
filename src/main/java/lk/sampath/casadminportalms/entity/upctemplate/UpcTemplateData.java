package lk.sampath.casadminportalms.entity.upctemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.upcsection.UpcSection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_UPC_TEMPLATE_DATA")
@ToString
public class UpcTemplateData implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;

    @Id
    @Column(name = "UPC_TEMPLATE_DATA_ID")
    private Integer upcTemplateDataID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPC_TEMPLATE_ID")
    private UpcTemplate upcTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPC_SECTION_ID")
    private UpcSection upcSection;

    @Column(name = "PARENT_SECTION_ID")
    private Integer parentSectionID;

    @Column(name = "SECTION_LEVEL")
    private Integer sectionLevel;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;
}
