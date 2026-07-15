package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DATableApprovalResponse {

    /** Designations from DA_DESIGNATION that have committee (isCommittee = Y) rows. */
    private List<DADesignationListDTO> committeeDesignationList = new ArrayList<>();

    /** Designations from DA_DESIGNATION that have individual (isCommittee = N) rows. */
    private List<DADesignationListDTO> individualDesignationList = new ArrayList<>();

    /** Built from DA_LIMITS (approved values only). */
    private DATableDataResponse approved = new DATableDataResponse();

    /** Built from DA_LIMITS_TEMP (pending values only). */
    private DATableDataResponse pending = new DATableDataResponse();
}
