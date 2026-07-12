package lk.sampath.casadminportalms.service.impl;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationRowResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DALimitValueRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DAReorderItemRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DAReorderRequest;
import lk.sampath.casadminportalms.dto.dadesignation.DATableResponse;
import lk.sampath.casadminportalms.entity.daDesignation.DADesignationMasterData;
import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.daDesignation.DADesignationMasterDao;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitDao;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitHeadingRepository;
import lk.sampath.casadminportalms.service.DaDesignationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DaDesignationServiceImpl implements DaDesignationService {

    private static final String STATUS_ACT = "ACT";
    private static final String STATUS_INA = "INA";
    private static final String TABLE_TYPE_INDIVIDUAL = "INDIVIDUAL";
    private static final String DESIGNATION_WITH = "DA Designation with ";
    private static final String DOES_NOT_EXISTS = " does not exist";

    private final DALimitHeadingRepository daLimitHeadingRepository;
    private final DADesignationMasterDao daDesignationMasterDao;
    private final DALimitDao daLimitDao;

    public DaDesignationServiceImpl(DALimitHeadingRepository daLimitHeadingRepository,
                                    DADesignationMasterDao daDesignationMasterDao,
                                    DALimitDao daLimitDao) {
        this.daLimitHeadingRepository = daLimitHeadingRepository;
        this.daDesignationMasterDao = daDesignationMasterDao;
        this.daLimitDao = daLimitDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<DAHeaderResponse>>> getAllLimitHeadings(String tableType) throws ApiRequestException {
        log.info("START : getAllLimitHeadings | tableType={}", tableType);
        validateTableType(tableType);

        List<DAHeaderResponse> rootHeaders = buildHeaderTree(tableType);
        StandardResponse<List<DAHeaderResponse>> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), rootHeaders);
        log.info("END : getAllLimitHeadings | roots={}", rootHeaders.size());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<DATableResponse>> getDaTable(String tableType, Pageable pageable) throws ApiRequestException {
        log.info("START : getDaTable | tableType={}", tableType);
        validateTableType(tableType);

        List<DAHeaderResponse> headers = buildHeaderTree(tableType);
        String isCommittee = resolveIsCommittee(tableType);

        Pageable orderedPageable = pageable.getSort().isSorted()
                ? pageable
                : org.springframework.data.domain.PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "displayOrder"));

        Page<DADesignationMasterData> designationPage =
                daDesignationMasterDao.findAllByIsCommitteeAndStatus(isCommittee, STATUS_ACT, orderedPageable);

        List<Integer> designationIds = designationPage.getContent().stream()
                .map(DADesignationMasterData::getId)
                .collect(Collectors.toList());

        Map<Integer, Map<Integer, Double>> limitsByDesignation = loadLimitsByDesignation(designationIds, isCommittee);

        List<DADesignationRowResponse> rows = designationPage.getContent().stream()
                .map(designation -> toRowResponse(designation, limitsByDesignation.getOrDefault(designation.getId(), Map.of())))
                .collect(Collectors.toList());

        DATableResponse tableResponse = new DATableResponse();
        tableResponse.setTableType(tableType.toUpperCase());
        tableResponse.setHeaders(headers);
        tableResponse.setRows(rows);

        StandardResponse<DATableResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), tableResponse);
        log.info("END : getDaTable | rows={}", rows.size());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<DADesignationRowResponse>> saveDesignation(DADesignationRequest request) throws ApiRequestException {
        log.info("START : saveDesignation | request={}", request);
        validateDesignationRequest(request, true);

        String isCommittee = resolveIsCommitteeFromRequest(request);
        Date now = new Date();

        DADesignationMasterData designation = new DADesignationMasterData();
        designation.setDesignation(request.getDesignation().trim());
        designation.setDesignationCode(request.getDesignationCode());
        designation.setDescription(request.getDescription());
        designation.setIsCommittee(isCommittee);
        designation.setDisplayOrder(resolveDisplayOrder(request.getDisplayOrder(), isCommittee));
        designation.setStatus(StringUtils.hasText(request.getStatus()) ? request.getStatus() : STATUS_ACT);
        designation.setApproveStatus(MasterDataApproveStatus.APPROVED.name());
        designation.setCreatedDate(now);
        designation.setModifiedDate(now);

        designation = daDesignationMasterDao.saveAndFlush(designation);
        saveOrReplaceLimits(designation.getId(), isCommittee, request.getLimits());

        DADesignationRowResponse row = toRowResponse(
                designation,
                toValueMap(daLimitDao.findAllByDesignationIdAndStatus(designation.getId(), AppsConstants.Status.ACT)));

        StandardResponse<DADesignationRowResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), row);
        log.info("END : saveDesignation | id={}", designation.getId());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<DADesignationRowResponse>> updateDesignation(Integer designationId, DADesignationRequest request)
            throws ApiRequestException {
        log.info("START : updateDesignation | id={}", designationId);
        if (designationId == null) {
            throw new ApiRequestException("Designation id cannot be null");
        }
        validateDesignationRequest(request, false);

        DADesignationMasterData designation = daDesignationMasterDao.findById(designationId)
                .orElseThrow(() -> new ApiRequestException(DESIGNATION_WITH + designationId + DOES_NOT_EXISTS));

        String isCommittee = StringUtils.hasText(request.getIsCommittee())
                ? request.getIsCommittee()
                : designation.getIsCommittee();

        if (StringUtils.hasText(request.getDesignation())) {
            designation.setDesignation(request.getDesignation().trim());
        }
        if (request.getDesignationCode() != null) {
            designation.setDesignationCode(request.getDesignationCode());
        }
        if (request.getDescription() != null) {
            designation.setDescription(request.getDescription());
        }
        if (request.getDisplayOrder() != null) {
            designation.setDisplayOrder(request.getDisplayOrder());
        }
        if (StringUtils.hasText(request.getStatus())) {
            designation.setStatus(request.getStatus());
        }
        designation.setIsCommittee(isCommittee);
        designation.setModifiedDate(new Date());

        designation = daDesignationMasterDao.saveAndFlush(designation);

        if (request.getLimits() != null) {
            saveOrReplaceLimits(designation.getId(), isCommittee, request.getLimits());
        }

        DADesignationRowResponse row = toRowResponse(
                designation,
                toValueMap(daLimitDao.findAllByDesignationIdAndStatus(designation.getId(), AppsConstants.Status.ACT)));

        StandardResponse<DADesignationRowResponse> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), row);
        log.info("END : updateDesignation | id={}", designationId);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<Void>> deleteDesignation(Integer designationId) throws ApiRequestException {
        log.info("START : deleteDesignation | id={}", designationId);
        if (designationId == null) {
            throw new ApiRequestException("Designation id cannot be null");
        }

        DADesignationMasterData designation = daDesignationMasterDao.findById(designationId)
                .orElseThrow(() -> new ApiRequestException(DESIGNATION_WITH + designationId + DOES_NOT_EXISTS));

        designation.setStatus(STATUS_INA);
        designation.setModifiedDate(new Date());
        daDesignationMasterDao.save(designation);

        List<DALimit> limits = daLimitDao.findAllByDesignationIdAndStatus(designationId, AppsConstants.Status.ACT);
        for (DALimit limit : limits) {
            limit.setStatus(AppsConstants.Status.INA);
            daLimitDao.save(limit);
        }

        StandardResponse<Void> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), null);
        log.info("END : deleteDesignation | id={}", designationId);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<StandardResponse<Void>> reorderDesignations(DAReorderRequest request) throws ApiRequestException {
        log.info("START : reorderDesignations");
        if (request == null || CollectionUtils.isEmpty(request.getItems())) {
            throw new ApiRequestException("Reorder items cannot be empty");
        }

        Date now = new Date();
        for (DAReorderItemRequest item : request.getItems()) {
            if (item.getDesignationId() == null || item.getDisplayOrder() == null) {
                throw new ApiRequestException("Each reorder item must include designationId and displayOrder");
            }
            DADesignationMasterData designation = daDesignationMasterDao.findById(item.getDesignationId())
                    .orElseThrow(() -> new ApiRequestException(DESIGNATION_WITH + item.getDesignationId() + DOES_NOT_EXISTS));
            designation.setDisplayOrder(item.getDisplayOrder());
            designation.setModifiedDate(now);
            daDesignationMasterDao.save(designation);
        }

        StandardResponse<Void> response =
                new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), null);
        log.info("END : reorderDesignations | count={}", request.getItems().size());
        return ResponseEntity.ok(response);
    }

    private List<DAHeaderResponse> buildHeaderTree(String tableType) {
        List<DATableHeader> headers = daLimitHeadingRepository.findByTableTypeOrderByDisplayOrderAsc(tableType.toUpperCase());

        Map<Long, DAHeaderResponse> headerMap = new LinkedHashMap<>();
        List<DAHeaderResponse> rootHeaders = new ArrayList<>();

        for (DATableHeader header : headers) {
            DAHeaderResponse response = new DAHeaderResponse();
            response.setId(header.getId());
            response.setLabel(header.getLabel());
            response.setLevelNo(header.getLevelNo());
            response.setDisplayOrder(header.getDisplayOrder());
            response.setColSpan(header.getColSpan());
            response.setRowSpan(header.getRowSpan());
            response.setChildren(new ArrayList<>());
            headerMap.put(header.getId(), response);
        }

        for (DATableHeader header : headers) {
            DAHeaderResponse current = headerMap.get(header.getId());
            if (header.getParent() == null) {
                rootHeaders.add(current);
            } else {
                DAHeaderResponse parent = headerMap.get(header.getParent().getId());
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

    private Map<Integer, Map<Integer, Double>> loadLimitsByDesignation(List<Integer> designationIds, String isCommittee) {
        Map<Integer, Map<Integer, Double>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(designationIds)) {
            return result;
        }

        List<DALimit> limits = daLimitDao.findAllByDesignationIdInAndStatus(designationIds, AppsConstants.Status.ACT);
        for (DALimit limit : limits) {
            if (StringUtils.hasText(isCommittee)
                    && StringUtils.hasText(limit.getIsCommittee())
                    && !isCommittee.equalsIgnoreCase(limit.getIsCommittee())) {
                continue;
            }
            result.computeIfAbsent(limit.getDesignationId(), key -> new HashMap<>())
                    .put(limit.getColumnId(), limit.getRiskValue());
        }
        return result;
    }

    private void saveOrReplaceLimits(Integer designationId, String isCommittee, List<DALimitValueRequest> limits) {
        daLimitDao.deleteAllByDesignationId(designationId);

        if (CollectionUtils.isEmpty(limits)) {
            return;
        }

        for (DALimitValueRequest limitRequest : limits) {
            if (limitRequest == null || limitRequest.getColumnId() == null) {
                continue;
            }
            DALimit limit = new DALimit();
            limit.setDesignationId(designationId);
            limit.setColumnId(limitRequest.getColumnId());
            limit.setRiskValue(limitRequest.getRiskValue());
            limit.setRiskRating(limitRequest.getRiskRating());
            limit.setIsCommittee(isCommittee);
            limit.setStatus(AppsConstants.Status.ACT);
            limit.setApproveStatus(MasterDataApproveStatus.APPROVED);
            daLimitDao.save(limit);
        }
    }

    private DADesignationRowResponse toRowResponse(DADesignationMasterData designation, Map<Integer, Double> values) {
        DADesignationRowResponse row = new DADesignationRowResponse();
        row.setDesignationId(designation.getId());
        row.setDesignation(designation.getDesignation());
        row.setDesignationCode(designation.getDesignationCode());
        row.setDescription(designation.getDescription());
        row.setDisplayOrder(designation.getDisplayOrder());
        row.setIsCommittee(designation.getIsCommittee());
        row.setStatus(designation.getStatus());
        row.setApproveStatus(designation.getApproveStatus());

        Map<String, Double> mappedValues = new LinkedHashMap<>();
        values.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> mappedValues.put(String.valueOf(entry.getKey()), entry.getValue()));
        row.setValues(mappedValues);
        return row;
    }

    private Map<Integer, Double> toValueMap(List<DALimit> limits) {
        Map<Integer, Double> values = new HashMap<>();
        for (DALimit limit : limits) {
            values.put(limit.getColumnId(), limit.getRiskValue());
        }
        return values;
    }

    private Integer resolveDisplayOrder(Integer requestedOrder, String isCommittee) {
        if (requestedOrder != null) {
            return requestedOrder;
        }
        return daDesignationMasterDao.findAllByIsCommitteeAndStatusOrderByDisplayOrderAsc(isCommittee, STATUS_ACT).stream()
                .map(DADesignationMasterData::getDisplayOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    private String resolveIsCommitteeFromRequest(DADesignationRequest request) {
        if (StringUtils.hasText(request.getIsCommittee())) {
            return request.getIsCommittee().trim().toUpperCase();
        }
        if (StringUtils.hasText(request.getTableType())) {
            return resolveIsCommittee(request.getTableType());
        }
        throw new ApiRequestException("Either isCommittee or tableType is required");
    }

    private String resolveIsCommittee(String tableType) {
        if (TABLE_TYPE_INDIVIDUAL.equalsIgnoreCase(tableType)) {
            return AppsConstants.YesNo.N.name();
        }
        return AppsConstants.YesNo.Y.name();
    }

    private void validateTableType(String tableType) {
        if (!StringUtils.hasText(tableType)) {
            throw new ApiRequestException("tableType cannot be empty");
        }
    }

    private void validateDesignationRequest(DADesignationRequest request, boolean isCreate) {
        if (request == null) {
            throw new ApiRequestException("Request body cannot be null");
        }
        if (isCreate && !StringUtils.hasText(request.getDesignation())) {
            throw new ApiRequestException("Designation name cannot be empty");
        }
        if (isCreate && !StringUtils.hasText(request.getIsCommittee()) && !StringUtils.hasText(request.getTableType())) {
            throw new ApiRequestException("Either isCommittee or tableType is required");
        }
    }
}
