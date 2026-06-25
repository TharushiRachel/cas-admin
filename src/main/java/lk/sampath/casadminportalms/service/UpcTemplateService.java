package lk.sampath.casadminportalms.service;


import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UpcTemplateService {

    ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllUpcTemplateTempList(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> findUpcTemplateTempById(Integer upcTemplateID) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> findAllApprovedUpcTemplates(Pageable pageable) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> saveUpcTemplate(UpcTemplateDTO upcTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> updateUpcTemplateTemp(Integer upcTemplateID, UpcTemplateDTO upcTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> approveRejectUpcTemplate(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> updateApprovedUpcTemplate(Integer upcTemplateID, UpcTemplateDTO upcTemplateDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Object>> findApprovedUpcTemplateById(Integer upcTemplateID) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteUpcTemplateFromTemp(Integer upcTemplateID) throws ApiRequestException;
}
