package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.List;

@Data
public class DATableHeaderDTO {
//    List<DAHeaderResponse> individualTableHeaders;
//    List<DAHeaderResponse> committeeTableHeaders;

    private List<DATableHeadingResponse> individualTableHeaders;
    private List<DATableHeadingResponse> committeeTableHeaders;

}
