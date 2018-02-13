package com.antheminc.oss.nimbus.support.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomDateDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = 1L;

	DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public CustomDateDeserializer() {
		this(null);
	}

	protected CustomDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		String date = jsonparser.getText();
		Date formattedDate = null;

		if (StringUtils.isEmpty(date)) {
			return null;
		}

		try {
			formattedDate = inputFormatter.parse(date);
			return formattedDate;
		} catch (Exception e) {
		}

		throw new JsonParseException(jsonparser,
				"Unparseable date: \"" + date + "\". Supported format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	}

}
