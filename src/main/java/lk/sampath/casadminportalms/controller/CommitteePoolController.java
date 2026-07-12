package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeePool.CommitteePoolDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CommitteePoolService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Log4j2
@RestController
@RequestMapping("/committeePool")
public class CommitteePoolController {

    private final CommitteePoolService committeePoolService;

    @Autowired
    public CommitteePoolController(CommitteePoolService committeePoolService){
        this.committeePoolService = committeePoolService;
    }

    @GetMapping("/getTempCommitteePool")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool() throws ApiRequestException {
//        log.info("START | getTempCommitteePool - CommitteePoolController | request : {}", response);
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response = committeePoolService.getTempCommitteePool();
        log.info("END | getTempCommitteePool - CommitteePoolController | response : {}", response);
        return ResponseEntity.ok().body(response.getBody());
    }

    @GetMapping("/getCommitteePool")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool() throws ApiRequestException {
//        log.info("START | getCommitteePool - CommitteePoolController | request : {}");
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response = committeePoolService.getCommitteePool();
         log.info("END | getCommitteePool - CommitteePoolController | response : {}", response);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping(value = "/savePoolUsers",headers = "Accept=application/json")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  savePoolUsers(
            @RequestBody List<CommitteePoolDTO> committeePoolUsers) throws ApiRequestException {
        log.info("START | savePoolUsers - CommitteePoolController | response : {}", committeePoolUsers);
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  response = committeePoolService.saveCommitteePoolUsers(committeePoolUsers);
        log.info("END | savePoolUsers - CommitteePoolController | response : {}", response);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("/saveTempPoolUser")
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  saveTempPoolUser(@RequestBody CommitteePoolDTO committeePoolDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<List<CommitteePoolDTO>>>  response = committeePoolService.saveTempCommitteePoolUser(committeePoolDTO);
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("/approveRejectCommitteePool")
    public ResponseEntity<StandardResponse<CommitteePoolDTO>> approveRejectPoolUser(@RequestBody CommitteePoolDTO committeePoolDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<CommitteePoolDTO>> response = committeePoolService.approveRejectPoolUser(committeePoolDTO);
        return ResponseEntity.ok().body(response.getBody());
    }
}
