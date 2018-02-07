/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import com.antheminc.oss.nimbus.AbstractPersistableUnitTests;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
public class ZonedDateTimeConverterTest extends AbstractPersistableUnitTests {

	private static final ZonedDateTime K_ZDT = ZonedDateTime.of(2018, 2, 6, 6, 52, 0, 0, ZoneId.of("US/Pacific-New"));
    
	@SuppressWarnings("serial")
	@Getter @Setter
    private static class _TestEntityWithZDT extends IdString {
		
    	private ZonedDateTime userEnteredZonedDT;
    }
    
	@Test
	public void t1_fromZ() {
		// create entity to save
		_TestEntityWithZDT entity_dbSave = new _TestEntityWithZDT();
		entity_dbSave.setUserEnteredZonedDT(K_ZDT);
		
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
		
		// save to DB in zone 1
		mongo.save(entity_dbSave);
		System.out.println("Saved in DB with ID: "+entity_dbSave.getId());
		
		_TestEntityWithZDT entity_dbRead = mongo.findById(entity_dbSave.getId(), _TestEntityWithZDT.class);
		
		assertNull(entity_dbRead.getUserEnteredZonedDT());
	}
	
}
