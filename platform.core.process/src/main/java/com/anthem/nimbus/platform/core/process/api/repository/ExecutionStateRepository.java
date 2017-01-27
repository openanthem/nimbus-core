/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
@Component
public class ExecutionStateRepository<V, C> {

	@Autowired ModelRepositoryFactory repFactory;
	
	public Template template(Command cmd) {
		return new Template(cmd);
	}
	
	@Getter @RequiredArgsConstructor
	public class Template  {
		private final Command rootDomainCmd;
		private final ModelRepository rep;
		
		/* lazy get to always get the most recent state */
		private final Supplier<QuadModel<V, C>> quadGetter;
		private final Consumer<QuadModel<V, C>> quadSetter;
		
		JustLogit logit = new JustLogit(this.getClass());
		
		public Template(Command cmd) {
			this(cmd, repFactory.get(cmd));
		}
		
		public Template(Command cmd, ModelRepository rep) {
			this.rootDomainCmd = cmd.getRootDomainCommand();
			this.rep = rep;
			
			this.quadGetter = () -> PlatformSession.getAttribute(getRootDomainCmd());
			this.quadSetter = s -> PlatformSession.setAttribute(getRootDomainCmd(), s);
		}
		
		/**
		 * 
		 */
//		public void save() {
//			quadGetter.get().getCore().getState()
//			
//			ExecutionState<V, C> exec = stateGetter.get();
//			rep._new(ExecutionState.class, "execution_state", exec);
//		}
		
		public void save(List<ModelEvent<Param<?>>> events) {
			
		}
		
		public void load() {
			
		}
	}
	
}
