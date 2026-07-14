package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import lk.sampath.casadminportalms.enums.AppsConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DALimitRepository extends JpaRepository<DALimit, Integer> {

    List<DALimit> findAllByDesignationIdAndStatus(Integer designationId, AppsConstants.Status status);

    DALimit findByDesignationIdAndColumnIdAndStatus(Integer designationId, Integer columnId, AppsConstants.Status status);

    List<DALimit> findAllByStatus(AppsConstants.Status status);

    List<DALimit> findAllByDesignationIdAndIsCommitteeAndStatus(Integer designationId, String isCommittee, AppsConstants.Status status);

    @Modifying
    @Query("delete from DALimit l where l.designationId = :designationId and l.isCommittee = :isCommittee")
    void deleteByDesignationIdAndIsCommittee(@Param("designationId") Integer designationId,
                                             @Param("isCommittee") String isCommittee);
}
