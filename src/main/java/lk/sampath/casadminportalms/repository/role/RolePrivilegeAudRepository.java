package lk.sampath.casadminportalms.repository.role;

import lk.sampath.casadminportalms.entity.role.RolePrivilegeAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePrivilegeAudRepository extends JpaRepository<RolePrivilegeAud, Integer> {
}
