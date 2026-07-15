package lk.sampath.casadminportalms.controller;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committee.CommitteeDTO;
import lk.sampath.casadminportalms.dto.committee.CommitteeLevelDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CommitteeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/committee")
public class CommitteeController {

  private final CommitteeService committeeService;

  public CommitteeController(CommitteeService committeeService) {
    this.committeeService = committeeService;
  }

  @GetMapping("getTempCommittees")
  public ResponseEntity<StandardResponse<List<CommitteeDTO>>> getTempCommittees()
      throws ApiRequestException {
    log.info("START : CommitteeController | getTempCommittees");
    ResponseEntity<StandardResponse<List<CommitteeDTO>>> response =
        this.committeeService.getTempCommittees();
    log.info("END : CommitteeController | getTempCommittees | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("getTempCommittee/{committeeId}")
  public ResponseEntity<StandardResponse<CommitteeDTO>> getTempCommittee(
      @PathVariable Integer committeeId) throws ApiRequestException {
    log.info("START : CommitteeController | getTempCommittee");
    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        this.committeeService.getTempCommitteeById(committeeId);
    log.info("END : CommitteeController | getTempCommittee | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("getCommittees")
  public ResponseEntity<StandardResponse<List<CommitteeDTO>>> getCommittees()
      throws ApiRequestException {
    log.info("START : CommitteeController | getCommittees");
    ResponseEntity<StandardResponse<List<CommitteeDTO>>> response =
        this.committeeService.getCommittees();
    log.info("END : CommitteeController | getCommittees | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("getCommittee/{committeeId}")
  public ResponseEntity<StandardResponse<CommitteeDTO>> getCommittee(
      @PathVariable Integer committeeId) throws ApiRequestException {
    log.info("START : CommitteeController | getCommittee");
    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        this.committeeService.getCommitteeById(committeeId);
    log.info("END : CommitteeController | getCommittee | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("saveTempCommittee")
  public ResponseEntity<StandardResponse<CommitteeDTO>> saveTempCommittee(
      @RequestBody CommitteeDTO committeeDTO) throws ApiRequestException {
    log.info("START : CommitteeController | saveTempCommittee | request : {}", committeeDTO);
    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        this.committeeService.saveTempCommittee(committeeDTO);
    log.info("END : CommitteeController | saveTempCommittee | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("approveRejectCommittee")
  public ResponseEntity<StandardResponse<CommitteeDTO>> approveRejectCommittee(
      @RequestBody CommitteeDTO committeeDTO) throws ApiRequestException {
    log.info("START : CommitteeController | approveRejectCommittee | request : {}", committeeDTO);
    ResponseEntity<StandardResponse<CommitteeDTO>> response =
        this.committeeService.approveRejectCommittee(committeeDTO);
    log.info("END : CommitteeController | approveRejectCommittee | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("deleteTempCommittee")
  public ResponseEntity<StandardResponse<Boolean>> deleteTempCommittee(
      @RequestBody CommitteeDTO committeeDTO) throws ApiRequestException {
    log.info("START : CommitteeController | deleteTempCommittee | request : {}", committeeDTO);
    StandardResponse<Boolean> response =
        this.committeeService.deleteCommitteeTemp(committeeDTO.getCommitteeId());
    log.info("END : CommitteeController | deleteTempCommittee | response : {}", response);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("getCommitteeLevelsByCommittee")
  public ResponseEntity<StandardResponse<List<CommitteeLevelDTO>>> getCommitteeLevelsByCommittee(
      @RequestBody CommitteeDTO committeeDTO) throws ApiRequestException {
    log.info(
        "START : CommitteeController | getCommitteeLevelsByCommittee | request : {}",
        committeeDTO.getCommitteeId());
    ResponseEntity<StandardResponse<List<CommitteeLevelDTO>>> response =
        this.committeeService.getCommitteeLevelsByCommittee(committeeDTO.getCommitteeId());
    log.info("END : CommitteeController | getCommitteeLevelsByCommittee | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }
}
