package lk.sampath.casadminportalms.repository.committeepool;

import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteePoolRepository extends JpaRepository<CommitteePool, Integer> {

  @Query(
      value =
          "SELECT * FROM CA_POOL_CONFIG WHERE USER_ID = :userId AND USER_STATUS = 'ACT' AND APPROVE_STATUS = 'APPROVED'",
      nativeQuery = true)
  CommitteePool findByUserId(Integer userId);
}
