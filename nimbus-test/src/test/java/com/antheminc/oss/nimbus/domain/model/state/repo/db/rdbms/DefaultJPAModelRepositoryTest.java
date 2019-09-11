/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.AddressJPACoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.PersonJPACoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.SampleJPARootCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.view.PersonLineItem;

/**
 * @author Soham Chakravarti
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultJPAModelRepositoryTest extends AbstractRdbmsTest {

	public static final String FH_SEARCH_BY_QUERY = "/_search?fn=query";

	private SimpleJpaRepository<SampleJPARootCoreEntity, Long> jpaRepo;

	@Before
	public void test_context_load() {
		assertNotNull(repo);
		assertNotNull(em);

		jpaRepo = new SimpleJpaRepository<>(SampleJPARootCoreEntity.class, em);
	}

	@Test
	public void t01_simple_jpa() {
		SampleJPARootCoreEntity core = new SampleJPARootCoreEntity();
		core.setId(1L);
		core.setA1("a1 " + new Date());

		repo._save("core", core);

		SampleJPARootCoreEntity actual = jpaRepo.findById(core.getId()).get();
		assertEquals(core.getA1(), actual.getA1());
	}

	@Test
	public void t02_simple_cmd() {
		HttpServletRequest req_new = MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT).addAction(Action._new).getMock();

		// id generation
		Object resp_new = controller.handleGet(req_new, null);
		assertNotNull(resp_new);

		Param<SampleJPARootCoreEntity> pCore = ExtractResponseOutputUtils.extractOutput(resp_new);
		assertNotNull(pCore);

		Long id = pCore.findStateByPath("/id");
		assertNotNull(id);

		// update attribute
		String a1 = "a1 @" + new Date();
		Object resp_update_a1 = controller.handlePost(MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT).addRefId(id)
				.addNested("/a1").addAction(Action._update).getMock(), jsonUtils.convert(a1));
		assertNotNull(resp_update_a1);

		// save to db
		Object resp_save = controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT).addRefId(id).addAction(Action._save).getMock(), null);
		assertNotNull(resp_save);

		SampleJPARootCoreEntity core = pCore.getState();
		SampleJPARootCoreEntity actual = jpaRepo.findById(core.getId()).get();

		assertEquals(id, actual.getId());
		assertEquals(a1, actual.getA1());
	}

	@SuppressWarnings("unchecked")
	private Output<SampleJPARootCoreEntity> createNewCore(SampleJPARootCoreEntity value) {
		HttpServletRequest newRequest = MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT).addAction(Action._new)
				.getMock();
		MultiOutput respNew = ((Holder<MultiOutput>) controller.handleGet(newRequest, jsonUtils.convert(value)))
				.getState();
		assertThat(respNew).isNotNull();
		return respNew.findFirstParamOutputEndingWithPath(CORE_ALIAS);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void t03_get() {
		// create expected entity
		SampleJPARootCoreEntity expected = new SampleJPARootCoreEntity();
		expected.setA1("getting it");

		// create core and get id
		Output<SampleJPARootCoreEntity> respNew = createNewCore(expected);
		assertThat(respNew).isNotNull();
		Long id = respNew.getRootDomainId();
		assertThat(id).isNotNull();

		// invoke _get on the new domain entity
		MultiOutput respGet = ((Holder<MultiOutput>) controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT).addRefId(id).addAction(Action._get).getMock(), null))
						.getState();
		assertThat(respGet).isNotNull();
		Param<SampleJPARootCoreEntity> pActual = (Param<SampleJPARootCoreEntity>) respGet.getSingleResult();
		assertThat(pActual.getState()).isEqualTo(expected);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void t04_delete() {
		// create expected entity
		SampleJPARootCoreEntity expected = new SampleJPARootCoreEntity();
		expected.setA1("deleting it");
		
		// create core and get id
		Output<SampleJPARootCoreEntity> respNew = createNewCore(expected);
		assertThat(respNew).isNotNull();
		Long id = respNew.getRootDomainId();
		assertThat(id).isNotNull();

		// invoke _delete on the new domain entity
		MultiOutput respDelete = ((Holder<MultiOutput>) controller.handleDelete(
				MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT).addRefId(id).addAction(Action._delete).getMock(), null))
						.getState();
		assertNotNull(respDelete);
		assertThat(respDelete.getSingleResult()).isEqualTo(true);
		
		// invoke _get on the new domain entity, it should not be found.
		try {
			controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT).addRefId(id).addAction(Action._get).getMock(), null);
		} catch (FrameworkRuntimeException e) {
			assertThat(e.getMessage()).contains("Entity not found for /sample_jpa_core:" + id);
		}
	}

	/**
	 * Showcases: Search By Query for JPA
	 * /p/sample_jpa_core/_search?fn=query&where=sample_jpa_core.a1.eq('John')
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testBasicQuery() {
		// create expected entity
		PersonJPACoreEntity expected = new PersonJPACoreEntity("John", "Doe-BasicQuery");

		// save to db
		MultiOutput _newResponse = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(expected));
		assertThat(_newResponse).isNotNull();
		Long refId = _newResponse.findFirstParamOutputEndingWithPath("/person").getRootDomainId();
		assertThat(refId).isNotNull();

		StringBuilder query = new StringBuilder(FH_SEARCH_BY_QUERY).append("&where=")
				.append("person.lastName.eq('Doe-BasicQuery')");
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/person" + query.toString()).getCommand();
		MultiOutput response = commandGateway.execute(cmd, null);
		assertThat(response).isNotNull();

		List<Param<PersonJPACoreEntity>> result = (List<Param<PersonJPACoreEntity>>) response.getSingleResult();
		assertThat(CollectionUtils.isNotEmpty(result)).isTrue();
		assertThat(CollectionUtils.isEqualCollection(result, Stream.of(expected).collect(Collectors.toList())))
				.isTrue();
	}

	/**
	 * Showcase: Create a new persistable entity with a nested persistable
	 * domain entity Person -> Address
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testNewNestedEntity() {
		PersonJPACoreEntity expectedPerson = new PersonJPACoreEntity("Homer", "Simpson");
		AddressJPACoreEntity expectedAddress = new AddressJPACoreEntity("742 Evergreen Terrace");
		expectedPerson.setAddress(expectedAddress);

		// save to db
		MultiOutput _newResponse = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(expectedPerson));
		assertThat(_newResponse).isNotNull();
		Param<PersonJPACoreEntity> pPerson = (Param<PersonJPACoreEntity>) _newResponse.getSingleResult();
		assertThat(pPerson).isNotNull();
		assertThat(pPerson.getState()).isEqualTo(expectedPerson);

		// retrieve nested entity by domain _get
		Long addressId = pPerson.getState().getAddress().getId();
		assertThat(addressId).isNotNull();
		Command cmdGetAddress = CommandBuilder.withUri(PLATFORM_ROOT + "/address:" + addressId + "/_get").getCommand();
		MultiOutput respGetAddress = commandGateway.execute(cmdGetAddress, null);
		assertThat(respGetAddress).isNotNull();
		Param<AddressJPACoreEntity> pAddress = (Param<AddressJPACoreEntity>) respGetAddress.getSingleResult();
		assertThat(pAddress).isNotNull();
		assertThat(pAddress.getState()).isEqualTo(expectedAddress);
	}

	/**
	 * Showcase projection the result set via "tuple"
	 * /p/person/_search?fn=query&select=(firstName)&where=person.lastName.eq('Doe')
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testTuple() {
		// create expected entities
		PersonJPACoreEntity person1 = new PersonJPACoreEntity("John", "Doe-Tuple");
		PersonJPACoreEntity person2 = new PersonJPACoreEntity("Jane", "Doe-Tuple");
		// (just collect the first names of any entities added to "person" to
		// create the expected result set)
		List<PersonJPACoreEntity> expected = Stream.of(person1, person2)
				.map(p -> new PersonJPACoreEntity(p.getFirstName(), null)).collect(Collectors.toList());

		// save to db
		MultiOutput _newResponse1 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person1));
		assertThat(_newResponse1).isNotNull();
		MultiOutput _newResponse2 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person2));
		assertThat(_newResponse2).isNotNull();

		// do tuple retrieval
		StringBuilder query = new StringBuilder(FH_SEARCH_BY_QUERY)
				.append("&select=(firstName)&where=person.lastName.eq('Doe-Tuple')");
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/person" + query.toString()).getCommand();
		MultiOutput response = commandGateway.execute(cmd, null);
		assertThat(response).isNotNull();

		// validate
		List<Param<PersonJPACoreEntity>> result = (List<Param<PersonJPACoreEntity>>) response.getSingleResult();
		assertThat(CollectionUtils.isNotEmpty(result)).isTrue();
		assertThat(CollectionUtils.isEqualCollection(result, expected)).isTrue();
	}

	/**
	 * Showcase server-side pagination
	 * /p/person/_search?fn=query&page=0&pageSize=5
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testPagination() {
		// create expected entities
		PersonJPACoreEntity person1 = new PersonJPACoreEntity("John", "Doe-Paginate");
		PersonJPACoreEntity person2 = new PersonJPACoreEntity("Jane", "Doe-Paginate");
		PersonJPACoreEntity person3 = new PersonJPACoreEntity("Jack", "Doe-Paginate");
		List<PersonJPACoreEntity> expectedPage1 = Stream.of(person1, person2).collect(Collectors.toList());
		List<PersonJPACoreEntity> expectedPage2 = Stream.of(person3).collect(Collectors.toList());

		// save to db
		MultiOutput _newResponse1 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person1));
		assertThat(_newResponse1).isNotNull();
		MultiOutput _newResponse2 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person2));
		assertThat(_newResponse2).isNotNull();
		MultiOutput _newResponse3 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person3));
		assertThat(_newResponse3).isNotNull();

		// do page 1 retrieval
		Command cmdPage1 = CommandBuilder
				.withUri(PLATFORM_ROOT
						+ "/person/_search?fn=query&where=person.lastName.eq('Doe-Paginate')&page=0&pageSize=2&b=$state")
				.getCommand();
		MultiOutput respPage1 = commandGateway.execute(cmdPage1, null);
		assertThat(respPage1).isNotNull();
		List<PersonJPACoreEntity> resultPage1 = (List<PersonJPACoreEntity>) respPage1.getSingleResult();
		assertThat(resultPage1).isNotNull();
		assertThat(CollectionUtils.isEqualCollection(resultPage1, expectedPage1));

		// do page 2 retrieval
		Command cmdPage2 = CommandBuilder
				.withUri(PLATFORM_ROOT
						+ "/person/_search?fn=query&where=person.lastName.eq('Doe-Paginate')&page=1&pageSize=2&b=$state")
				.getCommand();
		MultiOutput respPage2 = commandGateway.execute(cmdPage2, null);
		assertThat(respPage2).isNotNull();
		List<PersonJPACoreEntity> resultPage2 = (List<PersonJPACoreEntity>) respPage2.getSingleResult();
		assertThat(resultPage2).isNotNull();
		assertThat(CollectionUtils.isEqualCollection(resultPage2, expectedPage2));
	}

	/**
	 * Showcase server-side filtering
	 * /p/person/_search?fn=query&where=<!filterCriteria!> Payload: filters: [ {
	 * "code": "firstName", "value": "bob" } ]
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testFiltering() {
		// create expected entities
		PersonJPACoreEntity person1 = new PersonJPACoreEntity("Alpha", "Filter-A");
		PersonJPACoreEntity person2 = new PersonJPACoreEntity("Beta", "Filter-A");
		PersonJPACoreEntity person3 = new PersonJPACoreEntity("Gamma", "Filter-B");
		List<PersonJPACoreEntity> expectedResults1 = Stream.of(person1, person2).collect(Collectors.toList());
		List<PersonJPACoreEntity> expectedResults2 = Stream.of(person3).collect(Collectors.toList());

		// save to db
		MultiOutput _newResponse1 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person1));
		assertThat(_newResponse1).isNotNull();
		MultiOutput _newResponse2 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person2));
		assertThat(_newResponse2).isNotNull();
		MultiOutput _newResponse3 = commandGateway.execute(
				CommandBuilder.withUri(PLATFORM_ROOT + "/person").setAction(Action._new).getCommand(),
				jsonUtils.convert(person3));
		assertThat(_newResponse3).isNotNull();

		// create a new view, get the refId
		Command cmdNewView = CommandBuilder.withUri(VIEW_DOMAIN_ROOT).setAction(Action._new).getCommand();
		MultiOutput respNewView = commandGateway.execute(cmdNewView, null);
		assertNotNull(respNewView);
		Long viewRefId = respNewView.findFirstParamOutputEndingWithPath(VIEW_ALIAS).getRootDomainId();
		assertNotNull(viewRefId);

		// retrieve "Filter-A" and validate results are equal to expected
		Command cmdFilterA = CommandBuilder.withUri(VIEW_DOMAIN_ROOT + ":" + viewRefId + "/people/_get").getCommand();
		MultiOutput respFilterA = commandGateway.execute(cmdFilterA,
				"{ \"filters\": [ { \"code\": \"lastName\", \"value\": \"Filter-A\" } ] }");
		assertThat(respFilterA).isNotNull();
		List<Param<PersonLineItem>> gridResultsFilterA = (List<Param<PersonLineItem>>) respFilterA.getSingleResult();
		List<PersonJPACoreEntity> actualResults1 = gridResultsFilterA.stream()
				.map(a -> new PersonJPACoreEntity(a.getState().getFirstName(), a.getState().getLastName()))
				.collect(Collectors.toList());
		assertThat(actualResults1).isNotNull();
		assertThat(CollectionUtils.isEqualCollection(actualResults1, expectedResults1)).isTrue();

		// retrieve "Filter-B" and validate results are equal to expected
		Command cmdFilterB = CommandBuilder.withUri(VIEW_DOMAIN_ROOT + ":" + viewRefId + "/people/_get").getCommand();
		MultiOutput respFilterB = commandGateway.execute(cmdFilterB,
				"{ \"filters\": [ { \"code\": \"lastName\", \"value\": \"Filter-B\" } ] }");
		assertThat(respFilterB).isNotNull();
		List<Param<PersonLineItem>> gridResultsFilterB = (List<Param<PersonLineItem>>) respFilterB.getSingleResult();
		List<PersonJPACoreEntity> actualResults2 = gridResultsFilterB.stream()
				.map(a -> new PersonJPACoreEntity(a.getState().getFirstName(), a.getState().getLastName()))
				.collect(Collectors.toList());
		assertThat(actualResults2).isNotNull();
		assertThat(CollectionUtils.isEqualCollection(actualResults2, expectedResults2)).isTrue();
	}
}