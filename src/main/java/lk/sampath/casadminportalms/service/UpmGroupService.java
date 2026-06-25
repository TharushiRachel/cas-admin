package lk.sampath.casadminportalms.service;



import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UpmGroupService {

    ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupTempByID(Integer upmGroupID) throws ApiRequestException;

    default ResponseEntity<StandardResponse<List<UpmGroupDTO>>> findAllUpmGroupTempList() throws ApiRequestException {
        return findAllUpmGroupTempList(0, 10);
    }
    ResponseEntity<StandardResponse<List<UpmGroupDTO>>> findAllUpmGroupTempList(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupById(int upmGroupID);

    default ResponseEntity<StandardResponse<List<UpmGroupDTO>>> searchUpmGroups() {
        return searchUpmGroups(0, 10);
    }
    ResponseEntity<StandardResponse<List<UpmGroupDTO>>> searchUpmGroups(int page, int size);

    ResponseEntity<StandardResponse<UpmGroupDTO>> saveUPMGroupTemp(UpmGroupDTO upmGroupDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpmGroupDTO>> updateUpmGroupTemp(Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpmGroupDTO>> approveRejectUpmGroup(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpmGroupDTO>> updateApprovedUpmGroup(Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteUpmGroup(Integer upmGroupID) throws ApiRequestException;

}
