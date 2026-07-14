package lk.sampath.casadminportalms.repository.upctemplate;

import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateResponse;
import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcTemplateTempRepository extends JpaRepository<UpcTemplateTemp, Integer> , QuerydslPredicateExecutor<UpcTemplateTemp> {
    @Query(value = "SELECT SEQ_T_UPC_TEMPLATE.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getCurrentSequenceValue();

    @Query(value = """
    SELECT
        u.UPC_TEMPLATE_ID AS upcTemplateID,
        u.TEMPLATE_NAME AS templateName,
        u.DESCRIPTION AS description,
        u.STATUS AS status,
        u.APPROVE_STATUS AS approveStatus,
        u.APPROVED_DATE AS approvedDate,
        u.APPROVED_BY AS approvedBy,
        u.CREATED_DATE AS createdDate,
        u.CREATED_BY AS createdBy
    FROM CASV3_T_UPC_TEMPLATE_TEMP u
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM CASV3_T_UPC_TEMPLATE_TEMP
    """,
            nativeQuery = true)
    Page<UpcTemplateResponse> findAllTemplates(Pageable pageable);

}
