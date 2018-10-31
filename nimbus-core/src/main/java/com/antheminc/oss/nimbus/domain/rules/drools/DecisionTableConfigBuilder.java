/**
 * 
 */
package com.antheminc.oss.nimbus.domain.rules.drools;

import java.io.IOException;
import java.util.Map;

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
 * @author Swetha Vemuri
 *
 */
public class DecisionTableConfigBuilder extends AbstractRulesConfigBuilder implements RulesConfigBuilderStrategy {

	JustLogit logit = new JustLogit(getClass()); 
	/* (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.domain.rules.drools.RulesConfigBuilderStrategy#buildConfig(java.lang.String)
	 */
	@Override
	public RulesConfig buildConfig(String path, Map<String, RulesConfig> ruleConfigurations) {
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		RulesConfig ruleconfig = ruleConfigurations.get(path);
		
		if(ruleconfig != null) 
			return ruleconfig;
		
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
		
		KnowledgeBase kb = buildKnowledgeBase(kbBuilder, path);
		
		ruleconfig = new DroolsRulesConfig(path, kb);
		ruleConfigurations.put(path, ruleconfig);
		
		return ruleconfig;
	}

}
