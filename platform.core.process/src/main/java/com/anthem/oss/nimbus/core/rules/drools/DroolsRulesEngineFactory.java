/**
 * 
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
