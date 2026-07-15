package lk.sampath.casadminportalms.controller;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolDTO;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolResp;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CommitteePoolService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/committeePool")
public class CommitteePoolController {

  private final CommitteePoolService committeePoolService;

  @Autowired
  public CommitteePoolController(CommitteePoolService committeePoolService) {
    this.committeePoolService = committeePoolService;
  }

  @GetMapping("/getTempCommitteePool")
  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool()
      throws ApiRequestException {
    log.info("START | getTempCommitteePool - CommitteePoolController");
    ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
        committeePoolService.getTempCommitteePool();
    log.info("END | getTempCommitteePool - CommitteePoolController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("/getCommitteePool")
  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool()
      throws ApiRequestException {
    log.info("START | getCommitteePool - CommitteePoolController");
    ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
        committeePoolService.getCommitteePool();
    log.info("END | getCommitteePool - CommitteePoolController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping(value = "/savePoolUsers")
  public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> savePoolUsers(
      @RequestBody List<CommitteePoolDTO> committeePoolUsers) throws ApiRequestException {
    log.info("START | savePoolUsers - CommitteePoolController | request : {}", committeePoolUsers);
    ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> response =
        committeePoolService.saveCommitteePoolUsers(committeePoolUsers);
    log.info("END | savePoolUsers - CommitteePoolController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("/saveTempPoolUser")
  public ResponseEntity<StandardResponse<CommitteePoolResp>> saveTempPoolUser(
      @RequestBody CommitteePoolDTO committeePoolDTO) throws ApiRequestException {
    log.info("START | saveTempPoolUser - CommitteePoolController | request : {}", committeePoolDTO);
    ResponseEntity<StandardResponse<CommitteePoolResp>> response =
        committeePoolService.saveTempCommitteePoolUser(committeePoolDTO);
    log.info("END | saveTempPoolUser - CommitteePoolController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("/approveRejectCommitteePool")
  public ResponseEntity<StandardResponse<CommitteePoolResp>> approveRejectPoolUser(
      @RequestBody CommitteePoolDTO committeePoolDTO) throws ApiRequestException {
    log.info(
        "START | approveRejectPoolUser - CommitteePoolController | request : {}", committeePoolDTO);
    ResponseEntity<StandardResponse<CommitteePoolResp>> response =
        committeePoolService.approveRejectPoolUser(committeePoolDTO);
    log.info("END | approveRejectPoolUser - CommitteePoolController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }
}
