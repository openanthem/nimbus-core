package com.antheminc.oss.nimbus.core.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author Rakesh Patel
 *
 */
@Data
public class EntityAssociation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String domainAlias;
	List<Criteria> criteria;
	List<EntityAssociation> associatedEntities;
	String associationFrom;
	String associationTo;
	String associationStartWith;
	String projectionFields;
	String associationAlias;
	boolean unwind;
	
	@Data
	public static class Criteria implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String key;
		private String value;
	}

}
