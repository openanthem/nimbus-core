package com.anthem.oss.nimbus.core.spec.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

import com.anthem.nimbus.platform.spec.serializer.CustomLocalDateDeserializer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomLocalDateDeserializerTest {

	@InjectMocks
	private CustomLocalDateDeserializer testee;
	
	@Mock
	private JsonParser jsonParser;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void t1_deserialize() throws IOException {
		Mockito.when(this.jsonParser.getText()).thenReturn("1988-10-13");
		final LocalDate result = this.testee.deserialize(this.jsonParser, null);
		Mockito.verify(this.jsonParser, Mockito.times(1)).getText();
		Assert.assertEquals(LocalDate.of(1988, 10, 13), result);
	}
	
	@Test
	public void t2_deserializeDifferentFormat() throws IOException {
		Mockito.when(this.jsonParser.getText()).thenReturn("10/13/1988");
		final LocalDate result = this.testee.deserialize(this.jsonParser, null);
		Mockito.verify(this.jsonParser, Mockito.times(1)).getText();
		Assert.assertEquals(LocalDate.of(1988, 10, 13), result);
	}
	
	@Test
	public void t1_deserializeBadDate() throws IOException {
		Mockito.when(this.jsonParser.getText()).thenReturn("10-13-1988");
		this.thrown.expect(JsonParseException.class);
		this.thrown.expectMessage("Unparseable date: \"10-13-1988\". Supported formats: [yyyy-MM-dd, MM/dd/yyyy]");
		this.testee.deserialize(this.jsonParser, null);
	}
}
