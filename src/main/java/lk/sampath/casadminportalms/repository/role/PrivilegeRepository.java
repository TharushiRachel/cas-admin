package lk.sampath.casadminportalms.repository.role;

import java.util.List;
import java.util.Set;
import lk.sampath.casadminportalms.entity.role.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
  List<Privilege> findByPrivilegeCategoryPrivilegeCategoryID(Integer privilegeCategoryID);

  Set<Privilege> findByPrivilegeIDIn(List<Integer> privilegeIDs);
}
