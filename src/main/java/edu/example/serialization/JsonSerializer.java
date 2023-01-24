package edu.example.serialization;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class JsonSerializer<T> {
    private final Set<Field> publishedFields = new HashSet<>();

    public JsonSerializer(Class<T> serializedClass) {
        for (Field serializedClassField : serializedClass.getDeclaredFields()) {
            Published published = serializedClassField.getAnnotation(Published.class);

            if (published == null) {
                continue;
            }

            if (Modifier.isPrivate(serializedClassField.getModifiers())) {
                serializedClassField.setAccessible(true);
            }
            publishedFields.add(serializedClassField);
        }
    }

    public JSONObject serialize(T o) {
        JSONObject result = new JSONObject();

        for (Field field : publishedFields) {
            Object value;
            try {
                value = field.get(o);
            } catch (IllegalAccessException e) {
                continue;
            }
            result.put(field.getName(), value.toString());
        }

        return result;
    }
}
