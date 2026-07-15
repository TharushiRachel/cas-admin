package lk.sampath.casadminportalms.repository.role;

import lk.sampath.casadminportalms.entity.role.RoleTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTempRepository
    extends JpaRepository<RoleTemp, Integer>, QuerydslPredicateExecutor<RoleTemp> {

  @Query(value = "SELECT SEQ_T_ROLE.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();
}
