package lk.sampath.casadminportalms.repository.committeetype;

import lk.sampath.casadminportalms.entity.committeetype.CommitteeTypeAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeTypeAudRepository extends JpaRepository<CommitteeTypeAud, Integer> {}
