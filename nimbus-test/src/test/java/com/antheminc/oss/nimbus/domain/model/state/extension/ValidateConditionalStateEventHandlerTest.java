package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_1;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_2;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * 
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidateConditionalStateEventHandlerTest extends AbstractStateEventHandlerTests {

	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	@Test
	public void t00_init() {
		assertNotNull(_q.getRoot().findParamByPath("/sample_core/attr_validate_nested"));
		assertNotNull(_q.getRoot().findParamByPath("/sample_core/attr_validate_nested/condition"));
		assertNotNull(_q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p1"));
		assertNotNull(_q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p2"));
		assertNotNull(_q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3"));
		assertNotNull(_q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_1"));
	}
	
	@Test
	public void t01_groupValidation_1() {
		Param<String> condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/condition");
		
		Param<String> validate_p1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p1");
		Assert.assertEquals(2, validate_p1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p1.getActiveValidationGroups().length);
		
		Param<String> validate_p2 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p2");
		Assert.assertEquals(validate_p2.getConfig().getValidations().size(), 2);
		Assert.assertEquals(0, validate_p2.getActiveValidationGroups().length);
		
		condition.setState("rigby");
		
		Assert.assertEquals(1, validate_p1.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p1.getActiveValidationGroups()[0]);
		
		Assert.assertEquals(1, validate_p2.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p2.getActiveValidationGroups()[0]);
	}
	
	@Test
	public void t02_groupValidation_2() {
		Param<String> condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/condition");
		
		Param<String> validate_p1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p1");
		Assert.assertEquals(2, validate_p1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p1.getActiveValidationGroups().length);
		
		Param<String> validate_p2 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p2");
		Assert.assertEquals(2, validate_p2.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p2.getActiveValidationGroups().length);
		
		condition.setState("hooli");
		
		Assert.assertEquals(0, validate_p1.getActiveValidationGroups().length);
		
		Assert.assertEquals(1, validate_p2.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_2.class, validate_p2.getActiveValidationGroups()[0]);
	}
	
	@Test
	public void t03_groupValidation_nested_siblingsOnly() {
		Param<String> condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/condition");
		
		Param<String> validate_p3_1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_1");
		Assert.assertEquals(1, validate_p3_1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p3_1.getActiveValidationGroups().length);
		
		Param<String> validate_p3_2_1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_2/validate_p3_2_1");
		Assert.assertEquals(1, validate_p3_2_1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p3_2_1.getActiveValidationGroups().length);
		
		condition.setState("rigby");
		
		Assert.assertEquals(0, validate_p3_1.getActiveValidationGroups().length);
		Assert.assertEquals(0, validate_p3_2_1.getActiveValidationGroups().length);
		
		condition.setState("hooli");
		
		Assert.assertEquals(0, validate_p3_1.getActiveValidationGroups().length);
		Assert.assertEquals(0, validate_p3_2_1.getActiveValidationGroups().length);
	}
	
	@Test
	public void t04_groupValidation_nested_siblingNested() {
		Param<String> nested_condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/nested_condition");
		
		Param<String> validate_p3_1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_1");
		Assert.assertEquals(1, validate_p3_1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p3_1.getActiveValidationGroups().length);
		
		Param<String> validate_p3_2_1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_2/validate_p3_2_1");
		Assert.assertEquals(1, validate_p3_2_1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p3_2_1.getActiveValidationGroups().length);
		
		nested_condition.setState("rigby");
		
		Assert.assertEquals(1, validate_p3_1.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p3_1.getActiveValidationGroups()[0]);
		Assert.assertEquals(1, validate_p3_2_1.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p3_2_1.getActiveValidationGroups()[0]);
		
		nested_condition.setState("hooli");
		
		Assert.assertEquals(1, validate_p3_1.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_2.class, validate_p3_1.getActiveValidationGroups()[0]);
		Assert.assertEquals(1, validate_p3_2_1.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_2.class, validate_p3_2_1.getActiveValidationGroups()[0]);
	}
	
	@Test
	public void t05_removeValidationsNotApplicable() {
		Param<String> condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/condition");
		
		Param<String> validate_p1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p1");
		Assert.assertEquals(2, validate_p1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p1.getActiveValidationGroups().length);
		
		Param<String> validate_p2 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p2");
		Assert.assertEquals(validate_p2.getConfig().getValidations().size(), 2);
		Assert.assertEquals(0, validate_p2.getActiveValidationGroups().length);
		
		condition.setState("rigby");
		
		Assert.assertEquals(1, validate_p1.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p1.getActiveValidationGroups()[0]);
		
		Assert.assertEquals(1, validate_p2.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p2.getActiveValidationGroups()[0]);
		
		condition.setState("hooli");
		
		Assert.assertEquals(0, validate_p1.getActiveValidationGroups().length);
		
		Assert.assertEquals(1, validate_p2.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_2.class, validate_p2.getActiveValidationGroups()[0]);
	}
	
	@Test
	public void t06_validateEventGeneration_sibling() {
		Param<String> condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/condition");
		Param<String> validate_p1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p1");
		Param<String> validate_p2 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p2");
		
		addListener();
		condition.setState("rigby");
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(3, _paramEvents.size());
		
		List<Param<?>> expectedEventParams1 = new ArrayList<>();
		expectedEventParams1.add(validate_p1);
		expectedEventParams1.add(validate_p2);
		expectedEventParams1.add(condition);
		
		_paramEvents.stream().forEach(pe->expectedEventParams1.remove(pe.getParam()));
		assertTrue(expectedEventParams1.isEmpty());
		
		condition.setState("hooli");
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(3, _paramEvents.size());
		
		List<Param<?>> expectedEventParams2 = new ArrayList<>();
		expectedEventParams2.add(validate_p1);
		expectedEventParams2.add(validate_p2);
		expectedEventParams2.add(condition);
		
		_paramEvents.stream().forEach(pe->expectedEventParams2.remove(pe.getParam()));
		assertTrue(expectedEventParams2.isEmpty());
	}
	
	@Test
	public void t07_validateEventGeneration_nestedSibling() {
		Param<String> nested_condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/nested_condition");
		Param<String> validate_p1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p1");
		Param<String> validate_p2 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p2");
		Param<String> validate_p3_1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_1");
		Param<String> validate_p3_2_1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_2/validate_p3_2_1");
		Param<String> validate_p3_2_2 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p3/validate_p3_2/validate_p3_2_2");
		
		addListener();
		nested_condition.setState("rigby");
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(5, _paramEvents.size());
		
		List<Param<?>> expectedEventParams1 = new ArrayList<>();
		expectedEventParams1.add(validate_p1);
		expectedEventParams1.add(validate_p2);
		expectedEventParams1.add(validate_p3_1);
		expectedEventParams1.add(validate_p3_2_1);
		expectedEventParams1.add(nested_condition);
		
		_paramEvents.stream().forEach(pe->expectedEventParams1.remove(pe.getParam()));
		assertTrue(expectedEventParams1.isEmpty());
		
		nested_condition.setState("hooli");
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(6, _paramEvents.size());
		
		List<Param<?>> expectedEventParams2 = new ArrayList<>();
		expectedEventParams2.add(validate_p1);
		expectedEventParams2.add(validate_p2);
		expectedEventParams2.add(validate_p3_1);
		expectedEventParams2.add(validate_p3_2_1);
		expectedEventParams2.add(validate_p3_2_2);
		expectedEventParams2.add(nested_condition);
		
		_paramEvents.stream().forEach(pe->expectedEventParams2.remove(pe.getParam()));
		assertTrue(expectedEventParams2.isEmpty());
	}
	
	@Test
	public void t08_validationRemoval() {
		Param<String> condition = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/condition");
		
		Param<String> validate_p1 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p1");
		Assert.assertEquals(2, validate_p1.getConfig().getValidations().size());
		Assert.assertEquals(0, validate_p1.getActiveValidationGroups().length);
		
		Param<String> validate_p2 = _q.getRoot().findParamByPath("/sample_core/attr_validate_nested/validate_p2");
		Assert.assertEquals(validate_p2.getConfig().getValidations().size(), 2);
		Assert.assertEquals(0, validate_p2.getActiveValidationGroups().length);
		
		condition.setState("rigby");
		
		Assert.assertEquals(1, validate_p1.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p1.getActiveValidationGroups()[0]);
		
		Assert.assertEquals(1, validate_p2.getActiveValidationGroups().length);
		Assert.assertEquals(GROUP_1.class, validate_p2.getActiveValidationGroups()[0]);
		
		addListener();
		condition.setState("unknown");
		
		Assert.assertEquals(0, validate_p1.getActiveValidationGroups().length);
		Assert.assertEquals(0, validate_p2.getActiveValidationGroups().length);
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(3, _paramEvents.size());
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(validate_p1);
		expectedEventParams.add(validate_p2);
		expectedEventParams.add(condition);
		
		_paramEvents.stream().forEach(pe->expectedEventParams.remove(pe.getParam()));
		assertTrue(expectedEventParams.isEmpty());
	}
}
