package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CreditFacilityTemplateTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditFacilityTemplateTempRepository extends JpaRepository<CreditFacilityTemplateTemp, Integer>, QuerydslPredicateExecutor<CreditFacilityTemplateTemp> {
    @Query(value = "SELECT SEQ_T_CREDIT_FACILITY_TEMPLATE.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();
}
