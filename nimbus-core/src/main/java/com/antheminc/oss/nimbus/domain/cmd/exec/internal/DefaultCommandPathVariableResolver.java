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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.ParamPathExpressionParser;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
@Getter(value=AccessLevel.PROTECTED)
public class DefaultCommandPathVariableResolver implements CommandPathVariableResolver {

	protected final JustLogit logit = new JustLogit(DefaultCommandPathVariableResolver.class);
	
	private static final String NULL_STRING = "null";
	private static final String NULL_STRING_REGEX = "\\s*\"null\"\\s*";
	private static final Pattern NULL_STRING_PATTERN = Pattern.compile(NULL_STRING_REGEX);
	
	private final CommandMessageConverter converter;
	private final PropertyResolver propertyResolver;
	private final SessionProvider sessionProvider;
	private final Environment environment;
	private final CommandExecutorGateway executorGateway;
	
	public DefaultCommandPathVariableResolver(BeanResolverStrategy beanResolver, PropertyResolver propertyResolver) {
		this.converter = beanResolver.get(CommandMessageConverter.class);
		this.propertyResolver = propertyResolver;
		this.sessionProvider = beanResolver.get(SessionProvider.class);
		this.environment = beanResolver.get(Environment.class);
		this.executorGateway = beanResolver.find(CommandExecutorGateway.class);
	}
	
	
	@Override
	public String resolve(Param<?> param, String urlToResolve) {
		if(StringUtils.trimToNull(urlToResolve)==null)
			return urlToResolve;
		
		// resolve property place-holders first
		try {
			String resolvedPlaceHolders = getPropertyResolver().resolveRequiredPlaceholders(urlToResolve);
			return resolveInternal(param, resolvedPlaceHolders);
		} catch (RuntimeException ex) {
			throw new InvalidConfigException("Failed to resolve with property place-holders for param: "+param+" with url: "+urlToResolve, ex);
		}
	}
	
	
	protected String resolveInternal(Param<?> param, String urlToResolve) {
		Map<Integer, String> entries = ParamPathExpressionParser.parse(urlToResolve);
		if(MapUtils.isEmpty(entries))
			return urlToResolve;
		
		String out = urlToResolve;
		for(Integer i : entries.keySet()) {
			String key = entries.get(i);
			
			// look for relative path to passed in param's parent model
			String pathToResolve = ParamPathExpressionParser.stripPrefixSuffix(key);
			
			String val = map(param, pathToResolve);
			
			out = StringUtils.replace(out, key, val, 1);
		}
		
		Matcher m = NULL_STRING_PATTERN.matcher(out);
		out = m.replaceAll(NULL_STRING); // replaces all json="null" (including leading/trailing spaces) to json=null
		return out;
	}
	
	protected String map(Param<?> param, String pathToResolve) {
		// handle recursive
		if(ParamPathExpressionParser.containsPrefixSuffix(pathToResolve)) {
			String recursedPath = resolve(param, pathToResolve);
			pathToResolve = recursedPath; 
		}
			
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_SESSION_SELF.code))
			return mapSelf(param, pathToResolve);
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_ENV.code))
			return mapEnvironment(param, pathToResolve);
		
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_COMMAND_PARAM_CURRENT_SELF.code))
			return StringUtils.removeStart(param.getPath(), param.getRootDomain().getPath());
		
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_REF_ID.code))
			return String.valueOf(param.getRootExecution().getRootCommand().getRefId(Type.DomainAlias));

		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_ELEM_ID.code)) 
			return mapColElem(param, pathToResolve);
		
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.SEGMENT_PLATFORM_MARKER.code))
			return String.valueOf(mapCrossDomain(param, pathToResolve));
		
		return mapQuad(param, pathToResolve);
	}
	
	//TODO bean path evaluation to get value
	protected String mapSelf(Param<?> param, String pathToResolve) {
		if(StringUtils.endsWith(pathToResolve, "loginId"))
			return Optional.ofNullable(getSessionProvider().getLoggedInUser()).orElseGet(() -> new ClientUser()).getLoginId();
		if(StringUtils.endsWith(pathToResolve, "id")) {
			Long id = Optional.ofNullable(getSessionProvider().getLoggedInUser()).orElseGet(() -> new ClientUser()).getId();
			return String.valueOf(id);
		}
		
		return param.getRootExecution().getRootCommand().getElementSafely(Type.ClientAlias).getAlias();
	}
	
	protected String mapEnvironment(Param<?> param, String pathToResolve) {
		String property = pathToResolve.replace(Constants.MARKER_ENV.code+".", "");
		return environment.getProperty(property);
	}
	
	protected String mapQuad(Param<?> param, String pathToResolve) {
		if(StringUtils.startsWith(pathToResolve, "json(")) {
			String paramPath = StringUtils.substringBetween(pathToResolve, "json(", ")");
			Object state;
			if (StringUtils.startsWithIgnoreCase(paramPath, Constants.SEGMENT_PLATFORM_MARKER.code)) {
				state = mapCrossDomain(param, paramPath);
			} else {
				Param<?> p = param.findParamByPath(paramPath) != null? param.findParamByPath(paramPath): param.getParentModel().findParamByPath(paramPath);
				if(p == null) {
					logit.error(() -> new StringBuffer().append(" Param (using paramPath) ").append(paramPath).append(" not found from param reference: ").append(param).toString());
					return NULL_STRING;
				}				
				state = p.getLeafState();
			}
			String json = getConverter().toJson(state);
			return String.valueOf(json);
		} else {
			Param<?> p = param.findParamByPath(pathToResolve) != null? param.findParamByPath(pathToResolve): param.getParentModel().findParamByPath(pathToResolve);
			if(p == null) {
				logit.error(() -> new StringBuffer().append(" Param (using paramPath) ").append(pathToResolve).append(" not found from param reference: ").append(param).toString());
				return NULL_STRING;
			}
			return String.valueOf(p.getState());
		}
	}

	protected String mapColElem(Param<?> commandParam, String pathToResolve) {
		// check if command param is colElem
		if(commandParam.isCollectionElem())
			return commandParam.findIfCollectionElem().getElemId();
		
		// otherwise, if mapped, check if mapsTo param is colElem
		if(commandParam.isMapped())
			return mapColElem(commandParam.findIfMapped().getMapsTo(), pathToResolve);
		
		// throw ex ..or.. blank??
		return "";
	}
	
	/**
	 * 
	 * @param commandParam
	 * @param pathToResolve
	 * @return
	 * @since 1.1.11
	 */
	protected Object mapCrossDomain(Param<?> commandParam, String pathToResolve) {
		Command rootCmd = commandParam.getRootExecution().getRootCommand();
		CommandBuilder cmdBuilder = CommandBuilder.withPlatformRelativePath(rootCmd, Type.AppAlias, pathToResolve);
		cmdBuilder.setAction(Action._get);
		cmdBuilder.setBehaviors(new LinkedList<>(Arrays.asList(Behavior.$state)));
		Command cmd = cmdBuilder.getCommand();
		CommandMessage cmdMsg = new CommandMessage(cmd, null);		
		MultiOutput output = executorGateway.execute(cmdMsg);
		Object response = output.getSingleResult();
		
		if (response == null) {
			logit.error(() -> new StringBuffer().append(" Param (using paramPath) [").append(pathToResolve).append("] not found from param: ").append(commandParam).toString());
			return null;
		}
		
		logit.debug(() -> new StringBuffer().append(" Param (using paramPath) [").append(pathToResolve).append("] has been resolved to ").append(response).toString());
		return response;
	}
}
