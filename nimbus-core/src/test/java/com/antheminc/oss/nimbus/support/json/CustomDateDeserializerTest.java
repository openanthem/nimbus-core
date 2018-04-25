package com.antheminc.oss.nimbus.support.json;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
public class CustomDateDeserializerTest {

	@InjectMocks
	private CustomDateDeserializer testee;
	
	@Mock
	private JsonParser jsonParser;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void t1_deserialize() throws IOException {
		Mockito.when(this.jsonParser.getText()).thenReturn("1988-10-13T00:00:00.000Z");
		final Date result = this.testee.deserialize(this.jsonParser, null);
		Mockito.verify(this.jsonParser, Mockito.times(1)).getText();
		Assert.assertEquals(new GregorianCalendar(1988, Calendar.OCTOBER, 13).getTime(), result);
	}
	
	@Test
	public void t1_deserializeBadDate() throws IOException {
		Mockito.when(this.jsonParser.getText()).thenReturn("02-11-2018");
		this.thrown.expect(JsonParseException.class);
		this.thrown.expectMessage("Unparseable date: \"02-11-2018\". Supported format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		this.testee.deserialize(this.jsonParser, null);
	}
}
