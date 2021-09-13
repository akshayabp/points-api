package com.fetchrewards.pointsapi.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.springframework.expression.ParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomLocalDateDeserializer extends StdDeserializer<LocalDateTime>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomLocalDateDeserializer() {
		this(null);
	}

	public CustomLocalDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String iso8601DateTime = jsonparser.getText();
        try {
        	DateTimeFormatter formatter = new DateTimeFormatterBuilder()
    				.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME).optionalStart()
    				.appendOffset("+HH", "Z")
    			    .toFormatter();
    		
    		LocalDateTime timestamp = LocalDateTime.parse(iso8601DateTime,formatter);
    		return timestamp;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
	}

}
