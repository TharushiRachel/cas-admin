package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DADesignationMasterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DADesignationMasterRepository extends JpaRepository<DADesignationMasterData, Integer> {

    @Query(
            value = """
                    SELECT *
                    FROM DA_DESIGNATION
                    WHERE STATUS = :status
                    """,
            nativeQuery = true
    )
    List<DADesignationMasterData> findAllByStatus(@Param("status") String status);

    @Query(
            value = """
                    SELECT *
                    FROM DA_DESIGNATION
                    WHERE IS_COMMITTEE = :isCommittee
                      AND STATUS = :status
                    ORDER BY DISPLAY_ORDER ASC
                    """,
            nativeQuery = true
    )
    List<DADesignationMasterData> findAllByIsCommitteeAndStatusOrderByDisplayOrderAsc(
            @Param("isCommittee") String isCommittee,
            @Param("status") String status);

    @Query(
            value = """
                    SELECT *
                    FROM DA_DESIGNATION
                    WHERE DESIGNATION_CODE = :designationCode
                      AND IS_COMMITTEE = :isCommittee
                      AND STATUS = :status
                    """,
            nativeQuery = true
    )
    Optional<DADesignationMasterData> findByDesignationCodeAndIsCommitteeAndStatus(
            @Param("designationCode") String designationCode,
            @Param("isCommittee") String isCommittee,
            @Param("status") String status);
}
