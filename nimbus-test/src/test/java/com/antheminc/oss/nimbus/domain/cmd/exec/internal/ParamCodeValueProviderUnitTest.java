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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.CommandElementLinked;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.entity.StaticCodeValue;

/**
 * @author Tony Lopez
 *
 */
public class ParamCodeValueProviderUnitTest {

	private ParamCodeValueProvider testee;
	private DefaultActionExecutorSearch searchExecutor;
	
	@Before
	public void init() {
		this.searchExecutor = Mockito.mock(DefaultActionExecutorSearch.class);
		this.testee = new ParamCodeValueProvider(this.searchExecutor);
	}
	
	@Test
	public void testExecute_staticCodeValue_dbLookup_noResults() {
		String commandUri = "";
		CommandMessage commandMessage = Mockito.mock(CommandMessage.class);
		ExecutionContext eCtx = new ExecutionContext(commandMessage);
		Input input = new Input(commandUri, eCtx, Action._get, Behavior.$execute);
		
		Command command = Mockito.mock(Command.class);
		CommandElementLinked domainCmdElem = Mockito.mock(CommandElementLinked.class);
		CommandElementLinked rootDomainElem = Mockito.mock(CommandElementLinked.class);
	
		Mockito.when(commandMessage.getCommand()).thenReturn(command);
		Mockito.when(command.getElementSafely(Type.DomainAlias)).thenReturn(domainCmdElem);
		Mockito.when(command.getRootDomainElement()).thenReturn(rootDomainElem);
		Mockito.when(rootDomainElem.getRefId()).thenReturn(42L);
		
		List<StaticCodeValue> searchResults = new ArrayList<>();
		Output<List<StaticCodeValue>> searchResultsOutput = Output.instantiate(input, eCtx);
		searchResultsOutput.setValue(searchResults);
	
		Mockito.when(domainCmdElem.getAlias()).thenReturn(ParamCodeValueProvider.STATIC_CODE_VALUE);
		Mockito.when(this.searchExecutor.execute(input)).thenReturn(searchResultsOutput);
		
		Assert.assertNull(this.testee.execute(input).getValue());
	}
	
