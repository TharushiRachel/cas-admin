package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeePool.CommitteePoolDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommitteePoolService{

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool(Pageable pageable)throws ApiRequestException;

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool(Pageable pageable)throws ApiRequestException;

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveCommitteePoolUsers(List<CommitteePoolDTO> committeePoolUsers, Pageable pageable)throws ApiRequestException;

    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveTempCommitteePoolUser(CommitteePoolDTO committeePoolUser, Pageable pageable)throws ApiRequestException;

    public ResponseEntity<StandardResponse<CommitteePoolDTO>> approveRejectPoolUser(CommitteePoolDTO committeePoolUser)throws ApiRequestException;
}
