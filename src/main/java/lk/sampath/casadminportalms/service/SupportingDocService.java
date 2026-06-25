package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.supportingdoc.SupportingDocDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SupportingDocService {

    ResponseEntity<StandardResponse<List<SupportingDocDTO>>> findAllSupportingDocTempList(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<SupportingDocDTO>>> searchSupportingDocGroups(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> findSupportingDocTempById(Integer supportingDocID) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> findSupportingDocById(Integer supportingDocID) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> saveSupportingDocTemp (SupportingDocDTO supportingDocDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> approveRejectSupportingDoc (ApproveRejectRQ approveRejectRQ) throws  ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> updateSupportingDocTemp (Integer supportingDocID, SupportingDocDTO supportingDocDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> updateApprovedSupportingDoc (Integer supportingDocID, SupportingDocDTO supportingDocDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteSupportingDocTemp(Integer supportingDocID) throws ApiRequestException;
}
