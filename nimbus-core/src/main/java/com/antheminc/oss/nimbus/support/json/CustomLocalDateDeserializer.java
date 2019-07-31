/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.support.json;

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
 * @Author Mayur Mehta, Tony Lopez
 */
public class CustomLocalDateDeserializer extends StdDeserializer<LocalDate> {

	private static final long serialVersionUID = 1L;
	
	private static final String[] DATE_FORMATS = new String[] { "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" };
	private static final String NULL_STRING = "null";

	public CustomLocalDateDeserializer() {
		this(null);
	}

	public CustomLocalDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
//		LocalDateTime ldtDate = this.deserializer.deserialize(jsonparser, context);
//		return null==ldtDate ? null : ldtDate.toLocalDate();
		String date = jsonparser.getText();
		if (StringUtils.isEmpty(date) || NULL_STRING.equals(date)) {
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
