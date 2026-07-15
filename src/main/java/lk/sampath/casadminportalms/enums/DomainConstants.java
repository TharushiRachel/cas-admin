package lk.sampath.casadminportalms.enums; // package lk.sampath.casadminportalms.enums;
// import org.springframework.util.StringUtils;
//
// public class DomainConstants {
//
//    public enum MasterDataApproveStatus {
//        PENDING("Pending", "P"),
//        APPROVED("Approved", "A"),
//        REJECTED("Rejected", "R");
//
//        private String lablel;
//
//        private String value;
//
//        MasterDataApproveStatus(String lablel, String value) {
//            this.lablel = lablel;
//            this.value = value;
//        }
//
//        public String getLablel() {
//            return lablel;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public static MasterDataApproveStatus getEnum(String value) {
//            for (MasterDataApproveStatus clusterStatus : MasterDataApproveStatus.values()) {
//                if (clusterStatus.getValue().equalsIgnoreCase(value)) {
//                    return clusterStatus;
//                }
//            }
//            return null;
//        }
//
//        public static MasterDataApproveStatus resolve(String specifyAs) {
//            MasterDataApproveStatus result = null;
//            if (StringUtils.isEmpty(specifyAs)) {
//                result = MasterDataApproveStatus.valueOf(specifyAs);
//            }
//            return result;
//        }
//    }
//
//
//    public enum Title {
//
//        MR("Mr", "MR"),
//        MRS("Mrs", "MRS"),
//        MS("Ms", "MS"),
//        DR("Dr", "DR");
//        private String label;
//        private String value;
//
//        Title(String label, String value) {
//            this.label = label;
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public String getLabel() {
//            return label;
//        }
//
//        public static Title getEnum(String value) {
//            for (Title clusterStatus : Title.values()) {
//                if (clusterStatus.getValue().equalsIgnoreCase(value)) {
//                    return clusterStatus;
//                }
//            }
//            return null;
//        }
//    }
//
//    public enum Gender {
//
//        M("Male", "M"),
//        F("Female", "F");
//        private String label;
//        private String value;
//
//        Gender(String label, String value) {
//            this.label = label;
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public String getLabel() {
//            return label;
//        }
//
//        public static Gender getEnum(String value) {
//            for (Gender clusterStatus : Gender.values()) {
//                if (clusterStatus.getValue().equalsIgnoreCase(value)) {
//                    return clusterStatus;
//                }
//            }
//            return null;
//        }
//    }
//
//    public enum UserType {
//        ADMIN, AGENT;
//    }
//
////    public enum InterestRatingSubCategory {
////        EFFECTIVE("EFFECTIVE"),
////        REDUCING("REDUCING"),
////        FLAT("FLAT"),
////        OTHER("OTHER");
////
////        private String description;
////
////        InterestRatingSubCategory(String description) {
////            this.description = description;
////        }
////
////        public static InterestRatingSubCategory resolve(String status) {
////            InterestRatingSubCategory result = null;
////            if (org.apache.commons.lang3.StringUtils.isNotEmpty(status)) {
////                for (InterestRatingSubCategory interestRatingSubCategory :
// InterestRatingSubCategory.values()) {
////                    if (interestRatingSubCategory.name().equals(status.toUpperCase())) {
////                        result = interestRatingSubCategory;
////                        break;
////                    }
////                }
////            }
////            return result;
////        }
////
////        public String getDescription() {
////            return description;
////        }
////    }
//
////    public enum InputFieldValueType {
////        TEXT("Text"),
////        NUMBER("Number"),
////        CURRENCY("CURRENCY"),
////        DATE("Date"),
////        PERCENTAGE("Percentage");
////
////        private String description;
////
////        InputFieldValueType(String description) {
////            this.description = description;
////        }
////
////        public static InputFieldValueType resolve(String status) {
////            InputFieldValueType result = null;
////            if (org.apache.commons.lang3.StringUtils.isNotEmpty(status)) {
////                for (InputFieldValueType inputFieldValueType : InputFieldValueType.values()) {
////                    if (inputFieldValueType.name().equals(status.toUpperCase())) {
////                        result = inputFieldValueType;
////                        break;
////                    }
////                }
////            }
////            return result;
////        }
////
////        public String getDescription() {
////            return description;
////        }
////    }
//
// }
