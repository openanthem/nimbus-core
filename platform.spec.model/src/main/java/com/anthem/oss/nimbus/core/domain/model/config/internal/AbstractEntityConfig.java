/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.EntityConfig;
import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;
import com.anthem.oss.nimbus.core.util.JustLogit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
abstract public class AbstractEntityConfig<T> implements EntityConfig<T> {

	@JsonIgnore final protected JustLogit logit = new JustLogit(getClass());

	private AnnotationConfig uiStyles;

	@JsonIgnore private RulesConfig rulesConfig; 

}
