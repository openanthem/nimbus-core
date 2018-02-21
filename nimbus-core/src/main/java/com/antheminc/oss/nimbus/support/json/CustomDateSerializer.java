package com.antheminc.oss.nimbus.support.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDateSerializer extends StdSerializer<Date>{

	private static final long serialVersionUID = 1L;
	
	private final CustomLocalDateTimeSerializer serializer;

    public CustomDateSerializer() {
        this(null);
    }
    
	protected CustomDateSerializer(Class<Date> t) {
		super(t);
		this.serializer = new CustomLocalDateTimeSerializer();
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
	 	if(value == null)
    		return;
    	
	 	this.serializer.serialize(LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()), gen, provider);
	}

}
