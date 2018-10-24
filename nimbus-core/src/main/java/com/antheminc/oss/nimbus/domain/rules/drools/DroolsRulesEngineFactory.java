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

import org.drools.KnowledgeBase;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.drools.io.ResourceFactory;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
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
	
	Map<String,RulesConfig> ruleConfigurations = new ConcurrentHashMap<String,RulesConfig>();
	
	@Override
	public RulesConfig createConfig(String alias) {
		
		String drlpath = alias + ".drl";
		String dtablepath = alias + ".xls";
		
		URL verifyDrlUrl = getClass().getClassLoader().getResource(drlpath);
		URL verifyDtableUrl = getClass().getClassLoader().getResource(dtablepath);
		
		if(verifyDrlUrl != null && verifyDtableUrl != null) {
			throw new FrameworkRuntimeException("Found both rules file and decision table with the same name: "+alias);		
		}
		
		RulesConfig drlConfig = createDRLConfig(drlpath);
		RulesConfig dtableConfig = createDecisionTableConfig(dtablepath);
			
		if(drlConfig == null && dtableConfig == null) {
			logit.trace(()->"No rules file: ["+drlpath+"] or decision table: ["+dtablepath+"] found at location");
			return null;	
		} else
			return drlConfig != null ? drlConfig : dtableConfig;
	}

	@Override
	public RulesRuntime createRuntime(RulesConfig config) {
		DroolsRulesRuntime runtime = new DroolsRulesRuntime(config);
		return runtime;
	}
	
	private RulesConfig createDRLConfig(String drlpath) {
		RulesConfig drlconfig = ruleConfigurations.get(drlpath);
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		if(drlconfig != null)
			return drlconfig;
		
		URL verifyUrl = getClass().getClassLoader().getResource(drlpath);
		
		if(verifyUrl == null) {
			logit.trace(()->"No rules file found at location: "+drlpath);
			return null;
		} else {
			logit.trace(()->"Rules file found at location: "+verifyUrl);
			kbBuilder.add(ResourceFactory.newClassPathResource(drlpath), ResourceType.DRL);
			
			KnowledgeBase kb = null;
			try {
				kb = kbBuilder.newKnowledgeBase();
				kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
			} catch (Exception e) {
				throw new FrameworkRuntimeException("Could not build knowledgeBase, either correct or delete the drl file :"+drlpath+": ", e);
			}
			
			drlconfig = new DroolsRulesConfig(drlpath, kb);
			ruleConfigurations.put(drlpath, drlconfig);
			return drlconfig;
		}
	}
	
	private RulesConfig createDecisionTableConfig(String dtablepath) {
		RulesConfig dtableconfig = ruleConfigurations.get(dtablepath);
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		if(dtableconfig != null)
			return dtableconfig;
		
		URL verifyUrl = getClass().getClassLoader().getResource(dtablepath);
		
		if(verifyUrl == null) {
			logit.trace(()->"No decision table file found at location: "+dtablepath);
			return null;
		} else {
			logit.trace(()->"Decision table found at location: "+verifyUrl);
			DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
			dtconf.setInputType( DecisionTableInputType.XLS );
			
			kbBuilder.add(ResourceFactory.newClassPathResource(dtablepath), ResourceType.DTABLE,dtconf);
				
			DecisionTableProviderImpl decisionTableProvider = new DecisionTableProviderImpl();
			try {
				String convertedDrl = decisionTableProvider.loadFromInputStream(ResourceFactory.newClassPathResource(dtablepath).getInputStream(), dtconf);
				logit.info(() -> "drl translation of the decision table: "+dtablepath
				+"\n" +convertedDrl);
			} catch (IOException e) {
				logit.error(() -> "Could not convert decision table to drl, either correct or delete the decision table: "+dtablepath+":", e);
			}

			KnowledgeBase kb = null;
			try {
				kb = kbBuilder.newKnowledgeBase();
				kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
			} catch (Exception e) {
				throw new FrameworkRuntimeException("Could not build knowledgeBase, either correct or delete the drl file :"+dtablepath+": ", e);
			}
			
			dtableconfig = new DroolsRulesConfig(dtablepath, kb);
			ruleConfigurations.put(dtablepath, dtableconfig);
			return dtableconfig;
		}
	}
}
