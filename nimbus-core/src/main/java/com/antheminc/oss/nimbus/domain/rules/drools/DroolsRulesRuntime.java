/**
 *  Copyright 2016-2019 the original author or authors.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatelessKnowledgeSession;

import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.RulesRuntime;

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
