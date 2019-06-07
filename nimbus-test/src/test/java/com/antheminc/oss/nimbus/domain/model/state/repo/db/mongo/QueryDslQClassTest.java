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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.antheminc.oss.nimbus.entity.user.QUserRole;
import com.antheminc.oss.nimbus.entity.user.UserRole;
import com.querydsl.core.types.EntityPath;

/**
 * @author Soham Chakravarti
 *
 */
public class QueryDslQClassTest {

	@Test
	public void test_QClass_lookup() {
		EntityPath<UserRole> qClassActual = SimpleEntityPathResolver.INSTANCE.createPath(UserRole.class);
		assertEquals(QUserRole.userRole, qClassActual);
	}
}
