package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftVitalInfoTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CftVitalInfoTempRepository extends JpaRepository<CftVitalInfoTemp, Integer> {

  @Query(value = "SELECT SEQ_T_CFT_VITAL_INFO.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getNextSequenceValue();

  List<CftVitalInfoTemp> findAllByCreditFacilityTemplateCreditFacilityTemplateID(
      Integer creditFacilityTemplateID);
}
