package com.antheminc.oss.nimbus.test.scenarios.changelog;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.extension.ChangeLogCommandEventHandler.ChangeLogEntry;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChangeLogTest extends AbstractFrameworkIntegrationTests {

	private static final String CL_VIEW_A = "samplechangelogview_a";
	private static final String CL_VIEW_A_ROOT = PLATFORM_ROOT + "/" + CL_VIEW_A;
	
	private static Long CL_VIEW_A_ID;
	
	private Long executeNewOnView() {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(CL_VIEW_A_ROOT)
			.addAction(Action._new)
			.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		Param<?> p = ParamUtils.extractResponseByParamPath(controllerResp, CL_VIEW_A);
		assertThat(p).isNotNull();
		
		return p.findStateByPath("/.m/id");
	}
	
	private Object doEventNotifyUpdate(Long refId, String path, Object state) {
		HttpServletRequest req = MockHttpRequestBuilder.withUri(PLATFORM_ROOT)
				.addNested("/event/notify")
				.getMock();
		
		ModelEvent<String> modelEvent_q1 = new ModelEvent<>();
		modelEvent_q1.setId("/" + CL_VIEW_A + ":" + refId + path);
		modelEvent_q1.setType(Action._update.name());
		try {
			modelEvent_q1.setPayload(json.write(state).getJson());
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert json");
		}
		
		return controller.handleEventNotify(req, modelEvent_q1);
	}
	
	@Test
	public void t00_create_entity_A() {
		CL_VIEW_A_ID = executeNewOnView();
		List<ChangeLogEntry> entries = mongo.find(new Query(Criteria.where("url").regex("samplechangelogview_a/_new")), ChangeLogEntry.class, "changelog");
		assertThat(entries).isNotEmpty();
		
	}
	
	@Test
	public void t01_create_entity_B_via_config_from_A() {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(CL_VIEW_A_ROOT)
				.addRefId(CL_VIEW_A_ID)
				.addNested("/action_createEntityB")
				.addAction(Action._get)
				.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		ChangeLogEntry cmdEntry = mongo.findOne(new Query(Criteria.where("url").regex("action_createEntityB")), ChangeLogEntry.class, "changelog");
		assertThat(cmdEntry).isNotNull();
		
		ChangeLogEntry valueEntry_NestedEntityB = mongo.findOne(new Query(Criteria.where("value").is("Test_Nested_Status_B")), ChangeLogEntry.class, "changelog");
		
		// Changing from isNull to isNotNull. Init Enity is a function handler. Any change to state caused by the function handler should be processed by the corresponding state change handlers
		assertThat(valueEntry_NestedEntityB).isNotNull();
		
	}
	
	@Test
	public void t02_update_EventNotifyOnNestedParam_entity_A() {		
		Object controllerResp = doEventNotifyUpdate(CL_VIEW_A_ID, "/vpSampleCoreChangeLog/vtSampleCoreChangeLog/vsSampleCoreChangeLog/vfSampleCoreChangeLog/status/_update", "Test_Status");
		assertThat(controllerResp).isNotNull();
		
		ChangeLogEntry cmdEntry = mongo.findOne(new Query(Criteria.where("url").regex("vpSampleCoreChangeLog/vtSampleCoreChangeLog/vsSampleCoreChangeLog/vfSampleCoreChangeLog/status")), ChangeLogEntry.class, "changelog");
		assertThat(cmdEntry).isNotNull();
		
		ChangeLogEntry valueEntry = mongo.findOne(new Query(Criteria.where("value").is("Test_Status")), ChangeLogEntry.class, "changelog");
		assertThat(valueEntry).isNotNull();
		
		ChangeLogEntry valueEntryViaRule = mongo.findOne(new Query(Criteria.where("value").is("Test_Status_Via_Rule")), ChangeLogEntry.class, "changelog");
		assertThat(valueEntryViaRule).isNotNull();
		
	}
	
	@Test
	public void t03_execute_ParamWithConfigs_entity_A() {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(CL_VIEW_A_ROOT)
				.addRefId(CL_VIEW_A_ID)
				.addNested("/vpSampleCoreChangeLog/vtSampleCoreChangeLog/vsSampleCoreChangeLog/vfSampleCoreChangeLog/button")
				.addAction(Action._get)
				.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		ChangeLogEntry cmdEntry = mongo.findOne(new Query(Criteria.where("url").regex("vpSampleCoreChangeLog/vtSampleCoreChangeLog/vsSampleCoreChangeLog/vfSampleCoreChangeLog/button")), ChangeLogEntry.class, "changelog");
		assertThat(cmdEntry).isNotNull();
		
		ChangeLogEntry subCmdEntryWithRootOnlyPath = mongo.findOne(new Query(Criteria.where("url").regex("samplechangelogview_b/_new")), ChangeLogEntry.class, "changelog");
		assertThat(subCmdEntryWithRootOnlyPath).isNotNull();
		
		ChangeLogEntry subCmdEntryWithNestedPath = mongo.findOne(new Query(Criteria.where("url").regex("sampleCoreChangLogEntityNested/nestedStatus/_update")), ChangeLogEntry.class, "changelog");
		assertThat(subCmdEntryWithNestedPath).isNotNull();
		
		ChangeLogEntry valueEntry_NestedEntityA = mongo.findOne(new Query(Criteria.where("value").is("Test_Nested_Status")), ChangeLogEntry.class, "changelog");
		assertThat(valueEntry_NestedEntityA).isNotNull();
		
		ChangeLogEntry valueEntryViaRuleStateless = mongo.findOne(new Query(Criteria.where("value").is("Test_Status_Via_Rule_Stateless")), ChangeLogEntry.class, "changelog");
		assertThat(valueEntryViaRuleStateless).isNotNull();
		
	}
	
	@Test
	public void testPreviousState() {
		Long refId = executeNewOnView();
		assertThat(refId).isNotNull();
		
		Object resp1 = doEventNotifyUpdate(refId, "/vpSampleCoreChangeLog/vtSampleCoreChangeLog/vsSampleCoreChangeLog/vfSampleCoreChangeLog/status/_update", "before");
		assertThat(resp1).isNotNull();
		
		// validate previous value is null
		ChangeLogEntry before = mongo.findOne(new Query(Criteria.where("value").is("before")), ChangeLogEntry.class, "changelog");
		assertThat(before).isNotNull();
		assertThat(before.getPreviousValue()).isNull();
		
		Object resp2 = doEventNotifyUpdate(refId, "/vpSampleCoreChangeLog/vtSampleCoreChangeLog/vsSampleCoreChangeLog/vfSampleCoreChangeLog/status/_update", "after");
		assertThat(resp2).isNotNull();
		
		// validate previous value is "before"
		ChangeLogEntry after = mongo.findOne(new Query(Criteria.where("value").is("after")), ChangeLogEntry.class, "changelog");
		assertThat(after).isNotNull();
		assertThat(after.getPreviousValue()).isEqualTo("before");
	}
	
	@Test
	public void testSessionId() {
		Long refId = executeNewOnView();
		assertThat(refId).isNotNull();
		
		Object resp1 = doEventNotifyUpdate(refId, "/vpSampleCoreChangeLog/vtSampleCoreChangeLog/vsSampleCoreChangeLog/vfSampleCoreChangeLog/status/_update", "session-id-test");
		assertThat(resp1).isNotNull();
		
		ChangeLogEntry actual = mongo.findOne(new Query(Criteria.where("value").is("session-id-test")), ChangeLogEntry.class, "changelog");
		assertThat(actual).isNotNull();
		assertThat(actual.getSessionId()).isEqualTo("test-session");
	}
}
