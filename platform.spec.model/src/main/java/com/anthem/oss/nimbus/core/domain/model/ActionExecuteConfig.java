/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model;

import com.anthem.oss.nimbus.core.domain.Action;
import com.anthem.oss.nimbus.core.entity.Findable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor @ToString
public class ActionExecuteConfig<I, O> implements Findable<Action> {
	
/*	public static ModelConfig default_id(String alias, Class<?> referringClazz) {
		return oneParamModel(alias, referringClazz, default_string("id"));
	}
	
	public static ModelConfig default_ver(String alias, Class<?> referringClazz) {
		return oneParamModel(alias, referringClazz, default_string("v"));
	}

	public static ModelConfig oneParamModel(String alias, Class<?> referringClazz, ParamConfig pConfig) {
		ModelConfig m =  new ModelConfig(alias, referringClazz);
		m.templateParams().add(pConfig);
		return m;
	}

	
	public static ParamConfig default_string(String code) {
		ParamType type = new ParamType(false, "string");
		
		ParamConfig id = new ParamConfig(code);
		id.setType(type);
		return id;
	}*/
	
	
	
	@Getter @RequiredArgsConstructor @ToString
	public static class Input<T> {
		final private ModelConfig<T> model;
	}
	

	public enum Fetch {
		EAGER,
		LAZY;
	}
	
	
	
	@Getter @Setter @ToString(callSuper=true)
	public static class Output<T> extends Input<T> {
		
		private boolean paginated;
		
		private Fetch fetch;
		
		
		public Output(ModelConfig<T> model, boolean paginated, Fetch fetch) {
			super(model);
			this.paginated = paginated;
			this.fetch = fetch;
		}
	}
	
	
	final private Action action;
	
	private Input<I> input;
	
	private Output<O> output;
	
	
	@Override
	public boolean isFound(Action by) {
		return getAction() == by;
	}
	
	public boolean hasInput() {
		return getInput() != null;
	}

	public boolean hasOutput() {
		return getOutput() != null;
	}

	public ModelConfig<?> findModelConfig() {
		if (Action._new == getAction() || Action._replace == getAction() || Action._update == getAction())
			return getInput().getModel();

		return getOutput().getModel();
	}
	
}
