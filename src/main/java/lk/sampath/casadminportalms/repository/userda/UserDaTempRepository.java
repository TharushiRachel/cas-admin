package lk.sampath.casadminportalms.repository.userda;

import lk.sampath.casadminportalms.entity.userda.UserDaTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDaTempRepository
    extends JpaRepository<UserDaTemp, Integer>, QuerydslPredicateExecutor<UserDaTemp> {

  @Query(value = "SELECT SEQ_T_USER_DA.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();
}
