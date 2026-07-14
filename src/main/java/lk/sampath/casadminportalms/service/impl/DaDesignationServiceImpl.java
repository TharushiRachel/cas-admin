package lk.sampath.casadminportalms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.dadesignation.DAColumnValueRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationBulkSaveResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationCodeDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationSaveRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationSaveResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeaderDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeadingResponse;
import lk.sampath.casadminportalms.dto.userSession.UserContext;
import lk.sampath.casadminportalms.entity.daDesignation.DADesignationMasterData;
import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import lk.sampath.casadminportalms.entity.daDesignation.DALimitTemp;
import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.DaTableType;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.daDesignation.DADesignationMasterRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitHeadingRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitTempRepository;
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
import java.util.stream.Collectors;

@Service
@Log4j2
public class DaDesignationServiceImpl implements DaDesignationService {

    private static final String STATUS_ACT = "ACT";

    @Value("${load.designations}")
    private String loadDesignations;

    private final DALimitHeadingRepository daLimitHeadingRepository;
    private final DADesignationMasterRepository daDesignationMasterRepository;
    private final DALimitTempRepository daLimitTempRepository;
    private final DALimitRepository daLimitRepository;

    public DaDesignationServiceImpl(DALimitHeadingRepository daLimitHeadingRepository,
                                    DADesignationMasterRepository daDesignationMasterRepository,
                                    DALimitTempRepository daLimitTempRepository,
                                    DALimitRepository daLimitRepository) {
        this.daLimitHeadingRepository = daLimitHeadingRepository;
        this.daDesignationMasterRepository = daDesignationMasterRepository;
        this.daLimitTempRepository = daLimitTempRepository;
        this.daLimitRepository = daLimitRepository;
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
                .collect(Collectors.toList());

        List<DATableHeadingResponse> committee = rootHeaders.stream()
                .filter(h -> DaTableType.COMMITTEE.equals(h.getTableType()))
                .map(this::mapToJsonStructure)
                .collect(Collectors.toList());

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
                            .collect(Collectors.toList());

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
        Map<String, DADesignationMasterData> designationByCode = new HashMap<>();

        if (!CollectionUtils.isEmpty(request.getCommittee())) {
            for (DADesignationSaveRequest row : request.getCommittee()) {
                bulkResponse.getCommittee().add(saveOrUpdateSingleRow(row, DaTableType.COMMITTEE, designationByCode));
            }
        }

        if (!CollectionUtils.isEmpty(request.getIndividual())) {
            for (DADesignationSaveRequest row : request.getIndividual()) {
                bulkResponse.getIndividual().add(saveOrUpdateSingleRow(row, DaTableType.INDIVIDUAL, designationByCode));
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
                                                            Map<String, DADesignationMasterData> designationByCode) {
        validateSaveRequest(request, tableType);

        String dbTableType = tableType.name();
        String isCommittee = DaTableType.COMMITTEE.equals(tableType)
                ? AppsConstants.YesNo.Y.name()
                : AppsConstants.YesNo.N.name();

        Map<Integer, DATableHeader> columnHeadersBySubId = loadValidColumns(dbTableType);
        validateColumnValues(request.getColumnValues(), columnHeadersBySubId, tableType);

        DADesignationMasterData designation = upsertDesignation(request, designationByCode);
        replaceLimitTemps(designation, isCommittee, request.getColumnValues(), columnHeadersBySubId);
        cacheDesignation(designationByCode, designation);

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

    private DADesignationMasterData upsertDesignation(DADesignationSaveRequest request,
                                                     Map<String, DADesignationMasterData> designationByCode) {
        Date now = new Date();
        String username = UserContext.getUsername();

        DADesignationMasterData designation = resolveExistingDesignation(request, designationByCode);
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
        designation.setStatus(STATUS_ACT);
        designation.setApproveStatus(MasterDataApproveStatus.PENDING.name());
        designation.setDisplayOrder(
                resolveDisplayOrder(request.getDisplayOrder(), designation.getDisplayOrder()));
        designation.setModifiedDate(now);
        designation.setModifiedBy(username);

        if (isNew) {
            designation.setCreatedDate(now);
            designation.setCreatedBy(username);
        }

        designation = daDesignationMasterRepository.saveAndFlush(designation);
        cacheDesignation(designationByCode, designation);
        return designation;
    }

    private DADesignationMasterData resolveExistingDesignation(DADesignationSaveRequest request,
                                                              Map<String, DADesignationMasterData> designationByCode) {
        if (request.getDesignationId() != null) {
            DADesignationMasterData byId = daDesignationMasterRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new ApiRequestException(
                            "DA Designation with id " + request.getDesignationId() + " does not exist"));
            cacheDesignation(designationByCode, byId);
            return byId;
        }

        if (StringUtils.hasText(request.getDesignationCode())) {
            String codeKey = normalizeCode(request.getDesignationCode());
            DADesignationMasterData cached = designationByCode.get(codeKey);
            if (cached != null) {
                return cached;
            }
            return daDesignationMasterRepository
                    .findByDesignationCodeAndStatus(request.getDesignationCode().trim(), STATUS_ACT)
                    .orElseGet(DADesignationMasterData::new);
        }

        return new DADesignationMasterData();
    }

    private void cacheDesignation(Map<String, DADesignationMasterData> designationByCode,
                                  DADesignationMasterData designation) {
        if (designation != null && StringUtils.hasText(designation.getDesignationCode())) {
            designationByCode.put(normalizeCode(designation.getDesignationCode()), designation);
        }
    }

    private String normalizeCode(String designationCode) {
        return designationCode.trim().toUpperCase(Locale.ROOT);
    }

    private Integer resolveDisplayOrder(Integer requestedOrder, Integer existingOrder) {
        if (requestedOrder != null) {
            return requestedOrder;
        }
        if (existingOrder != null) {
            return existingOrder;
        }
        return daDesignationMasterRepository.findAllByStatus(STATUS_ACT).stream()
                .map(DADesignationMasterData::getDisplayOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;
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

    private void replaceLimitTemps(DADesignationMasterData designation,
                                   String isCommittee,
                                   List<DAColumnValueRequest> columnValues,
                                   Map<Integer, DATableHeader> columnHeadersBySubId) {
        // Avoid lazy collection access; manage temps through repository only.
        daLimitTempRepository.deleteByDesignationIdAndIsCommittee(designation.getId(), isCommittee);

        String username = UserContext.getUsername();
        Date now = new Date();

        for (DAColumnValueRequest columnValue : columnValues) {
            DATableHeader header = columnHeadersBySubId.get(columnValue.getSubId());

            DALimitTemp limitTemp = new DALimitTemp();
            limitTemp.setDaLimitsId(daLimitTempRepository.getCurrentSequenceValue());
            limitTemp.setDesignation(designation);
            limitTemp.setColumnId(columnValue.getSubId());
            limitTemp.setRiskValue(columnValue.getRiskValue());
            limitTemp.setRiskRating(header.getLabel());
            limitTemp.setIsCommittee(isCommittee);
            limitTemp.setStatus(AppsConstants.Status.ACT);
            limitTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
            limitTemp.setCreatedBy(username);
            limitTemp.setCreatedDate(now);

            daLimitTempRepository.save(limitTemp);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<DADesignationSaveResponse>> approveRejectDaDesignationLimits(
            ApproveRejectRQ request) throws ApiRequestException {
        log.info("START : approveRejectDaDesignationLimits | request={}", request);

        if (request == null || request.getApproveRejectDataID() == null) {
            throw new ApiRequestException("approveRejectDataID (designationId) cannot be null");
        }
        if (request.getApproveStatus() == null) {
            throw new ApiRequestException("approveStatus is required (APPROVED or REJECTED)");
        }

        Integer designationId = request.getApproveRejectDataID();
        DADesignationMasterData designation = daDesignationMasterRepository.findById(designationId)
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

        DADesignationSaveResponse saveResponse = buildApproveRejectResponse(designation, approveStatus);
        StandardResponse<DADesignationSaveResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), saveResponse);
        log.info("END : approveRejectDaDesignationLimits | designationId={}, status={}", designationId, approveStatus);
        return ResponseEntity.ok(response);
    }

    private void approveTempLimits(List<DALimitTemp> tempLimits,
                                   DADesignationMasterData designation,
                                   Date now,
                                   String username) {
        // Replace any previous master limits for this designation.
        daLimitRepository.deleteByDesignationId(designation.getId());

        for (DALimitTemp temp : tempLimits) {
            DALimit master = new DALimit();
            master.setDaLimitsId(temp.getDaLimitsId());
            master.setDesignationId(designation.getId());
            master.setColumnId(temp.getColumnId());
            master.setRiskValue(temp.getRiskValue());
            master.setRiskRating(temp.getRiskRating());
            master.setIsCommittee(temp.getIsCommittee());
            master.setStatus(temp.getStatus() != null ? temp.getStatus() : AppsConstants.Status.ACT);
            master.setAuthorizerDisplayName(temp.getAuthorizerDisplayName());
            master.setApproveStatus(MasterDataApproveStatus.APPROVED);
            master.setApprovedDate(now);
            master.setApprovedBy(username);
            master.setCreatedBy(temp.getCreatedBy());
            master.setCreatedDate(temp.getCreatedDate());
            master.setModifiedBy(username);
            master.setLastModifiedDate(now);
            daLimitRepository.save(master);
        }

        daLimitTempRepository.deleteByDesignationId(designation.getId());

        designation.setApproveStatus(MasterDataApproveStatus.APPROVED.name());
        designation.setApprovedDate(now);
        designation.setApprovedBy(username);
        designation.setModifiedDate(now);
        designation.setModifiedBy(username);
        daDesignationMasterRepository.saveAndFlush(designation);
    }

    private void rejectTempLimits(List<DALimitTemp> tempLimits,
                                  DADesignationMasterData designation,
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

        designation.setApproveStatus(MasterDataApproveStatus.REJECTED.name());
        designation.setApprovedDate(now);
        designation.setApprovedBy(username);
        designation.setModifiedDate(now);
        designation.setModifiedBy(username);
        daDesignationMasterRepository.saveAndFlush(designation);
    }

    private DADesignationSaveResponse buildApproveRejectResponse(DADesignationMasterData designation,
                                                                MasterDataApproveStatus approveStatus) {
        DADesignationSaveResponse response = new DADesignationSaveResponse();
        response.setDesignationId(designation.getId());
        response.setDesignationCode(designation.getDesignationCode());
        response.setDesignation(designation.getDesignation());
        response.setDescription(designation.getDescription());
        response.setDisplayOrder(designation.getDisplayOrder());
        response.setStatus(approveStatus.name());
        response.setIsCommittee(designation.getIsCommittee());

        Map<String, Double> values = new LinkedHashMap<>();
        if (MasterDataApproveStatus.APPROVED.equals(approveStatus)) {
            daLimitRepository.findAllByDesignationIdAndStatus(designation.getId(), AppsConstants.Status.ACT).stream()
                    .sorted(Comparator.comparing(DALimit::getColumnId, Comparator.nullsLast(Integer::compareTo)))
                    .forEach(limit -> values.put(String.valueOf(limit.getColumnId()), limit.getRiskValue()));
        } else {
            daLimitTempRepository.findAllByDesignationId(designation.getId()).stream()
                    .sorted(Comparator.comparing(DALimitTemp::getColumnId, Comparator.nullsLast(Integer::compareTo)))
                    .forEach(limit -> values.put(String.valueOf(limit.getColumnId()), limit.getRiskValue()));
        }
        response.setValues(values);
        return response;
    }

    private DADesignationSaveResponse buildSaveResponse(DADesignationMasterData designation,
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
        response.setStatus(designation.getApproveStatus());

        Map<String, Double> values = new LinkedHashMap<>();
        daLimitTempRepository
                .findAllByDesignationIdAndIsCommitteeAndStatus(
                        designation.getId(), isCommittee, AppsConstants.Status.ACT)
                .stream()
                .sorted(Comparator.comparing(DALimitTemp::getColumnId, Comparator.nullsLast(Integer::compareTo)))
                .forEach(limit -> values.put(String.valueOf(limit.getColumnId()), limit.getRiskValue()));
        response.setValues(values);
        return response;
    }
}
