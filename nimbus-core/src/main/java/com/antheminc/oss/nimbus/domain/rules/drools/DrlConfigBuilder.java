/**
 * 
 */
package com.antheminc.oss.nimbus.domain.rules.drools;

import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;

/**
 * @author Swetha Vemuri
 *
 */
public class DrlConfigBuilder extends AbstractRulesConfigBuilder implements RulesConfigBuilderStrategy {

	
	public DrlConfigBuilder() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.domain.rules.drools.RulesConfigBuilderStrategy#buildConfig(java.lang.String)
	 */
	@Override
	public RulesConfig buildConfig(String path, Map<String,RulesConfig> ruleConfigurations) {
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		RulesConfig ruleconfig = ruleConfigurations.get(path);
		
		if(ruleconfig != null) 
			return ruleconfig;
		
		kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
		
		KnowledgeBase kb = buildKnowledgeBase(kbBuilder, path);
		
		ruleconfig = new DroolsRulesConfig(path, kb);
		ruleConfigurations.put(path, ruleconfig);
		
		return ruleconfig;
	}

}
