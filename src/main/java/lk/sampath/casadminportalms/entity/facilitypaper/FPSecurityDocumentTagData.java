package lk.sampath.casadminportalms.entity.facilitypaper;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_SECURITY_DOCUMENT_TAG_DATA")
public class FPSecurityDocumentTagData implements Serializable {

  private static final long serialVersionUID = 2405172041950251807L;

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "SEQ_T_SECURITY_DOCUMENT_TAG_DATA")
  @SequenceGenerator(
      name = "SEQ_T_SECURITY_DOCUMENT_TAG_DATA",
      sequenceName = "SEQ_T_SECURITY_DOCUMENT_TAG_DATA",
      allocationSize = 1)
  @Column(name = "TAG_ID")
  private Integer tagID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SECURITY_DOCUMENT_ID")
  private FPSecurityDocument fpSecurityDocument;

  /* @Column(name = "SECURITY_DOCUMENT_ID")
  private Integer securityDocumentID;*/

  @Column(name = "TAG_ORDER")
  private Integer tagOrder;

  @Column(name = "TAG")
  private String tag;

  @Column(name = "TAG_VALUE")
  private String tagValue;

  @Column(name = "TAG_TYPE")
  private String tagType;

  @Column(name = "FIELD_TYPE")
  private String fieldType;
}
