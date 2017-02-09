/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DomainListElemParamState<E> extends DomainParamState<E> implements DomainState.ListElemParam<E> {
	
	private static final long serialVersionUID = 1L;	
	
	@FunctionalInterface
	public interface Creator<T> {
		public DomainParamState<T> apply(DomainListModelState<T> colModel, String elemId);
	}

	
	final private String elemId;
	
	public DomainListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, StateAndConfigSupportProvider provider, String elemId) {
		super(parentModel, config, provider);
		
		Objects.requireNonNull(elemId, "ElemId must not be null.");
		this.elemId = elemId;
	}	

	@SuppressWarnings("unchecked")
	@JsonIgnore @Override
	public ListModel<E> getParentModel() {
		return (ListModel<E>)super.getParentModel();
	}
	
	@Override
	protected String resolvePath() {
		String pathExpr = super.resolvePath();
		
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

}
