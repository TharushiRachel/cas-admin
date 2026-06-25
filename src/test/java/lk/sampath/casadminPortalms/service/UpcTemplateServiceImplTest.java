package lk.sampath.casadminPortalms.service;

import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDTO;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDataDTO;
import lk.sampath.casadminportalms.entity.upcsection.UpcSection;
import lk.sampath.casadminportalms.entity.upctemplate.*;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.upcsection.UpcSectionRepository;
import lk.sampath.casadminportalms.repository.upctemplate.*;
import lk.sampath.casadminportalms.service.impl.UpcTemplateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpcTemplateServiceImplTest {

    @Mock
    private UpcTemplateTempRepository upcTemplateTempRepository;

    @Mock
    private UpcTemplateRepository upcTemplateRepository;

    @InjectMocks
    private UpcTemplateServiceImpl upcTemplateService;

    @Mock
    private UpcTemplateDataTempRepository upcTemplateDataTempRepository;

    @Mock
    private UpcSectionRepository upcSectionRepository;

    @Mock
    private UpcTemplateDataRepository upcTemplateDataRepository;

    @Mock
    private UpcTemplateAudRepository upcTemplateAudRepository;

    @Mock
    private UpcTemplateDataAudRepository upcTemplateDataAudRepository;

    private static final String UPC_TEMPLATE_WITH = "UPC Template with";
    private static final String DOES_NOT_EXIST = "does not exist";

    /**
     * Test when the findAllUpcTemplateTempList()
     */
    @Test
    void testFindAllUpcTemplateTempList_Success() throws ApiRequestException {

        List<UpcTemplateTemp> upcTemplateTempList = new ArrayList<>();
        UpcTemplateTemp template1 = new UpcTemplateTemp();
        template1.setUpcTemplateID(1);
        template1.setTemplateName("Template 1");

        UpcTemplateTemp template2 = new UpcTemplateTemp();
        template2.setUpcTemplateID(2);
        template2.setTemplateName("Template 2");

        upcTemplateTempList.add(template1);
        upcTemplateTempList.add(template2);

        when(upcTemplateTempRepository.findAll()).thenReturn(upcTemplateTempList);

        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> response = upcTemplateService.findAllUpcTemplateTempList();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
    }

    @Test
    void testFindAllUpcTemplateTempList_EmptyList() throws ApiRequestException {
        List<UpcTemplateTemp> upcTemplateTempList = new ArrayList<>();
        when(upcTemplateTempRepository.findAll()).thenReturn(upcTemplateTempList);

        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> response = upcTemplateService.findAllUpcTemplateTempList();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

        @SuppressWarnings("unchecked")
        List<UpcTemplateDTO> responseList = (List<UpcTemplateDTO>) response.getBody().getResponse();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    void testFindAllUpcTemplateTempList_MixedData() throws ApiRequestException {
        // Arrange
        List<UpcTemplateTemp> upcTemplateTempList = new ArrayList<>();
        UpcTemplateTemp validTemplate = new UpcTemplateTemp();
        validTemplate.setUpcTemplateID(1);
        validTemplate.setTemplateName("Valid Template");

        UpcTemplateTemp invalidTemplate = new UpcTemplateTemp();
        invalidTemplate.setUpcTemplateID(2); // No template name provided

        upcTemplateTempList.add(validTemplate);
        upcTemplateTempList.add(invalidTemplate);

        when(upcTemplateTempRepository.findAll()).thenReturn(upcTemplateTempList);

        // Act
        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> response = upcTemplateService.findAllUpcTemplateTempList();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

        @SuppressWarnings("unchecked")
        List<UpcTemplateDTO> responseList = (List<UpcTemplateDTO>) response.getBody().getResponse();

        assertNotNull(responseList);
        assertEquals(2, responseList.size());
    }


    @Test
    void testFindAllUpcTemplateTempList_LargeDataset() throws ApiRequestException {
        // Arrange
        List<UpcTemplateTemp> upcTemplateTempList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            UpcTemplateTemp template = new UpcTemplateTemp();
            template.setUpcTemplateID(i);
            template.setTemplateName("Template " + i);
            upcTemplateTempList.add(template);
        }

        when(upcTemplateTempRepository.findAll()).thenReturn(upcTemplateTempList);

        // Act
        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> response = upcTemplateService.findAllUpcTemplateTempList();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

        @SuppressWarnings("unchecked")
        List<UpcTemplateDTO> responseList = (List<UpcTemplateDTO>) response.getBody().getResponse();

        assertNotNull(responseList);
        assertEquals(1000, responseList.size());
    }


    /**
     * Test when findUpcTemplateTempById()
     */
    @Test
    void testFindUpcTemplateTempById_Success() throws ApiRequestException {
        // Arrange
        Integer upcTemplateID = 1;
        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(upcTemplateID);
        upcTemplateTemp.setTemplateName("Test Template");

        when(upcTemplateTempRepository.findById(upcTemplateID)).thenReturn(Optional.of(upcTemplateTemp));

        // Act
        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.findUpcTemplateTempById(upcTemplateID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

        // Cast the response to UpcTemplateDTO
        UpcTemplateDTO upcTemplateDTO = (UpcTemplateDTO) response.getBody().getResponse();
        assertNotNull(upcTemplateDTO);
        assertEquals(upcTemplateID, upcTemplateDTO.getUpcTemplateID());
        assertEquals("Test Template", upcTemplateDTO.getTemplateName());
    }

    @Test
    void testFindUpcTemplateTempById_NotFound() {
        // Arrange
        Integer upcTemplateID = 1;
        when(upcTemplateTempRepository.findById(upcTemplateID)).thenReturn(Optional.empty());

        // Act & Assert
        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.findUpcTemplateTempById(upcTemplateID)
        );

        assertEquals("UPC Template with" + upcTemplateID + "does not exist", exception.getMessage());
    }

    @Test
    void testFindUpcTemplateTempById_RuntimeException() {

        Integer templateID = 2;

        when(upcTemplateTempRepository.findById(templateID))
                .thenThrow(new RuntimeException("Database connection error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            upcTemplateService.findUpcTemplateTempById(templateID);
        });

        assertEquals("Database connection error", exception.getMessage(),
                "Exception message should match the thrown RuntimeException.");

        verify(upcTemplateTempRepository, times(1)).findById(templateID);
    }

    /**
     * Test when findAllApprovedUpcTemplates()
     */

    @Test
    void testFindAllApprovedUpcTemplates_Success() throws ApiRequestException {
        UpcTemplate upcTemplate = new UpcTemplate();
        upcTemplate.setUpcTemplateID(1);
        upcTemplate.setTemplateName("Template 1");
        upcTemplate.setDescription("Description 1");

        List<UpcTemplate> upcTemplateList = Arrays.asList(upcTemplate);

        when(upcTemplateRepository.findAll()).thenReturn(upcTemplateList);

        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> response = upcTemplateService.findAllApprovedUpcTemplates();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

        List<UpcTemplateDTO> responseDTOList = (List<UpcTemplateDTO>) response.getBody().getResponse();
        assertNotNull(responseDTOList);
        assertEquals(1, responseDTOList.size());
        assertEquals("Template 1", responseDTOList.get(0).getTemplateName());
    }

    @Test
    void testFindAllApprovedUpcTemplates_EmptyList() throws ApiRequestException {

        when(upcTemplateRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<UpcTemplateDTO>>> response = upcTemplateService.findAllApprovedUpcTemplates();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

        List<UpcTemplateDTO> responseDTOList = (List<UpcTemplateDTO>) response.getBody().getResponse();
        assertNotNull(responseDTOList);
        assertTrue(responseDTOList.isEmpty());
    }


    /**
     * Test when findApprovedUpcTemplateById()
     */
    @Test
    void testFindUpcTemplateById_ThrowsApiRequestException() {

        Integer templateID = 999;

        when(upcTemplateRepository.findById(templateID))
                .thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            upcTemplateService.findApprovedUpcTemplateById(templateID);
        });

        assertEquals(UPC_TEMPLATE_WITH +templateID+ DOES_NOT_EXIST, exception.getMessage(),
                "Exception message should match when template ID does not exist.");

        verify(upcTemplateRepository, times(1)).findById(templateID);
    }

    @Test
    void testFindUpcTemplateById_RuntimeException() {

        Integer templateID = 2;

        when(upcTemplateRepository.findById(templateID))
                .thenThrow(new RuntimeException("Database connection error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            upcTemplateService.findApprovedUpcTemplateById(templateID);
        });

        assertEquals("Database connection error", exception.getMessage(),
                "Exception message should match the thrown RuntimeException.");

        verify(upcTemplateRepository, times(1)).findById(templateID);
    }

    /**
     * Test when saveUpcTemplate()
     */

    @Test
     void testSaveUpcTemplate_Success() throws ApiRequestException {
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName("Test Template");
        upcTemplateDTO.setUpcLabel("Label");
        upcTemplateDTO.setDescription("Test Description");
        upcTemplateDTO.setCreatedBy("User1");
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);

        when(upcTemplateTempRepository.getCurrentSequenceValue()).thenReturn(1);
        when(upcTemplateTempRepository.saveAndFlush(any(UpcTemplateTemp.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.saveUpcTemplate(upcTemplateDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());

        UpcTemplateDTO savedTemplate = (UpcTemplateDTO) response.getBody().getResponse();
        assertEquals("Test Template", savedTemplate.getTemplateName());
        assertEquals("Label", savedTemplate.getUpcLabel());
        assertEquals("Test Description", savedTemplate.getDescription());
        assertEquals("User1", savedTemplate.getCreatedBy());
        assertEquals(MasterDataApproveStatus.PENDING, savedTemplate.getApproveStatus());
    }

    @Test
     void testSaveUpcTemplate_DuplicateInTempRepository() {
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName("Duplicate Template");

        UpcTemplateTemp existingTemplate = new UpcTemplateTemp();
        existingTemplate.setTemplateName("Duplicate Template");

        when(upcTemplateTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(existingTemplate));

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.saveUpcTemplate(upcTemplateDTO)
        );
        assertEquals("Upc Section Template Already Exists", exception.getMessage());
    }

    @Test
     void testSaveUpcTemplate_WithModifiedData() throws ApiRequestException {

        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName("Modified Template");
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.Y);

        UpcTemplateDataDTO dataDTO = new UpcTemplateDataDTO();
        dataDTO.setUpcTemplateDataID(2);
        dataDTO.setUpcSectionID(1);
        upcTemplateDTO.setUpcTemplateDataDTOList(Collections.singletonList(dataDTO));

        UpcTemplateDataTemp existingData = new UpcTemplateDataTemp();
        existingData.setUpcTemplateDataID(1);

        UpcTemplateTemp mockTemplateTemp = new UpcTemplateTemp();
        mockTemplateTemp.setUpcTemplateID(1);

        UpcSection mockSection = new UpcSection();
        mockSection.setUpcSectionID(1);

        when(upcTemplateTempRepository.getCurrentSequenceValue()).thenReturn(1);
        when(upcTemplateDataTempRepository.findByUpcTemplateTempUpcTemplateID(anyInt())).thenReturn(Collections.singletonList(existingData));
        when(upcTemplateTempRepository.saveAndFlush(any(UpcTemplateTemp.class))).thenReturn(mockTemplateTemp);
        when(upcSectionRepository.findById(1)).thenReturn(Optional.of(mockSection));

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.saveUpcTemplate(upcTemplateDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
    }

    /**
     * Test when updateUpcTemplateTemp()
     */

    @Test
     void testUpdateUpcTemplateTemp_Success() {
        Integer upcTemplateID = 1;
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName("Updated Template");
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.Y);

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(upcTemplateID);
        upcTemplateTemp.setTemplateName("Existing Template");

        when(upcTemplateTempRepository.findById(upcTemplateID)).thenReturn(Optional.of(upcTemplateTemp));
        when(upcTemplateTempRepository.saveAndFlush(any(UpcTemplateTemp.class))).thenReturn(upcTemplateTemp);

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.updateUpcTemplateTemp(upcTemplateID, upcTemplateDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
    }

    @Test
     void testUpdateUpcTemplateTemp_TemplateIDNotFound() {
        Integer upcTemplateID = 1;
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();

        when(upcTemplateTempRepository.findById(upcTemplateID)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.updateUpcTemplateTemp(upcTemplateID, upcTemplateDTO)
        );
        assertEquals("UPC Template with1does not exist", exception.getMessage());
    }

    @Test
     void testSaveOrUpdateUpcTemplateData_SectionNotFound() {
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.Y);

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(1);

        UpcTemplateDataDTO dataDTO = new UpcTemplateDataDTO();
        dataDTO.setUpcSectionID(99);
        upcTemplateDTO.setUpcTemplateDataDTOList(Collections.singletonList(dataDTO));

        when(upcSectionRepository.findById(99)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.saveOrUpdateUpcTemplateData(upcTemplateDTO, upcTemplateTemp)
        );
        assertEquals("Upc section with ID 99 does not exist.", exception.getMessage());
    }

    @Test
     void testSaveOrUpdateUpcTemplateData_Success() {
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.Y);

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(1);

        UpcTemplateDataDTO dataDTO = new UpcTemplateDataDTO();
        dataDTO.setUpcSectionID(1);
        upcTemplateDTO.setUpcTemplateDataDTOList(Collections.singletonList(dataDTO));

        UpcSection upcSection = new UpcSection();
        upcSection.setUpcSectionID(1);

        when(upcSectionRepository.findById(1)).thenReturn(Optional.of(upcSection));
        when(upcTemplateDataTempRepository.getCurrentSequenceValue()).thenReturn(100);

        List<UpcTemplateDataTemp> result = upcTemplateService.saveOrUpdateUpcTemplateData(upcTemplateDTO, upcTemplateTemp);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getUpcTemplateDataID());
        assertEquals(upcTemplateTemp, result.get(0).getUpcTemplateTemp());
        assertEquals(upcSection, result.get(0).getUpcSection());
    }

    /**
     * Test when approveRejectUpcTemplate()
     */

    @Test
     void testApproveRejectUpcTemplate_Approve_Success() {
        ApproveRejectRQ request = new ApproveRejectRQ();
        request.setApproveRejectDataID(1);
        request.setApproveStatus(MasterDataApproveStatus.APPROVED);

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(1);
        upcTemplateTemp.setTemplateName("Test Template");

        when(upcTemplateTempRepository.findById(1)).thenReturn(Optional.of(upcTemplateTemp));
        when(upcTemplateRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.approveRejectUpcTemplate(request);

        assertNotNull(response);
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
        verify(upcTemplateTempRepository, times(1)).delete(upcTemplateTemp);
    }

    @Test
     void testApproveRejectUpcTemplate_Reject_Success() {
        ApproveRejectRQ request = new ApproveRejectRQ();
        request.setApproveRejectDataID(1);
        request.setApproveStatus(MasterDataApproveStatus.REJECTED);

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(1);
        upcTemplateTemp.setTemplateName("Test Template");
        upcTemplateTemp.setUpcTemplateDataTempList(Collections.emptyList());

        when(upcTemplateTempRepository.findById(1)).thenReturn(Optional.of(upcTemplateTemp));
        when(upcTemplateTempRepository.saveAndFlush(any(UpcTemplateTemp.class))).thenReturn(upcTemplateTemp);

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.approveRejectUpcTemplate(request);

        assertNotNull(response);
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(ErrorEnums.SUCCESS_CODE.getLabel(), response.getBody().getMessage());
        verify(upcTemplateAudRepository, times(1)).save(any(UpcTemplateAud.class));
    }


    @Test
     void testApproveRejectUpcTemplate_InvalidRequest_ThrowsException() {
        ApproveRejectRQ request = new ApproveRejectRQ();

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.approveRejectUpcTemplate(request)
        );
        assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
    }

    @Test
     void testApproveRejectUpcTemplate_TemplateNotFound_ThrowsException() {
        ApproveRejectRQ request = new ApproveRejectRQ();
        request.setApproveRejectDataID(1);

        when(upcTemplateTempRepository.findById(1)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.approveRejectUpcTemplate(request)
        );
        assertEquals("UPC Template with ID 1does not exist", exception.getMessage());
    }

    /**
     * Test when updateApprovedUpcTemplate()
     */

    @Test
     void testUpdateApprovedUpcTemplate_Success() throws ApiRequestException {
        Integer upcTemplateID = 1;
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName("New Template");
        upcTemplateDTO.setUpcLabel("New Label");
        upcTemplateDTO.setDescription("Description");
        upcTemplateDTO.setUpcLabelBackgroundColor("Black");
        upcTemplateDTO.setUpcLabelFontColor("White");
        upcTemplateDTO.setStatus(Status.ACT);
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);


        UpcTemplate upcTemplate = new UpcTemplate();
        upcTemplate.setUpcTemplateID(upcTemplateID);
        upcTemplate.setTemplateName("Old Template");

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(upcTemplateID);
        upcTemplateTemp.setTemplateName("New Template");

        when(upcTemplateRepository.findById(upcTemplateID)).thenReturn(Optional.of(upcTemplate));
        when(upcTemplateTempRepository.saveAndFlush(any(UpcTemplateTemp.class))).thenReturn(upcTemplateTemp);
        when(upcTemplateTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.updateApprovedUpcTemplate(upcTemplateID, upcTemplateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
     void testTemplateNameExistsInTemporaryRecords() {
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName("New Template");
        upcTemplateDTO.setUpcLabel("New Label");
        upcTemplateDTO.setDescription("Description");
        upcTemplateDTO.setUpcLabelBackgroundColor("Black");
        upcTemplateDTO.setUpcLabelFontColor("White");
        upcTemplateDTO.setStatus(Status.ACT);
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);

        lenient().when(upcTemplateTempRepository.exists(Mockito.any(Example.class))).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.updateApprovedUpcTemplate(1, upcTemplateDTO)
        );

        assertEquals("UPC Template with ID 1does not exist", exception.getMessage());
    }

    @Test
     void testTemplateNameExistsInMasterRecords() {
        String templateName = "Existing Template";
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName(templateName);
        upcTemplateDTO.setUpcLabel("New Label");
        upcTemplateDTO.setDescription("Description");
        upcTemplateDTO.setUpcLabelBackgroundColor("Black");
        upcTemplateDTO.setUpcLabelFontColor("White");
        upcTemplateDTO.setStatus(Status.ACT);
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);


        lenient().when(upcTemplateTempRepository.exists(Mockito.any(Example.class))).thenReturn(true);
        lenient().when(upcTemplateRepository.exists(Mockito.any(Example.class))).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.updateApprovedUpcTemplate(1, upcTemplateDTO)
        );

        assertEquals("UPC Template with ID 1does not exist", exception.getMessage());
    }

    @Test
     void testTemplateWithNoDataInUpcTemplateDataDTOList() throws ApiRequestException {
        String templateName = "Valid Template";
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName(templateName);
        upcTemplateDTO.setUpcLabel("New Label");
        upcTemplateDTO.setDescription("Description");
        upcTemplateDTO.setUpcLabelBackgroundColor("Black");
        upcTemplateDTO.setUpcLabelFontColor("White");
        upcTemplateDTO.setStatus(Status.ACT);
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);


        UpcTemplate upcTemplate = new UpcTemplate();
        upcTemplate.setUpcTemplateID(1);
        upcTemplate.setTemplateName(templateName);

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(1);
        upcTemplateTemp.setTemplateName(templateName);

        when(upcTemplateRepository.findById(1)).thenReturn(Optional.of(upcTemplate));
        when(upcTemplateTempRepository.saveAndFlush(Mockito.any(UpcTemplateTemp.class))).thenReturn(upcTemplateTemp);

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.updateApprovedUpcTemplate(1, upcTemplateDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
     void testTemplateWithNullFields() throws ApiRequestException {
        String templateName = "Valid Template with Null Fields";
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName(templateName);
        upcTemplateDTO.setUpcLabel("New Label");
        upcTemplateDTO.setDescription("Description");
        upcTemplateDTO.setUpcLabelBackgroundColor("Black");
        upcTemplateDTO.setUpcLabelFontColor("White");
        upcTemplateDTO.setStatus(Status.ACT);
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);

        UpcTemplate upcTemplate = new UpcTemplate();
        upcTemplate.setUpcTemplateID(1);
        upcTemplate.setTemplateName(templateName);

        UpcTemplateTemp upcTemplateTemp = new UpcTemplateTemp();
        upcTemplateTemp.setUpcTemplateID(1);
        upcTemplateTemp.setTemplateName(templateName);

        when(upcTemplateRepository.findById(1)).thenReturn(Optional.of(upcTemplate));
        when(upcTemplateTempRepository.saveAndFlush(Mockito.any(UpcTemplateTemp.class))).thenReturn(upcTemplateTemp);

        ResponseEntity<StandardResponse<Object>> response = upcTemplateService.updateApprovedUpcTemplate(1, upcTemplateDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNull(((UpcTemplateDTO) response.getBody().getResponse()).getUpcLabel());
        assertNull(((UpcTemplateDTO) response.getBody().getResponse()).getDescription());
    }

    @Test
    void testInvalidUpcSectionIDInUpcTemplateDataDTOList() {
        String templateName = "Template with Invalid Section";
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName(templateName);
        upcTemplateDTO.setUpcLabel("New Label");
        upcTemplateDTO.setDescription("Description");
        upcTemplateDTO.setUpcLabelBackgroundColor("Black");
        upcTemplateDTO.setUpcLabelFontColor("White");
        upcTemplateDTO.setStatus(Status.ACT);
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);

        UpcTemplateDataDTO dataDTO = new UpcTemplateDataDTO();
        dataDTO.setUpcSectionID(999); // Invalid section ID
        upcTemplateDTO.setUpcTemplateDataDTOList(List.of(dataDTO));

        UpcTemplate upcTemplate = new UpcTemplate();
        upcTemplate.setTemplateName("Existing Template Name"); // Set necessary fields to avoid null pointer
        when(upcTemplateRepository.findById(1)).thenReturn(Optional.of(upcTemplate));
        when(upcSectionRepository.findById(999)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.updateApprovedUpcTemplate(1, upcTemplateDTO)
        );

        assertEquals("UPC Section with ID 999does not exist", exception.getMessage());
    }


    @Test
     void testMissingRequiredFieldsInUpcTemplateDTO() {
        UpcTemplateDTO upcTemplateDTO = new UpcTemplateDTO();
        upcTemplateDTO.setTemplateName(null);
        upcTemplateDTO.setUpcLabel(null);
        upcTemplateDTO.setDescription(null);
        upcTemplateDTO.setUpcLabelBackgroundColor(null);
        upcTemplateDTO.setUpcLabelFontColor(null);
        upcTemplateDTO.setStatus(Status.ACT);
        upcTemplateDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        upcTemplateDTO.setIsModified(AppsConstants.YesNo.N);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.updateApprovedUpcTemplate(1, upcTemplateDTO)
        );

        assertFalse(exception.getMessage().contains("Template name is required"));
    }

    /**
     * Test when deleteUpcTemplateFromTemp()
     */
    @Test
     void testDeleteUpcTemplateFromTemp_TemplateDoesNotExist() throws ApiRequestException {
        Integer upcTemplateID = 99;

        when(upcTemplateTempRepository.existsById(upcTemplateID)).thenReturn(false);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                upcTemplateService.deleteUpcTemplateFromTemp(upcTemplateID)
        );

        assertEquals("UPC Template with ID 99 does not exist", exception.getMessage());

        verify(upcTemplateTempRepository, times(0)).deleteById(upcTemplateID);
        verify(upcTemplateDataTempRepository, times(0)).deleteByUpcTemplateTempUpcTemplateID(upcTemplateID);
    }

    /**
     * Test when saveOrUpdateMasterTemplateData()
     */
    @Test
     void testSaveOrUpdateMasterTemplateData_NullTemp() {

        UpcTemplate master = mock(UpcTemplate.class);

        List<UpcTemplateData> result = upcTemplateService.saveOrUpdateMasterTemplateData(null, master);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(upcTemplateDataRepository, times(0)).deleteAll(anyList());
        verify(upcTemplateDataRepository, times(0)).saveAll(anyList());
        verify(upcTemplateDataTempRepository, times(0)).deleteAll(anyList());
    }

    @Test
     void testSaveOrUpdateMasterTemplateData_NullMaster() {

        UpcTemplateTemp temp = mock(UpcTemplateTemp.class);

        List<UpcTemplateData> result = upcTemplateService.saveOrUpdateMasterTemplateData(temp, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(upcTemplateDataRepository, times(0)).deleteAll(anyList());
        verify(upcTemplateDataRepository, times(0)).saveAll(anyList());
        verify(upcTemplateDataTempRepository, times(0)).deleteAll(anyList());
    }

    @Test
     void testSaveOrUpdateMasterTemplateData_NullTempAndMaster() {

        List<UpcTemplateData> result = upcTemplateService.saveOrUpdateMasterTemplateData(null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(upcTemplateDataRepository, times(0)).deleteAll(anyList());
        verify(upcTemplateDataRepository, times(0)).saveAll(anyList());
        verify(upcTemplateDataTempRepository, times(0)).deleteAll(anyList());
    }

    @Test
     void testSaveOrUpdateMasterTemplateData_ValidInput() {

        UpcTemplateTemp temp = mock(UpcTemplateTemp.class);
        UpcTemplate master = mock(UpcTemplate.class);

        UpcTemplateDataTemp tempData1 = mock(UpcTemplateDataTemp.class);
        UpcTemplateDataTemp tempData2 = mock(UpcTemplateDataTemp.class);
        UpcSection upcSection1 = mock(UpcSection.class);
        UpcSection upcSection2 = mock(UpcSection.class);

        when(upcSection1.getUpcSectionID()).thenReturn(101);
        when(upcSection2.getUpcSectionID()).thenReturn(102);

        when(tempData1.getUpcSection()).thenReturn(upcSection1);
        when(tempData2.getUpcSection()).thenReturn(upcSection2);
        when(tempData1.getUpcTemplateTemp()).thenReturn(temp);
        when(tempData2.getUpcTemplateTemp()).thenReturn(temp);

        when(temp.getUpcTemplateDataTempList()).thenReturn(Arrays.asList(tempData1, tempData2));
        when(master.getUpcTemplateID()).thenReturn(1);

        UpcTemplateData existingData1 = new UpcTemplateData();
        UpcTemplateData existingData2 = new UpcTemplateData();
        when(upcTemplateDataRepository.findByUpcTemplateUpcTemplateID(1))
                .thenReturn(Arrays.asList(existingData1, existingData2));

        UpcTemplateData savedData1 = new UpcTemplateData();
        UpcTemplateData savedData2 = new UpcTemplateData();
        when(upcTemplateDataRepository.saveAll(anyList())).thenReturn(Arrays.asList(savedData1, savedData2));

        List<UpcTemplateData> result = upcTemplateService.saveOrUpdateMasterTemplateData(temp, master);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(upcTemplateDataRepository, times(1)).findByUpcTemplateUpcTemplateID(1);
        verify(upcTemplateDataRepository, times(1)).deleteAll(Arrays.asList(existingData1, existingData2));
        verify(upcTemplateDataRepository, times(1)).saveAll(anyList());
        verify(upcTemplateDataTempRepository, times(1)).deleteAll(anyList());
    }
}