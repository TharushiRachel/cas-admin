package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeePool.CommitteePoolDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommitteePoolService{

//    default ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool()throws ApiRequestException {
//        return getTempCommitteePool(0, 10);
//    }
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool() throws ApiRequestException;

//    default ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool()throws ApiRequestException {
//        return getCommitteePool(0, 10);
//    }
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool()throws ApiRequestException;

//    default ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveCommitteePoolUsers(List<CommitteePoolDTO> committeePoolUsers)throws ApiRequestException {
//        return saveCommitteePoolUsers(committeePoolUsers, 0, 10);
//    }
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveCommitteePoolUsers(List<CommitteePoolDTO> committeePoolUsers)throws ApiRequestException;

//    default ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveTempCommitteePoolUser(CommitteePoolDTO committeePoolUser)throws ApiRequestException {
//        return saveTempCommitteePoolUser(committeePoolUser, 0, 10);
//    }
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveTempCommitteePoolUser(CommitteePoolDTO committeePoolUser)throws ApiRequestException;

    public ResponseEntity<StandardResponse<CommitteePoolDTO>> approveRejectPoolUser(CommitteePoolDTO committeePoolUser)throws ApiRequestException;
}
