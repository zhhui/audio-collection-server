package com.ushow.tv.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JSONUtils {

	private final static Logger log = LoggerFactory.getLogger(JSONUtils.class);

	private final static ObjectMapper objectMapper = new CustomObjectMapper();

	static {
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static <T> T readValue(String json, JavaType valueType) {
		try {
			return objectMapper.readValue(json, valueType);
		} catch (Exception e) {
			log.error("error:{},json:{}", new Object[] { e.getMessage(), json });
		}
		return null;
	}

	public static <T> T readValue(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("error:{},json:{}", new Object[] { e.getMessage(), json });
		}
		return null;
	}

	public static <T> T readValue(JsonNode jsonNode, Class<T> clazz) {
		return JSONUtils.readValue(JSONUtils.writeValueAsString(jsonNode), clazz);
	}

	public static <T> T readValue(JsonNode jsonNode, JavaType valueType) {
		return JSONUtils.readValue(JSONUtils.writeValueAsString(jsonNode), valueType);
	}

	public static <T> List<T> readListValue(String json, Class<T> elementClass) {
		return JSONUtils.readValue(json, TypeFactory.defaultInstance()
				.constructCollectionType(List.class, elementClass));
	}

	public static <T> List<T> readListValue(JsonNode jsonNode, Class<T> elementClass) {
		return JSONUtils.readValue(jsonNode,
				TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass));
	}

	public static <T> Map<String, T> readMapValue(String json, Class<T> elementClass) {
		return JSONUtils.readValue(json,
				TypeFactory.defaultInstance().constructMapLikeType(Map.class, String.class, elementClass));
	}

	public static String writeValueAsString(Object obj) {
		try {
			if (obj != null) {
				return objectMapper.writeValueAsString(obj);
			}
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static String writerWithDefaultPrettyPrinter(Object obj) {
		try {
			if (obj != null) {
				return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			}
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static JsonNode readTree(String content) {
		try {
			if (content != null) {
				return objectMapper.readTree(content);
			}
		} catch (IOException e) {
			log.error("error:{},json:{}", new Object[] { e.getMessage(), content });
		}
		return null;
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}
}
