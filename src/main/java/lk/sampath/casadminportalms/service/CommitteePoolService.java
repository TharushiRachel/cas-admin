package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeePool.CommitteePoolDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommitteePoolService{

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool()throws ApiRequestException;

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool()throws ApiRequestException;

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveCommitteePoolUsers(List<CommitteePoolDTO> committeePoolUsers)throws ApiRequestException;

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveTempCommitteePoolUser(CommitteePoolDTO committeePoolUser)throws ApiRequestException;

    public ResponseEntity<StandardResponse<CommitteePoolDTO>> approveRejectPoolUser(CommitteePoolDTO committeePoolUser)throws ApiRequestException;
}
