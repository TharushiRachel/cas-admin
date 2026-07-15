package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.entity.committee.CommitteeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<CommitteeLevel, Integer> {

  void deleteByCommittee(Committee committee);
}
