package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import lk.sampath.casadminportalms.entity.daDesignation.DALimitTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DALimitTempRepository extends JpaRepository<DALimitTemp, Integer> {

    @Query(value = "SELECT SEQ_DA_LIMITS_TEMP.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getCurrentSequenceValue();

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS_TEMP
                    WHERE DESIGNATION_ID = :designationId
                    """,
            nativeQuery = true
    )
    List<DALimitTemp> findAllByDesignationId(@Param("designationId") Integer designationId);

    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    DELETE FROM DA_LIMITS_TEMP
                    WHERE DESIGNATION_ID = :designationId
                    """,
            nativeQuery = true
    )
    void deleteByDesignationId(@Param("designationId") Integer designationId);

    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    DELETE FROM DA_LIMITS_TEMP
                    WHERE DESIGNATION_ID = :designationId
                      AND IS_COMMITTEE = :isCommittee
                    """,
            nativeQuery = true
    )
    void deleteByDesignationIdAndIsCommittee(@Param("designationId") Integer designationId,
                                             @Param("isCommittee") String isCommittee);

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS
                    WHERE DESIGNATION_ID = :designationId
                      AND IS_COMMITTEE = :isCommittee
                      AND STATUS = :status
                    """,
            nativeQuery = true
    )
    List<DALimit> findAllByDesignationIdAndIsCommitteeAndStatus(@Param("designationId") Integer designationId,
                                                                @Param("isCommittee") String isCommittee,
                                                                @Param("status") String status);

}


