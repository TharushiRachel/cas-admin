package lk.sampath.casadminportalms.dto.facilitypaper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Formula {

  private String facilityType;
  private String calculatorType;
  private String name;
  private String code;
  private String type;
  private String value;
  private boolean output;
  private String outputCode;
  private String outputName;
  private int outputOrder;
  private boolean currency;
  private boolean percentage;
}
