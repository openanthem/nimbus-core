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

import java.io.IOException;

import org.drools.KnowledgeBase;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.drools.io.ResourceFactory;

import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * Implementation of DecisionTable strategy for drools.
 * @author Swetha Vemuri
 * @since 1.2
 */
public class DecisionTableConfigBuilder extends BaseDroolsConfigBuilder implements DroolsConfigBuilderStrategy {
	
	private static final String DECISIONTABLE_SUFFIX = ".xls" ;
	
	JustLogit logit = new JustLogit(getClass()); 
	
	/**
	 * 
	 */
	@Override
	public RulesConfig buildConfig(String alias) {
		String path = alias + DECISIONTABLE_SUFFIX;
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		//check if rule is present in cache
		RulesConfig ruleconfig = ruleConfigurations.get(path);
		
		if(ruleconfig != null) 
			return ruleconfig;
		
		DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		dtconf.setInputType( DecisionTableInputType.XLS );
		
		// Decision table to DRL conversion for debug purposes
		DecisionTableProviderImpl decisionTableProvider = new DecisionTableProviderImpl();
		try {
			String convertedDrl = decisionTableProvider.loadFromInputStream(ResourceFactory.newClassPathResource(path).getInputStream(), dtconf);
			logit.debug(() -> "drl translation of the decision table: "+path
			+"\n" +convertedDrl);
		} catch (IOException e) {
			logit.error(() -> "Could not convert decision table to drl, either correct or delete the decision table: "+path+":", e);
		}
		
		kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DTABLE,dtconf);	
		
		KnowledgeBase kb = buildKnowledgeBase(kbBuilder, path);
		
		ruleconfig = new DroolsRulesConfig(path, kb);
		ruleConfigurations.put(path, ruleconfig);
		
		return ruleconfig;
	}
	
	@Override
	public boolean isSupported(String alias) {
		return evalResource(alias, DECISIONTABLE_SUFFIX);
	}

}
