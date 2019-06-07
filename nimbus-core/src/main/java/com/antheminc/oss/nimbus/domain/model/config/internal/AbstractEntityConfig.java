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
package com.antheminc.oss.nimbus.domain.model.config.internal;

import java.util.concurrent.atomic.AtomicInteger;

import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
abstract public class AbstractEntityConfig<T> implements EntityConfig<T> {

	@JsonIgnore 
	final protected JustLogit logit = new JustLogit(getClass());

	private AnnotationConfig uiStyles;
	
	@JsonIgnore
	private AnnotationConfig gridFilter;

	@JsonIgnore 
	private RulesConfig rulesConfig; 
	
	private static final AtomicInteger counter = new AtomicInteger();
	
	private final String id;

	@JsonIgnore
	private EventHandlerConfig eventHandlerConfig;

	public AbstractEntityConfig() {
		this(generateNextId());
	}
	
	public AbstractEntityConfig(String id) {
		this.id = id;
	}
	
	protected static String generateNextId() {
		return String.valueOf(counter.incrementAndGet());
	}
}
