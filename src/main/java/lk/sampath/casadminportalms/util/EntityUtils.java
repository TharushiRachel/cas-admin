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

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.lang.reflect.Field;

public class EntityUtils {

  public static String getTableName(Class<?> entityClass) {
    if (entityClass.isAnnotationPresent(Table.class)) {
      Table table = entityClass.getAnnotation(Table.class);
      if (!table.name().isEmpty()) {
        return table.name();
      }
    }
    return entityClass.getSimpleName().toUpperCase();
  }

  public static String getColumnName(Class<?> entityClass, String fieldName) {
    try {
      Field field = entityClass.getDeclaredField(fieldName);

      if (field.isAnnotationPresent(Column.class)) {
        Column column = field.getAnnotation(Column.class);
        if (!column.name().isEmpty()) {
          return column.name();
        }
      }
      return field.getName().toUpperCase();
    } catch (NoSuchFieldException e) {
      return "Field not found: " + fieldName;
    }
  }
}
