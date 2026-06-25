package lk.sampath.casadminportalms.repository.supportingdoc;

import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportingDocRepository extends JpaRepository<SupportingDoc, Integer>, QuerydslPredicateExecutor<SupportingDoc> {

    @Query(value = "SELECT * FROM T_SUPPORTING_DOC WHERE APPROVE_STATUS = ('APPROVED')", nativeQuery = true )
    List<SupportingDoc> findAllSupportingDocs();
}
