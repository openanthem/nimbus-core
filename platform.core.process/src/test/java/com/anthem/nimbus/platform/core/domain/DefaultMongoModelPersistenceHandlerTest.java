package com.anthem.nimbus.platform.core.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;

import com.anthem.nimbus.platform.core.process.api.AbstractPlatformIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.DefaultMongoModelRepository;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.test.sample.um.model.UMCase;
import com.anthem.oss.nimbus.test.sample.um.model.view.UMCaseFlow;

import test.com.anthem.nimbus.platform.spec.model.comamnd.TestCommandFactory;

/**
 * Test class to verify mongodb persistence on setState.
 * 
 * @author Swetha Vemuri
 *
 */

@EnableAutoConfiguration
public class DefaultMongoModelPersistenceHandlerTest  extends AbstractPlatformIntegrationTests {

	@Autowired
	DefaultMongoModelRepository rep;

	@Autowired
	QuadModelBuilder quadModelBuilder;

	@Autowired
	MongoOperations mongoOps;
		
	@Before
	public void t_init() {
		Command cmd = TestCommandFactory.create_view_icr_UMCaseFlow();
		QuadModel<UMCaseFlow, UMCase> q = quadModelBuilder.build(cmd);
		assertNotNull(q);

		UserEndpointSession.setAttribute(cmd, q);
	}
	/*
	 * Test case to verify the persistence of setState() method
	 */
	@Test
	public void tc01_setState_persist() {

		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession
				.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		assertNotNull(q.getCore());
		assertNotNull(q.getView());

		Param<String> caseType = q.getCore().findParamByPath("/caseType");
	//	Param<String> caseType = q.getView().findParamByPath("/pg3/aloha");
		caseType.setState("medical");
		assertNotNull(mongoOps.getCollection("core_cmcase"));

		@SuppressWarnings("unchecked")
		Model<UMCase> um = (Model<UMCase>) q.getCore().getRootDomain();

		UMCase umcase = rep._get(um.getState().getId(), UMCase.class, "core_umcase");
		assertNotNull(umcase);
		assertEquals(umcase.getCaseType(), "medical");

		UMCase db_umc = mongoOps.findById(umcase.getId(), UMCase.class, "core_umcase");
		assertNotNull(db_umc);
		assertEquals(db_umc.getCaseType(), "medical");
	}

}
