package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DADesignationMasterData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DADesignationMasterDao extends JpaRepository<DADesignationMasterData, Integer> {

    List<DADesignationMasterData> findAllByStatus(String status);

    List<DADesignationMasterData> findAllByIsCommitteeAndStatusOrderByDisplayOrderAsc(String isCommittee, String status);

    Page<DADesignationMasterData> findAllByIsCommitteeAndStatus(String isCommittee, String status, Pageable pageable);
}
