package lk.sampath.casadminportalms.repository.creditfacilitytemplate;

import lk.sampath.casadminportalms.entity.creditfacilitytemplate.CftSupportingDoc;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CftSupportingDocRepository extends JpaRepository<CftSupportingDoc, Integer> {
    List<CftSupportingDoc> findAllBySupportingDoc_SupportingDocIDAndApproveStatusAndStatus(Integer supportDocID, MasterDataApproveStatus approveStatus, Status status);

    List<CftSupportingDoc> findAllByCreditFacilityTemplateCreditFacilityTemplateID(Integer creditFacilityTemplateID);
}
