/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.entity;

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
	
	private String domainAlias;
	private List<Criteria> criteria;
	private List<EntityAssociation> associatedEntities;
	private String associationFrom;
	private String associationTo;
	private String associationStartWith;
	private String projectionFields;
	private String associationAlias;
	private boolean unwind;
	
	@Data
	public static class Criteria implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String key;
		private String value;
	}

}
