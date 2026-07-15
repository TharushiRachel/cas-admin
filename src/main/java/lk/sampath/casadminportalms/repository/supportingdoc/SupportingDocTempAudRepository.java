package lk.sampath.casadminportalms.repository.supportingdoc;

import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDocAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingDocTempAudRepository extends JpaRepository<SupportingDocAud, Integer> {}
