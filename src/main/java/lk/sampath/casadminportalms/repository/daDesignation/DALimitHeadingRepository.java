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
                SELECT h
                FROM DATableHeader h
                ORDER BY h.displayOrder ASC
                """,
            nativeQuery = false
    )
    List<DATableHeader> findAllOrderByDisplayOrderAsc();

    List<DATableHeader> findByTableTypeAndSubIdIsNotNull(String tableType);

    @Query("""
            SELECT h FROM DATableHeader h
            WHERE h.tableType = :tableType
              AND h.subId = :subId
            """)
    List<DATableHeader> findByTableTypeAndSubId(@Param("tableType") String tableType,
                                                @Param("subId") Integer subId);
}
