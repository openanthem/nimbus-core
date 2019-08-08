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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.StaticCodeValue;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;


/**
 * @author Tony Lopez
 *
 */
public class DefaultParamValuesHandlerTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	private CacheManager cacheManager;
	
	@Before
	@Override
	public void before() {
		super.before();
		// load static code values
		mongo.insert(getSampleValues(), Constants.PARAM_VALUES_DOMAIN_ALIAS.code);
	}

	private StaticCodeValue getSampleValues() {
		StaticCodeValue foo = new StaticCodeValue("/foo", new ArrayList<>());
		foo.setId(1L);
		foo.getParamValues().add(new ParamValue());
		foo.getParamValues().get(0).setCode("bar");
		foo.getParamValues().get(0).setLabel("Bar");
		return foo;
	}
	
	@Test
	public void testCommandDSLRetrieval() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();
		Object response = controller.handleGet(request, null);
		
		Param<String> viewRoot = ParamUtils.extractResponseByParamPath(response, "/sample_view");
		Param<String> actual = viewRoot.findParamByPath("/page_green/tile/view_sample_form/comboBoxStaticCodeValuesLookup");
		Assert.assertNotNull(actual.getValues());
		Assert.assertTrue(CollectionUtils.isEqualCollection(getSampleValues().getParamValues(), actual.getValues()));
	}
	
	@Test
	public void testCacheRetrieval() {
		// populate the cache with the expected content
		List<ParamValue> expected = new ArrayList<>();
		expected.add(new ParamValue());
		expected.get(0).setCode("baz");
		expected.get(0).setLabel("Baz");
		cacheManager.getCache(Constants.PARAM_VALUES_CACHE_KEY.code).put("staticCodeValue.paramCode.eq('/foo')", expected);
		
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();
		Object response = controller.handleGet(request, null);

		Param<String> viewRoot = ParamUtils.extractResponseByParamPath(response, "/sample_view");
		Param<String> actual = viewRoot.findParamByPath("/page_green/tile/view_sample_form/comboBoxStaticCodeValuesLookup"); 
		Assert.assertTrue(CollectionUtils.isEqualCollection(expected, actual.getValues()));
	}
}
