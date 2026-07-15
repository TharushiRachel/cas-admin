package lk.sampath.casadminportalms.controller;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDTO;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UpcTemplateService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/upcTemplate")
public class UpcTemplateController {

  private final UpcTemplateService upcTemplateService;

  public UpcTemplateController(UpcTemplateService upcTemplateService) {
    this.upcTemplateService = upcTemplateService;
  }

    @GetMapping("/upcTemplateTemp/UpcTemplateTempList")
    public ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllUpcTemplateTempList(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
      ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> spcTemplates = upcTemplateService.findAllUpcTemplateTempList(pageable);
        return ResponseEntity.ok().body(spcTemplates.getBody());
    }

  @GetMapping("/upcTemplateTemp/UpcTemplateTempById/{upcTemplateID}")
  public ResponseEntity<StandardResponse<Object>> upcTemplateTempById(
      @PathVariable Integer upcTemplateID) throws ApiRequestException {
    ResponseEntity<StandardResponse<Object>> upcTemplate =
        upcTemplateService.findUpcTemplateTempById(upcTemplateID);
    return ResponseEntity.ok().body(upcTemplate.getBody());
  }

    @GetMapping("/upcTemplateTemp/findAllApprovedUpcTemplates")
    public ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllApprovedUpcTemplateList(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
      ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> upcTemplates = upcTemplateService.findAllApprovedUpcTemplates(pageable);
        return ResponseEntity.ok().body(upcTemplates.getBody());
    }

  @GetMapping("/findApprovedUpcTemplateById/{upcTemplateID}")
  public ResponseEntity<StandardResponse<Object>> findApprovedUpcTemplateById(
      @PathVariable Integer upcTemplateID) throws ApiRequestException {
    ResponseEntity<StandardResponse<Object>> upcTemplate =
        upcTemplateService.findApprovedUpcTemplateById(upcTemplateID);
    return ResponseEntity.ok().body(upcTemplate.getBody());
  }

  @PostMapping
  public ResponseEntity<StandardResponse<Object>> saveUpcTemplate(
      @Validated @RequestBody UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
    ResponseEntity<StandardResponse<Object>> upcTemplate =
        upcTemplateService.saveUpcTemplate(upcTemplateDTO);
    return ResponseEntity.ok().body(upcTemplate.getBody());
  }

  @PostMapping("/updateUpcTemplateTemp/{upcTemplateID}")
  public ResponseEntity<StandardResponse<Object>> updateUpcTemplate(
      @PathVariable Integer upcTemplateID, @Validated @RequestBody UpcTemplateDTO upcTemplateDTO)
      throws ApiRequestException {
    ResponseEntity<StandardResponse<Object>> upcTemplate =
        upcTemplateService.updateUpcTemplateTemp(upcTemplateID, upcTemplateDTO);
    return ResponseEntity.ok().body(upcTemplate.getBody());
  }

  @PostMapping("/approveRejectUpcTemplate")
  public ResponseEntity<StandardResponse<Object>> approveRejectUpcTemplate(
      @RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
    ResponseEntity<StandardResponse<Object>> upcTemplate =
        upcTemplateService.approveRejectUpcTemplate(approveRejectRQ);
    return ResponseEntity.ok().body(upcTemplate.getBody());
  }

  @PostMapping("/updateUpcTemplate/{upcTemplateID}")
  public ResponseEntity<StandardResponse<Object>> updateApprovedUpcTemplate(
      @PathVariable Integer upcTemplateID, @Validated @RequestBody UpcTemplateDTO upcTemplateDTO)
      throws ApiRequestException {
    ResponseEntity<StandardResponse<Object>> upcTemplate =
        upcTemplateService.updateApprovedUpcTemplate(upcTemplateID, upcTemplateDTO);
    return ResponseEntity.ok().body(upcTemplate.getBody());
  }

  @PostMapping("/upcTemplateTemp/deleteUpcTemplateFromTemp")
  public ResponseEntity<StandardResponse<Void>> deleteUpcTemplateFromTemp(
      @Validated @RequestBody UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
    ResponseEntity<StandardResponse<Void>> response =
        upcTemplateService.deleteUpcTemplateFromTemp(upcTemplateDTO.getUpcTemplateID());
    return ResponseEntity.ok().body(response.getBody());
  }
}
