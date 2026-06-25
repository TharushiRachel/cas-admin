package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.CreditFacilityTemplateDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CreditFacilityTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CreditFacilityTemplateController {

    private static final Logger LOG = LoggerFactory.getLogger(CreditFacilityTemplateController.class);

    @Autowired
    private CreditFacilityTemplateService creditFacilityTemplateService;

    @GetMapping("${app.endpoint.getAllCftTemp}")
    public ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplatesTemp(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> pagedCreditFacilityTemplates = creditFacilityTemplateService.getAllCreditFacilityTemplatesTemp(pageable);
        return ResponseEntity.ok().body(pagedCreditFacilityTemplates.getBody());
    }

    @GetMapping("${app.endpoint.getCftTempById}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateTempById(@PathVariable Integer creditFacilityTemplateID) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateTemp = creditFacilityTemplateService.getCreditFacilityTemplateTempByID(creditFacilityTemplateID);
        return ResponseEntity.ok().body(creditFacilityTemplateTemp.getBody());
    }

    @GetMapping("${app.endpoint.getAllCftMaster}")
    public ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> getCreditFacilityTemplates(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> pagedCreditFacilityTemplates = creditFacilityTemplateService.getAllCreditFacilityTemplates(pageable);
        return ResponseEntity.ok().body(pagedCreditFacilityTemplates.getBody());
    }

    @GetMapping("${app.endpoint.getCftById}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateById(@PathVariable Integer creditFacilityTemplateID) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplate = creditFacilityTemplateService.getCreditFacilityTemplateByID(creditFacilityTemplateID);
        return ResponseEntity.ok().body(creditFacilityTemplate.getBody());
    }

    @PostMapping("${app.endpoint.saveAndUpdateCftTemp}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> saveCreditFacilityTemplateTemp(@Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateTemp = creditFacilityTemplateService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(creditFacilityTemplateTemp.getBody());
    }

    @PostMapping("${app.endpoint.updateCftTemp}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplateTemp(@PathVariable Integer creditFacilityTemplateID, @Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateTemp = creditFacilityTemplateService.updateCreditFacilityTemplateTemp(creditFacilityTemplateID, creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(creditFacilityTemplateTemp.getBody());
    }

    @PostMapping("${app.endpoint.authorizeCft}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> authorizeCreditFacilityTemplate(@RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> approveRejectResponse = creditFacilityTemplateService.authorizeCreditFacilityTemplate(approveRejectRQ);
        return ResponseEntity.ok().body(approveRejectResponse.getBody());
    }

    @PostMapping("${app.endpoint.updateCft}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplate(@PathVariable Integer creditFacilityTemplateID, @Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateUpdatedDTO = creditFacilityTemplateService.updateCreditFacilityTemplate(creditFacilityTemplateID, creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(creditFacilityTemplateUpdatedDTO.getBody());
    }

    @PostMapping("${app.endpoint.deleteCftTemp}")
    public ResponseEntity<StandardResponse<Void>> deleteCreditFacilityTemplateTemp(@Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<Void>> approveRejectResponse = creditFacilityTemplateService.deleteCreditFacilityTemplateTemp(creditFacilityTemplateDTO.getCreditFacilityTemplateID());
        return ResponseEntity.ok().body(approveRejectResponse.getBody());
    }

}
