package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftInterestRateTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CftInterestRateTempRepository extends JpaRepository<CftInterestRateTemp, Integer> {

    @Query(value = "SELECT SEQ_T_CFT_INTEREST_RATE.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();


    List<CftInterestRateTemp> findAllByCreditFacilityTemplateCreditFacilityTemplateID(Integer creditFacilityTemplateID);
}
