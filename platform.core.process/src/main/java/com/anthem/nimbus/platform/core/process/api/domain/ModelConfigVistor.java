/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreModelConfig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor
public class ModelConfigVistor {

	@Getter
	private final Map<Class<?>, CoreModelConfig<?>> visitedModels = new HashMap<>();
	
	public boolean contains(Class<?> clazz) {
		if(getVisitedModels()==null) return false;
		
		return getVisitedModels().containsKey(clazz);
	}
	
	public <T> void set(Class<?> clazz, CoreModelConfig<T> mConfig) {
		getVisitedModels().put(clazz, mConfig);
	}
	
	@SuppressWarnings("unchecked")
	public <T> CoreModelConfig<T> get(Class<T> clazz) {
		return (CoreModelConfig<T>)getVisitedModels().get(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T> CoreModelConfig<T> mapsToModel(MapsTo.Model mapsTo) {
		return (mapsTo != null) ? (CoreModelConfig<T>)get(mapsTo.value()) : null;
	}
	
	public static List<String> determineRootPackages(List<String> basePackages) {
		if(CollectionUtils.isEmpty(basePackages)) return Collections.emptyList();
		
		ArrayList<String> rootPackages = new ArrayList<>(basePackages);
		for(String check : basePackages) {
			
			String remove = null;
			for(String existing : rootPackages) {
				if(!StringUtils.equals(existing, check) && StringUtils.startsWith(existing, check)) {
					remove = existing;
					break;
				} 
			}
			
			if(remove!=null) {
				rootPackages.remove(remove);
			}
		}
		
		return rootPackages;
	}

}
