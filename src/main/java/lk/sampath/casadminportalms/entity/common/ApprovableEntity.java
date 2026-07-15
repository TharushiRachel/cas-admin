package lk.sampath.casadminportalms.entity.common;

import jakarta.persistence.*;
import java.util.Date;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class ApprovableEntity extends UserTrackableEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "APPROVE_STATUS")
  private MasterDataApproveStatus approveStatus;

  @Column(name = "APPROVED_DATE")
  private Date approvedDate;

  @Column(name = "APPROVED_BY")
  private String approvedBy;

  @Override
  protected boolean allowAuditUpdate() {
    return approveStatus != MasterDataApproveStatus.APPROVED
        && approveStatus != MasterDataApproveStatus.REJECTED;
  }
}
