package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftCustomFacilityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CftCustomFacilityInfoRepository
    extends JpaRepository<CftCustomFacilityInfo, Integer> {
  List<CftCustomFacilityInfo> findAllByCreditFacilityTemplateCreditFacilityTemplateID(
      Integer creditFacilityTemplateID);
}
