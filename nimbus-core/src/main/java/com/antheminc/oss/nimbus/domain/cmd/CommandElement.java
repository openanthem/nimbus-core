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
package com.antheminc.oss.nimbus.domain.cmd;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.Constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
abstract public class CommandElement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter @RequiredArgsConstructor
	public enum Type {
		
		ClientAlias("ClientAlias", false),
		ClientOrgAlias("ClientOrgAlias", false),
		AppAlias("AppAlias", false),
		PlatformMarker("PlatformMarker", false),
		DomainAlias("DomainAlias", true),
		ProcessAlias("ProcessAlias", false),
		ParamName("ParamName", false);
		
		final private String desc;
		final private boolean recursive;
		
		public static Type findByDesc(String desc) {
			for(Type a : values()) {
                if(a.getDesc().equals(desc)) return a;
			}
			return null;
		}
	}
	
	private Type type;
	
	private String alias;

	private Long refId;	
	
	
	abstract public void detachChildElements();
	
	public boolean hasRefId() {
		return getRefId() != null;
	}
	
	public String getAliasUri() {
		StringBuilder sb = new StringBuilder();
		
		return sb.append(Constants.SEPARATOR_URI.code)
				.append(getAlias())
		        .toString();	
	}
	
	public String getUri(){
		StringBuilder sb = new StringBuilder();
		
		return hasRefId() 
			? sb.append(getAliasUri()).append(Constants.SEPARATOR_URI_VALUE.code+getRefId())
					.toString()
			: getAliasUri();
	}

	public void setUri(String uri) {
		int i = StringUtils.indexOf(uri, Constants.SEPARATOR_URI_VALUE.code);
		if(i == -1) {
			setAlias(uri);
		}
		else {
			String alias = StringUtils.substring(uri, 0, i);
			Long refId = Long.valueOf(StringUtils.substring(uri, i+1));
			setAlias(alias);
			setRefId(refId);
		}
	}

}
