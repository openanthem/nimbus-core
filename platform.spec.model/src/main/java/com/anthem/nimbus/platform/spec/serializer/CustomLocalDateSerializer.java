/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.nimbus.platform.spec.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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