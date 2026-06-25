package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CreditFacilityTemplate;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CreditFacilityTemplateRepository extends JpaRepository<CreditFacilityTemplate, Integer>, QuerydslPredicateExecutor<CreditFacilityTemplate> {
    List<CreditFacilityTemplate> findAllByCreditFacilityType_CreditFacilityTypeIDAndApproveStatusAndStatus(Integer creditFacilityTypeID, MasterDataApproveStatus approveStatus, AppsConstants.Status status);

    List<CreditFacilityTemplate> findAllByAndApproveStatus(MasterDataApproveStatus masterDataApproveStatus);
}
