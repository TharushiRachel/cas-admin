package lk.sampath.casadminportalms.repository.upmgroup;

import lk.sampath.casadminportalms.entity.upmgroup.UpmGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UpmGroupRepository extends JpaRepository<UpmGroup, Integer>, QuerydslPredicateExecutor<UpmGroup> {

    @Query( value = "SELECT * FROM t_upm_group WHERE approve_status = 'APPROVED'", nativeQuery = true)
    List<UpmGroup> findAllApprovedUpmGroups();
}

