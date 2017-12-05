package com.anthem.nimbus.platform.spec.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.antheminc.oss.nimbus.core.util.JsonParsingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @Author Sandeep Mantha
 */
public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CustomLocalDateTimeSerializer() {
        this(null);
    }

    public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializerProvider) {
        try {
            gen.writeString(formatter.format(value));
        } catch (IOException e) {
            throw new JsonParsingException(e);
        }
    }

}