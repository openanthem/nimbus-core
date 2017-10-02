/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.validation.Constraint;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.UnsupportedScenarioException;
import com.anthem.oss.nimbus.core.domain.config.builder.AnnotationConfigHandler;
import com.anthem.oss.nimbus.core.domain.definition.AssociatedEntity;
import com.anthem.oss.nimbus.core.domain.definition.ConfigLoadException;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.Converters;
import com.anthem.oss.nimbus.core.domain.definition.Converters.ParamConverter;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.ViewParamBehavior;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.ViewStyle;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.EventHandlerConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.MappedParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultEventHandlerConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StateContextEntity;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactoryProducer;
import com.anthem.oss.nimbus.core.util.GenericUtils;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
abstract public class AbstractEntityConfigBuilder {

	protected JustLogit logit = new JustLogit(getClass());
	
	private final BeanResolverStrategy beanResolver;
	private final RulesEngineFactoryProducer rulesEngineFactoryProducer;
	
	public AbstractEntityConfigBuilder(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.rulesEngineFactoryProducer = beanResolver.get(RulesEngineFactoryProducer.class);
	}
	
	abstract public <T> ModelConfig<T> buildModel(Class<T> clazz, EntityConfigVisitor visitedModels);
	
	abstract public <T> ParamConfig<?> buildParam(ModelConfig<T> mConfig, Field f, EntityConfigVisitor visitedModels);
	
	abstract protected <T, P> ParamType buildParamType(ModelConfig<T> mConfig, ParamConfig<P> pConfig, Field f, EntityConfigVisitor visitedModels);
	abstract protected <T, P> ParamType buildParamType(ModelConfig<T> mConfig, ParamConfig<P> pConfig, ParamType.CollectionType colType, Class<?> pDirectOrColElemType, EntityConfigVisitor visitedModels); 
	
	public boolean isPrimitive(Class<?> determinedType) {
		return ClassUtils.isPrimitiveOrWrapper(determinedType) || String.class==determinedType;
	}
	
	public <T> DefaultModelConfig<T> createModel(Class<T> referredClass, EntityConfigVisitor visitedModels) {
		MapsTo.Type mapsToType = AnnotationUtils.findAnnotation(referredClass, MapsTo.Type.class);
		
		final DefaultModelConfig<T> created;
		if(mapsToType!=null) {
			
			ModelConfig<?> mapsTo = visitedModels.get(mapsToType.value());
			if(mapsTo==null)
				throw new InvalidConfigException(MapsTo.Type.class.getSimpleName()+" not found: "+mapsToType + " in mapped: " + referredClass);
			
			created = new MappedDefaultModelConfig<>(mapsTo, referredClass);
			
		} else {
			created = new DefaultModelConfig<>(referredClass);
		}
		
		// annotation config: viewStyle
		created.setUiStyles(AnnotationConfigHandler.handleSingle(referredClass, ViewStyle.class));
		
		// handle repo
		Repo rep = AnnotationUtils.findAnnotation(referredClass, Repo.class);
		created.setRepo(rep);
		
		// set alias from domain or model
		assignDomainAndModel(created, created::setAlias);
				
		// rules
		Optional.ofNullable(created.getAlias())
			.map(d->rulesEngineFactoryProducer.getFactory(referredClass))
			.map(f->f.createConfig(created.getAlias()))
				.ifPresent(c->created.setRulesConfig(c));
		return created; 
	}
	
