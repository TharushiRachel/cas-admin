package lk.sampath.casadminportalms.repository.creditfacilitytype;

import lk.sampath.casadminportalms.entity.creditfacility.CreditFacilityTypeTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author yomesh
 */

@Repository
public interface CreditFacilityTypeTempRepository extends JpaRepository<CreditFacilityTypeTemp, Integer>, QuerydslPredicateExecutor<CreditFacilityTypeTemp> {


    @Query(value = "SELECT SEQ_T_CREDIT_FACILITY_TYPE.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getCurrentSequenceValue();

    boolean existsByFacilityTypeName(String facilityTypeName);

    boolean existsByCreditFacilityTypeID(Integer creditFacilityTypeID);
}