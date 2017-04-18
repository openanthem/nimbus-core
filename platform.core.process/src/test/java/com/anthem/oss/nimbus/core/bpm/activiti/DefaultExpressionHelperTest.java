package com.anthem.oss.nimbus.core.bpm.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.nimbus.platform.core.process.api.AbstractPlatformIntegrationTests;
import com.anthem.oss.nimbus.core.bpm.DefaultExpressionHelper;
import com.anthem.oss.nimbus.core.bpm.activiti.ExpressionHelperTestData.Author;
import com.anthem.oss.nimbus.core.bpm.activiti.ExpressionHelperTestData.Book;
import com.anthem.oss.nimbus.core.bpm.activiti.ExpressionHelperTestData.Book.Publisher;
import com.anthem.oss.nimbus.core.bpm.activiti.ExpressionHelperTestData.OrderBookFlow;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Swetha Vemuri
 *
 */
@EnableAutoConfiguration
@ActiveProfiles("test")
public class DefaultExpressionHelperTest extends AbstractPlatformIntegrationTests {
	
	@Autowired
	DefaultExpressionHelper expHlpr;
	
	@Autowired
	QuadModelBuilder quadModelBuilder;
	
	@Autowired private MongoTemplate mt;
	
	private static QuadModel<OrderBookFlow, Book> q ;
	
	private static Book book1;
	private static Book book2;
	
	
	@Before
	public void init() {
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_submitOrder/_process?b=$executeAnd$config").getCommand();
		q = quadModelBuilder.build(cmd);
		assertNotNull(q);

		UserEndpointSession.setAttribute(cmd, q);
		book1 = createBook_1();
		mt.insert(book1, AnnotationUtils.findAnnotation(Book.class, Domain.class).value());	
		book2 = createBook_2();
		mt.insert(book2, AnnotationUtils.findAnnotation(Book.class, Domain.class).value());			
	}
	
	@After
	public void tear_down() {
		mt.dropCollection("core_book");
		mt.dropCollection("author");
	}
	
	@Test
	public void t01_get_domainAlias() {
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book:"+book1.getId()+"/_submitOrder/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/core_book:#DomainAlias#");
		Object result = expHlpr._get(cmdMsg, execution, resolvedUri);
		assertNotNull(result);
		assertEquals(book1.getId(),((Book) result).getId());		
	}
	
	@Test
	public void t02_get_processAlias() {
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_submitOrder:"+book1.getId()+"/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/core_book:#ProcessAlias#");
		Object result = expHlpr._get(cmdMsg, execution, resolvedUri);
		assertNotNull(result);
		assertEquals(book1.getId(),((Book) result).getId());		
	}
	
	@Test
	public void t03_get_fail_improperRefId() {
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_submitOrder:"+book1.getId()+"/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/core_book:#DomainAlias#");
		Object result = expHlpr._get(cmdMsg, execution, resolvedUri);
		assertNull(result);
	}
	
	@Test
	public void t04_get_withpayload() {
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_submitOrder:"+book1.getId()+"/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/core_book");
		Object result = expHlpr._get(cmdMsg, execution, resolvedUri,"{\"id\" : \"#ProcessAlias#\"}");
		assertNull(result);
	}
	
