package lk.sampath.casadminportalms.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateDTO;
import lk.sampath.casadminportalms.dto.workflowtemplate.WorkflowTemplateRequest;
import lk.sampath.casadminportalms.enums.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public class WorkflowTemplateRepositoryJdbc {

  protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public WorkflowTemplateRepositoryJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public List<WorkflowTemplateDTO> getWorkflowTemplate(WorkflowTemplateRequest request) {
    Map<String, Object> params = new HashMap<>();

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT \n");
    sql.append("wft.WORK_FLOW_TEMPLATE_ID, \n");
    sql.append("wft.WORK_FLOW_TEMPLATE_NAME, \n");
    sql.append("wft.CODE, \n");
    sql.append("wft.DESCRIPTION, \n");
    sql.append("wft.STATUS, \n");
    sql.append("wft.APPROVE_STATUS, \n");
    sql.append("wft.APPROVED_BY, \n");
    sql.append("wft.APPROVED_DATE, \n");
    sql.append("wft.CREATED_DATE,       \n");
    sql.append("wft.CREATED_BY,          \n");
    sql.append("wft.MODIFIED_DATE,       \n");
    sql.append("wft.MODIFIED_BY          \n");
    sql.append("FROM T_WORK_FLOW_TEMPLATE wft \n");
    sql.append("WHERE wft.WORK_FLOW_TEMPLATE_ID IS NOT NULL \n");

    if (request.getWorkFlowTemplateID() != null) {
      sql.append("AND wft.WORK_FLOW_TEMPLATE_ID =:workFlowTemplateID \n");
      params.put("workFlowTemplateID", request.getWorkFlowTemplateID());
    }
    if (StringUtils.isNotEmpty(request.getWorkFlowTemplateName())) {
      sql.append("AND upper(wft.WORK_FLOW_TEMPLATE_NAME) LIKE '%")
          .append(request.getWorkFlowTemplateName().toUpperCase())
          .append("%' ");
    }
    if (StringUtils.isNotEmpty(request.getCode())) {
      sql.append("AND upper(wft.CODE) LIKE '%")
          .append(request.getCode().toUpperCase())
          .append("%' ");
    }
    if (StringUtils.isNotEmpty(request.getDescription())) {
      sql.append("AND upper(wft.DESCRIPTION) LIKE '%")
          .append(request.getDescription().toUpperCase())
          .append("%' ");
    }
    if (request.getApproveStatusList() != null && !request.getApproveStatusList().isEmpty()) {
      sql.append("AND wft.APPROVE_STATUS IN (")
          .append(buildSQLINQuery(request.getApproveStatusList()))
          .append(") ");
    }
    if (request.getStatus() != null) {
      sql.append("AND wft.STATUS =:status ");
      params.put("status", request.getStatus().toString());
    }

    sql.append("ORDER BY wft.WORK_FLOW_TEMPLATE_NAME DESC ");

    return namedParameterJdbcTemplate.query(
        sql.toString(),
        params,
        new RowMapper<WorkflowTemplateDTO>() {
          @Nullable
          @Override
          public WorkflowTemplateDTO mapRow(ResultSet rs, int i) throws SQLException {
            WorkflowTemplateDTO workflowTemplateDTO = null;
            workflowTemplateDTO.setWorkFlowTemplateId(rs.getInt("WORK_FLOW_TEMPLATE_ID"));
            workflowTemplateDTO.setWorkFlowTemplateName(rs.getString("WORK_FLOW_TEMPLATE_NAME"));
            workflowTemplateDTO.setCode(rs.getString("CODE"));
            workflowTemplateDTO.setDescription(rs.getString("DESCRIPTION"));
            workflowTemplateDTO.setStatus(Status.resolveStatus(rs.getString("STATUS")));
            return workflowTemplateDTO;
          }
        });
  }

  public static <T> String buildSQLINQuery(Collection<T> values) {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for (T val : values) {
      sb.append("'");
      sb.append(val);
      sb.append("'");
      if (i != values.size() - 1) sb.append(",");

      i++;
    }
    return sb.toString();
  }
}
