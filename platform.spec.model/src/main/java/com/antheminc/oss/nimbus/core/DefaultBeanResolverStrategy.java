/**
 * 
 */
package com.antheminc.oss.nimbus.core;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.util.JustLogit;

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
@Getter @Setter 
public class DefaultBeanResolverStrategy implements BeanResolverStrategy {

	private String beanPrefix = Constants.PREFIX_DEFAULT.code;
	
	private final ApplicationContext applicationContext;
	
	public DefaultBeanResolverStrategy(ApplicationContext applicationContext) {
		/* To ensure JustLogit is available in the spring context for all the beans, 
		 * provided all the framework beans use DefaultBeanResolverStrategy for resolving dependent beans */
		applicationContext.getBean(JustLogit.class);
		this.applicationContext = applicationContext;
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
		String bNmArr[] = applicationContext.getBeanNamesForType(type);
		
		// 1st: consider single bean declared with or without qualifier of given type
		if(ArrayUtils.isNotEmpty(bNmArr) && bNmArr.length==1)
			return applicationContext.getBean(bNmArr[0], type);
		
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
		if(applicationContext.containsBean(bNm)) 
			return applicationContext.getBean(bNm, type);
		
		// 2: check if prefix was overridden
		if(Constants.PREFIX_DEFAULT.code.equals(getBeanPrefix()))
			return null;
		
		// 3. find using initial default when bean not found using overridden prefix
		String defaultBeanNm = defaultBeanName(qualifier);
		return applicationContext.containsBean(defaultBeanNm) ? applicationContext.getBean(defaultBeanNm, type) : null;
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
		String beanNames[] = applicationContext.getBeanNamesForType(ResolvableType.forClassWithGenerics(type, generics));
		if(ArrayUtils.getLength(beanNames)!=1)
			throw new InvalidConfigException("Only one bean expected for type+generic lookup, but found: "+beanNames.length
					+" for type: "+type+" and generics: "+ArrayUtils.toString(generics));
		
		T bean = applicationContext.getBean(beanNames[0], type);
		return bean;
	}
	
	@Override
	public <T> T get(Class<T> type, Class<?>... generics) throws InvalidConfigException {
		return Optional.ofNullable(find(type, generics))
				.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured but not found"));
	}
	
	@Override
	public <T> Collection<T> findMultiple(Class<T> type) {
		String bNmArr[] = applicationContext.getBeanNamesForType(type);
		
		if(ArrayUtils.isNotEmpty(bNmArr))
			return applicationContext.getBeansOfType(type).values();
		
		return null;
	}

	@Override
	public <T> Collection<T> getMultiple(Class<T> type) throws InvalidConfigException {
		return Optional.ofNullable(findMultiple(type))
				.orElseThrow(()->new InvalidConfigException("Beans of type "+type+" must be configured"));
	}
}
