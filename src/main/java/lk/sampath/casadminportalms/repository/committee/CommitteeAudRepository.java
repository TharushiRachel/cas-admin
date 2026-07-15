package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.CommitteeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeAudRepository extends JpaRepository<CommitteeHistory, Integer> {}
