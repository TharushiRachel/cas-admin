package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CftOtherFacilityInfoRepository extends JpaRepository<CftOtherFacilityInformation, Integer> {
    List<CftOtherFacilityInformation> findAllByCreditFacilityTemplateCreditFacilityTemplateID(Integer creditFacilityTemplateID);
}
