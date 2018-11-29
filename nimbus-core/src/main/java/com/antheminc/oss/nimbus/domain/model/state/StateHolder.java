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
package com.antheminc.oss.nimbus.domain.model.state;

import java.util.List;
import java.util.Set;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.pojo.LockTemplate;

import lombok.Getter;

public class StateHolder {

	public static class EntityStateHolder<T, SE extends EntityState<T>> implements EntityState<T> {
		protected final SE ref;
		
		public EntityStateHolder(SE stateRef) {
			this.ref = stateRef;
		}
		
		@Override
		public String getPath() {
			return this.ref.getPath();
		}
		
		@Override
		public String getBeanPath() {
			return this.ref.getBeanPath();
		}
		
		@Override
		public EntityConfig<T> getConfig() {
			throw throwEx();	//return this.ref.getConfig();
		}
		
		@Override
		public String getConfigId() {
			return this.ref.getConfigId();
		}
		
		@Override
		public <S> S findStateByPath(String path) {
			return this.ref.findStateByPath(path);
		}
		
		@Override
		public <S> Model<S> findModelByPath(String path) {
			throw throwEx();	//return this.ref.findModelByPath(path);
		}
		
		@Override
		public <S> Model<S> findModelByPath(String[] pathArr) {
			throw throwEx();	//return this.ref.findModelByPath(pathArr);
		}
		
		@Override
		public <P> Param<P> findParamByPath(String path) {
			Param<P> p = this.ref.findParamByPath(path);
			return findParamByPath(p);
		}
		
		@Override
		public <P> Param<P> findParamByPath(String[] pathArr) {
			Param<P> p = this.ref.findParamByPath(pathArr);
			return findParamByPath(p);
		}
		
		@SuppressWarnings("unchecked")
		protected <P> Param<P> findParamByPath(Param<P> p) {
			if(p==null)
				return null;
			
			if((p==this.ref || p==this) 
					&& ParamStateHolder.class.isInstance(this))
				return ParamStateHolder.class.cast(this);
			
			return new ParamStateHolder.Writeable<>(p);
		}
		
		@Override
		public boolean isMapped() {
			return this.ref.isMapped();
		}
		
		@Override
		public boolean isRoot() {
			return this.ref.isRoot();
		}
		
		@Override
		public Model<?> getRootDomain() {
			throw throwEx();	//return this.ref.getRootDomain();
		}
		
		@Override
		public ExecutionModel<?> getRootExecution() {
			throw throwEx();	//return this.ref.getRootExecution();
		}

		@Override
		public void fireRules() {
			this.ref.fireRules();	
		}
		
		/* ********************** Operations Not Allowed *********************** */
		@Override
		public void initSetup() {
			throw throwEx();	
		}
		
		@Override
		public void initState(boolean doInternalStateInit) {
			throw throwEx();	
		}

		public boolean onLoad() {
			return !isStateInitialized();
		}
		
		@Override
		public boolean isStateInitialized() {
			return this.ref.isStateInitialized();
		}
		
		@Override
		public void setStateInitialized(boolean initialized) {
			throw throwEx();	
		}
		
		protected InvalidOperationAttemptedException throwEx() {
			return throwEx("Attempted operation not allowed in the config used on param: "+this.ref
					+", if this is needed - pls submit a feature request to bring it up for evaluation.");
		}
		
		protected InvalidOperationAttemptedException throwEx(String errMsg) {
			return new InvalidOperationAttemptedException(errMsg);
		}
	
		@Override
		public EntityStateAspectHandlers getAspectHandlers() {
			throw throwEx();
		}
		
		@Override
		public LockTemplate getLockTemplate() {
			throw throwEx();
		}

		@Override
		public Mapped<T, ?> findIfMapped() {
			throw throwEx();
		}
	}

	
	public static class ParamStateHolder<T> extends EntityStateHolder<T, Param<T>> implements Param<T> {
		
		@Getter
		private final T state;
		
		public ParamStateHolder(Param<T> param) {
			super(param);
			this.state = param.getLeafState();
		}
		
		public static class Writeable<S> extends ParamStateHolder<S> {
			public Writeable(Param<S> from) {
				super(from);
			}
			
			@Override
			public S getState() {
				return this.ref.getState();
			}
			
			@Override
			public Action setState(S state) {
				return this.ref.setState(state);
			}
		}
		
		public static class Mapped<S, M> extends ParamStateHolder<S> implements MappedParam<S, M> {
			
			private final ParamStateHolder<M> mapsTo;
			
			public Mapped(MappedParam<S, M> mapped, ParamStateHolder<M> mapsTo) {
				super(mapped);
				this.mapsTo = mapsTo;
			}
			
			@Override
			public Param<M> getMapsTo() {
				return mapsTo;
			}
			
			@Override
			public MappedParam<S, M> findIfMapped() {
				return this;
			}
			
			@Override
			public void handleNotification(Notification<M> event) {
				throw throwEx();
			}
			
			@Override
			public boolean requiresConversion() {
				throw throwEx();
			}
		}
		
		@Override
		public ParamConfig<T> getConfig() {
			throw throwEx();	//return ref.getConfig();
		}
		
		@Override
		public T getLeafState() {
			return this.ref.getLeafState();
		}
		
