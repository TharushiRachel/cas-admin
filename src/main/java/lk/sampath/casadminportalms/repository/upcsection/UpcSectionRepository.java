package lk.sampath.casadminportalms.repository.upcsection;

import lk.sampath.casadminportalms.entity.upcsection.UpcSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpcSectionRepository extends JpaRepository<UpcSection, Integer>, QuerydslPredicateExecutor<UpcSection> {

    @Query(value = "SELECT * FROM T_UPC_SECTION WHERE APPROVE_STATUS = 'APPROVED'", nativeQuery = true )
    List<UpcSection> findAllApprovedUpcSections();
}
