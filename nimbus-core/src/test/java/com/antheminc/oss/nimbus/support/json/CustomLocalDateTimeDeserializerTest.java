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

import com.antheminc.oss.nimbus.support.json.CustomLocalDateTimeDeserializer;
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
		this.thrown.expectMessage("Unparseable date: \"10-13-1988\". Supported formats: [yyyy-MM-dd'T'HH:mm:ss.SSS'Z']");
		this.testee.deserialize(this.jsonParser, null);
	}
}

