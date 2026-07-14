package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DATableDataResponse {

    private List<DATableHeadingResponse> committeeTableHeaders = new ArrayList<>();
    private List<DADesignationSaveResponse> committeeRows = new ArrayList<>();

    private List<DATableHeadingResponse> individualTableHeaders = new ArrayList<>();
    private List<DADesignationSaveResponse> individualRows = new ArrayList<>();
}
