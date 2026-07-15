package lk.sampath.casadminportalms.service;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committee.CommitteeDTO;
import lk.sampath.casadminportalms.dto.committee.CommitteeLevelDTO;
import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

public interface CommitteeService {

  ResponseEntity<StandardResponse<List<CommitteeDTO>>> getTempCommittees()
      throws ApiRequestException;

  ResponseEntity<StandardResponse<CommitteeDTO>> getTempCommitteeById(Integer committeeId)
      throws ApiRequestException;

  ResponseEntity<StandardResponse<CommitteeDTO>> saveTempCommittee(CommitteeDTO committeeDTO)
      throws ApiRequestException;

  ResponseEntity<StandardResponse<List<CommitteeDTO>>> getCommittees() throws ApiRequestException;

  ResponseEntity<StandardResponse<CommitteeDTO>> getCommitteeById(Integer committeeId)
      throws ApiRequestException;

  ResponseEntity<StandardResponse<CommitteeDTO>> approveRejectCommittee(CommitteeDTO committeeDTO)
      throws ApiRequestException;

  StandardResponse<Boolean> deleteCommitteeTemp(Integer committeeId) throws ApiRequestException;

  ResponseEntity<StandardResponse<List<CommitteeLevelDTO>>> getCommitteeLevelsByCommittee(
      Integer committeeId) throws ApiRequestException;

  void saveMasterCommitteeHistory(Committee committee);
}
