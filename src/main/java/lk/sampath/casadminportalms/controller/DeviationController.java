package lk.sampath.casadminportalms.controller;

import java.util.List;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.deviation.DeviationDTO;
import lk.sampath.casadminportalms.dto.deviation.DeviationTypeDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.DeviationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deviation")
public class DeviationController {

  private final DeviationService deviationService;

  public DeviationController(DeviationService deviationService) {
    this.deviationService = deviationService;
  }

  @PostMapping("/saveDeviationType")
  ResponseEntity<StandardResponse<DeviationTypeDTO>> saveDeviationType(
      @Validated @RequestBody DeviationTypeDTO deviationTypeDTO) throws ApiRequestException {
    ResponseEntity<StandardResponse<DeviationTypeDTO>> response =
        deviationService.saveDeviationType(deviationTypeDTO);
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("/getAllDeviationTypes")
  ResponseEntity<StandardResponse<List<DeviationTypeDTO>>> getAllDeviationTypes()
      throws ApiRequestException {
    ResponseEntity<StandardResponse<List<DeviationTypeDTO>>> response =
        deviationService.getAllDeviationTypes();
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("/getDeviationTypeById/{deviationTypeId}")
  ResponseEntity<StandardResponse<DeviationTypeDTO>> getDeviationTypeById(
      @PathVariable Integer deviationTypeId) throws ApiRequestException {
    ResponseEntity<StandardResponse<DeviationTypeDTO>> response =
        deviationService.getDeviationTypeById(deviationTypeId);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("/saveDeviationTemp")
  ResponseEntity<StandardResponse<DeviationDTO>> saveDeviation(
      @Validated @RequestBody DeviationDTO deviationDTO) throws ApiRequestException {
    ResponseEntity<StandardResponse<DeviationDTO>> response =
        deviationService.saveOrUpdateDeviation(deviationDTO);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("/approveRejectDeviation")
  ResponseEntity<StandardResponse<DeviationDTO>> approveRejectDeviation(
      @Validated @RequestBody ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
    ResponseEntity<StandardResponse<DeviationDTO>> response =
        deviationService.approveOrRejectDeviation(approveRejectRQ);
    return ResponseEntity.ok().body(response.getBody());
  }

  @PostMapping("/updateApprovedDiversion")
  ResponseEntity<StandardResponse<DeviationDTO>> updateApprovedDiversion(
      @RequestBody DeviationDTO deviationDTO) throws ApiRequestException {
    ResponseEntity<StandardResponse<DeviationDTO>> response =
        deviationService.updateApprovedDiversion(deviationDTO);
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("/getAllDeviationTempList")
  ResponseEntity<StandardResponse<List<DeviationDTO>>> getAllDeviationTempList()
      throws ApiRequestException {
    ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
        deviationService.getAllDeviationTempList();
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("/getDeviationTempById/{deviationId}")
  ResponseEntity<StandardResponse<DeviationDTO>> getDeviationTempById(
      @PathVariable Integer deviationId) throws ApiRequestException {
    ResponseEntity<StandardResponse<DeviationDTO>> response =
        deviationService.getDeviationTempById(deviationId);
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("/getAllDeviationMasterList")
  ResponseEntity<StandardResponse<List<DeviationDTO>>> getAllDeviationMasterList()
      throws ApiRequestException {
    ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
        deviationService.getAllDeviationMasterList();
    return ResponseEntity.ok().body(response.getBody());
  }

  @GetMapping("/getDeviationMasterById/{deviationId}")
  ResponseEntity<StandardResponse<DeviationDTO>> getDeviationMasterById(
      @PathVariable Integer deviationId) throws ApiRequestException {
    ResponseEntity<StandardResponse<DeviationDTO>> response =
        deviationService.getDeviationMasterById(deviationId);
    return ResponseEntity.ok().body(response.getBody());
  }
}