	protected void assignDomainAndModel(DefaultModelConfig<?> created, Consumer<String> cb) {
		// prefer @Domain or @Model declared on current class
		Domain domain = AnnotationUtils.findAnnotation(created.getReferredClass(), Domain.class);
		created.setDomain(domain);
		
		// set model if domain is absent
		Model model = AnnotationUtils.findAnnotation(created.getReferredClass(), Model.class);
		created.setModel(model);

		
		if(domain==null && model!=null)
			cb.accept(model.value());
		else
			if(domain!=null && model==null)
				cb.accept(domain.value());
		else
			if(domain!=null && model!=null // both present with different alias entries
					&& StringUtils.trimToNull(domain.value())!=null && StringUtils.trimToNull(model.value())!=null 
					&& !StringUtils.equals(domain.value(), model.value())) {
				
				// prefer annotation declared directly on class
				if(AnnotationUtils.isAnnotationInherited(Domain.class, created.getReferredClass()) 
						&& !AnnotationUtils.isAnnotationInherited(Model.class, created.getReferredClass()))
					cb.accept(model.value());
				else 
					if(!AnnotationUtils.isAnnotationInherited(Domain.class, created.getReferredClass()) 
							&& AnnotationUtils.isAnnotationInherited(Model.class, created.getReferredClass()))	
						cb.accept(domain.value());
				else
					throw new InvalidConfigException("A model can have alias defined in either @Domain or @Model. "
							+ "Found in both with different values for class: "+created.getReferredClass()
							+" with @Domain: "+domain+" and @Model: "+model);
			}
		else 
			cb.accept(domain.value());
		
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
	
	public DefaultParamConfig<?> createParam(ModelConfig<?> mConfig, Field f, EntityConfigVisitor visitedModels) {
		MapsTo.Path mapsToPath = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
		MapsTo.Mode mapsToMode = MapsTo.getMode(mapsToPath);
		
		// no path specified
		if(mapsToMode==MapsTo.Mode.UnMapped)
			return decorateParam(mConfig, f, DefaultParamConfig.instantiate(mConfig, f.getName()), visitedModels); 
			
		// check if field is mapped with linked=true: which would require parent model to also be mapped
		if(mapsToMode==MapsTo.Mode.MappedAttached)
			return createMappedParamAttached(mConfig, f, visitedModels, mapsToPath);
		else 
			return createMappedParamDetached(mConfig, f, visitedModels, mapsToPath);
	}
	
	private DefaultParamConfig<?> createMappedParamAttached(ModelConfig<?> mConfig, Field f, EntityConfigVisitor visitedModels, MapsTo.Path mapsToPath) {
		// param field is linked: enclosing model must be mapped
		if(!mConfig.isMapped())
			throw new InvalidConfigException("Mapped param field: "+f.getName()+" is mapped with linked=true. Enclosing model: "+mConfig.getReferredClass()+" must be mapped, but was not.");

		// param field is linked: enclosing mapped model's config must have been loaded
		Class<?> mapsToModelClass = mConfig.findIfMapped().getMapsTo().getReferredClass();
		ModelConfig<?> mapsToModel = visitedModels.get(mapsToModelClass);
		if(mapsToModel==null)
			throw new ConfigLoadException("Mapped param field: "+f.getName()+" is mapped with linked=true. Enclosing model: "+mConfig.getReferredClass()
				+" mapsToModelClass: "+mapsToModelClass+" which must have been loaded prior, but wasn't.");
		
		// find mapsTo param from mapsTo model
		ParamConfig<?> mapsToParam = findMappedParam(mapsToModel, f.getName(), mapsToPath);
		if(mapsToParam==null)
			throw new InvalidConfigException("No mapsTo param found for mapped param field: "+f.getName()+" in enclosing model:"+mConfig.getReferredClass()+" with mapsToPath: "+mapsToPath);
		
		// handle transient
		//DefaultParamConfig<?> mappedParamAttached = mapsToPath.nature()==MapsTo.Nature.TransientColElem ?
		
		return decorateParam(mConfig, f, new MappedDefaultParamConfig<>(f.getName(), mapsToModel, mapsToParam, mapsToPath), visitedModels);
	}
	
	private DefaultParamConfig<?> createMappedParamDetached(ModelConfig<?> mConfig, Field mappedField, EntityConfigVisitor visitedModels, MapsTo.Path mapsToPath) {
		if(!isCollection(mappedField.getType())) {
			return createMappedParamDetachedNested(mConfig, mappedField, visitedModels, mapsToPath);
		} 
		
		// detached collection
		return createMappedParamDetachedCollection(mConfig, mappedField, visitedModels, mapsToPath);
	}
	
	/**
	 * For detached mode, simulate mapsToParam. 
	 * This would require creating an enclosing ModelConfig which encloses the mapsTo Param.
	 * 
	 * Enclosing ModelConfig's type (referredClass) is top level holder class.
	 * 
	 * MapsTo ParaConfig's type is same as MapsTo.Type on mappedParam's referredClass. 
	 * If mappedParam's referredClass doesn't have MapsTo.Type defined, then mappedParam's referredClass would be used for self-detached mode simulation.
	 */
	private DefaultParamConfig<?> createMappedParamDetachedNested(ModelConfig<?> mConfig, Field mappedField, EntityConfigVisitor visitedModels, MapsTo.Path mapsToPath) {
		// create mapsToParam's enclosing ModelConfig
		DefaultModelConfig<?> simulatedEnclosingModel = new DefaultModelConfig<>(SimulatedNestedParamEnclosingEntity.class);
		
		// create mapsToParam and attach to enclosing model
		DefaultParamConfig<?> simulatedMapsToParam = decorateParam(simulatedEnclosingModel, DefaultParamConfig.instantiate(simulatedEnclosingModel, MapsTo.DETACHED_SIMULATED_FIELD_NAME), visitedModels);
		simulatedEnclosingModel.templateParams().add(simulatedMapsToParam);
		
		// build mapsToParam's type
		MapsTo.Type mappedParamRefClassMapsToType = AnnotationUtils.findAnnotation(mappedField.getType(), MapsTo.Type.class);
		Class<?> mappedParamRefClassMapsToEntityClass = mappedParamRefClassMapsToType!=null ? mappedParamRefClassMapsToType.value() : mappedField.getType();
		
		ParamType pType = buildParamType(simulatedEnclosingModel, simulatedMapsToParam, null, mappedParamRefClassMapsToEntityClass, visitedModels);
		simulatedMapsToParam.setType(pType);
		
		DefaultParamConfig<?> mappedParam = decorateParam(mConfig, mappedField, new MappedDefaultParamConfig<>(mappedField.getName(), simulatedEnclosingModel, simulatedMapsToParam, mapsToPath), visitedModels);
		return mappedParam;
	}
	
	public DefaultParamConfig<?> createMappedParamDetachedCollection(ModelConfig<?> mConfigOfMappedParam, Field mappedField, EntityConfigVisitor visitedModels, MapsTo.Path mapsToPath) {
		// create mapsToParam's enclosing ModelConfig
		DefaultModelConfig<?> simulatedEnclosingModel = new DefaultModelConfig<>(SimulatedCollectionParamEnclosingEntity.class);
		
		// create mapsToParam and attach to enclosing model
		DefaultParamConfig<?> simulatedMapsToParam = decorateParam(simulatedEnclosingModel, DefaultParamConfig.instantiate(simulatedEnclosingModel, MapsTo.DETACHED_SIMULATED_FIELD_NAME), visitedModels);
		simulatedEnclosingModel.templateParams().add(simulatedMapsToParam);
		
		// determine collection param generic element type
		final ParamType.CollectionType colType = determineCollectionType(mappedField.getType());
		final Class<?> determinedMappedColElemType = GenericUtils.resolveGeneric(mConfigOfMappedParam.getReferredClass(), mappedField);
		
		MapsTo.Type mappedParamRefClassMapsToType = AnnotationUtils.findAnnotation(determinedMappedColElemType, MapsTo.Type.class);
		Class<?> mappedParamRefClassMapsToEntityClass = mappedParamRefClassMapsToType!=null ? mappedParamRefClassMapsToType.value() : determinedMappedColElemType;
		
		ParamType pType = buildParamType(simulatedEnclosingModel, simulatedMapsToParam, colType, mappedParamRefClassMapsToEntityClass, visitedModels);
		simulatedMapsToParam.setType(pType);
		
		DefaultParamConfig<?> mappedParam = decorateParam(mConfigOfMappedParam, mappedField, new MappedDefaultParamConfig<>(mappedField.getName(), simulatedEnclosingModel, simulatedMapsToParam, mapsToPath), visitedModels);
		return mappedParam;
	}

	@Getter @Setter @Domain("_simulatedDetachedNested")
	public static class SimulatedNestedParamEnclosingEntity<E> {
		private E detachedParam; 
	}
	
	
	@Getter @Setter @Domain("_simulatedDetachedCollection")
	public static class SimulatedCollectionParamEnclosingEntity<E> {
		private List<E> detachedParam;
	}

	private <T, P> ParamConfig<P> createParamCollectionElemMappedAttached(ModelConfig<T> mConfig, MappedParamConfig<P, ?> pConfig, ModelConfig<List<P>> colModelConfig, EntityConfigVisitor visitedModels, Class<?> colElemClass, MapsTo.Path mapsToColParamPath) {
		// colParam is mapped as Attached, but parent enclosing Model is un-mapped :- throw Ex
		if(!mConfig.isMapped()) { 	
			throw new InvalidConfigException("Param: "+pConfig.getCode()+" has @MapsTo.Path "+mapsToColParamPath+" with resolved mode: "+MapsTo.Mode.MappedAttached
						+" Attached Mapped Param must have Model that is mapped, but found with no @MapsTo.Model mappings for: "+mConfig.getReferredClass());
		}
		
		//ModelConfig<?> mapsToEnclosingModel = visitedModels.get(mConfig.findIfMapped().getMapsTo().getReferredClass());
		//logit.debug(()->"[create.pColElem] [colParam is mapped] [elemClass same] [Attached] Found parent mapsToEnclosingModel: "+mapsToEnclosingModel+" from visitedMmodels using: "+mConfig.findIfMapped().getMapsTo().getReferredClass());
		
		return createParamCollectionElemMapped(/*mapsToEnclosingModel, */pConfig, colModelConfig, visitedModels, colElemClass, mapsToColParamPath);
	}
	
	private <T, P> ParamConfig<P> createParamCollectionElemMappedDetached(ModelConfig<T> mConfig, MappedParamConfig<P, ?> pConfig, ModelConfig<List<P>> colModelConfig, EntityConfigVisitor visitedModels, Class<?> colElemClass, MapsTo.Path mapsToColParamPath) {
		return createParamCollectionElemMapped(pConfig, colModelConfig, visitedModels, colElemClass, mapsToColParamPath);
	}
	
	
	private <T, P> ParamConfig<P> createParamCollectionElemMapped(MappedParamConfig<P, ?> pConfig, ModelConfig<List<P>> colModelConfig, EntityConfigVisitor visitedModels, Class<?> colElemClass, MapsTo.Path mapsToColParamPath) {
		//ParamConfig<?> mapsToColParamConfig = findMappedParam(mapsToEnclosingModel, pConfig.getCode(), mapsToColParamPath);
		ParamConfig<?> mapsToColParamConfig = pConfig.getMapsTo();
		logit.debug(()->"[create.pColElem] [colParam is mapped] [elemClass same] [Attached] Found mapsToColParamConfig for "+pConfig.getCode()+" with mapsToPath of colParam: "+mapsToColParamPath+" -> "+mapsToColParamConfig);
		
		@SuppressWarnings("unchecked")
		ParamConfig<P> mapsToColElemParamConfig = (ParamConfig<P>)mapsToColParamConfig.getType().findIfCollection().getElementConfig();

		
		// colParam is mapped: colElemModel is NOT explicitly mapped BUT colElemClass is NOT SAME as mappedElemClass :- throw Ex
		if(colElemClass!=mapsToColElemParamConfig.getReferredClass()) {
			
			// handle {index} scenario in MapsTo.Path
			//if(StringUtils.contains(mapsToColParamPath.value(), Constants.MARKER_COLLECTION_ELEM_INDEX.code)) {
			if(MapsTo.hasCollectionPath(mapsToColParamPath)) {
				String colElemPathAfterIndexMarker = mapsToColParamPath.colElemPath();//StringUtils.substringAfter(mapsToColParamPath.value(), Constants.MARKER_COLLECTION_ELEM_INDEX.code);
				ParamConfig<P> mapsToNestedColElemParamConfig = mapsToColElemParamConfig.findParamByPath(colElemPathAfterIndexMarker);
				
				return createParamCollectionElementInternal(colModelConfig, mapsToNestedColElemParamConfig, mapsToColParamPath, visitedModels, pConfig.getCode());
				
			} else {
			
				MapsTo.Type mapsToElemModel = AnnotationUtils.findAnnotation(colElemClass, MapsTo.Type.class);
				
				if(mapsToElemModel==null)
					throw new InvalidConfigException("Mapped Elem Class is not same as MapsTo Elem Class. Must be same or an explicit MapsTo.Model mapping is required. "
							//+ " For EnclosingModel: "+mapsToEnclosingModel.getReferredClass()
							+" param: "+pConfig.getCode()
							+ " Expected elemClass: "+colElemClass+" but found mapsToElemClass: "+mapsToColElemParamConfig.getReferredClass());
			}
		}
		
		return createParamCollectionElementInternal(colModelConfig, mapsToColElemParamConfig, mapsToColParamPath, visitedModels, pConfig.getCode());
	}
	
	public <T, P> ParamConfig<P> createParamCollectionElement(ModelConfig<T> mConfig, ParamConfig<P> pConfig, ModelConfig<List<P>> colModelConfig, EntityConfigVisitor visitedModels, Class<?> colElemClass) {
		logit.trace(()->"[create.pColElem] starting to process colElemClass: "+colElemClass+" with pConfig :"+pConfig.getCode());
		
		MapsTo.Path mapsToColParamPath = pConfig.isMapped() ? pConfig.findIfMapped().getPath() : null;
		MapsTo.Mode mapsToColParamMode = MapsTo.getMode(mapsToColParamPath);
		
		logit.debug(()->"[create.pColElem] mapsToColParam: "+mapsToColParamPath);
		logit.debug(()->"[create.pColElem] mapsToModeColParam: "+mapsToColParamMode);
		logit.debug(()->"[create.pColElem] colParamCode: "+pConfig.getCode());
		
		if(mapsToColParamMode==MapsTo.Mode.UnMapped) { 
			ParamConfig<P> pCoreElemConfig = createParamCollectionElementInternal(colModelConfig, null, null, visitedModels, pConfig.getCode());
		
			logit.trace(()->"[create.pColElem] [colParam is UnMapped] returning core pColElem Config as colElem is UnMapped.");
			return pCoreElemConfig;
			
		} else if(mapsToColParamMode==MapsTo.Mode.MappedAttached) {
			ParamConfig<P> pMappedAttachedElemConfig = createParamCollectionElemMappedAttached(mConfig, pConfig.findIfMapped(), colModelConfig, visitedModels, colElemClass, mapsToColParamPath);
			return pMappedAttachedElemConfig;
			
		} else if(mapsToColParamMode==MapsTo.Mode.MappedDetached) {
			ParamConfig<P> pMappedDetachedElemConfig = createParamCollectionElemMappedDetached(mConfig, pConfig.findIfMapped(), colModelConfig, visitedModels, colElemClass, mapsToColParamPath);
			return pMappedDetachedElemConfig;
			
		} else {
			throw new UnsupportedScenarioException("Param: "+pConfig.getCode()+" has @MapsTo.Path "+mapsToColParamPath+" with unknown mode: "+mapsToColParamMode
					+" in enclosing model: "+mConfig.getReferredClass());
		}
	}
	
	private <P> ParamConfig<P> createParamCollectionElementInternal(ModelConfig<List<P>> colModelConfig, ParamConfig<P> mapsToColElemParamConfig, MapsTo.Path mapsToColParamPath, EntityConfigVisitor visitedModels, String colParamCode) {
		final String collectionElemPath = createCollectionElementPath(colParamCode);
		
		final ParamConfig<P> created;
		if(colModelConfig.isMapped()) {
			final MapsTo.Path mapsToColElemParamPathAnnotation = 
					(mapsToColParamPath==null) 
						? null : MappedDefaultParamConfig.createNewImplicitMapping(collectionElemPath, mapsToColParamPath.linked(), mapsToColParamPath.colElemPath(), mapsToColParamPath.detachedState());
			
			created = new MappedDefaultParamConfig<>(collectionElemPath, colModelConfig, mapsToColElemParamConfig, mapsToColElemParamPathAnnotation);

		} else if(mapsToColElemParamConfig==null) {
			created = decorateParam(colModelConfig, DefaultParamConfig.instantiate(colModelConfig, collectionElemPath), visitedModels);
			
		} else {
			created = mapsToColElemParamConfig;
			
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
	
	private <P> DefaultParamConfig<P> decorateParam(ModelConfig<?> mConfig, Field f, DefaultParamConfig<P> created, EntityConfigVisitor visitedModels) {
		created.setUiNatures(AnnotationConfigHandler.handle(f, ViewParamBehavior.class));
		created.setUiStyles(AnnotationConfigHandler.handleSingle(f, ViewStyle.class));
		created.setExecutionConfigs(new ArrayList<>(AnnotatedElementUtils.findMergedRepeatableAnnotations(f, Execution.Config.class)));
		
		
		if(AnnotatedElementUtils.isAnnotated(f, Converters.class)) {
			Converters convertersAnnotation = AnnotationUtils.getAnnotation(f, Converters.class);
			
			List<ParamConverter> converters = new ArrayList<>();
			
			Arrays.asList(convertersAnnotation.converters())
						.forEach((converterClass)->converters.add(beanResolver.get(converterClass)));
			
			created.setConverters(converters);
		}

		if(AnnotatedElementUtils.isAnnotated(f, Model.Param.Values.class)) {
			Model.Param.Values aVal = AnnotationUtils.getAnnotation(f, Model.Param.Values.class);
			
			//Model.Param.Values.Source srcValues = ClassLoadUtils.newInstance(aVal.value());
			//List<ParamValue> values = srcValues.getValues(created.getCode());
			created.setValues(aVal);
			//created.setValues(values);
		}
		
		if(AnnotatedElementUtils.isAnnotated(f, AssociatedEntity.class)) {
			AssociatedEntity[] associatedEntityAnnotationArr = f.getAnnotationsByType(AssociatedEntity.class);
			created.setAssociatedEntities(Arrays.asList(associatedEntityAnnotationArr));
		}
		
		List<AnnotationConfig> vConfig = AnnotationConfigHandler.handle(f, Constraint.class);
		created.setValidations(vConfig);

		EventHandlerConfig eventConfig = createEventHandlerConfig(f);
		created.setEventHandlerConfig(eventConfig);
		
		return decorateParam(mConfig, created, visitedModels);
	}
	
	@SuppressWarnings("unchecked")
	private EventHandlerConfig createEventHandlerConfig(AnnotatedElement aElem) {
		final DefaultEventHandlerConfig eventConfig = new DefaultEventHandlerConfig();
		
//		addEventHandler(aElem, OnStateLoad.class, OnStateLoadHandler.class, (ac, handler)->{
//			eventConfig.add(ac, (OnStateLoadHandler<Annotation>)handler);
//		});
		
		List<AnnotationConfig> onStateLoadAnnotations = AnnotationConfigHandler.handle(aElem, OnStateLoad.class);
		if(!CollectionUtils.isEmpty(onStateLoadAnnotations)) { 
			
			onStateLoadAnnotations.stream()
				.forEach(ac->{
					OnStateLoadHandler<Annotation> handler = getBeanResolver().get(OnStateLoadHandler.class, ac.getAnnotation().annotationType());
					eventConfig.add(ac, handler);
				});
		}

		
		List<AnnotationConfig> onStateChangeAnnotations = AnnotationConfigHandler.handle(aElem, OnStateChange.class);
		if(!CollectionUtils.isEmpty(onStateChangeAnnotations)) { 
			
			onStateChangeAnnotations.stream()
				.forEach(ac->{
					OnStateChangeHandler<Annotation> handler = getBeanResolver().get(OnStateChangeHandler.class, ac.getAnnotation().annotationType());
					eventConfig.add(ac, handler);
				});
		}

		return eventConfig.isEmpty() ? null : eventConfig;
	}
	
//	private <T> void addEventHandler(AnnotatedElement aElem, Class<Annotation> aClass, Class<? extends T> handlerClass, BiConsumer<AnnotationConfig, T> cb) {
//		List<AnnotationConfig> eventAnnotations = AnnotationConfigHandler.handle(aElem, aClass);
//		if(!CollectionUtils.isEmpty(eventAnnotations)) { 
//			
//			eventAnnotations.stream()
//				.forEach(ac->{
//					T handler = getBeanResolver().find(handlerClass, ac.getAnnotation().annotationType());
//					cb.accept(ac, handler);
//				});
//		}
//	}
	
	private ParamConfig<StateContextEntity> cachedRuntimeEntityParamConfig;
	
	/**
	 * build and assign RuntimeEntity Config
	 */
	protected <P> DefaultParamConfig<P> decorateParam(ModelConfig<?> mConfig, DefaultParamConfig<P> created, EntityConfigVisitor visitedModels) {
		// do not make nested runtimeConfig;
		if(StringUtils.equals(created.getCode(), Constants.SEPARATOR_CONFIG_ATTRIB.code))
			return created;
		
		if(cachedRuntimeEntityParamConfig==null) {
			DefaultParamConfig<StateContextEntity> pRuntimeConfig = DefaultParamConfig.instantiate(mConfig, Constants.SEPARATOR_CONFIG_ATTRIB.code);
			cachedRuntimeEntityParamConfig = pRuntimeConfig;
			
			ModelConfig<StateContextEntity> mRuntimeConfig = buildModel(StateContextEntity.class, visitedModels);
			
			ParamType.Nested<StateContextEntity> nestedType = new ParamType.Nested<>(StateContextEntity.class.getSimpleName(), StateContextEntity.class);
			nestedType.setModel(mRuntimeConfig);
			
			pRuntimeConfig.setType(nestedType);
		}
		
		created.setContextParam(cachedRuntimeEntityParamConfig);
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
	
	protected <P> ParamType.NestedCollection<P> createNestedCollectionType(ParamType.CollectionType colType) {
		Class<?> referredClass = ArrayList.class;
		String name = ClassUtils.getShortName(referredClass);
		
		ParamType.NestedCollection<P> nestedColType = new ParamType.NestedCollection<>(name, referredClass, colType);
		return nestedColType;
	}
	
	protected <T, P> ParamType createParamType(boolean isArray, Class<P> determinedType, ModelConfig<?> mConfig, EntityConfigVisitor visitedModels) {
		final ParamType pType;
		if(isPrimitive(determinedType)) {	//Primitives bare or with wrapper & String
			String name = ClassUtils.getShortNameAsProperty(ClassUtils.resolvePrimitiveIfNecessary(determinedType));
			pType = new ParamType.Field(isArray, name, determinedType);

		} else if(lookUpTypeClassMapping(determinedType)!=null) { //custom mapping overrides
			String name = lookUpTypeClassMapping(determinedType);
			pType = new ParamType.Field(isArray, name, determinedType);
			
		} else if(AnnotationUtils.findAnnotation(determinedType, Model.class)!=null) { 
			String name = ClassUtils.getShortName(determinedType);
			pType = createParamTypeNested(name, determinedType, mConfig, visitedModels);
			
		} else { //All others: Treat as field type instead of complex object that requires config traversal
			pType = new ParamType.Field(isArray, ClassUtils.getShortName(determinedType), determinedType);
			
		}
		return pType;
	}
	
	protected <P> ParamType.Nested<P> createParamTypeNested(String typeName, Class<P> determinedType, ModelConfig<?> mConfig, EntityConfigVisitor visitedModels) {
		
		final ParamType.Nested<P> nestedParamType = new ParamType.Nested<>(typeName, determinedType);
		
		final ModelConfig<P> nmConfig; 
		if(mConfig.getReferredClass()==determinedType) { //nested reference to itself
			nmConfig = (ModelConfig<P>)mConfig;
			
		} else if(visitedModels.contains(determinedType)) { //any nested model in hierarchy pointing back to any of its parents
			nmConfig = (ModelConfig<P>)visitedModels.get(determinedType);
			
		} else {
			nmConfig = buildModel(determinedType, visitedModels);
		}
		
		nestedParamType.setModel(nmConfig);
		return nestedParamType;
	}
	
	protected boolean isCollection(Class<?> clazz) {
		return determineCollectionType(clazz) != null;
	}
	
	protected ParamType.CollectionType determineCollectionType(Class<?> clazz) {
		if(clazz.isArray())	//Array
			return ParamType.CollectionType.array;
		
		else if(Collection.class.isAssignableFrom(clazz))  //Collection
			return ParamType.CollectionType.list;
			
		else if(Page.class.isAssignableFrom(clazz))	//Page
			return ParamType.CollectionType.page;
		
		return null;
	}
	
	abstract protected String lookUpTypeClassMapping(Class<?> clazz);
}
