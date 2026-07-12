package lk.sampath.casadminportalms.entity.daDesignation;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "CASV3_T_DA_TABLE_HEADER")
@Data
@ToString
public class DATableHeader {

    @Id
    private Long id;

    @Column(name = "TABLE_TYPE")
    private String tableType;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "LEVEL_NO")
    private Integer levelNo;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "COL_SPAN")
    private Integer colSpan;

    @Column(name = "ROW_SPAN")
    private Integer rowSpan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private DATableHeader parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("displayOrder ASC")
    private List<DATableHeader> children;
}

