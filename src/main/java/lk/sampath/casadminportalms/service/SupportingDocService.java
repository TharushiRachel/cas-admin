package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.supportingdoc.SupportingDocDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SupportingDocService {

    default ResponseEntity<StandardResponse<List<SupportingDocDTO>>> findAllSupportingDocTempList() throws ApiRequestException {
        return findAllSupportingDocTempList(0, 10);
    }
    ResponseEntity<StandardResponse<List<SupportingDocDTO>>> findAllSupportingDocTempList(int page, int size) throws ApiRequestException;

    default ResponseEntity<StandardResponse<List<SupportingDocDTO>>> searchSupportingDocGroups() throws ApiRequestException {
        return searchSupportingDocGroups(0, 10);
    }
    ResponseEntity<StandardResponse<List<SupportingDocDTO>>> searchSupportingDocGroups(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> findSupportingDocTempById(Integer supportingDocID) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> findSupportingDocById(Integer supportingDocID) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> saveSupportingDocTemp (SupportingDocDTO supportingDocDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> approveRejectSupportingDoc (ApproveRejectRQ approveRejectRQ) throws  ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> updateSupportingDocTemp (Integer supportingDocID, SupportingDocDTO supportingDocDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<SupportingDocDTO>> updateApprovedSupportingDoc (Integer supportingDocID, SupportingDocDTO supportingDocDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteSupportingDocTemp(Integer supportingDocID) throws ApiRequestException;
}
