package lk.sampath.casadminportalms.entity.upctemplate;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_UPC_TEMPLATE_AUD")
public class UpcTemplateAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_UPC_TEMPLATE_AUD")
    @SequenceGenerator(name = "CASV3_SEQ_UPC_TEMPLATE_AUD", sequenceName = "CASV3_SEQ_UPC_TEMPLATE_AUD", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

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

    @Column(name = "ACTION_BY")
    private String actionBy;

    @Column(name = "ACTION_DATE")
    private Date actionDate;

    @Column(name = "ACTION_MSG")
    private String actionMsg;

}
