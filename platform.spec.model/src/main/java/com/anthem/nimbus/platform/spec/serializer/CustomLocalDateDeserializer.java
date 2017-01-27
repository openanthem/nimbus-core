package com.anthem.nimbus.platform.spec.serializer;

import com.anthem.nimbus.platform.spec.model.exception.JSonParsingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author Cheikh Niass on 12/2/16.
 */
public class CustomLocalDateDeserializer extends StdDeserializer<LocalDate> {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    public CustomLocalDateDeserializer() {
        this(null);
    }

    public CustomLocalDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context) {
        String date = null;
        try {
            date = jsonparser.getText();
            return !StringUtils.isEmpty(date) ? LocalDate.parse(date, formatter) : null;
        } catch (IOException e) {
            throw new JSonParsingException(e);
        }

    }
}

