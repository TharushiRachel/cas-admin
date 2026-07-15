package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.querydsl.core.BooleanBuilder;
import java.math.BigDecimal;
import java.util.*;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDoc;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.InputFieldValueType;
import lk.sampath.casadminportalms.enums.InterestRatingSubCategory;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.creditfacilitytemplate.*;
import lk.sampath.casadminportalms.repository.creditfacilitytype.CreditFacilityTypeRepository;
import lk.sampath.casadminportalms.repository.supportingdoc.SupportingDocRepository;
import lk.sampath.casadminportalms.service.impl.CreditFacilityTemplateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CreditFacilityTemplateServiceImplTest {

  @Mock private CreditFacilityTemplateTempRepository creditFacilityTemplateTempRepository;

  @Mock private CreditFacilityTemplateRepository creditFacilityTemplateRepository;

  @Mock private CftInterestRateTempRepository cftInterestRateTempRepository;

  @Mock private CftVitalInfoTempRepository cftVitalInfoTempRepository;

  @Mock private CftCustomFacilityInfoTempRepository cftCustomFacilityInfoTempRepository;

  @Mock private CftSupportingDocTempRepository cftSupportingDocTempRepository;

  @Mock private SupportingDocRepository supportingDocRepository;

  @Mock private CftOtherFacilityInfoTempRepository otherFacilityInfoTempRepository;

  @Mock private CreditFacilityTypeRepository creditFacilityTypeRepository;

  @Mock private CftVitalInfoAudRepo cftVitalInfoAudRepo;

  @Mock private CftInterestRateAudRepo cftInterestRateAudRepo;

  @Mock private CftSupportingDocAudRepo cftSupportingDocAudRepo;

  @Mock private CftOtherFacilityInfoAudRepo cftOtherFacilityInfoAudRepo;

  @Mock private CftCustomFacilityInfoAudRepo cftCustomFacilityInfoAudRepo;

  @Mock private CftVitalInfoRepository cftVitalInfoRepository;

  @Mock private CftInterestRateRepository cftInterestRateRepository;

  @Mock private CftSupportingDocRepository cftSupportingDocRepository;

  @Mock private CftOtherFacilityInfoRepository otherFacilityInfoRepository;

  @Mock private CftCustomFacilityInfoRepository cftCustomFacilityInfoRepository;

  @Mock private CreditFacilityTemplateAudRepo creditFacilityTemplateAudRepo;

  @InjectMocks private CreditFacilityTemplateServiceImpl creditFacilityService;

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

  /** getAllCreditFacilityTemplatesTemp * */
  @Test
  void testGetAllCreditFacilityTemplatesTemp_Success() {
    Pageable pageable = PageRequest.of(0, 10);
    CftResponse cftResponse = new CftResponse();
    cftResponse.setCreditFacilityTemplateID(1);
    cftResponse.setCreditFacilityName("Test Facility");
    Page<CftResponse> cftResponsePage = new PageImpl<>(Arrays.asList(cftResponse), pageable, 1);

    when(creditFacilityTemplateTempRepository.findAllTemplates(pageable)).thenReturn(cftResponsePage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplatesTemp(pageable);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());
    Page<CftResponse> returnedPage = (Page<CftResponse>) response.getBody().getResponse();
    assertEquals(1, returnedPage.getTotalElements());
    verify(creditFacilityTemplateTempRepository, times(1)).findAllTemplates(pageable);
  }

  @Test
  void testGetAllCreditFacilityTemplatesTemp_EmptyPage() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<CftResponse> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

    when(creditFacilityTemplateTempRepository.findAllTemplates(pageable)).thenReturn(emptyPage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplatesTemp(pageable);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    assertNotNull(response.getBody());
    Page<CftResponse> returnedPage = (Page<CftResponse>) response.getBody().getResponse();
    assertTrue(returnedPage.getContent().isEmpty());
    verify(creditFacilityTemplateTempRepository, times(1)).findAllTemplates(pageable);
  }

  @Test
  void testGetAllCreditFacilityTemplatesTemp_MultipleRecords() {
    Pageable pageable = PageRequest.of(0, 5);
    CftResponse first = new CftResponse();
    first.setCreditFacilityTemplateID(1);
    CftResponse second = new CftResponse();
    second.setCreditFacilityTemplateID(2);
    Page<CftResponse> cftResponsePage =
            new PageImpl<>(Arrays.asList(first, second), pageable, 2);

    when(creditFacilityTemplateTempRepository.findAllTemplates(pageable)).thenReturn(cftResponsePage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplatesTemp(pageable);

    assertNotNull(response);
    Page<CftResponse> returnedPage = (Page<CftResponse>) response.getBody().getResponse();
    assertEquals(2, returnedPage.getContent().size());
  }

  @Test
  void testGetAllCreditFacilityTemplatesTemp_UsesGivenPageable() {
    Pageable pageable = PageRequest.of(2, 20);
    Page<CftResponse> cftResponsePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

    when(creditFacilityTemplateTempRepository.findAllTemplates(any(Pageable.class)))
            .thenReturn(cftResponsePage);

    creditFacilityService.getAllCreditFacilityTemplatesTemp(pageable);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(creditFacilityTemplateTempRepository).findAllTemplates(pageableCaptor.capture());
    assertEquals(2, pageableCaptor.getValue().getPageNumber());
    assertEquals(20, pageableCaptor.getValue().getPageSize());
  }

  @Test
  void testGetAllCreditFacilityTemplatesTemp_SuccessResponseMessageMatches() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<CftResponse> cftResponsePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

    when(creditFacilityTemplateTempRepository.findAllTemplates(pageable)).thenReturn(cftResponsePage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplatesTemp(pageable);

    assertEquals("Success", response.getBody().getMessage());
  }

  /** getCreditFacilityTemplateTempByID * */
  @Test
  void testGetCreditFacilityTemplateTempByID_Success() {
    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.getCreditFacilityTemplateTempByID(1);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
  }

  @Test
  void testGetCreditFacilityTemplateTempByID_NotFound() {
    when(creditFacilityTemplateTempRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException thrown =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      creditFacilityService.getCreditFacilityTemplateTempByID(2);
                    });

    assertEquals("Credit Facility Template Temp with2Does Not Exists", thrown.getMessage());
  }

  @Test
  void testGetCreditFacilityTemplateTempByID_MapsFieldsCorrectly() {
    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.getCreditFacilityTemplateTempByID(1);

    CreditFacilityTemplateDTO returnedDTO =
            (CreditFacilityTemplateDTO) response.getBody().getResponse();
    assertEquals(creditFacilityTemplateTemp.getCreditFacilityName(), returnedDTO.getCreditFacilityName());
    assertEquals(creditFacilityTemplateTemp.getMaxFacilityAmount(), returnedDTO.getMaxFacilityAmount());
  }

  @Test
  void testGetCreditFacilityTemplateTempByID_VerifiesRepositoryCalledOnce() {
    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));

    creditFacilityService.getCreditFacilityTemplateTempByID(1);

    verify(creditFacilityTemplateTempRepository, times(1)).findById(1);
  }

  @Test
  void testGetCreditFacilityTemplateTempByID_DifferentIdReturnsCorrespondingRecord() {
    CreditFacilityTemplateTemp anotherTemp = new CreditFacilityTemplateTemp();
    anotherTemp.setCreditFacilityTemplateID(5);
    anotherTemp.setCreditFacilityName("Another Facility");
    anotherTemp.setCftVitalInfos(new HashSet<>());
    anotherTemp.setCftInterestRates(new HashSet<>());
    anotherTemp.setCftSupportingDocs(new HashSet<>());
    anotherTemp.setCftOtherFacilityInformations(new HashSet<>());
    anotherTemp.setCftCustomFacilityInfos(new HashSet<>());

    when(creditFacilityTemplateTempRepository.findById(5)).thenReturn(Optional.of(anotherTemp));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.getCreditFacilityTemplateTempByID(5);

    CreditFacilityTemplateDTO returnedDTO =
            (CreditFacilityTemplateDTO) response.getBody().getResponse();
    assertEquals("Another Facility", returnedDTO.getCreditFacilityName());
  }

  /** getAllCreditFacilityTemplates * */
  @Test
  void testGetAllCreditFacilityTemplates_EmptyList() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<CftResponse> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
    when(creditFacilityTemplateRepository.findAllTemplates(pageable)).thenReturn(emptyPage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplates(pageable);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    Page<CftResponse> returnedPage = (Page<CftResponse>) response.getBody().getResponse();
    assertTrue(returnedPage.getContent().isEmpty());
  }

  @Test
  void testGetAllCreditFacilityTemplates_Success() {
    Pageable pageable = PageRequest.of(0, 10);
    CftResponse cftResponse = new CftResponse();
    cftResponse.setCreditFacilityTemplateID(1);
    cftResponse.setCreditFacilityName("Test Facility");
    Page<CftResponse> cftResponsePage = new PageImpl<>(Arrays.asList(cftResponse), pageable, 1);

    when(creditFacilityTemplateRepository.findAllTemplates(pageable)).thenReturn(cftResponsePage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplates(pageable);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    Page<CftResponse> returnedPage = (Page<CftResponse>) response.getBody().getResponse();
    assertEquals(1, returnedPage.getTotalElements());
    verify(creditFacilityTemplateRepository, times(1)).findAllTemplates(pageable);
  }

  @Test
  void testGetAllCreditFacilityTemplates_MultipleRecords() {
    Pageable pageable = PageRequest.of(0, 5);
    CftResponse first = new CftResponse();
    first.setCreditFacilityTemplateID(1);
    CftResponse second = new CftResponse();
    second.setCreditFacilityTemplateID(2);
    Page<CftResponse> cftResponsePage = new PageImpl<>(Arrays.asList(first, second), pageable, 2);

    when(creditFacilityTemplateRepository.findAllTemplates(pageable)).thenReturn(cftResponsePage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplates(pageable);

    Page<CftResponse> returnedPage = (Page<CftResponse>) response.getBody().getResponse();
    assertEquals(2, returnedPage.getContent().size());
  }

  @Test
  void testGetAllCreditFacilityTemplates_UsesGivenPageable() {
    Pageable pageable = PageRequest.of(3, 15);
    Page<CftResponse> cftResponsePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

    when(creditFacilityTemplateRepository.findAllTemplates(any(Pageable.class)))
            .thenReturn(cftResponsePage);

    creditFacilityService.getAllCreditFacilityTemplates(pageable);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(creditFacilityTemplateRepository).findAllTemplates(pageableCaptor.capture());
    assertEquals(3, pageableCaptor.getValue().getPageNumber());
    assertEquals(15, pageableCaptor.getValue().getPageSize());
  }

  @Test
  void testGetAllCreditFacilityTemplates_SuccessResponseMessageMatches() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<CftResponse> cftResponsePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

    when(creditFacilityTemplateRepository.findAllTemplates(pageable)).thenReturn(cftResponsePage);

    ResponseEntity<StandardResponse<Page<CftResponse>>> response =
            creditFacilityService.getAllCreditFacilityTemplates(pageable);

    assertEquals("Success", response.getBody().getMessage());
  }

  /** getCreditFacilityTemplateByID * */
  @Test
  void testGetCreditFacilityTemplateByID_Success() {
    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.getCreditFacilityTemplateByID(1);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
  }

  @Test
  void testGetCreditFacilityTemplateByID_NotFound() {
    when(creditFacilityTemplateRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException thrown =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      creditFacilityService.getCreditFacilityTemplateByID(2);
                    });

    assertEquals("Credit Facility Template  with2Does Not Exists", thrown.getMessage());
  }

  @Test
  void testGetCreditFacilityTemplateByID_MapsFieldsCorrectly() {
    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.getCreditFacilityTemplateByID(1);

    CreditFacilityTemplateDTO returnedDTO =
            (CreditFacilityTemplateDTO) response.getBody().getResponse();
    assertEquals(creditFacilityTemplate.getCreditFacilityName(), returnedDTO.getCreditFacilityName());
    assertEquals(creditFacilityTemplate.getMinFacilityAmount(), returnedDTO.getMinFacilityAmount());
  }

  @Test
  void testGetCreditFacilityTemplateByID_VerifiesRepositoryCalledOnce() {
    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));

    creditFacilityService.getCreditFacilityTemplateByID(1);

    verify(creditFacilityTemplateRepository, times(1)).findById(1);
  }

  @Test
  void testGetCreditFacilityTemplateByID_DifferentIdReturnsCorrespondingRecord() {
    CreditFacilityTemplate anotherTemplate = new CreditFacilityTemplate();
    anotherTemplate.setCreditFacilityTemplateID(7);
    anotherTemplate.setCreditFacilityName("Another Master Facility");
    anotherTemplate.setCftVitalInfos(new HashSet<>());
    anotherTemplate.setCftInterestRates(new HashSet<>());
    anotherTemplate.setCftSupportingDocs(new HashSet<>());
    anotherTemplate.setCftOtherFacilityInformations(new HashSet<>());
    anotherTemplate.setCftCustomFacilityInfos(new HashSet<>());

    when(creditFacilityTemplateRepository.findById(7)).thenReturn(Optional.of(anotherTemplate));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.getCreditFacilityTemplateByID(7);

    CreditFacilityTemplateDTO returnedDTO =
            (CreditFacilityTemplateDTO) response.getBody().getResponse();
    assertEquals("Another Master Facility", returnedDTO.getCreditFacilityName());
  }

  /** saveCtfInterestRateTemp * */
  @Test
  void testSaveCtfInterestRateTemp_Success() {
    List<CftInterestRateDTO> interestRateDTOList = new ArrayList<>();
    CftInterestRateDTO rateDTO = new CftInterestRateDTO();
    rateDTO.setRateName("Test Rate");
    rateDTO.setRateCode("TR001");
    interestRateDTOList.add(rateDTO);

    creditFacilityTemplateDTO.setCftInterestRateDTOList(interestRateDTOList);
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.Y);

    when(cftInterestRateTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(
            anyInt()))
            .thenReturn(new ArrayList<>());
    when(cftInterestRateTempRepository.getNextSequenceValue()).thenReturn(1);

    Set<CftInterestRateTemp> result =
            creditFacilityService.saveCtfInterestRateTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testSaveCtfInterestRateTemp_NoChange() {
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);

    Set<CftInterestRateTemp> result =
            creditFacilityService.saveCtfInterestRateTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  /** saveCftVitalInfoTemp * */
  @Test
  void testSaveCftVitalInfoTemp_Success() {
    List<CftVitalInfoDTO> vitalInfoDTOList = new ArrayList<>();
    CftVitalInfoDTO vitalInfoDTO = new CftVitalInfoDTO();
    vitalInfoDTO.setVitalInfoName("Vital Info");
    vitalInfoDTOList.add(vitalInfoDTO);

    creditFacilityTemplateDTO.setCftVitalInfoDTOList(vitalInfoDTOList);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.Y);

    when(cftVitalInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(
            anyInt()))
            .thenReturn(new ArrayList<>());
    when(cftVitalInfoTempRepository.getNextSequenceValue()).thenReturn(1);

    Set<CftVitalInfoTemp> result =
            creditFacilityService.saveCftVitalInfoTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testSaveCftVitalInfoTemp_NoChange() {
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);

    Set<CftVitalInfoTemp> result =
            creditFacilityService.saveCftVitalInfoTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  /** saveCftCustomFacilityInfoTemp * */
  @Test
  void testSaveCftCustomFacilityInfoTemp_Success() {
    List<CftCustomFacilityInfoDTO> customFacilityInfoDTOList = new ArrayList<>();
    CftCustomFacilityInfoDTO customFacilityInfoDTO = new CftCustomFacilityInfoDTO();
    customFacilityInfoDTO.setCustomFacilityInfoName("Custom Facility Info");
    customFacilityInfoDTOList.add(customFacilityInfoDTO);

    creditFacilityTemplateDTO.setCftCustomFacilityInfoDTOList(customFacilityInfoDTOList);
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.Y);

    when(cftCustomFacilityInfoTempRepository
            .findAllByCreditFacilityTemplateCreditFacilityTemplateID(anyInt()))
            .thenReturn(new ArrayList<>());
    when(cftCustomFacilityInfoTempRepository.getNextSequenceValue()).thenReturn(1);

    Set<CftCustomFacilityInfoTemp> result =
            creditFacilityService.saveCftCustomFacilityInfoTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testSaveCftCustomFacilityInfoTemp_NoChange() {
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);

    Set<CftCustomFacilityInfoTemp> result =
            creditFacilityService.saveCftCustomFacilityInfoTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  /** saveCftSupportingDocTemp * */
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
    when(cftSupportingDocTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(
            anyInt()))
            .thenReturn(new ArrayList<>());
    when(cftSupportingDocTempRepository.getNextSequenceValue()).thenReturn(1);

    Set<CftSupportingDocTemp> result =
            creditFacilityService.saveCftSupportingDocTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testSaveCftSupportingDocTemp_NoChange() {
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);

    Set<CftSupportingDocTemp> result =
            creditFacilityService.saveCftSupportingDocTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  /** saveCftOtherFacilityInfoTemp * */
  @Test
  void testSaveCftOtherFacilityInfoTemp_Success() {
    List<CftOtherFacilityInfoDTO> facilityInfoDTOList = new ArrayList<>();
    CftOtherFacilityInfoDTO facilityInfoDTO = new CftOtherFacilityInfoDTO();
    facilityInfoDTO.setOtherFacilityInfoName("Other Facility Info");
    facilityInfoDTOList.add(facilityInfoDTO);

    creditFacilityTemplateDTO.setCftOtherFacilityInfoDTOList(facilityInfoDTOList);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.Y);

    when(otherFacilityInfoTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(
            anyInt()))
            .thenReturn(new ArrayList<>());
    when(otherFacilityInfoTempRepository.getNextSequenceValue()).thenReturn(1);

    Set<CftOtherFacilityInformationTemp> result =
            creditFacilityService.saveCftOtherFacilityInfoTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testSaveCftOtherFacilityInfoTemp_NoChange() {
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);

    Set<CftOtherFacilityInformationTemp> result =
            creditFacilityService.saveCftOtherFacilityInfoTemp(
                    creditFacilityTemplateDTO, creditFacilityTemplateTemp);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  /** createCreditFacilityTemplateTempObj * */
  @Test
  void testCreateCreditFacilityTemplateTempObj_New() {
    creditFacilityTemplateDTO.setCreditFacilityTypeID(1);
    CreditFacilityType creditFacilityType = new CreditFacilityType();
    when(creditFacilityTypeRepository.findById(1)).thenReturn(Optional.of(creditFacilityType));

    CreditFacilityTemplateTemp result =
            creditFacilityService.createCreditFacilityTemplateTempObj(creditFacilityTemplateDTO, true);

    assertNotNull(result);
    assertEquals(creditFacilityTemplateDTO.getCreditFacilityName(), result.getCreditFacilityName());
    assertEquals(creditFacilityType, result.getCreditFacilityType());
  }

  @Test
  void testCreateCreditFacilityTemplateTempObj_Existing() {
    creditFacilityTemplateDTO.setCreditFacilityTypeID(null);

    CreditFacilityTemplateTemp result =
            creditFacilityService.createCreditFacilityTemplateTempObj(creditFacilityTemplateDTO, false);

    assertNotNull(result);
    assertEquals(creditFacilityTemplateDTO.getCreditFacilityName(), result.getCreditFacilityName());
  }

  /** saveCreditFacilityTemplateTemp * */
  @Test
  void testSaveCreditFacilityTemplateTemp_FacilityAlreadyExistsInMaster() {
    List<CreditFacilityTemplate> masterList = new ArrayList<>();
    masterList.add(new CreditFacilityTemplate());
    when(creditFacilityTemplateTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(new ArrayList<>());
    when(creditFacilityTemplateRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(masterList);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      creditFacilityService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);
                    });

    assertEquals(
            "Credit Facility Template Name Already Exists in Master Table", exception.getMessage());
  }

  @Test
  void testSaveCreditFacilityTemplateTemp_FacilityAlreadyExistsInTemp() {
    List<CreditFacilityTemplateTemp> tempList = new ArrayList<>();
    tempList.add(creditFacilityTemplateTemp);
    when(creditFacilityTemplateTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(tempList);
    when(creditFacilityTemplateRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(new ArrayList<>());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> {
                      creditFacilityService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);
                    });

    assertEquals(
            "Credit Facility Template Name Already Exists in Temp Table", exception.getMessage());
  }

  @Test
  void testSaveCreditFacilityTemplateTemp_Success_CreatesNewTemplate() {
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);

    when(creditFacilityTemplateTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(new ArrayList<>());
    when(creditFacilityTemplateRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(new ArrayList<>());
    when(creditFacilityTemplateTempRepository.getNextSequenceValue()).thenReturn(5);
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());
    CreditFacilityTemplateDTO returnedDTO =
            (CreditFacilityTemplateDTO) response.getBody().getResponse();
    assertEquals(creditFacilityTemplateDTO.getCreditFacilityName(), returnedDTO.getCreditFacilityName());
    verify(creditFacilityTemplateTempRepository, times(1))
            .saveAndFlush(any(CreditFacilityTemplateTemp.class));
  }

  @Test
  void testSaveCreditFacilityTemplateTemp_CapturesSavedEntityFields() {
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);

    when(creditFacilityTemplateTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(new ArrayList<>());
    when(creditFacilityTemplateRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(new ArrayList<>());
    when(creditFacilityTemplateTempRepository.getNextSequenceValue()).thenReturn(9);
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    creditFacilityService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO);

    ArgumentCaptor<CreditFacilityTemplateTemp> captor =
            ArgumentCaptor.forClass(CreditFacilityTemplateTemp.class);
    verify(creditFacilityTemplateTempRepository).saveAndFlush(captor.capture());

    CreditFacilityTemplateTemp captured = captor.getValue();
    assertEquals(9, captured.getCreditFacilityTemplateID());
    assertEquals(creditFacilityTemplateDTO.getCreditFacilityName(), captured.getCreditFacilityName());
    assertEquals(creditFacilityTemplateDTO.getDescription(), captured.getDescription());
  }

  @Test
  void testSaveCreditFacilityTemplateTemp_BothListsNonEmpty_PrefersTempExistsException() {
    List<CreditFacilityTemplateTemp> tempList = new ArrayList<>();
    tempList.add(creditFacilityTemplateTemp);
    List<CreditFacilityTemplate> masterList = new ArrayList<>();
    masterList.add(creditFacilityTemplate);

    when(creditFacilityTemplateTempRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(tempList);
    when(creditFacilityTemplateRepository.findAll(any(BooleanBuilder.class)))
            .thenReturn(masterList);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityService.saveCreditFacilityTemplateTemp(creditFacilityTemplateDTO));

    assertEquals(
            "Credit Facility Template Name Already Exists in Temp Table", exception.getMessage());
    verify(creditFacilityTemplateTempRepository, never())
            .saveAndFlush(any(CreditFacilityTemplateTemp.class));
  }

  /** updateCreditFacilityTemplateTemp * */
  @Test
  void testUpdateCreditFacilityTemplateTemp_Success() {

    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.Y);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTemplateTempRepository.saveAndFlush(any()))
            .thenReturn(creditFacilityTemplateTemp);

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.updateCreditFacilityTemplateTemp(1, creditFacilityTemplateDTO);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
  }

  @Test
  void testUpdateCreditFacilityTemplateTemp_RecordNotFound() {
    when(creditFacilityTemplateTempRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityService.updateCreditFacilityTemplateTemp(
                                    1, creditFacilityTemplateDTO));

    assertEquals("Not Found", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTemplateTemp_NameConflictInTemp_ThrowsException() {
    creditFacilityTemplateDTO.setCreditFacilityName("Different Facility Name");

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTemplateTempRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityService.updateCreditFacilityTemplateTemp(
                                    1, creditFacilityTemplateDTO));

    assertEquals(
            "Credit Facility Template Name Already Exists in Temporary Records",
            exception.getMessage());
    verify(creditFacilityTemplateTempRepository, never()).saveAndFlush(any());
  }

  @Test
  void testUpdateCreditFacilityTemplateTemp_NameConflictInMaster_ThrowsException() {
    creditFacilityTemplateDTO.setCreditFacilityName("Different Facility Name");

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTemplateTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
    when(creditFacilityTemplateRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityService.updateCreditFacilityTemplateTemp(
                                    1, creditFacilityTemplateDTO));

    assertEquals(
            "Credit Facility Template Name Already Exists in Master Records", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTemplateTemp_CreditFacilityTypeNotFound_ThrowsException() {
    creditFacilityTemplateDTO.setCreditFacilityTypeID(42);

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTypeRepository.findById(42)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityService.updateCreditFacilityTemplateTemp(
                                    1, creditFacilityTemplateDTO));

    assertEquals("Credit Facility Type with ID 42 does not exist.", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTemplateTemp_Success_WithCreditFacilityTypeUpdate_CapturesSavedEntity() {
    creditFacilityTemplateDTO.setCreditFacilityTypeID(3);
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);

    CreditFacilityType creditFacilityType = new CreditFacilityType();
    creditFacilityType.setCreditFacilityTypeID(3);

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTypeRepository.findById(3)).thenReturn(Optional.of(creditFacilityType));
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.updateCreditFacilityTemplateTemp(1, creditFacilityTemplateDTO);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());

    ArgumentCaptor<CreditFacilityTemplateTemp> captor =
            ArgumentCaptor.forClass(CreditFacilityTemplateTemp.class);
    verify(creditFacilityTemplateTempRepository).saveAndFlush(captor.capture());
    assertEquals(creditFacilityType, captor.getValue().getCreditFacilityType());
  }

  /** insertToCftVitalInfoAuditTable * */
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
    Exception exception =
            assertThrows(
                    NullPointerException.class,
                    () -> creditFacilityService.insertToCftVitalInfoAuditTable(null, null));

    assertEquals(
            "Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftVitalInfo.getCftVitalInfoID()\" because \"cftVitalInfo\" is null",
            exception.getMessage());
  }

  /** insertToCftInterestRateAuditTable * */
  @Test
  void testInsertToCftInterestRateAuditTable_WithTempData() {
    when(cftInterestRateAudRepo.saveAndFlush(any())).thenReturn(new CftInterestRateAud());

    Boolean result =
            creditFacilityService.insertToCftInterestRateAuditTable(null, cftInterestRateTemp);

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
    Exception exception =
            assertThrows(
                    NullPointerException.class,
                    () -> creditFacilityService.insertToCftInterestRateAuditTable(null, null));

    assertEquals(
            "Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftInterestRate.getCftInterestRateID()\" because \"cftInterestRate\" is null",
            exception.getMessage());
  }

  /** insertToCftSupportingDocAuditTable * */
  @Test
  void testInsertToCftSupportingDocAuditTable_WithTempData() {
    when(cftSupportingDocAudRepo.saveAndFlush(any())).thenReturn(new CftSupportingDocAud());

    Boolean result =
            creditFacilityService.insertToCftSupportingDocAuditTable(null, cftSupportingDocTemp);

    assertTrue(result);
    verify(cftSupportingDocAudRepo, times(1)).saveAndFlush(any());
  }

  @Test
  void testInsertToCftSupportingDocAuditTable_WithPermanentData() {
    when(cftSupportingDocAudRepo.saveAndFlush(any())).thenReturn(new CftSupportingDocAud());

    Boolean result =
            creditFacilityService.insertToCftSupportingDocAuditTable(cftSupportingDoc, null);

    assertTrue(result);
    verify(cftSupportingDocAudRepo, times(1)).saveAndFlush(any());
  }

  @Test
  void testInsertToCftSupportingDocAuditTable_WithBothNull_ShouldThrowException() {
    Exception exception =
            assertThrows(
                    NullPointerException.class,
                    () -> creditFacilityService.insertToCftSupportingDocAuditTable(null, null));

    assertEquals(
            "Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftSupportingDoc.getCftSupportingDocID()\" because \"cftSupportingDoc\" is null",
            exception.getMessage());
  }

  /** insertToCftOtherFacilityInfoAuditTable * */
  @Test
  void testInsertToCftOtherFacilityInfoAuditTable_WithTempData() {
    when(cftOtherFacilityInfoAudRepo.saveAndFlush(any()))
            .thenReturn(new CftOtherFacilityInformationAud());

    Boolean result =
            creditFacilityService.insertToCftOtherFacilityInfoAuditTable(
                    null, cftOtherFacilityInformationTemp);

    assertTrue(result);
    verify(cftOtherFacilityInfoAudRepo, times(1)).saveAndFlush(any());
  }

  @Test
  void testInsertToCftOtherFacilityInfoAuditTable_WithPermanentData() {
    when(cftOtherFacilityInfoAudRepo.saveAndFlush(any()))
            .thenReturn(new CftOtherFacilityInformationAud());

    Boolean result =
            creditFacilityService.insertToCftOtherFacilityInfoAuditTable(
                    cftOtherFacilityInformation, null);

    assertTrue(result);
    verify(cftOtherFacilityInfoAudRepo, times(1)).saveAndFlush(any());
  }

  @Test
  void testInsertToCftOtherFacilityInfoAuditTable_WithBothNull_ShouldThrowException() {
    Exception exception =
            assertThrows(
                    NullPointerException.class,
                    () -> creditFacilityService.insertToCftOtherFacilityInfoAuditTable(null, null));

    assertEquals(
            "Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformation.getCftOtherFacilityInfoID()\" because \"cftOtherFacilityInformation\" is null",
            exception.getMessage());
  }

  /** insertToCftCustomFacilityInfoAuditTable * */
  @Test
  void testInsertToCftCustomFacilityInfoAuditTable_WithTempData() {
    when(cftCustomFacilityInfoAudRepo.saveAndFlush(any()))
            .thenReturn(new CftCustomFacilityInfoAud());

    Boolean result =
            creditFacilityService.insertToCftCustomFacilityInfoAuditTable(
                    null, cftCustomFacilityInfoTemp);

    assertTrue(result);
    verify(cftCustomFacilityInfoAudRepo, times(1)).saveAndFlush(any());
  }

  @Test
  void testInsertToCftCustomFacilityInfoAuditTable_WithPermanentData() {
    when(cftCustomFacilityInfoAudRepo.saveAndFlush(any()))
            .thenReturn(new CftCustomFacilityInfoAud());

    Boolean result =
            creditFacilityService.insertToCftCustomFacilityInfoAuditTable(cftCustomFacilityInfo, null);

    assertTrue(result);
    verify(cftCustomFacilityInfoAudRepo, times(1)).saveAndFlush(any());
  }

  @Test
  void testInsertToCftCustomFacilityInfoAuditTable_WithBothNull_ShouldThrowException() {
    Exception exception =
            assertThrows(
                    NullPointerException.class,
                    () -> creditFacilityService.insertToCftCustomFacilityInfoAuditTable(null, null));

    assertEquals(
            "Cannot invoke \"lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftCustomFacilityInfo.getCftCustomFacilityInfoID()\" because \"cftCustomFacilityInfo\" is null",
            exception.getMessage());
  }

  /** authorizeCreditFacilityTemplate * */
  @Test
  void testAuthorizeCreditFacilityTemplate_NullDataID_ThrowsException() {
    ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityService.authorizeCreditFacilityTemplate(approveRejectRQ));

    assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
  }

  @Test
  void testAuthorizeCreditFacilityTemplate_RecordNotFound_ThrowsException() {
    ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    when(creditFacilityTemplateTempRepository.findById(1)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityService.authorizeCreditFacilityTemplate(approveRejectRQ));

    assertEquals("Not Found", exception.getMessage());
  }

  @Test
  void testAuthorizeCreditFacilityTemplate_Approval_CreatesNewMasterRecord_Success() {
    creditFacilityTemplateTemp.setCreditFacilityType(new CreditFacilityType());

    ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenReturn(creditFacilityTemplateTemp);

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.authorizeCreditFacilityTemplate(approveRejectRQ);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());
    verify(creditFacilityTemplateRepository, times(1)).save(any(CreditFacilityTemplate.class));
    verify(creditFacilityTemplateTempRepository, times(1)).delete(creditFacilityTemplateTemp);
    verify(creditFacilityTemplateAudRepo, times(1))
            .saveAndFlush(any(CreditFacilityTemplateAud.class));
  }

  @Test
  void testAuthorizeCreditFacilityTemplate_Approval_UpdatesExistingMasterRecord_Success() {
    creditFacilityTemplateTemp.setCreditFacilityType(new CreditFacilityType());

    ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenReturn(creditFacilityTemplateTemp);

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.authorizeCreditFacilityTemplate(approveRejectRQ);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    verify(creditFacilityTemplateRepository, times(1)).save(creditFacilityTemplate);
    verify(cftVitalInfoRepository, times(1))
            .findAllByCreditFacilityTemplateCreditFacilityTemplateID(1);
    verify(creditFacilityTemplateTempRepository, times(1)).delete(creditFacilityTemplateTemp);
  }

  @Test
  void testAuthorizeCreditFacilityTemplate_Rejection_Success() {
    creditFacilityTemplateTemp.setCreditFacilityType(new CreditFacilityType());

    ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenReturn(creditFacilityTemplateTemp);

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.authorizeCreditFacilityTemplate(approveRejectRQ);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    assertTrue(response.getBody().getSuccess());
    verify(creditFacilityTemplateAudRepo, times(1))
            .saveAndFlush(any(CreditFacilityTemplateAud.class));
    verify(creditFacilityTemplateTempRepository, never()).delete(any());
  }

  @Test
  void testAuthorizeCreditFacilityTemplate_UnknownStatus_ThrowsException() {
    ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
    approveRejectRQ.setApproveRejectDataID(1);
    approveRejectRQ.setApproveStatus(MasterDataApproveStatus.PENDING);

    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));
    when(creditFacilityTemplateRepository.findById(1)).thenReturn(Optional.empty());
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenReturn(creditFacilityTemplateTemp);

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityService.authorizeCreditFacilityTemplate(approveRejectRQ));

    assertTrue(exception.getMessage().contains("Unknown approval status"));
  }

  /** updateCreditFacilityTemplate * */
  @Test
  void testUpdateCreditFacilityTemplate_RecordNotFound_ThrowsException() {
    when(creditFacilityTemplateRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityService.updateCreditFacilityTemplate(2, creditFacilityTemplateDTO));

    assertEquals("ROLE_WITH_ID2Does Not Exists", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTemplate_Success_SetsTemplateIdFromMaster() {
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setCreditFacilityTypeID(null);

    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.updateCreditFacilityTemplate(1, creditFacilityTemplateDTO);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());

    ArgumentCaptor<CreditFacilityTemplateTemp> captor =
            ArgumentCaptor.forClass(CreditFacilityTemplateTemp.class);
    verify(creditFacilityTemplateTempRepository).saveAndFlush(captor.capture());
    assertEquals(1, captor.getValue().getCreditFacilityTemplateID());
    assertEquals(creditFacilityTemplateDTO.getApproveStatus(), captor.getValue().getApproveStatus());
  }

  @Test
  void testUpdateCreditFacilityTemplate_CreditFacilityTypeNotFound_ThrowsException() {
    creditFacilityTemplateDTO.setCreditFacilityTypeID(99);

    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));
    when(creditFacilityTypeRepository.findById(99)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () ->
                            creditFacilityService.updateCreditFacilityTemplate(1, creditFacilityTemplateDTO));

    assertEquals("Credit Facility Type Id with 99 does not exist", exception.getMessage());
  }

  @Test
  void testUpdateCreditFacilityTemplate_WithInterestRateChange_Success() {
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.Y);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setCreditFacilityTypeID(null);

    CftInterestRateDTO rateDTO = new CftInterestRateDTO();
    rateDTO.setRateName("New Rate");
    rateDTO.setRateCode("NR001");
    List<CftInterestRateDTO> rateDTOList = new ArrayList<>();
    rateDTOList.add(rateDTO);
    creditFacilityTemplateDTO.setCftInterestRateDTOList(rateDTOList);

    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));
    when(cftInterestRateTempRepository.findAllByCreditFacilityTemplateCreditFacilityTemplateID(
            anyInt()))
            .thenReturn(new ArrayList<>());
    when(cftInterestRateTempRepository.getNextSequenceValue()).thenReturn(11);
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    creditFacilityService.updateCreditFacilityTemplate(1, creditFacilityTemplateDTO);

    ArgumentCaptor<CreditFacilityTemplateTemp> captor =
            ArgumentCaptor.forClass(CreditFacilityTemplateTemp.class);
    verify(creditFacilityTemplateTempRepository).saveAndFlush(captor.capture());
    assertEquals(1, captor.getValue().getCftInterestRates().size());
  }

  @Test
  void testUpdateCreditFacilityTemplate_MapsDtoFieldsOntoTempEntity() {
    creditFacilityTemplateDTO.setIsCftInterestRateDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftVitalInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftCustomFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftSupportingDocDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setIsCftOtherFacilityInfoDTOListChange(AppsConstants.YesNo.N);
    creditFacilityTemplateDTO.setCreditFacilityTypeID(null);
    creditFacilityTemplateDTO.setDescription("Updated Description");
    creditFacilityTemplateDTO.setMaxFacilityAmount(BigDecimal.valueOf(999999));

    when(creditFacilityTemplateRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplate));
    when(creditFacilityTemplateTempRepository.saveAndFlush(any(CreditFacilityTemplateTemp.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ResponseEntity<StandardResponse<CreditFacilityTemplateDTO>> response =
            creditFacilityService.updateCreditFacilityTemplate(1, creditFacilityTemplateDTO);

    CreditFacilityTemplateDTO returnedDTO =
            (CreditFacilityTemplateDTO) response.getBody().getResponse();
    assertEquals("Updated Description", returnedDTO.getDescription());
    assertEquals(BigDecimal.valueOf(999999), returnedDTO.getMaxFacilityAmount());
  }

  /** deleteCreditFacilityTemplateTemp * */
  @Test
  void testDeleteCreditFacilityTemplateTemp_Success() {
    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));

    ResponseEntity<StandardResponse<Void>> response =
            creditFacilityService.deleteCreditFacilityTemplateTemp(1);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getSuccess());
    assertEquals(1, response.getBody().getResponse());
    verify(creditFacilityTemplateTempRepository, times(1)).delete(creditFacilityTemplateTemp);
  }

  @Test
  void testDeleteCreditFacilityTemplateTemp_RecordNotFound_ThrowsException() {
    when(creditFacilityTemplateTempRepository.findById(2)).thenReturn(Optional.empty());

    ApiRequestException exception =
            assertThrows(
                    ApiRequestException.class,
                    () -> creditFacilityService.deleteCreditFacilityTemplateTemp(2));

    assertEquals("Not Found", exception.getMessage());
    verify(creditFacilityTemplateTempRepository, never()).delete(any());
  }

  @Test
  void testDeleteCreditFacilityTemplateTemp_VerifiesCorrectEntityDeleted() {
    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));

    creditFacilityService.deleteCreditFacilityTemplateTemp(1);

    ArgumentCaptor<CreditFacilityTemplateTemp> captor =
            ArgumentCaptor.forClass(CreditFacilityTemplateTemp.class);
    verify(creditFacilityTemplateTempRepository).delete(captor.capture());
    assertEquals(1, captor.getValue().getCreditFacilityTemplateID());
  }

  @Test
  void testDeleteCreditFacilityTemplateTemp_DifferentId_Success() {
    CreditFacilityTemplateTemp anotherTemp = new CreditFacilityTemplateTemp();
    anotherTemp.setCreditFacilityTemplateID(100);

    when(creditFacilityTemplateTempRepository.findById(100)).thenReturn(Optional.of(anotherTemp));

    ResponseEntity<StandardResponse<Void>> response =
            creditFacilityService.deleteCreditFacilityTemplateTemp(100);

    assertEquals(100, response.getBody().getResponse());
    verify(creditFacilityTemplateTempRepository, times(1)).delete(anotherTemp);
  }

  @Test
  void testDeleteCreditFacilityTemplateTemp_VerifiesFindByIdCalledOnce() {
    when(creditFacilityTemplateTempRepository.findById(1))
            .thenReturn(Optional.of(creditFacilityTemplateTemp));

    creditFacilityService.deleteCreditFacilityTemplateTemp(1);

    verify(creditFacilityTemplateTempRepository, times(1)).findById(1);
  }
}