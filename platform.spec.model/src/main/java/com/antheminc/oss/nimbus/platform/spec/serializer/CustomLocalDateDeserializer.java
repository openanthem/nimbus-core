package com.antheminc.oss.nimbus.platform.spec.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @Author Cheikh Niass on 12/2/16.
 */
public class CustomLocalDateDeserializer extends StdDeserializer<LocalDate> {

	private static final long serialVersionUID = 1L;

	private static final String[] DATE_FORMATS = new String[] { "yyyy-MM-dd", "MM/dd/yyyy" };

	public CustomLocalDateDeserializer() {
		this(null);
	}

	public CustomLocalDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {

		String date = jsonparser.getText();
		if (StringUtils.isEmpty(date)) {
			return null;
		}

		for (String DATE_FORMAT : DATE_FORMATS) {
			try {
				return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
			} catch (Exception e) {
			}
		}
		throw new JsonParseException(jsonparser,
				"Unparseable date: \"" + date + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));

	}
}
