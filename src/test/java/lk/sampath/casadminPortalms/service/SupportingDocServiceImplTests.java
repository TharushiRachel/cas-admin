package lk.sampath.casadminPortalms.service;


import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.supportingdoc.SupportingDocDTO;
import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDoc;
import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDocAud;
import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDocTemp;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocRepository;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocTempAudRepository;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocTempRepository;
import lk.sampath.casadminportalms.service.impl.SupportingDocServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class SupportingDocServiceImplTests {

    @Mock
    private SupportingDocTempRepository supportingDocTempRepository;

    @Mock
    private SupportingDocRepository supportingDocRepository;

    @Mock
    private SupportingDocTempAudRepository supportingDocTempAudRepository;

    @InjectMocks
    private SupportingDocServiceImpl supportingDocService;

    private SupportingDocTemp supportingDocTemp;

    private SupportingDoc supportingDoc;

    private SupportingDocDTO supportingDocDTO;


    private ApproveRejectRQ approveRejectRQ;

    @BeforeEach()
    void setup() {
        MockitoAnnotations.openMocks(this);

        supportingDocTemp = new SupportingDocTemp();
        supportingDocTemp.setSupportingDocID(1);
        supportingDocTemp.setDocumentName("Unit Testing");
        supportingDocTemp.setDescription("Unit Testing Description");
        supportingDocTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
        supportingDocTemp.setModifiedBy("User Test1");

        supportingDoc = new SupportingDoc();
        supportingDoc.setSupportingDocID(1);
        supportingDoc.setDocumentName("Unit Testing");
        supportingDoc.setDescription("Unit Testing Description");
        supportingDoc.setApproveStatus(MasterDataApproveStatus.APPROVED);

        supportingDocDTO = new SupportingDocDTO();
        supportingDocDTO.setSupportingDocID(1);
        supportingDocDTO.setDocumentName("Old Unit Testing");
        supportingDocDTO.setDescription("Old Unit Testing Description");
        supportingDocDTO.setStatus(Status.ACT);

        approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(1);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    }

    /** findAllSupportingDocTempList **/
    @Test
    void testFindSupportingDocTempList_Success() throws Exception{
        List<SupportingDocTemp> supportingDocTempList = Arrays.asList(supportingDocTemp);
        when(supportingDocTempRepository.findAll()).thenReturn(supportingDocTempList);

        ResponseEntity<StandardResponse<List<SupportingDocDTO>>> response = supportingDocService.findAllSupportingDocTempList();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(supportingDocTempRepository, times(1)).findAll();
    }

    @Test
    void testFindSupportingDocTempList_EmptyList() throws Exception{
        List<SupportingDocTemp> supportingDocTempList = Collections.emptyList();
        when(supportingDocTempRepository.findAll()).thenReturn(supportingDocTempList);

        ResponseEntity<StandardResponse<List<SupportingDocDTO>>> response = supportingDocService.findAllSupportingDocTempList();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(supportingDocTempRepository, times(1)).findAll();
    }

    /** findSupportingDocTempById **/

    @Test
    void testFindSupportingDocTempById_Success() throws ApiRequestException{
        when(supportingDocTempRepository.findById(supportingDocTemp.getSupportingDocID())).thenReturn(Optional.of(supportingDocTemp));

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.findSupportingDocTempById(supportingDocTemp.getSupportingDocID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(supportingDocTempRepository,times(1)).findById(supportingDocTemp.getSupportingDocID());
    }

    @Test
    void testFindSupportingDocTempById_NotFound() {
        when(supportingDocTempRepository.findById(2)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.findSupportingDocTempById(2);
        });

        assertEquals("Supporting Doc with 2does not exists", exception.getMessage());

        verify(supportingDocTempRepository, times(1)).findById(2);
    }

    /** searchSupportingDocGroups **/

    @Test
    void testFindAllApprovedSupportingDoc_Success() {
        List<SupportingDoc> supportingDocs = Arrays.asList(supportingDoc);
        when(supportingDocRepository.findAll()).thenReturn(supportingDocs);

        ResponseEntity<StandardResponse<List<SupportingDocDTO>>> response = supportingDocService.searchSupportingDocGroups();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(supportingDocRepository, times(1)).findAll();
    }

    @Test
    void testFindAllApprovedSupportingDoc_EmptyList() throws ApiRequestException{
        List<SupportingDoc> supportingDocs = Collections.emptyList();
        when(supportingDocRepository.findAll()).thenReturn(supportingDocs);

        ResponseEntity<StandardResponse<List<SupportingDocDTO>>> response = supportingDocService.searchSupportingDocGroups();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(supportingDocRepository, times(1)).findAll();
    }

    /** findSupportingDocById **/
    @Test
    void testFindApprovedSupportingDocById_Success() throws ApiRequestException{
        when(supportingDocRepository.findById(supportingDoc.getSupportingDocID())).thenReturn(Optional.of(supportingDoc));

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.findSupportingDocById(supportingDoc.getSupportingDocID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(supportingDocRepository, times(1)).findById(supportingDoc.getSupportingDocID());
    }

    @Test
    void testFindApprovedSupportingDocById_NotFound(){
        when(supportingDocRepository.findById(2)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.findSupportingDocById(2);
        });

        assertEquals("Supporting Doc with 2does not exists", exception.getMessage());

        verify(supportingDocRepository, times(1)).findById(2);
    }

    /** saveSupportingDocTemp **/
    @Test
    void testSaveSupportingDocTemp_Success() throws ApiRequestException{
        when(supportingDocTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(supportingDocTempRepository.getCurrentSequenceValue()).thenReturn(1);
        when(supportingDocTempRepository.saveAndFlush(any(SupportingDocTemp.class))).thenReturn(supportingDocTemp);

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.saveSupportingDocTemp(supportingDocDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        SupportingDocTemp savedUser = (SupportingDocTemp) response.getBody().getResponse();
        assertEquals(supportingDocTemp.getDocumentName(), savedUser.getDocumentName());

        verify(supportingDocTempRepository, times(1)).findAll(any(BooleanBuilder.class));
        verify(supportingDocTempRepository, times(1)).saveAndFlush(any(SupportingDocTemp.class));
    }

    @Test
    void testSaveSupportingDocTemp_UserAlreadyExists() throws ApiRequestException{
        when(supportingDocTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(List.of(supportingDocTemp));

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.saveSupportingDocTemp(supportingDocDTO);
        });

        assertEquals("Supporting Document Already Exists", exception.getMessage());

        verify(supportingDocTempRepository, times(1)).findAll(any(BooleanBuilder.class));
        verify(supportingDocTempRepository, never()).saveAndFlush(any(SupportingDocTemp.class));
    }

    /** approveRejectSupportingDoc **/

    @Test
    void testApproveRejectSupportingDoc_Success() throws ApiRequestException{
        when(supportingDocTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(supportingDocTemp));
        when(supportingDocRepository.findById(supportingDocTemp.getSupportingDocID())).thenReturn(Optional.of(supportingDoc));

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.approveRejectSupportingDoc(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(supportingDocTempRepository, times(1)).findById(approveRejectRQ.getApproveRejectDataID());
        verify(supportingDocTempRepository, times(1)).saveAndFlush(supportingDocTemp);

    }

    /** approveRejectSupportingDoc - Reject Path **/

    @Test
    void testApproveRejectSupportingDoc_RejectSuccess() throws ApiRequestException{
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);
        when(supportingDocTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(supportingDocTemp));

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.approveRejectSupportingDoc(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(supportingDocTempRepository, times(1)).findById(approveRejectRQ.getApproveRejectDataID());
        verify(supportingDocTempRepository, times(1)).saveAndFlush(supportingDocTemp);
    }

    /** approveRejectSupportingDoc - Invalid Request **/

    @Test
    void testApproveRejectSupportingDoc_InvalidRequest() {
        ApproveRejectRQ invalidRequest = new ApproveRejectRQ();
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.approveRejectSupportingDoc(invalidRequest);
        });

        assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
    }

    /** approveRejectSupportingDoc - SupportingDoc Not Found **/

    @Test
    void testApproveRejectSupportingDoc_NotFound() {
        when(supportingDocTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.approveRejectSupportingDoc(approveRejectRQ);
        });
        assertEquals("Supporting Doc with 1does not exists", exception.getMessage());

    }

    /** approveRejectSupportingDoc - Unknown Status **/

    @Test
    void testApproveRejectSupportingDoc_UnknownStatus() {
        approveRejectRQ.setApproveStatus(null);
        when(supportingDocTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(supportingDocTemp));

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.approveRejectSupportingDoc(approveRejectRQ);
        });

        assertTrue(exception.getMessage().contains("Unknown approval status"));
    }

    @Test
    void testHandleApproval_CallsMapSupportingDoc_ForNewUser() throws ApiRequestException{
        when(supportingDocTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(supportingDocTemp));
        when(supportingDocRepository.findById(supportingDocTemp.getSupportingDocID())).thenReturn(Optional.empty());
        when(supportingDocRepository.saveAndFlush(any(SupportingDoc.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.approveRejectSupportingDoc(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(supportingDocTempRepository, times(1)).delete(supportingDocTemp);
        verify(supportingDocRepository, times(1)).saveAndFlush(any(SupportingDoc.class));
    }

    @Test
    void testHandleApproval_CallsUpdateSupportingDocToMaster_ForExistingUser() throws ApiRequestException{
        when(supportingDocTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(supportingDocTemp));
        when(supportingDocRepository.findById(supportingDocTemp.getSupportingDocID())).thenReturn(Optional.of(supportingDoc));
        when(supportingDocRepository.saveAndFlush(any(SupportingDoc.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.approveRejectSupportingDoc(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(supportingDocTempRepository, times(1)).delete(supportingDocTemp);
        verify(supportingDocRepository, times(1)).saveAndFlush(supportingDoc);
    }

    /** updateSupportingDocTemp **/
    @Test
    void testUpdateSupportingDocTemp_Success() throws ApiRequestException{
        when(supportingDocTempRepository.findById(1)).thenReturn(Optional.of(supportingDocTemp));
        when(supportingDocTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.updateSupportingDocTemp(1, supportingDocDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(supportingDocTempRepository, times(1)).save(supportingDocTemp);
    }

    /** supportingDocTemp - SupportingDoc Not Found **/

    @Test
    void testUpdateSupportingDocTemp_SupportingDocTempNotFound() {
        when(supportingDocTempRepository.findById(1)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.updateSupportingDocTemp(1, supportingDocDTO);
        });

        assertEquals("Supporting Doc with 1does not exists", exception.getMessage());
    }

    @Test
    void testUpdateSupportingDocTemp_DocumentNameConflict(){
        SupportingDocTemp conflictingUser = new SupportingDocTemp();
        conflictingUser.setDocumentName(supportingDocDTO.getDocumentName());
        conflictingUser.setSupportingDocID(2);
        when(supportingDocTempRepository.findById(1)).thenReturn(Optional.of(supportingDocTemp));
        when(supportingDocTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(List.of(conflictingUser));

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.updateSupportingDocTemp(1, supportingDocDTO);
        });

        assertEquals("Supporting Doc Temp with Old Unit Testingalready exists", exception.getMessage());
    }

    /** Update Approved Supporting Doc**/

    @Test
    void testUpdateApprovedSupportingDoc_Success() throws ApiRequestException{
        when(supportingDocRepository.findById(supportingDocDTO.getSupportingDocID())).thenReturn(Optional.of(supportingDoc));
        when(supportingDocTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
        when(supportingDocRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
        when(supportingDocTempRepository.saveAndFlush(any(SupportingDocTemp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.updateApprovedSupportingDoc(1, supportingDocDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(supportingDocTempRepository, times(1)).saveAndFlush(any(SupportingDocTemp.class));
    }

    /** updateApprovedSupportingDoc - supporting doc Not Found **/

    @Test
    void testUpdateApprovedSupportingDoc_NotFound(){
        when(supportingDocRepository.findById(supportingDocDTO.getSupportingDocID())).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.updateApprovedSupportingDoc(1, supportingDocDTO);
        });

        assertEquals("Supporting Doc with 1does not exists", exception.getMessage());
    }


    /** updateApprovedSupportingDoc - DocumentName Conflict in Temporary Records **/

    @Test
    void testUpdateApprovedSupportingDoc_DocumentNameConflictInTemp() {
        when(supportingDocRepository.findById(supportingDocDTO.getSupportingDocID())).thenReturn(Optional.of(supportingDoc));
        when(supportingDocTempRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.updateApprovedSupportingDoc(1, supportingDocDTO);
        });

        assertEquals("Document name 'Old Unit Testing' already exists in the system.", exception.getMessage());
    }

    /** updateApprovedSupportingDoc - Document Name Conflict in Master Records **/

    @Test
    void testUpdateApprovedSupportingDoc_DocumentNameConflictsInMaster () {
        when(supportingDocRepository.findById(supportingDocDTO.getSupportingDocID())).thenReturn(Optional.of(supportingDoc));
        when(supportingDocTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
        when(supportingDocRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            supportingDocService.updateApprovedSupportingDoc(1, supportingDocDTO);
        });

        assertEquals("Document name 'Old Unit Testing' already exists in the system.", exception.getMessage());
    }

    /** delete SupportingDocFromTemp **/

    @Test
    void testDeleteSupportingDocFromTemp_Success() throws ApiRequestException {
        doNothing().when(supportingDocTempRepository).deleteById(supportingDocDTO.getSupportingDocID());

        ResponseEntity<StandardResponse<Void>> response = supportingDocService.deleteSupportingDocTemp(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getResponse());

        verify(supportingDocTempRepository, times(1)).deleteById(1);
    }

    @Test
    void testApproveRejectSupportingDoc_RejectionSuccess_WithAudit() throws ApiRequestException {
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

        when(supportingDocTempRepository.findById(approveRejectRQ.getApproveRejectDataID()))
                .thenReturn(Optional.of(supportingDocTemp));

        when(supportingDocTempAudRepository.save(any(SupportingDocAud.class)))
                .thenAnswer(invocation ->{
                    SupportingDocAud audit = invocation.getArgument(0);
                    audit.setId(1);
                    return audit;
                });

        ResponseEntity<StandardResponse<SupportingDocDTO>> response = supportingDocService.approveRejectSupportingDoc(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(supportingDocTempRepository, times(1)).findById(approveRejectRQ.getApproveRejectDataID());
        verify(supportingDocTempAudRepository, times(1)).save(any(SupportingDocAud.class));

        ArgumentCaptor<SupportingDocAud> auditCaptor = ArgumentCaptor.forClass(SupportingDocAud.class);
        verify(supportingDocTempAudRepository, times(1)).save(auditCaptor.capture());

        SupportingDocAud capturedAudit = auditCaptor.getValue();

        assertNotNull(capturedAudit);
        assertEquals(1, capturedAudit.getId());
        assertEquals(supportingDocTemp.getSupportingDocID(), capturedAudit.getSupportingDocID());
        assertEquals(supportingDocTemp.getStatus(), capturedAudit.getStatus());
        assertEquals(supportingDocTemp.getApproveStatus(), capturedAudit.getApproveStatus());
        assertEquals(supportingDocTemp.getDocumentName(), capturedAudit.getDocumentName());
        assertEquals(supportingDocTemp.getDescription(), capturedAudit.getDescription());
        assertEquals(supportingDocTemp.getSupportDocumentType(), capturedAudit.getSupportDocumentType());

    }

}
