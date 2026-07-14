package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DALimitAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DALimitAudRepository extends JpaRepository<DALimitAud, Integer> {

    @Query(value = "SELECT CASV3_SEQ_DA_LIMIT_AUD.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getCurrentSequenceValue();
}
