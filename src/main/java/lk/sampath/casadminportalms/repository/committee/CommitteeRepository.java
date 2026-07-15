package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Integer> {}
