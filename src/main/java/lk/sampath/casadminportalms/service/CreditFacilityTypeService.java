package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacility.CreditFacilityTypeDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 *
 *
 * @author yomesh
 */

public interface CreditFacilityTypeService {

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO >> saveCreditFacilityTypeTemp(CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>>  findCreditFacilityTypeTempByID(Integer creditFacilityTypeID) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateCreditFacilityTempType(Integer creditFacilityTypeID, CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException;


    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> approveRejectCreditFacilityType(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> updateApprovedCreditFacilityType(Integer creditFacilityTypeID,CreditFacilityTypeDTO creditFacilityTypeDTO) throws ApiRequestException;

    default ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> searchCreditFacilityTypes() throws ApiRequestException {
        return searchCreditFacilityTypes(0, 10);
    }
    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> searchCreditFacilityTypes(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTypeDTO>> findCreditFacilityTypeByID(Integer creditFacilityTypeID) throws ApiRequestException;

    default ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> findAllCreditFacilityTypeTempList() throws ApiRequestException {
        return findAllCreditFacilityTypeTempList(0, 10);
    }
    ResponseEntity<StandardResponse<List<CreditFacilityTypeDTO>>> findAllCreditFacilityTypeTempList(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<Integer>> deleteCreditFacilityTypeTemp( CreditFacilityTypeDTO creditFacilityTypeTempDTO) throws ApiRequestException;

}
