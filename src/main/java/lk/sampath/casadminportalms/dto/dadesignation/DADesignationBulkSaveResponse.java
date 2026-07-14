package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DADesignationBulkSaveResponse {

    private List<DADesignationSaveResponse> committee = new ArrayList<>();
    private List<DADesignationSaveResponse> individual = new ArrayList<>();
}
