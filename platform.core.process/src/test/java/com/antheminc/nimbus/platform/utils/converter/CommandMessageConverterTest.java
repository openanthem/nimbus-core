/**
 * 
 */
package com.antheminc.nimbus.platform.utils.converter;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import ch.mfrey.jackson.antpathfilter.AntPathFilterMixin;
import ch.mfrey.jackson.antpathfilter.AntPathPropertyFilter;

/**
 * @author Soham Chakravarti
 * @author Rakesh Patel
 *
 */
public class CommandMessageConverterTest {

	private static CommandMessageConverter converter;
	
	private static ObjectMapper mapper;
	
	//private static FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", new TheFilter());

	static String[] filter = new String[] {"**","-**.fName"};
	private static FilterProvider filters = new SimpleFilterProvider().addFilter("antPathFilter", new AntPathPropertyFilter(filter));

	private static BeanResolverStrategy beanResolver = Mockito.mock(BeanResolverStrategy.class);
	
	@BeforeClass
	public static void beforeClass() {
		
		mapper = new ObjectMapper();
		mapper.addMixIn(Object.class, AntPathFilterMixin.class);
		mapper.setFilters(filters);
		Mockito.when(beanResolver.get(ObjectMapper.class)).thenReturn(mapper);
		
		converter = new CommandMessageConverter(beanResolver);
	}
	
	@Test
	public void t0_convertStringToJson() {
		String str = "some value";
		
		String json = converter.convert(str);
		System.out.println("JSON: "+ json);
		
		String res = converter.convert(String.class, json);
		assertEquals(str, res);
	}
	
	@Test
	public final void t1_convertObjToJsonWithDefaultView() throws JsonProcessingException {
		
		TestObj<String> obj = new TestObj<String>();
		obj.setfName("Rakesh");
		obj.setlName("Patel");
		obj.setName("Rakesh Patel");
		
		TestObjWrapper wrapper = new TestObjWrapper();
		wrapper.setTestObj(obj);
		
		String json = mapper.writeValueAsString(wrapper);
		// final ObjectMapper mapperss = new ObjectMapper();
		//String json = mapperss.writer(filters).writeValueAsString(wrapper);
		System.out.println("JSON: "+ json);
		
		
		TestObj<String> obj2 = new TestObj<String>();
		obj2.setfName("Swetha");
		obj2.setlName("Patel");
		obj2.setName("Rakesh Patel");
		
		TestObjWrapper wrapper2 = new TestObjWrapper();
		wrapper2.setTestObj(obj2);
		String json2 = mapper.writeValueAsString(wrapper2);
		System.out.println("JSON2: "+ json2);
	}
	
	@Test
	public void t2_convertObjToJsonWithReplaceView() throws JsonProcessingException {
		TestObj<String> obj = new TestObj<String>();
		obj.setfName("Rakesh");
		obj.setlName("Patel");
		obj.setName("Rakesh Patel");
		
//		String json = mapper.writerWithView(Views.Replace.class).writeValueAsString(obj);
//		System.out.println("JSON: "+ json);
//		
		
	}
	
	@Test
	public void t3_convertObjWithPrivateBlankConstructor() {
		ClassWithBlankPrivateConstructor obj = new ClassWithBlankPrivateConstructor("some data");
		String json = converter.convert(obj);
		
		System.out.println("JSON: "+json);
		
		ClassWithBlankPrivateConstructor res = converter.convert(ClassWithBlankPrivateConstructor.class, json);
		assertEquals(obj.getNeeded(), res.getNeeded());
	}
	
	//@JsonFilter("antPathFilter")
	class TestObjWrapper {
		
		public TestObj testObj;

		public TestObj getTestObj() {
			return testObj;
		}

		public void setTestObj(TestObj testObj) {
			this.testObj = testObj;
		}
		
	}
	
	class TestObj<T> implements ITestObj<T> {
		
		private String fName;
		
		private String lName;
		
		private T name;

		public String getfName() {
			return fName;
		}

		public void setfName(String fName) {
			this.fName = fName;
		}
		
		public String getlName() {
			return lName;
		}

		public void setlName(String lName) {
			this.lName = lName;
		}

		public T getName() {
			return name;
		}

		public void setName(T name) {
			this.name = name;
		}
	}
	
	interface ITestObj<T> {
		
		//@JsonView(Views.Default.class)
		public String getfName();
		
		//@JsonView(Views.Replace.class)
		public String getlName();
		
		public T getName();
		
	}
	
	
}
