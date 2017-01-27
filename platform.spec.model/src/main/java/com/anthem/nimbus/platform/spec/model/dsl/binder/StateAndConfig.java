/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.List;
import java.util.function.Supplier;

import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.util.CollectionsTemplate;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateAndConfig<T, C> extends State<T> {

	public C getConfig();

	public <S, M extends ModelConfig<S>> Model<S, M> findModelByPath(String path);
	
	public <S, M extends ModelConfig<S>> Model<S, M> findModelByPath(String[] pathArr);

	public <P> Param<P> findParamByPath(String path);
	
	public <P> Param<P> findParamByPath(String[] pathArr);
	
    public <S> State<S> findStateByPath(String path);
    
	public <S> State<S> findStateByPath(String[] pathArr);

	public void initConfigState();
	
	public StateAndConfigSupportProvider getProvider();
	
	public StateAndConfig<?, ?> getParent();
	
	public StateAndConfig<?, ?> getRootParent();
	
	
	
	
	public interface Model<T, C extends ModelConfig<T>> extends StateAndConfig<T, C> {
		
		public String getPath();
		
		public String getRootDomainUri();
		
		public List<Param<? extends Object>> getParams();
		
		public C getConfig();
		
		public <B> Model<B, ?> getBackingCoreModel();
		
		Supplier<? extends T> getCreator();
		
		public CollectionsTemplate<List<Param<?>>, StateAndConfig.Param<?>> templateParams();
		
		@Override
		public Param<?> getParent();

		@Override
		StateAndConfig.Model<?, ? extends ModelConfig<?>> getRootParent();
		
		public ExecutionStateTree getExecutionStateTree();
	}
	
	
	
	public interface Param<T> extends StateAndConfig<T, ParamConfig<T>> {
		
		public String getPath();
		
		public TypeStateAndConfig getType();
		
		public boolean isMapped();
		
		public Param<T> getMapsTo();
		
		@Override
		public Model<?, ?> getParent();
		
		@Override
		StateAndConfig.Model<?, ? extends ModelConfig<?>> getRootParent();
	}
	
}
