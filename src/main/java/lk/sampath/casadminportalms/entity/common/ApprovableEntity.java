package lk.sampath.casadminportalms.entity.common;

import jakarta.persistence.*;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class ApprovableEntity extends UserTrackableEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVE_STATUS")
    private MasterDataApproveStatus approveStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "APPROVED_BY")
    private String approvedBy;


}
