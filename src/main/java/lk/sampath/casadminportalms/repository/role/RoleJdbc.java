package lk.sampath.casadminportalms.repository.role;

import lk.sampath.casadminportalms.dto.role.PrivilegeDTO;
import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.dto.role.UpmRolePrivilegeDTO;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.entity.role.Privilege;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleJdbc {

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RoleJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<UpmRolePrivilegeDTO> getUserPrivilegesByUMPCode(String groupCode) {

        Map<String, Object> params = new HashMap<>();
        params.put("groupCode", groupCode);
        params.put("status", AppsConstants.Status.ACT.toString());

        StringBuilder SQL = new StringBuilder();
        SQL.append(" SELECT  \n");
        SQL.append(" PR.PRIVILEGE_CODE,    \n");
        SQL.append(" PR.RESTRICTED_SOL_IDS, \n");
        SQL.append(" PR.ALLOWED_SOL_IDS     \n");
        SQL.append(" FROM \n");
        SQL.append("   T_ROLE UR \n");
        SQL.append("   LEFT JOIN T_ROLE_PRIVILEGE RP ON RP.ROLE_ID = UR.ROLE_ID \n");
        SQL.append("   LEFT JOIN T_PRIVILEGE PR ON PR.PRIVILEGE_ID = RP.PRIVILEGE_ID \n");
        SQL.append(" WHERE  \n");
        SQL.append(" UR.STATUS = :status \n");
        SQL.append(" AND UR.UPM_PRIVILAGE_CODE = :groupCode \n");

        return namedParameterJdbcTemplate.query(SQL.toString(), params, new RowMapper<UpmRolePrivilegeDTO>() {
            @Nullable
            @Override
            public UpmRolePrivilegeDTO mapRow(ResultSet rs, int i) throws SQLException {
                UpmRolePrivilegeDTO privilege = new UpmRolePrivilegeDTO();
                privilege.setCode(rs.getString("PRIVILEGE_CODE"));
                privilege.setAllowedSolIds(rs.getString("ALLOWED_SOL_IDS"));
                privilege.setRestrictedSolIds(rs.getString("RESTRICTED_SOL_IDS"));
                return privilege;
            }
        });
    }
}
