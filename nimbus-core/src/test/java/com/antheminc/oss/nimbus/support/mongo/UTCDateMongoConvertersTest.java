/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.AbstractPersistableUnitTests;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UTCDateMongoConvertersTest extends AbstractPersistableUnitTests {

	private static final ZonedDateTime K_ZDT = ZonedDateTime.of(2018, 2, 6, 6, 52, 0, 0, ZoneId.of("US/Pacific-New"));
	//private static final ZonedDateTime K_UTC = ZonedDateTime.of(2018, 2, 6, 14, 52, 0, 0, ZoneId.of("UTC"));
	
	@SuppressWarnings("serial")
	@Getter @Setter
    private static class _TestEntityWithDate extends IdString {
		
    	private Date userEntered;
    }
    
	@SuppressWarnings("serial")
	@Getter @Setter
    private static class _TestEntityWithLocalDateTime extends IdString {
		
    	private LocalDateTime userEntered;
    }
    
	@SuppressWarnings("serial")
	@Getter @Setter
    private static class _TestEntityWithLocalDate extends IdString {
		
    	private LocalDate userEntered;
    }
    
	
	@Test
	public void t1_date_main() {
		// simulate that the date provided is in PST
		Date in = Date.from(K_ZDT.toInstant());
		
		_TestEntityWithDate toDb = new _TestEntityWithDate();
		toDb.setUserEntered(in);
		
		mongo.save(toDb);
		
		_TestEntityWithDate fromDb = mongo.findById(toDb.getId(), _TestEntityWithDate.class);
		assertNotNull(fromDb);
		
		assertEquals(in, fromDb.getUserEntered());
	}
	
	@Test
	public void t2_date_null() {
		_TestEntityWithDate toDb = new _TestEntityWithDate();
		toDb.setUserEntered(null);
		
		mongo.save(toDb);
		
		_TestEntityWithDate fromDb = mongo.findById(toDb.getId(), _TestEntityWithDate.class);
		assertNotNull(fromDb);
		
		assertNull(fromDb.getUserEntered());
	}
	
	@Test
	public void t3_localdatetime_main() {
		// simulate that the date provided is in PST
		LocalDateTime in = K_ZDT.toLocalDateTime();
		
		_TestEntityWithLocalDateTime toDb = new _TestEntityWithLocalDateTime();
		toDb.setUserEntered(in);
		
		mongo.save(toDb);
		
		_TestEntityWithLocalDateTime fromDb = mongo.findById(toDb.getId(), _TestEntityWithLocalDateTime.class);
		assertNotNull(fromDb);
		
		assertEquals(in, fromDb.getUserEntered());
	}
	
	@Test
	public void t4_localdatetime_null() {
		_TestEntityWithLocalDateTime toDb = new _TestEntityWithLocalDateTime();
		toDb.setUserEntered(null);
		
		mongo.save(toDb);
		
		_TestEntityWithLocalDateTime fromDb = mongo.findById(toDb.getId(), _TestEntityWithLocalDateTime.class);
		assertNotNull(fromDb);
		
		assertNull(fromDb.getUserEntered());
	}	
	
	@Test
	public void t5_localdate_main() {
		// simulate that the date provided is in PST
		LocalDate in = K_ZDT.toLocalDate();
		
		_TestEntityWithLocalDate toDb = new _TestEntityWithLocalDate();
		toDb.setUserEntered(in);
		
		mongo.save(toDb);
		
		_TestEntityWithLocalDate fromDb = mongo.findById(toDb.getId(), _TestEntityWithLocalDate.class);
		assertNotNull(fromDb);
		
		assertEquals(in, fromDb.getUserEntered());
	}
	
	@Test
	public void t6_localdate_null() {
		_TestEntityWithLocalDate toDb = new _TestEntityWithLocalDate();
		toDb.setUserEntered(null);
		
		mongo.save(toDb);
		
		_TestEntityWithLocalDate fromDb = mongo.findById(toDb.getId(), _TestEntityWithLocalDate.class);
		assertNotNull(fromDb);
		
		assertNull(fromDb.getUserEntered());
	}		
}
