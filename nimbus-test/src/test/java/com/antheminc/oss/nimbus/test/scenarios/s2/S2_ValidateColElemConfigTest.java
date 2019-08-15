/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.search.DefaultSearchFunctionHandlerExample;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.search.DefaultSearchFunctionHandlerQuery;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s2.core.S2C_LineItemB;
import com.antheminc.oss.nimbus.test.scenarios.s2.core.S2C_Row;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S2_ValidateColElemConfigTest extends AbstractFrameworkIntegrationTests {
	
	private static final String K_URI_VR = PLATFORM_ROOT + "/s2v_main";
	
	//@MockBean(name="default._search$execute")
	//CommandExecutor<?> defaultActionExecutorSearch;
	
	@MockBean(name="default._search$execute?fn=query")
	DefaultSearchFunctionHandlerQuery<?,?> defaultSearchFunctionHandlerQuery;
	
	@MockBean(name="default._search$execute?fn=exmaple")
	DefaultSearchFunctionHandlerExample<?,?> defaultSearchFunctionHandlerExample;
	
	private static final List<S2C_Row> db_rows;
	private static final List<S2C_LineItemB> db_rows_nestedRowBodyLineItems;
	static {
		db_rows = new ArrayList<>();
		db_rows.add(new S2C_Row());

		db_rows.get(0).setTopValue1("0_topValue1_"+new Date());
		db_rows.get(0).setTopValue2("0_topValue2"+ new Date());
		
		db_rows_nestedRowBodyLineItems = new ArrayList<>();
		db_rows_nestedRowBodyLineItems.add(new S2C_LineItemB());
		
		db_rows_nestedRowBodyLineItems.get(0).setLi_v1("0_li_v1_"+new Date());
		db_rows_nestedRowBodyLineItems.get(0).setLi_v2("0_li_v2_"+new Date());
	}
	
	@Override
	public void before() {
		super.before();
		Mockito.when(defaultSearchFunctionHandlerQuery.execute(any(),any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ExecutionContext input = invocation.getArgument(0);
				if(input.getCommandMessage().getCommand().getAbsoluteUri().contains("s2c_row"))
					return db_rows;
				else
					return db_rows_nestedRowBodyLineItems;					
			}
		});
		Mockito.when(defaultSearchFunctionHandlerExample.execute(any(),any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ExecutionContext input = invocation.getArgument(0);
				if(input.getCommandMessage().getCommand().getAbsoluteUri().contains("s2c_row"))
					return db_rows;
				else
					return db_rows_nestedRowBodyLineItems;					
			}
		});

	}
	
	@Test
	public void t00_config_check() throws Exception {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(K_URI_VR)
			.addAction(Action._new)
			.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertNotNull(controllerResp);
		
		Object o = ExtractResponseOutputUtils.extractOutput(controllerResp);
		assertNotNull(o);
		
		//assertThat(json.write(o)).hasJsonPathValue("$.type.model.params[0].config.type.elementConfig.type.modelConfig.paramConfigs", new Object[]{});
		System.out.println(json.write(o).getJson());
		assertThat(json.write(o)).hasJsonPathValue("$.config.type.modelConfig.paramConfigs[0].type.elementConfig.type.modelConfig.paramConfigs", new Object[]{});
	}
	
	private Object createNew_VR() {
		return controller.handleGet(
					MockHttpRequestBuilder.withUri(K_URI_VR).addAction(Action._new).getMock(), 
					null);
	}
	
	@Test
	public void t01_col_nestedCol_update() throws Exception {
		// vr_main: new session for view root
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		
		// rows: simulate server side operation (via function handler) to set top level collection
		Object resp_rows_get = controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR).addNested("/rows").addAction(Action._get).getMock(),
				null);

		assertNotNull(resp_rows_get);
		
		// rows: check event update 
		Set<ParamEvent> controllerResp_rows_get$Events = ExtractResponseOutputUtils.extractAggregatedEvents(resp_rows_get);
		assertNotNull(controllerResp_rows_get$Events);
		assertEquals(1, controllerResp_rows_get$Events.size());
		assertSame(vp_main.findParamByPath("/rows"), controllerResp_rows_get$Events.iterator().next().getParam());
		
		// rows: check core to view state conversion 
		assertSame(db_rows.get(0).getTopValue1(), vp_main.findParamByPath("/rows/0/topValue1.m").getState());
		assertSame(db_rows.get(0).getTopValue2(), vp_main.findParamByPath("/rows.m/0/topValue2").getState());
		
		// nested row body lineItems: simulate server side operation (via function handler) to set nested collection
		Object resp_rowBodyLIs_get = controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_VR)
					.addNested("/rows/0/nestedRowBody/nestedRowBodyLineItems").addAction(Action._get).getMock(),
				null);

		assertNotNull(resp_rowBodyLIs_get);

		// nested row body lineItems: check event update 
		Set<ParamEvent> resp_rowBodyLIs_get$Events = ExtractResponseOutputUtils.extractAggregatedEvents(resp_rowBodyLIs_get);
		assertNotNull(resp_rowBodyLIs_get$Events);
		assertEquals(1, resp_rowBodyLIs_get$Events.size());
		assertSame(vp_main.findParamByPath("/rows/0/nestedRowBody/nestedRowBodyLineItems"), resp_rowBodyLIs_get$Events.iterator().next().getParam());

		System.out.println(json.write(ExtractResponseOutputUtils.extractOutput(controllerResp_new)).getJson());
	}
}