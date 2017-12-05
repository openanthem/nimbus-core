package com.antheminc.oss.nimbus.core.entity;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;

import lombok.Data;

/**
 * @author Rakesh Patel
 *
 */
@Data
public abstract class SearchCriteria<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private T where;
	private T orderby;
	private String aggregateCriteria;
	private ProjectCriteria projectCriteria;
	private String fetch;
	
	private String responseConverter;
	
	public abstract void validate(ExecutionContext executionContext);
	
	@Data
	public static class ProjectCriteria implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String alias;
		private Map<String, String> mapsTo;
		
	}
	
	@Data
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

	@Data
	public static class ExampleSearchCriteria<T> extends SearchCriteria<T> {

		private static final long serialVersionUID = 1L;

		@Override
		public void validate(ExecutionContext executionContext) {
			String[] whereClause = executionContext.getCommandMessage().getCommand().getRequestParams().get("where");
			if(ArrayUtils.isNotEmpty(whereClause)) {
				throw new FrameworkRuntimeException("ExampleSearchCriteria does not support the where request parameter in command: "+executionContext.getCommandMessage());
			}
		}
		
	}

	@Data
	public static class LookupSearchCriteria extends SearchCriteria<String> {

		private static final long serialVersionUID = 1L;
		
		@Override
		public void validate(ExecutionContext executionContext) {
			String rawPayload = executionContext.getCommandMessage().getRawPayload();
			
			if(StringUtils.isNotBlank(rawPayload)){
				throw new FrameworkRuntimeException("LookupSearchCriteria does not support the raw payload in command: "+executionContext.getCommandMessage());
			}
			
			String[] projectionAlias = executionContext.getCommandMessage().getCommand().getRequestParams().get("projection.alias");
			String[] projectionMapping = executionContext.getCommandMessage().getCommand().getRequestParams().get("projection.mapsTo");
			
			if(ArrayUtils.isNotEmpty(projectionAlias) && ArrayUtils.isNotEmpty(projectionMapping)){
				throw new FrameworkRuntimeException("LookupSearchCriteria does not support both projection.alias and projection.mapsTo in command: "+executionContext.getCommandMessage());
			}
			
		}
		
	}
	
}


