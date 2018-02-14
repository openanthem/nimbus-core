/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.rules.drools;

import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.event.rule.RuleFlowGroupActivatedEvent;
import org.drools.event.rule.RuleFlowGroupDeactivatedEvent;
import org.drools.runtime.StatefulKnowledgeSession;

import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Rakesh Patel
 *
 */
public class CustomRulesListener implements AgendaEventListener {
	
	protected JustLogit logit = new JustLogit(this.getClass());
	
	private StatefulKnowledgeSession session;
	
	
	public CustomRulesListener(StatefulKnowledgeSession session) {
		this.session = session;
	}

	/**
	 * 
	 */
	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		logit.info(() -> "Rule Fired: " + event.getActivation().getRule().getName() + " in Rule Session: "
				+ this.session.getId());
		
	}

	@Override
	public void activationCreated(ActivationCreatedEvent event) {
		// TODO Auto-generated method stub
		
    }

	@Override
	public void activationCancelled(ActivationCancelledEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeActivationFired(BeforeActivationFiredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agendaGroupPushed(AgendaGroupPushedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub

	}

}
