package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.List;

@Data
public class DATableHeadingResponse {

    private String label;
    private Integer colSpan;
    private Integer rowSpan;
    private Integer subId;
    private List<DATableHeadingResponse> subHeadings;

}
