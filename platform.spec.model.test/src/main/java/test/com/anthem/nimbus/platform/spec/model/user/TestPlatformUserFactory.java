/**
 * 
 */
package test.com.anthem.nimbus.platform.spec.model.user;

import com.anthem.nimbus.platform.spec.model.user.PlatformUser;

/**
 * @author Soham Chakravarti
 *
 */
public class TestPlatformUserFactory {
	//@Test
	public static PlatformUser createUser() {
		PlatformUser pu = new PlatformUser();
		pu.setLoginId("batman");
		pu.setEmail("batman@gotham.com");
		return pu;
	}
	
	//@Test
	public static PlatformUser createUser2() {
		PlatformUser pu = new PlatformUser();
		pu.setLoginId("John Doe");
		pu.setEmail("john.doe@anthem.com");
		return pu;
	}
	
	public static PlatformUser createUser3() {
		PlatformUser pu = new PlatformUser();
		pu.setLoginId("Freidoon Ghazi");
		pu.setEmail("ghazi@anthem.com");
		return pu;
	}
	
	public static PlatformUser createUser4() {
		PlatformUser pu = new PlatformUser();
		pu.setLoginId("Linda Walker");
		pu.setEmail("walker@anthem.com");
		return pu;
	}
}
