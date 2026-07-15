package lk.sampath.casadminportalms.repository.upctemplate;

import java.util.List;
import lk.sampath.casadminportalms.entity.upctemplate.UpcTemplateDataTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcTemplateDataTempRepository extends JpaRepository<UpcTemplateDataTemp, Integer> {

  @Query(value = "SELECT SEQ_T_UPC_TEMPLATE_DATA.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();

  List<UpcTemplateDataTemp> findByUpcTemplateTempUpcTemplateID(Integer upcTemplateID);

  List<UpcTemplateDataTemp> deleteByUpcTemplateTempUpcTemplateID(Integer upcTemplateID);
}
