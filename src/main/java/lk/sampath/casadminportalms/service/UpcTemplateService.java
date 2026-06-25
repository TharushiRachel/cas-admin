package lk.sampath.casadminportalms.service;


import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UpcTemplateService {

    default ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllUpcTemplateTempList() throws ApiRequestException {
        return findAllUpcTemplateTempList(0, 10);
    }
    ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllUpcTemplateTempList(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> findUpcTemplateTempById(Integer upcTemplateID) throws ApiRequestException;

    default ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllApprovedUpcTemplates() throws ApiRequestException {
        return findAllApprovedUpcTemplates(0, 10);
    }
    ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllApprovedUpcTemplates(int page, int size) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> saveUpcTemplate(UpcTemplateDTO upcTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> updateUpcTemplateTemp(Integer upcTemplateID, UpcTemplateDTO upcTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> approveRejectUpcTemplate(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> updateApprovedUpcTemplate(Integer upcTemplateID, UpcTemplateDTO upcTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> findApprovedUpcTemplateById(Integer upcTemplateID) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteUpcTemplateFromTemp(Integer upcTemplateID) throws ApiRequestException;
}
