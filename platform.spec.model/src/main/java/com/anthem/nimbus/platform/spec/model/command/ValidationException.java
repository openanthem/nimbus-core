/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.command;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
public class ValidationException extends PlatformRuntimeException {

	private static final long serialVersionUID = 1L;

	
	@Getter private final ValidationResult validationResult;
	
	
	public ValidationException(Set<ConstraintViolation<Object>> violations) {
		this.validationResult = new ValidationResult(violations);
	}

	public ValidationException(Set<ConstraintViolation<Object>> violations, String message) {
		super(message);
		this.validationResult = new ValidationResult(violations);
	}
	
	public ValidationException(ValidationResult result) {
		this.validationResult = result;
	}
	
	public ValidationException(ValidationResult result, String message) {
		super(message);
		this.validationResult = result;
	}
	
}
