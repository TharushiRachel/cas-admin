package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftInterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CftInterestRateRepository extends JpaRepository<CftInterestRate, Integer> {

  @Query(value = "SELECT SEQ_T_CFT_INTEREST_RATE.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getNextSequenceValue();

  List<CftInterestRate> findAllByCreditFacilityTemplateCreditFacilityTemplateID(
      Integer creditFacilityTemplateID);
}
