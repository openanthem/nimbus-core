/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.test.sample.domain.model.SampleCoreEntity;
import com.anthem.oss.nimbus.test.sample.domain.model.ui.VPSampleViewPageGreen;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditStateChangeHandlerTest extends AbstractStateEventHandlerTests {

	private static final String CORE_attr_String = "/sample_core/attr_String";
	private static final String CORE_attr_Integer = "/sample_core/attr_Integer";
	private static final String CORE_unmapped_Date = "/sample_core/unmapped_Date";

	private static final String VIEW_attr_String = "/sample_view/page_green/tile/attr_String";
	private static final String VIEW_unmapped_String = "/sample_view/page_green/tile/unmapped_String";
	private static final String VIEW_attr_Integer = "/sample_view/page_green/tile/attr_Integer";
	
	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	private Param<String> getCore_attr_string() {
		return _q.getRoot().findParamByPath(CORE_attr_String);
	}
	
	private Param<Integer> getCore_attr_integer() {
		return _q.getRoot().findParamByPath(CORE_attr_Integer);
	}
	
	private Param<Date> getCore_unmapped_date() {
		return _q.getRoot().findParamByPath(CORE_unmapped_Date);
	}
	
	
	private Param<String> getView_attr_string() {
		return _q.getRoot().findParamByPath(VIEW_attr_String);
	}
	
	private Param<Integer> getView_attr_integer() {
		return _q.getRoot().findParamByPath(VIEW_attr_Integer);
	}
	
	private Param<String> getView_unmapped_string() {
		return _q.getRoot().findParamByPath(VIEW_unmapped_String);
	}
	
	@Test
	public void t00_init_check() {
		assertNotNull(getCore_attr_string());
		assertNotNull(getCore_attr_integer());
		assertNotNull(getCore_unmapped_date());
		
		assertNotNull(getView_attr_string());
		assertNotNull(getView_attr_integer());
		assertNotNull(getView_unmapped_string());
		
		assertNotNull(getCore_attr_string().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(SampleCoreEntity.class, "attr_String", true).getAnnotation(Audit.class)));
		assertNotNull(getCore_attr_integer().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(SampleCoreEntity.class, "attr_Integer", true).getAnnotation(Audit.class)));
		assertNotNull(getCore_unmapped_date().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(SampleCoreEntity.class, "unmapped_Date", true).getAnnotation(Audit.class)));
		assertTrue(CollectionUtils.isEmpty(getCore_unmapped_date().getEventSubscribers()));
		
		assertNotNull(getView_attr_string().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(VPSampleViewPageGreen.TileGreen.class, "attr_String", true).getAnnotation(Audit.class)));
		assertNull(getView_attr_integer().getConfig().getEventHandlerConfig());
		assertNotNull(getView_unmapped_string().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(VPSampleViewPageGreen.TileGreen.class, "unmapped_String", true).getAnnotation(Audit.class)));
		assertTrue(CollectionUtils.isEmpty(getView_unmapped_string().getEventSubscribers()));
	}
	
	@Test
	public void t01_core_string() {
		
	}
}
