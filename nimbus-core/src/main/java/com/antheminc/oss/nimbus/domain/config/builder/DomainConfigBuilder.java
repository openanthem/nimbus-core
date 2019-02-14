/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.config.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.builder.EntityConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.builder.EntityConfigVisitor;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.pojo.ClassLoadUtils;

import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter(value=AccessLevel.PROTECTED)
public class DomainConfigBuilder {

	private final Map<String, ModelConfig<?>> cacheDomainRootModel;
	private final EntityConfigVisitor configVisitor;

	private final EntityConfigBuilder configBuilder;
	private final List<String> basePackages;
	private final List<String> packagesToexclude;
	
	ClassPathScanningCandidateComponentProvider scanner;
	
	protected final JustLogit logit = new JustLogit(DomainConfigBuilder.class);
	
	
	public DomainConfigBuilder(EntityConfigBuilder configBuilder, List<String> basePackages, List<String> packagesToexclude) {
		this.configBuilder = configBuilder;
		this.basePackages = Optional.ofNullable(basePackages)
								.orElseThrow(()->new InvalidConfigException("base packages must be configured for scanning domain models."));
		
		this.cacheDomainRootModel = new HashMap<>();
		this.configVisitor = new EntityConfigVisitor();
		this.packagesToexclude=packagesToexclude;
		this.scanner = new ClassPathScanningCandidateComponentProvider(false);
	}
	
	public ModelConfig<?> getRootDomainOrThrowEx(String rootAlias) {
		return Optional.ofNullable(getRootDomain(rootAlias))
				.orElseThrow(()->new InvalidConfigException("Domain model config not found for root-alias: "+rootAlias));
	}


	public ModelConfig<?> getRootDomain(String rootAlias) {
		return getCacheDomainRootModel().get(rootAlias);
	}
	
	public ModelConfig<?> getModel(String alias) {
		return getConfigVisitor().get(alias);
	}
	
	public ModelConfig<?> getModel(Class<?> mClass) {
		return getConfigVisitor().get(mClass);
	}
	
	@PostConstruct
	public void load() {
		logit.trace(()->"Start-> Load model config...");
		logit.trace(()->"Configured basePackages: "+getBasePackages());
		logit.trace(()->"Excluded basePackages: "+getPackagesToexclude());
		
		if (packagesToexclude != null)				
			packagesToexclude.forEach(pkg -> scanner.addExcludeFilter(new RegexPatternTypeFilter(Pattern.compile(pkg))));		
		
		scanner.addIncludeFilter(new AnnotationTypeFilter(Domain.class));
		
		List<String> rootBasePackages = EntityConfigVisitor.determineRootPackages(getBasePackages());
		
		rootBasePackages.forEach(this::handlePackage);
		
		logit.trace(()->"End-> Load model config...");
	}
	
	public <T> void handlePackage(String basePackage) {
				
		for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
			String classNm = bd.getBeanClassName();
			
			Class<T> clazz = ClassLoadUtils.loadClass(classNm);
			handleClass(clazz);
		}
	}

	public <T> void handleClass(Class<T> clazz) {
		Domain domain = AnnotationUtils.findAnnotation(clazz, Domain.class);
		
		ModelConfig<T> mConfig = getConfigBuilder().load(clazz, getConfigVisitor());
		getCacheDomainRootModel().put(domain.value(), mConfig);
	}
}
