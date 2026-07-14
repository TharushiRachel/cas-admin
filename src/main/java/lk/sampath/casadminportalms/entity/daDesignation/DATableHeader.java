package lk.sampath.casadminportalms.entity.daDesignation;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "CASV3_T_DA_TABLE_HEADER")
@Data
@ToString
public class DATableHeader {

    @Id
    private Long id;

    @Column(name = "TABLE_TYPE")
    private String tableType;

    @Column(name = "PARENT_ID")
    private Integer parentId;

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

    @Column(name = "SUB_ID")
    private Integer subId;
}

