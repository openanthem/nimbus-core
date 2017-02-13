/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor
public class EntityConfigVistor {

	@Getter
	private final Map<Class<?>, ModelConfig<?>> visitedModels = new HashMap<>();
	
	public boolean contains(Class<?> clazz) {
		if(getVisitedModels()==null) return false;
		
		return getVisitedModels().containsKey(clazz);
	}
	
	public void set(Class<?> clazz, ModelConfig<?> mConfig) {
		getVisitedModels().put(clazz, mConfig);
	}
	
	
	public ModelConfig<?> get(Class<?> clazz) {
		return getVisitedModels().get(clazz);
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
