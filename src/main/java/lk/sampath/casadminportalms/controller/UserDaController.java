package lk.sampath.casadminportalms.controller;

import lk.sampath.casadminportalms.controller.basecontroller.PaginationUtil;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.userda.UserDaDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.service.UserDaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userDa")
public class UserDaController {

    @Autowired
    private UserDaService userDaService;


    @GetMapping("/userDaTemp")
    public ResponseEntity<StandardResponse<List<UserDaDTO>>> viewAllUserDaTemp(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws  ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<UserDaDTO>>> userDaTempList = userDaService.findAllUserDaTempList(pageable);
        return ResponseEntity.ok().body(userDaTempList.getBody());
    }


    @GetMapping("/userDaTemp/{userDaID}")
    public ResponseEntity<StandardResponse<UserDaDTO>> viewUserDaTempById(@PathVariable Integer userDaID) throws ApiRequestException{
        ResponseEntity<StandardResponse<UserDaDTO>> userDaTempDTO = userDaService.findUserDaTempByID(userDaID);
        return ResponseEntity.ok().body(userDaTempDTO.getBody());
    }

    @PostMapping("/userDasList")
    public ResponseEntity<StandardResponse<List<UserDaDTO>>> getPagedUserDaData(
            @RequestHeader(name = "page", required = false) Integer headerPage,
            @RequestHeader(name = "size", required = false) Integer headerSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws ApiRequestException {
        Pageable pageable = PaginationUtil.createPageable(headerPage, headerSize, page, size);
        ResponseEntity<StandardResponse<List<UserDaDTO>>> userDaList = userDaService.findAllApprovedUserDa(pageable);
        return ResponseEntity.ok().body(userDaList.getBody());
    }

    @GetMapping("/{userDaID}")
    public ResponseEntity<StandardResponse<UserDaDTO>> viewUserDaById(@PathVariable Integer userDaID) throws ApiRequestException{
        ResponseEntity<StandardResponse<UserDaDTO>> userDa = userDaService.findApprovedUserDaById(userDaID);
        return ResponseEntity.ok().body(userDa.getBody());
    }

    @PostMapping
    public ResponseEntity<StandardResponse<UserDaDTO>> saveUserDa(@Validated @RequestBody UserDaDTO request) throws  ApiRequestException{
        ResponseEntity<StandardResponse<UserDaDTO>> userDaTemp = userDaService.saveUserDaTemp(request);
        return ResponseEntity.ok().body(userDaTemp.getBody());
    }

    @PostMapping("/approveRejectUserDa")
    public ResponseEntity<StandardResponse<UserDaDTO>> approveRejectUserDa(@Validated @RequestBody ApproveRejectRQ request) throws  ApiRequestException {
        ResponseEntity<StandardResponse<UserDaDTO>> userDa = userDaService.approveRejectUserDa(request);
        return ResponseEntity.ok().body(userDa.getBody());
    }

    @PostMapping("/updateUserDaTemp/{userDaID}")
    public ResponseEntity<StandardResponse<UserDaDTO>> updateUserDaTemp(@PathVariable Integer userDaID, @Validated @RequestBody UserDaDTO request) throws  ApiRequestException{
        ResponseEntity<StandardResponse<UserDaDTO>> userDa = userDaService.updateUserDaTemp(userDaID, request);
        return ResponseEntity.ok().body(userDa.getBody());
    }

    @PostMapping("/userDa/{userDaID}")
    public ResponseEntity<StandardResponse<UserDaDTO>> updateApprovedUserDa(@PathVariable Integer userDaID, @Validated @RequestBody UserDaDTO userDaDTO) throws ApiRequestException {
        ResponseEntity<StandardResponse<UserDaDTO>> userDa = userDaService.updateApprovedUserDa(userDaID, userDaDTO);
        return ResponseEntity.ok().body(userDa.getBody());
    }

    @PostMapping("/userDaTemp/deleteUserDaTemp")
    public ResponseEntity<StandardResponse<Void>> deleteUserDaTemp(@Validated @RequestBody UserDaDTO request) throws ApiRequestException{
        ResponseEntity<StandardResponse<Void>> userDa = userDaService.deleteUserDaFromTemp(request.getUserDaID());
        return ResponseEntity.ok().body(userDa.getBody());
    }


}
