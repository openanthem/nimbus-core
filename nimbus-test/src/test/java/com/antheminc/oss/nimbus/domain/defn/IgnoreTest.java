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
package com.antheminc.oss.nimbus.domain.defn;

import static com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType.persistence;
import static com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType.websocket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JsonContent;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.entity.AbstractEntity;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IgnoreTest extends AbstractFrameworkIntegrationTests {

	@Autowired 
	DomainConfigBuilder domainBuilder;
	
	@Autowired 
	QuadModelBuilder quadBuilder;
	
	private ModelConfig<?> mConfig;
	private QuadModel<?, ?> q;
	
	@Domain(value="ignoreTest", includeListeners={persistence, websocket})
	@Repo(value=Database.rep_mongodb)
	@Getter @Setter
	public static class _TestModel extends AbstractEntity.IdString {
		
		private static final long serialVersionUID = 1L;

		@Ignore
		private String ignoreEntirely;
		
		@Ignore(listeners=websocket)
		private String ignoreOnlyWebSocket;
		
		@Ignore(listeners={websocket, persistence})
		private String ignoreWebSocketAndDb;
		
		@Ignore(listeners=persistence)
		private String ignoreOnlyDb;
		
	}
	
	private static final String K_URI_VR = PLATFORM_ROOT + "/ignoreTest";
	
	@Before
	public void before() {
		super.before();
		
		domainBuilder.handleClass(_TestModel.class);
		
		this.mConfig = domainBuilder.getModel(_TestModel.class);
		Assert.assertNotNull(mConfig);
		
		Command cmd = CommandBuilder.withUri(K_URI_VR+"/_new").getCommand();
		q = quadBuilder.build(cmd);
	}
	
	@Test
	public void t00_init() {
		assertNull(mConfig.findParamByPath("/ignoreEntirely"));
		assertNotNull(mConfig.findParamByPath("/ignoreOnlyWebSocket"));
		assertNotNull(mConfig.findParamByPath("/ignoreWebSocketAndDb"));
		assertNotNull(mConfig.findParamByPath("/ignoreOnlyDb"));
	}
	
	@Test
	public void t01_ignore_conditions() throws Exception {
		List<AnnotationConfig> extensions = mConfig.findParamByPath("/ignoreOnlyWebSocket").getExtensions();
		assertNotNull(extensions);
		assertEquals(1, extensions.size());
		
		assertNotNull(extensions.get(0));
		assertEquals(Ignore.class, extensions.get(0).getAnnotation().annotationType());
		assertEquals(websocket, ((Ignore)extensions.get(0).getAnnotation()).listeners()[0]);
		
		Param<?> coreParam = q.getCore().getAssociatedParam();
		JsonContent<Object> jContent = json.write(coreParam);
		
		System.out.println(jContent.getJson());
		
		assertThat(jContent).doesNotHaveJsonPathValue("$.config.type.modelConfig.paramConfigs[?(@.code == 'ignoreOnlyWebSocket')]");
	}
}
