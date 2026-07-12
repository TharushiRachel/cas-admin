package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DALimit;
import lk.sampath.casadminportalms.enums.AppsConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DALimitDao extends JpaRepository<DALimit, Integer> {

    List<DALimit> findAllByDesignationIdAndStatus(Integer designationId, AppsConstants.Status status);

    DALimit findByDesignationIdAndColumnIdAndStatus(Integer designationId, Integer columnId, AppsConstants.Status status);

    List<DALimit> findAllByStatus(AppsConstants.Status status);

    List<DALimit> findAllByIsCommitteeAndStatus(String isCommittee, AppsConstants.Status status);

    List<DALimit> findAllByDesignationIdInAndStatus(List<Integer> designationIds, AppsConstants.Status status);

    void deleteAllByDesignationId(Integer designationId);
}
