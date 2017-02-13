/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Constraint;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.ClassUtils;

import com.anthem.oss.nimbus.core.domain.config.builder.AnnotationConfigHandler;
import com.anthem.oss.nimbus.core.domain.definition.ConfigLoadException;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.ViewParamBehavior;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.ViewStyle;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultParamConfigAttached;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultParamConfigDetached;
import com.anthem.oss.nimbus.core.util.ClassLoadUtils;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
abstract public class AbstractModelConfigBuilder {

	protected JustLogit logit = new JustLogit(getClass());
	
	abstract public <T> ModelConfig<T> buildModel(Class<T> clazz, ModelConfigVistor visitedModels);
	
	abstract public <T> ParamConfig<?> buildParam(ModelConfig<T> mConfig, Field f, ModelConfigVistor visitedModels);
	
	public boolean isPrimitive(Class<?> determinedType) {
		return ClassUtils.isPrimitiveOrWrapper(determinedType) || String.class==determinedType;
	}
	
	public <T> DefaultModelConfig<T> createModel(Class<T> referredClass, ModelConfigVistor visitedModels) {
		MapsTo.Type mapsToType = AnnotationUtils.findAnnotation(referredClass, MapsTo.Type.class);
		
		final DefaultModelConfig<T> created;
		if(mapsToType!=null) {
			
			ModelConfig<?> mapsTo = visitedModels.get(mapsToType.value());
			if(mapsTo==null)
				throw new InvalidConfigException(MapsTo.Type.class.getSimpleName()+" not found: "+mapsToType);
			
			created = new MappedDefaultModelConfig<>(mapsTo, referredClass);
			
		} else {
			created = new DefaultModelConfig<>(referredClass);
		}
		
		// handle repo
		Repo rep = AnnotationUtils.findAnnotation(referredClass, Repo.class);
		created.setRepo(rep);
		
		return created;
	}
	
	
	
	public <T> DefaultModelConfig<List<T>> createCollectionModel(Class<List<T>> referredClass, ParamConfig<?> associatedParamConfig) {
		//mapsTo is null when the model itself is a java List implementation (ArrayList, etc)
		
		DefaultModelConfig<List<T>> coreConfig = new DefaultModelConfig<>(referredClass);
		
		if(associatedParamConfig.isMapped()) {
			return new MappedDefaultModelConfig<>(coreConfig, referredClass);
		} else {
			return coreConfig;
		}
	}
	
	public DefaultParamConfig<?> createParam(ModelConfig<?> mConfig, Field f, ModelConfigVistor visitedModels) {
		MapsTo.Path mapsToPath = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
		
		// no path specified
		if(mapsToPath==null)
			return decorateParam(f, new DefaultParamConfig<>(f.getName())); 
			
		// check if field is mapped with linked=true: which would require parent model to also be mapped
		if(mapsToPath.linked())
			return createMappedParamLinked(mConfig, f, visitedModels, mapsToPath);
		else 
			return createMappedParamDelinked(mConfig, f, visitedModels, mapsToPath);
	}
	
	private DefaultParamConfig<?> createMappedParamLinked(ModelConfig<?> mConfig, Field f, ModelConfigVistor visitedModels, MapsTo.Path mapsToPath) {
		// param field is linked: enclosing model must be mapped
		if(!mConfig.isMapped())
			throw new InvalidConfigException("Mapped param field: "+f.getName()+" is mapped with linked=true. Enclosing model: "+mConfig.getReferredClass()+" must be mapped, but was not.");

		// param field is linked: enclosing mapped model's config must have been loaded
		Class<?> mapsToModelClass = mConfig.findIfMapped().getMapsTo().getReferredClass();
		ModelConfig<?> mapsToModel = visitedModels.get(mapsToModelClass);
		if(mapsToModel==null)
			throw new ConfigLoadException("Mapped param field: "+f.getName()+" is mapped with linked=true. Enclosing model: "+mConfig.getReferredClass()+" mapsToModelClass: "+mapsToModelClass
					+" which must have been loaded prior, but wasn't.");
		
		// find mapsTo param from mapsTo model
		ParamConfig<?> mapsToParam = findMappedParam(mapsToModel, f.getName(), mapsToPath);
		if(mapsToParam==null)
			throw new InvalidConfigException("No mapsTo param found for mapped param field: "+f.getName()+" in enclosing model:"+mConfig.getReferredClass()+" with mapsToPath: "+mapsToPath);
		
		return decorateParam(f, new MappedDefaultParamConfigAttached<>(f.getName(), mapsToParam, mapsToPath));
	}
	
