package com.anthem.oss.nimbus.core.events.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.nimbus.platform.core.process.api.AbstractPlatformIntegrationTests;
import com.anthem.oss.nimbus.core.config.QTestDSL;
import com.anthem.oss.nimbus.core.config.TestDSL;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.DefaultQuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository.Projection;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.mongo.DefaultMongoModelRepository;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
import com.anthem.oss.nimbus.core.entity.task.AssignmentTask;
import com.anthem.oss.nimbus.core.entity.task.AssignmentTask.TaskType;
import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.Book;
import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.OrderBookFlow;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * Test class to verify mongodb persistence on setState.
 * 
 * @author Swetha Vemuri
 *
 */

@EnableAutoConfiguration
@ActiveProfiles("test")
public class DefaultMongoModelPersistenceHandlerTest  extends AbstractPlatformIntegrationTests {

	@Autowired
	DefaultMongoModelRepository rep;

	@Autowired
	DefaultQuadModelBuilder quadModelBuilder;

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
	
	@Test 
	public void tc02_dsl() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("testDsl");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
			    
//		QTestDSL qTest = new QTestDSL("tes");
//		Predicate predicate = qTest.name.startsWith("t");
//		SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
//		List<TestDSL> list = query.where(predicate).fetch();
//		assertEquals(list.size(), 1);
//		
//		Predicate predicate1 = qTest.name.startsWith("a");
//		SpringDataMongodbQuery<TestDSL> query1 = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
//		List<TestDSL> list1 = query1.where(predicate1).fetch();
//		assertEquals(list1.size(), 0);
//	
//		List<TestDSL> list2 = rep._search(TestDSL.class, "testdsl", predicate);
//		assertEquals(list2.size(), 1);
//		
//		//buid predicates
//		DefaultPredicateBuilder builder = new DefaultPredicateBuilder(TestDSL.class,"tes").with("name", ":", "testDsl");
//		SpringDataMongodbQuery<TestDSL> newquery = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
//		List<TestDSL> list3 = newquery.where(builder.build()).fetch();
//		assertEquals(list3.size(), 1);
//		
//		DefaultPredicateBuilder builder1 = new DefaultPredicateBuilder(TestDSL.class,"tes").with("name", ":", "a");
//		SpringDataMongodbQuery<TestDSL> newquery1 = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
//		List<TestDSL> list4 = newquery1.where(builder1.build()).fetch();
//		assertEquals(list4.size(), 0);
		
//		SpringDataMongodbQuery<TestDSL> newquery2 = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
//		long cnt = newquery2.fetchCount();
		QTestDSL qTest = new QTestDSL("tes");
		String criteria = "testdsl.name.startsWith('t')";
		long cnt = rep._search(TestDSL.class, "testdsl",criteria,Projection.COUNT);
	
		List<TestDSL> list2 = rep._search(TestDSL.class, "testdsl", criteria);
		assertEquals(list2.size(), 1);
		
