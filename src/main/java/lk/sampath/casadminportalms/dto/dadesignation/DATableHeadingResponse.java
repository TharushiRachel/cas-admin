package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class DATableHeadingResponse {

    private String label;
    private Integer colSpan;
    private Integer rowSpan;
    private Integer subId;
    private List<DATableHeadingResponse> subHeadings;
    private Map<Integer, Double> tableValues = new LinkedHashMap<>();

}
