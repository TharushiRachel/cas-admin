package lk.sampath.casadminportalms.dto.creditfacilitytemplate;

import lk.sampath.casadminportalms.dto.common.PagedParamDTO;
import lk.sampath.casadminportalms.enums.AppsConstants;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@ToString
public class CreditFacilityTemplateSearchRQ extends PagedParamDTO implements Serializable {

    private Integer creditFacilityTemplateID;

    private Integer creditFacilityTypeID;

    private String creditFacilityName;

    private String facilityTypeName;

    private String description;

    private BigDecimal maxFacilityAmount;

    private BigDecimal minFacilityAmount;

    private AppsConstants.Status status;

    private MasterDataApproveStatus approveStatus;

    private List<MasterDataApproveStatus> approveStatusList;

    private String approvedDateStr;

    private String approvedBy;


}
