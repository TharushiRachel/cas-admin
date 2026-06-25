package lk.sampath.casadminportalms.repository.workflowtemplate;

import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkflowTemplateDataRepository extends JpaRepository<WorkflowTemplateData, Integer> {

    @Query(value = "SELECT SEQ_T_WORK_FLOW_DATA.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();

    @Query(value ="SELECT * FROM T_WORK_FLOW_TEMPLATE_DATA w WHERE w.APPROVE_STATUS IN ('APPROVED') AND w.WORK_FLOW_TEMPLATE_ID =:workflowTemplateId", nativeQuery = true)
    List<WorkflowTemplateData>  findAllWorkflowTemplateData(Integer workflowTemplateId);
}
