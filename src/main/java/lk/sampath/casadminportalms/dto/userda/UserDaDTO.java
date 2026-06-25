package lk.sampath.casadminportalms.dto.userda;


import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserDaDTO  {

    private Integer userDaID;
    
    private String userName;

    private BigDecimal maxAmount;

    private String description;

    private Status status;

    private MasterDataApproveStatus approveStatus;

    private Date approvedDate;

    private String approvedBy;

    private String createdBy;

    private  Date createdDate;

    private Date lastModifiedDate;

    private String modifiedBy;




}
