package lk.sampath.casadminportalms.entity.supportingdoc;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "CASV3_T_SUPPORTING_DOC_AUD")

public class SupportingDocAud extends ApprovableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASV3_SEQ_SUPPORTING_DOC_AUD")
    @SequenceGenerator(name= "CASV3_SEQ_SUPPORTING_DOC_AUD", sequenceName = "CASV3_SEQ_SUPPORTING_DOC_AUD", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SUPPORTING_DOC_ID", nullable = false, updatable = false)
    private Integer supportingDocID;

    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SUPPORT_DOCUMENT_TYPE")
    private String supportDocumentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

}
