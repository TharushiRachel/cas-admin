package lk.sampath.casadminportalms.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class AppsConstants {

  public enum YesNo {
    Y("Yes", true, 1),
    N("No", false, 0);

    private String strVal;

    private Boolean boolVal;

    private Integer intVal;

    YesNo(String strVal, Boolean boolVal, Integer intVal) {
      this.strVal = strVal;
      this.boolVal = boolVal;
      this.intVal = intVal;
    }

    public static YesNo valueOf(Boolean boolVal) {
      YesNo matchingStatus = null;
      for (YesNo yesno : YesNo.values()) {
        if (yesno.getBoolVal().equals(boolVal)) {
          matchingStatus = yesno;
          break;
        }
      }
      return matchingStatus;
    }

    public static YesNo resolver(String val) {
      YesNo matchingStatus = null;
      if (val != null) {
        matchingStatus = YesNo.valueOf(val);
      }
      return matchingStatus;
    }

    public static Map<String, String> getYesNoMap() {
      Map<String, String> map = new LinkedHashMap<String, String>();
      for (YesNo yesNo : YesNo.values()) {
        map.put(yesNo.toString(), yesNo.getStrVal());
      }
      return map;
    }

    public String getStrVal() {
      return strVal;
    }

    public Boolean getBoolVal() {
      return boolVal;
    }

    public Integer getIntVal() {
      return intVal;
    }
  }

  public enum Status {
    ACT("Active"),
    INA("Inactive"),
    RMV("Remove");

    private String description;

    Status(String description) {
      this.description = description;
    }

    public static Status resolveStatus(String statusStr) {
      Status matchingStatus = null;
      if (!StringUtils.isEmpty(statusStr)) {
        matchingStatus = Status.valueOf(statusStr.trim());
      }
      return matchingStatus;
    }

    public static Map<String, String> getStatusMap() {
      Map<String, String> result = new HashMap<>();
      Arrays.asList(Status.values())
          .forEach(
              status -> {
                result.put(status.toString(), status.getDescription());
              });
      return result;
    }

    public String getDescription() {
      return description;
    }
  }

  public enum CAPathType {
    REG("Regular"),
    ALT("Alternative");

    private String name;

    CAPathType(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public static CAPathType resolveCAPathType(String path) {
      CAPathType matchingStatus = null;
      if (!StringUtils.isEmpty(path)) {
        matchingStatus = CAPathType.valueOf(path.trim());
      }
      return matchingStatus;
    }
  }

  public enum RecordStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    NEW("New"),
    UPDATE("Update"),
    DELETE("Delete");

    private String name;

    RecordStatus(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public static RecordStatus resolveRecordStatus(String statusStr) {
      RecordStatus matchingStatus = null;
      if (!StringUtils.isEmpty(statusStr)) {
        matchingStatus = RecordStatus.valueOf(statusStr.trim());
      }
      return matchingStatus;
    }
  }
}
