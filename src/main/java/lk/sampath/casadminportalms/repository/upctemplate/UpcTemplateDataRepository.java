package lk.sampath.casadminportalms.repository.upctemplate;

import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpcTemplateDataRepository extends JpaRepository<UpcTemplateData, Integer>, QuerydslPredicateExecutor<UpcTemplateData> {

    List<UpcTemplateData> findByUpcTemplateUpcTemplateID(Integer upcTemplateID);
}
