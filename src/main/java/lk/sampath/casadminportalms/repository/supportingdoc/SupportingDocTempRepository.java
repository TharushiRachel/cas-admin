package lk.sampath.casadminportalms.repository.supportingdoc;

import lk.sampath.casadminportalms.entity.supportingdoc.SupportingDocTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingDocTempRepository
    extends JpaRepository<SupportingDocTemp, Integer>,
        QuerydslPredicateExecutor<SupportingDocTemp> {

  @Query(value = "SELECT SEQ_T_UPC_TEMPLATE_DATA.NEXTVAL FROM DUAL", nativeQuery = true)
  Integer getCurrentSequenceValue();

  boolean existsByDocumentName(String documentName);
}
