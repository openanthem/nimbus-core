package com.anthem.nimbus.platform.spec.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;

import com.anthem.oss.nimbus.core.util.JsonParsingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @Author Sandeep Mantha
 */
public class CustomLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CustomLocalDateTimeDeserializer() {
        this(null);
    }

    public CustomLocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context) {
        String date = null;
        try {
            date = jsonparser.getText();
            return !StringUtils.isEmpty(date) ? LocalDateTime.parse(date, formatter) : null;
        } catch (IOException e) {
            throw new JsonParsingException(e);
        }

    }
}

