package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.dadesignation.*;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DaDesignationService {

    ResponseEntity<StandardResponse<DATableHeaderDTO>> getAllLimitHeadings() throws ApiRequestException;

    ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> getAllDesignationCodeDetails() throws ApiRequestException;

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> saveDaDesignationLimits(
            DADesignationBulkSaveRequest request) throws ApiRequestException;

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> approveRejectDaDesignationLimits(
            ApproveRejectRQ request) throws ApiRequestException;

    ResponseEntity<StandardResponse<DATableHeaderDTO>> getDaTableById(Integer designationId) throws ApiRequestException;

    ResponseEntity<StandardResponse<DATableApprovalResponse>> getDaTable() throws ApiRequestException;

    ResponseEntity<StandardResponse<DADesignationListDTO>> deleteDaDesignation(Integer designationId) throws ApiRequestException;
}
