package lk.sampath.casadminportalms.dto.dadesignation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DAReorderRequest {

    private List<DAReorderItemRequest> items = new ArrayList<>();
}
