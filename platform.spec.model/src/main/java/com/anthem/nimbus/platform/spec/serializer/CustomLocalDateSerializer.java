package com.anthem.nimbus.platform.spec.serializer;

import com.anthem.nimbus.platform.spec.model.exception.JSonParsingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author Cheikh Niass on 12/2/16.
 */
public class CustomLocalDateSerializer extends StdSerializer<LocalDate> {
    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("M/d/yyyy");

    public CustomLocalDateSerializer() {
        this(null);
    }

    public CustomLocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializerProvider) {
        try {
            gen.writeString(formatter.format(value));
        } catch (IOException e) {
            throw new JSonParsingException(e);
        }
    }

}