		@Override
		public Action setState(T state) {
			if(this.ref.isStateInitialized())
				throw throwEx("'setState' not allowed once parameter is initialized to avoid circular event trigger on param: "+this.ref
						+", if this is needed - pls submit a feature request to bring it up for evaluation.");
			
			return this.ref.setState(state);
		}

		
		@Override
		public Model<?> getParentModel() {
			throw throwEx();	//return this.ref.getParentModel();
		}
		
		
		@Override
		public StateType getType() {
			throw throwEx();	//return this.ref.getType();
		}
		
		@Override
		public ListParam findIfCollection() {
			throw throwEx();	//return this.ref.findIfCollection();
		}
		
		@Override
		public ListElemParam<T> findIfCollectionElem() {
			throw throwEx();	//return this.ref.findIfCollectionElem();
		}
		
		@Override
		public LeafParam<T> findIfLeaf() {
			throw throwEx();	//return this.ref.findIfLeaf();
		}
		
		@Override
		public Param<?> findIfLinked() {
			throw throwEx();	//return this.ref.findIfLinked();
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public MappedParam<T, ?> findIfMapped() {
			MappedParam<T, ?> mp = this.ref.findIfMapped();
			if(mp==null)
				return null;
			
			
			ParamStateHolder<?> mapsTo = new ParamStateHolder<>(mp.getMapsTo());
			return new ParamStateHolder.Mapped(mp, mapsTo);
		}
		
		@Override
		public Model<T> findIfNested() {
			throw throwEx();	//return this.ref.findIfNested();
		}
		
		@Override
		public MappedTransientParam<T, ?> findIfTransient() {
			return this.ref.findIfTransient();
		}
		
		@Override
		public boolean isCollection() {
			return this.ref.isCollection();
		}
		
		@Override
		public boolean isCollectionElem() {
			return this.ref.isCollectionElem();
		}
		
		@Override
		public boolean isLeaf() {
			return this.ref.isLeaf();
		}
		
		@Override
		public boolean isLeafOrCollectionWithLeafElems() {
			return this.ref.isLeafOrCollectionWithLeafElems();
		}
		
		@Override
		public boolean isLinked() {
			return this.ref.isLinked();
		}
		
		@Override
		public boolean isNested() {
			return this.ref.isNested();
		}
		
		@Override
		public boolean isTransient() {
			return this.ref.isTransient();
		}
		
		@Override
		public boolean hasContextStateChanged() {
			return this.ref.hasContextStateChanged();
		}
		
		public boolean isAssigned() {
			if(!isTransient()) 
				throw new InvalidConfigException("Attempted method on non-transient parameter: "+this.ref);
			
			return findIfTransient().isAssinged();
		}
		
		/* ********************** Parameter Context State Attributes *********************** */
		@Override
		public void activate() {
			this.ref.activate();
		}
		
		@Override
		public void deactivate() {
			this.ref.deactivate();
		}
		
		@Override
		public boolean isActive() {
			return this.ref.isActive();
		}
		
		@Override
		public Class<? extends ValidationGroup>[] getActiveValidationGroups() {
			return this.ref.getActiveValidationGroups();
		}
		@Override
		public void setActiveValidationGroups(Class<? extends ValidationGroup>[] activeValidationGroups) {
			this.ref.setActiveValidationGroups(activeValidationGroups);
		}
		
		@Override
		public Set<Message> getMessages() {
			return this.ref.getMessages();
		}
		
		@Override
		public void setMessages(Set<Message> msgs) {
			this.ref.setMessages(msgs);
		}
		
		
		
		@Override
		public List<ParamValue> getValues() {
			return this.ref.getValues();
		}
		@Override
		public void setValues(List<ParamValue> values) {
			this.ref.setValues(values);
		}
		
		@Override
		public boolean isEnabled() {
			return this.ref.isEnabled();
		}
		@Override
		public void setEnabled(boolean enabled) {
			this.ref.setEnabled(enabled);
		}
		
		@Override
		public boolean isVisible() {
			return this.ref.isVisible();
		}
		@Override
		public void setVisible(boolean visible) {
			this.ref.setVisible(visible);
		}
		
		@Override
		public Set<LabelState> getLabels() {
			return this.ref.getLabels();
		}
		
		@Override
		public void setLabels(Set<LabelState> labels) {
			this.ref.setLabels(labels);
		}
		
		/* ********************** Operations Not Allowed *********************** */
		@Override
		public boolean deregisterConsumer(MappedParam<?, T> consumer) {
			throw throwEx();
		}
		
		@Override
		public void emitNotification(Notification<T> event) {
			throw throwEx();	
		}
		

		@Override
		public List<MappedParam<?, T>> getEventSubscribers() {
			throw throwEx();
		}
		
		
		@Override
		public ValueAccessor getValueAccessor() {
			throw throwEx();
		}
		

		@Override
		public void onStateLoadEvent() {
			throw throwEx();	
		}
		
		@Override
		public void onStateChangeEvent(ExecutionTxnContext txnCtx, Action a) {
			throw throwEx();	
		}
		
		@Override
		public void registerConsumer(MappedParam<?, T> consumer) {
			throw throwEx();	
		}

		@Override
		public LabelState getDefaultLabel() {
			return this.ref.getDefaultLabel();
		}

		@Override
		public LabelState getLabel(String localeLanguageTag) {
			return this.ref.getLabel(localeLanguageTag);
		}

		@Override
		public StyleState getStyle() {
			return this.ref.getStyle();
		}

		@Override
		public void setStyle(StyleState styleState) {
			this.ref.setStyle(styleState);
		}

		@Override
		public boolean isEmpty() {
			return this.ref.isEmpty();
		}

	}
}
