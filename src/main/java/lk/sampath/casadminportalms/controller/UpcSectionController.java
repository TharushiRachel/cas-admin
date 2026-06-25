package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upcsection.UpcSectionDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UpcSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/upcSection")
public class UpcSectionController {

    @Autowired
    private UpcSectionService upcSectionService;


    @GetMapping("/upcSectionTemp")
    public ResponseEntity<StandardResponse<List<UpcSectionDTO>>> viewAllUpcSectionTemp() throws ApiRequestException {
        ResponseEntity<StandardResponse<List<UpcSectionDTO>>> upcSectionTempList = upcSectionService.findAllUpcSectionTempList();
        return ResponseEntity.ok().body(upcSectionTempList.getBody());
    }

    @GetMapping("/upcSectionTemp/{upcSectionID}")
    public ResponseEntity<StandardResponse<UpcSectionDTO>> viewUpcSectionTempById(@PathVariable Integer upcSectionID) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpcSectionDTO>> upcSectionTemp = upcSectionService.findUpcSectionTempByID(upcSectionID);
        return ResponseEntity.ok().body(upcSectionTemp.getBody());
    }

    @GetMapping("/upcSectionList")
    public ResponseEntity<StandardResponse<List<UpcSectionDTO>>> getPagedUpcSectionData() throws ApiRequestException {
        ResponseEntity<StandardResponse<List<UpcSectionDTO>>> pageDataResult =
            upcSectionService.findAllApprovedUpcSection();

        return ResponseEntity.ok().body(pageDataResult.getBody());
    }


    @GetMapping("/{upcSectionID}")
    public ResponseEntity<StandardResponse<UpcSectionDTO>> viewUpcSectionById(@PathVariable Integer upcSectionID) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpcSectionDTO>>  upcSectionDTO = upcSectionService.findApprovedUpcSectionByID(upcSectionID);
        return ResponseEntity.ok().body(upcSectionDTO.getBody());
    }

    @PostMapping
    public ResponseEntity<StandardResponse<UpcSectionDTO>> saveUpcSection(@Validated @RequestBody UpcSectionDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpcSectionDTO>> saveUpcSection = upcSectionService.saveUpcSectionTemp(request);
        return ResponseEntity.ok().body(saveUpcSection.getBody());
    }

    @PostMapping("/approveRejectUpcSection")
    public ResponseEntity<StandardResponse<UpcSectionDTO>> approveRejectUpcSection(@RequestBody ApproveRejectRQ request) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpcSectionDTO>> approvedUpcSection = upcSectionService.approveRejectUpcSection(request);
        return ResponseEntity.ok().body(approvedUpcSection.getBody());
    }

    @PostMapping("/updateUpcSectionTemp/{upcSectionID}")
    public ResponseEntity<StandardResponse<UpcSectionDTO>> updateUpcSectionTemp(@PathVariable Integer upcSectionID, @Validated @RequestBody UpcSectionDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpcSectionDTO>> upcSectionTemp = upcSectionService.updateUpcSectionTemp(upcSectionID, request);
        return ResponseEntity.ok().body(upcSectionTemp.getBody());
    }

    @PostMapping("/updateUpcSectionMaster/{upcSectionID}")
    public ResponseEntity<StandardResponse<UpcSectionDTO>> updateApprovedUpcSection(@PathVariable Integer upcSectionID, @Validated @RequestBody UpcSectionDTO upcSectionDTO) throws ApiRequestException{
        ResponseEntity<StandardResponse<UpcSectionDTO>> updateApprovedUpcSection = upcSectionService.updateApprovedUpcSection(upcSectionID, upcSectionDTO);
        return ResponseEntity.ok().body(updateApprovedUpcSection.getBody());
    }


    @PostMapping("/upcSectionTemp/deleteUpcSectionTemp")
    public ResponseEntity<StandardResponse<Void>> deleteUpcSectionTemp(@Validated @RequestBody UpcSectionDTO request) throws ApiRequestException{
        ResponseEntity<StandardResponse<Void>> upcSection = upcSectionService.deleteUpcSectionFormTemp(request.getUpcSectionID());
        return ResponseEntity.ok().body(upcSection.getBody());
    }

}
