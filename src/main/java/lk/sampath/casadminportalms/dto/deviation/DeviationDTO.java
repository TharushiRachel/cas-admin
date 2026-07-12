package lk.sampath.casadminportalms.dto.deviation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lk.sampath.casadminportalms.entity.deviation.Deviation;
import lk.sampath.casadminportalms.entity.deviation.TempDeviation;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class DeviationDTO implements Serializable {

    private Integer deviationId;

    private Integer parentId;

    private String deviationType;

    private String description;

    private String status;

    private MasterDataApproveStatus approveStatus;

    private Date approvedDate;

    private String approvedBy;

    private Date createdDate;

    private String createdBy;

    private Date lastModifiedDate;

    private String modifiedBy;

    @JsonProperty("isTempRecord")
    private boolean isTempRecord;

    public DeviationDTO(){
    }

    public DeviationDTO(Deviation deviation) {
        this.deviationId = deviation.getDeviationId();
        this.deviationType = deviation.getDeviationType();
        this.description = deviation.getDescription();
        this.status = deviation.getStatus();
        this.createdBy = deviation.getCreatedBy();
        this.createdDate = deviation.getCreatedDate();
        this.modifiedBy = deviation.getModifiedBy();
        this.lastModifiedDate = deviation.getLastModifiedDate();
        this.approveStatus = deviation.getApproveStatus();
        this.approvedDate = deviation.getApprovedDate();
        this.approvedBy = deviation.getApprovedBy();
        this.isTempRecord = false;
    }

    public DeviationDTO(TempDeviation deviation) {
        this.deviationId = deviation.getDeviationId();
        this.parentId = deviation.getParentId();
        this.deviationType = deviation.getDeviationType();
        this.description = deviation.getDescription();
        this.status = deviation.getStatus();
        this.createdBy = deviation.getCreatedBy();
        this.createdDate = deviation.getCreatedDate();
        this.modifiedBy = deviation.getModifiedBy();
        this.lastModifiedDate = deviation.getLastModifiedDate();
        this.approveStatus = deviation.getApproveStatus();
        this.approvedDate = deviation.getApprovedDate();
        this.approvedBy = deviation.getApprovedBy();
        this.isTempRecord = true;
    }
}
