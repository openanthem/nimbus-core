package test.com.anthem.oss.nimbus.core.domain.model;

import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class SampleCoreNestedEntity extends IdString {
	private static final long serialVersionUID = 1L;
	
	private String nested_attr_String;
}
