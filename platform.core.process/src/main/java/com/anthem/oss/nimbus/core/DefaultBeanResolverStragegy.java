/**
 * 
 */
package com.anthem.oss.nimbus.core;

import java.util.Optional;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.ApplicationContext;

import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor
public class DefaultBeanResolverStragegy implements BeanResolverStrategy {

	private String beanPrefix = Constants.PREFIX_DEFAULT.code;
	
	private final ApplicationContext applicationContext;
	
	protected String resolvePrefix() {
		return getBeanPrefix()==null ? "" : getBeanPrefix();
	}
	
	protected String resolveBeanName(Class<?> type) {
		return resolvePrefix() + type.getSimpleName();
	}
	
	protected String resolveBeanName(String qualifier) {
		return resolvePrefix() + qualifier;
	}
	
	
	
	@Override
	public <T> T find(Class<T> type) {
		String bNmArr[] = applicationContext.getBeanNamesForType(type);
		
		// 1st: consider single bean declared with or without qualifier of given type
		if(ArrayUtils.isNotEmpty(bNmArr) && bNmArr.length==1)
			return applicationContext.getBean(bNmArr[0], type);
		
		// 2nd: if no bean OR multiple beans found by type, then use type's name as qualifier
		String bNm = resolveBeanName(type);
		return applicationContext.containsBean(bNm) ? applicationContext.getBean(bNm, type) : null;
	}

	@Override
	public <T> T get(Class<T> type) throws InvalidConfigException {
		return Optional.ofNullable(find(type))
				.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured with bean name following pattern: "
						+ " a) Single bean of type "+type
						+ " b) Bean with qualifier "+resolveBeanName(type)));
	}

	@Override
	public <T> T find(Class<T> type, String qualifier) {
		String bNm = resolveBeanName(qualifier);
		return applicationContext.containsBean(bNm) ? applicationContext.getBean(bNm, type) : null;
	}

	@Override
	public <T> T get(Class<T> type, String qualifier) throws InvalidConfigException {
		return Optional.ofNullable(find(type, qualifier))
				.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured with bean name following pattern: "+resolveBeanName(qualifier)));
	}

}
