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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.labelstate.core.Sample_Core_Label_Entity;
import com.antheminc.oss.nimbus.test.scenarios.labelstate.core.Sample_Core_Label_Entity.Nested_Attr;
import com.antheminc.oss.nimbus.test.scenarios.labelstate.core.Sample_Core_Label_Entity.Nested_Attr_Level2;
import com.antheminc.oss.nimbus.test.scenarios.labelstate.view.Sample_View_Label_Entity;
import com.antheminc.oss.nimbus.test.scenarios.labelstate.view.Sample_View_Label_Entity.Sample_View_Nested_Label;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 * @author Swetha Vemuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabelStateOnLoadEventHandlerTest extends AbstractFrameworkIntegrationTests {
	
	public static String CORE_ROOT = PLATFORM_ROOT + "/sample_core_label";
	public static String VIEW_ROOT = PLATFORM_ROOT + "/sample_view_label";
	public static String VIEW_ROOT_2 = PLATFORM_ROOT + "/invalid_multiple_samelocale";
	public static String VIEW_ROOT_3 = PLATFORM_ROOT + "/invalid_min_text";
	
	public static final Long CORE_REF_ID_1 = new Long(1);
	
	private Object prepareParam(String viewRoot) {
		Sample_Core_Label_Entity sc_main = new Sample_Core_Label_Entity();
		sc_main.setId(CORE_REF_ID_1);
		sc_main.setAttr1("Green");
		mongo.insert(sc_main, "sample_core_label");
		
		Object sv_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(viewRoot)
				.addRefId(CORE_REF_ID_1)
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		
		return sv_newResp;
	}
	
	/**
	 * @Label("Test Label A")
	 * private String label_a_en;
	 */
	@Test
	public void t00_label_state_single() {		
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Set<LabelState> label_a_en = view_param.findParamByPath("/label_a_en").getLabels();
		assertNotNull(label_a_en);
		Param<?> label_a_p = view_param.findParamByPath("/label_a_en");
		assertNotNull(label_a_p.getConfig().getLabels());
		assertFalse(label_a_p.getConfig().getLabels().isEmpty());
		assertEquals(1,label_a_p.getConfig().getLabels().size());
		assertEquals("Test Label A",getLabelState(label_a_p, Locale.getDefault()).getText());
	}
	
	/**
	 * @Label(value="Test Label C in English", helpText="some tooltip text here C")
	 * @Label(value="Test Label A in French", localeLanguageTag="fr")
	 * private String label_c_multiple;
	 */
	@Test
	public void t01_label_state_multiple() {
		Object sv_newResp = prepareParam(VIEW_ROOT);		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Param<?> multiple_label_param = view_param.findParamByPath("/label_c_multiple");
		Set<LabelState> label_c_multiple = multiple_label_param.getLabels();
		assertNotNull(label_c_multiple);
		assertFalse(label_c_multiple.isEmpty());
		assertEquals(2,label_c_multiple.size());
		assertEquals("Test Label C in English", getLabelState(multiple_label_param, Locale.getDefault()).getText());
		assertEquals("some tooltip text here C", getLabelState(multiple_label_param, Locale.getDefault()).getHelpText());
		assertEquals("Test Label A in French", getLabelState(multiple_label_param, Locale.FRENCH).getText());
		assertNull(getLabelState(multiple_label_param, Locale.FRENCH).getHelpText());
		
		assertNotNull(multiple_label_param.getConfig().getLabels());
		assertFalse(multiple_label_param.getConfig().getLabels().isEmpty());
		assertEquals(2,multiple_label_param.getConfig().getLabels().size());
	}
	
	/**
	 * private String label_empty;
	 */
	@Test
	public void t03_empty_label() {		
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Param<?> empty_label_param = view_param.findParamByPath("/label_empty");
		assertNull(empty_label_param.getLabels());
		assertNull(empty_label_param.getConfig().getLabels());
	}
	
	/**
	 * @Label(value="Test Label B in French", localeLanguageTag="fr", helpText="some tooltip text here B")
	 * private String label_b_fr;
	 */
	@Test
	public void t04_label_state_fr() {
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Param<?> label_fr_p = view_param.findParamByPath("/label_b_fr");
		assertNotNull(label_fr_p.getLabels());
		assertEquals(1, label_fr_p.getLabels().size());
		Set<LabelState> label_a_fr = label_fr_p.getLabels();
		assertNotNull(label_a_fr);
		assertNotNull(label_fr_p.getConfig().getLabels());
		assertFalse(label_fr_p.getConfig().getLabels().isEmpty());
		assertEquals(1,label_fr_p.getConfig().getLabels().size());
		assertEquals("Test Label B in French",getLabelState(label_fr_p, Locale.FRENCH).getText());
		assertEquals("some tooltip text here B", getLabelState(label_fr_p, Locale.FRENCH).getHelpText());
		
	}
	
	/**
	 * @Label("Test Grid Label")
	 * private List<Sample_View_Nested_Label> label_nested_coll_1;
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void t05_label_nested_coll_single_nostate() {
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Param<?> nested_coll_p = view_param.findParamByPath("/label_nested_coll_1");
		assertNotNull(nested_coll_p);
		assertNotNull(nested_coll_p.getLabels());
		assertEquals(1, nested_coll_p.getLabels().size());
		
		assertEquals("Test Grid Label", getLabelState(nested_coll_p, Locale.getDefault()).getText());
		/*Elem labels*/
		assertNotNull(nested_coll_p.findIfCollection().getElemLabels());
		assertEquals(2,nested_coll_p.findIfCollection().getElemLabels().size());
		
		ParamConfig<?> elemConfig_attr1 = getElemConfigByCode(nested_coll_p, "label_nested_attr1");
		ParamConfig<?> elemConfig_attr2 = getElemConfigByCode(nested_coll_p, "label_nested_attr2");
		assertNotNull(nested_coll_p.findIfCollection().getElemLabels().get(Optional.ofNullable(elemConfig_attr1.getId()).orElse(null)));
		assertNotNull(nested_coll_p.findIfCollection().getElemLabels().get(Optional.ofNullable(elemConfig_attr2.getId()).orElse(null)));
		assertTrue(nested_coll_p.findIfCollection().getElemLabels().containsKey(elemConfig_attr1.getId()));
		assertTrue(nested_coll_p.findIfCollection().getElemLabels().containsKey(elemConfig_attr2.getId()));
		assertFalse(nested_coll_p.findIfCollection().getElemLabels().containsKey(nested_coll_p.getConfigId()));
		assertEquals("Test Label Nested attr1", getElemLabelState(nested_coll_p, Locale.getDefault(), elemConfig_attr1.getId()).getText());
		assertEquals("Test Label Nested attr2", getElemLabelState(nested_coll_p, Locale.getDefault(), elemConfig_attr2.getId()).getText());
		assertEquals("some nested attt2 help text", getElemLabelState(nested_coll_p, Locale.getDefault(), elemConfig_attr2.getId()).getHelpText());
		assertEquals("Test Label Nested attr2 fr", getElemLabelState(nested_coll_p, Locale.FRENCH, elemConfig_attr2.getId()).getText());
			
		Sample_View_Nested_Label nested_1 = new Sample_View_Nested_Label();
		nested_1.setLabel_nested_attr1("nested attr1");
		Sample_View_Nested_Label nested_2 = new Sample_View_Nested_Label();
		nested_2.setLabel_nested_attr1("nested attr2");
		
		List<Sample_View_Nested_Label> nested_coll_attr = new ArrayList<>();
		nested_coll_attr.add(nested_1);
		nested_coll_attr.add(nested_2);
		
		view_param.findParamByPath("/label_nested_coll_1").setState(nested_coll_attr);
		Object newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId(CORE_REF_ID_1)
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		
		assertNotNull(newResp);	
		Param<Sample_View_Label_Entity> updated_param = ExtractResponseOutputUtils.extractOutput(newResp, 0);
		
		Param<?> updated_nested_coll_p = updated_param.findParamByPath("/label_nested_coll_1");
		assertNotNull(updated_nested_coll_p);
		assertNotNull(updated_nested_coll_p.getLabels());
		assertEquals(1, updated_nested_coll_p.getLabels().size());
		
		assertEquals("Test Grid Label", getLabelState(updated_nested_coll_p, Locale.getDefault()).getText());
		assertNotNull(updated_nested_coll_p.findParamByPath("/0"));
	}
	
	/**
	 * @Label("Test label coll en")
	 * @Label(value="Test label coll fr",localeLanguageTag="fr")
	 * private List<Nested_Attr> nested_coll_attr2;
	 */
	@Test
	public void t06_label_nested_coll_multiple_withstate() {
		Sample_Core_Label_Entity sc_main = new Sample_Core_Label_Entity();
		sc_main.setId(new Long(2));
		Nested_Attr nested_1 = new Nested_Attr();
		nested_1.setAttr_nested_1("nested attr1");
		Nested_Attr_Level2 attr_level2 = new Nested_Attr_Level2();
		attr_level2.setAttr_nested_level_2("nested level 2 attr");
		nested_1.setAttr_nested_2(attr_level2);
		
		Nested_Attr nested_2 = new Nested_Attr();
		nested_2.setAttr_nested_1("nested attr2");
		Nested_Attr_Level2 nested_2_attr_level21 = new Nested_Attr_Level2();
		nested_2_attr_level21.setAttr_nested_level_2("nested 2 level 2 attr");
		nested_1.setAttr_nested_2(nested_2_attr_level21);
		
		List<Nested_Attr> nested_coll_attr = new ArrayList<>();
		nested_coll_attr.add(nested_1);
		nested_coll_attr.add(nested_2);
		sc_main.setNested_coll_attr2(nested_coll_attr);
		
		mongo.insert(sc_main, "sample_core_label");
		
		Object sv_resp = controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_ROOT)
				.addRefId(new Long(2))
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		Param<Sample_Core_Label_Entity> core_param = ExtractResponseOutputUtils.extractOutput(sv_resp, 0);
		assertNotNull(core_param);
		Param<List<Nested_Attr>> nested_coll_p = core_param.findParamByPath("/nested_coll_attr2");
		assertNotNull(nested_coll_p);
		assertNotNull(nested_coll_p.getState());
		assertEquals(2, nested_coll_p.getState().size());
		assertEquals(2, nested_coll_p.getLabels().size());
		assertEquals("Test label coll en", getLabelState(nested_coll_p,Locale.getDefault()).getText());
		assertEquals("Test label coll fr", getLabelState(nested_coll_p,Locale.FRENCH).getText());
		
		assertNotNull(nested_coll_p.findParamByPath("/0"));
		assertNotNull(nested_coll_p.findParamByPath("/0").getState());
		assertNull(nested_coll_p.findParamByPath("/0").getLabels());
		
		assertNotNull(nested_coll_p.findParamByPath("/1"));
		assertNotNull(nested_coll_p.findParamByPath("/1").getState());
		assertNull(nested_coll_p.findParamByPath("/1").getLabels());	
		
		/*Elem labels*/
		assertNotNull(nested_coll_p.findIfCollection().getElemLabels());
		assertEquals(2,nested_coll_p.findIfCollection().getElemLabels().size());
		ParamConfig<?> elemConfig_attr1 = getElemConfigByCode(nested_coll_p, "attr_nested_1");
		ParamConfig<?> elemConfig_attr2 = getElemConfigByCode(nested_coll_p, "attr_nested_2");
		
		assertNotNull(nested_coll_p.findIfCollection().getElemLabels().get(Optional.ofNullable(elemConfig_attr1.getId()).orElse(null)));
		assertTrue(nested_coll_p.findIfCollection().getElemLabels().containsKey(elemConfig_attr1.getId()));
		assertEquals("Test label nested attr", getElemLabelState(nested_coll_p, Locale.getDefault(), elemConfig_attr1.getId()).getText());

		assertNotNull(nested_coll_p.findIfCollection().getElemLabels().get(Optional.ofNullable(elemConfig_attr2.getId()).orElse(null)));
		assertTrue(nested_coll_p.findIfCollection().getElemLabels().containsKey(elemConfig_attr2.getId()));
		assertEquals("Test label nested level 2", getElemLabelState(nested_coll_p, Locale.getDefault(), elemConfig_attr2.getId()).getText());
		
	}
	
	@Test
	public void t07_label_state_nested_attr() {
		Sample_Core_Label_Entity sc_main = new Sample_Core_Label_Entity();
		sc_main.setId(new Long(3));
		Nested_Attr nested_1 = new Nested_Attr();
		nested_1.setAttr_nested_1("nested attr1");
		sc_main.setAttr_nested(nested_1);
		
		mongo.insert(sc_main, "sample_core_label");
		
		Object sv_resp = controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_ROOT)
				.addRefId(new Long(3))
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		Param<Sample_Core_Label_Entity> core_param = ExtractResponseOutputUtils.extractOutput(sv_resp, 0);
		assertNotNull(core_param);
		Param<?> nested_param = core_param.findParamByPath("/attr_nested");
		Param<?> nested_attr_param = nested_param.findParamByPath("/attr_nested_1");
		assertEquals("Test label nested en", getLabelState(nested_param, Locale.getDefault()).getText());
		assertEquals("Test label nested attr", getLabelState(nested_attr_param, Locale.getDefault()).getText());
	}
	
	@Test
	public void t08_manual_setLabel() {
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Param<?> label_a_p = view_param.findParamByPath("/label_empty");
		assertNull(label_a_p.getLabels());
		Set<LabelState> labels = new HashSet<>();
		LabelState l = new LabelState();
		l.setText("Set label text");
		l.setHelpText("Set label helpText");
		labels.add(l);
		label_a_p.setLabels(labels);
		
		assertNotNull(label_a_p.getLabels());
		assertEquals(1,label_a_p.getLabels().size());
		assertEquals("Set label text", getLabelState(label_a_p,Locale.getDefault()).getText());
		assertEquals("Set label helpText", getLabelState(label_a_p,Locale.getDefault()).getHelpText());
		assertEquals(Locale.getDefault().toLanguageTag(), getLabelState(label_a_p,Locale.getDefault()).getLocale());
	}
	
	/**
	 * Manually setting param.setLabel() in code will overwrite existing labels
	 */
	@Test
	public void t09_manual_setLabel_samelocale() {
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Param<?> label_a_p = view_param.findParamByPath("/label_a_en");
		assertNotNull(label_a_p.getLabels());
		assertNotNull(label_a_p.getConfig().getLabels());
		assertFalse(label_a_p.getConfig().getLabels().isEmpty());
		assertEquals(1,label_a_p.getLabels().size());
		assertEquals("Test Label A",getLabelState(label_a_p, Locale.getDefault()).getText());
		Set<LabelState> labels = new HashSet<>();
		LabelState l = new LabelState();
		l.setText("Set label text");
		l.setHelpText("Set label helpText");
		labels.add(l);
		label_a_p.setLabels(labels);
		
		assertNotNull(label_a_p.getLabels());
		assertEquals(1,label_a_p.getLabels().size());
		assertEquals("Set label text", getLabelState(label_a_p,Locale.getDefault()).getText());
		assertEquals("Set label helpText", getLabelState(label_a_p,Locale.getDefault()).getHelpText());
		assertEquals(Locale.getDefault().toLanguageTag(), getLabelState(label_a_p,Locale.getDefault()).getLocale());
	}
	
	@Test(expected=FrameworkRuntimeException.class)
	public void t10_manual_multiple_setLabel_samelocale() {
		Sample_Core_Label_Entity sc_main = new Sample_Core_Label_Entity();
		sc_main.setId(new Long(4));
		sc_main.setAttr2("test");
		
		mongo.insert(sc_main, "sample_core_label");
		
		Object sv_resp = controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_ROOT)
				.addRefId(new Long(4))
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		Param<Sample_Core_Label_Entity> core_param = ExtractResponseOutputUtils.extractOutput(sv_resp, 0);
		assertNotNull(core_param);
		Param<?> label_a_p = core_param.findParamByPath("/attr2");
		assertNotNull(label_a_p.getLabels());
		assertNotNull(label_a_p.getConfig().getLabels());
		assertFalse(label_a_p.getConfig().getLabels().isEmpty());
		assertEquals(1,label_a_p.getLabels().size());
		assertEquals("Test Label en",getLabelState(label_a_p, Locale.getDefault()).getText());
		Set<LabelState> labels = new HashSet<>();
		LabelState l = new LabelState();
		l.setText("Set label text");
		l.setHelpText("Set label helpText");
		labels.add(l);
		LabelState l2 = new LabelState();
		l2.setText("Set label text 2");
		l2.setHelpText("Set label helpText");
		labels.add(l2);
		label_a_p.setLabels(labels);
	}
	
	/**
	 * Adding duplicate labels with same local and text will result in adding only 1 label,
	 * since the equals method in LabelState checks for unique combination of text and locale. 
	 */
	@Test
	public void t11_manual_duplicatetext_setLabel_samelocale() {
		
		Sample_Core_Label_Entity sc_main = new Sample_Core_Label_Entity();
		sc_main.setId(new Long(5));
		sc_main.setAttr2("test");
		
		mongo.insert(sc_main, "sample_core_label");
		
		Object sv_resp = controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_ROOT)
				.addRefId(new Long(5))
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		Param<Sample_Core_Label_Entity> core_param = ExtractResponseOutputUtils.extractOutput(sv_resp, 0);
		assertNotNull(core_param);
		Param<?> label_a_p = core_param.findParamByPath("/attr2");
		assertNotNull(label_a_p.getLabels());
		assertNotNull(label_a_p.getConfig().getLabels());
		assertFalse(label_a_p.getConfig().getLabels().isEmpty());
		assertEquals(1,label_a_p.getLabels().size());
		assertEquals("Test Label en",getLabelState(label_a_p, Locale.getDefault()).getText());
		Set<LabelState> labels = new HashSet<>();
		LabelState l = new LabelState();
		l.setText("Set label text");
		l.setHelpText("Set label helpText");
		labels.add(l);
		LabelState l2 = new LabelState();
		l2.setText("Set label text");
		l2.setHelpText("Set label helpText");
		labels.add(l2);
		label_a_p.setLabels(labels);
		
		assertNotNull(label_a_p.getLabels());
		assertEquals(1,label_a_p.getLabels().size());
	}
	
	/**
	 * @Label(value = "Test Label D in English", style = @Style(cssClass=" foo bar "))
	 * private String label_d_styles;
	 */
	@Test
	public void t12_styleConfig() {
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		Param<?> p = view_param.findParamByPath("/label_d_styles");
		assertEquals("Test Label D in English", getLabelState(p, Locale.getDefault()).getText());
		assertNull(getLabelState(p, Locale.getDefault()).getHelpText());
		assertEquals("foo bar", getLabelState(p, Locale.getDefault()).getCssClass());
	}
	
	/**
	 * Testing invalid config scenario where multiple Label entries are found for the same locale - only 1 is allowed
	 * Only the first label annotation executed gets added to the param labels
	 */
	@Test
	public void t11_multiple_same_locale() {
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		Param<?> p = view_param.findParamByPath("/same_locale_multiple");
		assertNotNull(p.getLabels());
		assertEquals(1, p.getLabels().size());
	}
	
	@Model @Getter @Setter
	@Domain("invalid_min_text") @Type(Sample_Core_Label_Entity.class)
	private static class TestInvalidScenario_MinOneTextAndHelpText {
		
		@Label
		private String atleast_one_text_or_help_text;
	}
	
	/**
	 * Testing invalid config scenario where multiple Label entries are found for the same locale - only 1 is allowed
	 */
	@Test(expected=InvalidConfigException.class)
	public void t12_ex_multiple_same_locale() {
		prepareParam(VIEW_ROOT_3);
	}
	
	/**
	 * @Label("This label color is <!../label_replace!>")
	 * private String label_dynamic_a;
	 */
	@Test
	public void t12_label_dynamic() {		
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Set<LabelState> label_a_en = view_param.findParamByPath("/label_dynamic_a").getLabels();
		assertNotNull(label_a_en);
		Param<?> label_a_p = view_param.findParamByPath("/label_dynamic_a");
		assertNotNull(label_a_p.getConfig().getLabels());
		assertFalse(label_a_p.getConfig().getLabels().isEmpty());
		assertEquals(1,label_a_p.getConfig().getLabels().size());
		assertEquals("This label color is Green",getLabelState(label_a_p, Locale.getDefault()).getText());
		assertEquals("some help text in Green",getLabelState(label_a_p, Locale.getDefault()).getHelpText());
	}
	
	/**
	 * @Label("This label color is <!../invalid_param!>")
	 * private String label_dynamic_negative;
	 */
	@Test
	public void t13_label_dynamic_invalid() {		
		Object sv_newResp = prepareParam(VIEW_ROOT);
		
		assertNotNull(sv_newResp);	
		Param<Sample_View_Label_Entity> view_param = ExtractResponseOutputUtils.extractOutput(sv_newResp, 0);
		assertNotNull(view_param);
		Set<LabelState> label_a_en = view_param.findParamByPath("/label_dynamic_negative").getLabels();
		assertNotNull(label_a_en);
		Param<?> label_a_p = view_param.findParamByPath("/label_dynamic_negative");
		assertNotNull(label_a_p.getConfig().getLabels());
		assertFalse(label_a_p.getConfig().getLabels().isEmpty());
		assertEquals(1,label_a_p.getConfig().getLabels().size());
		assertEquals("This label color is null",getLabelState(label_a_p, Locale.getDefault()).getText());
	}
	
	@Test
	public void t14_multiple_nested_coll_withstate() {}
	
	@Test
	public void t14_multiple_nested_coll_withoutstate() {}
	
	private static LabelState getLabelState(Param<?> p, Locale expectedLocale) {	
		if(CollectionUtils.isNotEmpty(p.getLabels())) {
			return p.getLabels().stream()
					.filter(lc->lc.getLocale().equals(expectedLocale.toLanguageTag()))
					.findFirst()
					.orElseGet(()->{
						fail();
						return null;
					});
		}else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	private static LabelState getElemLabelState(Param<?> p, Locale expectedLocale, String configId) {	
		Map<String, Set<LabelState>> elemLabels = p.findIfCollection().getElemLabels();
		
		if(CollectionUtils.isNotEmpty(elemLabels.entrySet())) {
		return	elemLabels.entrySet().stream()
					.filter(el -> el.getKey().equals(configId))
					.findFirst().get().getValue()
						.stream().filter(lc->lc.getLocale().equals(expectedLocale.toLanguageTag()))
						.findFirst()
						.orElse(null);
		}else
			return null;
	}

	private ParamConfig<?> getElemConfigByCode(Param<?> coll_param, String code) {
		ParamConfig<?> p = coll_param.getConfig().getType().findIfCollection().getElementConfig();
		ParamConfig<?> elemConfig = p.getType().findIfNested().getModelConfig().getParamConfigs().stream()
			.filter(pc -> pc.getCode().equalsIgnoreCase(code))
			.findFirst()
			.orElseGet(()->{
				fail();
				return null;
			});
		return elemConfig;		
	}

}
