package lk.sampath.casadminPortalms.service;

import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDoc;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.InputFieldValueType;
import lk.sampath.casadminportalms.enums.InterestRatingSubCategory;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeRepository;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocRepository;
import lk.sampath.casadminportalms.service.impl.CreditFacilityTemplateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditFacilityTemplateServiceImplTest {

    @Mock
    private CreditFacilityTemplateTempRepository creditFacilityTemplateTempRepository;

    @Mock
    private CreditFacilityTemplateRepository creditFacilityTemplateRepository;

    @Mock
    private CftInterestRateTempRepository cftInterestRateTempRepository;

    @Mock
    private CftVitalInfoTempRepository cftVitalInfoTempRepository;

    @Mock
    private CftCustomFacilityInfoTempRepository cftCustomFacilityInfoTempRepository;

    @Mock
    private CftSupportingDocTempRepository cftSupportingDocTempRepository;

    @Mock
    private SupportingDocRepository supportingDocRepository;

    @Mock
    private CftOtherFacilityInfoTempRepository otherFacilityInfoTempRepository;

    @Mock
    private CreditFacilityTypeRepository creditFacilityTypeRepository;

    @Mock
    private CftVitalInfoAudRepo cftVitalInfoAudRepo;

    @Mock
    private CftInterestRateAudRepo cftInterestRateAudRepo;

    @Mock
    private CftSupportingDocAudRepo cftSupportingDocAudRepo;

    @Mock
    private CftOtherFacilityInfoAudRepo cftOtherFacilityInfoAudRepo;

    @Mock
    private CftCustomFacilityInfoAudRepo cftCustomFacilityInfoAudRepo;

    @InjectMocks
    private CreditFacilityTemplateServiceImpl creditFacilityService;

    private CreditFacilityTemplateTemp creditFacilityTemplateTemp;

    private CreditFacilityTemplateDTO creditFacilityTemplateDTO;
    private CreditFacilityTemplate creditFacilityTemplate;
    private CftVitalInfo cftVitalInfo;
    private CftVitalInfoTemp cftVitalInfoTemp;
    private CftInterestRate cftInterestRate;
    private CftInterestRateTemp cftInterestRateTemp;
    private CftSupportingDoc cftSupportingDoc;
    private CftSupportingDocTemp cftSupportingDocTemp;
    private CftOtherFacilityInformationTemp cftOtherFacilityInformationTemp;
    private CftOtherFacilityInformation cftOtherFacilityInformation;
    private CftCustomFacilityInfoTemp cftCustomFacilityInfoTemp;
    private CftCustomFacilityInfo cftCustomFacilityInfo;

    @BeforeEach
    void setUp() {

        creditFacilityTemplateTemp = new CreditFacilityTemplateTemp();
        creditFacilityTemplateTemp.setCreditFacilityTemplateID(1);
        creditFacilityTemplateTemp.setCreditFacilityName("Test Facility");
        creditFacilityTemplateTemp.setDescription("Test Description");
        creditFacilityTemplateTemp.setMaxFacilityAmount(BigDecimal.valueOf(100000));
        creditFacilityTemplateTemp.setMinFacilityAmount(BigDecimal.valueOf(1000));
        creditFacilityTemplateTemp.setCftVitalInfos(new HashSet<>());
        creditFacilityTemplateTemp.setCftInterestRates(new HashSet<>());
        creditFacilityTemplateTemp.setCftSupportingDocs(new HashSet<>());
        creditFacilityTemplateTemp.setCftOtherFacilityInformations(new HashSet<>());
        creditFacilityTemplateTemp.setCftCustomFacilityInfos(new HashSet<>());

        creditFacilityTemplateDTO = new CreditFacilityTemplateDTO(creditFacilityTemplateTemp);

        creditFacilityTemplate = new CreditFacilityTemplate();
        creditFacilityTemplate.setCreditFacilityTemplateID(1);
        creditFacilityTemplate.setCreditFacilityName("Test Facility");
        creditFacilityTemplate.setDescription("Test Description");
        creditFacilityTemplate.setMaxFacilityAmount(BigDecimal.valueOf(100000));
        creditFacilityTemplate.setMinFacilityAmount(BigDecimal.valueOf(1000));
        creditFacilityTemplate.setCftVitalInfos(new HashSet<>());
        creditFacilityTemplate.setCftInterestRates(new HashSet<>());
        creditFacilityTemplate.setCftSupportingDocs(new HashSet<>());
        creditFacilityTemplate.setCftOtherFacilityInformations(new HashSet<>());
        creditFacilityTemplate.setCftCustomFacilityInfos(new HashSet<>());

        cftVitalInfo = new CftVitalInfo();
        cftVitalInfo.setCftVitalInfoID(10);
        cftVitalInfo.setCreditFacilityTemplate(creditFacilityTemplate);
        cftVitalInfo.setVitalInfoName("Vital Info");
        cftVitalInfo.setMandatory(AppsConstants.YesNo.Y);
        cftVitalInfo.setDisplayOrder(1);
        cftVitalInfo.setStatus(AppsConstants.Status.ACT);
        cftVitalInfo.setCreatedDate(new Date());
        cftVitalInfo.setLastModifiedDate(new Date());
        cftVitalInfo.setRecordStatus("New");

        // Setup CftVitalInfoTemp
        cftVitalInfoTemp = new CftVitalInfoTemp();
        cftVitalInfoTemp.setCftVitalInfoID(20);
        cftVitalInfoTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
        cftVitalInfoTemp.setVitalInfoName("Temp Vital Info");
        cftVitalInfoTemp.setMandatory(AppsConstants.YesNo.N);
        cftVitalInfoTemp.setDisplayOrder(2);
        cftVitalInfoTemp.setStatus(AppsConstants.Status.ACT);
        cftVitalInfoTemp.setCreatedDate(new Date());
        cftVitalInfoTemp.setLastModifiedDate(new Date());
        cftVitalInfoTemp.setRecordStatus("Temp");

        // Setup CftInterestRate
        cftInterestRate = new CftInterestRate();
        cftInterestRate.setCftInterestRateID(10);
        cftInterestRate.setCreditFacilityTemplate(creditFacilityTemplate);
        cftInterestRate.setRateName("Fixed Rate");
        cftInterestRate.setRateCode("FR001");
        cftInterestRate.setValue(5.5);
        cftInterestRate.setIsDefault(AppsConstants.YesNo.Y);
        cftInterestRate.setStatus(AppsConstants.Status.ACT);
        cftInterestRate.setInterestRatingSubCategory(InterestRatingSubCategory.EFFECTIVE);
        cftInterestRate.setIsEditable(AppsConstants.YesNo.N);
        cftInterestRate.setCreatedDate(new Date());
        cftInterestRate.setLastModifiedDate(new Date());
        cftInterestRate.setRecordStatus("New");

        // Setup CftInterestRateTemp
        cftInterestRateTemp = new CftInterestRateTemp();
        cftInterestRateTemp.setCftInterestRateID(20);
        cftInterestRateTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
        cftInterestRateTemp.setRateName("Variable Rate");
        cftInterestRateTemp.setRateCode("VR001");
        cftInterestRateTemp.setValue(4.2);
        cftInterestRateTemp.setIsDefault(AppsConstants.YesNo.N);
        cftInterestRateTemp.setStatus(AppsConstants.Status.ACT);
        cftInterestRateTemp.setInterestRatingSubCategory(InterestRatingSubCategory.FLAT);
        cftInterestRateTemp.setIsEditable(AppsConstants.YesNo.Y);
        cftInterestRateTemp.setCreatedDate(new Date());
        cftInterestRateTemp.setLastModifiedDate(new Date());
        cftInterestRateTemp.setRecordStatus("Temp");

        SupportingDoc supportingDoc = new SupportingDoc();
        supportingDoc.setSupportingDocID(100);

        // Setup CftSupportingDoc
        cftSupportingDoc = new CftSupportingDoc();
        cftSupportingDoc.setCftSupportingDocID(10);
        cftSupportingDoc.setCreditFacilityTemplate(creditFacilityTemplate);
        cftSupportingDoc.setSupportingDoc(supportingDoc);
        cftSupportingDoc.setMandatory(AppsConstants.YesNo.Y);
        cftSupportingDoc.setStatus(AppsConstants.Status.ACT);
        cftSupportingDoc.setCreatedDate(new Date());
        cftSupportingDoc.setLastModifiedDate(new Date());
        cftSupportingDoc.setRecordStatus("New");

        // Setup CftSupportingDocTemp
        cftSupportingDocTemp = new CftSupportingDocTemp();
        cftSupportingDocTemp.setCftSupportingDocID(20);
        cftSupportingDocTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
        cftSupportingDocTemp.setSupportingDoc(supportingDoc);
        cftSupportingDocTemp.setMandatory(AppsConstants.YesNo.N);
        cftSupportingDocTemp.setStatus(AppsConstants.Status.ACT);
        cftSupportingDocTemp.setCreatedDate(new Date());
        cftSupportingDocTemp.setLastModifiedDate(new Date());
        cftSupportingDocTemp.setRecordStatus("Temp");

        // Setup cftOtherFacilityInformation
        cftOtherFacilityInformation = new CftOtherFacilityInformation();
        cftOtherFacilityInformation.setCftOtherFacilityInfoID(1);
        cftOtherFacilityInformation.setCreditFacilityTemplate(creditFacilityTemplate);
        cftOtherFacilityInformation.setOtherFacilityInfoName("Test");
        cftOtherFacilityInformation.setDescription("Test Description");
        cftOtherFacilityInformation.setOtherFacilityInfoCode("CFT001");
        cftOtherFacilityInformation.setOtherFacilityInfoFieldType(InputFieldValueType.CURRENCY);
        cftOtherFacilityInformation.setDefaultValue("D");
        cftOtherFacilityInformation.setDisplayOrder(1);
        cftOtherFacilityInformation.setMandatory(AppsConstants.YesNo.Y);
        cftOtherFacilityInformation.setStatus(AppsConstants.Status.ACT);
        cftOtherFacilityInformation.setCreatedDate(new Date());
        cftOtherFacilityInformation.setLastModifiedDate(new Date());
        cftOtherFacilityInformation.setRecordStatus("Master");

        // Setup cftOtherFacilityInformationTemp
        cftOtherFacilityInformationTemp = new CftOtherFacilityInformationTemp();
        cftOtherFacilityInformationTemp.setCftOtherFacilityInfoID(1);
        cftOtherFacilityInformationTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
        cftOtherFacilityInformationTemp.setOtherFacilityInfoName("Test");
        cftOtherFacilityInformationTemp.setDescription("Test Description");
        cftOtherFacilityInformationTemp.setOtherFacilityInfoCode("CFT001");
        cftOtherFacilityInformationTemp.setOtherFacilityInfoFieldType(InputFieldValueType.CURRENCY);
        cftOtherFacilityInformationTemp.setDefaultValue("D");
        cftOtherFacilityInformationTemp.setDisplayOrder(1);
        cftOtherFacilityInformationTemp.setMandatory(AppsConstants.YesNo.Y);
        cftOtherFacilityInformationTemp.setStatus(AppsConstants.Status.ACT);
        cftOtherFacilityInformationTemp.setCreatedDate(new Date());
        cftOtherFacilityInformationTemp.setLastModifiedDate(new Date());
        cftOtherFacilityInformationTemp.setRecordStatus("Temp");

        // Setup cftCustomFacilityInfo
        cftCustomFacilityInfo = new CftCustomFacilityInfo();
        cftCustomFacilityInfo.setCftCustomFacilityInfoID(1);
        cftCustomFacilityInfo.setCreditFacilityTemplate(creditFacilityTemplate);
        cftCustomFacilityInfo.setCustomFacilityInfoName("Test");
        cftCustomFacilityInfo.setDescription("Test Description");
        cftCustomFacilityInfo.setCustomFacilityInfoCode("C001");
        cftCustomFacilityInfo.setFieldType("F");
        cftCustomFacilityInfo.setMandatory(AppsConstants.YesNo.Y);
        cftCustomFacilityInfo.setStatus(AppsConstants.Status.ACT);
        cftCustomFacilityInfo.setDisplayOrder(1);
        cftCustomFacilityInfo.setCreatedDate(new Date());
        cftCustomFacilityInfo.setLastModifiedDate(new Date());
        cftCustomFacilityInfo.setRecordStatus("Master");

        // Setup cftCustomFacilityInfoTemp
        cftCustomFacilityInfoTemp = new CftCustomFacilityInfoTemp();
        cftCustomFacilityInfoTemp.setCftCustomFacilityInfoID(1);
        cftCustomFacilityInfoTemp.setCreditFacilityTemplate(creditFacilityTemplateTemp);
        cftCustomFacilityInfoTemp.setCustomFacilityInfoName("Test");
        cftCustomFacilityInfoTemp.setDescription("Test Description");
        cftCustomFacilityInfoTemp.setCustomFacilityInfoCode("C001");
        cftCustomFacilityInfoTemp.setFieldType("F");
        cftCustomFacilityInfoTemp.setMandatory(AppsConstants.YesNo.Y);
        cftCustomFacilityInfoTemp.setStatus(AppsConstants.Status.ACT);
        cftCustomFacilityInfoTemp.setDisplayOrder(1);
        cftCustomFacilityInfoTemp.setCreatedDate(new Date());
        cftCustomFacilityInfoTemp.setLastModifiedDate(new Date());
        cftCustomFacilityInfoTemp.setRecordStatus("Temp");
    }

    /** getAllCreditFacilityTemplatesTemp **/
    @Test
    void testGetAllCreditFacilityTemplatesTemp() {
        List<CreditFacilityTemplateTemp> creditFacilityTemplateTempList = Arrays.asList(creditFacilityTemplateTemp);

        when(creditFacilityTemplateTempRepository.findAll()).thenReturn(creditFacilityTemplateTempList);

        ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> response = creditFacilityService.getAllCreditFacilityTemplatesTemp();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(creditFacilityTemplateTempRepository, times(1)).findAll();
    }


    /** getCreditFacilityTemplateTempByID **/
    @Test
    void testGetCreditFacilityTemplateTempByID_Success() {
        when(creditFacilityTemplateTempRepository.findById(1)).thenReturn(Optional.of(creditFacilityTemplateTemp));

        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response = creditFacilityService.getCreditFacilityTemplateTempByID(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetCreditFacilityTemplateTempByID_NotFound() {
        when(creditFacilityTemplateTempRepository.findById(2)).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            creditFacilityService.getCreditFacilityTemplateTempByID(2);
        });

        assertEquals("Credit Facility Template Temp with2Does Not Exists", thrown.getMessage());
    }

    /** getAllCreditFacilityTemplates **/
    @Test
    void testGetAllCreditFacilityTemplates_EmptyList() {
        when(creditFacilityTemplateRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> response = creditFacilityService.getAllCreditFacilityTemplates();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllCreditFacilityTemplates_Success() {
        List<CreditFacilityTemplate> templates = new ArrayList<>();
        templates.add(creditFacilityTemplate);

        when(creditFacilityTemplateRepository.findAll()).thenReturn(templates);

        ResponseEntity<StandardResponse<List<CreditFacilityTemplateDTO>>> response = creditFacilityService.getAllCreditFacilityTemplates();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }


    /** getCreditFacilityTemplateByID **/

    @Test
    void testGetCreditFacilityTemplateByID_Success() {
        when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.of(creditFacilityTemplate));

        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response = creditFacilityService.getCreditFacilityTemplateByID(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetCreditFacilityTemplateByID_NotFound() {
        when(creditFacilityTemplateRepository.findById(2)).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            creditFacilityService.getCreditFacilityTemplateByID(2);
        });

        assertEquals("Credit Facility Template  with2Does Not Exists", thrown.getMessage());
    }

    /** saveCtfInterestRateTemp **/
    @Test
    void testSaveCtfInterestRateTemp_Success() {
        List<CftInterestRateDTO> interestRateDTOList = new ArrayList<>();
        CftInterestRateDTO rateDTO = new CftInterestRateDTO();
        rateDTO.setRateName("Test Rate");
        rateDTO.setRateCode("TR001");
        interestRateDTOList.add(rateDTO);

        creditFacilityTemplateDTO.setCftInterestRateDTOList(interestRateDTOList);
        creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.Y);

        when(cftInterestRateTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(anyInt())).thenReturn(new ArrayList<>());
        when(cftInterestRateTempRepository.getNextSequenceValue()).thenReturn(1);

        Set<CftInterestRateTemp> result = creditFacilityService.saveCtfInterestRateTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSaveCtfInterestRateTemp_NoChange() {
        creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);

        Set<CftInterestRateTemp> result = creditFacilityService.saveCtfInterestRateTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** saveCftVitalInfoTemp **/
    @Test
    void testSaveCftVitalInfoTemp_Success() {
        List<CftVitalInfoDTO> vitalInfoDTOList = new ArrayList<>();
        CftVitalInfoDTO vitalInfoDTO = new CftVitalInfoDTO();
        vitalInfoDTO.setVitalInfoName("Vital Info");
        vitalInfoDTOList.add(vitalInfoDTO);

        creditFacilityTemplateDTO.setCftVitalInfoDTOList(vitalInfoDTOList);
        creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.Y);

        when(cftVitalInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(anyInt())).thenReturn(new ArrayList<>());
        when(cftVitalInfoTempRepository.getNextSequenceValue()).thenReturn(1);

        Set<CftVitalInfoTemp> result = creditFacilityService.saveCftVitalInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSaveCftVitalInfoTemp_NoChange() {
        creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);

        Set<CftVitalInfoTemp> result = creditFacilityService.saveCftVitalInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** saveCftCustomFacilityInfoTemp **/
    @Test
    void testSaveCftCustomFacilityInfoTemp_Success() {
        List<CftCustomFacilityInfoDTO> customFacilityInfoDTOList = new ArrayList<>();
        CftCustomFacilityInfoDTO customFacilityInfoDTO = new CftCustomFacilityInfoDTO();
        customFacilityInfoDTO.setCustomFacilityInfoName("Custom Facility Info");
        customFacilityInfoDTOList.add(customFacilityInfoDTO);

        creditFacilityTemplateDTO.setCftCustomFacilityInfoDTOList(customFacilityInfoDTOList);
        creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.Y);

        when(cftCustomFacilityInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(anyInt())).thenReturn(new ArrayList<>());
        when(cftCustomFacilityInfoTempRepository.getNextSequenceValue()).thenReturn(1);

        Set<CftCustomFacilityInfoTemp> result = creditFacilityService.saveCftCustomFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSaveCftCustomFacilityInfoTemp_NoChange() {
        creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);

        Set<CftCustomFacilityInfoTemp> result = creditFacilityService.saveCftCustomFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** saveCftSupportingDocTemp **/
    @Test
    void testSaveCftSupportingDocTemp_Success() {
        List<CftSupportingDocDTO> supportingDocDTOList = new ArrayList<>();
        CftSupportingDocDTO supportingDocDTO = new CftSupportingDocDTO();
        supportingDocDTO.setSupportingDocID(1);
        supportingDocDTOList.add(supportingDocDTO);

        creditFacilityTemplateDTO.setCftSupportingDocDTOList(supportingDocDTOList);
        creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.Y);

        SupportingDoc supportingDoc = new SupportingDoc();
        when(supportingDocRepository.findById(1)).thenReturn(Optional.of(supportingDoc));
        when(cftSupportingDocTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(anyInt())).thenReturn(new ArrayList<>());
        when(cftSupportingDocTempRepository.getNextSequenceValue()).thenReturn(1);

        Set<CftSupportingDocTemp> result = creditFacilityService.saveCftSupportingDocTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSaveCftSupportingDocTemp_NoChange() {
        creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);

        Set<CftSupportingDocTemp> result = creditFacilityService.saveCftSupportingDocTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** saveCftOtherFacilityInfoTemp **/
    @Test
    void testSaveCftOtherFacilityInfoTemp_Success() {
        List<CftOtherFacilityInfoDTO> facilityInfoDTOList = new ArrayList<>();
        CftOtherFacilityInfoDTO facilityInfoDTO = new CftOtherFacilityInfoDTO();
        facilityInfoDTO.setOtherFacilityInfoName("Other Facility Info");
        facilityInfoDTOList.add(facilityInfoDTO);

        creditFacilityTemplateDTO.setCftOtherFacilityInfoDTOList(facilityInfoDTOList);
        creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.Y);

        when(otherFacilityInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(anyInt())).thenReturn(new ArrayList<>());
        when(otherFacilityInfoTempRepository.getNextSequenceValue()).thenReturn(1);

        Set<CftOtherFacilityInformationTemp> result = creditFacilityService.saveCftOtherFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSaveCftOtherFacilityInfoTemp_NoChange() {
        creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);

        Set<CftOtherFacilityInformationTemp> result = creditFacilityService.saveCftOtherFacilityInfoTemp(creditFacilityTemplateDTO, creditFacilityTemplateTemp);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /** createCreditFacilityTemplateTempObj **/
    @Test
    void testCreateCreditFacilityTemplateTempObj_New() {
        creditFacilityTemplateDTO.setCreditFacilityTypeID(1);
        CreditFacilityType creditFacilityType = new CreditFacilityType();
        when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));

        CreditFacilityTemplateTemp result = creditFacilityService.createCreditFacilityTemplateTempObj(creditFacilityTemplateDTO, true);

        assertNotNull(result);
        assertEquals(creditFacilityTemplateDTO.getCreditFacilityName(), result.getCreditFacilityName());
        assertEquals(creditFacilityType, result.getCreditFacilityType());
    }

    @Test
    void testCreateCreditFacilityTemplateTempObj_Existing() {
        creditFacilityTemplateDTO.setCreditFacilityTypeID(null);

        CreditFacilityTemplateTemp result = creditFacilityService.createCreditFacilityTemplateTempObj(creditFacilityTemplateDTO, false);

        assertNotNull(result);
        assertEquals(creditFacilityTemplateDTO.getCreditFacilityName(), result.getCreditFacilityName());
    }

    /** saveCreditFacilityTemplateTemp **/
    @Test
    void testSaveCreditFacilityTemplateTemp_FacilityAlreadyExistsInMaster() {
        List<CreditFacilityTemplate> masterList = new ArrayList<>();
        masterList.add(new CreditFacilityTemplate());
        when(creditFacilityTemplateTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(new ArrayList<>());
        when(creditFacilityTemplateRepository.findAll(any(BooleanBuilder.class))).thenReturn(masterList);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            creditFacilityService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);
        });

        assertEquals("Credit Facility Template Name Already Exists in Master Table", exception.getMessage());
    }

    @Test
    void testSaveCreditFacilityTemplateTemp_FacilityAlreadyExistsInTemp() {
        List<CreditFacilityTemplateTemp> tempList = new ArrayList<>();
        tempList.add(creditFacilityTemplateTemp);
        when(creditFacilityTemplateTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(tempList);
        when(creditFacilityTemplateRepository.findAll(any(BooleanBuilder.class))).thenReturn(new ArrayList<>());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            creditFacilityService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);
        });

        assertEquals("Credit Facility Template Name Already Exists in Temp Table", exception.getMessage());
    }

    /** updateCreditFacilityTemplateTemp **/

    @Test
    void testUpdateCreditFacilityTemplateTemp_Success() {

        creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
        creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);
        creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);
        creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.Y);
        creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);


        when(creditFacilityTemplateTempRepository.findById(1)).thenReturn(Optional.of(creditFacilityTemplateTemp));
        when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
        when(creditFacilityTemplateTempRepository.saveAndFlush(any())).thenReturn(creditFacilityTemplateTemp);

        ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
                creditFacilityService.updateCreditFacilityTemplateTemp(1, creditFacilityTemplateDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateCreditFacilityTemplateTemp_RecordNotFound() {
        when(creditFacilityTemplateTempRepository.findById(1)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                creditFacilityService.updateCreditFacilityTemplateTemp(1, creditFacilityTemplateDTO));

        assertEquals("Not Found", exception.getMessage());
    }

    /** insertToCftVitalInfoAuditTable **/
    @Test
    void testInsertToCftVitalInfoAuditTable_WithTempData() {
        when(cftVitalInfoAudRepo.saveAndFlush(any())).thenReturn(new CftVitalInfoAud());

        Boolean result = creditFacilityService.insertToCftVitalInfoAuditTable(null, cftVitalInfoTemp);

        assertTrue(result);
        verify(cftVitalInfoAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftVitalInfoAuditTable_WithPermanentData() {
        when(cftVitalInfoAudRepo.saveAndFlush(any())).thenReturn(new CftVitalInfoAud());

        Boolean result = creditFacilityService.insertToCftVitalInfoAuditTable(cftVitalInfo, null);

        assertTrue(result);
        verify(cftVitalInfoAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftVitalInfoAuditTable_WithBothNull_ShouldThrowException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                creditFacilityService.insertToCftVitalInfoAuditTable(null, null));

        assertEquals("Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftVitalInfo.getCftVitalInfoID()\" because \"cftVitalInfo\" is null", exception.getMessage());
    }

    /** insertToCftInterestRateAuditTable **/

    @Test
    void testInsertToCftInterestRateAuditTable_WithTempData() {
        when(cftInterestRateAudRepo.saveAndFlush(any())).thenReturn(new CftInterestRateAud());

        Boolean result = creditFacilityService.insertToCftInterestRateAuditTable(null, cftInterestRateTemp);

        assertTrue(result);
        verify(cftInterestRateAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftInterestRateAuditTable_WithPermanentData() {
        when(cftInterestRateAudRepo.saveAndFlush(any())).thenReturn(new CftInterestRateAud());

        Boolean result = creditFacilityService.insertToCftInterestRateAuditTable(cftInterestRate, null);

        assertTrue(result);
        verify(cftInterestRateAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftInterestRateAuditTable_WithBothNull_ShouldThrowException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                creditFacilityService.insertToCftInterestRateAuditTable(null, null));

        assertEquals("Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftInterestRate.getCftInterestRateID()\" because \"cftInterestRate\" is null", exception.getMessage());
    }

    /** insertToCftSupportingDocAuditTable **/

    @Test
    void testInsertToCftSupportingDocAuditTable_WithTempData() {
        when(cftSupportingDocAudRepo.saveAndFlush(any())).thenReturn(new CftSupportingDocAud());

        Boolean result = creditFacilityService.insertToCftSupportingDocAuditTable(null, cftSupportingDocTemp);

        assertTrue(result);
        verify(cftSupportingDocAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftSupportingDocAuditTable_WithPermanentData() {
        when(cftSupportingDocAudRepo.saveAndFlush(any())).thenReturn(new CftSupportingDocAud());

        Boolean result = creditFacilityService.insertToCftSupportingDocAuditTable(cftSupportingDoc, null);

        assertTrue(result);
        verify(cftSupportingDocAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftSupportingDocAuditTable_WithBothNull_ShouldThrowException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                creditFacilityService.insertToCftSupportingDocAuditTable(null, null));

        assertEquals("Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftSupportingDoc.getCftSupportingDocID()\" because \"cftSupportingDoc\" is null", exception.getMessage());
    }

    /** insertToCftOtherFacilityInfoAuditTable **/
    @Test
    void testInsertToCftOtherFacilityInfoAuditTable_WithTempData() {
        when(cftOtherFacilityInfoAudRepo.saveAndFlush(any())).thenReturn(new CftOtherFacilityInformationAud());

        Boolean result = creditFacilityService.insertToCftOtherFacilityInfoAuditTable(null, cftOtherFacilityInformationTemp);

        assertTrue(result);
        verify(cftOtherFacilityInfoAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftOtherFacilityInfoAuditTable_WithPermanentData() {
        when(cftOtherFacilityInfoAudRepo.saveAndFlush(any())).thenReturn(new CftOtherFacilityInformationAud());

        Boolean result = creditFacilityService.insertToCftOtherFacilityInfoAuditTable(cftOtherFacilityInformation, null);

        assertTrue(result);
        verify(cftOtherFacilityInfoAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftOtherFacilityInfoAuditTable_WithBothNull_ShouldThrowException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                creditFacilityService.insertToCftOtherFacilityInfoAuditTable(null, null));

        assertEquals("Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformation.getCftOtherFacilityInfoID()\" because \"cftOtherFacilityInformation\" is null", exception.getMessage());
    }

    /** insertToCftCustomFacilityInfoAuditTable **/
    @Test
    void testInsertToCftCustomFacilityInfoAuditTable_WithTempData() {
        when(cftCustomFacilityInfoAudRepo.saveAndFlush(any())).thenReturn(new CftCustomFacilityInfoAud());

        Boolean result = creditFacilityService.insertToCftCustomFacilityInfoAuditTable(null, cftCustomFacilityInfoTemp);

        assertTrue(result);
        verify(cftCustomFacilityInfoAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftCustomFacilityInfoAuditTable_WithPermanentData() {
        when(cftCustomFacilityInfoAudRepo.saveAndFlush(any())).thenReturn(new CftCustomFacilityInfoAud());

        Boolean result = creditFacilityService.insertToCftCustomFacilityInfoAuditTable(cftCustomFacilityInfo, null);

        assertTrue(result);
        verify(cftCustomFacilityInfoAudRepo, times(1)).saveAndFlush(any());
    }

    @Test
    void testInsertToCftCustomFacilityInfoAuditTable_WithBothNull_ShouldThrowException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                creditFacilityService.insertToCftCustomFacilityInfoAuditTable(null, null));

        assertEquals("Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftCustomFacilityInfo.getCftCustomFacilityInfoID()\" because \"cftCustomFacilityInfo\" is null", exception.getMessage());
    }
}