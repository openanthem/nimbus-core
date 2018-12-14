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

import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.drools.io.ResourceFactory;

import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * Implementation of DecisionTable strategy for drools.
 * @author Swetha Vemuri
 * @since 2.0
 */
public class DecisionTableConfigBuilder extends AbstractDroolsConfigBuilder implements DroolsConfigBuilderStrategy {
	
	private static final String DECISIONTABLE_SUFFIX = ".xls" ;
	
	private final JustLogit logit = new JustLogit(DecisionTableConfigBuilder.class); 
	
	@Override
	public boolean isSupported(String alias) {
		return evalResource(alias, DECISIONTABLE_SUFFIX);
	}

	@Override
	public String constructRulePath(String alias) {
		return alias + DECISIONTABLE_SUFFIX;
	}

	@Override
	public void createKnowledgeBuilder(KnowledgeBuilder kbBuilder, String path) {
		DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		dtconf.setInputType( DecisionTableInputType.XLS );
		
		try {
			// Decision table to DRL conversion for debug purposes
			DecisionTableProviderImpl decisionTableProvider = new DecisionTableProviderImpl();
			String convertedDrl = decisionTableProvider.loadFromInputStream(ResourceFactory.newClassPathResource(path).getInputStream(), dtconf);
			logit.debug(() -> "drl translation of the decision table: "+path
			+"\n" +convertedDrl);
		} catch (IOException e) {
			logit.error(() -> "Could not convert decision table to drl, either correct or delete the decision table: "+path+":", e);
		}
		kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DTABLE,dtconf);	
	}

}
