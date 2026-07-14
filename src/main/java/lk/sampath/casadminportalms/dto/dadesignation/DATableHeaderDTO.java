package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.List;

@Data
public class DATableHeaderDTO {
    private List<DATableHeadingResponse> individualTableHeaders;
    private List<DATableHeadingResponse> committeeTableHeaders;

}
