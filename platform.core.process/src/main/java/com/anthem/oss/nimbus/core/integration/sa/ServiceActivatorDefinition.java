/**
 * 
 */
package com.anthem.oss.nimbus.core.integration.sa;


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
