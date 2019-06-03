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

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * 
 * @author Tony Lopez
 *
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomLocalDateSerializerTest {

	@InjectMocks
	private CustomLocalDateSerializer testee;
	
	@Mock
	private JsonGenerator gen;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void t1_serialize() throws IOException {
		final LocalDate date = LocalDate.of(1988, 10, 13);
		Mockito.doNothing().when(this.gen).writeString(Mockito.anyString());
		this.testee.serialize(date, gen, null);
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(this.gen).writeString(captor.capture());
		Assert.assertEquals("1988-10-13T00:00:00.000Z", captor.getValue());
	}
	
	@Test
	public void t1_serializeBadValue() throws IOException {
		final LocalDate date = LocalDate.of(1988, 10, 13);
		Mockito.doThrow(new IOException()).when(this.gen).writeString(Mockito.anyString());
		this.thrown.expect(JsonParsingException.class);
		this.testee.serialize(date, gen, null);
	}
}
