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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * 
 * @author Mayur Mehta
 *
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomLocalDateTimeSerializerTest {

	@InjectMocks
	private CustomLocalDateTimeSerializer testee;
	
	@Mock
	private JsonGenerator gen;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void t1_serialize() throws IOException {
		final LocalDate date = LocalDate.of(1988, 10, 13);
		final LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.NOON);
		Mockito.doNothing().when(this.gen).writeString(Mockito.anyString());
		this.testee.serialize(dateTime, gen, null);
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(this.gen).writeString(captor.capture());
		Assert.assertEquals("1988-10-13T12:00:00.000Z", captor.getValue());
	}
	
	@Test
	public void t1_serializeBadValue() throws IOException {
		final LocalDate date = LocalDate.of(1988, 10, 13);
		final LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.NOON);
		Mockito.doThrow(new IOException()).when(this.gen).writeString(Mockito.anyString());
		this.thrown.expect(JsonParsingException.class);
		this.testee.serialize(dateTime, gen, null);
	}
}
