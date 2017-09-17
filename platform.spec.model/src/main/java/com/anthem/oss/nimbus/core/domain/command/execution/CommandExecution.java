/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
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
		
		private String rootDomainId;
		
		private ValidationResult validation;
		private ExecuteError error;
		
		public Output(String inputCommandUri, ExecutionContext context, Action action, Behavior b) {
			this(inputCommandUri, context, action, Arrays.asList(b));
		}
		
		protected Output(String inputCommandUri, ExecutionContext context, Action action, List<Behavior> behaviors) {
			super(inputCommandUri, context, action, behaviors);
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
		
	}

}
