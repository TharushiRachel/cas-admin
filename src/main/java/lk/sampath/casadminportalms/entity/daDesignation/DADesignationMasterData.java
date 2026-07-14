package lk.sampath.casadminportalms.entity.daDesignation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "DA_DESIGNATION")
public class DADesignationMasterData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DA_DESIGNATION_MASTER")
    @SequenceGenerator(name = "SEQ_DA_DESIGNATION_MASTER", sequenceName = "SEQ_DA_DESIGNATION_MASTER", allocationSize = 1)
    @Column(name = "DESIGNATION_ID")
    private Integer id;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "PARENT_REC_ID")
    private Integer parentRecId;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "AUTHORIZER_DISPLAY_NAME")
    private String authorizedDisplayName;

    @Column(name = "APPROVE_STATUS")
    private String approveStatus;

    @Column(name = "DESIGNATION_CODE")
    private String designationCode;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "IS_COMMITTEE")
    private String isCommittee;

    /**
     * One designation has many temp limit column values.
     */
    @OneToMany(mappedBy = "designation", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DALimitTemp> limitTemps = new ArrayList<>();

    public void addLimitTemp(DALimitTemp limitTemp) {
        limitTemps.add(limitTemp);
        limitTemp.setDesignation(this);
    }

    public void clearLimitTemps() {
        for (DALimitTemp limitTemp : limitTemps) {
            limitTemp.setDesignation(null);
        }
        limitTemps.clear();
    }
}
