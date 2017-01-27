/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.validation.ValidatorProvider;

/**
 * @author Rakesh Patel
 *
 */
@Component
public class DefaultValidatorProvider implements ValidatorProvider {

	public static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    
    public Validator getValidator() {
    	return factory.getValidator();
    }
} 
