/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.S4C_AnotherModel;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.S4C_CoreMain;
import com.antheminc.oss.nimbus.test.scenarios.s4.view.S4V_ViewMain.VForm;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S4_MappedTransientTest extends AbstractFrameworkIntegrationTests {
	
	private static final String K_URI_VR = PLATFORM_ROOT + "/s4v_main";
	
	
	private Object createNew_VR() {
		return controller.handleGet(
					MockHttpRequestBuilder.withUri(K_URI_VR).addAction(Action._new).getMock(), 
					null);
	}

	@Test
	public void t00_onLoad_check() {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");
		assertNotNull(pForm);
		assertTrue(pForm.isTransient());
		
		// check transient param is assigned to simulated detached
		assertFalse(pForm.findIfTransient().isAssinged());
		assertNotNull(pForm.findIfTransient().getMapsTo());
		
		// check cancel is enabled and delete is disabled
		assertTrue(pForm.findParamByPath("/cancelButton").isEnabled());
		assertFalse(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure no element has been added to core or view collections
		assertEquals(0, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(0, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
		
		// verify that db has no entries
		assertTrue(mongo.findAll(S4C_CoreMain.class, "s4c_main").isEmpty());
	}
	
	@Test
	public void t01_openForm_new_unassigned() throws Exception {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");
		
		// set value of param within form while its in un-assigned state
		final String K1 = "str1 val" + new Date();
		controller.handlePost(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/str1").addAction(Action._replace).getMock(), 
				json.write(K1).getJson());
		
		assertEquals(K1, pForm.findParamByPath("/str1").getLeafState());

		// check cancel is enabled and delete is disabled
		assertTrue(pForm.findParamByPath("/cancelButton").isEnabled());
		assertFalse(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure no element has been added to core or view collections
		assertEquals(0, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(0, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
		
		// verify that db has no entries
		assertTrue(mongo.findAll(S4C_CoreMain.class, "s4c_main").isEmpty());
		
	}
	
	@Test
	public void t02_openForm_new_unassigned_discard() throws Exception {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");
		
		// set value of param within form while its in un-assigned state
		final String K1 = "str1 val" + new Date();
		controller.handlePost(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/str1").addAction(Action._replace).getMock(), 
				json.write(K1).getJson());
		
		assertEquals(K1, pForm.findParamByPath("/str1").getLeafState());
		
		// hit cancel
		controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/cancelButton").addAction(Action._get).getMock(), 
				null);
		
		// check that the values are reset
		assertNull(pForm.findParamByPath("/str1").getLeafState());
		
		// check cancel is enabled and delete is disabled
		assertTrue(pForm.findParamByPath("/cancelButton").isEnabled());
		assertFalse(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure no element has been added to core or view collections
		assertEquals(0, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(0, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
		
		// verify that db has no entries
		assertTrue(mongo.findAll(S4C_CoreMain.class, "s4c_main").isEmpty());
		
	}
	
	@Test
	public void t03_openForm_save() throws Exception {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");
		
		// set value of param within form while its in un-assigned state
		final String K1 = "str1 val" + new Date();
		controller.handlePost(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/str1").addAction(Action._replace).getMock(), 
				json.write(K1).getJson());
		
		assertEquals(K1, pForm.findParamByPath("/str1").getLeafState());

		// save: create payload
		final String K2 = "str2 val" + new Date();
		VForm formEntity = new VForm();
		formEntity.setStr1(K1);
		formEntity.setStr2(K2);
		formEntity.setUnmappedAttr1("unmappedAttr1 val "+ new Date());
		
		// save: call
		controller.handlePost(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/saveButton").addAction(Action._get).getMock(), 
				json.write(formEntity).getJson());
		
		// check that the transient param is now assigend
		assertTrue(pForm.findIfTransient().isAssinged());
		
		// check cancel is DISABLED and delete is ENABLED
		assertFalse(pForm.findParamByPath("/cancelButton").isEnabled());
		assertTrue(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure no element has been added to core or view collections
		assertEquals(1, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(1, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
		
		// verify that db has 1 entry
		assertFalse(mongo.findAll(S4C_CoreMain.class, "s4c_main").isEmpty());
	}
	
	@Test
	public void t04_openForm_save_delete() throws Exception {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");
		
		final String K1 = "str1 val" + new Date();
		final String K2 = "str2 val" + new Date();
		VForm formEntity = new VForm();
		formEntity.setStr1(K1);
		formEntity.setStr2(K2);
		formEntity.setUnmappedAttr1("unmappedAttr1 val "+ new Date());
		
		// save: call
		controller.handlePost(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/saveButton").addAction(Action._get).getMock(), 
				json.write(formEntity).getJson());
		
		// delete
		controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/deleteButton").addAction(Action._get).getMock(), 
				null);
		
		// check that the values are reset
		assertNull(pForm.findParamByPath("/str1").getLeafState());
		
		// check cancel is enabled and delete is disabled
		assertTrue(pForm.findParamByPath("/cancelButton").isEnabled());
		assertFalse(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure no element has been added to core or view collections
		assertEquals(0, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(0, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
		
		// verify that db has no entries for collection, outer entity would have been created
		S4C_CoreMain core = mongo.findById(refId, S4C_CoreMain.class, "s4c_main");
		assertNotNull(core);
		assertTrue(core.getAnotherModeList().isEmpty());
	}
	
	private static final Long K_DB_REFID = -99L;
	
	@Test
	public void t05_onLoad_check() throws Exception {
		// save in db
		final String K1 = "str1 val" + new Date();
		final String K2 = "str2 val" + new Date();
		S4C_AnotherModel formEntity = new S4C_AnotherModel();
		formEntity.setStr1(K1);
		formEntity.setStr2(K2);
		
		S4C_CoreMain core = new S4C_CoreMain();
		core.setId(K_DB_REFID);
		core.setAnotherModeList(Arrays.asList(formEntity));
		
		// save: call
		mongo.save(core, "s4c_main");
		
		// get view root
		Object controllerResp_get = controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(K_DB_REFID).addAction(Action._get).getMock(), 
				null);
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_get);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");
		
		// check cancel is enabled and delete is disabled
		assertTrue(pForm.findParamByPath("/cancelButton").isEnabled());
		assertFalse(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure element has been added to core or view collections
		assertEquals(1, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(1, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
	}
	
	@Test
	public void t05_onLoad_edit() throws Exception {
		// load db
		t05_onLoad_check();
		
		// get view root
		Object controllerResp_get = controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(K_DB_REFID).addAction(Action._get).getMock(), 
				null);
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_get);
		
		// click edit
		controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(K_DB_REFID).addNested("/summaryGrid/0/editLink").addAction(Action._get).getMock(),
				null);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");
		
		// check that the transient param is now assigend
		assertTrue(pForm.findIfTransient().isAssinged());
		
		// check cancel is DISABLED and delete is ENABLED
		assertFalse(pForm.findParamByPath("/cancelButton").isEnabled());
		assertTrue(pForm.findParamByPath("/deleteButton").isEnabled());
	}

	@Test
	public void t06_add() throws Exception {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		Param<VForm> pForm = vp_main.findParamByPath("/form");

		// click on add
		controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/addButton").addAction(Action._get).getMock(), 
				null);
		
		// check that the values are reset
		assertNull(pForm.findParamByPath("/str1").getLeafState());
		
		// check cancel is enabled and delete is disabled
		assertTrue(pForm.findParamByPath("/cancelButton").isEnabled());
		assertFalse(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure no element has been added to core or view collections
		assertEquals(0, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(0, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
		
		// verify that db has no entries
		assertTrue(mongo.findAll(S4C_CoreMain.class, "s4c_main").isEmpty());
		
		// save: create payload
		final String K1 = "str1 val" + new Date();
		final String K2 = "str2 val" + new Date();
		VForm formEntity = new VForm();
		formEntity.setStr1(K1);
		formEntity.setStr2(K2);
		formEntity.setUnmappedAttr1("unmappedAttr1 val "+ new Date());
		
		// save: call
		controller.handlePost(
				MockHttpRequestBuilder.withUri(K_URI_VR).addRefId(refId).addNested("/form/saveButton").addAction(Action._get).getMock(), 
				json.write(formEntity).getJson());
		
		// check that the transient param is now assigend
		assertTrue(pForm.findIfTransient().isAssinged());
		
		// check cancel is DISABLED and delete is ENABLED
		assertFalse(pForm.findParamByPath("/cancelButton").isEnabled());
		assertTrue(pForm.findParamByPath("/deleteButton").isEnabled());
		
		// ensure no element has been added to core or view collections
		assertEquals(1, vp_main.findParamByPath(".m/anotherModeList").findIfCollection().size());
		assertEquals(1, vp_main.findParamByPath("/summaryGrid").findIfCollection().size());
		
		// verify that db has 1 entry
		assertFalse(mongo.findAll(S4C_CoreMain.class, "s4c_main").isEmpty());
	}
}
