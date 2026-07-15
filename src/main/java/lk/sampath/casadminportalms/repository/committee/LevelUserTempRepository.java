package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.CommitteeTemp;
import lk.sampath.casadminportalms.entity.committee.LevelUserTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelUserTempRepository extends JpaRepository<LevelUserTemp, Integer> {

  @Query(value = "SELECT SEQ_CA_USER_CONFIG_TEMP.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();

  void deleteByCommitteeTemp(CommitteeTemp committeeTemp);
}
