package lk.sampath.casadminportalms.controller;


import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.CommitteeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Contains controllers to CommitteeType.
 *
 * @author SITS-Ushan
 */
@RestController
@RequestMapping("/committeeType")
public class CommitteeTypeController {

    private final CommitteeTypeService committeeTypeService;

    @Autowired
    public CommitteeTypeController(CommitteeTypeService committeeTypeService) {
        this.committeeTypeService = committeeTypeService;
    }

    /**
     * Returns all CommitteeTypes
     * @return ResponseEntity<StandardResponse<List<CommitteeType>>>
     */
    @GetMapping(value = "/getCommitteeType")
    public ResponseEntity<StandardResponse<List<CommitteeType>>> getCommitteeTypes(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        StandardResponse<List<CommitteeType>> response = committeeTypeService.getCommitteeTypes(pageable);
        return ResponseEntity.ok().body(response);
    }

    /**
     * save Committee Type
     * @return  AppResponse<CommitteeTypeCreateResponse>
     */
    @PostMapping(value = "/saveCommitteeType")
    public ResponseEntity<StandardResponse<CommitteeTypeDTO>> saveCommitteeType(@Validated @RequestBody CommitteeTypeDTO request)
            throws ApiRequestException {
        StandardResponse<CommitteeTypeDTO> response = committeeTypeService.saveCommitteeType(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update Committee Type
     * @param request
     * @return  AppResponse<CommitteeTypeCreateResponse>
     * @throws ApiRequestException
     */
    @PostMapping(value = "/updateCommitteeType/{committeeTypeId}")
    public ResponseEntity<StandardResponse<CommitteeTypeDTO>> updateCommitteeType(@PathVariable int committeeTypeId,
                                                                             @Validated @RequestBody CommitteeTypeDTO request)
            throws ApiRequestException {
        StandardResponse<CommitteeTypeDTO> response = committeeTypeService.updateCommitteeType(committeeTypeId,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
