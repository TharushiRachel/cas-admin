package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CreditFacilityTemplateAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditFacilityTemplateAudRepo
    extends JpaRepository<CreditFacilityTemplateAud, Integer> {}
