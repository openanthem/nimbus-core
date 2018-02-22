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

import java.beans.PropertyDescriptor;
import java.util.List;

import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.pojo.LockTemplate;

import lombok.Getter;

public class ParamStateHolder<T> implements Param<T> {

	private final Param<T> param;
	
	@Getter
	private final T state;
	
	public ParamStateHolder(Param<T> param) {
		this.param = param;
		this.state = param.getLeafState();
	}
	
	@Override
	public <S> S findStateByPath(String path) {
		return this.param.findStateByPath(path);
	}
	
	@Override
	public <S> Model<S> findModelByPath(String path) {
		return this.param.findModelByPath(path);
	}
	
	@Override
	public <S> Model<S> findModelByPath(String[] pathArr) {
		return this.param.findModelByPath(pathArr);
	}
	
	@Override
	public <P> Param<P> findParamByPath(String path) {
		return this.param.findParamByPath(path);
	}
	
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
		return this.param.findParamByPath(pathArr);
	}
	
	@Override
	public String getPath() {
		return this.param.getPath();
	}
	
	@Override
	public String getBeanPath() {
		return this.param.getBeanPath();
	}
	
	@Override
	public ParamConfig<T> getConfig() {
		return this.param.getConfig();
	}
	
	@Override
	public String getConfigId() {
		return this.param.getConfigId();
	}
	
	@Override
	public T getLeafState() {
		return this.param.getLeafState();
	}
	
	@Override
	public Model<?> getParentModel() {
		return this.param.getParentModel();
	}
	
	@Override
	public Model<?> getRootDomain() {
		return this.param.getRootDomain();
	}
	
	@Override
	public ExecutionModel<?> getRootExecution() {
		return this.param.getRootExecution();
	}
	
	@Override
	public StateType getType() {
		return this.param.getType();
	}
	
	@Override
	public boolean isStateInitialized() {
		return this.param.isStateInitialized();
	}
	
	@Override
	public ListParam findIfCollection() {
		return this.param.findIfCollection();
	}
	
	@Override
	public ListElemParam<T> findIfCollectionElem() {
		return this.param.findIfCollectionElem();
	}
	
	@Override
	public LeafParam<T> findIfLeaf() {
		return this.param.findIfLeaf();
	}
	
	@Override
	public Param<?> findIfLinked() {
		return this.param.findIfLinked();
	}
	
	@Override
	public MappedParam<T, ?> findIfMapped() {
		return this.param.findIfMapped();
	}
	
	@Override
	public Model<T> findIfNested() {
		return this.param.findIfNested();
	}
	
	@Override
	public MappedTransientParam<T, ?> findIfTransient() {
		return this.param.findIfTransient();
	}
	
	@Override
	public boolean isCollection() {
		return this.param.isCollection();
	}
	
	@Override
	public boolean isCollectionElem() {
		return this.param.isCollectionElem();
	}
	
	@Override
	public boolean isLeaf() {
		return this.param.isLeaf();
	}
	
	@Override
	public boolean isLeafOrCollectionWithLeafElems() {
		return this.param.isLeafOrCollectionWithLeafElems();
	}
	
	@Override
	public boolean isLinked() {
		return this.param.isLinked();
	}
	
	@Override
	public boolean isMapped() {
		return this.param.isMapped();
	}
	
	@Override
	public boolean isNested() {
		return this.param.isNested();
	}
	
	@Override
	public boolean isRoot() {
		return this.param.isRoot();
	}
	
	@Override
	public boolean isTransient() {
		return this.param.isTransient();
	}
	
	/* ********************** Parameter Context State Attributes *********************** */
	@Override
	public void activate() {
		this.param.activate();
	}
	
	@Override
	public void deactivate() {
		this.param.deactivate();
	}
	
	@Override
	public boolean isActive() {
		return this.param.isActive();
	}
	
	@Override
	public Class<? extends ValidationGroup>[] getActiveValidationGroups() {
		return this.param.getActiveValidationGroups();
	}
	@Override
	public void setActiveValidationGroups(Class<? extends ValidationGroup>[] activeValidationGroups) {
		this.param.setActiveValidationGroups(activeValidationGroups);
	}
	
	@Override
	public Message getMessage() {
		return this.param.getMessage();
	}
	
	@Override
	public void setMessage(Message msg) {
		this.param.setMessage(msg);
	}
	
	@Override
	public List<ParamValue> getValues() {
		return this.param.getValues();
	}
	@Override
	public void setValues(List<ParamValue> values) {
		this.param.setValues(values);
	}
	
	@Override
	public boolean isEnabled() {
		return this.param.isEnabled();
	}
	@Override
	public void setEnabled(boolean enabled) {
		this.setEnabled(enabled);
	}
	
	@Override
	public boolean isVisible() {
		return this.param.isVisible();
	}
	@Override
	public void setVisible(boolean visible) {
		this.param.setVisible(visible);
	}
	
	/* ********************** Operations Not Allowed *********************** */
	private InvalidOperationAttemptedException throwEx() {
		return new InvalidOperationAttemptedException("Attempted operation not allowed in the config used on param: "+this.param);
	}
	
	@Override
	public boolean deregisterConsumer(MappedParam<?, T> consumer) {
		throw throwEx();
	}
	
	@Override
	public void emitNotification(Notification<T> event) {
		throw throwEx();	
	}
	
	@Override
	public void fireRules() {
		throw throwEx();	
	}
	
	@Override
	public EntityStateAspectHandlers getAspectHandlers() {
		throw throwEx();
	}
	
	@Override
	public List<MappedParam<?, T>> getEventSubscribers() {
		throw throwEx();
	}
	
	@Override
	public LockTemplate getLockTemplate() {
		throw throwEx();
	}
	
	@Override
	public PropertyDescriptor getPropertyDescriptor() {
		throw throwEx();
	}
	
	@Override
	public void initSetup() {
		throw throwEx();	
	}
	
	@Override
	public void initState() {
		throw throwEx();	
	}
	
	@Override
	public void setStateInitialized(boolean initialized) {
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
	public Action setState(T state) {
		throw throwEx();	
	}
	
}
