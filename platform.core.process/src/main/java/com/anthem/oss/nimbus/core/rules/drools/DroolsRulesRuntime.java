/**
 * 
 */
package com.anthem.oss.nimbus.core.rules.drools;

import java.util.Optional;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

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

	private StatefulKnowledgeSession session;
	
	@Override
	public void start() {
		setSession(getRulesConfig().unwrap(KnowledgeBase.class).newStatefulKnowledgeSession());
		
		getSession().addEventListener(new CustomRulesListener(getSession()));
	}

	@Override
	public void fireRules(Param<?> param,Object... objects) {
		if(getSession()==null) return;
		
		createOrUpdateFactHandle(param);
		
		for (Object object : objects) {
			createOrUpdateFactHandle(object);
		}
		
		Object state = param.getState();
		createOrUpdateFactHandle(state);
		
		getSession().fireAllRules();
	}

	protected FactHandle createOrUpdateFactHandle(Object o) {
		FactHandle factHandle = getSession().getFactHandle(o);
		if(factHandle==null) {
			factHandle = getSession().insert(o);	
		} else {
			getSession().update(factHandle, o);
		}
		return factHandle;
	}
	
	@Override
	public void shutdown() {
		Optional.ofNullable(getSession())
			.ifPresent(s->s.dispose());
	}

	@Override
	public <S> S unwrap(Class<S> clazz) {
		if(getSession()==null) return null;
		
		if(clazz.isInstance(getSession()))
			return clazz.cast(getSession());
		
		return null;
	}

}
