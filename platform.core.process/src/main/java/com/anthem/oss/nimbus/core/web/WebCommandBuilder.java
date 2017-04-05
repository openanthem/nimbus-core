/**
 * 
 */
package com.anthem.oss.nimbus.core.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
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
