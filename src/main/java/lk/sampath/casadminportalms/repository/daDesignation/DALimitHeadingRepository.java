package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DALimitHeadingRepository extends JpaRepository<DATableHeader, Long> {

    @Query(
            value = """
                    SELECT *
                    FROM CASV3_T_DA_TABLE_HEADER
                    ORDER BY DISPLAY_ORDER ASC
                    """,
            nativeQuery = true
    )
    List<DATableHeader> findAllOrderByDisplayOrderAsc();

    @Query(
            value = """
                    SELECT *
                    FROM CASV3_T_DA_TABLE_HEADER
                    WHERE TABLE_TYPE = :tableType
                      AND SUB_ID IS NOT NULL
                    ORDER BY DISPLAY_ORDER ASC
                    """,
            nativeQuery = true
    )
    List<DATableHeader> findByTableTypeAndSubIdIsNotNull(@Param("tableType") String tableType);

}
