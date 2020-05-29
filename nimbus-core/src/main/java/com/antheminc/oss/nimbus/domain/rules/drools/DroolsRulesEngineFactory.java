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
package com.antheminc.oss.nimbus.domain.rules.drools;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.domain.model.state.RulesRuntime;
import com.antheminc.oss.nimbus.domain.rules.RulesEngineFactory;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 * @author Jayant Chaudhuri
 * @author Swetha Vemuri
 *
 */
@Getter @Setter
public class DroolsRulesEngineFactory implements RulesEngineFactory {

	private final JustLogit logit = new JustLogit(DroolsRulesEngineFactory.class);
	
	private final Collection<DroolsConfigBuilderStrategy> strategies;
	
	public DroolsRulesEngineFactory(BeanResolverStrategy beanResolver) {
		// lookup of multiple DroolsConfigBuilder Strategy implementation beans
		this.strategies = beanResolver.getMultiple(DroolsConfigBuilderStrategy.class);
		
	}
	
	@Override
	public RulesConfig createConfig(String alias) {
		
		// Filter the strategies which are supported for the alias
		List<DroolsConfigBuilderStrategy> supportedStrategies = 
			strategies.stream()
				.filter(strategy -> strategy.isSupported(alias))
				.collect(Collectors.toList());
		
		//Cannot have more than one supported strategy for the same rule alias. Ex. test.drl and test.xls
		if (CollectionUtils.size(supportedStrategies) > 1) {
			throw new InvalidConfigException("Found muliple rule files with the same name: "+alias);	
			
		} else if (CollectionUtils.isEmpty(supportedStrategies)) {
			logit.trace(() -> "No rules file found with alias: "+alias);
			return null;
								
		} else {
			//Build config for the supported strategy
			RulesConfig config = supportedStrategies.get(0).buildConfig(alias);
			return config;		
		} 
	}

	@Override
	public RulesRuntime createRuntime(RulesConfig config) {
		return new DroolsRulesRuntime(config);
	}
}
