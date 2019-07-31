/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.AbstractPersistableUnitTests;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZonedDateTimeConverterTest extends AbstractPersistableUnitTests {

	private static final ZonedDateTime K_ZDT = ZonedDateTime.of(2018, 2, 6, 6, 52, 0, 0, ZoneId.of("US/Pacific-New"));
    
	@SuppressWarnings("serial")
	@Getter @Setter
    private static class _TestEntityWithZDT extends IdLong {
		
    	private ZonedDateTime userEnteredZonedDT;
    }
    
	@Test
	public void t1_fromZ() {
		// create entity to save
		_TestEntityWithZDT entity_dbSave = new _TestEntityWithZDT();
		entity_dbSave.setUserEnteredZonedDT(K_ZDT);
		entity_dbSave.setId(new Random().nextLong());
		
		// save to DB in zone 1
		mongo.save(entity_dbSave);
		System.out.println("Saved in DB with ID: "+entity_dbSave.getId());
		
		//& read back in another JVM with different zone
		//System.setProperty("user.timezone", K_READ_JVM_ZONE_ID.getId());
		//assertEquals(K_READ_JVM_ZONE_ID, ZoneId.systemDefault());
		
		_TestEntityWithZDT entity_dbRead = mongo.findById(entity_dbSave.getId(), _TestEntityWithZDT.class);
		
		assertEquals(K_ZDT, entity_dbRead.getUserEnteredZonedDT());
	}
	
	@Test
	public void t2_null() {
		_TestEntityWithZDT entity_dbSave = new _TestEntityWithZDT();
		entity_dbSave.setUserEnteredZonedDT(null);
		entity_dbSave.setId(new Random().nextLong());
		// save to DB in zone 1
		mongo.save(entity_dbSave);
		System.out.println("Saved in DB with ID: "+entity_dbSave.getId());
		
		_TestEntityWithZDT entity_dbRead = mongo.findById(entity_dbSave.getId(), _TestEntityWithZDT.class);
		
		assertNull(entity_dbRead.getUserEnteredZonedDT());
	}
	
}
