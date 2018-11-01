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

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

/**
 * Implementation of drl rule files strategy for Drools
 * @author Swetha Vemuri
 * @since 1.0
 *
 */
public class DrlConfigBuilder extends AbstractDroolsConfigBuilder implements DroolsConfigBuilderStrategy {

	private static final String DRL_SUFFIX = ".drl" ;

	@Override
	public boolean isSupported(String alias) {
		return evalResource(alias, DRL_SUFFIX);
	}

	@Override
	public void createKnowledgeBuilder(KnowledgeBuilder kbBuilder, String path) {
		kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
	}

	@Override
	public String constructRulePath(String alias) {
		return alias + DRL_SUFFIX;
	}

}
