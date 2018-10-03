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
package com.antheminc.oss.nimbus.context;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.defn.Constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * This class acts as BeanResolver for all the beans within the framework. 
 * BeanResolverStrategy should be used for resolving dependent beans within the framework instead of directly 
 * autowiring beans.
 * 
 * 
 * @author Soham Chakravarti
 *
 */
@Getter(value=AccessLevel.PROTECTED) @Setter 
public class DefaultBeanResolverStrategy implements BeanResolverStrategy {

	private String beanPrefix = Constants.PREFIX_DEFAULT.code;
	
	private final ApplicationContext applicationContext;
	
	public DefaultBeanResolverStrategy(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Environment getEnvironment() {
		return this.getApplicationContext().getEnvironment();
	}
	
	protected String resolvePrefix() {
		return getBeanPrefix()==null ? "" : getBeanPrefix();
	}
	
	protected String resolveBeanName(String qualifier) {
		return resolvePrefix() + qualifier;
	}
	
	protected String defaultBeanName(String qualifier) {
		return Constants.PREFIX_DEFAULT.code + qualifier;
	}
	
	
	@Override
	public <T> T find(Class<T> type) {
		String bNmArr[] = getApplicationContext().getBeanNamesForType(type);
		
		// 1st: consider single bean declared with or without qualifier of given type
		if(ArrayUtils.isNotEmpty(bNmArr) && bNmArr.length==1)
			return getApplicationContext().getBean(bNmArr[0], type);
		
		// 2nd: if no bean OR multiple beans found by type, then use type's name as qualifier
		return find(type, type.getSimpleName());
	}

	@Override
	public <T> T get(Class<T> type) throws InvalidConfigException {
		return Optional.ofNullable(find(type))
				.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured with bean name following pattern: "
						+ " a) Single bean of type "+type
						+ " b) Bean with qualifier "+resolveBeanName(type.getSimpleName())
						+ " c) Bean with qualifier "+defaultBeanName(type.getSimpleName())));
	}

	@Override
	public <T> T find(Class<T> type, String qualifier) {
		// 1: find using configured prefix
		String bNm = resolveBeanName(qualifier);
		if(getApplicationContext().containsBean(bNm)) 
			return getApplicationContext().getBean(bNm, type);
		
		// 2: check if prefix was overridden
		if(Constants.PREFIX_DEFAULT.code.equals(getBeanPrefix()))
			return null;
		
		// 3. find using initial default when bean not found using overridden prefix
		String defaultBeanNm = defaultBeanName(qualifier);
		return getApplicationContext().containsBean(defaultBeanNm) ? getApplicationContext().getBean(defaultBeanNm, type) : null;
	}

	@Override
	public <T> T get(Class<T> type, String qualifier) throws InvalidConfigException {
		return Optional.ofNullable(find(type, qualifier))
				.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured with bean name following pattern: "
						+ " a) Bean with qualifier "+resolveBeanName(qualifier)
						+ " b) Bean with qualifier "+defaultBeanName(qualifier)));

	}
	
	@Override
	public <T> T find(Class<T> type, Class<?>...generics) {
		String beanNames[] = getApplicationContext().getBeanNamesForType(ResolvableType.forClassWithGenerics(type, generics));
		if(ArrayUtils.getLength(beanNames)!=1)
			throw new InvalidConfigException("Only one bean expected for type+generic lookup, but found: "+beanNames.length
					+" for type: "+type+" and generics: "+ArrayUtils.toString(generics));
		
		T bean = getApplicationContext().getBean(beanNames[0], type);
		return bean;
	}
	
	@Override
	public <T> T get(Class<T> type, Class<?>... generics) throws InvalidConfigException {
		return Optional.ofNullable(find(type, generics))
				.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured but not found"));
	}
	
	@Override
	public <T> Collection<T> findMultiple(Class<T> type) {
		String bNmArr[] = getApplicationContext().getBeanNamesForType(type);
		
		if(ArrayUtils.isNotEmpty(bNmArr))
			return getApplicationContext().getBeansOfType(type).values();
		
		return null;
	}

	@Override
	public <T> Collection<T> getMultiple(Class<T> type) throws InvalidConfigException {
		return Optional.ofNullable(findMultiple(type))
				.orElseThrow(()->new InvalidConfigException("Beans of type "+type+" must be configured"));
	}
	
	@Override
	public Resource getResource(String path) {
		return getApplicationContext().getResource(path);
	}
}
