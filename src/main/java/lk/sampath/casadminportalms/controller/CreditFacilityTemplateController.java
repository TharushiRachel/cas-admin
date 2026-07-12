package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.CreditFacilityTemplateDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CreditFacilityTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creditFacilityTemplates")
public class CreditFacilityTemplateController {

    private final CreditFacilityTemplateService creditFacilityTemplateService;

    public CreditFacilityTemplateController(CreditFacilityTemplateService creditFacilityTemplateService) {
        this.creditFacilityTemplateService = creditFacilityTemplateService;
    }

    @GetMapping("/getAllCftTemp")
    public ResponseEntity<StandardResponse<Page<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplatesTemp(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        int effectivePage = headerPage != null ? headerPage : page;
        int effectiveSize = headerSize != null ? headerSize : size;
        Pageable pageable = PageRequest.of(effectivePage, effectiveSize);
        ResponseEntity<StandardResponse<Page<CreditFacilityTemplateDTO>>> pagedCreditFacilityTemplates = creditFacilityTemplateService.getAllCreditFacilityTemplatesTemp(pageable);
        return ResponseEntity.ok().body(pagedCreditFacilityTemplates.getBody());
    }

    @GetMapping("/getCftTempById/{creditFacilityTemplateID}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateTempById(@PathVariable Integer creditFacilityTemplateID) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateTemp = creditFacilityTemplateService.getCreditFacilityTemplateTempByID(creditFacilityTemplateID);
        return ResponseEntity.ok().body(creditFacilityTemplateTemp.getBody());
    }

    @GetMapping("/getAllCft")
    public ResponseEntity<StandardResponse<Page<CreditFacilityTemplateDTO>>> getCreditFacilityTemplates(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        int effectivePage = headerPage != null ? headerPage : page;
        int effectiveSize = headerSize != null ? headerSize : size;
        Pageable pageable = PageRequest.of(effectivePage, effectiveSize);
        ResponseEntity<StandardResponse<Page<CreditFacilityTemplateDTO>>> pagedCreditFacilityTemplates = creditFacilityTemplateService.getAllCreditFacilityTemplates(pageable);
        return ResponseEntity.ok().body(pagedCreditFacilityTemplates.getBody());
    }

    @GetMapping("/getCftById/{creditFacilityTemplateID}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateById(@PathVariable Integer creditFacilityTemplateID) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplate = creditFacilityTemplateService.getCreditFacilityTemplateByID(creditFacilityTemplateID);
        return ResponseEntity.ok().body(creditFacilityTemplate.getBody());
    }

    @PostMapping("/saveAndUpdateCftTemp")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> saveCreditFacilityTemplateTemp(@Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateTemp = creditFacilityTemplateService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(creditFacilityTemplateTemp.getBody());
    }

    @PostMapping("/updateCftTemp/{creditFacilityTemplateID}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplateTemp(@PathVariable Integer creditFacilityTemplateID, @Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateTemp = creditFacilityTemplateService.updateCreditFacilityTemplateTemp(creditFacilityTemplateID, creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(creditFacilityTemplateTemp.getBody());
    }

    @PostMapping("/authorizeCft")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> authorizeCreditFacilityTemplate(@RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> approveRejectResponse = creditFacilityTemplateService.authorizeCreditFacilityTemplate(approveRejectRQ);
        return ResponseEntity.ok().body(approveRejectResponse.getBody());
    }

    @PostMapping("/updateCft/{creditFacilityTemplateID}")
    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplate(@PathVariable Integer creditFacilityTemplateID, @Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> creditFacilityTemplateUpdatedDTO = creditFacilityTemplateService.updateCreditFacilityTemplate(creditFacilityTemplateID, creditFacilityTemplateDTO);
        return ResponseEntity.ok().body(creditFacilityTemplateUpdatedDTO.getBody());
    }

    @PostMapping("/deleteCftTemp")
    public ResponseEntity<StandardResponse<Void>> deleteCreditFacilityTemplateTemp(@Validated @RequestBody CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<Void>> approveRejectResponse = creditFacilityTemplateService.deleteCreditFacilityTemplateTemp(creditFacilityTemplateDTO.getCreditFacilityTemplateID());
        return ResponseEntity.ok().body(approveRejectResponse.getBody());
    }

}
