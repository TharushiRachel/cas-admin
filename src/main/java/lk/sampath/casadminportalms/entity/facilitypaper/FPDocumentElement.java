package lk.sampath.casadminportalms.entity.facilitypaper;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CreditFacilityTemplate;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CreditFacilityTemplateTemp;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "T_FACILITY_DOCUMENT_ELEMENT")
public class FPDocumentElement implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_FACILITY_DOCUMENT_ELEMENT")
    @SequenceGenerator(name = "SEQ_T_FACILITY_DOCUMENT_ELEMENT", sequenceName = "SEQ_T_FACILITY_DOCUMENT_ELEMENT", allocationSize = 1)
    @Column(name = "ELEMENT_ID")
    private Integer elementID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDIT_FACILITY_TEMPLATE_ID", referencedColumnName = "CREDIT_FACILITY_TEMPLATE_ID", nullable = true)
    private CreditFacilityTemplate creditFacilityTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDIT_FACILITY_TEMPLATE_TEMP_ID", referencedColumnName = "CREDIT_FACILITY_TEMPLATE_ID", nullable = true)
    private CreditFacilityTemplateTemp creditFacilityTemplateTemp;


    @Column(name = "CREDIT_FACILITY_NAME")
    private String creditFacilityName;

    @Column(name = "PARENT_ID")
    private Integer parentID;

    @Column(name = "ELEMENT_NAME")
    private String elementName;

    @Column(name = "ELEMENT_TYPE")
    private String elementType;

    @Column(name = "FACILITY_TYPE_ID")
    private Integer facilityTypeID;

    @Column(name = "IS_NEW")
    private String isNew;

    @Column(name = "DOCUMENT_CONTENT")
    private String documentContent;


    @Column(name = "KEY")
    private String key;

    @Column(name = "STATUS")
    private String status;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "fpDocumentElement")
    private Set<FPSecurityDocument> fpSecurityDocument;

    public Set<FPSecurityDocument> getFpSecurityDocument() {
        if (fpSecurityDocument == null) {
            fpSecurityDocument = new HashSet<>();
        }
        return fpSecurityDocument;
    }
}
