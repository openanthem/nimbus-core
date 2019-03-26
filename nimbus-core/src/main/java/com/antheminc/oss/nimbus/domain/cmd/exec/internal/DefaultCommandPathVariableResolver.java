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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
@Getter(value=AccessLevel.PROTECTED)
public class DefaultCommandPathVariableResolver implements CommandPathVariableResolver {

	public class ElemIdResolver extends Resolver {
			
		public ElemIdResolver() {
			super(Constants.MARKER_ELEM_ID.code);
		}

		@Override
		public String resolve(Param<?> param, String pathToResolve) {
			return mapColElem(param, pathToResolve);
		}
		
		protected String mapColElem(Param<?> param, String pathToResolve) {
			// check if command param is colElem
			if(param.isCollectionElem())
				return param.findIfCollectionElem().getElemId();
			
			// otherwise, if mapped, check if mapsTo param is colElem
			if(param.isMapped())
				return mapColElem(param.findIfMapped().getMapsTo(), pathToResolve);
			
			// throw ex ..or.. blank??
			return "";
		}
	}
	
	public class PassthroughResolver extends Resolver {

		public PassthroughResolver(String keyword) {
			super(keyword);
		}

		@Override
		String resolve(Param<?> param, String pathToResolve) {
			return Constants.MARKER_PLATFROM_EXPR_PREFIX.code + pathToResolve + Constants.MARKER_PLATFROM_EXPR_SUFFIX.code;
		}
	}
	
	public class EnvResolver extends Resolver {

		public EnvResolver() {
			super(Constants.MARKER_ENV.code);
		}
		
		@Override
		public String resolve(Param<?> param, String pathToResolve) {
			String property = pathToResolve.replace(Constants.MARKER_ENV.code+".", "");
			return environment.getProperty(property);
		}
	}
	
	public class PlatformMarkerResolver extends Resolver {
		
		public PlatformMarkerResolver() {
			super(Constants.SEGMENT_PLATFORM_MARKER.code);
		}
		
		@Override
		public String resolve(Param<?> param, String pathToResolve) {
			return String.valueOf(mapCrossDomain(param, pathToResolve));
		}
	}
	
	public class RefIdResolver extends Resolver {
		
		public RefIdResolver() {
			super(Constants.MARKER_REF_ID.code);
		}
		
		@Override
		public String resolve(Param<?> param, String pathToResolve) {
			return String.valueOf(param.getRootExecution().getRootCommand().getRefId(Type.DomainAlias));
		}
	}
	
	@RequiredArgsConstructor
	public abstract class Resolver {
		
		@Getter
		private final String keyword;
		
		abstract String resolve(Param<?> param, String pathToResolve);
		
		protected boolean shouldApply(String pathToResolve) {
			return StringUtils.startsWithIgnoreCase(pathToResolve, this.keyword);
		}
	}
	
	public class SelfResolver extends Resolver {

		public SelfResolver() {
			super(Constants.MARKER_SESSION_SELF.code);
		}
		
		@Override
		public String resolve(Param<?> param, String pathToResolve) {
			if(StringUtils.endsWith(pathToResolve, "loginId"))
				return Optional.ofNullable(getSessionProvider().getLoggedInUser()).orElseGet(() -> new ClientUser()).getLoginId();
			if(StringUtils.endsWith(pathToResolve, "id")) {
				Long id = Optional.ofNullable(getSessionProvider().getLoggedInUser()).orElseGet(() -> new ClientUser()).getId();
				return String.valueOf(id);
			}
			
			return param.getRootExecution().getRootCommand().getElementSafely(Type.ClientAlias).getAlias();
		}
	}
	
	public class ThisResolver extends Resolver {

		public ThisResolver() {
			super(Constants.MARKER_COMMAND_PARAM_CURRENT_SELF.code);
		}
		
		@Override
		public String resolve(Param<?> param, String pathToResolve) {
			return StringUtils.removeStart(param.getPath(), param.getRootDomain().getPath());
		}
	}

	private static final String NULL_STRING = "null";
	private static final String NULL_STRING_REGEX = "\\s*\"null\"\\s*";
	private static final Pattern NULL_STRING_PATTERN = Pattern.compile(NULL_STRING_REGEX);
	
	private final CommandMessageConverter converter;
	private final Environment environment;
	private final CommandExecutorGateway executorGateway;
	private final PropertyResolver propertyResolver;
	private final SessionProvider sessionProvider;
	private final ReservedKeywordRegistry reservedKeywordRegistry;
	private final List<Resolver> resolvers = new ArrayList<>();
	
	protected final JustLogit logit = new JustLogit(DefaultCommandPathVariableResolver.class);
	
	public DefaultCommandPathVariableResolver(BeanResolverStrategy beanResolver, PropertyResolver propertyResolver) {
		this.converter = beanResolver.get(CommandMessageConverter.class);
		this.propertyResolver = propertyResolver;
		this.sessionProvider = beanResolver.get(SessionProvider.class);
		this.environment = beanResolver.get(Environment.class);
		this.executorGateway = beanResolver.find(CommandExecutorGateway.class);
		this.reservedKeywordRegistry = beanResolver.find(ReservedKeywordRegistry.class);
		
		registerDefaultResolvers();
	}
	
	public void registerDefaultResolvers() {
		registerResolver(new SelfResolver());
		registerResolver(new EnvResolver());
		registerResolver(new ThisResolver());
		registerResolver(new RefIdResolver());
		registerResolver(new ElemIdResolver());
		registerResolver(new PlatformMarkerResolver());
		registerResolver(new PassthroughResolver(Constants.MARKER_COL_PARAM.code));
	}
	
	public void registerResolver(Resolver resolver) {
		this.reservedKeywordRegistry.register(resolver.getKeyword());
		this.resolvers.add(resolver);
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

	protected String map(Param<?> param, String pathToResolve) {
		// handle recursive
		if(ParamPathExpressionParser.containsPrefixSuffix(pathToResolve)) {
			String recursedPath = resolve(param, pathToResolve);
			pathToResolve = recursedPath; 
		}
		
		for(Resolver resolver: resolvers) {
			if (resolver.shouldApply(pathToResolve)) {
				return resolver.resolve(param, pathToResolve);
			}
		}
		
		return mapQuad(param, pathToResolve);
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
}
