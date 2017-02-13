/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.util.CollectionUtils;

import com.anthem.nimbus.platform.spec.model.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data
public class ValidationResult {

	private List<ValidationError> errors;
	
	@JsonIgnore
	public final transient CollectionsTemplate<List<ValidationError>, ValidationError> template = new CollectionsTemplate<>(
			() -> getErrors(), (e) -> setErrors(e), () -> new ArrayList<>());
	
	
	public ValidationResult(){ }
	
	public<T> ValidationResult(Set<ConstraintViolation<T>> violations) {
		add(violations);
	}
	
	
	/**
	 * 
	 * @param violations
	 */
	public <T> void addValidationErrors(Set<ConstraintViolation<T>> violations) {
		add(violations);
	}
	
	/**
	 * 
	 * @param violations
	 */
	private <T> void add(Set<ConstraintViolation<T>> violations) {
		if(CollectionUtils.isEmpty(violations)) return;
		
		for(ConstraintViolation<T> c : violations) {
			ValidationError.Model e = new ValidationError.Model();
			e.setCode(c.getPropertyPath().toString());
			e.setModelAlias(null);
			e.setMsg(c.getMessage());
			
			this.template.add(e);
		}
	}
	
}
