package lk.sampath.casadminportalms.repository.deviation;

import lk.sampath.casadminportalms.entity.deviation.DeviationAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviationAudRepository extends JpaRepository<DeviationAud, Integer> {
}
