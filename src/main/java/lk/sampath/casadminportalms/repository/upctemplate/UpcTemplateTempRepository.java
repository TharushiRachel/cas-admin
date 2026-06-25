package lk.sampath.casadminportalms.repository.upctemplate;

import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcTemplateTempRepository extends JpaRepository<UpcTemplateTemp, Integer> , QuerydslPredicateExecutor<UpcTemplateTemp> {
    @Query(value = "SELECT SEQ_T_UPC_TEMPLATE.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getCurrentSequenceValue();

}
