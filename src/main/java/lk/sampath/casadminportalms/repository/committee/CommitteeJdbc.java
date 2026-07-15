package lk.sampath.casadminportalms.repository.committee;

import jakarta.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import lk.sampath.casadminportalms.dto.committee.CommitteeDTO;
import lk.sampath.casadminportalms.dto.committee.CommitteeLevelDTO;
import lk.sampath.casadminportalms.dto.committee.LevelUserDTO;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
public class CommitteeJdbc {

  protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private static final String COMMITTEE_ID = "COMMITTEE_ID";

  private static final String LEVEL_CONFIG_ID = "LEVEL_CONFIG_ID";

  public CommitteeJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public List<CommitteeDTO> getAllCommittees(boolean tempTable) throws ApiRequestException {

    try {
      String sql = generateSQLForAllView(tempTable);

      return namedParameterJdbcTemplate.query(
          sql,
          rs -> {
            Map<Integer, CommitteeDTO> committeeMap = new LinkedHashMap<>();
            while (rs.next()) {
              Integer committeeId = rs.getInt(COMMITTEE_ID);
              committeeMap.computeIfAbsent(committeeId, id -> mapCommittee(rs));
            }

            return new ArrayList<>(committeeMap.values());
          });

    } catch (Exception e) {
      log.error("ERROR : CommitteeJdbc | getAllCommittees :", e);
      throw new ApiRequestException("Failed to retrieve committees.");
    }
  }

  public CommitteeDTO getCommittee(Integer committeeId, boolean tempTable) {

    try {
      Map<String, Object> params = new HashMap<>();
      params.put("committeeId", committeeId);

      String sql =
          generateSQL(tempTable)
              + " WHERE c.COMMITTEE_ID = :committeeId"
              + " ORDER BY cl.LEVEL_ID, lu.USER_CONFIG_ID";

      return namedParameterJdbcTemplate.query(
          sql,
          params,
          rs -> {
            CommitteeDTO committee = null;
            while (rs.next()) {
              if (committee == null) {
                committee = mapCommittee(rs);
              }
              processLevels(rs, committee);
            }
            return committee;
          });

    } catch (Exception e) {
      log.error("ERROR : CommitteeJdbc | getCommittee :", e);
      throw new ApiRequestException("Failed to retrieve committee.");
    }
  }

  private void processLevels(ResultSet rs, CommitteeDTO committee) throws SQLException {

    int levelConfigId = rs.getInt(LEVEL_CONFIG_ID);
    if (!rs.wasNull()) {
      CommitteeLevelDTO level =
          committee.getLevels().stream()
              .filter(l -> levelConfigId == l.getLevelId())
              .findFirst()
              .orElse(null);

      if (level == null) {
        level = mapLevel(rs);
        committee.getLevels().add(level);
      }
      if (!rs.wasNull()) {
        level.getLevelUsers().add(mapLevelUser(rs));
      }
    }
  }

  private CommitteeDTO mapCommittee(ResultSet rs) {
    CommitteeDTO dto = new CommitteeDTO();

    try {
      dto.setCommitteeId(rs.getInt(COMMITTEE_ID));
      dto.setCommitteeTypeId(rs.getInt("COMMITTEE_TYPE_ID"));
      dto.setCommitteeName(rs.getString("COMMITTEE_NAME"));
      dto.setCommitteeType(rs.getString("COMMITTEE_TYPE"));
      dto.setCommitteeTypeName(rs.getString("COMMITTEE_TYPE_NAME"));
      dto.setDelegatedAuthority(rs.getBigDecimal("DELEGATED_AUTHORITY_IN_BILLION"));
      dto.setReviewer(rs.getString("REVIEWER"));
      dto.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
      dto.setCreatedBy(rs.getString("CREATED_BY"));
      dto.setModifiedBy(rs.getString("MODIFIED_BY"));
      dto.setLastModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
      dto.setStatus(AppsConstants.Status.resolveStatus(rs.getString("STATUS")));
      dto.setApproveStatus(
          MasterDataApproveStatus.resolveApproveStatus(rs.getString("APPROVE_STATUS")));
      dto.setApprovedBy(rs.getString("APPROVED_BY"));
      dto.setApprovedDate(rs.getTimestamp("APPROVED_DATE"));
      dto.setRecordStatus(
          AppsConstants.RecordStatus.resolveRecordStatus(rs.getString("COMMITTEE_STATUS")));
      dto.setReviewerDisplayName(rs.getString("REVIEWER_USER_DISPLAY_NAME"));

      dto.setLevels(new ArrayList<>());

    } catch (SQLException e) {
      log.error("SQLException : CommitteeJdbc | mapCommittee : ", e);
    }
    return dto;
  }

