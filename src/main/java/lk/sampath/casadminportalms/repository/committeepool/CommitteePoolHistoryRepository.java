package lk.sampath.casadminportalms.repository.committeepool;

import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteePoolHistoryRepository
    extends JpaRepository<CommitteePoolHistory, Integer> {}
