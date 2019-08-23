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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.RefId;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
public final class CommandExecution {

	@RequiredArgsConstructor @ToString
	private static class ActionBehavior {
		
		@Getter private final String inputCommandUri;
		
		@JsonIgnore	
		@Getter private final ExecutionContext context;

		@Getter private final Action action;
		private final List<Behavior> behaviors;

		protected List<Behavior> getBehaviors() {
			return Optional.ofNullable(behaviors).map(Collections::unmodifiableList).orElse(Collections.emptyList());
		}
	}
	
	@Getter @ToString(callSuper=true)
	public static class Input extends ActionBehavior {
		
		public Input(String inputCommandUri, ExecutionContext context, Action action, Behavior b) {
			super(inputCommandUri, context, action, Arrays.asList(b));
		}
		
		public Behavior getBehavior() {
			return getBehaviors().get(0);
		}
	}
	
	@Getter @Setter @ToString(callSuper=true)
	public static class Output<T> extends ActionBehavior {
		
		private T value;
		
		private Long rootDomainId;
		
		private ValidationResult validation;
		private ExecuteError error;
		
		@JsonIgnore
		@Getter @Setter 
		private Set<ParamEvent> aggregatedEvents = new HashSet<>();

		
		public Output(String inputCommandUri, ExecutionContext context, Action action, Behavior b) {
			this(inputCommandUri, context, action, Arrays.asList(b));
		}
		
		public Output(String inputCommandUri, ExecutionContext context, Action action, List<Behavior> behaviors) {
			this(inputCommandUri, context, action, behaviors, null);
		}
		
		public Output(String inputCommandUri, ExecutionContext context, Action action, List<Behavior> behaviors, T value) {
			super(inputCommandUri, context, action, behaviors);
			setValue(value);
			setRootDomainId(context.getCommandMessage().getCommand().getRootDomainElement().getRefId());
		}
	
		public static <T> Output<T> instantiate(Input input, ExecutionContext eCtx) {
			return new Output<>(input.getInputCommandUri(), eCtx, input.getAction(), input.getBehavior());
		}
		
		public static <T> Output<T> instantiate(Input input, ExecutionContext eCtx, T value) {
			Output<T> output = instantiate(input, input.getAction(), eCtx, value);
			return output;
		}
		
		public static <T> Output<T> instantiate(Input input, Action a, ExecutionContext eCtx, T value) {
			Output<T> output = new Output<>(input.getInputCommandUri(), eCtx, a, input.getBehavior());
			output.setValue(value);
			output.setRootDomainId(eCtx.getCommandMessage().getCommand().getRootDomainElement().getRefId());
			return output;
		}
		
		private void setRootDomainId(RefId<?> refId) {
			this.rootDomainId = RefId.nullSafeGetId(refId);
		}
		
		@Override
		public List<Behavior> getBehaviors() {
			return super.getBehaviors();
		}
	}
	
	public static class EventOutput<T> extends Output<T> {
		public EventOutput(Action action, Behavior b) {
			super(null, null, action, b);
		}
		public EventOutput(T value, Action action, Behavior b) {
			this(action, b);
			
			setValue(value);
		}
	}
	
	@ToString(callSuper=true)
	public static class MultiOutput extends Output<Object> {
		
		@Getter @Setter 
		private List<Output<?>> outputs;
		
		public MultiOutput(String inputCommandUri, ExecutionContext context, Action action, Behavior b) {
			super(inputCommandUri, context, action, b);
		}
		
		public MultiOutput(String inputCommandUri, ExecutionContext context, Action action, List<Behavior> behaviors) {
			super(inputCommandUri, context, action, behaviors);
		}
		
		@JsonIgnore
		private final CollectionsTemplate<List<Output<?>>, Output<?>> template = CollectionsTemplate.linked(this::getOutputs, this::setOutputs);
		
		public CollectionsTemplate<List<Output<?>>, Output<?>> template() {
			return template;
		}
		
		@JsonIgnore
		public Object getSingleResult() {
			if(CollectionUtils.isEmpty(getOutputs())) return null;
			
//			if(getOutputs().size() > 1) 
//				throw new IllegalStateException("Multi output contains more than one output elements: "+getOutputs());
			
			return getOutputs().get(0).getValue();
		}
		
		@SuppressWarnings("unchecked")
		@JsonIgnore
		public <T> List<Output<T>> findParamOutputsEndingWithPath(String path) {
			List<Output<T>> result = new ArrayList<>();
			for (Output<?> output : getOutputs()) {
				if (output.getValue() instanceof Param) {
					Param<?> param = (Param<?>) output.getValue();
					if (!StringUtils.isEmpty(param.getPath()) && param.getPath().endsWith(path)) {
						result.add((Output<T>) output);
					}
				}
			}
			return result;
		}
		
		@SuppressWarnings("unchecked")
		@JsonIgnore
		public <T> Output<T> findFirstParamOutputEndingWithPath(String path) {
			return Optional.ofNullable((Output<T>) findParamOutputsEndingWithPath(path).get(0)).orElse(null);
		}
		
	}

}
