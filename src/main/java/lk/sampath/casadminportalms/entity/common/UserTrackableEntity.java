package lk.sampath.casadminportalms.entity.common;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class UserTrackableEntity implements Serializable {

  private static final long serialVersionUID = 2405172041950251807L;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_BY")
  private String createdBy;

  @Column(name = "MODIFIED_DATE")
  private Date lastModifiedDate;

  @Column(name = "MODIFIED_BY")
  private String modifiedBy;

  @PrePersist
  public void prePersist() {

    if (createdBy != null && !createdBy.isEmpty()) {
      return;
    }

    this.createdBy = UserContext.getUsername();
    this.createdDate = new Date();
  }

  @PreUpdate
  public void preUpdate() {

    if (!allowAuditUpdate() || (modifiedBy != null && !modifiedBy.isEmpty())) {
      return;
    }

    this.modifiedBy = UserContext.getUsername();
    this.lastModifiedDate = new Date();
  }

  protected boolean allowAuditUpdate() {
    return true;
  }
}
