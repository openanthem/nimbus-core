package com.anthem.oss.nimbus.core.events.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;

import com.anthem.nimbus.platform.core.process.api.AbstractPlatformIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.DefaultMongoModelRepository;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.Book;
import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.OrderBookFlow;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

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
	
	private static QuadModel<OrderBookFlow, Book> q;
	
	private static boolean done =false;
	
	@Before
	public void t_init() {
		if(!done) {
			quadModelBuilder.getDomainConfigApi().setBasePackages(
					Arrays.asList(
							ProcessFlow.class.getPackage().getName(),
							Book.class.getPackage().getName(),
							OrderBookFlow.class.getPackage().getName()
							));
			quadModelBuilder.getDomainConfigApi().reload();
			done = true;
		}
			
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_new?b=$config").getCommand();
		q = quadModelBuilder.build(cmd);
		assertNotNull(q);

		UserEndpointSession.setAttribute(cmd, q);
	}
	
	@After
	public void tearDown() {
		UserEndpointSession.clearSession();
		mongoOps.dropCollection("core_book");
	}
	/*
	 * Test case to verify the persistence of setState() method
	 */
	@Test
	public void tc01_setState_persist() {

		Param<String> category = q.getCore().findParamByPath("/category");
		category.setState("history");
		assertNotNull(mongoOps.getCollection("core_book"));

		@SuppressWarnings("unchecked")
		Model<Book> mBook = (Model<Book>) q.getCore().getRootDomain();

		Book book = rep._get(mBook.getState().getId(), Book.class, "core_book");
		assertNotNull(book);
		assertEquals(book.getCategory(), "history");

		Book dbBook = mongoOps.findById(book.getId(), Book.class, "core_book");
		assertNotNull(dbBook);
		assertEquals(dbBook.getCategory(), "history");
	}

}
