package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftVitalInfoAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CftVitalInfoAudRepo extends JpaRepository<CftVitalInfoAud, Integer> {}
