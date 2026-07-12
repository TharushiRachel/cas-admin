package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacility.CreditFacilityTypeDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CreditFacilityTypeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *
 * @author yomesh
 */


@RestController
@RequestMapping("/creditFacilityType")
public class CreditFacilityTypeController {

    private final CreditFacilityTypeService creditFacilityTypeService;

    public CreditFacilityTypeController(CreditFacilityTypeService creditFacilityTypeService) {
        this.creditFacilityTypeService = creditFacilityTypeService;
    }

    @PostMapping("/saveCreditFacilityType")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> saveCreditFacilityType(@Validated @RequestBody CreditFacilityTypeDTO request) throws ApiRequestException {

        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> saveCreditFacilityType = creditFacilityTypeService.saveCreditFacilityTypeTemp(request);
        return ResponseEntity.ok().body(saveCreditFacilityType.getBody());
    }

    @GetMapping("/getCreditFacilityTempTypeById/{creditFacilityTypeID}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> getCreditFacilityTypeTempById(@PathVariable Integer creditFacilityTypeID) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>>  creditFacilityTypeTemp = creditFacilityTypeService.findCreditFacilityTypeTempByID(creditFacilityTypeID);
        return ResponseEntity.ok().body(creditFacilityTypeTemp.getBody());
    }

    @PostMapping("/updateCreditFacilityTempType/{creditFacilityTypeID}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateCreditFacilityTypeTemp(@PathVariable Integer creditFacilityTypeID, @Validated @RequestBody CreditFacilityTypeDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> facilityTypeTemp = creditFacilityTypeService.updateCreditFacilityTempType(creditFacilityTypeID, request);

        return ResponseEntity.ok().body(facilityTypeTemp.getBody());
    }

    @PostMapping("/approveRejectCreditFacilityType")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> approveRejectCreditFacilityTypeTemp(@Validated @RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {

        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> approveCreditFacilityType = creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ);

        return ResponseEntity.ok().body(approveCreditFacilityType.getBody());
    }


    @GetMapping("/getCreditFacilityTypeMasterList")
    public  ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> getCreditFacilityTypeMasterList(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {

        int effectivePage = headerPage != null ? headerPage : page;
        int effectiveSize = headerSize != null ? headerSize : size;
        Pageable pageable = PageRequest.of(effectivePage, effectiveSize);
        ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>>  creditFacilityTypeDTOList =  creditFacilityTypeService.searchCreditFacilityTypes(pageable);

        return ResponseEntity.ok().body(creditFacilityTypeDTOList.getBody());
    }

    @GetMapping("/getCreditFacilityType/{creditFacilityTypeID}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> getCreditFacilityTypeId(@PathVariable Integer creditFacilityTypeID) throws ApiRequestException {

        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> creditFacilityType = creditFacilityTypeService.findCreditFacilityTypeByID(creditFacilityTypeID);


        return ResponseEntity.ok().body(creditFacilityType.getBody());
    }

    @GetMapping("/getCreditFacilityTypeTempList")
    public ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> getCreditFacilityTypeTempList(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        int effectivePage = headerPage != null ? headerPage : page;
        int effectiveSize = headerSize != null ? headerSize : size;
        Pageable pageable = PageRequest.of(effectivePage, effectiveSize);
        ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> creditFacilityTypeTempList = creditFacilityTypeService.findAllCreditFacilityTypeTempList(pageable);

        return ResponseEntity.ok().body(creditFacilityTypeTempList.getBody());
    }

    @PostMapping("/deleteCreditFacilityTypeTemp")
    public ResponseEntity<StandardResponse<Integer>> deleteCreditFacilityTypeTemp( @RequestBody  CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException {

        ResponseEntity<StandardResponse<Integer>> response =  creditFacilityTypeService.deleteCreditFacilityTypeTemp( creditFacilityTypeTempDTO);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("/updateCreditFacilityMasterType/{creditFacilityTypeID}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateCreditFacilityTypeMaster(@PathVariable Integer creditFacilityTypeID, @Validated @RequestBody CreditFacilityTypeDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> facilityTypeTemp = creditFacilityTypeService.updateApprovedCreditFacilityType(creditFacilityTypeID, request);

        return ResponseEntity.ok().body(facilityTypeTemp.getBody());
    }
}
