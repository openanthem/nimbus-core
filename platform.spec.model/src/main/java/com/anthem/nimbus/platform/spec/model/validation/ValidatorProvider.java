/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.validation;

import javax.validation.Validator;

/**
 * @author Rakesh Patel
 *
 */
public interface ValidatorProvider {

	public Validator getValidator();
	
}
