package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationCodeDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeaderDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.DaDesignationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/save")
    public ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> saveDaDesignationLimits(
            @Validated @RequestBody DADesignationBulkSaveRequest request) throws ApiRequestException {
        return daDesignationService.saveDaDesignationLimits(request);
    }
}
