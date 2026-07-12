package lk.sampath.casadminportalms.dto.dadesignation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({"id", "label", "levelNo", "colSpan", "rowSpan", "children"})
public class DAHeaderResponse {

    private Long id;
    private String label;
    private Integer levelNo;
    private Integer colSpan;
    private Integer rowSpan;
    private List<DAHeaderResponse> children;
}
