/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class RulesEngineRuntime implements Serializable{
	
	protected JustLogit logit = new JustLogit(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	private StatefulKnowledgeSession ruleSession;
	
	private FactHandle stateFactHandle;
	
	private FactHandle sacFactHandle;
	
	
	public RulesEngineRuntime(KnowledgeBase rulesContainer, Model<?> sac){
		ruleSession = rulesContainer.newStatefulKnowledgeSession();
		stateFactHandle = ruleSession.insert(sac.getState());
		sacFactHandle = ruleSession.insert(sac);
		//ruleSession.addEventListener(new CustomRulesListener(ruleSession));
	}
	
	
	/**
	 * 
	 * @param state
	 * @param config
	 */
	public void fireAllRules(Model<?> sac){
		ruleSession.update(stateFactHandle, sac.getState());
		ruleSession.update(sacFactHandle, sac);
		ruleSession.fireAllRules();
	}
	
	/**
	 * Dispose off the session to avoid memory leaks
	 */
	public void dispose(){
		ruleSession.dispose();
	}
	
}
