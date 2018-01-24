/**
 *  Copyright 2016-2018 the original author or authors.
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
package test.com.anthem.nimbus.platform.spec.model.user;

import com.antheminc.oss.nimbus.entity.user.DefaultUser;

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
