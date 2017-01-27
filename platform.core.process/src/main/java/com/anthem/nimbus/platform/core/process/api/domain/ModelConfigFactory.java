/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.Model;
import com.anthem.nimbus.platform.spec.model.dsl.config.AbstractParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.AnnotationConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.util.ModelsTemplate;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ViewDomain;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ViewParamBehavior;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ViewStyle;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewModelConfig;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewParamConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Component
@Getter @Setter
public class ModelConfigFactory {

	public static final boolean DEFAULT_USE_IMPLICIT_PARAM_MAPPING = false;
	
	private boolean useImplicitParamMapping = DEFAULT_USE_IMPLICIT_PARAM_MAPPING;
	
	public <T> CoreModelConfig<T> createModel(Class<T> referredClass, ModelConfigVistor visitedModels) {
		MapsTo.Model mapsTo = AnnotationUtils.findAnnotation(referredClass, MapsTo.Model.class);
		ViewConfig vc = AnnotationUtils.findAnnotation(referredClass, ViewConfig.class);
		
		if(vc!=null) {
			CoreModelConfig<T> coreConfig = visitedModels.mapsToModel(mapsTo);
			
			/* determine view type */
			return createViewModel(referredClass, visitedModels, coreConfig, mapsTo);
		} else {
			
			return createCoreModel(referredClass, visitedModels, mapsTo);
		}
	}
	
	protected <T> ViewModelConfig<T, ?> createViewModel(Class<T> referredClass, ModelConfigVistor visitedModels, CoreModelConfig<T> coreConfig, MapsTo.Model mapsTo) {
		ViewDomain flow = AnnotationUtils.findAnnotation(referredClass, ViewDomain.class);
		return (flow!=null) ? 
				new ViewModelConfig.Flow<>(coreConfig, referredClass, mapsTo) : 
					new ViewModelConfig<>(coreConfig, referredClass, mapsTo);
	}
	
	protected <T> CoreModelConfig<T> createCoreModel(Class<T> referredClass, ModelConfigVistor visitedModels, MapsTo.Model mapsTo) {
		return new CoreModelConfig<>(referredClass, mapsTo);
	}

	/**
	 * 1. If enclosing model is mapped, then attempt to map all params to the mapped model's params
	 * 2. Check if Field has been explicitly annotated with {@linkplain MapsTo.Path} 
	 * 3. Use path if found, else follow convention that the property name is at the same level as the mapped model's params
	 * 4. Throw exception if MapsTo.Model.silent is set to false if mapped param is not found  
	 */
	//TODO-DONE If Param is also marked with @MapsTo, then replace the ParamConfig with that of the MapsTo config. Should it be cloned??...probably No. 
	public <T, P> AbstractParamConfig<?> createParam(ModelConfig<T> mConfig, Field f, ModelConfigVistor visitedModels) {
		
		/* current model is not mapped, hence all child params cannot be mapped */
		if(!mConfig.isMapped()) {
			return createParam(mConfig, null, null, f);
		}
		
		/* current model is mapped, verify that it exists */
		CoreModelConfig<T> mappedToModel = visitedModels.mapsToModel(mConfig.getMapsTo());
		if(mappedToModel==null)
			throw new InvalidConfigException("Mapped Model not found for param: "+f.getName()+" when building View Config for View Model: "+mConfig.getReferredClass());
		
		MapsTo.Path mapsTo = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
		//mapsTo = (mapsTo==null || !mapsTo.linked()) ? null : mapsTo; //treat as null if not linked
		
		
		//find param from mapped model
		ParamConfig<?> mappedToParam = findMappedParam(mappedToModel, f.getName(), mapsTo);
		
		if(mapsTo==null && mappedToParam==null) { //00: Model mapped but param is not implicitly/explicitly mapped
			return createParam(mConfig, null, null, f);
			
		} else if((mapsTo==null || !mapsTo.linked()) && mappedToParam!=null) { //01: Param is implicitly mapped
			
			if(isUseImplicitParamMapping()) { //==IMPLICIT MAPPING:
				return createParam(mConfig, (CoreParamConfig<?>)mappedToParam, defaultPath, f);
			} else {
				return createParam(mConfig, null, null, f);
			}
			
		} else if((mapsTo!=null && mapsTo.linked()) && mappedToParam==null) { //10: Param mapped explicitly but none found
			throw new InvalidConfigException("Mapped Param :"+f.getName()+" not found in Mapped Model: "+mappedToModel
					+" when building View Config for View Model: "+mConfig.getReferredClass());
			
		} else { //11: Param mapped explicitly 
			return createParam(mConfig, (CoreParamConfig<?>)mappedToParam, mapsTo, f);

		}
		
	}
	
	//TODO-DONE 4. If the field is part of a model, which has UI Nature (@Page, @Section, etc.. anything that inherits @ViewComponent) then instantiate ParamConfigView wrapper class. 
	//TODO-DONE 5. Create ParamConfigView wrapper class over ParamConfig and add all additional attributes needed to support UI nature
	private <P> AbstractParamConfig<P> createParam(ModelConfig<?> mConfig, CoreParamConfig<P> cParam, MapsTo.Path mapsTo, Field f) {
		final CoreParamConfig<P> created;
		if(mConfig instanceof ViewModelConfig) {
			ViewParamConfig<P> vc = new ViewParamConfig<>(f.getName(), 
					(cParam!=null && cParam.isLeaf()) ? cParam : null, //if mapped param is leaf, then point to core otherwise, create new instance for view model param
					mapsTo,
					mConfig);
			
			List<AnnotationConfig> aConfigs = AnnotationConfigHandler.handle(f, ViewParamBehavior.class);
			vc.setUiNatures(aConfigs);

			vc.setUiStyles(AnnotationConfigHandler.handleSingle(f, ViewStyle.class));
			
			created = vc;
		} else if(cParam==null) {
			created = new CoreParamConfig<>(f.getName(), mapsTo, mConfig);
		} else {
			created = cParam;
		}
		
		if(AnnotatedElementUtils.isAnnotated(f, Model.Param.Values.class)) {
			Model.Param.Values aVal = AnnotationUtils.getAnnotation(f, Model.Param.Values.class);
			
			
			//Model.Param.Values.Source srcValues = ModelsTemplate.newInstance(aVal.value());
			//List<ParamValue> values = srcValues.getValues(created.getCode());
			created.setValuesUrl(aVal.url());
			//created.setValues(values);
		}
		
		return created;
	}
	
	//TODO Implement mapped param lookup given via explicit path reference
	private <T> ParamConfig<?> findMappedParam(ModelConfig<T> mappedToModel, String fieldNm, MapsTo.Path mapsTo) {
		if(mapsTo!=null && !StringUtils.isEmpty(mapsTo.value())) { //: if @Path has an explicit value
			String path = mapsTo.value();
			ParamConfig<?> mappedToParam = mappedToModel.findParamByPath(path);
			return mappedToParam;
			//throw new UnsupportedOperationException("TODO: Explicit Mapped Path lookup is not yet implemented. Found: "+ mapsTo.value());
		}
		
		ParamConfig<?> mappedToParam = mappedToModel.templateParams().find(fieldNm);
		return mappedToParam;
	}
	
	private MapsTo.Path defaultPath = determineDefaultPath();
	
	private MapsTo.Path determineDefaultPath() {
		Field f = FieldUtils.getField(MapsTo.class, "DEFAULT_PATH");
		MapsTo.Path mapsTo = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
		return mapsTo;
	}
	

}
