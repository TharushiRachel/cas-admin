package lk.sampath.casadminportalms.repository.upctemplate;

import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcTemplateRepository extends JpaRepository<UpcTemplate, Integer>, QuerydslPredicateExecutor<UpcTemplate> {
}
