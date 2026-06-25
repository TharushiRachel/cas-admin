package lk.sampath.casadminportalms.entity.upctemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_UPC_TEMPLATE")
@ToString
public class UpcTemplate extends ApprovableEntity {

    @Id
    @Column(name = "UPC_TEMPLATE_ID")
    private Integer upcTemplateID;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UPC_LABEL")
    private String upcLabel;

    @Column(name = "UPC_LABEL_BACKGROUND_COLOR")
    private String upcLabelBackgroundColor;

    @Column(name = "UPC_LABEL_FONT_COLOR")
    private String upcLabelFontColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE}, mappedBy = "upcTemplate")
    private List<UpcTemplateData> upcTemplateDataList;
}
