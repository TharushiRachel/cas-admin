package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.dadesignation.DAColumnValueRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationCodeDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationListDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationSaveRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DATableApprovalResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeaderDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeadingResponse;
import lk.sampath.casadminportalms.entity.daDesignation.DADesignationData;
import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import lk.sampath.casadminportalms.entity.daDesignation.DALimitAud;
import lk.sampath.casadminportalms.entity.daDesignation.DALimitTemp;
import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.daDesignation.DADesignationRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitAudRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitHeadingRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitTempRepository;
import lk.sampath.casadminportalms.service.impl.DaDesignationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

class DaDesignationServiceImplTest {

  @Mock private DALimitHeadingRepository daLimitHeadingRepository;

  @Mock private DALimitTempRepository daLimitTempRepository;

  @Mock private DADesignationRepository daDesignationMasterRepository;

  @Mock private DALimitRepository daLimitRepository;

  @Mock private DALimitAudRepository daLimitAudRepository;

  @InjectMocks private DaDesignationServiceImpl daDesignationService;

  private DADesignationData designation;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    designation = new DADesignationData();
    designation.setId(1);
    designation.setDesignation("Managing Director");
    designation.setDesignationCode("MD");
    designation.setDescription("Managing Director Desc");
    designation.setStatus(Status.ACT);
    designation.setApproveStatus(MasterDataApproveStatus.APPROVED);
    designation.setDisplayOrder(1);
    designation.setIsCommittee("Y");
  }

  // ---------------------------------------------------------------------
  // helpers
  // ---------------------------------------------------------------------

  private DATableHeader header(
      Long id, Integer parentId, String tableType, String label, Integer levelNo, Integer displayOrder, Integer subId) {
    DATableHeader h = new DATableHeader();
    h.setId(id);
    h.setParentId(parentId);
    h.setTableType(tableType);
    h.setLabel(label);
    h.setLevelNo(levelNo);
    h.setDisplayOrder(displayOrder);
    h.setSubId(subId);
    return h;
  }

  private DALimit limit(Integer designationId, String isCommittee, Integer columnId, Double riskValue) {
    DALimit l = new DALimit();
    l.setDaLimitsId(columnId);
    l.setDesignationId(designationId);
    l.setIsCommittee(isCommittee);
    l.setColumnId(columnId);
    l.setRiskValue(riskValue);
    l.setStatus(AppsConstants.Status.ACT);
    return l;
  }

  private DALimitTemp tempLimit(DADesignationData owner, String isCommittee, Integer columnId, Double riskValue) {
    DALimitTemp t = new DALimitTemp();
    t.setDaLimitsId(columnId + 1000);
    t.setDesignation(owner);
    t.setIsCommittee(isCommittee);
    t.setColumnId(columnId);
    t.setRiskValue(riskValue);
    t.setStatus(AppsConstants.Status.ACT);
    t.setApproveStatus(MasterDataApproveStatus.PENDING);
    return t;
  }

  private DADesignationSaveRequest saveRequest(
      Integer designationId, String designationCode, String designationName, Integer displayOrder,
      List<DAColumnValueRequest> columnValues) {
    DADesignationSaveRequest r = new DADesignationSaveRequest();
    r.setDesignationId(designationId);
    r.setDesignationCode(designationCode);
    r.setDesignation(designationName);
    r.setDisplayOrder(displayOrder);
    r.setColumnValues(columnValues);
    return r;
  }

  private DAColumnValueRequest columnValue(Integer subId, Double riskValue) {
    DAColumnValueRequest c = new DAColumnValueRequest();
    c.setSubId(subId);
    c.setRiskValue(riskValue);
    return c;
  }

  private ApproveRejectRQ approveRejectRequest(Integer designationId, MasterDataApproveStatus status) {
    ApproveRejectRQ rq = new ApproveRejectRQ();
    rq.setApproveRejectDataID(designationId);
    rq.setApproveStatus(status);
    return rq;
  }

  private HttpServer startHttpServer(int statusCode, String body) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
    server.createContext(
        "/",
        exchange -> {
          byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
          exchange.getResponseHeaders().add("Content-Type", "application/json");
          exchange.sendResponseHeaders(statusCode, bytes.length);
          exchange.getResponseBody().write(bytes);
          exchange.close();
        });
    server.start();
    return server;
  }

  // ---------------------------------------------------------------------
  // getAllLimitHeadings()
  // ---------------------------------------------------------------------

  @Test
  void getAllLimitHeadings_splitsRootHeadersIntoCommitteeAndIndividual() throws ApiRequestException {
    DATableHeader committeeRoot = header(1L, null, "COMMITTEE", "Committee Table", 1, 1, null);
    DATableHeader individualRoot = header(2L, null, "INDIVIDUAL", "Individual Table", 1, 2, null);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc())
        .thenReturn(List.of(committeeRoot, individualRoot));

    ResponseEntity<StandardResponse<DATableHeaderDTO>> response = daDesignationService.getAllLimitHeadings();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    DATableHeaderDTO body = (DATableHeaderDTO) response.getBody().getResponse();
    assertEquals(1, body.getCommitteeTableHeaders().size());
    assertEquals("Committee Table", body.getCommitteeTableHeaders().get(0).getLabel());
    assertEquals(1, body.getIndividualTableHeaders().size());
    assertEquals("Individual Table", body.getIndividualTableHeaders().get(0).getLabel());
  }

  @Test
  void getAllLimitHeadings_mapsNestedChildHeadersWithSubId() throws ApiRequestException {
    DATableHeader root = header(1L, null, "COMMITTEE", "Committee Table", 1, 1, null);
    DATableHeader child = header(2L, 1, "COMMITTEE", "Column 1", 2, 1, 10);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(root, child));

    ResponseEntity<StandardResponse<DATableHeaderDTO>> response = daDesignationService.getAllLimitHeadings();

    DATableHeaderDTO body = (DATableHeaderDTO) response.getBody().getResponse();
    List<DATableHeadingResponse> subHeadings = body.getCommitteeTableHeaders().get(0).getSubHeadings();
    assertEquals(1, subHeadings.size());
    assertEquals("Column 1", subHeadings.get(0).getLabel());
    assertEquals(10, subHeadings.get(0).getSubId());
  }

  @Test
  void getAllLimitHeadings_sortsChildrenByDisplayOrderAscending() throws ApiRequestException {
    DATableHeader root = header(1L, null, "COMMITTEE", "Committee Table", 1, 1, null);
    DATableHeader childB = header(2L, 1, "COMMITTEE", "Second", 2, 2, 20);
    DATableHeader childA = header(3L, 1, "COMMITTEE", "First", 2, 1, 10);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(root, childB, childA));

    ResponseEntity<StandardResponse<DATableHeaderDTO>> response = daDesignationService.getAllLimitHeadings();

    List<DATableHeadingResponse> subHeadings =
        ((DATableHeaderDTO) response.getBody().getResponse()).getCommitteeTableHeaders().get(0).getSubHeadings();
    assertEquals("First", subHeadings.get(0).getLabel());
    assertEquals("Second", subHeadings.get(1).getLabel());
  }

  @Test
  void getAllLimitHeadings_emptyHeaderList_returnsEmptyHeaderLists() throws ApiRequestException {
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableHeaderDTO>> response = daDesignationService.getAllLimitHeadings();

    DATableHeaderDTO body = (DATableHeaderDTO) response.getBody().getResponse();
    assertTrue(body.getCommitteeTableHeaders().isEmpty());
    assertTrue(body.getIndividualTableHeaders().isEmpty());
  }

  @Test
  void getAllLimitHeadings_unknownTableType_excludedFromBothLists() throws ApiRequestException {
    DATableHeader unknown = header(1L, null, "UNKNOWN_TYPE", "Mystery", 1, 1, null);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(unknown));

    ResponseEntity<StandardResponse<DATableHeaderDTO>> response = daDesignationService.getAllLimitHeadings();

    DATableHeaderDTO body = (DATableHeaderDTO) response.getBody().getResponse();
    assertTrue(body.getCommitteeTableHeaders().isEmpty());
    assertTrue(body.getIndividualTableHeaders().isEmpty());
  }

  @Test
  void getAllLimitHeadings_orphanChildWithMissingParent_becomesRoot() throws ApiRequestException {
    DATableHeader orphan = header(1L, 999, "INDIVIDUAL", "Orphan Root", 1, 1, null);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(orphan));

    ResponseEntity<StandardResponse<DATableHeaderDTO>> response = daDesignationService.getAllLimitHeadings();

    DATableHeaderDTO body = (DATableHeaderDTO) response.getBody().getResponse();
    assertEquals(1, body.getIndividualTableHeaders().size());
    assertEquals("Orphan Root", body.getIndividualTableHeaders().get(0).getLabel());
  }

  @Test
  void getAllLimitHeadings_returnsSuccessStandardResponseMetadata() throws ApiRequestException {
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableHeaderDTO>> response = daDesignationService.getAllLimitHeadings();

    assertTrue(response.getBody().getSuccess());
    assertEquals("Success", response.getBody().getMessage());
    verify(daLimitHeadingRepository, times(1)).findAllOrderByDisplayOrderAsc();
  }

  // ---------------------------------------------------------------------
  // getAllDesignationCodeDetails()
  // ---------------------------------------------------------------------

  @Test
  void getAllDesignationCodeDetails_success_returnsMappedList() throws Exception {
    HttpServer server =
        startHttpServer(200, "[{\"DESIGNATION_CODE\":\"MD\",\"DESIGNATION_DESCRIPTION\":\"Managing Director\"}]");
    try {
      ReflectionTestUtils.setField(
          daDesignationService, "loadDesignations", "http://localhost:" + server.getAddress().getPort() + "/");

      ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> response =
          daDesignationService.getAllDesignationCodeDetails();

      assertEquals(HttpStatus.OK, response.getStatusCode());
      @SuppressWarnings("unchecked")
      List<DADesignationCodeDTO> body = (List<DADesignationCodeDTO>) response.getBody().getResponse();
      assertEquals(1, body.size());
      assertEquals("MD", body.get(0).getCode());
      assertEquals("Managing Director", body.get(0).getDescription());
    } finally {
      server.stop(0);
    }
  }

  @Test
  void getAllDesignationCodeDetails_multipleEntries_returnsAllMapped() throws Exception {
    HttpServer server =
        startHttpServer(
            200,
            "[{\"DESIGNATION_CODE\":\"MD\",\"DESIGNATION_DESCRIPTION\":\"Managing Director\"},"
                + "{\"DESIGNATION_CODE\":\"CEO\",\"DESIGNATION_DESCRIPTION\":\"Chief Executive Officer\"}]");
    try {
      ReflectionTestUtils.setField(
          daDesignationService, "loadDesignations", "http://localhost:" + server.getAddress().getPort() + "/");

      ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> response =
          daDesignationService.getAllDesignationCodeDetails();

      @SuppressWarnings("unchecked")
      List<DADesignationCodeDTO> body = (List<DADesignationCodeDTO>) response.getBody().getResponse();
      assertEquals(2, body.size());
    } finally {
      server.stop(0);
    }
  }

  @Test
  void getAllDesignationCodeDetails_emptyArray_returnsEmptyList() throws Exception {
    HttpServer server = startHttpServer(200, "[]");
    try {
      ReflectionTestUtils.setField(
          daDesignationService, "loadDesignations", "http://localhost:" + server.getAddress().getPort() + "/");

      ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> response =
          daDesignationService.getAllDesignationCodeDetails();

      @SuppressWarnings("unchecked")
      List<DADesignationCodeDTO> body = (List<DADesignationCodeDTO>) response.getBody().getResponse();
      assertTrue(body.isEmpty());
    } finally {
      server.stop(0);
    }
  }

  @Test
  void getAllDesignationCodeDetails_malformedJson_throwsRuntimeException() throws Exception {
    HttpServer server = startHttpServer(200, "{not-valid-json");
    try {
      ReflectionTestUtils.setField(
          daDesignationService, "loadDesignations", "http://localhost:" + server.getAddress().getPort() + "/");

      assertThrows(RuntimeException.class, () -> daDesignationService.getAllDesignationCodeDetails());
    } finally {
      server.stop(0);
    }
  }

  @Test
  void getAllDesignationCodeDetails_missingFields_mappedAsNull() throws Exception {
    HttpServer server = startHttpServer(200, "[{\"DESIGNATION_CODE\":\"MD\"}]");
    try {
      ReflectionTestUtils.setField(
          daDesignationService, "loadDesignations", "http://localhost:" + server.getAddress().getPort() + "/");

      ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> response =
          daDesignationService.getAllDesignationCodeDetails();

      @SuppressWarnings("unchecked")
      List<DADesignationCodeDTO> body = (List<DADesignationCodeDTO>) response.getBody().getResponse();
      assertEquals("MD", body.get(0).getCode());
      assertNull(body.get(0).getDescription());
    } finally {
      server.stop(0);
    }
  }

  @Test
  void getAllDesignationCodeDetails_returnsSuccessStandardResponseMetadata() throws Exception {
    HttpServer server = startHttpServer(200, "[]");
    try {
      ReflectionTestUtils.setField(
          daDesignationService, "loadDesignations", "http://localhost:" + server.getAddress().getPort() + "/");

      ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> response =
          daDesignationService.getAllDesignationCodeDetails();

      assertTrue(response.getBody().getSuccess());
      assertEquals("Success", response.getBody().getMessage());
    } finally {
      server.stop(0);
    }
  }

  // ---------------------------------------------------------------------
  // saveDaDesignationLimits(DADesignationBulkSaveRequest)
  // ---------------------------------------------------------------------

  @Test
  void saveDaDesignationLimits_nullRequest_throwsApiRequestException() {
    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(null));
    assertEquals("Request body cannot be null", ex.getMessage());
  }

  @Test
  void saveDaDesignationLimits_emptyCommitteeAndIndividual_throwsApiRequestException() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(request));
    assertEquals("Provide at least one committee or individual designation row", ex.getMessage());
  }

  @Test
  void saveDaDesignationLimits_newCommitteeRow_createsDesignationAndTempLimits() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request.getCommittee().add(saveRequest(null, "MD", "Managing Director", null, List.of(columnValue(1, 100.0))));

    DATableHeader column = header(10L, null, "COMMITTEE", "Limit", 1, 1, 1);
    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("COMMITTEE")).thenReturn(List.of(column));
    when(daDesignationMasterRepository.findByDesignationCodeAndStatus("MD", Status.ACT.name()))
        .thenReturn(Optional.empty());
    when(daDesignationMasterRepository.findMaxDisplayOrderByStatus(Status.ACT.name())).thenReturn(null);
    when(daDesignationMasterRepository.save(any(DADesignationData.class)))
        .thenAnswer(
            invocation -> {
              DADesignationData d = invocation.getArgument(0);
              d.setId(100);
              return d;
            });
    when(daLimitTempRepository.getNextSequenceValues(1)).thenReturn(List.of(500));
    when(daLimitTempRepository.findAllByDesignationIdAndIsCommitteeAndStatus(
            eq(100), eq("Y"), eq(AppsConstants.Status.ACT.name())))
        .thenReturn(List.of(tempLimit(designation, "Y", 1, 100.0)));

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> response =
        daDesignationService.saveDaDesignationLimits(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    DADesignationBulkSaveResponse body = (DADesignationBulkSaveResponse) response.getBody().getResponse();
    assertEquals(1, body.getCommittee().size());
    assertEquals("Y", body.getCommittee().get(0).getIsCommittee());
    assertEquals(1, body.getCommittee().get(0).getDisplayOrder());
    verify(daLimitTempRepository).deleteByDesignationIdAndIsCommittee(100, "Y");
    verify(daLimitTempRepository).saveAll(anyList());
  }

  @Test
  void saveDaDesignationLimits_newIndividualRow_createsDesignationAndTempLimits() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request
        .getIndividual()
        .add(saveRequest(null, "CFO", "Chief Financial Officer", null, List.of(columnValue(2, 50.0))));

    DATableHeader column = header(11L, null, "INDIVIDUAL", "Limit", 1, 1, 2);
    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("INDIVIDUAL")).thenReturn(List.of(column));
    when(daDesignationMasterRepository.findByDesignationCodeAndStatus("CFO", Status.ACT.name()))
        .thenReturn(Optional.empty());
    when(daDesignationMasterRepository.findMaxDisplayOrderByStatus(Status.ACT.name())).thenReturn(5);
    when(daDesignationMasterRepository.save(any(DADesignationData.class)))
        .thenAnswer(
            invocation -> {
              DADesignationData d = invocation.getArgument(0);
              d.setId(200);
              return d;
            });
    when(daLimitTempRepository.getNextSequenceValues(1)).thenReturn(List.of(600));
    when(daLimitTempRepository.findAllByDesignationIdAndIsCommitteeAndStatus(
            eq(200), eq("N"), eq(AppsConstants.Status.ACT.name())))
        .thenReturn(List.of(tempLimit(designation, "N", 2, 50.0)));

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> response =
        daDesignationService.saveDaDesignationLimits(request);

    DADesignationBulkSaveResponse body = (DADesignationBulkSaveResponse) response.getBody().getResponse();
    assertEquals(1, body.getIndividual().size());
    assertEquals("N", body.getIndividual().get(0).getIsCommittee());
    assertEquals(6, body.getIndividual().get(0).getDisplayOrder());
  }

  @Test
  void saveDaDesignationLimits_missingDesignationIdentifiers_throwsApiRequestException() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request.getCommittee().add(saveRequest(null, null, null, null, List.of(columnValue(1, 10.0))));

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(request));
    assertTrue(ex.getMessage().contains("designationId, designation, or designationCode is required"));
  }

  @Test
  void saveDaDesignationLimits_emptyColumnValues_throwsApiRequestException() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request.getCommittee().add(saveRequest(null, "MD", "Managing Director", null, new ArrayList<>()));

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(request));
    assertTrue(ex.getMessage().contains("columnValues cannot be empty"));
  }

  @Test
  void saveDaDesignationLimits_invalidSubId_throwsApiRequestException() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request.getCommittee().add(saveRequest(null, "MD", "Managing Director", null, List.of(columnValue(999, 10.0))));

    DATableHeader column = header(10L, null, "COMMITTEE", "Limit", 1, 1, 1);
    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("COMMITTEE")).thenReturn(List.of(column));

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(request));
    assertTrue(ex.getMessage().contains("Invalid subId"));
  }

  @Test
  void saveDaDesignationLimits_duplicateSubId_throwsApiRequestException() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request
        .getCommittee()
        .add(saveRequest(null, "MD", "Managing Director", null, List.of(columnValue(1, 10.0), columnValue(1, 20.0))));

    DATableHeader column = header(10L, null, "COMMITTEE", "Limit", 1, 1, 1);
    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("COMMITTEE")).thenReturn(List.of(column));

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(request));
    assertTrue(ex.getMessage().contains("Duplicate subId"));
  }

  @Test
  void saveDaDesignationLimits_noValidColumnsForTableType_throwsApiRequestException() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request.getCommittee().add(saveRequest(null, "MD", "Managing Director", null, List.of(columnValue(1, 10.0))));

    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("COMMITTEE")).thenReturn(Collections.emptyList());

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(request));
    assertTrue(ex.getMessage().contains("No value columns found"));
  }

  @Test
  void saveDaDesignationLimits_existingDesignationById_updatesInPlace() {
    designation.setDisplayOrder(3);
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request.getCommittee().add(saveRequest(1, null, null, null, List.of(columnValue(1, 15.0))));

    DATableHeader column = header(10L, null, "COMMITTEE", "Limit", 1, 1, 1);
    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("COMMITTEE")).thenReturn(List.of(column));
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daDesignationMasterRepository.findMaxDisplayOrderByStatus(Status.ACT.name())).thenReturn(3);
    when(daDesignationMasterRepository.save(any(DADesignationData.class))).thenAnswer(inv -> inv.getArgument(0));
    when(daLimitTempRepository.getNextSequenceValues(1)).thenReturn(List.of(700));
    when(daLimitTempRepository.findAllByDesignationIdAndIsCommitteeAndStatus(
            eq(1), eq("Y"), eq(AppsConstants.Status.ACT.name())))
        .thenReturn(List.of(tempLimit(designation, "Y", 1, 15.0)));

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> response =
        daDesignationService.saveDaDesignationLimits(request);

    DADesignationBulkSaveResponse body = (DADesignationBulkSaveResponse) response.getBody().getResponse();
    assertEquals(1, body.getCommittee().get(0).getDesignationId());
    assertEquals(3, body.getCommittee().get(0).getDisplayOrder());
    verify(daDesignationMasterRepository, times(1)).findMaxDisplayOrderByStatus(Status.ACT.name());
  }

  @Test
  void saveDaDesignationLimits_multipleNewRowsWithoutDisplayOrder_queriesMaxDisplayOrderOnlyOnce() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request.getCommittee().add(saveRequest(null, "MD", "Managing Director", null, List.of(columnValue(1, 10.0))));
    request.getCommittee().add(saveRequest(null, "CEO", "Chief Executive", null, List.of(columnValue(1, 20.0))));

    DATableHeader column = header(10L, null, "COMMITTEE", "Limit", 1, 1, 1);
    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("COMMITTEE")).thenReturn(List.of(column));
    when(daDesignationMasterRepository.findByDesignationCodeAndStatus(anyString(), eq(Status.ACT.name())))
        .thenReturn(Optional.empty());
    when(daDesignationMasterRepository.findMaxDisplayOrderByStatus(Status.ACT.name())).thenReturn(10);
    AtomicInteger idSeq = new AtomicInteger(300);
    when(daDesignationMasterRepository.save(any(DADesignationData.class)))
        .thenAnswer(
            inv -> {
              DADesignationData d = inv.getArgument(0);
              d.setId(idSeq.getAndIncrement());
              return d;
            });
    when(daLimitTempRepository.getNextSequenceValues(1)).thenReturn(List.of(1));
    when(daLimitTempRepository.findAllByDesignationIdAndIsCommitteeAndStatus(any(), any(), any()))
        .thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> response =
        daDesignationService.saveDaDesignationLimits(request);

    DADesignationBulkSaveResponse body = (DADesignationBulkSaveResponse) response.getBody().getResponse();
    assertEquals(11, body.getCommittee().get(0).getDisplayOrder());
    assertEquals(12, body.getCommittee().get(1).getDisplayOrder());
    verify(daDesignationMasterRepository, times(1)).findMaxDisplayOrderByStatus(Status.ACT.name());
  }

  @Test
  void saveDaDesignationLimits_insufficientSequenceValues_throwsApiRequestException() {
    DADesignationBulkSaveRequest request = new DADesignationBulkSaveRequest();
    request
        .getCommittee()
        .add(
            saveRequest(
                null, "MD", "Managing Director", null, List.of(columnValue(1, 10.0), columnValue(2, 20.0))));

    DATableHeader column1 = header(10L, null, "COMMITTEE", "Limit1", 1, 1, 1);
    DATableHeader column2 = header(11L, null, "COMMITTEE", "Limit2", 1, 2, 2);
    when(daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull("COMMITTEE"))
        .thenReturn(List.of(column1, column2));
    when(daDesignationMasterRepository.findByDesignationCodeAndStatus("MD", Status.ACT.name()))
        .thenReturn(Optional.empty());
    when(daDesignationMasterRepository.findMaxDisplayOrderByStatus(Status.ACT.name())).thenReturn(0);
    when(daDesignationMasterRepository.save(any(DADesignationData.class)))
        .thenAnswer(
            inv -> {
              DADesignationData d = inv.getArgument(0);
              d.setId(400);
              return d;
            });
    when(daLimitTempRepository.getNextSequenceValues(2)).thenReturn(List.of(800));

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.saveDaDesignationLimits(request));
    assertTrue(ex.getMessage().contains("Unable to generate DA_LIMITS_TEMP ids"));
  }

  // ---------------------------------------------------------------------
  // approveRejectDaDesignationLimits(ApproveRejectRQ)
  // ---------------------------------------------------------------------

  @Test
  void approveRejectDaDesignationLimits_nullRequest_throwsApiRequestException() {
    ApiRequestException ex =
        assertThrows(
            ApiRequestException.class, () -> daDesignationService.approveRejectDaDesignationLimits(null));
    assertEquals("approveRejectDataID (designationId) cannot be null", ex.getMessage());
  }

  @Test
  void approveRejectDaDesignationLimits_missingApproveStatus_throwsApiRequestException() {
    ApproveRejectRQ rq = approveRejectRequest(1, null);

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.approveRejectDaDesignationLimits(rq));
    assertEquals("approveStatus is required (APPROVED or REJECTED)", ex.getMessage());
  }

  @Test
  void approveRejectDaDesignationLimits_designationNotFound_throwsApiRequestException() {
    ApproveRejectRQ rq = approveRejectRequest(99, MasterDataApproveStatus.APPROVED);
    when(daDesignationMasterRepository.findById(99)).thenReturn(Optional.empty());

    assertThrows(ApiRequestException.class, () -> daDesignationService.approveRejectDaDesignationLimits(rq));
  }

  @Test
  void approveRejectDaDesignationLimits_noTempLimits_throwsApiRequestException() {
    ApproveRejectRQ rq = approveRejectRequest(1, MasterDataApproveStatus.APPROVED);
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(Collections.emptyList());

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.approveRejectDaDesignationLimits(rq));
    assertTrue(ex.getMessage().contains("No DA_LIMITS_TEMP records found"));
  }

  @Test
  void approveRejectDaDesignationLimits_approve_createsMasterLimitAndAudit() {
    ApproveRejectRQ rq = approveRejectRequest(1, MasterDataApproveStatus.APPROVED);
    DALimitTemp temp = tempLimit(designation, "Y", 1, 100.0);
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(List.of(temp));
    when(daLimitRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(Collections.emptyList())
        .thenReturn(List.of(limit(1, "Y", 1, 100.0)));
    when(daLimitAudRepository.getCurrentSequenceValue()).thenReturn(1);
    when(daLimitRepository.save(any(DALimit.class))).thenAnswer(inv -> inv.getArgument(0));
    when(daDesignationMasterRepository.saveAndFlush(any(DADesignationData.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> response =
        daDesignationService.approveRejectDaDesignationLimits(rq);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(daLimitRepository).save(any(DALimit.class));
    verify(daLimitAudRepository).save(any(DALimitAud.class));
    verify(daLimitTempRepository).deleteByDesignationId(1);
    assertEquals(MasterDataApproveStatus.APPROVED, designation.getApproveStatus());
  }

  @Test
  void approveRejectDaDesignationLimits_approve_updatesExistingMasterLimitWhenKeyMatches() {
    ApproveRejectRQ rq = approveRejectRequest(1, MasterDataApproveStatus.APPROVED);
    DALimitTemp temp = tempLimit(designation, "Y", 1, 150.0);
    DALimit existingMaster = limit(1, "Y", 1, 90.0);
    existingMaster.setDaLimitsId(555);

    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(List.of(temp));
    when(daLimitRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(existingMaster));
    when(daLimitAudRepository.getCurrentSequenceValue()).thenReturn(1);
    when(daLimitRepository.save(any(DALimit.class))).thenAnswer(inv -> inv.getArgument(0));
    when(daDesignationMasterRepository.saveAndFlush(any(DADesignationData.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    daDesignationService.approveRejectDaDesignationLimits(rq);

    ArgumentCaptor<DALimit> captor = ArgumentCaptor.forClass(DALimit.class);
    verify(daLimitRepository).save(captor.capture());
    assertEquals(555, captor.getValue().getDaLimitsId());
    assertEquals(150.0, captor.getValue().getRiskValue());
  }

  @Test
  void approveRejectDaDesignationLimits_reject_marksTempsRejectedAndUpdatesDesignation() {
    ApproveRejectRQ rq = approveRejectRequest(1, MasterDataApproveStatus.REJECTED);
    DALimitTemp temp = tempLimit(designation, "Y", 1, 100.0);
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(List.of(temp));
    when(daDesignationMasterRepository.saveAndFlush(any(DADesignationData.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    daDesignationService.approveRejectDaDesignationLimits(rq);

    verify(daLimitTempRepository).save(temp);
    assertEquals(MasterDataApproveStatus.REJECTED, temp.getApproveStatus());
    assertEquals(MasterDataApproveStatus.REJECTED, designation.getApproveStatus());
    verify(daLimitTempRepository, never()).deleteByDesignationId(anyInt());
  }

  @Test
  void approveRejectDaDesignationLimits_unknownApproveStatus_throwsApiRequestException() {
    ApproveRejectRQ rq = approveRejectRequest(1, MasterDataApproveStatus.DRAFT);
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(List.of(tempLimit(designation, "Y", 1, 10.0)));

    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.approveRejectDaDesignationLimits(rq));
    assertTrue(ex.getMessage().contains("Unknown approval status"));
  }

  // ---------------------------------------------------------------------
  // getDaTableById(Integer)
  // ---------------------------------------------------------------------

  @Test
  void getDaTableById_nullDesignationId_throwsApiRequestException() {
    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.getDaTableById(null));
    assertEquals("designationId cannot be null", ex.getMessage());
  }

  @Test
  void getDaTableById_designationNotFound_throwsApiRequestException() {
    when(daDesignationMasterRepository.findById(99)).thenReturn(Optional.empty());

    assertThrows(ApiRequestException.class, () -> daDesignationService.getDaTableById(99));
  }

  @Test
  void getDaTableById_approvedValuesOnly_populatesApprovedRowValuesAndLeavesPendingEmpty() {
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(limit(1, "Y", 1, 100.0)));
    when(daLimitTempRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(Collections.emptyList());
    DATableHeader root = header(1L, null, "COMMITTEE", "Committee", 1, 1, null);
    DATableHeader child = header(2L, 1, "COMMITTEE", "Limit1", 2, 1, 1);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(root, child));

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTableById(1);

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertEquals(1, body.getApproved().getCommitteeRows().size());
    assertEquals(100.0, body.getApproved().getCommitteeRows().get(0).getTableValues().get(1));
    assertEquals(1, body.getPending().getCommitteeRows().size());
    assertTrue(body.getPending().getCommitteeRows().get(0).getTableValues().isEmpty());
  }

  @Test
  void getDaTableById_pendingValuesOnly_populatesPendingRowValuesAndLeavesApprovedEmpty() {
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(Collections.emptyList());
    when(daLimitTempRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(tempLimit(designation, "Y", 1, 55.0)));
    DATableHeader root = header(1L, null, "COMMITTEE", "Committee", 1, 1, null);
    DATableHeader child = header(2L, 1, "COMMITTEE", "Limit1", 2, 1, 1);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(root, child));

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTableById(1);

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertTrue(body.getApproved().getCommitteeRows().get(0).getTableValues().isEmpty());
    assertEquals(55.0, body.getPending().getCommitteeRows().get(0).getTableValues().get(1));
  }

  @Test
  void getDaTableById_legacyNullIsCommitteeInApprovedLimit_treatedAsIndividual() {
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    DALimit legacyLimit = limit(1, null, 1, 42.0);
    when(daLimitRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(legacyLimit));
    when(daLimitTempRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(Collections.emptyList());
    DATableHeader root = header(1L, null, "INDIVIDUAL", "Individual", 1, 1, null);
    DATableHeader child = header(2L, 1, "INDIVIDUAL", "Limit1", 2, 1, 1);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(root, child));

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTableById(1);

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertEquals(42.0, body.getApproved().getIndividualRows().get(0).getTableValues().get(1));
    assertTrue(body.getApproved().getCommitteeRows().get(0).getTableValues().isEmpty());
  }

  @Test
  void getDaTableById_populatesBothCommitteeAndIndividualDesignationListsForSingleDesignation() {
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(limit(1, "Y", 1, 10.0)));
    when(daLimitTempRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(Collections.emptyList());
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTableById(1);

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertEquals(1, body.getCommitteeDesignationList().size());
    assertEquals(1, body.getIndividualDesignationList().size());
    assertEquals(designation.getId(), body.getCommitteeDesignationList().get(0).getDesignationId());
  }

  @Test
  void getDaTableById_noLimitsAtAll_stillReturnsRowWithEmptyValues() {
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daLimitRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(Collections.emptyList());
    when(daLimitTempRepository.findAllByDesignationIdAndStatus(1, AppsConstants.Status.ACT.name()))
        .thenReturn(Collections.emptyList());
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTableById(1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertEquals(1, body.getApproved().getCommitteeRows().size());
    assertTrue(body.getApproved().getCommitteeRows().get(0).getTableValues().isEmpty());
  }

  // ---------------------------------------------------------------------
  // getDaTable()
  // ---------------------------------------------------------------------

  @Test
  void getDaTable_noDesignations_returnsEmptyListsWithoutException() {
    when(daDesignationMasterRepository.findAllByStatus(Status.ACT.name())).thenReturn(Collections.emptyList());
    when(daLimitRepository.findAllByStatus(AppsConstants.Status.ACT.name())).thenReturn(Collections.emptyList());
    when(daLimitTempRepository.findAllByStatus(AppsConstants.Status.ACT.name())).thenReturn(Collections.emptyList());
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTable();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertTrue(body.getApproved().getCommitteeRows().isEmpty());
    assertTrue(body.getCommitteeDesignationList().isEmpty());
  }

  @Test
  void getDaTable_approvedLimitsPopulateApprovedSectionOnly() {
    when(daDesignationMasterRepository.findAllByStatus(Status.ACT.name())).thenReturn(List.of(designation));
    when(daLimitRepository.findAllByStatus(AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(limit(1, "Y", 1, 77.0)));
    when(daLimitTempRepository.findAllByStatus(AppsConstants.Status.ACT.name())).thenReturn(Collections.emptyList());
    DATableHeader root = header(1L, null, "COMMITTEE", "Committee", 1, 1, null);
    DATableHeader child = header(2L, 1, "COMMITTEE", "Limit1", 2, 1, 1);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(root, child));

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTable();

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertEquals(1, body.getApproved().getCommitteeRows().size());
    assertEquals(77.0, body.getApproved().getCommitteeRows().get(0).getTableValues().get(1));
    assertTrue(body.getPending().getCommitteeRows().isEmpty());
  }

  @Test
  void getDaTable_pendingLimitsPopulatePendingSectionOnly() {
    when(daDesignationMasterRepository.findAllByStatus(Status.ACT.name())).thenReturn(List.of(designation));
    when(daLimitRepository.findAllByStatus(AppsConstants.Status.ACT.name())).thenReturn(Collections.emptyList());
    when(daLimitTempRepository.findAllByStatus(AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(tempLimit(designation, "N", 2, 33.0)));
    DATableHeader root = header(1L, null, "INDIVIDUAL", "Individual", 1, 1, null);
    DATableHeader child = header(2L, 1, "INDIVIDUAL", "Limit1", 2, 1, 2);
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(List.of(root, child));

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTable();

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertTrue(body.getApproved().getIndividualRows().isEmpty());
    assertEquals(1, body.getPending().getIndividualRows().size());
    assertEquals(33.0, body.getPending().getIndividualRows().get(0).getTableValues().get(2));
  }

  @Test
  void getDaTable_splitsDesignationsIntoCommitteeAndIndividualLists() {
    DADesignationData designation2 = new DADesignationData();
    designation2.setId(2);
    designation2.setDesignation("Chief Risk Officer");
    designation2.setDesignationCode("CRO");
    designation2.setStatus(Status.ACT);
    designation2.setDisplayOrder(2);

    when(daDesignationMasterRepository.findAllByStatus(Status.ACT.name()))
        .thenReturn(List.of(designation, designation2));
    when(daLimitRepository.findAllByStatus(AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(limit(1, "Y", 1, 10.0)));
    when(daLimitTempRepository.findAllByStatus(AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(tempLimit(designation2, "N", 2, 20.0)));
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTable();

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertEquals(1, body.getCommitteeDesignationList().size());
    assertEquals(1, body.getCommitteeDesignationList().get(0).getDesignationId());
    assertEquals(1, body.getIndividualDesignationList().size());
    assertEquals(2, body.getIndividualDesignationList().get(0).getDesignationId());
  }

  @Test
  void getDaTable_designationWithNoLimits_excludedFromRows() {
    DADesignationData designationWithoutLimits = new DADesignationData();
    designationWithoutLimits.setId(3);
    designationWithoutLimits.setDesignation("Vacant Role");
    designationWithoutLimits.setStatus(Status.ACT);

    when(daDesignationMasterRepository.findAllByStatus(Status.ACT.name()))
        .thenReturn(List.of(designationWithoutLimits));
    when(daLimitRepository.findAllByStatus(AppsConstants.Status.ACT.name())).thenReturn(Collections.emptyList());
    when(daLimitTempRepository.findAllByStatus(AppsConstants.Status.ACT.name())).thenReturn(Collections.emptyList());
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTable();

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertTrue(body.getApproved().getCommitteeRows().isEmpty());
    assertTrue(body.getApproved().getIndividualRows().isEmpty());
    assertTrue(body.getCommitteeDesignationList().isEmpty());
    assertTrue(body.getIndividualDesignationList().isEmpty());
  }

  @Test
  void getDaTable_legacyNullIsCommitteeInDbLimit_normalizedToIndividualRow() {
    when(daDesignationMasterRepository.findAllByStatus(Status.ACT.name())).thenReturn(List.of(designation));
    when(daLimitRepository.findAllByStatus(AppsConstants.Status.ACT.name()))
        .thenReturn(List.of(limit(1, null, 1, 88.0)));
    when(daLimitTempRepository.findAllByStatus(AppsConstants.Status.ACT.name())).thenReturn(Collections.emptyList());
    when(daLimitHeadingRepository.findAllOrderByDisplayOrderAsc()).thenReturn(Collections.emptyList());

    ResponseEntity<StandardResponse<DATableApprovalResponse>> response = daDesignationService.getDaTable();

    DATableApprovalResponse body = (DATableApprovalResponse) response.getBody().getResponse();
    assertTrue(body.getApproved().getCommitteeRows().isEmpty());
    assertEquals(1, body.getApproved().getIndividualRows().size());
    assertEquals(88.0, body.getApproved().getIndividualRows().get(0).getTableValues().get(1));
  }

  // ---------------------------------------------------------------------
  // deleteDaDesignation(Integer)
  // ---------------------------------------------------------------------

  @Test
  void deleteDaDesignation_nullDesignationId_throwsApiRequestException() {
    ApiRequestException ex =
        assertThrows(ApiRequestException.class, () -> daDesignationService.deleteDaDesignation(null));
    assertEquals("designationId cannot be null", ex.getMessage());
  }

  @Test
  void deleteDaDesignation_designationNotFound_throwsApiRequestException() {
    when(daLimitTempRepository.findAllByDesignationId(99)).thenReturn(Collections.emptyList());
    when(daDesignationMasterRepository.findById(99)).thenReturn(Optional.empty());

    assertThrows(ApiRequestException.class, () -> daDesignationService.deleteDaDesignation(99));
  }

  @Test
  void deleteDaDesignation_marksTempLimitsInactiveAndSavesThem() {
    DALimitTemp temp1 = tempLimit(designation, "Y", 1, 10.0);
    DALimitTemp temp2 = tempLimit(designation, "N", 2, 20.0);
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(List.of(temp1, temp2));
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daDesignationMasterRepository.save(any(DADesignationData.class))).thenAnswer(inv -> inv.getArgument(0));

    daDesignationService.deleteDaDesignation(1);

    assertEquals(AppsConstants.Status.INA, temp1.getStatus());
    assertEquals(AppsConstants.Status.INA, temp2.getStatus());
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<DALimitTemp>> captor = ArgumentCaptor.forClass(List.class);
    verify(daLimitTempRepository).saveAll(captor.capture());
    assertEquals(2, captor.getValue().size());
  }

  @Test
  void deleteDaDesignation_marksDesignationStatusInactive() {
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(Collections.emptyList());
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daDesignationMasterRepository.save(any(DADesignationData.class))).thenAnswer(inv -> inv.getArgument(0));

    daDesignationService.deleteDaDesignation(1);

    assertEquals(Status.INA, designation.getStatus());
    verify(daDesignationMasterRepository).save(designation);
  }

  @Test
  void deleteDaDesignation_noTempRecordsExist_stillDeletesDesignationSuccessfully() {
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(Collections.emptyList());
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daDesignationMasterRepository.save(any(DADesignationData.class))).thenAnswer(inv -> inv.getArgument(0));

    ResponseEntity<StandardResponse<DADesignationListDTO>> response = daDesignationService.deleteDaDesignation(1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(daLimitTempRepository).saveAll(anyList());
  }

  @Test
  void deleteDaDesignation_responseContainsUpdatedDesignationListDTO() {
    when(daLimitTempRepository.findAllByDesignationId(1)).thenReturn(Collections.emptyList());
    when(daDesignationMasterRepository.findById(1)).thenReturn(Optional.of(designation));
    when(daDesignationMasterRepository.save(any(DADesignationData.class))).thenAnswer(inv -> inv.getArgument(0));

    ResponseEntity<StandardResponse<DADesignationListDTO>> response = daDesignationService.deleteDaDesignation(1);

    DADesignationListDTO body = (DADesignationListDTO) response.getBody().getResponse();
    assertEquals(1, body.getDesignationId());
    assertEquals(Status.INA, body.getStatus());
    assertTrue(response.getBody().getSuccess());
  }
}
