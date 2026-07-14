package lk.sampath.casadminportalms.dto.dadesignation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DADesignationCodeDTO {

    @JsonProperty("DESIGNATION_CODE")
    private String code;

    @JsonProperty("DESIGNATION_DESCRIPTION")
    private String description;
}
