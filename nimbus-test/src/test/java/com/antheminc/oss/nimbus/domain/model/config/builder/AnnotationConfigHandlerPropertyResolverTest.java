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
/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Image;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Initialize;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.DefaultEntityConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleExprEvalEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnnotationConfigHandlerPropertyResolverTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired 
	DefaultEntityConfigBuilder entityConfigBuilder;
	
	@Autowired 
	CommandPathVariableResolver cmdPathResolver;
	
	@Autowired
	QuadModelBuilder quadBuilder;
	
	ModelConfig<SampleExprEvalEntity> mConfig;
	
	@Value(SampleExprEvalEntity.K_LINK_URL)
	String expected_url_link;
	
	@Value(SampleExprEvalEntity.K_IMAGE_URL)
	String expected_url_image;
	
	@Value(SampleExprEvalEntity.K_INITIALIZE_URL)
	String expected_url_initialize;
	
	@Value("${test.url.code}")
	String expected_env_code;
	
	public static final String K_PARAM_REF_VALUE = "pass value p1";
	
	
	@Before
	public void before() {
		mConfig = entityConfigBuilder.load(SampleExprEvalEntity.class, new EntityConfigVisitor());
	}
	
	@Test
	public void t01_link() {
		AnnotationConfig acLink = mConfig.findParamByPath("/link").getUiStyles();
		assertNotNull(acLink);
		assertEquals(Link.class, acLink.getAnnotation().annotationType());

		assertNotNull(expected_url_link);
		assertEquals(expected_url_link, acLink.getAttributes().get("url"));
	}
	
	@Test
	public void t02_image() {
		AnnotationConfig acImage = mConfig.findParamByPath("/image").getUiStyles();
		assertNotNull(acImage);
		assertEquals(Image.class, acImage.getAnnotation().annotationType());

		assertNotNull(expected_url_image);
		assertEquals(expected_url_image, acImage.getAttributes().get("imgSrc"));
		assertEquals(expected_url_link, acImage.getAttributes().get("alias"));
	}
	
	@Test
	public void t03_multiple_image_initialize() {
		AnnotationConfig acImage = mConfig.findParamByPath("/initialize").getUiStyles();
		assertNotNull(acImage);
		assertEquals(Image.class, acImage.getAnnotation().annotationType());

		assertNotNull(expected_url_image);
		assertEquals(expected_url_image, acImage.getAttributes().get("imgSrc"));
		assertEquals(expected_url_link, acImage.getAttributes().get("alias"));
		
		List<AnnotationConfig> acList = mConfig.findParamByPath("/initialize").getUiNatures();
		assertNotNull(acList);
		assertEquals(1, acList.size());
		
		AnnotationConfig acInitialize = acList.get(0);
		assertNotNull(acInitialize);
		assertEquals(Initialize.class, acInitialize.getAnnotation().annotationType());
		
		assertEquals(expected_url_initialize, acInitialize.getAttributes().get("alias"));
	}
	
	@Test
	public void t04_config() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_expr/_new").getCommand();
		
		QuadModel<?, ?> q = quadBuilder.build(cmd);
		assertNotNull(q);
		
		q.getCore().findParamByPath("/p1_val").setState(K_PARAM_REF_VALUE);
		
		Param<?> triggerParam = q.getCore().findParamByPath("/trigger");
		assertNotNull(triggerParam);
		
		String pathToResolve = ((Config)triggerParam.getConfig().getExecutionConfig().get().get(0)).url();
		
		String resolvedPath = cmdPathResolver.resolve(triggerParam, pathToResolve);
		assertEquals("/p/_anotherdomain_"+expected_env_code+"/p1/"+K_PARAM_REF_VALUE, resolvedPath);
	}
}
