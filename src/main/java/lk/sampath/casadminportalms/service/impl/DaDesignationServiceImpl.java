package lk.sampath.casadminportalms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
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
import lk.sampath.casadminportalms.entity.daDesignation.DALimitTemp;
import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.DaTableType;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitHeadingRepository;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitTempRepository;
import lk.sampath.casadminportalms.service.DaDesignationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DaDesignationServiceImpl implements DaDesignationService {

    @Value("${load.designations}")
    private String loadDesignations;

    private final DALimitHeadingRepository daLimitHeadingRepository;
    private final DALimitTempRepository daLimitTempRepository;

    public DaDesignationServiceImpl(DALimitHeadingRepository daLimitHeadingRepository,
                                    DALimitTempRepository daLimitTempRepository) {
        this.daLimitHeadingRepository = daLimitHeadingRepository;
        this.daLimitTempRepository = daLimitTempRepository;
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

        if (!CollectionUtils.isEmpty(request.getCommittee())) {
            for (DADesignationSaveRequest row : request.getCommittee()) {
                bulkResponse.getCommittee().add(saveSingleRow(row, DaTableType.COMMITTEE));
            }
        }

        if (!CollectionUtils.isEmpty(request.getIndividual())) {
            for (DADesignationSaveRequest row : request.getIndividual()) {
                bulkResponse.getIndividual().add(saveSingleRow(row, DaTableType.INDIVIDUAL));
            }
        }

        StandardResponse<DADesignationBulkSaveResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), bulkResponse);
        log.info("END : saveDaDesignationLimits | committee={}, individual={}",
                bulkResponse.getCommittee().size(), bulkResponse.getIndividual().size());
        return ResponseEntity.ok(response);
    }

    private DADesignationSaveResponse saveSingleRow(DADesignationSaveRequest request, DaTableType tableType) {
        validateSaveRequest(request, tableType);

        String dbTableType = tableType.name();
        String isCommittee = DaTableType.COMMITTEE.equals(tableType)
                ? AppsConstants.YesNo.Y.name()
                : AppsConstants.YesNo.N.name();

        Map<Integer, DATableHeader> columnHeadersBySubId = loadValidColumns(dbTableType);
        validateColumnValues(request.getColumnValues(), columnHeadersBySubId, tableType);

        saveColumnValuesToTemp(request.getDesignationId(), isCommittee, request.getColumnValues(), columnHeadersBySubId);

        return buildSaveResponse(request, tableType, isCommittee);
    }

    private void validateSaveRequest(DADesignationSaveRequest request, DaTableType tableType) {
        if (request == null) {
            throw new ApiRequestException("Designation row cannot be null for " + tableType.name());
        }
        if (request.getDesignationId() == null) {
            throw new ApiRequestException("designationId is required for " + tableType.name());
        }
        if (CollectionUtils.isEmpty(request.getColumnValues())) {
            throw new ApiRequestException("columnValues cannot be empty for " + tableType.name());
        }
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

    private void saveColumnValuesToTemp(Integer designationId,
                                        String isCommittee,
                                        List<DAColumnValueRequest> columnValues,
                                        Map<Integer, DATableHeader> columnHeadersBySubId) {
        daLimitTempRepository.deleteByDesignationIdAndIsCommittee(designationId, isCommittee);

        String username = UserContext.getUsername();
        Date now = new Date();

        for (DAColumnValueRequest columnValue : columnValues) {
            DATableHeader header = columnHeadersBySubId.get(columnValue.getSubId());

            DALimitTemp limitTemp = new DALimitTemp();
            limitTemp.setDesignationId(designationId);
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

    private DADesignationSaveResponse buildSaveResponse(DADesignationSaveRequest request,
                                                        DaTableType tableType,
                                                        String isCommittee) {
        DADesignationSaveResponse response = new DADesignationSaveResponse();
        response.setDesignationId(request.getDesignationId());
        response.setDesignationCode(request.getDesignationCode());
        response.setDesignation(request.getDesignation());
        response.setDescription(request.getDescription());
        response.setTableType(tableType.name());
        response.setIsCommittee(isCommittee);
        response.setDisplayOrder(request.getDisplayOrder());
        response.setStatus(MasterDataApproveStatus.PENDING.name());

        Map<String, Double> values = new LinkedHashMap<>();
        List<DALimitTemp> limits = daLimitTempRepository
                .findAllByDesignationIdAndIsCommitteeAndStatus(
                        request.getDesignationId(), isCommittee, AppsConstants.Status.ACT);
        limits.stream()
                .sorted(Comparator.comparing(DALimitTemp::getColumnId, Comparator.nullsLast(Integer::compareTo)))
                .forEach(limit -> values.put(String.valueOf(limit.getColumnId()), limit.getRiskValue()));
        response.setValues(values);
        return response;
    }
}
