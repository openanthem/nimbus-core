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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.extension.ChangeLog;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.CommandEventHandlers.OnRootCommandExecuteHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.CommandEventHandlers.OnSelfCommandExecuteHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
public class ChangeLogCommandEventHandler implements OnRootCommandExecuteHandler<ChangeLog>, OnSelfCommandExecuteHandler<ChangeLog> {

	private ModelRepositoryFactory repositoryFactory;
	
	@SuppressWarnings("serial")
	@Getter @Setter @ToString
	public static class ChangeLogEntry implements Persistable<String> {

	    // when
	    @CreatedDate
		private ZonedDateTime on;

		// who
	    @CreatedBy
		private String by;
		
	    // what: common
		private String root;
		private Long refId;
		private Action action;
		
		
		// what: command
		private String url;	

		// what: model/parameter
		private String path;

		// what: parameter
		private Object value;
		
		@Override
		public String getId() {
			return null;
		}
		
		@Override
		public boolean isNew() {
			return true;
		}
	}
	
	public ChangeLogCommandEventHandler(BeanResolverStrategy beanResolver) {
		this.repositoryFactory = beanResolver.get(ModelRepositoryFactory.class);
	}
	
	@Override
	public void handleOnRootStart(ChangeLog configuredAnnotation, Command cmd) {
		
	}
	
	@Override
	public void handleOnSelfStart(ChangeLog configuredAnnotation, Command cmd) {
		
	}

	@Override
	public void handleOnRootStop(ChangeLog configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {
		ModelRepository rep = getResolvedRepo();
		
		// log command : ALL
		Handler.forCommand(cmd, rep).addUrl().save();

		if(aggregatedEvents==null || aggregatedEvents.isEmpty())
			return;
		
		// log model
//		aggregatedEvents.keySet().stream()
//			.forEach(execModel->{
//				execModel.
//			});
		
		// log parameter
		aggregatedEvents.entrySet().stream()
			.forEach(eventEntry->{
				eventEntry.getValue().stream()
					.filter(pe->!pe.getParam().isMapped()) // core
					.filter(pe->pe.getParam().isLeafOrCollectionWithLeafElems()) // leaf
						.forEach(pe->Handler.forParam(cmd, rep, pe).save());
			});
	}
	
	@Override
	public void handleOnSelfStop(ChangeLog configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {
		ModelRepository rep = getResolvedRepo();
		
		// log command : only root command CRUD actions
		if(cmd.isRootDomainOnly() && cmd.getAction().isCrud())
			Handler.forCommand(cmd, rep).addUrl().save();
		
	}
	
	private ModelRepository getResolvedRepo() {
		return repositoryFactory.get(Repo.Database.rep_mongodb);
	}
	
	@RequiredArgsConstructor
	private static class Handler {
		@Getter
		private final ChangeLogEntry entry = new ChangeLogEntry();
		
		private final Command cmd;
		private final ModelRepository rep;
		
		public static Handler forCommand(Command cmd, ModelRepository rep) {
			Handler h = new Handler(cmd, rep);

			h.addCommon();
			h.entry.setAction(cmd.getAction());
			return h;
		}
		
		public Handler addUrl() {
			entry.setUrl(cmd.getAbsoluteUri());
			return this;
		}
	
		public void save() {
			rep._save("changelog", entry);
		}
		
		public void addCommon() {
			entry.setRoot(cmd.getRootDomainAlias());
			entry.setRefId(cmd.root().getRefId());
		}

		public static Handler forParam(Command cmd, ModelRepository rep, ParamEvent pEvent) {
			Handler h = new Handler(cmd, rep);

			h.addCommon();
			h.entry.setAction(pEvent.getAction());
			h.entry.setPath(pEvent.getParam().getPath());
			h.entry.setValue(pEvent.getParam().getLeafState());
			return h;
		} 
	}
	
	

}
