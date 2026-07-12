package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationRowResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAReorderRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DATableResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.DaDesignationService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/da-designation")
public class DaDesignationController {

    private final DaDesignationService daDesignationService;

    public DaDesignationController(DaDesignationService daDesignationService) {
        this.daDesignationService = daDesignationService;
    }

    @GetMapping("/headers/{tableType}")
    public ResponseEntity<StandardResponse<List<DAHeaderResponse>>> getHeaders(@PathVariable String tableType)
            throws ApiRequestException {
        return daDesignationService.getAllLimitHeadings(tableType);
    }

    @GetMapping("/getAllLimitHeadings/{tableType}")
    public ResponseEntity<StandardResponse<List<DAHeaderResponse>>> getAllLimitHeadings(@PathVariable String tableType)
            throws ApiRequestException {
        return daDesignationService.getAllLimitHeadings(tableType);
    }

    @GetMapping("/table/{tableType}")
    public ResponseEntity<StandardResponse<DATableResponse>> getDaTable(
            @PathVariable String tableType,
            @RequestHeader(value = "page", required = false) Integer headerPage,
            @RequestHeader(value = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        return daDesignationService.getDaTable(tableType, pageable);
    }

    @PostMapping
    public ResponseEntity<StandardResponse<DADesignationRowResponse>> saveDesignation(
            @Validated @RequestBody DADesignationRequest request) throws ApiRequestException {
        return daDesignationService.saveDesignation(request);
    }

    @PostMapping("/update/{designationId}")
    public ResponseEntity<StandardResponse<DADesignationRowResponse>> updateDesignation(
            @PathVariable Integer designationId,
            @Validated @RequestBody DADesignationRequest request) throws ApiRequestException {
        return daDesignationService.updateDesignation(designationId, request);
    }

    @PostMapping("/delete/{designationId}")
    public ResponseEntity<StandardResponse<Void>> deleteDesignation(@PathVariable Integer designationId)
            throws ApiRequestException {
        return daDesignationService.deleteDesignation(designationId);
    }

    @PostMapping("/reorder")
    public ResponseEntity<StandardResponse<Void>> reorderDesignations(
            @Validated @RequestBody DAReorderRequest request) throws ApiRequestException {
        return daDesignationService.reorderDesignations(request);
    }
}
