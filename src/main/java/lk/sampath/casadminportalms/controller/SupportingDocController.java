package lk.sampath.casadminportalms.controller;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.supportingdoc.SupportingDocDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.SupportingDocService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supportingDoc")
public class SupportingDocController {

  private final SupportingDocService supportingDocService;

  public SupportingDocController(SupportingDocService supportingDocService) {
    this.supportingDocService = supportingDocService;
  }

  @GetMapping("/supportingDocTemp")
  public ResponseEntity<StandardResponse<List<SupportingDocDTO>>> viewAllSupportingDocsTemp(
      @RequestHeader(name = "page", required = false) Integer headerPage,
      @RequestHeader(name = "size", required = false) Integer headerSize,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size)
      throws ApiRequestException {
    Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
    ResponseEntity<StandardResponse<List<SupportingDocDTO>>> supportingDocTempList =
        supportingDocService.findAllSupportingDocTempList(pageable);
    return ResponseEntity.ok().body(supportingDocTempList.getBody());
  }

  @GetMapping("/supportingDocTemp/{supportingDocID}")
  public ResponseEntity<StandardResponse<SupportingDocDTO>> viewSupportingDocTempById(
      @PathVariable Integer supportingDocID) throws ApiRequestException {
    ResponseEntity<StandardResponse<SupportingDocDTO>> supportingDocDTO =
        supportingDocService.findSupportingDocTempById(supportingDocID);
    return ResponseEntity.ok().body(supportingDocDTO.getBody());
  }

  @GetMapping("/supportingDocsList")
  public ResponseEntity<StandardResponse<List<SupportingDocDTO>>> getApprovedSupportingDocData(
      @RequestHeader(name = "page", required = false) Integer headerPage,
      @RequestHeader(name = "size", required = false) Integer headerSize,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size)
      throws ApiRequestException {
    Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
    ResponseEntity<StandardResponse<List<SupportingDocDTO>>> supportingDocs =
        supportingDocService.searchSupportingDocGroups(pageable);
    return ResponseEntity.ok().body(supportingDocs.getBody());
  }

  @GetMapping("/{supportingDocID}")
  public ResponseEntity<StandardResponse<SupportingDocDTO>> viewSupportingDocById(
      @PathVariable Integer supportingDocID) throws ApiRequestException {
    ResponseEntity<StandardResponse<SupportingDocDTO>> supportingDocDTO =
        supportingDocService.findSupportingDocById(supportingDocID);
    return ResponseEntity.ok().body(supportingDocDTO.getBody());
  }

  @PostMapping
  public ResponseEntity<StandardResponse<SupportingDocDTO>> saveSupportingDoc(
      @Validated @RequestBody SupportingDocDTO request) throws ApiRequestException {
    ResponseEntity<StandardResponse<SupportingDocDTO>> supportingDocTempSave =
        supportingDocService.saveSupportingDocTemp(request);
    return ResponseEntity.ok().body(supportingDocTempSave.getBody());
  }

  @PostMapping("/approveRejectSupportingDoc")
  public ResponseEntity<StandardResponse<SupportingDocDTO>> approveRejectSupportingDoc(
      @Validated @RequestBody ApproveRejectRQ request) throws ApiRequestException {
    ResponseEntity<StandardResponse<SupportingDocDTO>> approveSupportingDoc =
        supportingDocService.approveRejectSupportingDoc(request);
    return ResponseEntity.ok().body(approveSupportingDoc.getBody());
  }

  @PostMapping("/updateSupportingDocTemp/{supportingDocID}")
  public ResponseEntity<StandardResponse<SupportingDocDTO>> updateSupportingDocTemp(
      @PathVariable Integer supportingDocID, @Validated @RequestBody SupportingDocDTO request)
      throws ApiRequestException {
    ResponseEntity<StandardResponse<SupportingDocDTO>> updateSupportingDoc =
        supportingDocService.updateSupportingDocTemp(supportingDocID, request);
    return ResponseEntity.ok().body(updateSupportingDoc.getBody());
  }

  @PostMapping("/updateApprovedSupportingDoc/{supportingDocID}")
  public ResponseEntity<StandardResponse<SupportingDocDTO>> updateApprovedSupportingDoc(
      @PathVariable Integer supportingDocID, @Validated @RequestBody SupportingDocDTO request)
      throws ApiRequestException {
    ResponseEntity<StandardResponse<SupportingDocDTO>> updateApprovedSupportingDocs =
        supportingDocService.updateApprovedSupportingDoc(supportingDocID, request);
    return ResponseEntity.ok().body(updateApprovedSupportingDocs.getBody());
  }

  @PostMapping("/supportingDocTemp/deleteSupportingDocTemp")
  public ResponseEntity<StandardResponse<Void>> deleteSupportingDocTemp(
      @Validated @RequestBody SupportingDocDTO supportingDocDTO) throws ApiRequestException {
    ResponseEntity<StandardResponse<Void>> supportingDoc =
        supportingDocService.deleteSupportingDocTemp(supportingDocDTO.getSupportingDocID());
    return ResponseEntity.ok().body(supportingDoc.getBody());
  }
}