  private CommitteeLevelDTO mapLevel(ResultSet rs) {
    CommitteeLevelDTO dto = new CommitteeLevelDTO();

    try {
      dto.setLevelId(rs.getInt(LEVEL_CONFIG_ID));
      dto.setLevel(rs.getInt("LEVEL_ID"));
      dto.setCommitteeId(rs.getInt(COMMITTEE_ID));
      dto.setCombination(rs.getString("COMBINATION"));
      dto.setUserCount(rs.getInt("USER_COUNT"));
      dto.setCreatedDate(rs.getTimestamp("LEVEL_CREATED_DATE"));
      dto.setCreatedBy(rs.getString("LEVEL_CREATED_BY"));
      dto.setPathType(AppsConstants.CAPathType.resolveCAPathType(rs.getString("PATH_TYPE")));

      dto.setLevelUsers(new ArrayList<>());

    } catch (SQLException e) {
      log.error("SQLException : CommitteeJdbc | mapLevel : ", e);
    }

    return dto;
  }

  private LevelUserDTO mapLevelUser(ResultSet rs) {
    LevelUserDTO dto = new LevelUserDTO();

    try {
      dto.setLevelUserId(rs.getInt("USER_CONFIG_ID"));
      dto.setLevelId(rs.getInt(LEVEL_CONFIG_ID));
      dto.setCommitteeId(rs.getInt(COMMITTEE_ID));
      dto.setUserId(rs.getInt("USER_ID"));
      dto.setUserName(rs.getString("POOL_USER_NAME"));
      dto.setUserDisplayName(rs.getString("POOL_USER_DISPLAY_NAME"));
      dto.setCreatedDate(rs.getTimestamp("LEVEL_USER_CREATED_DATE"));
      dto.setCreatedBy(rs.getString("LEVEL_USER_CREATED_BY"));

    } catch (SQLException e) {
      log.error("SQLException : CommitteeJdbc | mapLevelUser : ", e);
    }

    return dto;
  }

  private String generateSQLForAllView(boolean tempTable) {

    String committeeTable = tempTable ? "CA_COMMITTEE_CONFIG_TEMP c" : "CA_COMMITTEE_CONFIG c";

    return """
                SELECT
                    c.COMMITTEE_ID,
                    c.COMMITTEE_NAME,
                    c.COMMITTEE_TYPE_ID,
                    c.COMMITTEE_TYPE,
                    c.DELEGATED_AUTHORITY_IN_BILLION,
                    c.STATUS,
                    c.REVIEWER,
                    c.CURRENT_PATH,
                    c.CREATED_DATE,
                    c.MODIFIED_DATE,
                    c.CREATED_BY,
                    c.MODIFIED_BY,
                    c.APPROVE_STATUS,
                    c.APPROVED_BY,
                    c.APPROVED_DATE,
                    c.COMMITTEE_STATUS,

                    ct.COMMITTEE_TYPE_NAME,

                    cp.USER_DISPLAY_NAME AS REVIEWER_USER_DISPLAY_NAME
                FROM %s
                LEFT JOIN CA_COMMITTEE_TYPE_CONFIG ct
                    ON c.COMMITTEE_TYPE_ID = ct.COMMITTEE_TYPE_ID
                   AND c.COMMITTEE_TYPE = ct.COMMITTEE_TYPE

                LEFT JOIN CA_POOL_CONFIG cp
                    ON c.REVIEWER = cp.USER_NAME
                """
            .formatted(committeeTable)
        + " ORDER BY c.COMMITTEE_ID DESC";
  }

