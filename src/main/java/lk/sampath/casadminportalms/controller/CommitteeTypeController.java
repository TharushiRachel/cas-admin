package lk.sampath.casadminportalms.controller;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CommitteeTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/committeeType")
public class CommitteeTypeController {

  private final CommitteeTypeService committeeTypeService;

  @Autowired
  public CommitteeTypeController(CommitteeTypeService committeeTypeService) {
    this.committeeTypeService = committeeTypeService;
  }

  @GetMapping(value = "/getCommitteeTypes")
  public ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> getCommitteeTypes()
      throws ApiRequestException {
    log.info("START | getCommitteeType - CommitteeTypeController");
    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeService.getCommitteeTypes();
    log.info("END | getCommitteeType - CommitteeTypeController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping(value = "/saveCommitteeType")
  public ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> saveCommitteeType(
      @Validated @RequestBody CommitteeTypeDTO request) throws ApiRequestException {
    log.info("START | saveCommitteeType - CommitteeTypeController | request : {}", request);
    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeService.saveCommitteeType(request);
    log.info("END | saveCommitteeType - CommitteeTypeController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping(value = "/updateCommitteeType")
  public ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> updateCommitteeType(
      @Validated @RequestBody CommitteeTypeDTO request) throws ApiRequestException {
    log.info("START | updateCommitteeType - CommitteeTypeController | request : {}", request);
    ResponseEntity<StandardResponse<List<CommitteeTypeDTO>>> response =
        committeeTypeService.updateCommitteeType(request);
    log.info("END | updateCommitteeType - CommitteeTypeController | response : {}", response);
    return ResponseEntity.ok().body(response.getBody());
  }
}
