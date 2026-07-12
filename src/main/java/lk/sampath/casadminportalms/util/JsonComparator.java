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

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonComparator {

  public static Map<String, Map<String, String>> compareJsonNodes(
      JsonNode oldNode, JsonNode newNode, String parentKey) {
    Map<String, Map<String, String>> changes = new LinkedHashMap<>(); // Store changed fields

    Iterator<Map.Entry<String, JsonNode>> fields = oldNode.fields();

    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> entry = fields.next();
      String fieldName = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();

      JsonNode oldValue = entry.getValue();
      JsonNode newValue = newNode.get(entry.getKey());

      if (newValue == null) {
        // Field removed
        changes.put(fieldName, Map.of("old", oldValue.toString(), "new", "null"));
      } else if (oldValue.isObject() && newValue.isObject()) {
        // Recursively compare nested objects
        changes.putAll(compareJsonNodes(oldValue, newValue, fieldName));
      } else if (!oldValue.equals(newValue)) {
        // Field changed
        changes.put(fieldName, Map.of("old", oldValue.toString(), "new", newValue.toString()));
      }
    }

    // Check for new fields added
    Iterator<Map.Entry<String, JsonNode>> newFields = newNode.fields();
    while (newFields.hasNext()) {
      Map.Entry<String, JsonNode> entry = newFields.next();
      String fieldName = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();

      if (!oldNode.has(entry.getKey())) {
        changes.put(fieldName, Map.of("old", "null", "new", entry.getValue().toString()));
      }
    }

    return changes;
  }
}
