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
package com.anthem.oss.nimbus.core.rules.drools;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;
import com.anthem.oss.nimbus.core.domain.model.state.RulesRuntime;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactory;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 * @author Jayant Chaudhuri
 *
 */
public class DroolsRulesEngineFactory implements RulesEngineFactory {

	JustLogit logit = new JustLogit(getClass());
	
	Map<String,RulesConfig> ruleConfigurations = new ConcurrentHashMap<String,RulesConfig>();
	
	@Override
	public RulesConfig createConfig(String alias) {
		String path = alias + ".drl";
		RulesConfig config = ruleConfigurations.get(path);
		if(config != null)
			return config;
		
		URL verifyUrl = getClass().getClassLoader().getResource(path);
		if(verifyUrl==null) {
			logit.info(()->"No rules file found at location: "+verifyUrl);
			return null;
			
		} else { 
			logit.info(()->"Rules file found at location: "+verifyUrl);
		}
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
		
		KnowledgeBase kb = null;
		try {
			kb = kbBuilder.newKnowledgeBase();
			kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
		} catch (Exception e) {
			throw new FrameworkRuntimeException("Could not build knowledgeBase, either correct or delete the drl file :"+path+": ", e);
		}
		config = new DroolsRulesConfig(path, kb);
		ruleConfigurations.put(path, config);
		return config;
	}

	@Override
	public RulesRuntime createRuntime(RulesConfig config) {
		DroolsRulesRuntime runtime = new DroolsRulesRuntime(config);
		return runtime;
	}

}
