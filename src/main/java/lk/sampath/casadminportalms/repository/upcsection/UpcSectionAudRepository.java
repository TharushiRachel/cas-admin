package lk.sampath.casadminportalms.repository.upcsection;


import lk.sampath.casadminportalms.entity.upcsection.UpcSectionAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcSectionAudRepository extends JpaRepository<UpcSectionAud, Integer> {
}
