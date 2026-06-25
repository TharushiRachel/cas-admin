package lk.sampath.casadminportalms.entity.upcsection;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.entity.common.ApprovableEntity;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "T_UPC_SECTION_AUD")
public class UpcSectionAud extends ApprovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_UPC_SECTION_AUD")
    @SequenceGenerator(name = "SEQ_T_UPC_SECTION_AUD", sequenceName = "SEQ_T_UPC_SECTION_AUD", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

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
