package lk.sampath.casadminportalms.repository.userda;

import lk.sampath.casadminportalms.entity.userda.UserDaAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDaAudRepository extends JpaRepository<UserDaAud, Integer> {
}
