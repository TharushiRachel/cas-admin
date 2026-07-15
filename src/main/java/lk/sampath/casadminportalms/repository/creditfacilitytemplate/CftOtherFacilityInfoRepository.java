package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CftOtherFacilityInfoRepository
    extends JpaRepository<CftOtherFacilityInformation, Integer> {
  List<CftOtherFacilityInformation> findAllByCreditFacilityTemplateCreditFacilityTemplateID(
      Integer creditFacilityTemplateID);
}
