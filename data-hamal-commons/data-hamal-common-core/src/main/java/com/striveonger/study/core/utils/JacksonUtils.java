package com.striveonger.study.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Mr.Lee
 * @description: JSON 辅助工具
 * @date 2023-02-24 10:42
 */
public class JacksonUtils {

    private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);
    private static final TypeReference<Map<String, Object>> mapType = new TypeReference<>() {};
    private static final ObjectMapper mapper;

    static {
        mapper = JsonMapper.builder()
                           .addModule(new JavaTimeModule())
                           .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                           .build();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJSONString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert String error ", e);
        }
        return null;
    }

    public static Map<String, Object> toMap(String s) {
        try {
            return mapper.readValue(s, mapType);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert Map error ", e);
        }
        return null;
    }

    public static <T> T toObject(String s, Class<T> clazz) {
        try {
            return mapper.readValue(s, clazz);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert Object error ", e);
        }
        return null;
    }

    public static <T> T toObject(String s, TypeReference<T> type) {
        try {
            return mapper.readValue(s, type);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert Object error ", e);
        }
        return null;
    }

    public static byte[] toJSONBytes(Object o) {
        String str = toJSONString(o);
        return str == null ? null : str.getBytes(StandardCharsets.UTF_8);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
