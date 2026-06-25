package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeePool.CommitteePoolDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CommitteePoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommitteePoolController {

    private final CommitteePoolService committeePoolService;

    @Autowired
    public CommitteePoolController(CommitteePoolService committeePoolService){
        this.committeePoolService = committeePoolService;
    }

    @GetMapping("${app.endpoint.getTempCommitteePool}")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response = committeePoolService.getTempCommitteePool(page, size);
        return ResponseEntity.ok().body(response.getBody());
    }

    @GetMapping("${app.endpoint.getCommitteePool}")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response = committeePoolService.getCommitteePool(page, size);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping(value = "${app.endpoint.savePoolUsers}",headers = "Accept=application/json")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  savePoolUsers(
            @RequestBody List<CommitteePoolDTO> committeePoolUsers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  response = committeePoolService.saveCommitteePoolUsers(committeePoolUsers, page, size);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("${app.endpoint.saveTempPoolUser}")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveTempPoolUser(
            @RequestBody CommitteePoolDTO committeePoolDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  response = committeePoolService.saveTempCommitteePoolUser(committeePoolDTO, page, size);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("${app.endpoint.approveRejectCommitteePool}")
    public ResponseEntity<StandardResponse<CommitteePoolDTO>> approveRejectPoolUser(@RequestBody CommitteePoolDTO committeePoolDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CommitteePoolDTO>> response = committeePoolService.approveRejectPoolUser(committeePoolDTO);
        return ResponseEntity.ok().body(response.getBody());
    }

}
