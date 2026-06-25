package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.userda.UserDaDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserDaService {
    ResponseEntity<StandardResponse<List<UserDaDTO>>> findAllUserDaTempList(Pageable pageable) throws ApiRequestException;
    ResponseEntity<StandardResponse<UserDaDTO>> findUserDaTempByID(Integer userDaID) throws ApiRequestException;
    ResponseEntity<StandardResponse<List<UserDaDTO>>> findAllApprovedUserDa(Pageable pageable) throws ApiRequestException;
    ResponseEntity<StandardResponse<UserDaDTO>> findApprovedUserDaById(Integer userDaID) throws ApiRequestException;
    ResponseEntity<StandardResponse<UserDaDTO>> saveUserDaTemp(UserDaDTO userDaDTO) throws ApiRequestException;
    ResponseEntity<StandardResponse<UserDaDTO>> approveRejectUserDa(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;
    ResponseEntity<StandardResponse<UserDaDTO>> updateUserDaTemp(Integer userDaID, UserDaDTO userDaDTO) throws ApiRequestException;
    ResponseEntity<StandardResponse<UserDaDTO>> updateApprovedUserDa(Integer userDaID, UserDaDTO userDaDTO) throws ApiRequestException;
    ResponseEntity<StandardResponse<Void>> deleteUserDaFromTemp(Integer userDaID) throws ApiRequestException;

}
