package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DALimitRepository extends JpaRepository<DALimit, Integer> {

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS
                    WHERE DESIGNATION_ID = :designationId
                      AND STATUS = :status
                    """,
            nativeQuery = true
    )
    List<DALimit> findAllByDesignationIdAndStatus(@Param("designationId") Integer designationId,
                                                  @Param("status") String status);

    @Query(
            value = """
                    SELECT *
                    FROM DA_LIMITS
                    WHERE STATUS = :status
                    ORDER BY DESIGNATION_ID ASC, IS_COMMITTEE ASC, COLUMN_ID ASC
                    """,
            nativeQuery = true
    )
    List<DALimit> findAllByStatus(@Param("status") String status);
}
