/**
 * 
 */
package com.antheminc.oss.nimbus.domain.rules.drools;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;

/**
 * @author Swetha Vemuri
 *
 */
public abstract class AbstractRulesConfigBuilder {
	
	Map<String,RulesConfig> ruleConfigurations = new ConcurrentHashMap<String,RulesConfig>();
	
	public KnowledgeBase buildKnowledgeBase(KnowledgeBuilder kbBuilder, String path) {
		
		KnowledgeBase kb = null;
		try {
			kb = kbBuilder.newKnowledgeBase();
			kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
			return kb;
		} catch (Exception e) {
			throw new FrameworkRuntimeException("Could not build knowledgeBase, either correct or delete the drl file :"+path+": ", e);
		}
	}
	
	public boolean evalResource(String path) {
		
		URL verifyUrl = getClass().getClassLoader().getResource(path);
		
		if (verifyUrl != null)
			return true;
		
		return false;
	}

}