	@Test
	public void t05_search_nopayload() {
		assertNotNull(book2.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_searchBooks/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/core_book");
		Object result = expHlpr._search(cmdMsg, execution, resolvedUri);
		assertNotNull(result);
		assertTrue(result instanceof List<?>);
		assertEquals(2,((List<Book>)result).size());
	}
	
	@Test
	public void t05_search_withpayload() {
		assertNotNull(book2.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_searchBooks/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/core_book");
		Object result = expHlpr._search(cmdMsg, execution, resolvedUri,"{\"name\" : \"Clean Code\"}");
		assertNotNull(result);	
		assertTrue(result instanceof List<?>);
		assertEquals(1,((List<Book>)result).size());
		assertEquals(book2.getId(),((List<Book>)result).get(0).getId());
	}
	
	@Test
	public void t06_setExternal_single_arg() {
		insertAuthors();
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_listBooks/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/author/_search?b=$execute");
		assertNull(q.getCore().findParamByPath("/coAuthors").getState());
		expHlpr._setExternal(cmdMsg, execution, resolvedUri, "/pg1/coAuthors");	
		assertNotNull(q.getView().findParamByPath("/pg1/coAuthors").getState());
		assertEquals(2,((List<Author>)q.getView().findParamByPath("/pg1/coAuthors").getState()).size());
		assertEquals("Smith",q.getView().findParamByPath("/pg1/coAuthors/0/name").getState());
	}
	
	@Test
	public void t07_setExternal_single_arg_payload() {
		insertAuthors();
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_listBooks/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		cmdMsg.setRawPayload("{\"firstName\" : \"Tim\"}");
		assertNull(q.getCore().findParamByPath("/coAuthors").getState());
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/author/_search?b=$execute");
		expHlpr._setExternal(cmdMsg, execution, resolvedUri, "/pg1/coAuthors");	
		assertNotNull(q.getView().findParamByPath("/pg1/coAuthors").getState());
		assertEquals(1,((List<Author>)q.getView().findParamByPath("/pg1/coAuthors").getState()).size());
	}
	
	@Test
	public void t08_setExternal_multiple_args() {
		insertAuthors();
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_listBooks/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/author/_search?b=$execute");
		expHlpr._setExternal(cmdMsg, execution, resolvedUri, "/pg1/coAuthors" ,"{\"lastName\" : \"Smith\"}");				
		assertNotNull(q.getView().findParamByPath("/pg1/coAuthors").getState());
		assertEquals(2,((List<Author>)q.getView().findParamByPath("/pg1/coAuthors").getState()).size());
	}
	
	@Test
	//TODO - need to see how to set execution variable in mock execution delegate
	public void t09_setInternal() {
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_submitOrder/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		String resolvedUri = expHlpr.getResolvedUri(cmdMsg,"/category");	
		execution.setVariable("category", "new");
		q.getCore().findParamByPath("/category").setState("Fiction");
		expHlpr._setInternal(cmdMsg, execution, resolvedUri, "category");				
		assertEquals("new",q.getCore().findParamByPath("/category").getState());
	}
	
	@Test
	public void t10_fireAllRules() {
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_submitOrder:"+book1.getId()+"/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		expHlpr._fireAllRules(cmdMsg, execution);
		assertEquals("coreRuleUpdate",q.getCore().findParamByPath("/category").getState());
		assertEquals("viewRuleUpdate",q.getView().findParamByPath("/pg1/displayName").getState());
	}
	
	@Test
	// TODO - check if this functionality is implemented in framework. i.e firing rules selectively based on domain
	public void t11_fireRulesByPath() {
		assertNotNull(book1.getId());
		DelegateExecution execution = mock(DelegateExecution.class);
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_submitOrder:"+book1.getId()+"/_process?b=$executeAnd$config").getCommand();
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		expHlpr._fireRulesByPath(cmdMsg, execution , "/author");
		assertEquals("authorNameUpdate",q.getCore().findParamByPath("/author/firstName").getState());
	}
	
	private Book createBook_1() {
		Book book = new Book();
		book.setName("The Prodigal Daughter");
		book.setCategory("Fiction");
		
		Author author = new Author();
		author.setFirstName("Jeffrey");
		author.setLastName("Archer");
		book.setAuthor(author);
		return book;
	}
	
	private Book createBook_2() {
		Book book = new Book();
		book.setName("Clean Code");
		book.setCategory("Education");
		
		Author author = new Author();
		author.setFirstName("Robert");
		author.setLastName("Martin");
		book.setAuthor(author);
		
		Publisher p1 = new Publisher();
		p1.setName("Pivotal Books");
		Publisher p2 = new Publisher();
		p1.setName("Hallmark");
		
		List<Publisher> p_list = new ArrayList<>();
		p_list.add(p1);
		p_list.add(p2);
		book.setPublishers(p_list);
		return book;
	}
	
	private List<Author> insertAuthors() {
		Author author1 = new Author();
		author1.setFirstName("John");
		author1.setLastName("Smith");
		mt.insert(author1, "author");	
		
		Author author2 = new Author();
		author2.setFirstName("Tim");
		author2.setLastName("Smith");
		mt.insert(author2, "author");	
		
		List<Author> list = new ArrayList<>();
		list.add(author1);
		list.add(author2);
		return list;
	}
}
