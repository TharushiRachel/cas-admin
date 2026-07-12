package lk.sampath.casadminportalms.repository.deviation;

import lk.sampath.casadminportalms.entity.deviation.DeviationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviationTypeRepository extends JpaRepository<DeviationType, Integer> {
}
