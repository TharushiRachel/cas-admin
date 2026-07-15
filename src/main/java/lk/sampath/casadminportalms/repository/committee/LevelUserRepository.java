package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.entity.committee.LevelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelUserRepository extends JpaRepository<LevelUser, Integer> {

  void deleteByCommittee(Committee committee);
}
