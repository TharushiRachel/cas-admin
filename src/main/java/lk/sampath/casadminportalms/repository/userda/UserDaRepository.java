package lk.sampath.casadminportalms.repository.userda;

import lk.sampath.casadminportalms.entity.userda.UserDa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDaRepository extends JpaRepository<UserDa, Integer>,QuerydslPredicateExecutor<UserDa> {

    @Query(value = "SELECT * FROM T_USER_DA WHERE APPROVE_STATUS = 'APPROVED'", nativeQuery = true )
    List<UserDa> findAllApprovedUserDas();
}