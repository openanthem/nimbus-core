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

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;

/**
 * <p>BaseDroolsConfigBuilder acts as a base class for building drools config for different 
 * drools implementation strategies.
 * 
 * @see com.antheminc.oss.nimbus.domain.rules.drools.DecisionTableConfigBuilder
 * @see com.antheminc.oss.nimbus.domain.rules.drools.DrlConfigBuilder
 * @author Swetha Vemuri
 * @since 2.0
 *
 */
public abstract class AbstractDroolsConfigBuilder {
	
	protected Map<String,RulesConfig> ruleConfigurations = new ConcurrentHashMap<String,RulesConfig>();
	
	
	public RulesConfig buildConfig(String alias) {
		String path = constructRulePath(alias);
		//check if rule is present in cache
		RulesConfig ruleconfig = ruleConfigurations.get(path);
		
		if(ruleconfig != null) 
			return ruleconfig;
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		createKnowledgeBuilder(kbBuilder, path);
		
		KnowledgeBase kb = buildKnowledgeBase(kbBuilder, path);
		
		ruleconfig = new DroolsRulesConfig(path, kb);
		ruleConfigurations.put(path, ruleconfig);
		
		return ruleconfig;
	}
	
	/**
	 * Builds the knowledgeBase for drools runtime
	 * @param kbBuilder
	 * @param path - the rule fine name as it appears on classpath(with extension. Ex: test.drl, test.xls)
	 * @return
	 */
	final public KnowledgeBase buildKnowledgeBase(KnowledgeBuilder kbBuilder, String path) {
		
		try {
			KnowledgeBase kb = kbBuilder.newKnowledgeBase();
			kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
			return kb;
		} catch (Exception e) {
			throw new FrameworkRuntimeException("Could not build knowledgeBase, either correct or delete the rule file :"+path+": ", e);
		}
	}
	/**
	 * Evaluates the presence of a rule resource in classpath 
	 * based on the rule name and the type of extension.
	 * @param alias
	 * @param type
	 * @return
	 */
	public boolean evalResource(String alias, String type) {
		String path = alias + type;
		
		URL verifyUrl = getClass().getClassLoader().getResource(path);
		
		if (verifyUrl != null)
			return true;
		
		return false;
	}
	abstract public String constructRulePath(String alias);
	
	abstract public void createKnowledgeBuilder(KnowledgeBuilder kbBuilder, String path);
}
