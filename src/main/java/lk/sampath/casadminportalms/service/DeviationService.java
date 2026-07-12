package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.deviation.DeviationDTO;
import lk.sampath.casadminportalms.dto.deviation.DeviationTypeDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeviationService {

    ResponseEntity<StandardResponse<DeviationTypeDTO>> saveDeviationType(DeviationTypeDTO deviationTypeDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<DeviationTypeDTO>>> getAllDeviationTypes() throws ApiRequestException;

    ResponseEntity<StandardResponse<DeviationTypeDTO>> getDeviationTypeById(Integer deviationTypeId) throws ApiRequestException;

    ResponseEntity<StandardResponse<DeviationDTO>> saveOrUpdateDeviation(DeviationDTO deviationDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<DeviationDTO>> approveOrRejectDeviation(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<DeviationDTO>> updateApprovedDiversion(DeviationDTO deviationDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<DeviationDTO>>> getAllDeviationTempList() throws ApiRequestException;

    ResponseEntity<StandardResponse<DeviationDTO>> getDeviationTempById(Integer deviationId) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<DeviationDTO>>> getAllDeviationMasterList() throws ApiRequestException;

    ResponseEntity<StandardResponse<DeviationDTO>> getDeviationMasterById(Integer deviationId) throws ApiRequestException;

}
