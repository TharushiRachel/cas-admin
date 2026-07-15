package lk.sampath.casadminportalms.service;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolDTO;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolResp;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

public interface CommitteePoolService {

  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool()
      throws ApiRequestException;

  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool()
      throws ApiRequestException;

  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> saveCommitteePoolUsers(
      List<CommitteePoolDTO> committeePoolUsers) throws ApiRequestException;

  public ResponseEntity<StandardResponse<CommitteePoolResp>> saveTempCommitteePoolUser(
      CommitteePoolDTO committeePoolUser) throws ApiRequestException;

  public ResponseEntity<StandardResponse<CommitteePoolResp>> approveRejectPoolUser(
      CommitteePoolDTO committeePoolUser) throws ApiRequestException;
}
