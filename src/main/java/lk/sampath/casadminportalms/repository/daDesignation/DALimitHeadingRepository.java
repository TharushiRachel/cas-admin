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
        WHERE TABLE_TYPE = :tableType
        """,
            nativeQuery = true
    )
    List<DATableHeader> findByTableType(@Param("tableType") String tableType);
}
