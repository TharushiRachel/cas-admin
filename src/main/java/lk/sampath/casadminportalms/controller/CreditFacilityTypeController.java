package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacility.CreditFacilityTypeDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CreditFacilityTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CreditFacilityTypeController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CreditFacilityTypeService creditFacilityTypeService;

    @PostMapping("${app.endpoint.saveCreditFacilityType}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> saveCreditFacilityType(@Validated @RequestBody CreditFacilityTypeDTO request) throws ApiRequestException {

        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> saveCreditFacilityType = creditFacilityTypeService.saveCreditFacilityTypeTemp(request);
        return ResponseEntity.ok().body(saveCreditFacilityType.getBody());
    }

    @GetMapping("${app.endpoint.getCreditFacilityTempTypeById}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> getCreditFacilityTypeTempById(@PathVariable Integer creditFacilityTypeID) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>>  creditFacilityTypeTemp = creditFacilityTypeService.findCreditFacilityTypeTempByID(creditFacilityTypeID);
        return ResponseEntity.ok().body(creditFacilityTypeTemp.getBody());
    }

    @PostMapping("${app.endpoint.updateCreditFacilityTempType}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateCreditFacilityTypeTemp(@PathVariable Integer creditFacilityTypeID, @Validated @RequestBody CreditFacilityTypeDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> facilityTypeTemp = creditFacilityTypeService.updateCreditFacilityTempType(creditFacilityTypeID, request);

        return ResponseEntity.ok().body(facilityTypeTemp.getBody());
    }

    @PostMapping("${app.endpoint.creditFacilityTypeApproveReject}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> approveRejectCreditFacilityTypeTemp(@Validated @RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {

        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> approveCreditFacilityType = creditFacilityTypeService.approveRejectCreditFacilityType(approveRejectRQ);

        return ResponseEntity.ok().body(approveCreditFacilityType.getBody());
    }


    @GetMapping("${app.endpoint.getCreditFacilityTypeMasterList}")
        public  ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> getCreditFacilityTypeMasterList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {

        ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>>  creditFacilityTypeDTOList =  creditFacilityTypeService.searchCreditFacilityTypes(page, size);

        return ResponseEntity.ok().body(creditFacilityTypeDTOList.getBody());
    }

    @GetMapping("${app.endpoint.getCreditFacilityType}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> getCreditFacilityTypeId(@PathVariable Integer creditFacilityTypeID) throws ApiRequestException {

        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> creditFacilityType = creditFacilityTypeService.findCreditFacilityTypeByID(creditFacilityTypeID);


        return ResponseEntity.ok().body(creditFacilityType.getBody());
    }

    @GetMapping("${app.endpoint.getCreditFacilityTypeTempList}")
        public ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> getCreditFacilityTypeTempList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {

        ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> creditFacilityTypeTempList = creditFacilityTypeService.findAllCreditFacilityTypeTempList(page, size);

        return ResponseEntity.ok().body(creditFacilityTypeTempList.getBody());
    }

    @PostMapping("${app.endpoint.deleteCreditFacilityTypeTemp}")
    public ResponseEntity<StandardResponse<Integer>> deleteCreditFacilityTypeTemp( @RequestBody  CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException {

        ResponseEntity<StandardResponse<Integer>> response =  creditFacilityTypeService.deleteCreditFacilityTypeTemp( creditFacilityTypeTempDTO);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("${app.endpoint.updateCreditFacilityMasterType}")
    public ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateCreditFacilityTypeMaster(@PathVariable Integer creditFacilityTypeID, @Validated @RequestBody CreditFacilityTypeDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> facilityTypeTemp = creditFacilityTypeService.updateApprovedCreditFacilityType(creditFacilityTypeID, request);

        return ResponseEntity.ok().body(facilityTypeTemp.getBody());
    }
}
