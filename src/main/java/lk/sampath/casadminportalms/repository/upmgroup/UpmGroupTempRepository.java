package lk.sampath.casadminportalms.repository.upmgroup;

import java.util.List;
import lk.sampath.casadminportalms.entity.upmgroup.UpmGroupTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpmGroupTempRepository
    extends JpaRepository<UpmGroupTemp, Integer>, QuerydslPredicateExecutor<UpmGroupTemp> {

  @Query(value = "SELECT SEQ_T_UPM_GROUP.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();

  @Query(
      value = "SELECT * FROM t_upm_group_temp WHERE approve_status NOT IN ('APPROVED')",
      nativeQuery = true)
  List<UpmGroupTemp> findAllUpmGroupTemp();
}
