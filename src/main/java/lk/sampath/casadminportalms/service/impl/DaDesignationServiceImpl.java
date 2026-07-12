package lk.sampath.casadminportalms.service.impl;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.dadesignation.DAHeaderResponse;
import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.daDesignation.DALimitHeadingRepository;
import lk.sampath.casadminportalms.service.DaDesignationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class DaDesignationServiceImpl implements DaDesignationService {

    private final DALimitHeadingRepository daLimitHeadingRepository;

    public DaDesignationServiceImpl(DALimitHeadingRepository daLimitHeadingRepository) {
        this.daLimitHeadingRepository = daLimitHeadingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<DAHeaderResponse>>> getAllLimitHeadings(String tableType) throws ApiRequestException {

        log.info("START : DaDesignationServiceImpl | getAllLimitHeadings | Fetching all DA Limit Headings with their subheadings from the database.");
        List<DATableHeader> headers =
                daLimitHeadingRepository.findAll();

        log.info("Fetched {} headers from the database for tableType: {}", headers.size(), tableType);

        Map<Long, DAHeaderResponse> headerMap = new LinkedHashMap<>();
        List<DAHeaderResponse> rootHeaders = new ArrayList<>();

        // Create DTOs
        for (DATableHeader header : headers) {

            DAHeaderResponse response = new DAHeaderResponse();
            response.setId(header.getId());
            response.setLabel(header.getLabel());
            response.setLevelNo(header.getLevelNo());
            response.setColSpan(header.getColSpan());
            response.setRowSpan(header.getRowSpan());
            response.setChildren(new ArrayList<>());

            headerMap.put(header.getId(), response);
        }

        // Build Tree
        for (DATableHeader header : headers) {

            DAHeaderResponse current = headerMap.get(header.getId());

            if (header.getParent() == null) {
                rootHeaders.add(current);
            } else {

                DAHeaderResponse parent =
                        headerMap.get(header.getParent().getId());

                if (parent != null) {
                    parent.getChildren().add(current);
                }
            }
        }
        log.info("END : DaDesignationServiceImpl | getAllLimitHeadings | Successfully fetched all DA Limit Headings with their subheadings from the database. {}", rootHeaders.size());
        StandardResponse<List<DAHeaderResponse>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), rootHeaders);
        return ResponseEntity.ok(response);
    }
}
