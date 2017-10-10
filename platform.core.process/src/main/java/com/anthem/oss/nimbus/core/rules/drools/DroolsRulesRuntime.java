/**
 * 
 */
package com.anthem.oss.nimbus.core.rules.drools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatelessKnowledgeSession;

import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.RulesRuntime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor
public class DroolsRulesRuntime implements RulesRuntime {

	final private RulesConfig rulesConfig;

	private KnowledgeBase knowledgeBase;
	
	@Override
	public void start() {
		setKnowledgeBase(getRulesConfig().unwrap(KnowledgeBase.class));
	}

	@Override
	public void fireRules(Param<?>... params) {
		if(getKnowledgeBase()==null) return;
		StatelessKnowledgeSession session = getKnowledgeBase().newStatelessKnowledgeSession();
		List<Object> facts = new ArrayList<Object>();
		if(params.length > 0) {
			Arrays.asList(params).forEach((param) -> { 
				facts.add(param);
				facts.add(param.getState());
			});
		}
		session.execute(facts);
	}

	@Override
	public void shutdown() {}

	@Override
	public <S> S unwrap(Class<S> clazz) {
		if(getKnowledgeBase()==null) return null;
		
		if(clazz.isInstance(getKnowledgeBase()))
			return clazz.cast(getKnowledgeBase());
		
		return null;
	}

}
