package lk.sampath.casadminportalms.repository.committeepool;

import jakarta.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lk.sampath.casadminportalms.dto.committeepool.CommitteePoolDTO;
import lk.sampath.casadminportalms.entity.committee.Committee;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommitteePoolJdbc {

  protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public CommitteePoolJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public List<CommitteePoolDTO> findAllCommitteePoolTempList() {
    String sql = generateSQL(true);
    return namedParameterJdbcTemplate.query(sql, committeePoolRowMapper);
  }

  public List<CommitteePoolDTO> findAllCommitteePoolList() {
    String sql = generateSQL(false);
    return namedParameterJdbcTemplate.query(sql, committeePoolRowMapper);
  }

  private String generateSQL(boolean tempTable) {

    String table = tempTable ? "CA_POOL_CONFIG_TEMP" : "CA_POOL_CONFIG";

    return """
           SELECT USER_ID,
                  POOL_ID,
                  USER_NAME,
                  USER_DISPLAY_NAME,
                  APPROVED_DATE,
                  APPROVED_BY,
                  PARENT_REC_ID,
                  CREATED_DATE,
                  CREATED_BY,
                  MODIFIED_DATE,
                  MODIFIED_BY,
                  APPROVE_STATUS,
                  APPROVED_DATE,
                  USER_STATUS,
                  GROUP_CODE,
                  REFERENCE_NAME
           FROM %s
           """
        .formatted(table);
  }

  private final RowMapper<CommitteePoolDTO> committeePoolRowMapper =
      (rs, rowNum) -> {
        CommitteePoolDTO dto = new CommitteePoolDTO();
        dto.setUserId(rs.getInt("USER_ID"));
        dto.setCommitteePoolId(rs.getInt("POOL_ID"));
        dto.setUserName(rs.getString("USER_NAME"));
        dto.setUserDisplayName(rs.getString("USER_DISPLAY_NAME"));
        dto.setStatus(AppsConstants.Status.resolveStatus(rs.getString("USER_STATUS")));
        dto.setWorkClass(rs.getString("GROUP_CODE"));
        dto.setDesignation(rs.getString("REFERENCE_NAME"));
        dto.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
        dto.setCreatedBy(rs.getString("CREATED_BY"));
        dto.setLastModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
        dto.setModifiedBy(rs.getString("MODIFIED_BY"));
        dto.setApproveStatus(
            MasterDataApproveStatus.resolveApproveStatus(rs.getString("APPROVE_STATUS")));
        dto.setApprovedDate(rs.getTimestamp("APPROVED_DATE"));
        dto.setApprovedBy(rs.getString("APPROVED_BY"));
        dto.setParentRecId(rs.getInt("PARENT_REC_ID"));
        return dto;
      };

  public List<Committee> getCommitteesByUserName(String userName) {

    Map<String, Object> params = new HashMap<>();
    params.put("userName", userName);

    String sql =
        """
              SELECT
                  CAC.COMMITTEE_ID,
                  CAC.COMMITTEE_NAME
              FROM CA_USER_CONFIG CAU
              LEFT JOIN CA_COMMITTEE_CONFIG CAC
                  ON CAU.COMMITTEE_ID = CAC.COMMITTEE_ID
              WHERE CAU.USER_NAME = :userName
              """;

    return this.namedParameterJdbcTemplate.query(
        sql,
        params,
        new RowMapper<Committee>() {
          @Override
          public Committee mapRow(@Nullable ResultSet rs, int i) throws SQLException {
            Committee committee = new Committee();
            if (rs != null) {
              committee.setCommitteeId(rs.getInt("COMMITTEE_ID"));
              committee.setCommitteeName(rs.getString("COMMITTEE_NAME"));
            }
            return committee;
          }
        });
  }

  public Integer changeCommitteePaperCurrentLevel(
      Integer committeeID, AppsConstants.CAPathType path) {

    Map<String, Object> params = new HashMap<>();
    params.put("currentPath", path.toString());
    params.put("committeeID", committeeID);

    String sql =
        """
                      SELECT CA_WORKFLOW.CHANGE_COMMITTEE_PAPER_CURRENT_LEVEL(:committeeID, :currentPath) RETURN_CODE FROM DUAL
                          """;

    return namedParameterJdbcTemplate.query(
        sql,
        params,
        new ResultSetExtractor<Integer>() {
          @Override
          public Integer extractData(@Nullable ResultSet rs)
              throws SQLException, DataAccessException {
            Integer response = null;
            if (rs != null && rs.next()) {
              response = rs.getInt("RETURN_CODE");
            }
            return response;
          }
        });
  }
}
