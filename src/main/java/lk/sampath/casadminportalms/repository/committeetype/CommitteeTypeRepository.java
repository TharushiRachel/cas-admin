package lk.sampath.casadminportalms.repository.committeetype;

import java.util.List;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeTypeRepository extends JpaRepository<CommitteeType, Integer> {

  @Query(
      value =
          "SELECT * FROM CA_COMMITTEE_TYPE_CONFIG WHERE STATUS = 'ACT' AND LOWER(TRIM(COMMITTEE_TYPE)) = :committeeType",
      nativeQuery = true)
  List<CommitteeType> findByCommitteeType(String committeeType);
}
