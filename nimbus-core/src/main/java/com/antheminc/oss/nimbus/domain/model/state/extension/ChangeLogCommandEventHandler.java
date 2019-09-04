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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.defn.extension.ChangeLog;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.CommandEventHandlers.OnRootCommandExecuteHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.CommandEventHandlers.OnSelfCommandExecuteHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter(value=AccessLevel.PROTECTED)
public class ChangeLogCommandEventHandler implements OnRootCommandExecuteHandler<ChangeLog>, OnSelfCommandExecuteHandler<ChangeLog> {

	private final ModelRepository modelRepository;
	private final SessionProvider sessionProvider;
	
	@Getter @Setter @ToString
	public static class ChangeLogEntry {

	    // when
		private Date on;

		// who
		private String by;
		private String sessionId;
		
	    // what: common 
		private String root;
		private Long refId;
		private Action action;
		
		
		// what: command
		private String url;	

		// what: model/parameter
		private String path;

		// what: parameter
		private Object previousValue;
		private Object value;
		
	}
	
	public ChangeLogCommandEventHandler(BeanResolverStrategy beanResolver, ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
		this.sessionProvider = beanResolver.get(SessionProvider.class);
	}
	
	@Override
	public void handleOnRootStart(ChangeLog configuredAnnotation, Command cmd) {
	}
	
	@Override
	public void handleOnSelfStart(ChangeLog configuredAnnotation, Command cmd) {
	}

	@Override
	public void handleOnRootStop(ChangeLog configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {
		ModelRepository rep = getModelRepository();
		
		// log command : ALL
		if(!cmd.isRootDomainOnly())
			Handler.forCommand(cmd, rep, getSessionProvider()).addUrl().save();		
		
		// log model
//		aggregatedEvents.keySet().stream()
//			.forEach(execModel->{
//				execModel.
//			});
		
		
	}

	/* TODO: [SOHAM] Interim solution: Would be refactored when event lifecycle for Command, Runtime, TxnCtx, State are implemented
	 * */
	public void handleOnRootStopEvents(Command cmd, MultiOutput mout) {
		Set<ParamEvent> aggregatedEvents = new HashSet<>();
		populateUniqueEvents(aggregatedEvents, mout);
		
		if(aggregatedEvents==null || aggregatedEvents.isEmpty())
			return;

		ModelRepository rep = getModelRepository();
		
		// log parameter
		aggregatedEvents.stream()
			.filter(pe->!pe.getParam().isMapped()) // core
			.filter(pe->pe.getParam().isLeafOrCollectionWithLeafElems()) // leaf
				.forEach(pe->Handler.forParam(rep, pe, getSessionProvider()).save());
			
	}
	
	private void populateUniqueEvents(Set<ParamEvent> aggregatedEvents, Output<?> out) {
		if(out==null)
			return;
		
		if(out.getAggregatedEvents()!=null && !out.getAggregatedEvents().isEmpty())
			aggregatedEvents.addAll(out.getAggregatedEvents());
		
		if(!MultiOutput.class.isInstance(out))
			return;
		
		MultiOutput mout = MultiOutput.class.cast(out);
		if(mout.getOutputs()==null || mout.getOutputs().isEmpty())
			return;
		
		mout.getOutputs().stream()
			.forEach(cout->populateUniqueEvents(aggregatedEvents, cout));
	}
	
	@Override
	public void handleOnSelfStop(ChangeLog configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {
		ModelRepository rep = getModelRepository();
		
		// log command : only root command CRUD actions
		//if(cmd.isRootDomainOnly() && cmd.getAction().isCrud())
			Handler.forCommand(cmd, rep, getSessionProvider()).addUrl().save();

	}
	
	@RequiredArgsConstructor
	private static class Handler {
		@Getter
		private final ChangeLogEntry entry = new ChangeLogEntry();
		
		private final Command cmd;
		private final ModelRepository rep;
		
		public static Handler forCommand(Command cmd, ModelRepository rep, SessionProvider sessionProvider) {
			Handler h = new Handler(cmd, rep);

			h.entry.setBy(Optional.ofNullable(sessionProvider.getLoggedInUser()).map(ClientUser::getLoginId).orElse(null));
			h.entry.setSessionId(sessionProvider.getSessionId());
			
			h.entry.setOn(Date.from(cmd.getCreatedInstant()));
			
			h.entry.setRoot(cmd.getRootDomainAlias());
			cmd.getRootDomainElement().doIfRefIdPresent(h.entry::setRefId);
			h.entry.setAction(cmd.getAction());
			
			return h;
		}

		public static Handler forParam(ModelRepository rep, ParamEvent pEvent, SessionProvider sessionProvider) {
			ExecutionModel<?> root = pEvent.getParam().getRootExecution();
			Command rootCmd = root.getRootCommand();

			Handler h = new Handler(rootCmd, rep);

			h.entry.setBy(Optional.ofNullable(sessionProvider.getLoggedInUser()).map(ClientUser::getLoginId).orElse(null));
			h.entry.setSessionId(sessionProvider.getSessionId());
			h.entry.setOn(Date.from(pEvent.getCreatedInstant()));
			
			h.entry.setRoot(rootCmd.getRootDomainAlias());
			rootCmd.getRootDomainElement().doIfRefIdPresent(h.entry::setRefId);
			
			h.entry.setAction(pEvent.getAction());
			h.entry.setPath(pEvent.getParam().getPath());
			h.entry.setPreviousValue(pEvent.getParam().getPreviousLeafState());
			h.entry.setValue(pEvent.getParam().getLeafState());
			return h;
		} 
		
		public Handler addUrl() {
			entry.setUrl(cmd.getAbsoluteUri());
			return this;
		}
	
		public void save() {
			rep._save("changelog", entry);
		}
		
	}
	
	

}
