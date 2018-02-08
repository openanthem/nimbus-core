/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.config.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.antheminc.oss.nimbus.core.domain.model.config.builder.EntityConfigVisitor;
import com.antheminc.oss.nimbus.core.util.ClassLoadUtils;
import com.antheminc.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
@RefreshScope
public class DomainConfigBuilder {

	private final Map<String, ModelConfig<?>> cacheDomainRootModel;
	private final EntityConfigVisitor configVisitor;

	private final EntityConfigBuilder configBuilder;
	private final List<String> basePackages;
	
	protected final JustLogit logit = new JustLogit(this.getClass());
	
	
	public DomainConfigBuilder(EntityConfigBuilder configBuilder, List<String> basePackages) {
		this.configBuilder = configBuilder;
		this.basePackages = Optional.ofNullable(basePackages)
								.orElseThrow(()->new InvalidConfigException("base packages must be configured for scanning domain models."));
		
		this.cacheDomainRootModel = new HashMap<>();
		this.configVisitor = new EntityConfigVisitor();
	}
	
	public ModelConfig<?> getRootDomainOrThrowEx(String rootAlias) {
		return Optional.ofNullable(getRootDomain(rootAlias))
				.orElseThrow(()->new InvalidConfigException("Domain model config not found for root-alias: "+rootAlias));
	}


	public ModelConfig<?> getRootDomain(String rootAlias) {
		return cacheDomainRootModel.get(rootAlias);
	}
	
	public ModelConfig<?> getModel(String alias) {
		return configVisitor.get(alias);
	}
	
	public ModelConfig<?> getModel(Class<?> mClass) {
		return configVisitor.get(mClass);
	}
	
	@PostConstruct
	public void load() {
		logit.trace(()->"Start-> Load model config...");
		logit.trace(()->"Configured basePackages: "+basePackages);
		
		List<String> rootBasePackages = EntityConfigVisitor.determineRootPackages(basePackages);
		
		rootBasePackages.forEach(this::handlePackage);
		
		logit.trace(()->"End-> Load model config...");
	}
	
	public <T> void handlePackage(String basePackage) {
		
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Domain.class));
		
		for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
			String classNm = bd.getBeanClassName();
			
			Class<T> clazz = ClassLoadUtils.loadClass(classNm);
			Domain domain = AnnotationUtils.findAnnotation(clazz, Domain.class);
			
			ModelConfig<T> mConfig = configBuilder.load(clazz, configVisitor);
			cacheDomainRootModel.put(domain.value(), mConfig);
		}
	}

}
