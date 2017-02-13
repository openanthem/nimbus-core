/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.config;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.entity.Findable;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;

/**
 * @author Soham Chakravarti
 *
 */
public interface DomainConfig extends Findable<String> {

	public Domain getDomain();
	
	default String getAlias() {
		return getDomain().value();
	}
	
	public List<ActionExecuteConfig<?, ?>> getActionExecuteConfigs();
	public CollectionsTemplate<List<ActionExecuteConfig<?, ?>>, ActionExecuteConfig<?, ?>> templateActionConfigs();
	
}
