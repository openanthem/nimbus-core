package com.antheminc.oss.nimbus.support.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomDateDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = 1L;

	private final CustomLocalDateTimeDeserializer deserializer;

	public CustomDateDeserializer() {
		this(null);
	}

	protected CustomDateDeserializer(Class<?> vc) {
		super(vc);
		this.deserializer = new CustomLocalDateTimeDeserializer();
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {

		LocalDateTime ldtDate = this.deserializer.deserialize(jsonparser, context);
		return null==ldtDate ? null : Date.from(ldtDate.atZone(ZoneId.systemDefault()).toInstant());
		

	}

}
