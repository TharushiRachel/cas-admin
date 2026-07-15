package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftVitalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CftVitalInfoRepository extends JpaRepository<CftVitalInfo, Integer> {
  @Query(value = "SELECT SEQ_T_CFT_VITAL_INFO.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getNextSequenceValue();

  List<CftVitalInfo> findAllByCreditFacilityTemplateCreditFacilityTemplateID(
      Integer creditFacilityTemplateID);
}
