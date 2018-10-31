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
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.drools.KnowledgeBase;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.drools.io.ResourceFactory;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.domain.model.state.RulesRuntime;
import com.antheminc.oss.nimbus.domain.rules.RulesEngineFactory;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Soham Chakravarti
 * @author Jayant Chaudhuri
 * @author Swetha Vemuri
 *
 */
public class DroolsRulesEngineFactory implements RulesEngineFactory {

	JustLogit logit = new JustLogit(getClass());
	private static final String DRL_EXTENSION = ".drl" ;
	private static final String DECISIONTABLE_EXTENSION = ".xls" ;
	
	private final DrlConfigBuilder drlbuilder;
	private final DecisionTableConfigBuilder dtablebuilder;
	
	public DroolsRulesEngineFactory(BeanResolverStrategy beanResolver) {
		this.drlbuilder = beanResolver.get(DrlConfigBuilder.class);
		this.dtablebuilder = beanResolver.get(DecisionTableConfigBuilder.class);
	}
	Map<String,RulesConfig> ruleConfigurations = new ConcurrentHashMap<String,RulesConfig>();
	
	@Override
	public RulesConfig createConfig(String alias) {
		RulesConfig config = null;
		
		String drlpath = alias + DRL_EXTENSION;
		String dtablepath = alias + DECISIONTABLE_EXTENSION;
		
		URL verifyDrlUrl = getClass().getClassLoader().getResource(drlpath);
		URL verifyDtableUrl = getClass().getClassLoader().getResource(dtablepath);
		
		if (verifyDrlUrl != null && verifyDtableUrl != null) {
			throw new FrameworkRuntimeException("Found both drl file and decision table with the same name: "+alias);	
			
		} else if (verifyDrlUrl != null) {
			logit.trace(()->"Rules file found at location: "+verifyDrlUrl);
			config = drlbuilder.buildConfig(drlpath, ruleConfigurations);
			return config;
			
		} else if (verifyDtableUrl != null) {
			logit.trace(()->"Decision table found at location: "+verifyDtableUrl);
			config = dtablebuilder.buildConfig(dtablepath, ruleConfigurations);
			return config;
			
		} else {
			logit.debug(() -> "No rules file found with alias: "+alias);
			return null;
		}
	}

	@Override
	public RulesRuntime createRuntime(RulesConfig config) {
		DroolsRulesRuntime runtime = new DroolsRulesRuntime(config);
		return runtime;
	}
	
	private RulesConfig createRulesConfig(String path) {	
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		RulesConfig ruleconfig = ruleConfigurations.get(path);
		
		if(ruleconfig != null) 
			return ruleconfig;
	
		if (StringUtils.endsWith(path, DRL_EXTENSION)) {
			
			kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
			
		} else if(StringUtils.endsWith(path, DECISIONTABLE_EXTENSION)) {
			
			DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
			dtconf.setInputType( DecisionTableInputType.XLS );
			
			DecisionTableProviderImpl decisionTableProvider = new DecisionTableProviderImpl();
			try {
				String convertedDrl = decisionTableProvider.loadFromInputStream(ResourceFactory.newClassPathResource(path).getInputStream(), dtconf);
				logit.debug(() -> "drl translation of the decision table: "+path
				+"\n" +convertedDrl);
			} catch (IOException e) {
				logit.error(() -> "Could not convert decision table to drl, either correct or delete the decision table: "+path+":", e);
			}
			
			kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DTABLE,dtconf);				
		}
			
		KnowledgeBase kb = null;
		try {
			kb = kbBuilder.newKnowledgeBase();
			kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
		} catch (Exception e) {
			throw new FrameworkRuntimeException("Could not build knowledgeBase, either correct or delete the drl file :"+path+": ", e);
		}
		
		ruleconfig = new DroolsRulesConfig(path, kb);
		ruleConfigurations.put(path, ruleconfig);
		return ruleconfig;
	}
}
