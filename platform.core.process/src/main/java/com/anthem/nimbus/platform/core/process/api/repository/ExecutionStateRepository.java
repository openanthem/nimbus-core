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
package com.anthem.nimbus.platform.core.process.api.repository;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class ExecutionStateRepository<V, C> {

//	ModelRepositoryFactory repFactory;
//	
//	public Template template(Command cmd) {
//		return new Template(cmd);
//	}
//	
//	public ExecutionStateRepository(ModelRepositoryFactory repFactory) {
//		this.repFactory = repFactory;
//	}
//
//	@Getter @RequiredArgsConstructor
//	public class Template  {
//		private final Command rootDomainCmd;
//		private final ModelRepository rep;
//		
//		/* lazy get to always get the most recent state */
//		private final Supplier<QuadModel<V, C>> quadGetter;
//		private final Consumer<QuadModel<V, C>> quadSetter;
//		
//		JustLogit logit = new JustLogit(this.getClass());
//		
//		public Template(Command cmd) {
//			this(cmd, repFactory.get(cmd));
//		}
//		
//		public Template(Command cmd, ModelRepository rep) {
//			this.rootDomainCmd = cmd.getRootDomainCommand();
//			this.rep = rep;
//			
//			this.quadGetter = () -> UserEndpointSession.getAttribute(getRootDomainCmd());
//			this.quadSetter = s -> UserEndpointSession.setAttribute(getRootDomainCmd(), s);
//		}
//		
//		/**
//		 * 
//		 */
////		public void save() {
////			quadGetter.get().getCore().getState()
////			
////			ExecutionState<V, C> exec = stateGetter.get();
////			rep._new(ExecutionState.class, "execution_state", exec);
////		}
//		
//		public void save(List<ModelEvent<Param<?>>> events) {
//			
//		}
//		
//		public void load() {
//			
//		}
//	}
	
}
