/**
 * 
 */
package com.antheminc.oss.nimbus.domain.rules.drools;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;

/**
 * Implementation of drl rule files strategy for Drools
 * @author Swetha Vemuri
 * @since 1.0
 *
 */
public class DrlConfigBuilder extends BaseDroolsConfigBuilder implements DroolsConfigBuilderStrategy {

	private static final String DRL_SUFFIX = ".drl" ;
	
	@Override
	public RulesConfig buildConfig(String alias) {
		
		String path = alias + DRL_SUFFIX;
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		//check if rule is present in cache
		RulesConfig ruleconfig = ruleConfigurations.get(path);
		
		if(ruleconfig != null) 
			return ruleconfig;
		
		kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
		
		KnowledgeBase kb = buildKnowledgeBase(kbBuilder, path);
		
		ruleconfig = new DroolsRulesConfig(path, kb);
		ruleConfigurations.put(path, ruleconfig);
		
		return ruleconfig;
	}

	@Override
	public boolean isSupported(String alias) {
		return evalResource(alias, DRL_SUFFIX);
	}

}
