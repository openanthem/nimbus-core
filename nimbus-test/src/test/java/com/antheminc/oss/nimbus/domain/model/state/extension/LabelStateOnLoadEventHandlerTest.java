/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s7.core.S7C_CoreMain;
import com.antheminc.oss.nimbus.test.scenarios.s7.view.S7V_ViewMain;

/**
 * @author AC63348
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabelStateOnLoadEventHandlerTest extends AbstractFrameworkIntegrationTests {
	
	public static String VIEW_ROOT = PLATFORM_ROOT + "/s7v_main";
	
	public static final Long CORE_REF_ID_1 = new Long(1);
	
	@Autowired SessionProvider sessionProvider;
	
	@Test
	public void t00_label_state_single() {
		S7C_CoreMain s7c_main_1 = new S7C_CoreMain();
		s7c_main_1.setId(CORE_REF_ID_1);
		s7c_main_1.setAttr1_clone("test_2");
		mongo.insert(s7c_main_1, "s7c_main");
		
		Object s7v_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId(CORE_REF_ID_1)
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		
		assertNotNull(s7v_newResp);	
		Param<S7V_ViewMain> actual = ExtractResponseOutputUtils.extractOutput(s7v_newResp, 0);
		assertNotNull(actual);
		assertTrue(actual.isVisible());
		Set<Param.LabelState> actual_labels = actual.findParamByPath("/attr3").getLabels();
		assertNotNull(actual.findParamByPath("/attr3").getConfig().getLabels());		
		assertNotNull(actual_labels);
		Param.LabelState lc = new Param.LabelState();
		lc.setText("New label");
		Set<Param.LabelState> new_labels = new HashSet<>();
		new_labels.add(lc);
		actual.findParamByPath("/attr3").setLabels(new_labels);
		actual.findParamByPath("/attr3").setVisible(false);
	}
	
	/*@Test
	public void t01_labels_state_onload_multiple() {
		S7C_CoreMain s7c_main_1 = new S7C_CoreMain();
		s7c_main_1.setId(CORE_REF_ID_1);
		s7c_main_1.setAttr1_clone("test_2");
		mongo.insert(s7c_main_1, "s7c_main");
		
		Object s7v_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId(CORE_REF_ID_1)
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		assertNotNull(s7v_newResp);	
		Param<S7V_ViewMain> actual = ExtractResponseOutputUtils.extractOutput(s7v_newResp, 0);
		assertNotNull(actual);
		List<LabelState> actual_labels = actual.findParamByPath("/attr4").getLabels();
		assertNotNull(actual.findParamByPath("/attr4").getConfig().getLabels());		
		assertNotNull(actual_labels);
		assertEquals(2, actual_labels.size());
		assertEquals("French label", actual_labels.get(0).getText());
		assertEquals("Attr 5", actual_labels.get(1).getText());
		Param.LabelState lc = new Param.LabelState();
		lc.setText("New label");
		List<Param.LabelState> new_labels = new ArrayList<>();
		new_labels.add(lc);
		actual.setLabels(new_labels);
		
	}*/

}
