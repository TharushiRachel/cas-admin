package lk.sampath.casadminportalms.repository.upctemplate;

import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcTemplateAudRepository extends JpaRepository<UpcTemplateAud, Integer>{
}
