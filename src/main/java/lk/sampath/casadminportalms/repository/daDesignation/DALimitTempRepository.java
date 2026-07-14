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

    @Query("select l from DALimitTemp l where l.designation.id = :designationId and l.status = :status")
    List<DALimitTemp> findAllByDesignationIdAndStatus(@Param("designationId") Integer designationId,
                                                      @Param("status") AppsConstants.Status status);

    @Query("select l from DALimitTemp l where l.designation.id = :designationId and l.columnId = :columnId and l.status = :status")
    DALimitTemp findByDesignationIdAndColumnIdAndStatus(@Param("designationId") Integer designationId,
                                                        @Param("columnId") Integer columnId,
                                                        @Param("status") AppsConstants.Status status);

    List<DALimitTemp> findAllByStatus(AppsConstants.Status status);

    @Query("""
            select l from DALimitTemp l
            where l.designation.id = :designationId
              and l.isCommittee = :isCommittee
              and l.status = :status
            """)
    List<DALimitTemp> findAllByDesignationIdAndIsCommitteeAndStatus(@Param("designationId") Integer designationId,
                                                                    @Param("isCommittee") String isCommittee,
                                                                    @Param("status") AppsConstants.Status status);

    @Modifying
    @Query("delete from DALimitTemp l where l.designation.id = :designationId and l.isCommittee = :isCommittee")
    void deleteByDesignationIdAndIsCommittee(@Param("designationId") Integer designationId,
                                             @Param("isCommittee") String isCommittee);
}