	private DefaultParamConfig<?> createMappedParamDelinked(ModelConfig<?> mConfig, Field f, ModelConfigVistor visitedModels, MapsTo.Path mapsToPath) {
		Class<?> mappedParamClass = f.getType();
		
		ModelConfig<?> mapsToModel;

		// for delinked, mapped param must be of type Model with MapsTo.Type specified
		MapsTo.Type mappedParamMapsToType = AnnotationUtils.findAnnotation(mappedParamClass, MapsTo.Type.class);
		if(mappedParamMapsToType==null) {
			logit.warn(()->"Mapped param field: "+f.getName()+" is mapped with linked=false in enclosing model: "+mConfig.getReferredClass()+". "
					+ "Mapped param field of type"+mappedParamClass+" is NOT configured with: "+MapsTo.Type.class+". Taking intended config as self mapping.");
		
			mapsToModel = visitedModels.get(mappedParamClass);
		} else {
			mapsToModel = visitedModels.get(mappedParamMapsToType.value());
		}
		
		
		if(mapsToModel==null && !isCollection(mappedParamClass))
			throw new ConfigLoadException("Mapped param field: "+f.getName()+" is mapped with linked=false in enclosing model: "+mConfig.getReferredClass()+". "
					+ "Mapped param field of type: "+mappedParamClass+" is configured with mapsTo.Type: "+mappedParamMapsToType
					+" which must have been loaded prior, but wasn't.");
		
		// detached collection
		if(mapsToModel==null) {
			DefaultModelConfig<?> coreConfig = new DefaultModelConfig<>(mappedParamClass);
			
			mapsToModel = new MappedDefaultModelConfig<>(coreConfig, mappedParamClass);
		}
		
		return decorateParam(f, new MappedDefaultParamConfigDetached<>(f.getName(), mapsToModel, mapsToPath));
	}
	
		
	
	public <T, P> DefaultParamConfig<P> createParamCollectionElement(ModelConfig<T> mConfig, Field pColField, ParamConfig<P> pConfig, ModelConfig<List<P>> colModelConfig, ModelConfigVistor visitedModels, Class<P> colElemClass) {
		logit.trace(()->"[create.pColElem] starting to process colElemClass: "+colElemClass+" with pColField :"+pColField);
		
		MapsTo.Path mapsToPathColParam = AnnotationUtils.findAnnotation(pColField, MapsTo.Path.class);
		MapsTo.Mode mapsToModeColParam = MapsTo.getMode(mapsToPathColParam);
		String colParamCode = pConfig.getCode();//f.getName();
		
		logit.debug(()->"[create.pColElem] mapsToColParam: "+mapsToPathColParam);
		logit.debug(()->"[create.pColElem] mtModeColParam: "+mapsToModeColParam);
		logit.debug(()->"[create.pColElem] colParamCode: "+colParamCode);
		
		
		if(mapsToModeColParam==MapsTo.Mode.UnMapped) { //unmapped
			DefaultParamConfig<P> pCoreElemConfig = createParamCollectionElementInternal(colModelConfig, null, null, colParamCode);
		
			logit.trace(()->"[create.pColElem] [colParam is UnMapped] returning core pColElem Config as colElem is UnMapped.");
			return pCoreElemConfig;
		}
		
		// colParam is mapped: attached or detached
		logit.trace(()->"[create.pColElem] [colParam is mapped: Attached/Detached] begin processing for mapped Attached/Detached colElem: "+colElemClass);
		
		// colParam is mapped as Attached, but parent enclosing Model is un-mapped :- throw Ex
		if(mapsToModeColParam==MapsTo.Mode.MappedAttached && !mConfig.isMapped()) { 	// mapped: attached
			throw new InvalidConfigException("Param: "+pConfig.getCode()+" has @MapsTo.Path "+mapsToPathColParam+" with resolved mode: "+mapsToModeColParam
						+" Attached Mapped Param must have Model that is mapped, but found with no @MapsTo.Model mappings for: "+mConfig.getReferredClass());
		}
		
		MapsTo.Type mapsToElemModel = AnnotationUtils.findAnnotation(colElemClass, MapsTo.Type.class);
		logit.debug(()->"[create.pColElem] colElemModel MapsTo.Model: "+mapsToElemModel);

		ModelConfig<?> mapsTo = visitedModels.get(mConfig.findIfMapped().getMapsTo().getReferredClass());
		logit.debug(()->"[create.pColElem] [colParam is mapped] [elemClass same] [Attached] Found parent mapsToModel: "+mapsTo+" from visitedMmodels using: "+mConfig.findIfMapped().getMapsTo().getReferredClass());
		
		ParamConfig<?> mapsToColParamConfig = findMappedParam(mapsTo, pConfig.getCode(), mapsToPathColParam);
		logit.debug(()->"[create.pColElem] [colParam is mapped] [elemClass same] [Attached] Found mapsToColParamConfig for "+pConfig.getCode()+" with mapsToPath of colParam: "+mapsToPathColParam+" -> "+mapsToColParamConfig);
		
		@SuppressWarnings("unchecked")
		DefaultParamConfig<P> mapsToColElemParamConfig = (DefaultParamConfig<P>)mapsToColParamConfig.getType().findIfCollection().getElementConfig();

		
		// colParam is mapped: colElemModel is NOT explicitly mapped BUT colElemClass is NOT SAME as mappedElemClass :- throw Ex
		if(mapsToElemModel==null && colElemClass!=mapsToColElemParamConfig.getReferredClass()) {
			
			throw new InvalidConfigException("Mapped Elem Class is not same as MapsTo Elem Class. Must be same or an explicit MapsTo.Model mapping is required. "
					+ " For Model: "+mConfig.getReferredClass()+" param: "+colParamCode
					+ " Expected elemClass: "+colElemClass+" but found mapsToElemClass: "+mapsToPathColParam.getClass());
		}
		
		//colParam is mapped: colElemModel is NOT explicitly mapped BUT colElemClass is SAME as mappedElemClass	:- map parameter as-is: Scenario 1, 2
		//if(mapsToElemModel==null && colElemClass==mapsToPathColParam.getClass()) {
			//logit.trace(()->"[create.pColElem] [colParam is mapped] [elemClass same] map parameter as-is: Scenario 1, 2, 3");

			// handle Attached:
			if(mapsToModeColParam==MapsTo.Mode.MappedAttached) {
				logit.trace(()->"[create.pColElem] [colParam is mapped] [elemClass same] [Attached] begin processing: Scenario 1,2 ");
				
				logit.trace(()->"[create.pColElem] [colParam is mapped] [elemClass same] [Attached] returing  ");
				return createParamCollectionElementInternal(colModelConfig, mapsToColElemParamConfig, mapsToPathColParam, colParamCode);
				
			} else {	// Scenario: 3
				logit.trace(()->"[create.pColElem] [colParam is mapped] [elemClass same] [Attached] begin processing: Scenario: 3 ");
				throw new UnsupportedOperationException("MapsTo.Path Mode: MappedDetached NOT yet implemented for SAME elemClass: "+colElemClass+" for colParam: "+colParamCode);
			}
		//}
		
		//colParam is mapped: colElemModel IS explicitly mapped :- Scenario 4
		//logit.trace(()->"[create.pColElem] [colParam is mapped] [elemClass mapped] Scenario 4, 5, 6, 7 colElemModel MapsTo.Model: "+mapsToElemModel);
		
		
		//throw new UnsupportedOperationException("Mapped collection param not yet implemented...ParamConfig");
	}
	
