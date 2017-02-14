/**
 * 
 */
package com.anthem.oss.nimbus.core.rules.drools;

import org.drools.KnowledgeBase;

import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class DroolsRulesConfig implements RulesConfig {

	final private String path; 

	final private KnowledgeBase knowledgeBase;
	
	@Override
	public <R> R unwrap(Class<R> clazz) {
		if(getKnowledgeBase()==null) return null;
		
		if(clazz.isInstance(getKnowledgeBase()))
			return clazz.cast(getKnowledgeBase());
		
		return null;
	}

}
