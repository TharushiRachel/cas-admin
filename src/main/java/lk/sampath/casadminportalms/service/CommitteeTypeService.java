package lk.sampath.casadminportalms.service;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

public interface CommitteeTypeService {

  ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> getCommitteeTypes()
      throws ApiRequestException;

  ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> saveCommitteeType(
      CommitteeTypeDTO committeeTypeDTO) throws ApiRequestException;

  ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> updateCommitteeType(
      CommitteeTypeDTO request) throws ApiRequestException;
}
