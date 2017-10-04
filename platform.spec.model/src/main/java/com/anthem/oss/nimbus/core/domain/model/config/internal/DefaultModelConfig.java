/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StateContextEntity;
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
@Getter @Setter @ToString(callSuper=true, of={"alias", "referredClass"}) @RequiredArgsConstructor
public class DefaultModelConfig<T> extends AbstractEntityConfig<T> implements ModelConfig<T>, Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final Class<T> referredClass;
	private String alias;
	
	@JsonIgnore private Domain domain;
	@JsonIgnore private Model model;
	
	@JsonIgnore private Repo repo;
	
	private List<ParamConfig<?>> params;
	
	@JsonIgnore private transient ParamConfig<?> idParam;
	
	@JsonIgnore	private transient ParamConfig<?> versionParam;
	
	public static class StateContextModelConfig<T> extends DefaultModelConfig<T> {
		private static final long serialVersionUID = 1L;
		
		public StateContextModelConfig(Class<T> referredClass) {
			super(referredClass);
		}
	}
	
	public static final <T> DefaultModelConfig<T> instantiate(Class<T> referredClass) {
		if(referredClass == StateContextEntity.class)
			return new StateContextModelConfig<>(referredClass);
		
		return new DefaultModelConfig<>(referredClass);
	}
	
	@JsonIgnore
	private final transient CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams = new CollectionsTemplate<>(
			() -> getParams(), (p) -> setParams(p), () -> new LinkedList<>());

	@Override @JsonIgnore
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams() {
		return templateParams;
	}

	@Override @JsonIgnore 
	public String getDomainLifecycle() {
		return Optional.ofNullable(getDomain()).map(Domain::lifecycle).orElse(null);
	}
	
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		// handle scenario if path = "/"
		String splits[] = StringUtils.equals(Constants.SEPARATOR_URI.code, StringUtils.trimToNull(path))
				? new String[] { path } : StringUtils.split(path, Constants.SEPARATOR_URI.code);

		return findParamByPath(splits);
	}
	
	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		if(ArrayUtils.isEmpty(pathArr)) return null;
		
		String nPath = pathArr[0];
		
		@SuppressWarnings("unchecked")
		ParamConfig<K> p = (ParamConfig<K>)templateParams().find(nPath);
		if(p == null) return null;
		
		if(pathArr.length == 1) { //last one
			return p;
		}
		
		return p.findParamByPath(ArrayUtils.remove(pathArr, 0));
	}
	
}
