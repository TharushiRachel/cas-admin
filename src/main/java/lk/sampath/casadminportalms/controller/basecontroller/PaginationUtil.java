package lk.sampath.casadminportalms.controller.basecontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PaginationUtil {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private PaginationUtil() {
    }

    public static <T> StandardResponse<List<T>> paginate(StandardResponse<List<T>> response, int page, int size) {
        if (response == null) {
            return null;
        }

        int safePage = Math.max(page, DEFAULT_PAGE);
        int safeSize = size > 0 ? size : DEFAULT_SIZE;

        Object payload = response.getResponse();
        if (!(payload instanceof List<?>)) {
            return response;
        }

        @SuppressWarnings("unchecked")
        List<T> data = (List<T>) payload;

        int fromIndex = safePage * safeSize;
        if (fromIndex >= data.size()) {
            return new StandardResponse<>(response.getSuccess(), response.getMessage(), Collections.emptyList());
        }

        int toIndex = Math.min(fromIndex + safeSize, data.size());
        List<T> pagedData = new ArrayList<>(data.subList(fromIndex, toIndex));

        return new StandardResponse<>(response.getSuccess(), response.getMessage(), pagedData);
    }
}
