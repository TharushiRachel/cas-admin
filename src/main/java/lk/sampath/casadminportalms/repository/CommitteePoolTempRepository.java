package lk.sampath.casadminportalms.repository;

import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteePoolTempRepository extends JpaRepository<CommitteePoolTemp, Integer> {
}
