package lk.sampath.casadminportalms.repository.upcsection;

import lk.sampath.casadminportalms.entity.upcsection.UpcSectionTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcSectionTempRepository extends JpaRepository<UpcSectionTemp, Integer>, QuerydslPredicateExecutor<UpcSectionTemp> {
    @Query(value = "SELECT SEQ_T_UPC_SECTION.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getCurrentSequenceValue();
}
