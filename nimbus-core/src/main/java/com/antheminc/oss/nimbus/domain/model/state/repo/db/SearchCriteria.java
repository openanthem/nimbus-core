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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import static com.antheminc.oss.nimbus.domain.defn.Constants.SEARCH_REQ_PROJECT_ALIAS_MARKER;
import static com.antheminc.oss.nimbus.domain.defn.Constants.SEARCH_REQ_PROJECT_MAPPING_MARKER;
import static com.antheminc.oss.nimbus.domain.defn.Constants.SEARCH_REQ_WHERE_MARKER;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter
public abstract class SearchCriteria<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	protected T where;
	protected T orderby;
	protected String aggregateCriteria;
	protected ProjectCriteria projectCriteria;
	protected String fetch;
	protected Pageable pageRequest;
	
	private Command cmd;
	
	@Getter @Setter
	public static class PageFilter implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private List<FilterCriteria> filters;
		
		@Getter @Setter
		public static class FilterCriteria implements Serializable {
			private static final long serialVersionUID = 1L;
			
			private String code;
			private String value; // client to send dates in ISODate even for filter (uuuu-MM-ddTHH:mm:ss:SSS'T')
		}
		
	}
	
	public abstract void validate(ExecutionContext executionContext);
	
	@Getter @Setter
	public static class ProjectCriteria implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String alias;
		private Map<String, String> mapsTo;
		
	}
	
	@Getter @Setter
	public static class QuerySearchCriteria extends SearchCriteria<String> {

		private static final long serialVersionUID = 1L;

		@Override
		public void validate(ExecutionContext executionContext) {
			String rawPayload = executionContext.getCommandMessage().getRawPayload();
			
			// TODO uncomment once we know the rawpayload is not passed from once config to the other when not needed
//			if(StringUtils.isNotBlank(rawPayload)){
//				throw new FrameworkRuntimeException("QuerySearchCriteria does not support the raw paylaod in command: "+executionContext.getCommandMessage()); 
//			}
		}
		
	}

	@Getter @Setter
	public static class ExampleSearchCriteria<T> extends SearchCriteria<T> {

		private static final long serialVersionUID = 1L;

		@Override
		public void validate(ExecutionContext executionContext) {
			String[] whereClause = executionContext.getCommandMessage().getCommand().getRequestParams().get(SEARCH_REQ_WHERE_MARKER.code);
			if(ArrayUtils.isNotEmpty(whereClause)) {
				throw new FrameworkRuntimeException("ExampleSearchCriteria does not support the where request parameter in command: "+executionContext.getCommandMessage());
			}
		}
		
	}

	@Getter @Setter
	public static class LookupSearchCriteria extends SearchCriteria<String> {

		private static final long serialVersionUID = 1L;
		
		@Override
		public void validate(ExecutionContext executionContext) {
			String rawPayload = executionContext.getCommandMessage().getRawPayload();
			
			if(StringUtils.isNotBlank(rawPayload)){
				throw new FrameworkRuntimeException("LookupSearchCriteria does not support the raw payload in command: "+executionContext.getCommandMessage());
			}
			
			String[] projectionAlias = executionContext.getCommandMessage().getCommand().getRequestParams().get(SEARCH_REQ_PROJECT_ALIAS_MARKER.code);
			String[] projectionMapping = executionContext.getCommandMessage().getCommand().getRequestParams().get(SEARCH_REQ_PROJECT_MAPPING_MARKER.code);
			
			if(ArrayUtils.isNotEmpty(projectionAlias) && ArrayUtils.isNotEmpty(projectionMapping)){
				throw new FrameworkRuntimeException("LookupSearchCriteria does not support both projection.alias and projection.mapsTo in command: "+executionContext.getCommandMessage());
			}
			
		}
		
	}
	
}


