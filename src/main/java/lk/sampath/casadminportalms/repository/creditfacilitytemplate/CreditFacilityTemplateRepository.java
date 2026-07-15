package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.dto.creditfacilitytemplate.CftResponse;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.CreditFacilityTemplateDTO;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CreditFacilityTemplate;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditFacilityTemplateRepository extends JpaRepository<CreditFacilityTemplate, Integer>, QuerydslPredicateExecutor<CreditFacilityTemplate> {

    @Query(
            value = """
        SELECT
            t.CREDIT_FACILITY_TEMPLATE_ID AS creditFacilityTemplateID,
            t.CREDIT_FACILITY_TYPE_ID AS creditFacilityTypeID,
            t.CREDIT_FACILITY_NAME AS creditFacilityName,
            ft.FACILITY_TYPE_NAME AS facilityTypeName,
            t.DESCRIPTION AS description,
            t.MAXIMUM_FACILITY_AMOUNT AS maxFacilityAmount,
            t.MINIMUM_FACILITY_AMOUNT AS minFacilityAmount,
            t.STATUS AS status,
            t.APPROVE_STATUS AS approveStatus,
            t.APPROVED_DATE AS approvedDate,
            t.APPROVED_BY AS approvedBy,
            t.CREATED_DATE AS createdDate,
            t.CREATED_BY AS createdBy
        FROM T_CREDIT_FACILITY_TEMPLATE t
        JOIN T_CREDIT_FACILITY_TYPE ft
            ON ft.CREDIT_FACILITY_TYPE_ID = t.CREDIT_FACILITY_TYPE_ID
        ORDER BY
            CASE
                WHEN t.APPROVE_STATUS = 'APPROVED'
                 AND t.STATUS = 'ACT'
                THEN 0
                ELSE 1
            END,
            t.APPROVED_DATE DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM T_CREDIT_FACILITY_TEMPLATE t
        """,
            nativeQuery = true
    )
    Page<CftResponse> findAllTemplates(Pageable pageable);
}
