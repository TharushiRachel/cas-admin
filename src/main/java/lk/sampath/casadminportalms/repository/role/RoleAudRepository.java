package lk.sampath.casadminportalms.repository.role;

import java.util.Date;
import lk.sampath.casadminportalms.entity.role.RoleAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleAudRepository extends JpaRepository<RoleAud, Integer> {
  @Modifying
  @Query(
      value = "UPDATE T_ROLE_PRIVILEGE_AUD SET ACTION_DATE = :actionDate WHERE ROLE_ID = :roleId",
      nativeQuery = true)
  void updateActionDate(@Param("roleId") Integer roleId, @Param("actionDate") Date actionDate);
}
