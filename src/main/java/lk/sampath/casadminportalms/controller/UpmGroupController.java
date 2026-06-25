package lk.sampath.casadminportalms.controller;


import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UpmGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/upmGroup")
public class UpmGroupController {

    @Autowired
    private UpmGroupService masterDataService;


    @GetMapping("/upmGroupTemp/{upmGroupID}")
    public ResponseEntity<StandardResponse<UpmGroupDTO>> viewUpmGroupTempByID(@PathVariable int upmGroupID) throws ApiRequestException{
        ResponseEntity<StandardResponse<UpmGroupDTO>> upmGroupTempDTO = masterDataService.findUpmGroupTempByID(upmGroupID);
        return ResponseEntity.ok().body(upmGroupTempDTO.getBody());
    }

    @GetMapping("/upmGroupTemp")
    public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> listUpmGroupTemp(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException{
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<UpmGroupDTO>>> upmGroupTempList = masterDataService.findAllUpmGroupTempList(pageable);
        return ResponseEntity.ok().body(upmGroupTempList.getBody());
    }

    @GetMapping("/{upmGroupID}")
    public ResponseEntity<StandardResponse<UpmGroupDTO>> viewUpmGroupById(@PathVariable int upmGroupID){
        ResponseEntity<StandardResponse<UpmGroupDTO>> upmGroup = masterDataService.findUpmGroupById((upmGroupID));
        return ResponseEntity.ok().body(upmGroup.getBody());
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<UpmGroupDTO>>> getPagedUpmGroupData(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<UpmGroupDTO>>> upmGroupList = masterDataService.searchUpmGroups(pageable);
        return ResponseEntity.ok().body(upmGroupList.getBody());
    }

    @PostMapping("/saveUpmGroup")
    public ResponseEntity<StandardResponse<UpmGroupDTO>> saveUPMGroup(@Validated @RequestBody UpmGroupDTO request) throws ApiRequestException{
        ResponseEntity<StandardResponse<UpmGroupDTO>> upmGroup = masterDataService.saveUPMGroupTemp(request);
        return ResponseEntity.ok().body(upmGroup.getBody());
    }

    @PostMapping("/updateUpmGroupTemp/{upmGroupID}")
    public ResponseEntity<StandardResponse<UpmGroupDTO>> updateUpmGroupTemp(@PathVariable int upmGroupID, @Validated @RequestBody UpmGroupDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpmGroupDTO>> upmGroup = masterDataService.updateUpmGroupTemp(upmGroupID, request);
        return ResponseEntity.ok().body(upmGroup.getBody());
    }

    @PostMapping("/approvedRejectUpmGroup")
    public ResponseEntity<StandardResponse<UpmGroupDTO>> approveRejectUpmGroup(@Validated @RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpmGroupDTO>> upmGroup = masterDataService.approveRejectUpmGroup(approveRejectRQ);
        return ResponseEntity.ok().body(upmGroup.getBody());
    }

    @PostMapping("/updateUpmGroup/{upmGroupID}")
    public ResponseEntity<StandardResponse<UpmGroupDTO>> updateApprovedUpmGroup(@PathVariable int upmGroupID, @Validated @RequestBody UpmGroupDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<UpmGroupDTO>> upmGroup = masterDataService.updateApprovedUpmGroup(upmGroupID, request);
        return ResponseEntity.ok().body(upmGroup.getBody());
    }

    @PostMapping("/deleteUpmGroup")
    public ResponseEntity<StandardResponse<Void>> deleteUpmGroup(@Validated @RequestBody UpmGroupDTO request) throws ApiRequestException {
        ResponseEntity<StandardResponse<Void>> upmGroup = masterDataService.deleteUpmGroup(request.getUpmGroupID());
        return ResponseEntity.ok().body(upmGroup.getBody());
    }
}
