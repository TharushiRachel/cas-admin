package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DaDesignationService {

    ResponseEntity<StandardResponse<List<DAHeaderResponse>>> getAllLimitHeadings(String tableType) throws ApiRequestException;
}
