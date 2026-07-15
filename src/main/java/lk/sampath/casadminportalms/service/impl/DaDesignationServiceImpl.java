package lk.sampath.casadminportalms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.dadesignation.*;
import lk.sampath.casadminportalms.dto.userSession.UserContext;
import lk.sampath.casadminportalms.entity.daDesignation.*;
import lk.sampath.casadminportalms.enums.*;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.daDesignation.*;
import lk.sampath.casadminportalms.service.DaDesignationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DaDesignationServiceImpl implements DaDesignationService {

    @Value("${load.designations}")
    private String loadDesignations;

    private final DALimitHeadingRepository daLimitHeadingRepository;

    private final DALimitTempRepository daLimitTempRepository;

    private final DADesignationRepository daDesignationMasterRepository;

    private final DALimitRepository daLimitRepository;

    private final DALimitAudRepository daLimitAudRepository;

    public DaDesignationServiceImpl(DALimitHeadingRepository daLimitHeadingRepository, DALimitTempRepository daLimitTempRepository, DADesignationRepository daDesignationMasterRepository, DALimitRepository daLimitRepository, DALimitAudRepository daLimitAudRepository) {
        this.daLimitHeadingRepository = daLimitHeadingRepository;
        this.daLimitTempRepository = daLimitTempRepository;
        this.daDesignationMasterRepository = daDesignationMasterRepository;
        this.daLimitRepository = daLimitRepository;
        this.daLimitAudRepository = daLimitAudRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<DATableHeaderDTO>> getAllLimitHeadings() throws ApiRequestException {

        log.info("START : DaDesignationServiceImpl |getAllLimitHeadings ");

        DATableHeaderDTO rootHeaders = buildHeaderResponse();
        StandardResponse<DATableHeaderDTO> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), rootHeaders);
        log.info("END : getAllLimitHeadings | roots={}", rootHeaders.getCommitteeTableHeaders().size() + rootHeaders.getIndividualTableHeaders().size());
        return ResponseEntity.ok(response);
    }

    private DATableHeaderDTO buildHeaderResponse() {

        List<DAHeaderResponse> rootHeaders = buildHeaderTree();

        List<DATableHeadingResponse> individual = rootHeaders.stream()
                .filter(h -> DaTableType.INDIVIDUAL.equals(h.getTableType()))
                .map(this::mapToJsonStructure)
                .toList();

        List<DATableHeadingResponse> committee = rootHeaders.stream()
                .filter(h -> DaTableType.COMMITTEE.equals(h.getTableType()))
                .map(this::mapToJsonStructure)
                .toList();

        DATableHeaderDTO dto = new DATableHeaderDTO();
        dto.setIndividualTableHeaders(individual);
        dto.setCommitteeTableHeaders(committee);

        return dto;
    }

    private DATableHeadingResponse mapToJsonStructure(DAHeaderResponse source) {

        DATableHeadingResponse target = new DATableHeadingResponse();

        target.setLabel(source.getLabel());
        target.setColSpan(source.getColSpan());
        target.setRowSpan(source.getRowSpan());
        target.setSubId(source.getSubId());

        if (!CollectionUtils.isEmpty(source.getChildren())) {

            List<DATableHeadingResponse> children =
                    source.getChildren()
                            .stream()
                            .sorted(Comparator.comparing(
                                    DAHeaderResponse::getDisplayOrder,
                                    Comparator.nullsLast(Integer::compareTo)))
                            .map(this::mapToJsonStructure)
                            .toList();

            target.setSubHeadings(children);
        }

        return target;
    }

    private List<DAHeaderResponse> buildHeaderTree() {
        List<DATableHeader> headers = daLimitHeadingRepository.findAllOrderByDisplayOrderAsc();
        log.info("Fetched {} headers from the database", headers.size());

        Map<Long, DAHeaderResponse> headerMap = new LinkedHashMap<>();
        List<DAHeaderResponse> rootHeaders = new ArrayList<>();

        for (DATableHeader header : headers) {
            DAHeaderResponse response = new DAHeaderResponse();
            response.setId(header.getId());
            response.setTableType(resolveTableType(header.getTableType()));
            response.setLabel(header.getLabel());
            response.setLevelNo(header.getLevelNo());
            response.setDisplayOrder(header.getDisplayOrder());
            response.setColSpan(header.getColSpan());
            response.setRowSpan(header.getRowSpan());
            response.setSubId(header.getSubId());
            response.setChildren(new ArrayList<>());
            headerMap.put(header.getId(), response);
        }

        for (DATableHeader header : headers) {
            DAHeaderResponse current = headerMap.get(header.getId());
            if (header.getParentId() == null) {
                rootHeaders.add(current);
            } else {
                DAHeaderResponse parent = headerMap.get(header.getParentId().longValue());
                if (parent != null) {
                    parent.getChildren().add(current);
                } else {
                    rootHeaders.add(current);
                }
            }
        }

        sortHeaderTree(rootHeaders);
        return rootHeaders;
    }

    private void sortHeaderTree(List<DAHeaderResponse> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        nodes.sort(Comparator.comparing(DAHeaderResponse::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)));
        for (DAHeaderResponse node : nodes) {
            sortHeaderTree(node.getChildren());
        }
    }

    private DaTableType resolveTableType(String rawType) {
        DaTableType fromValue = DaTableType.getEnum(rawType);
        if (fromValue != null) {
            return fromValue;
        }

        try {
            return DaTableType.resolveStatus(rawType);
        } catch (IllegalArgumentException ex) {
            log.warn("Unknown DA table type value: {}", rawType);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<DADesignationCodeDTO>>> getAllDesignationCodeDetails() throws ApiRequestException{
        log.info("START : DaDesignationServiceImpl |getAllDesignationCodeDetails ");

        String url = this.loadDesignations;
        WebClient webClient = WebClient.create();

        try{
            String response = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
            log.info("END : DaDesignationServiceImpl |getAllDesignationCodeDetails | response={}", response);
            List<DADesignationCodeDTO> daDesignationCodeDTOList = Arrays.asList(new ObjectMapper().readValue(response, DADesignationCodeDTO[].class));
            StandardResponse<List<DADesignationCodeDTO>> standardResponse = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), daDesignationCodeDTOList);
            return ResponseEntity.ok(standardResponse);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> saveDaDesignationLimits(
            DADesignationBulkSaveRequest request) throws ApiRequestException {
        log.info("START : saveDaDesignationLimits | request={}", request);

        if (request == null) {
            throw new ApiRequestException("Request body cannot be null");
        }
        if (CollectionUtils.isEmpty(request.getCommittee()) && CollectionUtils.isEmpty(request.getIndividual())) {
            throw new ApiRequestException("Provide at least one committee or individual designation row");
        }

        DADesignationBulkSaveResponse bulkResponse = new DADesignationBulkSaveResponse();
        // Same designationCode (e.g. MD) in committee + individual reuses one DA_DESIGNATION row.
        Map<String, DADesignationData> designationByCode = new HashMap<>();
        // Computed once (single scalar query) instead of re-scanning the whole table for every new row.
        AtomicInteger nextDisplayOrder = new AtomicInteger(
                Optional.ofNullable(daDesignationMasterRepository.findMaxDisplayOrderByStatus(Status.ACT.name()))
                        .orElse(0) + 1);

        if (!CollectionUtils.isEmpty(request.getCommittee())) {
            for (DADesignationSaveRequest row : request.getCommittee()) {
                bulkResponse.getCommittee().add(
                        saveOrUpdateSingleRow(row, DaTableType.COMMITTEE, designationByCode, nextDisplayOrder));
            }
        }

        if (!CollectionUtils.isEmpty(request.getIndividual())) {
            for (DADesignationSaveRequest row : request.getIndividual()) {
                bulkResponse.getIndividual().add(
                        saveOrUpdateSingleRow(row, DaTableType.INDIVIDUAL, designationByCode, nextDisplayOrder));
            }
        }

        StandardResponse<DADesignationBulkSaveResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), bulkResponse);
        log.info("END : saveDaDesignationLimits | committee={}, individual={}, uniqueDesignations={}",
                bulkResponse.getCommittee().size(),
                bulkResponse.getIndividual().size(),
                designationByCode.size());
        return ResponseEntity.ok(response);
    }

    private DADesignationSaveResponse saveOrUpdateSingleRow(DADesignationSaveRequest request,
                                                            DaTableType tableType,
                                                            Map<String, DADesignationData> designationByCode,
                                                            AtomicInteger nextDisplayOrder) {
        validateSaveRequest(request, tableType);

        String dbTableType = tableType.name();
        String isCommittee = DaTableType.COMMITTEE.equals(tableType)
                ? AppsConstants.YesNo.Y.name()
                : AppsConstants.YesNo.N.name();

        Map<Integer, DATableHeader> columnHeadersBySubId = loadValidColumns(dbTableType);
        validateColumnValues(request.getColumnValues(), columnHeadersBySubId, tableType);

        DADesignationData designation = upsertDesignation(request, designationByCode, nextDisplayOrder);
        replaceLimitTemps(designation, isCommittee, request.getColumnValues(), columnHeadersBySubId);

        return buildSaveResponse(designation, tableType, isCommittee);
    }

    private void validateSaveRequest(DADesignationSaveRequest request, DaTableType tableType) {
        if (request == null) {
            throw new ApiRequestException("Designation row cannot be null for " + tableType.name());
        }
        if (request.getDesignationId() == null
                && !StringUtils.hasText(request.getDesignation())
                && !StringUtils.hasText(request.getDesignationCode())) {
            throw new ApiRequestException(
                    "designationId, designation, or designationCode is required for " + tableType.name());
        }
        if (CollectionUtils.isEmpty(request.getColumnValues())) {
            throw new ApiRequestException("columnValues cannot be empty for " + tableType.name());
        }
    }

    private DADesignationData upsertDesignation(DADesignationSaveRequest request,
                                                Map<String, DADesignationData> designationByCode,
                                                AtomicInteger nextDisplayOrder) {
        Date now = new Date();
        String username = UserContext.getUsername();

        DADesignationData designation = resolveExistingDesignation(request, designationByCode);
        boolean isNew = designation.getId() == null;

        if (StringUtils.hasText(request.getDesignation())) {
            designation.setDesignation(request.getDesignation().trim());
        } else if (isNew) {
            throw new ApiRequestException("designation name is required for a new row");
        }

        if (StringUtils.hasText(request.getDesignationCode())) {
            designation.setDesignationCode(request.getDesignationCode().trim());
        }
        if (request.getDescription() != null) {
            designation.setDescription(request.getDescription());
        }

        // Master is shared across committee/individual; table type lives on DA_LIMITS_TEMP.IS_COMMITTEE.
        designation.setStatus(Status.ACT);
        designation.setApproveStatus(MasterDataApproveStatus.PENDING);
        designation.setDisplayOrder(
                resolveDisplayOrder(request.getDisplayOrder(), designation.getDisplayOrder(), nextDisplayOrder));
        designation.setModifiedDate(now);
        designation.setModifiedBy(username);

        if (isNew) {
            designation.setCreatedDate(now);
            designation.setCreatedBy(username);
        }

        designation = daDesignationMasterRepository.save(designation);
        cacheDesignation(designationByCode, designation);
        return designation;
    }

    private DADesignationData resolveExistingDesignation(DADesignationSaveRequest request,
                                                         Map<String, DADesignationData> designationByCode) {
        if (request.getDesignationId() != null) {
            DADesignationData byId = daDesignationMasterRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new ApiRequestException(
                            "DA Designation Master with id " + request.getDesignationId() + " does not exist in Master table"));
            cacheDesignation(designationByCode, byId);
            return byId;
        }

        if (StringUtils.hasText(request.getDesignationCode())) {
            String codeKey = normalizeCode(request.getDesignationCode());
            DADesignationData cached = designationByCode.get(codeKey);
            if (cached != null) {
                return cached;
            }
            return daDesignationMasterRepository
                    .findByDesignationCodeAndStatus(request.getDesignationCode().trim(), Status.ACT.name())
                    .orElseGet(DADesignationData::new);
        }

        return new DADesignationData();
    }

    private void cacheDesignation(Map<String, DADesignationData> designationByCode,
                                  DADesignationData designation) {
        if (designation != null && StringUtils.hasText(designation.getDesignationCode())) {
            designationByCode.put(normalizeCode(designation.getDesignationCode()), designation);
        }
    }

    private String normalizeCode(String designationCode) {
        return designationCode.trim().toUpperCase(Locale.ROOT);
    }

    private Integer resolveDisplayOrder(Integer requestedOrder, Integer existingOrder, AtomicInteger nextDisplayOrder) {
        if (requestedOrder != null) {
            return requestedOrder;
        }
        if (existingOrder != null) {
            return existingOrder;
        }
        // nextDisplayOrder is seeded once per bulk-save call (see saveDaDesignationLimits) instead of
        // re-scanning the whole DA_DESIGNATION table for every row that needs an auto-assigned order.
        return nextDisplayOrder.getAndIncrement();
    }

    private Map<Integer, DATableHeader> loadValidColumns(String dbTableType) {
        List<DATableHeader> columns = daLimitHeadingRepository.findByTableTypeAndSubIdIsNotNull(dbTableType);
        if (CollectionUtils.isEmpty(columns)) {
            throw new ApiRequestException("No value columns found for tableType: " + dbTableType);
        }
        Map<Integer, DATableHeader> bySubId = new LinkedHashMap<>();
        for (DATableHeader column : columns) {
            bySubId.put(column.getSubId(), column);
        }
        return bySubId;
    }

    private void validateColumnValues(List<DAColumnValueRequest> columnValues,
                                      Map<Integer, DATableHeader> columnHeadersBySubId,
                                      DaTableType tableType) {
        Set<Integer> seen = new HashSet<>();
        for (DAColumnValueRequest columnValue : columnValues) {
            if (columnValue == null || columnValue.getSubId() == null) {
                throw new ApiRequestException("Each column value must include subId for " + tableType.name());
            }
            if (!columnHeadersBySubId.containsKey(columnValue.getSubId())) {
                throw new ApiRequestException(
                        "Invalid subId " + columnValue.getSubId() + " for tableType " + tableType.name());
            }
            if (!seen.add(columnValue.getSubId())) {
                throw new ApiRequestException(
                        "Duplicate subId " + columnValue.getSubId() + " for tableType " + tableType.name());
            }
        }
    }

    private void replaceLimitTemps(DADesignationData designation,
                                   String isCommittee,
                                   List<DAColumnValueRequest> columnValues,
                                   Map<Integer, DATableHeader> columnHeadersBySubId) {
        // Avoid lazy collection access; manage temps through repository only.
        daLimitTempRepository.deleteByDesignationIdAndIsCommittee(designation.getId(), isCommittee);

        if (CollectionUtils.isEmpty(columnValues)) {
            return;
        }

        String username = UserContext.getUsername();
        Date now = new Date();
        List<Integer> sequenceValues = daLimitTempRepository.getNextSequenceValues(columnValues.size());
        if (CollectionUtils.isEmpty(sequenceValues) || sequenceValues.size() < columnValues.size()) {
            throw new ApiRequestException("Unable to generate DA_LIMITS_TEMP ids for batch save");
        }

        List<DALimitTemp> tempRows = new ArrayList<>(columnValues.size());
        int rowIndex = 0;

        for (DAColumnValueRequest columnValue : columnValues) {
            DATableHeader header = columnHeadersBySubId.get(columnValue.getSubId());

            DALimitTemp limitTemp = new DALimitTemp();
            limitTemp.setDaLimitsId(sequenceValues.get(rowIndex++));
            limitTemp.setDesignation(designation);
            limitTemp.setColumnId(columnValue.getSubId());
            limitTemp.setRiskValue(columnValue.getRiskValue());
            limitTemp.setRiskRating(header.getLabel());
            limitTemp.setIsCommittee(isCommittee);
            limitTemp.setStatus(AppsConstants.Status.ACT);
            limitTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
            limitTemp.setCreatedBy(username);
            limitTemp.setCreatedDate(now);
            tempRows.add(limitTemp);
        }

        daLimitTempRepository.saveAll(tempRows);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<DADesignationBulkSaveResponse>> approveRejectDaDesignationLimits(
            ApproveRejectRQ request) throws ApiRequestException {
        log.info("START : approveRejectDaDesignationLimits | request={}", request);

        if (request == null || request.getApproveRejectDataID() == null) {
            throw new ApiRequestException("approveRejectDataID (designationId) cannot be null");
        }
        if (request.getApproveStatus() == null) {
            throw new ApiRequestException("approveStatus is required (APPROVED or REJECTED)");
        }

        Integer designationId = request.getApproveRejectDataID();
        DADesignationData designation = daDesignationMasterRepository.findById(designationId)
                .orElseThrow(() -> new ApiRequestException(
                        "DA Designation with id " + designationId + " does not exist"));

        List<DALimitTemp> tempLimits = daLimitTempRepository.findAllByDesignationId(designationId);
        if (CollectionUtils.isEmpty(tempLimits)) {
            throw new ApiRequestException("No DA_LIMITS_TEMP records found for designationId " + designationId);
        }

        Date now = new Date();
        String username = UserContext.getUsername();
        MasterDataApproveStatus approveStatus = request.getApproveStatus();

        if (MasterDataApproveStatus.APPROVED.equals(approveStatus)) {
            approveTempLimits(tempLimits, designation, now, username);
        } else if (MasterDataApproveStatus.REJECTED.equals(approveStatus)) {
            rejectTempLimits(tempLimits, designation, now, username);
        } else {
            throw new ApiRequestException("Unknown approval status: " + approveStatus);
        }

        DADesignationBulkSaveResponse bulkResponse = buildApproveRejectBulkResponse(designation, approveStatus);
        StandardResponse<DADesignationBulkSaveResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), bulkResponse);
        log.info("END : approveRejectDaDesignationLimits | designationId={}, status={}", designationId, approveStatus);
        return ResponseEntity.ok(response);
    }

    private void approveTempLimits(List<DALimitTemp> tempLimits,
                                   DADesignationData designation,
                                   Date now,
                                   String username) {
        List<DALimit> existingMasters = daLimitRepository
                .findAllByDesignationIdAndStatus(designation.getId(), AppsConstants.Status.ACT.name());
        Map<String, DALimit> existingByKey = new HashMap<>();
        for (DALimit existing : existingMasters) {
            existingByKey.put(buildLimitKey(existing.getIsCommittee(), existing.getColumnId()), existing);
        }

        for (DALimitTemp temp : tempLimits) {
            String key = buildLimitKey(temp.getIsCommittee(), temp.getColumnId());
            DALimit master = existingByKey.get(key);
            boolean isNew = (master == null);

            if (isNew) {
                master = new DALimit();
                master.setDaLimitsId(temp.getDaLimitsId());
                master.setDesignationId(designation.getId());
                master.setCreatedBy(temp.getCreatedBy());
                master.setCreatedDate(temp.getCreatedDate());
            }

            master.setColumnId(temp.getColumnId());
            master.setRiskValue(temp.getRiskValue());
            master.setRiskRating(temp.getRiskRating());
            master.setIsCommittee(temp.getIsCommittee());
            master.setStatus(temp.getStatus() != null ? temp.getStatus() : AppsConstants.Status.ACT);
            master.setAuthorizerDisplayName(temp.getAuthorizerDisplayName());
            master.setApproveStatus(MasterDataApproveStatus.APPROVED);
            master.setApprovedDate(now);
            master.setApprovedBy(username);
            master.setModifiedBy(username);
            master.setLastModifiedDate(now);
            DALimit savedMaster = daLimitRepository.save(master);
            saveDaLimitAudit(savedMaster);
        }

        daLimitTempRepository.deleteByDesignationId(designation.getId());

        designation.setApproveStatus(MasterDataApproveStatus.APPROVED);
        designation.setApprovedDate(now);
        designation.setApprovedBy(username);
        designation.setModifiedDate(now);
        designation.setModifiedBy(username);
        daDesignationMasterRepository.saveAndFlush(designation);
    }

    private String buildLimitKey(String isCommittee, Integer columnId) {
        return normalizeIsCommittee(isCommittee) + "|" + columnId;
    }

    /**
     * Legacy DA_LIMITS / DA_LIMITS_TEMP rows can have a null IS_COMMITTEE.
     * Following DA_DESIGNATION's convention (Y = committee, null = individual),
     * any value other than "Y" is normalized to "N" (individual).
     */
    private String normalizeIsCommittee(String isCommittee) {
        return AppsConstants.YesNo.Y.name().equalsIgnoreCase(isCommittee)
                ? AppsConstants.YesNo.Y.name()
                : AppsConstants.YesNo.N.name();
    }

    private void rejectTempLimits(List<DALimitTemp> tempLimits,
                                  DADesignationData designation,
                                  Date now,
                                  String username) {
        for (DALimitTemp temp : tempLimits) {
            temp.setApproveStatus(MasterDataApproveStatus.REJECTED);
            temp.setApprovedDate(now);
            temp.setApprovedBy(username);
            temp.setModifiedBy(username);
            temp.setLastModifiedDate(now);
            daLimitTempRepository.save(temp);
        }

        designation.setApproveStatus(MasterDataApproveStatus.REJECTED);
        designation.setApprovedDate(now);
        designation.setApprovedBy(username);
        designation.setModifiedDate(now);
        designation.setModifiedBy(username);
        daDesignationMasterRepository.saveAndFlush(designation);
    }

    private DADesignationBulkSaveResponse buildApproveRejectBulkResponse(DADesignationData designation,
                                                                         MasterDataApproveStatus approveStatus) {
        DADesignationBulkSaveResponse bulkResponse = new DADesignationBulkSaveResponse();

        String committeeFlag = AppsConstants.YesNo.Y.name();
        String individualFlag = AppsConstants.YesNo.N.name();

        Map<String, Double> committeeValues = loadValuesForApproveReject(designation.getId(), committeeFlag, approveStatus);
        Map<String, Double> individualValues = loadValuesForApproveReject(designation.getId(), individualFlag, approveStatus);

        if (!committeeValues.isEmpty()) {
            bulkResponse.getCommittee().add(
                    buildRowResponse(designation, DaTableType.COMMITTEE, committeeFlag, approveStatus, committeeValues));
        }
        if (!individualValues.isEmpty()) {
            bulkResponse.getIndividual().add(
                    buildRowResponse(designation, DaTableType.INDIVIDUAL, individualFlag, approveStatus, individualValues));
        }

        if (bulkResponse.getCommittee().isEmpty() && bulkResponse.getIndividual().isEmpty()) {
            bulkResponse.getCommittee().add(
                    buildRowResponse(designation, DaTableType.COMMITTEE, committeeFlag, approveStatus, new LinkedHashMap<>()));
        }

        return bulkResponse;
    }

    private Map<String, Double> loadValuesForApproveReject(Integer designationId,
                                                           String isCommittee,
                                                           MasterDataApproveStatus approveStatus) {
        Map<String, Double> values = new LinkedHashMap<>();

        if (MasterDataApproveStatus.APPROVED.equals(approveStatus)) {
                daLimitRepository.findAllByDesignationIdAndStatus(designationId, AppsConstants.Status.ACT.name()).stream()
                    .filter(limit -> isCommittee.equalsIgnoreCase(normalizeIsCommittee(limit.getIsCommittee())))
                    .sorted(Comparator.comparing(DALimit::getColumnId, Comparator.nullsLast(Integer::compareTo)))
                    .forEach(limit -> values.put(String.valueOf(limit.getColumnId()), limit.getRiskValue()));
        } else {
            daLimitTempRepository.findAllByDesignationId(designationId).stream()
                .filter(limit -> isCommittee.equalsIgnoreCase(normalizeIsCommittee(limit.getIsCommittee())))
                .filter(limit -> AppsConstants.Status.ACT.equals(limit.getStatus()))
                .sorted(Comparator.comparing(DALimitTemp::getColumnId, Comparator.nullsLast(Integer::compareTo)))
                    .forEach(limit -> values.put(String.valueOf(limit.getColumnId()), limit.getRiskValue()));
        }

        return values;
    }

    private DADesignationSaveResponse buildRowResponse(DADesignationData designation,
                                                       DaTableType tableType,
                                                       String isCommittee,
                                                       MasterDataApproveStatus approveStatus,
                                                       Map<String, Double> values) {
        DADesignationSaveResponse row = new DADesignationSaveResponse();
        row.setDesignationId(designation.getId());
        row.setDesignationCode(designation.getDesignationCode());
        row.setDesignation(designation.getDesignation());
        row.setDescription(designation.getDescription());
        row.setTableType(tableType.name());
        row.setIsCommittee(isCommittee);
        row.setDisplayOrder(designation.getDisplayOrder());
        row.setStatus(designation.getStatus());
        row.setApproveStatus(approveStatus);
        row.setValues(values);
        return row;
    }

    private DADesignationSaveResponse buildSaveResponse(DADesignationData designation,
                                                        DaTableType tableType,
                                                        String isCommittee) {
        DADesignationSaveResponse response = new DADesignationSaveResponse();
        response.setDesignationId(designation.getId());
        response.setDesignationCode(designation.getDesignationCode());
        response.setDesignation(designation.getDesignation());
        response.setDescription(designation.getDescription());
        response.setTableType(tableType.name());
        response.setIsCommittee(isCommittee);
        response.setDisplayOrder(designation.getDisplayOrder());
        response.setStatus(designation.getStatus());
        response.setApproveStatus(designation.getApproveStatus());

        Map<String, Double> values = new LinkedHashMap<>();
        daLimitTempRepository
        .findAllByDesignationIdAndIsCommitteeAndStatus(
            designation.getId(), isCommittee, AppsConstants.Status.ACT.name())
                .stream()
                .sorted(Comparator.comparing(
                        limit -> limit.getColumnId(),
                        Comparator.nullsLast(Integer::compareTo)))
                .forEach(limit -> values.put(String.valueOf(limit.getColumnId()), limit.getRiskValue()));

        response.setValues(values);
        return response;
    }

    private void saveDaLimitAudit(DALimit daLimit) {
        if (daLimit == null || daLimit.getDaLimitsId() == null) {
            return;
        }

        DALimitAud audit = new DALimitAud();
        audit.setId(daLimitAudRepository.getCurrentSequenceValue());
        audit.setDaLimitsId(daLimit.getDaLimitsId());
        audit.setDesignationId(daLimit.getDesignationId());
        audit.setColumnId(daLimit.getColumnId());
        audit.setRiskValue(daLimit.getRiskValue());
        audit.setStatus(daLimit.getStatus());
        audit.setAuthorizerDisplayName(daLimit.getAuthorizerDisplayName());
        audit.setRiskRating(daLimit.getRiskRating());
        audit.setIsCommittee(daLimit.getIsCommittee());
        audit.setApproveStatus(daLimit.getApproveStatus());
        audit.setApprovedDate(daLimit.getApprovedDate());
        audit.setApprovedBy(daLimit.getApprovedBy());
        audit.setCreatedBy(daLimit.getCreatedBy());
        audit.setCreatedDate(daLimit.getCreatedDate() != null ? daLimit.getCreatedDate() : new Date());
        audit.setModifiedBy(daLimit.getModifiedBy());
        audit.setLastModifiedDate(daLimit.getLastModifiedDate());
        daLimitAudRepository.save(audit);
        log.info("Saved DA limit audit for DA_LIMITS_ID={}", daLimit.getDaLimitsId());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<DATableApprovalResponse>> getDaTable() throws ApiRequestException {
        log.info("START : DaDesignationServiceImpl | getDaTable");

        List<DADesignationData> designations =
                daDesignationMasterRepository.findAllByStatus(Status.ACT.name());

        List<DALimit> approvedLimits = daLimitRepository.findAllByStatus(AppsConstants.Status.ACT.name());
        List<DALimitTemp> pendingLimits = daLimitTempRepository.findAllByStatus(AppsConstants.Status.ACT.name());
        Map<Integer, Map<String, Map<Integer, Double>>> approvedValueIndex = indexApprovedValues(approvedLimits);
        Map<Integer, Map<String, Map<Integer, Double>>> pendingValueIndex = indexPendingValues(pendingLimits);

        List<DAHeaderResponse> headerTree = buildHeaderTree();
        DATableHeaderDTO approvedHeaders = buildHeaderResponse(headerTree);
        DATableHeaderDTO pendingHeaders = buildHeaderResponse(headerTree);

        DATableApprovalResponse approvalResponse = new DATableApprovalResponse();
        approvalResponse.setApproved(buildTableData(designations, false, approvedHeaders, approvedValueIndex));
        approvalResponse.setPending(buildTableData(designations, false, pendingHeaders, pendingValueIndex));
        populateDesignationLists(approvalResponse, designations);

        StandardResponse<DATableApprovalResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), approvalResponse);
        log.info("END : getDaTable | committeeDesignations={}, individualDesignations={}, approvedCommitteeRows={}, pendingCommitteeRows={}",
                approvalResponse.getCommitteeDesignationList().size(),
                approvalResponse.getIndividualDesignationList().size(),
                approvalResponse.getApproved().getCommitteeRows().size(),
                approvalResponse.getPending().getCommitteeRows().size());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<DATableApprovalResponse>> getDaTableById(Integer designationId)
            throws ApiRequestException {
        log.info("START : DaDesignationServiceImpl | getDaTableById | designationId={}", designationId);

        if (designationId == null) {
            throw new ApiRequestException("designationId cannot be null");
        }

        DADesignationData designation = daDesignationMasterRepository.findById(designationId)
                .orElseThrow(() -> new ApiRequestException(
                        "DA Designation with id " + designationId + " does not exist"));

        List<DALimit> approvedLimits = daLimitRepository.findAllByDesignationIdAndStatus(
            designationId, AppsConstants.Status.ACT.name());
        List<DALimitTemp> pendingLimits = daLimitTempRepository.findAllByDesignationIdAndStatus(
            designationId, AppsConstants.Status.ACT.name());
        Map<Integer, Map<String, Map<Integer, Double>>> approvedValueIndex = indexApprovedValues(approvedLimits);
        Map<Integer, Map<String, Map<Integer, Double>>> pendingValueIndex = indexPendingValues(pendingLimits);

        List<DAHeaderResponse> headerTree = buildHeaderTree();
        DATableHeaderDTO approvedHeaders = buildHeaderResponse(headerTree);
        DATableHeaderDTO pendingHeaders = buildHeaderResponse(headerTree);

        DATableApprovalResponse approvalResponse = new DATableApprovalResponse();
        approvalResponse.setApproved(buildTableData(Collections.singletonList(designation), true, approvedHeaders, approvedValueIndex));
        approvalResponse.setPending(buildTableData(Collections.singletonList(designation), true, pendingHeaders, pendingValueIndex));
        populateDesignationLists(approvalResponse, Collections.singletonList(designation));

        StandardResponse<DATableApprovalResponse> response =
            new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), approvalResponse);
        log.info("END : getDaTableById | designationId={}", designationId);
        return ResponseEntity.ok(response);
    }

    /**
     * Splits designations into committee/individual lists based on whether they appear
     * in the committee or individual rows of either the approved or pending table data.
     */
    private void populateDesignationLists(DATableApprovalResponse approvalResponse,
                                          List<DADesignationData> designations) {
        Set<Integer> committeeIds = new HashSet<>();
        Set<Integer> individualIds = new HashSet<>();

        for (DATableDataResponse tableData : Arrays.asList(approvalResponse.getApproved(), approvalResponse.getPending())) {
            tableData.getCommitteeRows().stream()
                    .map(DADesignationTableDTO::getDesignationId)
                    .filter(Objects::nonNull)
                    .forEach(committeeIds::add);
            tableData.getIndividualRows().stream()
                    .map(DADesignationTableDTO::getDesignationId)
                    .filter(Objects::nonNull)
                    .forEach(individualIds::add);
        }

        String committeeFlag = AppsConstants.YesNo.Y.name();
        String individualFlag = AppsConstants.YesNo.N.name();

        List<DADesignationListDTO> committeeList = designations.stream()
                .filter(designation -> committeeIds.contains(designation.getId()))
                .map(designation -> new DADesignationListDTO(designation, committeeFlag))
                .sorted(Comparator.comparing(DADesignationListDTO::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .toList();

        List<DADesignationListDTO> individualList = designations.stream()
                .filter(designation -> individualIds.contains(designation.getId()))
                .map(designation -> new DADesignationListDTO(designation, individualFlag))
                .sorted(Comparator.comparing(DADesignationListDTO::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .toList();

        approvalResponse.setCommitteeDesignationList(committeeList);
        approvalResponse.setIndividualDesignationList(individualList);
    }

    /**
     * Builds table response. Leaf headers carry tableValues as designationId -> riskValue.
     * Rows carry tableValues as column subId -> riskValue.
     */
    private DATableDataResponse buildTableData(List<DADesignationData> designations,
                                               boolean includeEmptyRows,
                                               DATableHeaderDTO headers,
                                               Map<Integer, Map<String, Map<Integer, Double>>> valueIndex) {
        DATableDataResponse tableData = new DATableDataResponse();
        tableData.setCommitteeTableHeaders(headers.getCommitteeTableHeaders());
        tableData.setIndividualTableHeaders(headers.getIndividualTableHeaders());

        Map<Integer, DATableHeadingResponse> committeeLeaves = indexLeafHeaders(tableData.getCommitteeTableHeaders());
        Map<Integer, DATableHeadingResponse> individualLeaves = indexLeafHeaders(tableData.getIndividualTableHeaders());

        String committeeFlag = AppsConstants.YesNo.Y.name();
        String individualFlag = AppsConstants.YesNo.N.name();

        for (DADesignationData designation : designations) {
            Map<Integer, Double> committeeValues = getValuesFromIndex(valueIndex, designation.getId(), committeeFlag);
            if (includeEmptyRows || !committeeValues.isEmpty()) {
                tableData.getCommitteeRows().add(
                        buildRowResponse(designation, DaTableType.COMMITTEE, committeeFlag, committeeValues));
                putDesignationValuesOnHeaders(committeeLeaves, designation.getId(), committeeValues);
            }

            Map<Integer, Double> individualValues = getValuesFromIndex(valueIndex, designation.getId(), individualFlag);
            if (includeEmptyRows || !individualValues.isEmpty()) {
                tableData.getIndividualRows().add(
                        buildRowResponse(designation, DaTableType.INDIVIDUAL, individualFlag, individualValues));
                putDesignationValuesOnHeaders(individualLeaves, designation.getId(), individualValues);
            }
        }

        tableData.getCommitteeRows().sort(Comparator.comparing(
                DADesignationTableDTO::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)));
        tableData.getIndividualRows().sort(Comparator.comparing(
                DADesignationTableDTO::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)));
        return tableData;
    }

        private DATableHeaderDTO buildHeaderResponse(List<DAHeaderResponse> rootHeaders) {
        List<DATableHeadingResponse> individual = rootHeaders.stream()
            .filter(h -> DaTableType.INDIVIDUAL.equals(h.getTableType()))
            .map(this::mapToJsonStructure)
            .toList();

        List<DATableHeadingResponse> committee = rootHeaders.stream()
            .filter(h -> DaTableType.COMMITTEE.equals(h.getTableType()))
            .map(this::mapToJsonStructure)
            .toList();

        DATableHeaderDTO dto = new DATableHeaderDTO();
        dto.setIndividualTableHeaders(individual);
        dto.setCommitteeTableHeaders(committee);
        return dto;
        }

    private DADesignationTableDTO buildRowResponse(DADesignationData designation,
                                                   DaTableType tableType,
                                                   String isCommittee,
                                                   Map<Integer, Double> values) {
        DADesignationTableDTO row = new DADesignationTableDTO();
        row.setDesignationId(designation.getId());
        row.setDesignationCode(designation.getDesignationCode());
        row.setDesignation(designation.getDesignation());
        row.setDescription(designation.getDescription());
        row.setTableType(tableType.name());
        row.setIsCommittee(isCommittee);
        row.setDisplayOrder(designation.getDisplayOrder());
        row.setStatus(designation.getStatus());
        row.setApproveStatus(designation.getApproveStatus());
        row.setTableValues(values);
        return row;
    }


    private Map<Integer, Map<String, Map<Integer, Double>>> indexApprovedValues(List<DALimit> limits) {
        Map<Integer, Map<String, Map<Integer, Double>>> valueIndex = new HashMap<>();
        if (CollectionUtils.isEmpty(limits)) {
            return valueIndex;
        }

        for (DALimit limit : limits) {
            if (limit == null || limit.getDesignationId() == null
                    || limit.getColumnId() == null || limit.getRiskValue() == null) {
                continue;
            }

            valueIndex
                    .computeIfAbsent(limit.getDesignationId(), k -> new HashMap<>())
                    .computeIfAbsent(normalizeIsCommittee(limit.getIsCommittee()), k -> new LinkedHashMap<>())
                    .put(limit.getColumnId(), limit.getRiskValue());
        }

        return valueIndex;
    }

    private Map<Integer, Map<String, Map<Integer, Double>>> indexPendingValues(List<DALimitTemp> limits) {
        Map<Integer, Map<String, Map<Integer, Double>>> valueIndex = new HashMap<>();
        if (CollectionUtils.isEmpty(limits)) {
            return valueIndex;
        }

        for (DALimitTemp limit : limits) {
            if (limit == null || limit.getDesignation() == null || limit.getDesignation().getId() == null
                    || limit.getColumnId() == null || limit.getRiskValue() == null) {
                continue;
            }

            valueIndex
                    .computeIfAbsent(limit.getDesignation().getId(), k -> new HashMap<>())
                    .computeIfAbsent(normalizeIsCommittee(limit.getIsCommittee()), k -> new LinkedHashMap<>())
                    .put(limit.getColumnId(), limit.getRiskValue());
        }

        return valueIndex;
    }

    private Map<Integer, Double> getValuesFromIndex(Map<Integer, Map<String, Map<Integer, Double>>> valueIndex,
                                                    Integer designationId,
                                                    String isCommittee) {
        if (designationId == null || !StringUtils.hasText(isCommittee) || CollectionUtils.isEmpty(valueIndex)) {
            return Collections.emptyMap();
        }

        Map<String, Map<Integer, Double>> byCommittee = valueIndex.get(designationId);
        if (CollectionUtils.isEmpty(byCommittee)) {
            return Collections.emptyMap();
        }

        Map<Integer, Double> values = byCommittee.get(isCommittee);
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }

        // Values are only read downstream (row DTO + header population); avoid an extra copy per row.
        return values;
    }

    /** Puts designationId -> riskValue onto each leaf header for the matching subId. */
    private void putDesignationValuesOnHeaders(Map<Integer, DATableHeadingResponse> leafBySubId,
                                               Integer designationId,
                                               Map<Integer, Double> valuesBySubId) {
        if (designationId == null || CollectionUtils.isEmpty(valuesBySubId)) {
            return;
        }
        for (Map.Entry<Integer, Double> entry : valuesBySubId.entrySet()) {
            DATableHeadingResponse leaf = leafBySubId.get(entry.getKey());
            if (leaf != null && entry.getValue() != null) {
                leaf.getTableValues().put(designationId, entry.getValue());
            }
        }
    }

    private Map<Integer, DATableHeadingResponse> indexLeafHeaders(List<DATableHeadingResponse> headers) {
        Map<Integer, DATableHeadingResponse> leafBySubId = new HashMap<>();
        collectLeafHeaders(headers, leafBySubId);
        return leafBySubId;
    }

    private void collectLeafHeaders(List<DATableHeadingResponse> headers,
                                    Map<Integer, DATableHeadingResponse> leafBySubId) {
        if (CollectionUtils.isEmpty(headers)) {
            return;
        }
        for (DATableHeadingResponse header : headers) {
            if (header == null) {
                continue;
            }
            if (CollectionUtils.isEmpty(header.getSubHeadings())) {
                if (header.getSubId() != null) {
                    leafBySubId.put(header.getSubId(), header);
                }
            } else {
                collectLeafHeaders(header.getSubHeadings(), leafBySubId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<DADesignationListDTO>> deleteDaDesignation(Integer designationId)
            throws ApiRequestException {
        log.info("START : DaDesignationServiceImpl | deleteDaDesignation | designationId={}", designationId);

        if (designationId == null) {
            throw new ApiRequestException("designationId cannot be null");
        }
        List<DALimitTemp> deletedRecords = new ArrayList<>();
        List<DALimitTemp> tempLimits = daLimitTempRepository.findAllByDesignationId(designationId);
        for( DALimitTemp temp : tempLimits) {
            temp.setStatus(AppsConstants.Status.INA);
            temp.setModifiedBy(UserContext.getUsername());
            temp.setLastModifiedDate(new Date());
            deletedRecords.add(temp);
        }

        daLimitTempRepository.saveAll(deletedRecords);

        DADesignationData designation = daDesignationMasterRepository.findById(designationId)
                .orElseThrow(() -> new ApiRequestException(
                        "DA Designation with id " + designationId + " does not exist"));
        designation.setStatus(Status.INA);
        designation.setModifiedBy(UserContext.getUsername());
        designation.setModifiedDate(new Date());
        daDesignationMasterRepository.save(designation);


        StandardResponse<DADesignationListDTO> response = new StandardResponse<>(
                ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), new DADesignationListDTO(designation));
        log.info("END : deleteDaDesignation | designationId={}", designationId);
        return ResponseEntity.ok(response);
    }
}
