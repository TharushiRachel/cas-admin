package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftOtherFacilityInformationTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CftOtherFacilityInfoTempRepository extends JpaRepository<CftOtherFacilityInformationTemp, Integer> {
//    void deleteAllByCreditFacilityTemplateID(Integer creditFacilityTemplateID);

    @Query(value = "SELECT SEQ_T_OTHER_FACILITY_INFO.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();


    List<CftOtherFacilityInformationTemp> findAllByCreditFacilityTemplateCreditFacilityTemplateID(Integer creditFacilityTemplateID);
}
