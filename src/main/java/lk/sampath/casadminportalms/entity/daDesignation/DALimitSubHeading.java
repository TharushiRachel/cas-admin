package lk.sampath.casadminportalms.entity.daDesignation;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CASV3_T_DA_LIMIT_SUB_HEADING")
@Data
public class DALimitSubHeading {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private DATableHeader group;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "SUB_ID")
    private Integer subId;

    @Column(name = "COL_SPAN")
    private Integer colSpan;

    @Column(name = "ROW_SPAN")
    private Integer rowSpan;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;
}

