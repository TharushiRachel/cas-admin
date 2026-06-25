package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upcsection.UpcSectionDTO;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UpcSectionService {
    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> findAllUpcSectionTempList() throws ApiRequestException;

    ResponseEntity<StandardResponse<UpcSectionDTO>> findUpcSectionTempByID(Integer upcSectionID) throws ApiRequestException;

    ResponseEntity<StandardResponse<List<UpcSectionDTO>>> findAllApprovedUpcSection() throws ApiRequestException;

    ResponseEntity<StandardResponse<UpcSectionDTO>> findApprovedUpcSectionByID(Integer upcSectionID) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpcSectionDTO>> saveUpcSectionTemp(UpcSectionDTO upcSectionDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpcSectionDTO>> approveRejectUpcSection(ApproveRejectRQ approveRejectRQ) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpcSectionDTO>> updateUpcSectionTemp(Integer upcSectionID, UpcSectionDTO upcSectionDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<UpcSectionDTO>> updateApprovedUpcSection(Integer upcSectionID, UpcSectionDTO upcSectionDTO) throws ApiRequestException;

    ResponseEntity<StandardResponse<Void>> deleteUpcSectionFormTemp(Integer upcSectionID) throws ApiRequestException;
}