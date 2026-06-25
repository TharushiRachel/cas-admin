package lk.sampath.casadminportalms.enums;

public enum InterestRatingSubCategory {
    EFFECTIVE("EFFECTIVE"),
    REDUCING("REDUCING"),
    FLAT("FLAT"),
    OTHER("OTHER");

    private String description;

    InterestRatingSubCategory(String description) {
        this.description = description;
    }

    public static InterestRatingSubCategory resolve(String status) {
        InterestRatingSubCategory result = null;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(status)) {
            for (InterestRatingSubCategory interestRatingSubCategory : InterestRatingSubCategory.values()) {
                if (interestRatingSubCategory.name().equals(status.toUpperCase())) {
                    result = interestRatingSubCategory;
                    break;
                }
            }
        }
        return result;
    }

    public String getDescription() {
        return description;
    }
}
