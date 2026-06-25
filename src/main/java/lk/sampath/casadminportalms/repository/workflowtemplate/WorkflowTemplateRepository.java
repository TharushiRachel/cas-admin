package lk.sampath.casadminportalms.repository.workflowtemplate;

import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate, Integer> {

    @Query(value = "SELECT SEQ_T_WORKFLOW.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();

    @Query(value ="SELECT * FROM T_WORK_FLOW_TEMPLATE w WHERE w.APPROVE_STATUS IN ('APPROVED')", nativeQuery = true)
    List<WorkflowTemplate> findAllWorkflowTemplate();
}
