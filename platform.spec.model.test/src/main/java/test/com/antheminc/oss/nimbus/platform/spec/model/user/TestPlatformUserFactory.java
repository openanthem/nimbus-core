/**
 * 
 */
package test.com.antheminc.oss.nimbus.platform.spec.model.user;

import com.antheminc.oss.nimbus.core.entity.user.DefaultUser;

/**
 * @author Soham Chakravarti
 *
 */
public class TestPlatformUserFactory {
	//@Test
	public static DefaultUser createUser() {
		DefaultUser pu = new DefaultUser();
		pu.setLoginId("batman");
		pu.setEmail("batman@gotham.com");
		return pu;
	}
	
	//@Test
	public static DefaultUser createUser2() {
		DefaultUser pu = new DefaultUser();
		pu.setLoginId("John Doe");
		pu.setEmail("john.doe@anthem.com");
		return pu;
	}
	
	public static DefaultUser createUser3() {
		DefaultUser pu = new DefaultUser();
		pu.setLoginId("Freidoon Ghazi");
		pu.setEmail("ghazi@anthem.com");
		return pu;
	}
	
	public static DefaultUser createUser4() {
		DefaultUser pu = new DefaultUser();
		pu.setLoginId("Linda Walker");
		pu.setEmail("walker@anthem.com");
		return pu;
	}
	
}
