/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
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
public abstract class AbstractParamConfig<P> implements ParamConfig<P> {

	private static final long serialVersionUID = 1L;
	

	final private String code;

	final private MapsTo.Path mapsTo;
	
	@JsonIgnore final transient private Supplier<ModelConfig<?>> parentModel;
	
	private String valuesUrl;
	
	
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
	public boolean isMapped() {
		return getMapsTo() != null && getMapsTo().linked();
	}
	
	
	/**
	 * 
	 */
	@Override
	public boolean isImplicitlyMapped() {
		if(isMapped()) return true;
		
		/* still considered mapped, if parent model is mapped and current param's nested model is mapped with same class */
		ModelConfig<?> mConfig = getParentModel().get();
		
		if(mConfig.isMapped() && getType().isNested()) {	//check if parent model is mapped AND current param is nested
			
			ModelConfig<?> mpNmConfig = getType().findIfNested().getModel();
			
			if(mpNmConfig.isMapped()) {	//check if param.nestedModel is mapped AND is same parent's mapsTo model
				
				Class<?> mappedParamNmMapsToClass = mpNmConfig.getMapsTo().value();	//param.nestedModel mapsTo
				Class<?> mMapsToClass = mConfig.getMapsTo().value();	//parent's mapsTo
				
				return mappedParamNmMapsToClass == mMapsToClass;
			}
		}
			
		return false;
	}
	
	@Override
	public boolean isLeaf() {
		return !getType().isNested();
	}
	
	/**
	 * 
	 */
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findParamByPath(splits);
	}
	
	
	/**
	 * 
	 */
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
	
	
	abstract public void setDesc(ParamConfig.Desc desc);
	
	abstract public void setValidations(List<AnnotationConfig> validations);
	
	abstract public void setType(ParamType type);
	
	abstract public void setAnnotations(List<Annotation> annotations);
	
}
