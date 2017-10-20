/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.expr;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;

/**
 * @author Swetha Vemuri
 *
 */
@RunWith(SpringRunner.class)
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
	public void t02_evaluate_contains_multiple_and_elements_expression() {
		String expr = "state != null && state.?[#this=='Apple'] != new String[]{} && state.?[#this=='Google'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
	}
	
	@Test
	public void t03_evaluate_equals_expression() {		
		String expr = "state == new String[]{'Apple','Google','Facebook',' ','Hewlett Packard', null}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
		assertTrue(result);
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
		String expr = "state != null && (state.?[#this=='Apple'] != new String[]{} || state.?[#this=='Twitter'] != new String[]{})";
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
	public void t08_evaluate_contains_single_element_expression() {		
		String expr = "state != null && state.?[#this=='Apple'] != new String[]{}";
		Boolean result = expressionEvaluator.getValue(expr, new Holder<>(strArr), Boolean.class);
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
}
