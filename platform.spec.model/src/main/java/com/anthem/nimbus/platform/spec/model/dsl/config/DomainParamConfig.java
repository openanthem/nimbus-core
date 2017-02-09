/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.spec.model.dsl.Constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor @ToString(callSuper=true, of={"type", "validations", "values"})
public class DomainParamConfig<P> extends AbstractConfig<P> implements ParamConfig<P>, Serializable {

	private static final long serialVersionUID = 1L;

	final private String code;

	@Setter private ParamType type;	

	@Setter private Desc desc = new Desc();
	
	@Setter private List<AnnotationConfig> validations;
	
	@Setter private List<ParamValue> values;
	
	@Setter private List<AnnotationConfig> uiNatures;

	@Setter private AnnotationConfig uiStyles;

	@SuppressWarnings("unchecked")
	@Override
	public Class<P> getReferredClass() {
		return (Class<P>)getType().getReferredClass();
	}
	
	@Override
	public boolean isFound(String by) {
		return StringUtils.equals(getCode(), by);
	}
	
	@Override
	public boolean isLeaf() {
		return !getType().isNested();
	}
	
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findParamByPath(splits);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		if(ArrayUtils.isEmpty(pathArr))
			return null;

		/* param is not leaf node */
		ParamType.Nested<?> mp = getType().findIfNested();
		if(mp != null) {
			return mp.getModel().findParamByPath(pathArr);
		}
		
		/* if param is a leaf node and requested path has more children, then return null */
		if(pathArr.length > 1) return null;
		
		/* param is leaf node */
		ParamConfig<K> p = isFound(pathArr[0]) ? (ParamConfig<K>) this : null;
		return p;
	}
	
}
