/**
 * 
 */
package com.antheminc.nimbus.platform.core.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.antheminc.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamType;
import com.antheminc.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.antheminc.oss.nimbus.core.domain.model.config.builder.EntityConfigVisitor;
import com.antheminc.oss.nimbus.core.entity.person.Address;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti 
 *
 */
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelConfigHandlerTest extends AbstractFrameworkIntegrationTests {

	@Autowired EntityConfigBuilder handler;
	
	@Model @Getter @Setter
	public static class TestBaseModel {
		private String id;
		
		
	}
	
	@Getter @Setter
	public static class TestNestedModel<ID extends Serializable, A extends Address<ID>> extends TestBaseModel {
		private Set<A> addresses;
	}
	
	@Domain("test") @Getter @Setter
	public static class TestModel extends TestBaseModel {
		
		private int primitive_int;
		private Integer class_int;
		
		private List<Long> collection_primitive_long;
		private LocalDate localDate;
		
		//==private TestNestedModel<String, Address.IdString> nested;
		
	}
	
	private ModelConfig<TestModel> mConfig;
	
	@Before
	public void before() {
		this.mConfig = this.handler.load(TestModel.class, new EntityConfigVisitor());
		Assert.assertNotNull(mConfig);
	}
	
	@Test
	public void test_id() {
		ParamConfig<?> p = mConfig.templateParams().find("id");
		Assert.assertNotNull(p);
		Assert.assertEquals("id", p.getCode());
		Assert.assertFalse(p.getType().isNested());
		Assert.assertEquals("string", p.getType().getName());
		Assert.assertNull(p.getType().findIfCollection());
	}
	
	@Test
	public void test_primitive_int() {
		ParamConfig<?> p = mConfig.templateParams().find("primitive_int");
		Assert.assertNotNull(p);
		Assert.assertFalse(p.getType().isNested());
		Assert.assertEquals("integer", p.getType().getName());
		Assert.assertNull(p.getType().findIfCollection());
	}
	
	@Test
	public void test_class_int() {
		ParamConfig<?> p = mConfig.templateParams().find("class_int");
		Assert.assertNotNull(p);
		Assert.assertFalse(p.getType().isNested());
		Assert.assertEquals("integer", p.getType().getName());
		Assert.assertNull(p.getType().findIfCollection());
	}
	
	@Test 
	public void test_collection_primitive_long() {
		ParamConfig<?> p = mConfig.templateParams().find("collection_primitive_long");
		Assert.assertNotNull(p);
		Assert.assertTrue(p.getType().isNested());
		Assert.assertEquals("ArrayList", p.getType().getName());
		Assert.assertSame(ParamType.CollectionType.list, p.getType().findIfCollection().getCollectionType());
	}
	
	@Test
	public void test_localDate() {
		ParamConfig<?> p = mConfig.templateParams().find("localDate");
		Assert.assertNotNull(p);
		Assert.assertFalse(p.getType().isNested());
		Assert.assertEquals("date", p.getType().getName());
		Assert.assertNull(p.getType().findIfCollection());
	}
	
	@Ignore
	@Test
	public void test1_nested() {
		ParamConfig<?> p = mConfig.templateParams().find("nested");
		Assert.assertNotNull(p);
		Assert.assertTrue(p.getType().isNested());
		Assert.assertEquals(TestNestedModel.class.getSimpleName(), p.getType().getName());
		Assert.assertNull(p.getType().findIfCollection());
		
		ModelConfig<?> nmNested = ((ParamType.Nested)p.getType()).getModel();
		Assert.assertNotNull(nmNested);
	}
	
}
