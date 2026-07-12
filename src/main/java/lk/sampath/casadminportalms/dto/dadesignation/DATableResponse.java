package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DATableResponse {

    private String tableType;
    private List<DAHeaderResponse> headers = new ArrayList<>();
    private List<DADesignationRowResponse> rows = new ArrayList<>();
}
