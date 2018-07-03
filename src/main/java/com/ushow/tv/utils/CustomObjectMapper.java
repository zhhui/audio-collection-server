package com.ushow.tv.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {

	/***/
	private static final long serialVersionUID = 1L;

	public CustomObjectMapper() {
		super();
		// configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
}