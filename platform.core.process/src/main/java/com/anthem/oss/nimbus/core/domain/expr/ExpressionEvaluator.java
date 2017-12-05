/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.expr;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExpressionEvaluator {

	public Object getValue(String exprValue, Object rootObject);
	
	public <T> T getValue(String exprValue, Object rootObject, Class<T> returnType);
}
