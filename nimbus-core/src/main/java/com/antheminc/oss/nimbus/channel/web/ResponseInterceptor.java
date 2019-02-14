/**
 * 
 */
package com.antheminc.oss.nimbus.channel.web;

/**
 * @author Rakesh Patel
 *
 */
public interface ResponseInterceptor<I> {

	public boolean intercept(I responseBody);
	
}
