package lk.sampath.casadminportalms.controller;


import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UpcTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UpcTemplateController {

    @Autowired
    private UpcTemplateService upcTemplateService;

    @GetMapping("${app.endpoint.upcTemplateTempList}")
    public ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllUpcTemplateTempList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> spcTemplates = upcTemplateService.findAllUpcTemplateTempList(pageable);
        return ResponseEntity.ok().body(spcTemplates.getBody());
    }

    @GetMapping("${app.endpoint.UpcTemplateTempById}")
    public ResponseEntity<StandardResponse<Object>> upcTemplateTempById(@PathVariable Integer upcTemplateID) throws ApiRequestException {
        ResponseEntity<StandardResponse<Object>> upcTemplate = upcTemplateService.findUpcTemplateTempById(upcTemplateID);
        return ResponseEntity.ok().body(upcTemplate.getBody());
    }

    @GetMapping("${app.endpoint.findAllApprovedUpcTemplateList}")
    public ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllApprovedUpcTemplateList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> upcTemplates = upcTemplateService.findAllApprovedUpcTemplates(pageable);
        return ResponseEntity.ok().body(upcTemplates.getBody());
    }

    @GetMapping("${app.endpoint.findApprovedUpcTemplateById}")
    public ResponseEntity<StandardResponse<Object>> findApprovedUpcTemplateById(@PathVariable Integer upcTemplateID) throws ApiRequestException {
        ResponseEntity<StandardResponse<Object>> upcTemplate = upcTemplateService.findApprovedUpcTemplateById(upcTemplateID);
        return ResponseEntity.ok().body(upcTemplate.getBody());
    }

    @PostMapping("${app.endpoint.saveUpcTemplate}")
    public ResponseEntity<StandardResponse<Object>> saveUpcTemplate(@Validated @RequestBody UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<Object>> upcTemplate = upcTemplateService.saveUpcTemplate(upcTemplateDTO);
        return ResponseEntity.ok().body(upcTemplate.getBody());
    }

    @PostMapping("${app.endpoint.updateUpcTemplate}")
    public ResponseEntity<StandardResponse<Object>> updateUpcTemplate(@PathVariable Integer upcTemplateID, @Validated @RequestBody UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<Object>> upcTemplate = upcTemplateService.updateUpcTemplateTemp(upcTemplateID, upcTemplateDTO);
        return ResponseEntity.ok().body(upcTemplate.getBody());
    }

    @PostMapping("${app.endpoint.approveRejectUpcTemplate}")
    public ResponseEntity<StandardResponse<Object>> approveRejectUpcTemplate(@RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        ResponseEntity<StandardResponse<Object>> upcTemplate = upcTemplateService.approveRejectUpcTemplate(approveRejectRQ);
        return ResponseEntity.ok().body(upcTemplate.getBody());
    }

    @PostMapping("${app.endpoint.updateUpcTemplateMaster}")
    public ResponseEntity<StandardResponse<Object>> updateApprovedUpcTemplate(@PathVariable Integer upcTemplateID, @Validated @RequestBody UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<Object>> upcTemplate = upcTemplateService.updateApprovedUpcTemplate(upcTemplateID, upcTemplateDTO);
        return ResponseEntity.ok().body(upcTemplate.getBody());
    }

    @PostMapping("${app.endpoint.deleteUpcTemplateFromTemp}")
    public ResponseEntity<StandardResponse<Void>> deleteUpcTemplateFromTemp(@Validated @RequestBody UpcTemplateDTO upcTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<Void>> response = upcTemplateService.deleteUpcTemplateFromTemp(upcTemplateDTO.getUpcTemplateID());
        return ResponseEntity.ok().body(response.getBody());
    }
}
