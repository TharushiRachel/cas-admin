package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftSupportingDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CftSupportingDocRepository extends JpaRepository<CftSupportingDoc, Integer> {
  List<CftSupportingDoc> findAllByCreditFacilityTemplateCreditFacilityTemplateID(
      Integer creditFacilityTemplateID);
}
