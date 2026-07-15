package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.CommitteeLevelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeLevelAudRepository
    extends JpaRepository<CommitteeLevelHistory, Integer> {}
