package lk.sampath.casadminportalms.repository.workflowtemplate;

import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowTemplateTempRepository extends JpaRepository<WorkflowTemplateTemp, Integer>{

    @Query(value = "SELECT SEQ_T_WORKFLOW_TEMP.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();

    @Query(value ="SELECT * FROM T_WORK_FLOW_TEMPLATE_TEMP w WHERE w.APPROVE_STATUS NOT IN ('APPROVED')", nativeQuery = true)
    Page<WorkflowTemplateTemp> findAllWorkflowTemplateTemp(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM T_WORK_FLOW_TEMPLATE_TEMP WHERE STATUS = 'ACT' AND WORK_FLOW_TEMPLATE_NAME = :templateName ", nativeQuery = true)
    Integer getWorkflowTempCountByName(String templateName);
}
