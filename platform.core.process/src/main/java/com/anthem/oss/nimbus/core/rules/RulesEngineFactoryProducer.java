/**
 * 
 */
package com.anthem.oss.nimbus.core.rules;

import java.lang.reflect.Field;

/**
 * @author Soham Chakravarti
 *
 */
public interface RulesEngineFactoryProducer {

	public RulesEngineFactory getFactory(Class<?> model);
	
	public RulesEngineFactory getFactory(Field param);
}
