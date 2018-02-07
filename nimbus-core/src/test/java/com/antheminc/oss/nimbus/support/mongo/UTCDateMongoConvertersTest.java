/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Test;

import com.antheminc.oss.nimbus.AbstractPersistableUnitTests;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
public class UTCDateMongoConvertersTest extends AbstractPersistableUnitTests {

	private static final ZonedDateTime K_ZDT = ZonedDateTime.of(2018, 2, 6, 6, 52, 0, 0, ZoneId.of("US/Pacific-New"));
	//private static final ZonedDateTime K_UTC = ZonedDateTime.of(2018, 2, 6, 14, 52, 0, 0, ZoneId.of("UTC"));
	
	@SuppressWarnings("serial")
	@Getter @Setter
    private static class _TestEntityWithDate extends IdString {
		
    	private Date userEnteredDate;
    }
    
	
	@Test
	public void t1_main() {
		// simulate that the date provided is in PST
		Date in = Date.from(K_ZDT.toInstant());
		
		_TestEntityWithDate toDb = new _TestEntityWithDate();
		toDb.setUserEnteredDate(in);
		
		mongo.save(toDb);
		
		_TestEntityWithDate fromDb = mongo.findById(toDb.getId(), _TestEntityWithDate.class);
		assertNotNull(fromDb);
		
		assertEquals(in, fromDb.getUserEnteredDate());
	}
	
	@Test
	public void t2_null() {
		_TestEntityWithDate toDb = new _TestEntityWithDate();
		toDb.setUserEnteredDate(null);
		
		mongo.save(toDb);
		
		_TestEntityWithDate fromDb = mongo.findById(toDb.getId(), _TestEntityWithDate.class);
		assertNotNull(fromDb);
		
		assertNull(fromDb.getUserEnteredDate());
	}
}
