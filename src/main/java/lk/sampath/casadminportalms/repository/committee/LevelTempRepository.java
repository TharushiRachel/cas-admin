package lk.sampath.casadminportalms.repository.committee;

import lk.sampath.casadminportalms.entity.committee.CommitteeLevelTemp;
import lk.sampath.casadminportalms.entity.committee.CommitteeTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelTempRepository extends JpaRepository<CommitteeLevelTemp, Integer> {

  @Query(value = "SELECT SEQ_CA_LEVEL_CONFIG_TEMP.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();

  void deleteByCommitteeTemp(CommitteeTemp committeeTemp);
}
