/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.sa;


/**
 * @author Soham Chakravarti
 *
 */
public interface ServiceActivatorDefinition {

	public String getServiceName();
	public String getServiceMethod();
	public String getRequestHandler();
	public String getResponseHandler();
}
