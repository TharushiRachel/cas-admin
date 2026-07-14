package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DADesignationBulkSaveRequest {

    /**
     * Rows for the committee table (isCommittee = Y).
     * tableType on each item is optional; COMMITTEE is applied automatically.
     */
    private List<DADesignationSaveRequest> committee = new ArrayList<>();

    /**
     * Rows for the individual table (isCommittee = N).
     * tableType on each item is optional; INDIVIDUAL is applied automatically.
     */
    private List<DADesignationSaveRequest> individual = new ArrayList<>();
}
