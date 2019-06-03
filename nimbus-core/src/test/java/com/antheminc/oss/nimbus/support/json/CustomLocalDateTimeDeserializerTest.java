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
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

/**
 * 
 * @author Mayur Mehta
 *
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomLocalDateTimeDeserializerTest {

	@InjectMocks
	private CustomLocalDateTimeDeserializer testee;
	
	@Mock
	private JsonParser jsonParser;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void t1_deserialize() throws IOException {
		Mockito.when(this.jsonParser.getText()).thenReturn("1988-10-13T12:00:00.000Z");
		final LocalDateTime result = this.testee.deserialize(this.jsonParser, null);
		Mockito.verify(this.jsonParser, Mockito.times(1)).getText();
		Assert.assertEquals(LocalDateTime.of(LocalDate.of(1988, 10, 13), LocalTime.NOON), result);
	}
	
	@Test
	public void t1_deserializeBadDate() throws IOException {
		Mockito.when(this.jsonParser.getText()).thenReturn("10-13-1988");
		this.thrown.expect(JsonParseException.class);
		this.thrown.expectMessage("Unparseable date: \"10-13-1988\". Supported format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		this.testee.deserialize(this.jsonParser, null);
	}
}

