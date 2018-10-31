/**
 * 
 */
package com.antheminc.oss.nimbus.domain.rules.drools;

import java.util.Map;

import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;

/**
 * @author Swetha Vemuri
 *
 */
public interface RulesConfigBuilderStrategy {
	
	RulesConfig buildConfig(String alias, Map<String,RulesConfig> ruleConfigurations);
}
