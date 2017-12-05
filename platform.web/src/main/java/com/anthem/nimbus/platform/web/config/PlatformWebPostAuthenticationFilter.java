/**
 * 
 */
package com.anthem.nimbus.platform.web.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.command.CommandMessage;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.antheminc.oss.nimbus.core.session.UserEndpointSession;
import com.antheminc.oss.nimbus.core.util.JustLogit;

/**
 * @author Swetha Vemuri
 *
 */
@ConfigurationProperties(prefix="session")
public class PlatformWebPostAuthenticationFilter extends OncePerRequestFilter {
	
	private static String userKey = "client-user-key";
	
	private JustLogit logit = new JustLogit(this.getClass());
	
	public PlatformWebPostAuthenticationFilter(RedisOperationsSessionRepository redisOperation) {
		super();
		this.redisOperation = redisOperation;
	}

	private RedisOperationsSessionRepository redisOperation;
	
	@Autowired
	@Qualifier("default.processGateway")
	CommandExecutorGateway commandGateway;
	
	@Value("${platform.config.cookies.gateway.name}")
	private String gatewayCookieName;
	

	public CommandExecutorGateway getCommandGateway() {
		return commandGateway;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		/*if(UserEndpointSession.getAttribute(userKey) == null){
			final String principal = "swetha";
			logit.debug(() -> "Security context principal :: "+principal);
			logit.trace(() -> "Begin to convert authentication principal to clientuser object");
			String searchUri = "/Anthem/fep/p/clientuser/_search?fn=query&where=clientuser.loginId.eq('"+principal+"')";
			Command cmd = CommandBuilder.withUri(searchUri).getCommand();
			cmd.setAction(Action._search);
			
			CommandMessage cmdMsg = new CommandMessage();
			cmdMsg.setCommand(cmd);
			MultiOutput multiOp = getCommandGateway().execute(cmdMsg);
			List<Output<?>> ops  = multiOp.getOutputs();
			List<?> values = (List<?>)ops.get(0).getValue();
			if(values!= null && values.size() > 0) {
				logit.debug(() -> "Authenticated clientuser : "+values.get(0));				
				UserEndpointSession.setAttribute(userKey, values.get(0));
				logit.debug(() -> "setting userendpointsession with authenticated user -> key: "+userKey+" value: "+values.get(0));
			}			
		}*/
		String reqCookie = request.getHeader("Cookie");
		Map<String, String> cookieMap = new HashMap<>();
		if(!StringUtils.isBlank(reqCookie)) {
			String[] rawCookieParams = StringUtils.split(reqCookie,";");
			for (String rawCookieNameAndValue : rawCookieParams) {
				String[] rawCookieNameAndValuePair = StringUtils.split(StringUtils.strip(rawCookieNameAndValue), "=");
				if(rawCookieNameAndValuePair!=null && rawCookieNameAndValuePair.length>1){
					cookieMap.put(rawCookieNameAndValuePair[0], rawCookieNameAndValuePair[1]);
					logit.debug(() -> "Session cookie map :: "+cookieMap);
				}
			}
		}
		
		String sessionid = cookieMap.get(this.gatewayCookieName);
		logit.trace(() -> "session id : "+sessionid);
		if (sessionid != null) {
			if (redisOperation != null) {
				Session session = redisOperation.getSession(sessionid);
				if (session != null) {
					final Object obj = session
							.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
					if (obj instanceof SecurityContext) {
						final SecurityContext context = (SecurityContext) obj;
						final Authentication authentication = context.getAuthentication();
						if (authentication != null) {
							SecurityContextHolder.getContext().setAuthentication(authentication);
							final String principal = (String)authentication.getPrincipal();
							if((UserEndpointSession.getStaticLoggedInUser() != null && 
									!StringUtils.equals(UserEndpointSession.getStaticLoggedInUser().getLoginId(), principal)) || UserEndpointSession.getStaticLoggedInUser() == null) {
								
								logit.debug(() -> "Security context principal :: "+principal);
								logit.trace(() -> "Begin to convert authentication principal to clientuser object");
								String searchUri = "Anthem/fep/icr/p/clientuser/_search?fn=query&where=clientuser.loginId.eq('"+principal+"')";
								Command cmd = CommandBuilder.withUri(searchUri).getCommand();
								cmd.setAction(Action._search);
								
								CommandMessage cmdMsg = new CommandMessage();
								cmdMsg.setCommand(cmd);
								MultiOutput multiOp = getCommandGateway().execute(cmdMsg);
								List<Output<?>> ops  = multiOp.getOutputs();
								List<?> values = (List<?>)ops.get(0).getValue();
								if(values != null && values.size() >0 ){
									logit.debug(() -> "Authenticated clientuser : "+values.get(0));
									
									UserEndpointSession.setAttribute(userKey, values.get(0));
									logit.debug(() -> "setting userendpointsession with authenticated user -> key: "+userKey+" value: "+values.get(0));
								} else 
									logit.debug(() -> "Platform clientUser not found for userId :"+principal+". Is the user registered in platform ?");
							}							
							
						}
					}
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}