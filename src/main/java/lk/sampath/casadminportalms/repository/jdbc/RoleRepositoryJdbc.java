package lk.sampath.casadminportalms.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryJdbc {

  @Autowired protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<RoleDTO> viewAllRolesTemp(boolean isTemp, boolean excludeApproved) {
    Map<String, Object> params = new HashMap<>();

    StringBuilder SQL = new StringBuilder();
    SQL.append("SELECT r.role_id, \n");
    SQL.append("  r.role_name, \n");
    SQL.append("  r.UPM_PRIVILAGE_CODE, \n");
    SQL.append("  r.status, \n");
    SQL.append("r.APPROVE_STATUS, \n");
    SQL.append("r.APPROVED_BY, \n");
    SQL.append("r.APPROVED_DATE \n");
    SQL.append("FROM ").append(isTemp ? "t_role_temp" : "t_role").append(" r \n");
    SQL.append("WHERE r.role_id IS NOT NULL \n");

    if (isTemp && excludeApproved) {
      SQL.append("  AND r.APPROVE_STATUS NOT IN ('APPROVED') \n");
    }

    SQL.append("ORDER BY r.role_name");

    return namedParameterJdbcTemplate.query(
        SQL.toString(),
        params,
        new RowMapper<RoleDTO>() {
          @Nullable
          @Override
          public RoleDTO mapRow(ResultSet rs, int i) throws SQLException {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleID(rs.getInt("role_id"));
            roleDTO.setRoleName(rs.getString("role_name"));
            roleDTO.setUpmPrivilegeCode(rs.getString("UPM_PRIVILAGE_CODE"));
            roleDTO.setApproveStatus(
                MasterDataApproveStatus.valueOf(rs.getString("APPROVE_STATUS")));
            roleDTO.setApprovedBy(rs.getString("APPROVED_BY"));
            roleDTO.setStatus(Status.resolveStatus(rs.getString("status")));
            return roleDTO;
          }
        });
  }
}
