/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.expr;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExpressionEvaluator {

	public Object getValue(String exprValue, Object rootObject);
}
