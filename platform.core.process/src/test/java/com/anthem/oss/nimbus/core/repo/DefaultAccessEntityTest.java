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
package com.anthem.oss.nimbus.core.repo;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;

import test.com.anthem.nimbus.platform.spec.model.access.AccessEntityFactory;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultAccessEntityTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	MongoOperations mongoOps;
	
	
	@Test
	public void test_createStaticAccesses() {
		DefaultAccessEntity staticAccessess = AccessEntityFactory.createPlatformAndSubTree1();
		mongoOps.save(staticAccessess, "defaultAccessEntity");
	}
	
}
