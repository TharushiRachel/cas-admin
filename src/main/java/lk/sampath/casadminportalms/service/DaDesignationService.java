package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationCodeDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeaderDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DaDesignationService {

    ResponseEntity<StandardResponse<DATableHeaderDTO>> getAllLimitHeadings() throws ApiRequestException;

    ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> getAllDesignationCodeDetails() throws ApiRequestException;

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> saveDaDesignationLimits(DADesignationBulkSaveRequest request)
            throws ApiRequestException;
}
