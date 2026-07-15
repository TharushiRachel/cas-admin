package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DADesignationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DADesignationRepository extends JpaRepository<DADesignationData,Integer> {

    @Query(
            value = """
                    SELECT *
                    FROM DA_DESIGNATION
                    WHERE STATUS = :status
                    ORDER BY DISPLAY_ORDER ASC
                    """,
            nativeQuery = true
    )
    List<DADesignationData> findAllByStatus(@Param("status") String status);

    /**
     * Lightweight scalar lookup for the next display order, avoiding a full table
     * fetch (used when auto-assigning display order for newly created designations).
     */
    @Query(
            value = """
                    SELECT MAX(DISPLAY_ORDER)
                    FROM DA_DESIGNATION
                    WHERE STATUS = :status
                    """,
            nativeQuery = true
    )
    Integer findMaxDisplayOrderByStatus(@Param("status") String status);

    /**
     * One designation master per code (shared by committee and individual limit rows).
     */
    @Query(
            value = """
                    SELECT *
                    FROM DA_DESIGNATION
                    WHERE DESIGNATION_CODE = :designationCode
                      AND STATUS = :status
                    FETCH FIRST 1 ROW ONLY
                    """,
            nativeQuery = true
    )
    Optional<DADesignationData> findByDesignationCodeAndStatus(
            @Param("designationCode") String designationCode,
            @Param("status") String status);
}
