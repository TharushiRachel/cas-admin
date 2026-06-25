package lk.sampath.casadminportalms.repository;

import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteePoolRepository  extends JpaRepository<CommitteePool, Integer> {
}
