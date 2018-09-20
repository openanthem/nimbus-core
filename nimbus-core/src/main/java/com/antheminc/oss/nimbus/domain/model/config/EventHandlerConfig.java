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
package com.antheminc.oss.nimbus.domain.model.config;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.model.config.event.ConfigEventHandlers.OnParamCreateHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadNewHandler;

/**
 * @author Soham Chakravarti
 *
 */
public interface EventHandlerConfig {

	public Set<Annotation> getOnParamCreateAnnotations();
	public Optional<OnParamCreateHandler<Annotation>> findOnParamCreateHandler(Annotation a);
	public OnParamCreateHandler<Annotation> getOnParamCreateHandler(Annotation a) throws InvalidConfigException;
	
	
	public Set<Annotation> getOnStateLoadAnnotations();
	public Optional<OnStateLoadHandler<Annotation>> findOnStateLoadHandler(Annotation a);
	public OnStateLoadHandler<Annotation> getOnStateLoadHandler(Annotation a) throws InvalidConfigException;
	
	public Set<Annotation> getOnStateLoadNewAnnotations();
	public Optional<OnStateLoadNewHandler<Annotation>> findOnStateLoadNewHandler(Annotation a);
	public OnStateLoadNewHandler<Annotation> getOnStateLoadNewHandler(Annotation a) throws InvalidConfigException;
	
	
	public Set<Annotation> getOnStateChangeAnnotations();
	public Optional<OnStateChangeHandler<Annotation>> findOnStateChangeHandler(Annotation a);
	public OnStateChangeHandler<Annotation> getOnStateChangeHandler(Annotation a) throws InvalidConfigException;
	
 }
