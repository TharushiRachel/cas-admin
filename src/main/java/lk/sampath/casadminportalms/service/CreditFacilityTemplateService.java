package lk.sampath.casadminportalms.service;


import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.CreditFacilityTemplateDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CreditFacilityTemplateService {

    default ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplatesTemp() throws ApiRequestException {
        return getAllCreditFacilityTemplatesTemp(0, 10);
    }
    ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplatesTemp(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateTempByID(Integer creditFacilityTemplateID) throws ApiRequestException;

    default ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplates() throws ApiRequestException {
        return getAllCreditFacilityTemplates(0, 10);
    }
    ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> getAllCreditFacilityTemplates(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> getCreditFacilityTemplateByID(Integer creditFacilityTemplateID) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> saveCreditFacilityTemplateTemp(CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException;

    public ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplateTemp(Integer creditFacilityTemplateID, CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> authorizeCreditFacilityTemplate(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> updateCreditFacilityTemplate(Integer creditFacilityTemplateID, CreditFacilityTemplateDTO creditFacilityTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteCreditFacilityTemplateTemp(Integer creditFacilityTemplateID);
}
