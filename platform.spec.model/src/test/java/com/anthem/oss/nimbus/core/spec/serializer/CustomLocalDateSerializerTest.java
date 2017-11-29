package com.anthem.oss.nimbus.core.spec.serializer;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
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

import com.anthem.nimbus.platform.spec.serializer.CustomLocalDateSerializer;
import com.anthem.oss.nimbus.core.util.JsonParsingException;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * 
 * @author Tony Lopez (AF42192)
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
		Assert.assertEquals("10/13/1988", captor.getValue());
	}
	
	@Test
	public void t1_serializeBadValue() throws IOException {
		final LocalDate date = LocalDate.of(1988, 10, 13);
		Mockito.doThrow(new IOException()).when(this.gen).writeString(Mockito.anyString());
		this.thrown.expect(JsonParsingException.class);
		this.testee.serialize(date, gen, null);
	}
}
