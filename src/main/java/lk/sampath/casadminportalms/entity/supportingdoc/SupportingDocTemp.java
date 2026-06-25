package lk.sampath.casadminportalms.entity.supportingdoc;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "T_SUPPORTING_DOC_TEMP")
@ToString
@RequiredArgsConstructor

public class SupportingDocTemp extends ApprovableEntity  {

    @Id
    @Column(name = "SUPPORTING_DOC_ID",nullable = false, updatable = false)
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
