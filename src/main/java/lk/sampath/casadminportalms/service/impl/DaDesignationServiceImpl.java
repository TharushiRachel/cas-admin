package lk.sampath.casadminportalms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DADesignationCodeDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeaderDTO;
import lk.sampath.casadminportalms.dto.dadesignation.DATableHeadingResponse;
import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import lk.sampath.casadminportalms.enums.DaTableType;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitHeadingRepository;
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

    public DaDesignationServiceImpl(DALimitHeadingRepository daLimitHeadingRepository) {
        this.daLimitHeadingRepository = daLimitHeadingRepository;
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
}
