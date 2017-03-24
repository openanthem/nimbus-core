package com.anthem.oss.nimbus.core.repo;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
import com.anthem.oss.nimbus.core.config.Neo4jConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientEntityRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientRepository;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(BPMEngineConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Profile("test")
public class ClientEntityRepositoryTest {

	@Autowired ClientRepository cRep;
	
	@Autowired ClientEntityRepository ceRep;
	
	@Test
	public void getClientByCode(){
		Client c = cRep.findOne(cRep.findByCode("antm").getId());
		System.out.println(c);
	}
	
	@Test
	public void getAllClients(){
		PageRequest p = new PageRequest(0,10);
		Page<ClientEntity> ce = ceRep.findAll(p);
		Assert.assertNotNull(ce);
	}
	
	@Test
	public void getClientByCode2(){
		Client c = cRep.findOne(cRep.findByCode("agp").getId());
		System.out.println(c);
	}
	
//	@Test
//	public void getAllClients() {
//		PageRequest pageReq = new PageRequest(0, Integer.MAX_VALUE);
//		Page<ClientEntity> ce = ceRep.findAll(pageReq);
//		System.out.println(ce);
//	}
	
}
