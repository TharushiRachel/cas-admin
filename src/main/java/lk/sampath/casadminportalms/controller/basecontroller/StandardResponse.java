package lk.sampath.casadminportalms.controller.basecontroller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class StandardResponse<T> {

    private Boolean success;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object response;

    public StandardResponse(Boolean success, String message, Object response) {
        this.success = success;
        this.message = message;
        this.response = response;
    }
}
