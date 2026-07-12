package lk.sampath.casadminportalms.repository.deviation;

import lk.sampath.casadminportalms.entity.deviation.Deviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviationRepository extends JpaRepository<Deviation, Integer> {
}
