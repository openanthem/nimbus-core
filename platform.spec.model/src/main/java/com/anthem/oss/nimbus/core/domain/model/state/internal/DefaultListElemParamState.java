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
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultListElemParamState<E> extends DefaultParamState<E> implements EntityState.ListElemParam<E> {
	
	private static final long serialVersionUID = 1L;	
	
	@FunctionalInterface
	public interface Creator<T> {
		public DefaultParamState<T> apply(DefaultListModelState<T> colModel, String elemId);
	}
	
	final private String elemId;

	
	public static class LeafElemState<E> extends DefaultListElemParamState<E> implements LeafParam<E> {
		private static final long serialVersionUID = 1L;
		
		public LeafElemState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers aspectHandlers, String elemId) {
			super(parentModel, config, aspectHandlers, elemId);
		}
	}

	
	public DefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers aspectHandlers, String elemId) {
		super(parentModel, config, aspectHandlers);
		
		Objects.requireNonNull(elemId, "ElemId must not be null.");
		this.elemId = elemId;
	}	

	@SuppressWarnings("unchecked")
	@JsonIgnore @Override
	public ListModel<E> getParentModel() {
		return (ListModel<E>)super.getParentModel();
	}
	
//	@Override
//	protected String[] resolvePath() {
//		return replaceIndexConstantWithElemId(super.resolvePath());
//	}
//	
//	@Override
//	protected String[] resolveBeanPath() {
//		return replaceIndexConstantWithElemId(super.resolveBeanPath());
//	}

	@Override
	public String getPath() {
		String p = super.getPath();
		return replaceIndexConstantWithElemId(p);
	}
	
	@Override
	public String getBeanPath() {
		String p = super.getBeanPath();
		return replaceIndexConstantWithElemId(p);
	}
	
	private String replaceIndexConstantWithElemId(String pathExpr) {
//		int index = ArrayUtils.indexOf(pathExpr, Constants.MARKER_COLLECTION_ELEM_INDEX.code);
//		if(index==-1)
//			throw new InvalidStateException("Expected marker "+Constants.MARKER_COLLECTION_ELEM_INDEX.code+" not found in listElem: ");
//		
//		String[] copy = Arrays.copyOf(pathExpr, pathExpr.length);
//		copy[index] = getElemId();
//		return copy;
		String rPath = StringUtils.replace(pathExpr, Constants.MARKER_COLLECTION_ELEM_INDEX.code, getElemId());
		return rPath;
	}
	
	@Override
	public boolean isFound(String by) {
		return StringUtils.equals(getElemId(), by);
	}

	@Override
	public int getElemIndex() {
		return getParentModel().fromElemId(getElemId());
	}

	@Override
	public boolean remove() {
		return getParentModel().remove(this);
	}
}
