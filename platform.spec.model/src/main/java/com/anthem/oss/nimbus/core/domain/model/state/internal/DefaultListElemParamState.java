/**
 * 
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
	
	public static class StateContextListElemParamState<E> extends DefaultListElemParamState<E> {
		private static final long serialVersionUID = 1L;
		
		public StateContextListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers aspectHandlers, String elemId) {
			super(parentModel, config, aspectHandlers, elemId);
		}
		
		@JsonIgnore @Override
		public ParamConfig<E> getConfig() {
			return super.getConfig();
		}
	}
	
	public DefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers aspectHandlers, String elemId) {
		super(parentModel, config, aspectHandlers);
		
		Objects.requireNonNull(elemId, "ElemId must not be null.");
		this.elemId = elemId;
	}	

	public static <E> DefaultListElemParamState<E> instantiate(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers aspectHandlers, String elemId) {
		if(isStateContextConfig(parentModel, config))
			return new StateContextListElemParamState<>(parentModel, config, aspectHandlers, elemId);
		
		return new DefaultListElemParamState<>(parentModel, config, aspectHandlers, elemId);
	}
	
	@SuppressWarnings("unchecked")
	@JsonIgnore @Override
	public ListModel<E> getParentModel() {
		return (ListModel<E>)super.getParentModel();
	}
	
	@Override
	protected String resolvePath() {
		return replaceIndexConstantWithElemId(super.resolvePath());
	}
	
	@Override
	protected String resolveBeanPath() {
		return replaceIndexConstantWithElemId(super.resolveBeanPath());
	}
	
	private String replaceIndexConstantWithElemId(String pathExpr) {
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
