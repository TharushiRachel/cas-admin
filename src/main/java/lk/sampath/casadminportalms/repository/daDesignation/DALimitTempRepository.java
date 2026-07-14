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

    List<DALimitTemp> findAllByDesignationIdAndStatus(Integer designationId, AppsConstants.Status status);

    DALimitTemp findByDesignationIdAndColumnIdAndStatus(Integer designationId, Integer columnId, AppsConstants.Status status);

    List<DALimitTemp> findAllByStatus(AppsConstants.Status status);

    List<DALimitTemp> findAllByDesignationIdAndIsCommitteeAndStatus(Integer designationId,
                                                                    String isCommittee,
                                                                    AppsConstants.Status status);

    @Modifying
    @Query("delete from DALimitTemp l where l.designationId = :designationId and l.isCommittee = :isCommittee")
    void deleteByDesignationIdAndIsCommittee(@Param("designationId") Integer designationId,
                                             @Param("isCommittee") String isCommittee);
}
