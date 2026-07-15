package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.dadesignation.*;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.DaDesignationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/da-designation")
public class DaDesignationController {

    private final DaDesignationService daDesignationService;

    public DaDesignationController(DaDesignationService daDesignationService) {
        this.daDesignationService = daDesignationService;
    }

    @GetMapping("/headers")
    public ResponseEntity<StandardResponse<DATableHeaderDTO>> getHeaders()
            throws ApiRequestException {
        return daDesignationService.getAllLimitHeadings();
    }

    @GetMapping("/getDesignationsFromUPM")
    public ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> getDesignationsFromUPM()
            throws ApiRequestException {
        return daDesignationService.getAllDesignationCodeDetails();
    }

    @PostMapping("/saveOrUpdateDesignationLimits")
    public ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> saveDaDesignationLimits(
            @RequestBody DADesignationBulkSaveRequest request) throws ApiRequestException {
        return daDesignationService.saveDaDesignationLimits(request);
    }

    @PostMapping("/approveRejectDesignationLimits")
    public ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> approveRejectDaDesignationLimits(
            @RequestBody ApproveRejectRQ request) throws ApiRequestException {
        return daDesignationService.approveRejectDaDesignationLimits(request);
    }

    @GetMapping("/table")
    public ResponseEntity<StandardResponse<DATableApprovalResponse>> getDaTable()
            throws ApiRequestException {
        return daDesignationService.getDaTable();
    }

    @GetMapping("/table/{designationId}")
    public ResponseEntity<StandardResponse<DATableApprovalResponse>> getDaTableById(
            @PathVariable Integer designationId) throws ApiRequestException {
        return daDesignationService.getDaTableById(designationId);
    }
}
