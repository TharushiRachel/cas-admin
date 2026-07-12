package lk.sampath.casadminportalms.repository.daDesignation;

import lk.sampath.casadminportalms.entity.daDesignation.DATableHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DALimitHeadingRepository extends JpaRepository<DATableHeader, Long> {

    List<DATableHeader> findByTableTypeOrderByDisplayOrderAsc(String tableType);
}
