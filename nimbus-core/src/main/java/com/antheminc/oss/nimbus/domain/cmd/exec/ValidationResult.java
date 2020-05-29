/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.cmd.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;
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
	public final CollectionsTemplate<List<ValidationError>, ValidationError> template = new CollectionsTemplate<>(
			this::getErrors, this::setErrors, ArrayList::new);
	
	
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
		if(CollectionUtils.isEmpty(violations)) {
			return;
		}
		
		for(ConstraintViolation<T> c : violations) {
			ValidationError.Model e = new ValidationError.Model();
			e.setCode(c.getPropertyPath().toString());
			e.setModelAlias(null);
			e.setMsg(c.getMessage());
			
			this.template.add(e);
		}
	}
	
}
