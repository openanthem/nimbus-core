/**
 * 
 */
package com.anthem.oss.nimbus.core.rules.drools;

import java.net.URL;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;
import com.anthem.oss.nimbus.core.domain.model.state.RulesRuntime;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactory;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 * @author Jayant Chaudhuri
 *
 */
@Component("rules.factory.drools")
public class DroolsRulesEngineFactory implements RulesEngineFactory {

	JustLogit logit = new JustLogit(getClass());
	
	@Override
	public RulesConfig createConfig(String alias) {
		String path = alias + ".drl";
		
		URL verifyUrl = getClass().getClassLoader().getResource(path);
		if(verifyUrl==null) {
			logit.info(()->"No rules file found at location: "+verifyUrl);
			return null;
			
		} else { 
			logit.info(()->"Rules file found at location: "+verifyUrl);
		}
		
		KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbBuilder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
		
		KnowledgeBase kb = kbBuilder.newKnowledgeBase();
		kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
		
		DroolsRulesConfig config = new DroolsRulesConfig(path, kb);
		return config;
	}

	@Override
	public RulesRuntime createRuntime(RulesConfig config) {
		DroolsRulesRuntime runtime = new DroolsRulesRuntime(config);
		return runtime;
	}

}
