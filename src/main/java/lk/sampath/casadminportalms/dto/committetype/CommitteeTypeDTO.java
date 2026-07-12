package lk.sampath.casadminportalms.dto.committetype;

import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;
import java.util.Date;

/**
 * Contains DTO to CommitteeType
 */
@Data
public class CommitteeTypeDTO {

    private Integer committeeTypeId;
    private String committeeType;
    private String committeeTypeName;
    private Date createdDate;
    private String createdBy;
    private String createdUserDisplayName;
    private String authorizerDisplayName;
    private AppsConstants.Status status;
    private Date modifiedDate;
    private String modifiedBy;
    private Integer version;
    private Integer isSystem;

}
