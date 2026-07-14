package lk.sampath.casadminportalms.dto.dadesignation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lk.sampath.casadminportalms.enums.DaTableType;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({"id", "label", "levelNo", "colSpan", "rowSpan","subId", "children"})
public class DAHeaderResponse {

    private Long id;
    private DaTableType tableType;
    private String parentId;
    private String label;
    private Integer levelNo;
    private Integer displayOrder;
    private Integer colSpan;
    private Integer rowSpan;
    private Integer subId;
    private List<DAHeaderResponse> children;

}
