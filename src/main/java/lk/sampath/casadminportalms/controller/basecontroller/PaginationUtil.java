package lk.sampath.casadminportalms.controller.basecontroller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static Pageable createPageable(Integer headerPage, Integer headerSize, int page, int size) {
        int effectivePage = headerPage != null ? headerPage : page;
        int effectiveSize = headerSize != null ? headerSize : size;
        return PageRequest.of(effectivePage, effectiveSize);
    }
}
