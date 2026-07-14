package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DALimitTemp;
import lk.sampath.casadminportalms.enums.AppsConstants;
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

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS_TEMP
                    WHERE DESIGNATION_ID = :designationId
                      AND STATUS = :status
                    """,
            nativeQuery = true
    )
    List<DALimitTemp> findAllByDesignationIdAndStatus(@Param("designationId") Integer designationId,
                                                      @Param("status") AppsConstants.Status status);

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS_TEMP
                    WHERE DESIGNATION_ID = :designationId
                      AND COLUMN_ID = :columnId
                      AND STATUS = :status
                    """,
            nativeQuery = true
    )
    DALimitTemp findByDesignationIdAndColumnIdAndStatus(@Param("designationId") Integer designationId,
                                                        @Param("columnId") Integer columnId,
                                                        @Param("status") AppsConstants.Status status);

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS_TEMP
                    WHERE STATUS = :status
                    """,
            nativeQuery = true
    )
    List<DALimitTemp> findAllByStatus(@Param("status") AppsConstants.Status status);

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS_TEMP
                    WHERE DESIGNATION_ID = :designationId
                      AND IS_COMMITTEE = :isCommittee
                      AND STATUS = :status
                    """,
            nativeQuery = true
    )
    List<DALimitTemp> findAllByDesignationIdAndIsCommitteeAndStatus(@Param("designationId") Integer designationId,
                                                                    @Param("isCommittee") String isCommittee,
                                                                    @Param("status") AppsConstants.Status status);

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

    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    DELETE FROM DA_LIMITS_TEMP
                    WHERE DESIGNATION_ID = :designationId
                    """,
            nativeQuery = true
    )
    void deleteByDesignationId(@Param("designationId") Integer designationId);
}