	private <P> DefaultParamConfig<P> createParamCollectionElementInternal(ModelConfig<List<P>> mConfig, DefaultParamConfig<P> mapsToElemParamConfig, MapsTo.Path mapsToColParam, String colParamCode) {
		final String collectionElemPath = createCollectionElementPath(colParamCode);
		
		final MapsTo.Path mapsToColElemParam = mapsToColParam==null ? null : createNewImplicitMapping(collectionElemPath, mapsToColParam.linked());
		
		final DefaultParamConfig<P> created;
		if(mConfig instanceof MappedDefaultModelConfig) {
			created = new MappedDefaultParamConfigAttached<>(collectionElemPath, mapsToElemParamConfig, mapsToColElemParam);

		} else if(mapsToElemParamConfig==null) {
			created = new DefaultParamConfig<>(collectionElemPath);
			
		} else {
			created = mapsToElemParamConfig;
			
		}
		return created;
	}
	
	public static String createCollectionElementPath(String collectionPath) {
		return new StringBuilder()
				//.append(collectionPath)
				//.append(Constants.SEPARATOR_URI.code)
				.append(Constants.MARKER_COLLECTION_ELEM_INDEX.code)
				.toString();
	}
	
	private DefaultParamConfig<?> decorateParam(Field f, DefaultParamConfig<?> created) {
		List<AnnotationConfig> aConfigs = AnnotationConfigHandler.handle(f, ViewParamBehavior.class);
		created.setUiNatures(aConfigs);

		created.setUiStyles(AnnotationConfigHandler.handleSingle(f, ViewStyle.class));

		
		if(AnnotatedElementUtils.isAnnotated(f, Model.Param.Values.class)) {
			Model.Param.Values aVal = AnnotationUtils.getAnnotation(f, Model.Param.Values.class);
			
			Model.Param.Values.Source srcValues = ClassLoadUtils.newInstance(aVal.value());
			List<ParamValue> values = srcValues.getValues(created.getCode());
			created.setValues(values);
		}
		
		String value ="";
		if(AnnotatedElementUtils.isAnnotated(f, Model.Param.Text.class)) {
			Model.Param.Text aVal = AnnotationUtils.getAnnotation(f, Model.Param.Text.class);
			
			//Model.Param.String srcValues = ModelsTemplate.newInstance(aVal.value());
			value = aVal.label();
		}
		//not mapped Param
		ParamConfig.Desc desc = new ParamConfig.Desc();
		desc.setHelp(created.getCode());
		desc.setHint(created.getCode());
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(value)){
			desc.setLabel(value);
		}
		else{
			desc.setLabel(created.getCode());
		}
		
