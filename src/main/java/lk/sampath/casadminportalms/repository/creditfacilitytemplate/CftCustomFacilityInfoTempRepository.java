package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftCustomFacilityInfoTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CftCustomFacilityInfoTempRepository extends JpaRepository<CftCustomFacilityInfoTemp, Integer> {
//    void deleteAllByCreditFacilityTemplateID(Integer creditFacilityTemplateID);

    @Query(value = "SELECT SEQ_T_CFT_INTEREST_RATE_TEMP.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();

    List<CftCustomFacilityInfoTemp> findAllByCreditFacilityTemplateCreditFacilityTemplateID(Integer creditFacilityTemplateID);
}
