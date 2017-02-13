/**
 * 
 */
package com.anthem.nimbus.platform.utils.converter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.oss.nimbus.core.domain.Command;
import com.anthem.oss.nimbus.core.domain.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class WebCommandBuilder {

	private JustLogit logit = new JustLogit(this.getClass());
	
	public Command build(HttpServletRequest request) {
		logit.trace(()->"Received http request. "+request.getMethod()+" URI: "+request.getRequestURI());
		
		return handleInternal(request.getRequestURI(), request.getParameterMap());
	}
	
	public Command build(HttpServletRequest request, ModelEvent<String> event) {
		String uri = request.getRequestURI();
		logit.info(()->"Received http request. "+request.getMethod()+" URI: "+uri+" with event: "+event);
		
		final String constructedUri;
		if(event==null) {
			constructedUri = request.getRequestURI();
		} else {
			String clientUri = StringUtils.substringBefore(uri, Constants.SEPARATOR_URI_PLATFORM.code+Constants.SEPARATOR_URI.code); //separator = /p/
			constructedUri = clientUri + Constants.SEPARATOR_URI_PLATFORM.code + event.getPath() + Constants.SEPARATOR_URI.code +event.getType();
			
			logit.info(()->"Constructed URI: "+constructedUri);
		}
		
		
		Command cmd = handleInternal(constructedUri, request.getParameterMap());
		return cmd;
	}
	
	public Command handleInternal(String uri, Map<String, String[]> rParams) {
		Command cmd = CommandBuilder.withUri(uri).addParams(rParams).getCommand();
		return cmd;
	} 
	

	
}
