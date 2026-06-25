package lk.sampath.casadminportalms.entity.facilitypaper;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "T_FACILITY_SECURITY_DOCUMENT")
public class FPSecurityDocument implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_FACILITY_SECURITY_DOCUMENT")
    @SequenceGenerator(name = "SEQ_T_FACILITY_SECURITY_DOCUMENT", sequenceName = "SEQ_T_FACILITY_SECURITY_DOCUMENT", allocationSize = 1)
    @Column(name = "SECURITY_DOCUMENT_ID")
    private Integer securityDocumentID;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ELEMENT_ID")
    private FPDocumentElement fpDocumentElement;

    @Column(name = "FACILITY_PAPER_ID")
    private Integer facilityPaperID;

    @Column(name = "FACILITY_ID")
    private Integer facilityID;

    @Column(name = "CREDIT_FACILITY_TEMPLATE_ID")
    private Integer creditFacilityTemplateID;

    @Column(name = "CREDIT_FACILITY_NAME")
    private String creditFacilityName;

    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Column(name = "DOCUMENT_CONTENT")
    private String documentContent;

    @Column(name = "SAVED_BY")
    private String savedBy;

    @Column(name = "PRINTED_BY")
    private String printedBy;

    @Column(name = "SAVED_BY_DIV")
    private String savedByDiv;

    @Column(name = "AUTH_BY_DIV")
    private String authByDiv;

    @Column(name = "SAVED_DATE")
    private Date savedDate;

    @Column(name = "PRINTED_DATE")
    private Date printedDate;

    @Column(name = "RETURN_COMMENT")
    private String returnComment;

    @Column(name = "AUTH_BY")
    private String authBy;

    @Column(name = "AUTH_DATE")
    private Date authDate;

    @Column(name = "PRINTED_BY_DIV")
    private String printedByDiv;

    @Column(name = "SAVED_BY_DISPLAY_NAME")
    private String savedByDisplayName;

    @Column(name = "AUTH_BY_DISPLAY_NAME")
    private String authByDisplayName;

    @Column(name = "PRINTED_BY_DISPLAY_NAME")
    private String printedByDisplayName;

    @Column(name = "DOCUMENT_STATUS")
    private String documentStatus;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "fpSecurityDocument")
    private Set<FPSecurityDocumentTagData> fpSecurityDocumentTagDataSet;

}
