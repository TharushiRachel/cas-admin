package lk.sampath.casadminportalms.repository.upmgroup;

import lk.sampath.casadminportalms.entity.upmgroup.UpmGroupTempAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpmGroupTempAudRepository extends JpaRepository<UpmGroupTempAud, Integer> {}
