package lk.sampath.casadminportalms.dto.deviation;

import lk.sampath.casadminportalms.entity.deviation.DeviationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class DeviationTypeDTO {

    private Integer deviationTypeId;

    private String deviationType;

    private String status;

    private Date createdDate;

    private String createdBy;

    private Date modifiedDate;

    private String modifiedBy;

    public DeviationTypeDTO() {
    }

    public DeviationTypeDTO(DeviationType deviationType) {
        this.deviationTypeId = deviationType.getDeviationTypeId();
        this.deviationType = deviationType.getDeviationType();
        this.status = deviationType.getStatus();
        this.createdDate = deviationType.getCreatedDate();
        this.createdBy = deviationType.getCreatedBy();
        this.modifiedDate = deviationType.getLastModifiedDate();
        this.modifiedBy = deviationType.getModifiedBy();
    }
}
