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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.EMPTY;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.pojo.ClassLoadUtils;

import lombok.Getter;

/**
 * @author Rakesh Patel
 * @author Tony Lopez
 *
 */
@Getter
public class DefaultParamValuesHandler implements ParamValuesOnLoadHandler {

	private final JustLogit logIt = new JustLogit(DefaultParamValuesHandler.class);

	protected final CommandPathVariableResolver pathVariableResolver;
	protected final CommandExecutorGateway gateway;
	private final CacheManager cacheManager;

	public DefaultParamValuesHandler(BeanResolverStrategy beanResolver) {
		this.pathVariableResolver = beanResolver.get(CommandPathVariableResolver.class);
		this.gateway = beanResolver.get(CommandExecutorGateway.class);
		this.cacheManager = beanResolver.get(CacheManager.class);
	}

	@Override
	public void onStateLoad(Values configuredAnnotation, Param<?> param) {
		List<ParamValue> result = buildParamValues(configuredAnnotation, param, param);
		if (result != null)
			param.setValues(result);
		else
			logIt.warn(() -> "param values lookup returned null for param " + param + " with config "
					+ configuredAnnotation);
	}

	@Override
	public List<ParamValue> buildParamValues(Values values, Param<?> srcParam, Param<?> targetParam) {
		if (values == null) {
			return null;
		}

		if (values.value() != EMPTY.class) {
			return fromType(values, targetParam);
		} else {
			return fromCommandURL(values, srcParam);
		}
	}

	protected List<ParamValue> fromType(Values values, Param<?> targetParam) {
		Source srcValues = ClassLoadUtils.newInstance(values.value());
		return srcValues.getValues(targetParam.getConfig().getCode());
	}

	@SuppressWarnings("unchecked")
	protected List<ParamValue> fromCommandURL(Values values, Param<?> srcParam) {
		String valuesUrl = getPathVariableResolver().resolve(srcParam, values.url());
		valuesUrl = srcParam.getRootExecution().getRootCommand().getRelativeUri(valuesUrl);
		Command cmd = CommandBuilder.withUri(valuesUrl).getCommand();
		cmd.setAction(Action._search);

		// retrieve staticCodeValue lookup searches from cache if it
		// exists, otherwise continue
		if (cmd.getRootDomainAlias().equals(Constants.PARAM_VALUES_DOMAIN_ALIAS.code)
				&& values.useParamValuesCacheOnLoad()) {
			List<ParamValue> cachedValue = getCachedParamValue(cmd);
			if (null != cachedValue) {
				return cachedValue;
			}
		}

		MultiOutput multiOp = getGateway().execute(new CommandMessage(cmd, null));
		return (List<ParamValue>) multiOp.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	protected List<ParamValue> getCachedParamValue(Command cmd) {
		if (null == this.cacheManager) {
			logIt.warn(() -> "Attempted to retrieve a cached value via command: " + cmd
					+ ", but the configured CacheManager is null.");
			return null;
		}
		Cache cache = this.cacheManager.getCache(Constants.PARAM_VALUES_CACHE_KEY.code);
		if (null == cache) {
			return null;
		}
		ValueWrapper cacheValue = cache.get(cmd.getFirstParameterValue("where"));
		if (null == cacheValue) {
			return null;
		}
		return (List<ParamValue>) cacheValue.get();
	}
}
