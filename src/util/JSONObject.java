package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONObject {
    private Map<String, Object> map = new HashMap<>();

    // Default constructor
    public JSONObject() {}

    // Constructor that parses a flat JSON string (no nested objects/arrays)
    public JSONObject(String json) {
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();
            if (!json.isEmpty()) {
                String[] pairs = json.split(",");
                for (String pair : pairs) {
                    String[] kv = pair.split(":", 2);
                    if (kv.length == 2) {
                        String key = kv[0].trim();
                        String value = kv[1].trim();
                        // Remove quotes from key
                        if (key.startsWith("\"") && key.endsWith("\"")) {
                            key = key.substring(1, key.length() - 1);
                        }
                        // Remove quotes from value if present
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                            map.put(key, value);
                        } else {
                            // Try to parse as int, else as is
                            try {
                                int intValue = Integer.parseInt(value);
                                map.put(key, intValue);
                            } catch (NumberFormatException e) {
                                map.put(key, value);
                            }
                        }
                    }
                }
            }
        }
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public String getString(String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    public int getInt(String key) {
        Object value = map.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Value for key '" + key + "' is not an int: " + value);
            }
        }
        throw new RuntimeException("Value for key '" + key + "' is not an int: " + value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Set<String> keys = map.keySet();
        int i = 0;
        for (String key : keys) {
            sb.append("\"").append(key).append("\":");
            Object value = map.get(key);
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else {
                sb.append(value);
            }
            if (i < keys.size() - 1) {
                sb.append(",");
            }
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
}