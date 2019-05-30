/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.support.expr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.support.Holder;

/**
 * @author Swetha Vemuri
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpelExpressionEvaluatorTest {
	
	private ExpressionEvaluator expressionEvaluator = new SpelExpressionEvaluator();
	private static final String[] strArr = {"Apple","Google","Facebook"," ","Hewlett Packard", null};
	
	@Test
	public void t01_evaluate_null_array_expressions() {		
		String expr = "state != null";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}

	@Test
	public void t02_evaluate_exact_equals_expression() {		
		String expr = "state == new String[]{'Apple','Google','Facebook',' ','Hewlett Packard', null}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}

	@Test
	public void t03a_PREFERRED_UNORDERED_evaluate_contains_multiple_and_elements_expression() {
		String expr = "state != null && state.?[#this=='Apple'] != new String[]{} && state.?[#this=='Google'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}
	
	@Test
	public void t03b_ORDER_KNOWN_evaluate_contains_multiple_and_elements_expression() {
		String expr = "state!=null && state.?[#this=='Apple' || #this=='Google'] == new String[]{'Apple', 'Google'}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}
	
	@Test
	public void t03b_ORDER_UNKNOWN_FAIL_evaluate_contains_multiple_and_elements_expression() {
		String expr = "state!=null && state.?[#this=='Apple' || #this=='Google'] == new String[]{'Google', 'Apple'}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		// fail
		assertFalse(result);
	}
	
	@Test
	public void t03d_PREFERRED_NEGATIVE_evaluate_contains_multiple_and_elements_expression() {
		String expr = "state != null && state.?[#this=='XYZ'] != new String[]{} && state.?[#this=='Google'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertFalse(result);
	}
	
	@Test
	public void t04_evaluate_equals_non_empty_expression() {		
		String expr = "state == new String[]{'Apple','Google','Facebook','Hewlett Packard', null}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertFalse(result);
	}
	
	@Test
	public void t05_evaluate_equals_non_null_expression() {		
		String expr = "state == new String[]{'Apple','Google','Facebook','Hewlett Packard'}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertFalse(result);
	}
	
	@Test
	public void t06_evaluate_contains_multiple_or_elements_elements_expression() {		
		String expr = "state!=null && state.?[#this=='Apple' || #this=='Google'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}
	
	@Test
	public void t07_evaluate_compare_arraylength_expression() {		
		String expr = "state != null && state.length > 2";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}
	
	@Test
	public void t08a_evaluate_contains_single_element_expression() {		
		String expr = "state != null && state.?[#this=='Apple'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}
	
	@Test
	public void t08b_evaluate_exatcly_one_single_element_expression() {		
		String expr = "state == new String[]{'Apple'}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(new String[]{"Apple"}), Boolean.class);
		assertTrue(result);
	}
	
	@Test
	public void t09_evaluate_invalid_elements_expression() {		
		String expr = "state != null && state.?[#this=='Twitter'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertFalse(result);
	}
	
	@Test
	public void t10_evaluate_type_mismatch_expression() {		
		String expr = "state != null && state == 'Apple'";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertFalse(result);
	}
	
	@Test
	public void t12_evaluate_contains_multiple_and_elements_withinvalid_expression() {
		String expr = "state != null && state.?[#this=='Apple'] != new String[]{} && state.?[#this=='Twitter'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertFalse(result);
	}
	
	@Test
	public void t13_evaluate_findParamByState_expression() {
		
		Param<?> onChangeParam = Mockito.mock(Param.class);
		@SuppressWarnings("rawtypes")
		ParamStateHolder holder = Mockito.mock(ParamStateHolder.class);
		
		Mockito.when(holder.getState()).thenReturn("foo");		
		Mockito.when(holder.findStateByPath("somepath")).thenReturn("foo");
		
		String expr = "state == findStateByPath('somepath')";
		Boolean result = expressionEvaluator.getValue(expr, new ParamStateHolder<>(onChangeParam), Boolean.class);
		assertTrue(result);
	}
}
