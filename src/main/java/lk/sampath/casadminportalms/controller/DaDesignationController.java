package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.service.DaDesignationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/getAllLimitHeadings/{tableType}")
    public ResponseEntity<StandardResponse<List<DAHeaderResponse>>> getAllDALimitsAndDesignations(@PathVariable String tableType) {
        return daDesignationService.getAllLimitHeadings(tableType);
    }
}
