package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformationAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CftOtherFacilityInfoAudRepo
    extends JpaRepository<CftOtherFacilityInformationAud, Integer> {}
