package io.github.adigunhammedolalekan.authkit.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T parse(String json, Class<T> clazz) {
        return MAPPER.convertValue(json, clazz);
    }

    public static String create(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
