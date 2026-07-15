package lk.sampath.casadminportalms.repository.workflowtemplate;

import lk.sampath.casadminportalms.entity.workflowtemplate.WorkflowTemplateAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowTemplateAudRepository
    extends JpaRepository<WorkflowTemplateAud, Integer> {}
