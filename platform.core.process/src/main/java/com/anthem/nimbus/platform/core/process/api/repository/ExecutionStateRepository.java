/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class ExecutionStateRepository<V, C> {

	ModelRepositoryFactory repFactory;
	
	public Template template(Command cmd) {
		return new Template(cmd);
	}
	
	public ExecutionStateRepository(ModelRepositoryFactory repFactory) {
		this.repFactory = repFactory;
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
			
			this.quadGetter = () -> UserEndpointSession.getAttribute(getRootDomainCmd());
			this.quadSetter = s -> UserEndpointSession.setAttribute(getRootDomainCmd(), s);
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
