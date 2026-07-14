package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.dto.creditfacilitytemplate.CftResponse;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CreditFacilityTemplateTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditFacilityTemplateTempRepository extends JpaRepository<CreditFacilityTemplateTemp, Integer>, QuerydslPredicateExecutor<CreditFacilityTemplateTemp> {
    @Query(value = "SELECT SEQ_T_CREDIT_FACILITY_TEMPLATE.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();

    @Query(
            value = """
        SELECT
            cft.CREDIT_FACILITY_TEMPLATE_ID AS creditFacilityTemplateID,
            cft.CREDIT_FACILITY_TYPE_ID AS creditFacilityTypeID,
            cft.CREDIT_FACILITY_NAME AS creditFacilityName,
            cftt.FACILITY_TYPE_NAME AS facilityTypeName,
            cft.DESCRIPTION AS description,
            cft.MAXIMUM_FACILITY_AMOUNT AS maxFacilityAmount,
            cft.MINIMUM_FACILITY_AMOUNT AS minFacilityAmount,
            cft.STATUS AS status,
            cft.APPROVE_STATUS AS approveStatus,
            cft.APPROVED_DATE AS approvedDate,
            cft.APPROVED_BY AS approvedBy,
            cft.CREATED_DATE AS createdDate,
            cft.CREATED_BY AS createdBy
        FROM CASV3_T_CREDIT_FACILITY_TEMPLATE_TEMP cft
        LEFT JOIN T_CREDIT_FACILITY_TYPE cftt
            ON cft.CREDIT_FACILITY_TYPE_ID = cftt.CREDIT_FACILITY_TYPE_ID
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM CASV3_T_CREDIT_FACILITY_TEMPLATE_TEMP cft
        """,
            nativeQuery = true
    )
    Page<CftResponse> findAllTemplates(Pageable pageable);
}
