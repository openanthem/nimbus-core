/**
 *  Copyright 2016-2018 the original author or authors.
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

import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;

/**
 * Interface for DroolsConfigBuilder Strategies.
 * 
 * @see com.antheminc.oss.nimbus.domain.rules.drools.DecisionTableConfigBuilder
 * @see com.antheminc.oss.nimbus.domain.rules.drools.DrlConfigBuilder
 * @author Swetha Vemuri
 *
 */
public interface DroolsConfigBuilderStrategy {
	
	RulesConfig buildConfig(String alias);
	
	
	/**
	 * <p>Evaluates if the <tt>alias</tt> is supported by the implemented drools strategy </p>
	 * 
	 * @param alias The rule file alias
	 * @return Returns true if the resource with <tt>alias</tt> is present in the classpath.
	 */
	boolean isSupported(String alias);
}
