package lk.sampath.casadminportalms.service;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

public interface UpmGroupService {

  ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupTempByID(Integer upmGroupID)
      throws ApiRequestException;

  ResponseEntity<StandardResponse<List<UpmGroupDTO>>> findAllUpmGroupTempList()
      throws ApiRequestException;

  ResponseEntity<StandardResponse<UpmGroupDTO>> findUpmGroupById(int upmGroupID);

  ResponseEntity<StandardResponse<List<UpmGroupDTO>>> searchUpmGroups();

  ResponseEntity<StandardResponse<UpmGroupDTO>> saveUPMGroupTemp(UpmGroupDTO upmGroupDTO)
      throws ApiRequestException;

  ResponseEntity<StandardResponse<UpmGroupDTO>> updateUpmGroupTemp(
      Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException;

  ResponseEntity<StandardResponse<UpmGroupDTO>> approveRejectUpmGroup(
      ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

  ResponseEntity<StandardResponse<UpmGroupDTO>> updateApprovedUpmGroup(
      Integer upmGroupID, UpmGroupDTO upmGroupDTO) throws ApiRequestException;

  ResponseEntity<StandardResponse<Void>> deleteUpmGroup(Integer upmGroupID)
      throws ApiRequestException;

  ResponseEntity<StandardResponse<List<UpmGroupDTO>>> findAllActiveUpmGroups()
      throws ApiRequestException;
}
