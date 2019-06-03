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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.LinkedList;
import java.util.List;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO: Work In Progress
 * @author Soham Chakravarti
 *
 */
@Getter
public class CommandExecutionContext {

	private final Command command;
	private final List<ParamEvent> events = new LinkedList<>();
	
	protected CommandExecutionContext currentContext;
	
	@Setter
	private List<CommandExecutionContext> subContexts;
	
	private final CollectionsTemplate<List<CommandExecutionContext>, CommandExecutionContext> templateSubContexts = CollectionsTemplate.linked(this::getSubContexts, this::setSubContexts);
	
	public static class RootCommandExecutionContext extends CommandExecutionContext {
		public RootCommandExecutionContext(Command command) {
			super(command);
		}
	}
	
	public CommandExecutionContext(Command command) {
		this.command = command;
	}
	
	protected void setCurrentContext(CommandExecutionContext currentContext) {
		this.currentContext = currentContext;
	}
	
	public static CommandExecutionContext root(Command rootCmd) {
		return new RootCommandExecutionContext(rootCmd);
	}
	
	
	
	public CommandExecutionContext addNext(Command childCmd) {
		CommandExecutionContext childCtx = new CommandExecutionContext(childCmd);
		templateSubContexts.add(childCtx);
		
		return this;
	}
}
