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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.Script;
import com.antheminc.oss.nimbus.domain.defn.extension.Script.Type;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class ScriptEventHandler extends EvalExprWithCrudActions<Script> {

	private ExpressionEvaluator expressionEvaluator;
	
	private static final ScriptEngine groovyEngine = new ScriptEngineManager().getEngineByName("groovy");
	
	private JustLogit logit = new JustLogit(getClass());
	
	public ScriptEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.expressionEvaluator = beanResolver.get(ExpressionEvaluator.class);
	}
	
	@Override
	protected void handleInternal(Param<?> param, Script configuredAnnotation) {
		
		String value = Optional.of(configuredAnnotation.value())
				.map(StringUtils::trimToNull)
				.orElseThrow(()->new InvalidConfigException("Script text must not be empty declared on param: "+param));

		// TODO: strategy pattern
		try {
			if(configuredAnnotation.type() == Type.SPEL_INLINE) {
				handleSpelInline(value, param);
				
			} else if(configuredAnnotation.type() == Type.SPEL_FILE) {
				handleSpelFile(value, param);
				
			} else if(configuredAnnotation.type() == Type.GROOVY) {
				handleGroovyFile(value, param);
				
			}
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to execute script: "+configuredAnnotation+" declared on param: "+param, ex);
		}
	}
	
	protected void handleSpelInline(String expr, Param<?> param) {
		getExpressionEvaluator().getValue(expr, new ParamStateHolder<>(param), Object.class);
	}
	
	protected void handleSpelFile(String path, Param<?> param) {
		String expr = readResourceAsString(path, param);
		handleSpelInline(expr, param);
	}
	
	protected void handleGroovyFile(String path, Param<?> param) throws ScriptException {
		String script = readResourceAsString(path, param);
		Bindings b = groovyEngine.createBindings();
		
		b.put("param", param);
		
		groovyEngine.eval(script, b);
	}

	protected synchronized String readResourceAsString(String path, Param<?> param) {
		Resource r = findResource(path, param);
		try{
			InputStream is = r.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			StringBuilder sb = new StringBuilder();
			
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			} 
			br.close();
			
			return sb.toString();
	        	
		} catch(Exception ex) {
			throw new InvalidConfigException("Script file: "+r.getDescription()+" could not be read.", ex);
		}
	}
	
	protected Resource findResource(String path, Param<?> param) {
		Resource r = beanResolver.getResource(path);
		
		if(r==null || !r.exists())
			throw new InvalidConfigException("Script resource path not found at: "+path+" configured on param: "+param);
		
		return r;
	}
	

}
