package lk.sampath.casadminportalms.dto.committeepool;

import java.util.List;
import lombok.Data;

@Data
public class CommitteePoolResp {

  private List<CommitteePoolDTO> committeePoolTempList;

  private List<CommitteePoolDTO> committeePoolList;
}
