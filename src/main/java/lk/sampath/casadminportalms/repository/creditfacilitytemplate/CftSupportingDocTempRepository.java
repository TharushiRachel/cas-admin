package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftSupportingDocTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CftSupportingDocTempRepository extends JpaRepository<CftSupportingDocTemp, Integer> {

    @Query(value = "SELECT SEQ_T_CFT_SUPPORTING_DOC_TEMP.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();

    List<CftSupportingDocTemp> findAllByCreditFacilityTemplateCreditFacilityTemplateID(Integer creditFacilityTemplateID);
}
