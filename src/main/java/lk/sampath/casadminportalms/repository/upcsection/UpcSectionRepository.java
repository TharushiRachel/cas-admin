package lk.sampath.casadminportalms.repository.upcsection;

import java.util.List;
import lk.sampath.casadminportalms.entity.upcsection.UpcSection;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcSectionRepository
    extends JpaRepository<UpcSection, Integer>, QuerydslPredicateExecutor<UpcSection> {
  List<UpcSection> findByStatusAndApproveStatus(
      Status status, MasterDataApproveStatus masterDataApproveStatus);
}
