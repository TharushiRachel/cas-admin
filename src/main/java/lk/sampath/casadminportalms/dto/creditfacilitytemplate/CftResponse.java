package lk.sampath.casadminportalms.dto.creditfacilitytemplate;

import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CftResponse {

    private Integer creditFacilityTemplateID;

    private Integer creditFacilityTypeID;

    private String facilityTypeName;

    private String creditFacilityName;

    private String description;

    private BigDecimal maxFacilityAmount;

    private BigDecimal minFacilityAmount;

    private String status;

    private String approveStatus;

    private LocalDateTime approvedDate;

    private String approvedBy;

    private LocalDateTime createdDate;

    private String createdBy;
}
