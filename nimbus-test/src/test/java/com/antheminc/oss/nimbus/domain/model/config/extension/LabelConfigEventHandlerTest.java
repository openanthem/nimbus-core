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
package com.antheminc.oss.nimbus.domain.model.config.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.LabelConfig;
import com.antheminc.oss.nimbus.domain.model.config.builder.EntityConfigVisitor;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.DefaultEntityConfigBuilder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabelConfigEventHandlerTest extends AbstractFrameworkIntegrationTests {

	@Autowired DomainConfigBuilder domainConfigBuilder;
	
	@Autowired DefaultEntityConfigBuilder entityConfigBuilder;
	
	ModelConfig<?> mConfig;
	
	@Before
	public void before() {
		mConfig = domainConfigBuilder.getModel(SampleCoreEntity.class);
	}
	
	/**
	 * @Label("Test Label A")
	 * private String label_a_en;
	 */
	@Test
	public void t00_default_en() {
		ParamConfig<String> p = mConfig.findParamByPath("/label_a_en");
		
		assertNotNull(p.getLabelConfigs());
		assertSame(1, p.getLabelConfigs().size());
		
		assertEquals(Locale.getDefault().toLanguageTag(), p.getLabelConfigs().get(0).getLocale());
		assertEquals("Test Label A", p.getLabelConfigs().get(0).getText());
		assertNull(p.getLabelConfigs().get(0).getHelpText());
	}
	
	/**
	 * @Label(value="Test Label B in French", localeLanguageTag="fr", helpText="some tooltip text here B")
	 * private String label_b_fr;
	 */
	@Test
	public void t02_fr() {
		ParamConfig<String> p = mConfig.findParamByPath("/label_b_fr");
		
		assertNotNull(p.getLabelConfigs());
		assertSame(1, p.getLabelConfigs().size());
		
		assertEquals(Locale.FRENCH.toLanguageTag(), p.getLabelConfigs().get(0).getLocale());
		assertEquals("Test Label B in French", p.getLabelConfigs().get(0).getText());
		assertEquals("some tooltip text here B", p.getLabelConfigs().get(0).getHelpText());
	}
	
	/**
	 * @Label(value="Test Label C in English", helpText="some tooltip text here C")
	 * @Label(value="Test Label A in French", localeLanguageTag="fr")
	 * private String label_c_multiple;
	 */
	@Test
	public void t03_multiple() {
		ParamConfig<String> p = mConfig.findParamByPath("/label_c_multiple");
		
		assertNotNull(p.getLabelConfigs());
		assertSame(2, p.getLabelConfigs().size());
		
		assertEquals("Test Label C in English", getLabelConfig(p, Locale.getDefault()).getText());
		assertEquals("some tooltip text here C", getLabelConfig(p, Locale.getDefault()).getHelpText());
		
		assertEquals("Test Label A in French", getLabelConfig(p, Locale.FRENCH).getText());
		assertNull(getLabelConfig(p, Locale.FRENCH).getHelpText());
	}
	
	private static LabelConfig getLabelConfig(ParamConfig<?> p, Locale expectedLocale) {
		return p.getLabelConfigs().stream()
				.filter(lc->lc.getLocale().equals(expectedLocale.toLanguageTag()))
				.findFirst()
				.orElseGet(()->{
					fail();
					return null;
				});
	}
	
	
	@Model @Getter @Setter
	public static class TestInvalidScenario_MultipleSameLocaleEntity {
		
		@Label("T1")
		@Label("T2")
		private String same_locale_multiple;
	}
	
	/**
	 * Testing invalid config scenario where multiple Label entries are found for the same locale - only 1 is allowed
	 */
	@Test(expected=InvalidConfigException.class)
	public void t04_ex_multiple_same_locale() {
		entityConfigBuilder.buildModel(TestInvalidScenario_MultipleSameLocaleEntity.class, new EntityConfigVisitor());
	}
	
	@Model @Getter @Setter
	private static class TestInvalidScenario_MinOneTextAndHelpText {
		
		@Label
		private String atleast_one_text_or_help_text;
	}
	
	/**
	 * Testing invalid config scenario where multiple Label entries are found for the same locale - only 1 is allowed
	 */
	@Test(expected=InvalidConfigException.class)
	public void t05_ex_multiple_same_locale() {
		entityConfigBuilder.buildModel(TestInvalidScenario_MinOneTextAndHelpText.class, new EntityConfigVisitor());
	}
}
