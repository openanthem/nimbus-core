/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor @ToString
public abstract class AbstractModelConfig<T> implements ModelConfig<T> {

	final private Class<T> referredClass;
	
	final private MapsTo.Model mapsTo;
	
	@JsonIgnore transient private Repo repo;
	

	@Override
	public boolean isMapped() {
		return getMapsTo() != null;
	}
	
	/**
	 * 
	 */
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		// handle scenario if path = "/"
		String splits[] = StringUtils.equals(Constants.SEPARATOR_URI.code, StringUtils.trimToNull(path))
				? new String[] { path } : StringUtils.split(path, Constants.SEPARATOR_URI.code);

		return findParamByPath(splits);
	}
	
	/**
	 * 
	 */
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
