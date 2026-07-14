package lk.sampath.casadminportalms.dto.upctemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpcTemplateResponse {

    private Integer upcTemplateID;

    private String templateName;

    private String description;

    private String status;

    private String approveStatus;

    private LocalDateTime approvedDate;

    private String approvedBy;

    private LocalDateTime createdDate;

    private String createdBy;
}
