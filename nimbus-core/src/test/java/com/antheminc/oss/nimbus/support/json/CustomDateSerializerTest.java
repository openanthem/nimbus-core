package com.antheminc.oss.nimbus.support.json;

import java.io.IOException;
import java.time.LocalDate;
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
public class CustomDateSerializerTest {

	@InjectMocks
	private CustomDateSerializer testee;
	
	@Mock
	private JsonGenerator gen;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void t1_serialize() throws IOException {
		Date date = new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime();
		Mockito.doNothing().when(this.gen).writeString(Mockito.anyString());
		this.testee.serialize(date, gen, null);
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(this.gen).writeString(captor.capture());
		Assert.assertEquals("02/11/2018 12:00 AM", captor.getValue());
	}
	
	@Test
	public void t1_serializeBadValue() throws IOException {
		Date date = new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime();
		Mockito.doThrow(new IOException()).when(this.gen).writeString(Mockito.anyString());
		this.thrown.expect(JsonParsingException.class);
		this.testee.serialize(date, gen, null);
	}
}