	@Test
	public void testExecute_staticCodeValue_dbLookup_oneResult() {
		String commandUri = "";
		CommandMessage commandMessage = Mockito.mock(CommandMessage.class);
		ExecutionContext eCtx = new ExecutionContext(commandMessage);
		Input input = new Input(commandUri, eCtx, Action._get, Behavior.$execute);
		
		Command command = Mockito.mock(Command.class);
		CommandElementLinked domainCmdElem = Mockito.mock(CommandElementLinked.class);
		CommandElementLinked rootDomainElem = Mockito.mock(CommandElementLinked.class);
	
		Mockito.when(commandMessage.getCommand()).thenReturn(command);
		Mockito.when(command.getElementSafely(Type.DomainAlias)).thenReturn(domainCmdElem);
		Mockito.when(command.getRootDomainElement()).thenReturn(rootDomainElem);
		Mockito.when(rootDomainElem.getRefId()).thenReturn(42L);
		
		List<ParamValue> expected = new ArrayList<ParamValue>();
		List<StaticCodeValue> searchResults = new ArrayList<>();
		searchResults.add(new StaticCodeValue("A", expected));
		Output<List<StaticCodeValue>> searchResultsOutput = Output.instantiate(input, eCtx);
		searchResultsOutput.setValue(searchResults);
	
		Mockito.when(domainCmdElem.getAlias()).thenReturn(ParamCodeValueProvider.STATIC_CODE_VALUE);
		Mockito.when(this.searchExecutor.execute(input)).thenReturn(searchResultsOutput);
		
		Assert.assertEquals(expected, this.testee.execute(input).getValue());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testExecute_staticCodeValue_dbLookup_moreThanOneResult() {
		String commandUri = "";
		CommandMessage commandMessage = Mockito.mock(CommandMessage.class);
		ExecutionContext eCtx = new ExecutionContext(commandMessage);
		Input input = new Input(commandUri, eCtx, Action._get, Behavior.$execute);
		
		Command command = Mockito.mock(Command.class);
		CommandElementLinked domainCmdElem = Mockito.mock(CommandElementLinked.class);
		CommandElementLinked rootDomainElem = Mockito.mock(CommandElementLinked.class);
	
		Mockito.when(commandMessage.getCommand()).thenReturn(command);
		Mockito.when(command.getElementSafely(Type.DomainAlias)).thenReturn(domainCmdElem);
		Mockito.when(command.getRootDomainElement()).thenReturn(rootDomainElem);
		Mockito.when(rootDomainElem.getRefId()).thenReturn(42L);
		
		List<ParamValue> expected = new ArrayList<ParamValue>();
		List<StaticCodeValue> searchResults = new ArrayList<>();
		searchResults.add(new StaticCodeValue("A", expected));
		searchResults.add(new StaticCodeValue("A", expected));
		Output<List<StaticCodeValue>> searchResultsOutput = Output.instantiate(input, eCtx);
		searchResultsOutput.setValue(searchResults);
	
		Mockito.when(domainCmdElem.getAlias()).thenReturn(ParamCodeValueProvider.STATIC_CODE_VALUE);
		Mockito.when(this.searchExecutor.execute(input)).thenReturn(searchResultsOutput);
		
		this.testee.execute(input).getValue();
	}
	
	@Test
	public void testExecute_staticCodeValue_configServerLookup() {
		String commandUri = "";
		final String codeKey = "A";
		String rawPayload = codeKey;
		CommandMessage commandMessage = Mockito.mock(CommandMessage.class);
		ExecutionContext eCtx = new ExecutionContext(commandMessage);
		Input input = new Input(commandUri, eCtx, Action._get, Behavior.$execute);
		
		Command command = Mockito.mock(Command.class);
		CommandElementLinked domainCmdElem = Mockito.mock(CommandElementLinked.class);
		CommandElementLinked rootDomainElem = Mockito.mock(CommandElementLinked.class);
	
		Mockito.when(commandMessage.getCommand()).thenReturn(command);
		Mockito.when(command.getElementSafely(Type.DomainAlias)).thenReturn(domainCmdElem);
		Mockito.when(command.getRootDomainElement()).thenReturn(rootDomainElem);
		Mockito.when(rootDomainElem.getRefId()).thenReturn(42L);
		
		List<ParamValue> expected = new ArrayList<ParamValue>();
		expected.add(new ParamValue());
		
		Map<String, List<ParamValue>> values = new HashMap<>();
		values.put(codeKey, expected);
		this.testee.setValues(values);
	
		Mockito.when(domainCmdElem.getAlias()).thenReturn(ParamCodeValueProvider.STATIC_CODE_VALUE);
		Mockito.when(commandMessage.getRawPayload()).thenReturn(rawPayload);
		
		Assert.assertEquals(expected, this.testee.execute(input).getValue());
	}
	
	@Test
	public void testExecute_genericCode_configServerLookup() {
		final String lookupCode = "someRandomLookupCode";
		final String codeKey = "A";
		final String rootDomainAlias = codeKey;
		
		String commandUri = "";
		CommandMessage commandMessage = Mockito.mock(CommandMessage.class);
		ExecutionContext eCtx = new ExecutionContext(commandMessage);
		Input input = new Input(commandUri, eCtx, Action._get, Behavior.$execute);
		
		Command command = Mockito.mock(Command.class);
		CommandElementLinked domainCmdElem = Mockito.mock(CommandElementLinked.class);
		CommandElementLinked rootDomainElem = Mockito.mock(CommandElementLinked.class);
	
		Mockito.when(commandMessage.getCommand()).thenReturn(command);
		Mockito.when(command.getElementSafely(Type.DomainAlias)).thenReturn(domainCmdElem);
		Mockito.when(command.getRootDomainElement()).thenReturn(rootDomainElem);
		Mockito.when(rootDomainElem.getRefId()).thenReturn(42L);
		
		List<ParamValue> expected = new ArrayList<ParamValue>();
		expected.add(new ParamValue());
		
		Map<String, List<ParamValue>> values = new HashMap<>();
		values.put(codeKey, expected);
		this.testee.setValues(values);
	
		Mockito.when(domainCmdElem.getAlias()).thenReturn(lookupCode);
		Mockito.when(command.getRootDomainAlias()).thenReturn(rootDomainAlias);
		
		Assert.assertEquals(expected, this.testee.execute(input).getValue());
	}
}
