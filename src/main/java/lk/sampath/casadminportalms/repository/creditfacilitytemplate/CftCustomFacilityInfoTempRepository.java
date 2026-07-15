package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftCustomFacilityInfoTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CftCustomFacilityInfoTempRepository
    extends JpaRepository<CftCustomFacilityInfoTemp, Integer> {
  //    void deleteAllByCreditFacilityTemplateID(Integer creditFacilityTemplateID);

  @Query(value = "SELECT SEQ_T_CUSTOM_FACILITY_INFO.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getNextSequenceValue();

  List<CftCustomFacilityInfoTemp> findAllByCreditFacilityTemplateCreditFacilityTemplateID(
      Integer creditFacilityTemplateID);
}
