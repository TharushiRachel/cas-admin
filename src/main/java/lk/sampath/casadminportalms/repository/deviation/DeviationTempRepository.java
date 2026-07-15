package lk.sampath.casadminportalms.repository.deviation;

import lk.sampath.casadminportalms.entity.deviation.TempDeviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviationTempRepository extends JpaRepository<TempDeviation, Integer> {

  @Query(value = "SELECT SEQ_T_DEVIATIONS.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();
}
