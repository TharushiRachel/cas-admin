package lk.sampath.casadminportalms.controller;


import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.supportingdoc.SupportingDocDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.SupportingDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SupportingDocController {

    @Autowired
    private SupportingDocService supportingDocService;


    @GetMapping("${app.endpoint.viewSupportingDocTempList}")
    public ResponseEntity<StandardResponse<List<SupportingDocDTO>>> viewAllSupportingDocsTemp(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<StandardResponse<List<SupportingDocDTO>>> supportingDocTempList = supportingDocService.findAllSupportingDocTempList(pageable);
        return ResponseEntity.ok().body(supportingDocTempList.getBody());
    }

    @GetMapping("${app.endpoint.supportingDocTempViewById}")
    public ResponseEntity<StandardResponse<SupportingDocDTO>> viewSupportingDocTempById(@PathVariable Integer supportingDocID) throws ApiRequestException {
        ResponseEntity<StandardResponse<SupportingDocDTO>> supportingDocDTO = supportingDocService.findSupportingDocTempById(supportingDocID);
        return ResponseEntity.ok().body(supportingDocDTO.getBody());
    }
    @GetMapping("${app.endpoint.viewSupportingDocList}")
    public ResponseEntity<StandardResponse<List<SupportingDocDTO>>> getApprovedSupportingDocData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<StandardResponse<List<SupportingDocDTO>>> supportingDocs = supportingDocService.searchSupportingDocGroups(pageable);
        return ResponseEntity.ok().body(supportingDocs.getBody());
    }


    @GetMapping("${app.endpoint.viewSupportingDocById}")
    public ResponseEntity<StandardResponse<SupportingDocDTO>> viewSupportingDocById(@PathVariable Integer supportingDocID) throws ApiRequestException {
        ResponseEntity<StandardResponse<SupportingDocDTO>> supportingDocDTO= supportingDocService.findSupportingDocById(supportingDocID);
        return ResponseEntity.ok().body(supportingDocDTO.getBody());
    }

    @PostMapping("${app.endpoint.saveSupportingDoc}")
    public ResponseEntity<StandardResponse<SupportingDocDTO>> saveSupportingDoc(@Validated @RequestBody SupportingDocDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<SupportingDocDTO>> supportingDocTempSave = supportingDocService.saveSupportingDocTemp(request);
        return ResponseEntity.ok().body(supportingDocTempSave.getBody());
    }

    @PostMapping("${app.endpoint.supportingDocApproveReject}")
    public ResponseEntity<StandardResponse<SupportingDocDTO>> approveRejectSupportingDoc(@Validated @RequestBody ApproveRejectRQ request) throws ApiRequestException {
        ResponseEntity<StandardResponse<SupportingDocDTO>> approveSupportingDoc = supportingDocService.approveRejectSupportingDoc(request);
        return ResponseEntity.ok().body(approveSupportingDoc.getBody());
    }

    @PostMapping("${app.endpoint.updateSupportingDocTemp}")
    public ResponseEntity<StandardResponse<SupportingDocDTO>> updateSupportingDocTemp(@PathVariable Integer supportingDocID, @Validated @RequestBody SupportingDocDTO request) throws  ApiRequestException {
        ResponseEntity<StandardResponse<SupportingDocDTO>> updateSupportingDoc = supportingDocService.updateSupportingDocTemp(supportingDocID, request);
        return ResponseEntity.ok().body(updateSupportingDoc.getBody());
    }

    @PostMapping("${app.endpoint.updateApprovedSupportingDoc}")
    public  ResponseEntity<StandardResponse<SupportingDocDTO>> updateApprovedSupportingDoc(@PathVariable Integer supportingDocID, @Validated @RequestBody SupportingDocDTO request) throws  ApiRequestException {
        ResponseEntity<StandardResponse<SupportingDocDTO>> updateApprovedSupportingDocs = supportingDocService.updateApprovedSupportingDoc(supportingDocID, request);
        return ResponseEntity.ok().body(updateApprovedSupportingDocs.getBody());
    }


    @PostMapping("${app.endpoint.deleteSupportingDocTemp}")
    public ResponseEntity<StandardResponse<Void>> deleteSupportingDocTemp (@Validated @RequestBody SupportingDocDTO supportingDocDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<Void>> supportingDoc = supportingDocService.deleteSupportingDocTemp(supportingDocDTO.getSupportingDocID());
        return ResponseEntity.ok().body(supportingDoc.getBody());
    }

}
