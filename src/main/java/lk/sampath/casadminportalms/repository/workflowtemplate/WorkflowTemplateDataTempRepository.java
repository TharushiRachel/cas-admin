package lk.sampath.casadminportalms.repository.workflowtemplate;

import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateDataTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkflowTemplateDataTempRepository extends JpaRepository<WorkflowTemplateDataTemp, Integer> {
    @Query(value = "SELECT SEQ_T_WORK_FLOW_DATA_TEMP.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer getNextSequenceValue();

    @Query(value = "SELECT * FROM T_WORK_FLOW_TEMPLATE_DATA_TEMP w WHERE w.APPROVE_STATUS NOT IN ('APPROVED') AND w.WORK_FLOW_TEMPLATE_ID =:workflowTemplateId", nativeQuery = true)
    List<WorkflowTemplateDataTemp> findAllTempWorkflowTemplateData(Integer workflowTemplateId);
}
