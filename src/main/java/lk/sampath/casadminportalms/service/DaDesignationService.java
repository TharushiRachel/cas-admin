package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationRowResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAReorderRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DATableResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DaDesignationService {

    ResponseEntity<StandardResponse<List<DAHeaderResponse>>> getAllLimitHeadings(String tableType) throws ApiRequestException;

    ResponseEntity<StandardResponse<DATableResponse>> getDaTable(String tableType, Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<DADesignationRowResponse>> saveDesignation(DADesignationRequest request) throws ApiRequestException;

    ResponseEntity<StandardResponse<DADesignationRowResponse>> updateDesignation(Integer designationId, DADesignationRequest request) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteDesignation(Integer designationId) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> reorderDesignations(DAReorderRequest request) throws ApiRequestException;
}
