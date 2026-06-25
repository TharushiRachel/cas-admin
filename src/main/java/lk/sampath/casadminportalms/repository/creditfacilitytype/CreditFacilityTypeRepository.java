package lk.sampath.casadminportalms.repository.creditfacilitytype;

import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author yomesh
 */

@Repository
public interface CreditFacilityTypeRepository extends JpaRepository<CreditFacilityType, Integer>, QuerydslPredicateExecutor<CreditFacilityType> {

    @Query( value = "SELECT * FROM T_CREDIT_FACILITY_TYPE WHERE approve_status = 'APPROVED'", nativeQuery = true)
    List<CreditFacilityType> findAllApprovedCreditFacilityTypes();
    boolean existsByFacilityTypeName(String facilityTypeName);

    boolean existsByCreditFacilityTypeID(Integer creditFacilityTypeID);

    @Query( value = "SELECT * FROM T_CREDIT_FACILITY_TYPE  WHERE FACILITY_TYPE_NAME = :facilityTypeName", nativeQuery = true)
    CreditFacilityType findCreditFacilityTypeByName( String facilityTypeName);

}