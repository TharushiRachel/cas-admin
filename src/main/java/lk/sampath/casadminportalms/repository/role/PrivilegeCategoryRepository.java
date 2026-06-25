package lk.sampath.casadminportalms.repository.role;

import lk.sampath.casadminportalms.entity.role.PrivilegeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeCategoryRepository extends JpaRepository<PrivilegeCategory, Integer>, CrudRepository<PrivilegeCategory, Integer> {
}
