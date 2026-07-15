package lk.sampath.casadminportalms.repository.upmgroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lk.sampath.casadminportalms.dto.upmgroup.UpmGroupDTO;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UpmGroupJdbc {

  protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public UpmGroupJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public List<UpmGroupDTO> findAllUpmGroupTempList() {
    String sql = generateSQL(true);
    return namedParameterJdbcTemplate.query(sql, upmGroupRowMapper);
  }

  public List<UpmGroupDTO> findAllUpmGroupList() {
    String sql = generateSQL(false);
    return namedParameterJdbcTemplate.query(sql, upmGroupRowMapper);
  }

  public UpmGroupDTO findUpmGroupTempById(int upmGroupID) {
    Map<String, Object> params = new HashMap<>();
    params.put("upmGroupID", upmGroupID);
    String sql = generateSQL(true) + " WHERE UPM_GROUP_ID = :upmGroupID";
    return namedParameterJdbcTemplate.queryForObject(sql, params, upmGroupRowMapper);
  }

  public UpmGroupDTO findUpmGroupById(int upmGroupID) {
    Map<String, Object> params = new HashMap<>();
    params.put("upmGroupID", upmGroupID);
    String sql = generateSQL(false) + " WHERE UPM_GROUP_ID = :upmGroupID";
    return namedParameterJdbcTemplate.queryForObject(sql, params, upmGroupRowMapper);
  }

  public List<UpmGroupDTO> findAllActiveUpmGroups() {
    Map<String, Object> params = new HashMap<>();
    params.put("status", AppsConstants.Status.ACT.toString());
    params.put("approveStatus", MasterDataApproveStatus.APPROVED.toString());
    String sql = generateSQL(false) + " WHERE STATUS = :status AND APPROVE_STATUS = :approveStatus";
    return namedParameterJdbcTemplate.query(sql, params, upmGroupRowMapper);
  }

  private String generateSQL(boolean tempTable) {

    String table = tempTable ? "CASV3_T_UPM_GROUP_TEMP" : "T_UPM_GROUP";

    return """
           SELECT UPM_GROUP_ID,
                  GROUP_CODE,
                  REFERENCE_NAME,
                  WORK_FLOW_LEVEL,
                  STATUS,
                  CREATED_DATE,
                  CREATED_BY,
                  MODIFIED_DATE,
                  MODIFIED_BY,
                  APPROVE_STATUS,
                  APPROVED_DATE,
                  APPROVED_BY
           FROM %s
           """
        .formatted(table);
  }

  private final RowMapper<UpmGroupDTO> upmGroupRowMapper =
      (rs, rowNum) -> {
        UpmGroupDTO dto = new UpmGroupDTO();
        dto.setUpmGroupID(rs.getInt("UPM_GROUP_ID"));
        dto.setGroupCode(rs.getString("GROUP_CODE"));
        dto.setReferenceName(rs.getString("REFERENCE_NAME"));
        dto.setWorkFlowLevel(rs.getInt("WORK_FLOW_LEVEL"));
        dto.setStatus(Status.resolveStatus(rs.getString("STATUS")));
        dto.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
        dto.setCreatedBy(rs.getString("CREATED_BY"));
        dto.setLastModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
        dto.setModifiedBy(rs.getString("MODIFIED_BY"));
        dto.setApproveStatus(
            MasterDataApproveStatus.resolveApproveStatus(rs.getString("APPROVE_STATUS")));
        dto.setApprovedDate(rs.getTimestamp("APPROVED_DATE"));
        dto.setApprovedBy(rs.getString("APPROVED_BY"));
        return dto;
      };
}
