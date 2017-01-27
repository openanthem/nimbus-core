/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Soham Chakravarti
 *
 */
public class PrefixValidator implements ConstraintValidator<Prefix, String>{

	private Prefix prefix;
	

	@Override
	public void initialize(Prefix prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext ctx) {
		String expected = prefix.value();
		return StringUtils.startsWith(value, expected);
	}
	
}
