package lk.sampath.casadminportalms.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.io.Serializable;

@MappedSuperclass
public abstract class Persistent implements Serializable {

  @Version
  @Column(name = "VERSION", nullable = false)
  protected Long version = null;
}