		created.setDesc(desc);

		List<AnnotationConfig> vConfig = AnnotationConfigHandler.handle(f, Constraint.class);
		created.setValidations(vConfig);
		return created;
	}
	
	//TODO Implement mapped param lookup given via explicit path reference
	private <T> ParamConfig<?> findMappedParam(ModelConfig<T> mapsToModel, String fieldNm, MapsTo.Path mapsTo) {
		if(mapsTo!=null && !StringUtils.isEmpty(mapsTo.value())) { //: if @Path has an explicit value
			String path = mapsTo.value();
			ParamConfig<?> mapsToParam = mapsToModel.findParamByPath(path);
			return mapsToParam;
			//throw new UnsupportedOperationException("TODO: Explicit Mapped Path lookup is not yet implemented. Found: "+ mapsTo.value());
		}
		
		ParamConfig<?> mappedToParam = mapsToModel.templateParams().find(fieldNm);
		return mappedToParam;
	}
	
	private MapsTo.Path defaultPath = determineDefaultPath();
	
	private MapsTo.Path determineDefaultPath() {
		Field f = FieldUtils.getField(MapsTo.class, "DEFAULT_PATH");
		MapsTo.Path mapsTo = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
		return mapsTo;
	}
	
	public static MapsTo.Path createNewImplicitMapping(String mappedPath, boolean linked) {
		return new MapsTo.Path() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return MapsTo.Path.class;
			}
			
			@Override
			public String value() {
				return mappedPath;
			}
			
			@Override
			public boolean linked() {
				return linked;
			}

			@Override
			public KeyValue[] kv() {
				return null;
			}
			
			@Override
			public String toString() {
				return new StringBuilder().append(MapsTo.Path.class)
							.append(" value: ").append(value())
							.append(" linked: ").append(linked())
							.toString();
			}
		};
	}


	protected <P> ParamType.NestedCollection<P> createNestedCollectionType(ParamType.CollectionType colType) {
		Class<?> referredClass = ArrayList.class;
		String name = ClassUtils.getShortName(referredClass);
		
		ParamType.NestedCollection<P> nestedColType = new ParamType.NestedCollection<>(name, referredClass, colType);
		return nestedColType;
	}
	
	protected <T, P> ParamType createParamType(Class<P> determinedType, ModelConfig<?> mConfig, ModelConfigVistor visitedModels) {
		final ParamType pType;
		if(isPrimitive(determinedType)) {	//Primitives bare or with wrapper & String
			String name = ClassUtils.getShortNameAsProperty(ClassUtils.resolvePrimitiveIfNecessary(determinedType));
			pType = new ParamType.Field(name, determinedType);

		} else if(lookUpTypeClassMapping(determinedType)!=null) { //custom mapping overrides
			String name = lookUpTypeClassMapping(determinedType);
			pType = new ParamType.Field(name, determinedType);
			
		} else if(AnnotationUtils.findAnnotation(determinedType, Model.class)!=null) { 
			String name = ClassUtils.getShortName(determinedType);
			
			final ParamType.Nested<P> model; 
			pType = model = new ParamType.Nested<>(name, determinedType);
			
			final ModelConfig<P> nmConfig; 
			if(mConfig.getReferredClass()==determinedType) { //nested reference to itself
				nmConfig = (ModelConfig<P>)mConfig;
				
			} else if(visitedModels.contains(determinedType)) { //any nested model in hierarchy pointing back to any of its parents
				nmConfig = (ModelConfig<P>)visitedModels.get(determinedType);
				
			} else {
				nmConfig = buildModel(determinedType, visitedModels);
			}
			
			model.setModel(nmConfig);
			
		} else { //All others: Treat as field type instead of complex object that requires config traversal
			pType = new ParamType.Field(ClassUtils.getShortName(determinedType), determinedType);
			
		}
		return pType;
	}
	
	protected boolean isCollection(Class<?> clazz) {
		return determineCollectionType(clazz) != null;
	}
	
	protected ParamType.CollectionType determineCollectionType(Class<?> clazz) {
		if(clazz.isArray()	||	//Array
				Collection.class.isAssignableFrom(clazz)) { //Collection
			return ParamType.CollectionType.list;
			
		} else if(Page.class.isAssignableFrom(clazz)) {	//Page
			return ParamType.CollectionType.page;
		}
		return null;
	}
	
	abstract protected String lookUpTypeClassMapping(Class<?> clazz);
}
