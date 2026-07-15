package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.LevelUserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelUserAudRepository extends JpaRepository<LevelUserHistory, Integer> {}
