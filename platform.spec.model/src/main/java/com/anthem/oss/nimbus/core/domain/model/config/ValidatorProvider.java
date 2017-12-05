/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.config;

import javax.validation.Validator;

/**
 * @author Rakesh Patel
 *
 */
public interface ValidatorProvider {

	public Validator getValidator();
	
}
