/**
 * 
 */
package com.antheminc.oss.nimbus.core.rules.drools;

import org.apache.commons.lang.StringUtils;
import org.drools.KnowledgeBase;

import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.domain.model.config.RulesConfig;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class DroolsRulesConfig implements RulesConfig {

	final private String path; 

	final private KnowledgeBase knowledgeBase;
	
	public DroolsRulesConfig(String path, KnowledgeBase knowledgeBase) {
		if(StringUtils.isNotBlank(path) && knowledgeBase == null) {
			throw new InvalidConfigException("Found null knowledgeBase for drl file: "+path);
		}
		this.path = path;
		this.knowledgeBase = knowledgeBase;
	}
	
	@Override
	public <R> R unwrap(Class<R> clazz) {
		if(getKnowledgeBase()==null) return null;
		
		if(clazz.isInstance(getKnowledgeBase()))
			return clazz.cast(getKnowledgeBase());
		
		return null;
	}

}
