/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;

/**
 * @author Soham.Chakravarti
 *
 */
public abstract class AbstractDBSearchByQuery extends AbstractDBSearch {

	private static final ScriptEngine groovyEngine = new ScriptEngineManager().getEngineByName("groovy");

	/**
	 * @param beanResolver
	 */
	public AbstractDBSearchByQuery(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@SuppressWarnings("unchecked")
	protected <T> T evaluate(Class<?> referredClass, String alias, String criteria) {
		if(StringUtils.isBlank(criteria)) 
			return null;
		
		try {
			EntityPath<?> qInstance = SimpleEntityPathResolver.INSTANCE.createPath(referredClass);
			
			Bindings b = groovyEngine.createBindings();
			b.put(alias, qInstance);
			
			return (T)groovyEngine.eval(criteria, b);
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Cannot instantiate queryDsl class for entity: "+referredClass+ " "
					+ "please make sure the entity has been annotated with either @Domain or @Model and a Q Class has been generated for it", ex);	
		}
	}
	
	protected PathBuilder<?>[] buildProjectionPathBuilder(Class<?> referredClass, SearchCriteria<?> criteria) {
		List<PathBuilder<?>> paths = new ArrayList<>();
		if(criteria.getProjectCriteria() != null && !MapUtils.isEmpty(criteria.getProjectCriteria().getMapsTo())) {
			Collection<String> fields = criteria.getProjectCriteria().getMapsTo().values();
			fields.forEach((f)->paths.add(new PathBuilder<>(referredClass, f)));
			return paths.toArray(new PathBuilder[paths.size()]);
		}
		return paths.toArray(new PathBuilder[paths.size()]);
	}
}
