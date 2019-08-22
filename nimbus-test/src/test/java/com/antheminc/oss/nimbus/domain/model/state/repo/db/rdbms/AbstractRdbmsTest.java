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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.test.domain.support.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tony Lopez
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RdbmsRepositoryTestApplication.class)
@ActiveProfiles("rdbms-test")
public abstract class AbstractRdbmsTest {

	public static final String CLIENT_ID = "hooli";
	public static final String PLATFORM_ROOT = "/"+CLIENT_ID+"/thebox/p";
	public static final String CORE_ALIAS = "sample_jpa_core";
	public static final String VIEW_ALIAS = "sample_jpa_view";
	public static final String CORE_DOMAIN_ROOT = PLATFORM_ROOT + "/" + CORE_ALIAS;
	public static final String VIEW_DOMAIN_ROOT = PLATFORM_ROOT + "/" + VIEW_ALIAS;
	
	@Autowired
	protected DefaultJpaModelRepository repo;
	
	@Autowired
	@PersistenceContext
	protected EntityManager em;
	
	@Autowired 
	protected WebActionController controller;
	
	@Autowired
	protected CommandExecutorGateway commandGateway;
	
	@Autowired 
	protected ObjectMapper om;
	
	protected JsonUtils jsonUtils = JsonUtils.get();	
}