  private String generateSQL(boolean tempTable) {

    String committeeTable = tempTable ? "CA_COMMITTEE_CONFIG_TEMP c" : "CA_COMMITTEE_CONFIG c";

    String levelTable = tempTable ? "CA_LEVEL_CONFIG_TEMP cl" : "CA_LEVEL_CONFIG cl";

    String userTable = tempTable ? "CA_USER_CONFIG_TEMP lu" : "CA_USER_CONFIG lu";

    return """
                SELECT
                    c.COMMITTEE_ID,
                    c.COMMITTEE_NAME,
                    c.COMMITTEE_TYPE,
                    c.DELEGATED_AUTHORITY_IN_BILLION,
                    c.STATUS,
                    c.REVIEWER,
                    c.CURRENT_PATH,
                    c.CREATED_DATE,
                    c.MODIFIED_DATE,
                    c.CREATED_BY,
                    c.MODIFIED_BY,
                    c.APPROVE_STATUS,
                    c.APPROVED_BY,
                    c.APPROVED_DATE,
                    c.COMMITTEE_STATUS,

                    ct.COMMITTEE_TYPE_ID,
                    ct.COMMITTEE_TYPE,
                    ct.COMMITTEE_TYPE_NAME,

                    cl.LEVEL_CONFIG_ID,
                    cl.LEVEL_ID,
                    cl.COMBINATION,
                    cl.PATH_TYPE,
                    cl.USER_COUNT,
                    cl.STATUS AS LEVEL_STATUS,
                    cl.CREATED_DATE AS LEVEL_CREATED_DATE,
                    cl.CREATED_BY AS LEVEL_CREATED_BY,

                    lu.USER_CONFIG_ID,
                    lu.USER_NAME AS LEVEL_USER_NAME,
                    lu.CREATED_DATE AS LEVEL_USER_CREATED_DATE,
                    lu.CREATED_BY AS LEVEL_USER_CREATED_BY,

                    cp.USER_ID,
                    cp.USER_NAME AS POOL_USER_NAME,
                    cp.USER_DISPLAY_NAME AS POOL_USER_DISPLAY_NAME,
                    cpc.USER_DISPLAY_NAME AS REVIEWER_USER_DISPLAY_NAME
                FROM %s

                LEFT JOIN CA_COMMITTEE_TYPE_CONFIG ct
                    ON c.COMMITTEE_TYPE_ID = ct.COMMITTEE_TYPE_ID
                   AND c.COMMITTEE_TYPE = ct.COMMITTEE_TYPE

                LEFT JOIN %s
                    ON c.COMMITTEE_ID = cl.COMMITTEE_ID

                LEFT JOIN %s
                    ON cl.LEVEL_CONFIG_ID = lu.LEVEL_CONFIG_ID
                    AND cl.PATH_TYPE = lu.PATH_TYPE

                LEFT JOIN CA_POOL_CONFIG cp
                    ON lu.USER_NAME = cp.USER_NAME

                LEFT JOIN CA_POOL_CONFIG cpc
                    ON c.REVIEWER = cpc.USER_NAME
                """
        .formatted(committeeTable, levelTable, userTable);
  }

  public CommitteeDTO findCommitteeByCommitteeName(String committeeName) {
    Map<String, Object> params = new HashMap<>();
    params.put("committeeName", committeeName);

    String sql =
        """
                SELECT
                    COMMITTEE_ID,
                    COMMITTEE_NAME
                FROM CA_COMMITTEE_CONFIG
                WHERE COMMITTEE_NAME = :committeeName
                """;

    return this.namedParameterJdbcTemplate.query(
        sql,
        params,
        new ResultSetExtractor<CommitteeDTO>() {
          @Override
          public CommitteeDTO extractData(@Nullable ResultSet rs) throws SQLException {
            CommitteeDTO committee = new CommitteeDTO();
            if (rs != null && rs.next()) {
              committee.setCommitteeId(rs.getInt(COMMITTEE_ID));
              committee.setCommitteeName(rs.getString("COMMITTEE_NAME"));
              return committee;
            }
            return null;
          }
        });
  }

  public Integer removeCommitteeLevel(Integer committeeId) {

    try {
      Map<String, Object> params = new HashMap<>();
      params.put("committeeId", committeeId);
      String sql =
          """
                    SELECT CA_COMMITTEE_LEVEL_REMOVE(:committeeId) AS STATUS5    FROM DUAL
                        """;

      return namedParameterJdbcTemplate.query(
          sql,
          params,
          new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(@Nullable ResultSet rs)
                throws SQLException, DataAccessException {
              int status = 0;
              while (rs != null && rs.next()) {
                status = rs.getInt("STATUS");
              }

              return status;
            }
          });

    } catch (Exception e) {
      log.error("ERROR : CommitteeJdbc | removeCommitteeLevel : ", e);
      return 0;
    }
  }
}
