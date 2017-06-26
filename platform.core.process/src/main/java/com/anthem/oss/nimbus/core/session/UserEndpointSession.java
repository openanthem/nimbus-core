/**
 * 
 */
package com.anthem.oss.nimbus.core.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.client.user.TestClientUserFactory;

/**
 * @author Jayant Chaudhuri Platform Session provides the ability for platform
 *         to access session variables. Platform Session can lookup any existing
 *         HTTPSession in the thread and access session variables. For non HTTP
 *         requests, platform session provides the ability to load a session by
 *         session id and associate the session with the current thread
 *
 */
public class UserEndpointSession implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PLATFORM_CONTEXT_KEY = "platform-context";

	private static Map<String, Object> sessionMap = new HashMap<>();

	private static ClientUser clientUser;
	
	private static String sessionUserKey = "client-user-key";

	static {
		sessionMap.put(PLATFORM_CONTEXT_KEY, new SessionContext());
	}

	void loadSession(String sessionId) {

	}

	public static void clearSession() {
		sessionMap.put(PLATFORM_CONTEXT_KEY, new SessionContext());
	}

	@SuppressWarnings("unchecked")
	public static <R> R getAttribute(String key) {
		return (R) getPlatformContext().getAttribute(key);
	}

	public static void setAttribute(String key, Object value) {
		getPlatformContext().setAttribute(key, value);
	}

	public static <R> R getAttribute(Command cmd) {
		System.out.println("Root domain URI: " + cmd.getRootDomainUri());
		return getAttribute(cmd.getRootDomainUri());
	}

	public static void setAttribute(Command cmd, Object value) {
		setAttribute(cmd.getRootDomainUri(), value);
	}

	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public static <R> R getOrThrowEx(Command cmd) {
		return getOrThrowEx(cmd.getRootDomainUri());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private static <R> R getOrThrowEx(String key) {
		R value = getAttribute(key);
		if (value != null)
			return value;

		throw new FrameworkRuntimeException("Required value not found with provided key: " + key);
	}

	/**
	 * 
	 * @return
	 */
	private static SessionContext getPlatformContext() {
		SessionContext pContext = getPlatfromContextFromSession();

		if (pContext == null) {
			pContext = createPlatformContext();
		}
		return pContext;
	}

	/**
	 * 
	 * @return
	 */
	private static SessionContext getPlatfromContextFromSession() {
		return (SessionContext) sessionMap.get(PLATFORM_CONTEXT_KEY);
		//
		// RequestAttributes requestAttributes =
		// RequestContextHolder.getRequestAttributes();
		// if(requestAttributes != null){
		// return
		// (PlatformContext)requestAttributes.getAttribute(PLATFORM_CONTEXT_KEY,
		// RequestAttributes.SCOPE_SESSION);
		// }
		// return null;
	}

	/**
	 * 
	 * @return
	 */
	private static SessionContext createPlatformContext() {
		SessionContext pContext = new SessionContext();
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			requestAttributes.setAttribute(PLATFORM_CONTEXT_KEY, pContext, RequestAttributes.SCOPE_SESSION);
		}
		return pContext;
	}

	public ClientUser getLoggedInUser() {
		if(getAttribute(sessionUserKey) instanceof ClientUser) {
			return getAttribute(sessionUserKey);
		} 		
		return null;
	}

	public static ClientUser getStaticLoggedInUser() {
		if (clientUser == null) {
			if (getAttribute(sessionUserKey) instanceof ClientUser) {
				return getAttribute(sessionUserKey);
			}
		}
		return clientUser;
	}

}