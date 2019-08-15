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

import org.apache.commons.lang3.StringUtils;
import org.drools.KnowledgeBase;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;

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
