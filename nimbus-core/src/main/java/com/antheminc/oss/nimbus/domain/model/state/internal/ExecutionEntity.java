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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.internal.MappedDefaultParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.antheminc.oss.nimbus.entity.AbstractEntity;
import com.antheminc.oss.nimbus.entity.process.ProcessFlow;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */

@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true, of={"c", "v"})
public class ExecutionEntity<V, C> extends AbstractEntity.IdLong implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private final JustLogit logit = new JustLogit(getClass());
	
	private C c;

	@Path("/c") private V v;
	
	private ProcessFlow f;
	
	private Map<String, Object> paramRuntimes = new HashMap<>();
	
	@JsonIgnore private transient boolean isNew;
	
	public ExecutionEntity() { }
	
	public ExecutionEntity(V view, C core) {
		setView(view);
		setCore(core);
	}
	
	public static ExecutionEntity<?, ?> resolveAndInstantiate(Object view, Object core) {
		if(view==null && core==null)
			throw new InvalidStateException("Both view and core cannot be null.");
		
		if(view==null && core!=null)
			return new ExecutionEntity<>(null, core);
		
		if(view!=null && core==null)	//swap
			return new ExecutionEntity<>(null, view);
		
		return new ExecutionEntity<>(view, core);	
	}
	
	public C getCore() {return getC();}
	public void setCore(C c) {setC(c);}
	
	public V getView() {return getV();}
	public void setView(V v) {setV(v);}
	
	public ProcessFlow getFlow() {return getF();}
	public void setFlow(ProcessFlow f) {setF(f);}
	
	private ExecutionEntity<V, C> _this() {
		return this;
	}
	
	@Getter @Setter @ToString @RequiredArgsConstructor
	public static class ExConfig<V, C> {
		final private ModelConfig<C> core;
		final private ModelConfig<V> view;
		final private ModelConfig<ProcessFlow> flow;
	}
	
	@Getter @Setter @ToString(callSuper=true)
	public class ExParamConfig extends DefaultParamConfig<ExecutionEntity<V, C>> {
		private static final long serialVersionUID = 1L;
		
		final private ModelConfig<ExecutionEntity<V, C>> rootParent;
		
		public ExParamConfig(ExConfig<V, C> exConfig) {
			super("");
			this.rootParent = new ExModelConfig(exConfig); 
			
			ParamConfigType.Nested<ExecutionEntity<V, C>> pType = new ParamConfigType.Nested<>(_this().getClass().getSimpleName(), _this().getClass());
			pType.setModelConfig(getRootParent());
			this.setType(pType);
		}
	}
	
	@Getter @Setter @ToString(callSuper=true)
	public class ExModelConfig extends DefaultModelConfig<ExecutionEntity<V, C>> {
		private static final long serialVersionUID = 1L;
		
		final private ParamConfig<C> coreParam;
		final private ParamConfig<V> viewParam;
		final private ParamConfig<ProcessFlow> flowParam;
		
		@SuppressWarnings("unchecked")
		public ExModelConfig(ExConfig<V, C> exConfig) {
			super((Class<ExecutionEntity<V, C>>)_this().getClass());
			
			//TODO: change to client default 
			setRepo(AnnotationUtils.findAnnotation(ExecutionEntity.class, Repo.class));
			
			Objects.requireNonNull(exConfig.getCore(), "Core ModelConfig must be provided.");
			this.coreParam = attachParams("c", exConfig.getCore());
			this.viewParam = exConfig.getView()!=null ? attachParams("v", exConfig.getView()) : null;
			this.flowParam = exConfig.getFlow()!=null ? attachParams("f", exConfig.getFlow()) : null;
		}
		
		@Override
		public boolean isRoot() {
			return true;
		}
		
		private <T> ParamConfig<T> attachParams(String pCode, ModelConfig<T> modelConfig) {
			DefaultParamConfig<T> pConfig = createParam(modelConfig, pCode);
			Class<T> pClass = modelConfig.getReferredClass();
			
			ParamConfigType.Nested<T> pType = new ParamConfigType.Nested<>(ClassUtils.getShortName(pClass), pClass);
			pConfig.setType(pType);
			pType.setModelConfig(modelConfig);
			
			templateParamConfigs().add(pConfig);
			return pConfig;
		}
		
		private <T> DefaultParamConfig<T> createParam(ModelConfig<T> modelConfig, String pCode) {
			Field f = FieldUtils.getDeclaredField(ExecutionEntity.class, pCode, true);
			MapsTo.Path path = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
			
			
			String domainRootAlias = /*"/" + pCode + "/" +*/ AbstractEntityState.getDomainRootAlias(modelConfig);
			DefaultParamConfig<T> pConfig = path==null ? DefaultParamConfig.instantiate(modelConfig, domainRootAlias, pCode) : new MappedDefaultParamConfig<>(domainRootAlias, pCode, this, findParamByPath(path.value()), path);
			return pConfig;
		} 
	}

	@ToString(callSuper=true)
	public class ExParamLinked extends ExParam {
		private static final long serialVersionUID = 1L;
		
		private final Param<?> linkedParam;
		
		public ExParamLinked(Command rootCommand, EntityStateAspectHandlers provider, ExConfig<V, C> exConfig, Param<?> linkedParam) {
			super(rootCommand, provider, exConfig);
			this.linkedParam = linkedParam;
		}
		
		@Override
		public String getPath() {
			String p = super.getPath();
			
			p = linkedParam.getPath() + p;
			return p;
		}
		
		@Override
		public String getBeanPath() {
			String p = super.getBeanPath();
			
			p = linkedParam.getBeanPath() + p;
			return p;
		}
		
		@Override
		public boolean isLinked() {
			return true;
		}
		
		@Override
		public Param<?> findIfLinked() {
			return linkedParam;
		}
	}
	
	@Getter @Setter @ToString(callSuper=true, of="rootRefId")
	public class ExParam extends DefaultParamState<ExecutionEntity<V, C>> {
		private static final long serialVersionUID = 1L;
		
		private final ExecutionModel<ExecutionEntity<V, C>> rootModel;
		
		private final Serializable rootRefId;
		
		public ExParam(Command rootCommand, EntityStateAspectHandlers provider, ExConfig<V, C> exConfig) {
			super(null, new ExParamConfig(exConfig), provider);
			
			ExParamConfig pConfig = ((ExParamConfig)getConfig());
			
			// TODO use in getPath to append refId to root domain alias
			rootRefId = rootCommand.getRootDomainElement().getRefId();
			
			this.rootModel = new ExModel(rootCommand, this, pConfig.getRootParent(), provider);
			this.setType(new StateType.Nested<>(getConfig().getType().findIfNested(), getRootExecution()));
		}
		
		@Override
		public boolean isRoot() {
			return true;
		}
		
		@Override
		public ExecutionModel<ExecutionEntity<V, C>> getRootExecution() {
			return rootModel;
		}
		
		@Override
		public void fireRules() {
			Optional.ofNullable(findIfNested())
				.map(Model::getParams)
				.ifPresent(
						params->params.stream()
							.forEach(Param::fireRules)
				);
		}
	}

	
	@Getter @Setter @ToString(callSuper=true, of="rootCommand")
	public class ExModel extends DefaultModelState<ExecutionEntity<V, C>> implements ExecutionModel<ExecutionEntity<V, C>> {
		private static final long serialVersionUID = 1L;
		
		final private Command rootCommand;
		
		@JsonIgnore
		final private DefaultExecutionRuntime executionRuntime;
		
		public ExModel(Command rootCommand, ExParam associatedParam, ModelConfig<ExecutionEntity<V, C>> modelConfig, EntityStateAspectHandlers provider) {
			this(rootCommand, associatedParam, modelConfig, provider, 
					new DefaultExecutionRuntime(rootCommand, new DefaultStateEventDelegator(provider)));
			
			this.executionRuntime.setRootExecution(this);
		}
		
		private ExModel(Command rootCommand, ExParam associatedParam, ModelConfig<ExecutionEntity<V, C>> modelConfig, EntityStateAspectHandlers provider, DefaultExecutionRuntime executionRuntime) {
			super(associatedParam, modelConfig, provider);
			this.rootCommand = rootCommand;
			this.executionRuntime = executionRuntime;
		}
		
		@Override
		public boolean isNew() {
			return _this().isNew;
		}
		
		@Override
		protected void initSetupInternal() {
			getExecutionRuntime().start();
			
			//==initState();
		}
		
		@Override
		public ExecutionEntity<V, C> instantiateOrGet() {
			return _this();
		}
		
		@Override
		public ExModelConfig getConfig() {
			return (ExModelConfig)super.getConfig();
		}
		
		@JsonIgnore
		@Override
		public ExParam getAssociatedParam() {
			return (ExParam)super.getAssociatedParam();
		}

		@Override
		public Command getRootCommand() {
			if(getAssociatedParam().isLinked()) {
				return getAssociatedParam().findIfLinked().getRootExecution().getRootCommand();
			}
			
			return rootCommand;
		}
		
		@Override
		public void fireRules() {
			getAssociatedParam().fireRules();
		}
		
		@JsonIgnore
		@Override
		public ExecutionEntity<V, C> getState() {
			return _this();
		}
		
		@JsonIgnore
		@Override
		public Map<String, Object> getParamRuntimes() {
			return _this().getParamRuntimes();
		}
		
		@JsonIgnore
		@Override
		public boolean isRoot() {
			return true;
		}
		
		@Override
		public ExecutionModel<ExecutionEntity<V, C>> findIfRoot() {
			return this;
		}
		
		@Override
		public <U> U unwrap(Class<U> c) {
			if(c.isInstance(this))
				return c.cast(this);
			
			return null;
		}
		
		@Override
		protected void finalize() throws Throwable {
			getExecutionRuntime().stop();
			
			super.finalize();
		}
	}
}
