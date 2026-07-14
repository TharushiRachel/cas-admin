package lk.sampath.casadminportalms.repository.upctemplate;

import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateResponse;
import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcTemplateRepository extends JpaRepository<UpcTemplate, Integer>, QuerydslPredicateExecutor<UpcTemplate> {
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
    FROM T_UPC_TEMPLATE u
    ORDER BY
        CASE
            WHEN u.APPROVE_STATUS = 'APPROVED'
             AND u.STATUS = 'ACT'
            THEN 0
            ELSE 1
        END,
        u.APPROVED_DATE DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM T_UPC_TEMPLATE
    """,
            nativeQuery = true)
    Page<UpcTemplateResponse> findAllTemplates(Pageable pageable);

}
