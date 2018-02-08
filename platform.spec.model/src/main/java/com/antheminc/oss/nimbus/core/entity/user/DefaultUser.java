/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.user;

import java.util.Set;

import com.antheminc.oss.nimbus.core.entity.access.DefaultAccessEntity;
import com.antheminc.oss.nimbus.core.entity.access.DefaultRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public class DefaultUser extends AbstractUser<DefaultRole> {

	private static final long serialVersionUID = 1L;
	
}
