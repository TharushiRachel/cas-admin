package lk.sampath.casadminportalms.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;


@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class UserTrackableEntity implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date lastModifiedDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

}
