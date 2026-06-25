package lk.sampath.casadminportalms.repository.role;

import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.entity.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, QuerydslPredicateExecutor<Role> {

    @Query( value = "SELECT * FROM T_ROLE WHERE approve_status = 'APPROVED'", nativeQuery = true)
    List<RoleDTO> findAllApprovedRoles();

}
