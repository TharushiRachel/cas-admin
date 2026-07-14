package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DADesignationBulkSaveRequest {

    private List<DADesignationSaveRequest> committee = new ArrayList<>();

    private List<DADesignationSaveRequest> individual = new ArrayList<>();
}
