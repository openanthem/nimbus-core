package com.anthem.nimbus.platform.spec.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.anthem.oss.nimbus.core.util.JsonParsingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @Author Cheikh Niass on 12/2/16.
 */
public class CustomLocalDateSerializer extends StdSerializer<LocalDate> {
    
	private static final long serialVersionUID = 1L;
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public CustomLocalDateSerializer() {
        this(null);
    }

    public CustomLocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializerProvider) {
        try {
        	if(value == null)
        		return;
            gen.writeString(formatter.format(value));
        } catch (IOException e) {
            throw new JsonParsingException(e);
        }
    }

}