		assertEquals(cnt, 1);
	}
	
	@Test
	public void tc03_groovy_startwith_positive() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("testDsl");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		final String groovyScript = "qTest.name.startsWith('t')";
		final Binding binding = new Binding();
		QTestDSL qTest = new QTestDSL("tes");
        binding.setVariable("qTest", qTest);

        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(predicate).fetch();
		assertEquals(list.size(), 1);
		
	}
	
	@Test
	public void tc04_groovy_startwith_negative() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("testDsl");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		final String groovyScript = "qTest.name.startsWith('a')";
		final Binding binding = new Binding();
		QTestDSL qTest = new QTestDSL("te");
        binding.setProperty("qTest", qTest);

        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(predicate).fetch();
		assertEquals(list.size(), 0);
		
	}
	
	@Test
	public void tc05_groovy_between_positive() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("testDsl");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		final String groovyScript = "qTest.age.between(1,11)";
		final Binding binding = new Binding();
		QTestDSL qTest = new QTestDSL("a");
        binding.setProperty("qTest", qTest);

        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(predicate).fetch();
		assertEquals(list.size(), 1);
		
	}
	
	@Test
	public void tc06_groovy_between_negative() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("sandeep");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		final String groovyScript = "qTest.age.between(1,9)";
		final Binding binding = new Binding();
		QTestDSL qTest = new QTestDSL("a");
        binding.setProperty("qTest", qTest);

        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(predicate).fetch();
		assertEquals(list.size(), 0);
		
	}
	
	@Test
	public void tc07_dynamicQuery_date_positive() {
		AssignmentTask assgnmt = new AssignmentTask();
		assgnmt.setTaskType(TaskType.patienteligibility);
		assgnmt.setDueDate(LocalDate.now());
		mongoOps.insert(assgnmt);
		
		final String groovyScript = "assignmenttask.dueDate.eq(todaydate)";
		final Binding binding = new Binding();
		Object obj = createQueryDslClassInstance(AssignmentTask.class);
		
	    assertNotNull("Not Null", obj);
        binding.setProperty("assignmenttask", obj);
        
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        localDateTime.atZone(TimeZone.getDefault().toZoneId());
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        
        binding.setProperty("todaydate",localDateTime);
        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery query = new SpringDataMongodbQuery(mongoOps, AssignmentTask.class);
		List list = query.where(predicate).fetch();
		assertEquals(list.size(), 1);
		
	}
	
	@Test
	public void tc08_dynamicQuery_between_positive() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("sandeep");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		final String groovyScript = "qTest.age.between(1,9)";
		final Binding binding = new Binding();
		Object obj = createQueryDslClassInstance(TestDSL.class);
		
	    assertNotNull("Not Null", obj);
        binding.setProperty("qTest", obj);
    
        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(predicate).fetch();
		assertEquals(list.size(), 0);
		
	}
	
	@Test
	public void tc09_dynamicQuery_multipleFilter_positive() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("sandeep");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		String[] groovyScript = {"qTest.age.between(1,11)","qTest.name.startsWith('s')"};
		final Binding binding = new Binding();
		Object obj = createQueryDslClassInstance(TestDSL.class);
		
	    assertNotNull("Not Null", obj);
        binding.setProperty("qTest", obj);
        final GroovyShell shell = new GroovyShell(binding); 
        List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
        for (String string : groovyScript) {
        	 BooleanExpression exp = (BooleanExpression)shell.evaluate(string); 
        	 if(exp!=null) {
        		 predicates.add(exp);
        	 }
		}
        BooleanExpression result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = result.and(predicates.get(i));
        }
        assertNotNull("Not Null", result);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(result).fetch();
		assertEquals(list.size(), 1);
		
	}

	@Test
	public void tc10_dynamicQuery_multipleFilter() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("sandeep");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		String[] groovyScript = {"qTest.age.between(1,11)","qTest.name.startsWith('a')"};
		final Binding binding = new Binding();
		Object obj = createQueryDslClassInstance(TestDSL.class);
		
	    assertNotNull("Not Null", obj);
        binding.setProperty("qTest", obj);
        final GroovyShell shell = new GroovyShell(binding); 
        List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
        for (String string : groovyScript) {
        	 BooleanExpression exp = (BooleanExpression)shell.evaluate(string); 
        	 if(exp!=null) {
        		 predicates.add(exp);
        	 }
		}
        BooleanExpression result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = result.and(predicates.get(i));
        }
        assertNotNull("Not Null", result);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(result).fetch();
		assertEquals(list.size(), 0);
		
	}
	
	public Object createQueryDslClassInstance(Class<?> referredClass) {
		Object obj = null;
		try {
			String cannonicalQuerydslclass = referredClass.getCanonicalName().replace(referredClass.getSimpleName(), "Q".concat(referredClass.getSimpleName()));
			Class cl = Class.forName(cannonicalQuerydslclass);
			Constructor con = cl.getConstructor(String.class);
			obj = con.newInstance(referredClass.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	@Test
	public void tc06_groovy_and_or_postive() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("sandeep");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		final String groovyScript = "qTest.age.between(1,9).or(qTest.name.startsWith('s'))";
		final Binding binding = new Binding();
		QTestDSL qTest = new QTestDSL("a");
        binding.setProperty("qTest", qTest);

        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(predicate).fetch();
		assertEquals(list.size(), 0);
		
	}
	
	@Test
	public void tc08_dynamicQuery_and_or_positive() {
		TestDSL testDsl = new TestDSL();
		testDsl.setName("sandeep");
		testDsl.setAge(10);
		mongoOps.insert(testDsl);
		
		final String groovyScript = "qTest.age.between(1,9).or(qTest.name.startsWith('s')).and(qTest.age.between(1,11))";
		final Binding binding = new Binding();
		Object obj = createQueryDslClassInstance(TestDSL.class);
		
	    assertNotNull("Not Null", obj);
        binding.setProperty("qTest", obj);
        
        final GroovyShell shell = new GroovyShell(binding); 
        Predicate predicate = (Predicate)shell.evaluate(groovyScript); 
        assertNotNull("Not Null", predicate);
        SpringDataMongodbQuery<TestDSL> query = new SpringDataMongodbQuery<>(mongoOps, TestDSL.class);
		List<TestDSL> list = query.where(predicate).fetch();
		assertEquals(list.size(), 1);
		
	}
	
	@Test 
	public void tc02_search() {
		AssignmentTask assgnmt = new AssignmentTask();
		assgnmt.setTaskType(TaskType.patienteligibility);
		assgnmt.setDueDate(LocalDate.now());
		mongoOps.insert(assgnmt);
			    
		final String groovyScript = "assignmenttask.dueDate.eq(todaydate)";
		//long cnt = rep._search(AssignmentTask.class, "testdsl",groovyScript,Projection.COUNT);
	
		List<AssignmentTask> list2 = rep._search(AssignmentTask.class, "assignmenttask", groovyScript);
		assertEquals(list2.size(), 1);
		
		//assertEquals(cnt, 1);
	}
}
