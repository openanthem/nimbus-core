/**
 * 
 */
package com.anthem.nimbus.platform.utils.converter;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;

/**
 * @author Soham Chakravarti
 *
 */
public class CommandMessageConverterTest {

	private static CommandMessageConverter converter;
	
	@BeforeClass
	public static void beforeClass() {
		converter = new CommandMessageConverter();
	}
	
	@Test
	public void t_convertStringToJson() {
		String str = "some value";
		
		String json = converter.convert(str);
		System.out.println("JSON: "+ json);
		
		String res = converter.convert(String.class, json);
		assertEquals(str, res);
	}
	
	@Test
	public void t_convertObjWithPrivateBlankConstructor() {
		ClassWithBlankPrivateConstructor obj = new ClassWithBlankPrivateConstructor("some data");
		String json = converter.convert(obj);
		
		System.out.println("JSON: "+json);
		
		ClassWithBlankPrivateConstructor res = converter.convert(ClassWithBlankPrivateConstructor.class, json);
		assertEquals(obj.getNeeded(), res.getNeeded());
	}
}
