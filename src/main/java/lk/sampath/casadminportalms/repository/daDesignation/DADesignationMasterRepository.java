package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DADesignationMasterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DADesignationMasterRepository extends JpaRepository<DADesignationMasterData, Integer> {

    List<DADesignationMasterData> findAllByStatus(String status);

    List<DADesignationMasterData> findAllByIsCommitteeAndStatusOrderByDisplayOrderAsc(String isCommittee, String status);

    Optional<DADesignationMasterData> findByDesignationCodeAndIsCommitteeAndStatus(String designationCode, String isCommittee, String status);
}
