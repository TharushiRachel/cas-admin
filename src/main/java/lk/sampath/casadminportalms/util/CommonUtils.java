/*
 * -------------------------------------------------------------------------------------------------------------------
 * Copyright © Sampath Bank PLC. All rights reserved.
 *
 * <p>This software and its source code are the exclusive property of Sampath Bank PLC. Unauthorized
 * copying, modification, distribution, or use - whether in whole or in part - is strictly
 * prohibited without prior written consent from Sampath Bank PLC.
 * -------------------------------------------------------------------------------------------------------------------
 */
package lk.sampath.casadminportalms.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

  public static LocalDate parseDate(String dateStr) {
    if (CommonUtils.isNullOrEmpty(dateStr)) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return LocalDate.parse(dateStr, formatter);
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }
}
