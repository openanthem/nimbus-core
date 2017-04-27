/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
abstract public class CommandElement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public enum Type {
		
		ClientAlias("ClientAlias"),
		ClientOrgAlias("ClientOrgAlias"),
		AppAlias("AppAlias"),
		PlatformMarker("PlatformMarker"),
		DomainAlias("DomainAlias"),
		ProcessAlias("ProcessAlias"),
		ParamName("ParamName");
		
		final private String desc;
		
		private Type(String desc) {
			this.desc = desc;
		}
		
		public String getDesc() {
            return desc;
		}
		
		public static Type findByDesc(String desc) {
			for(Type a : values()) {
                if(a.getDesc().equals(desc)) return a;
			}
			return null;			
		}
		
		public static final List<Type> allowedRecursive = Arrays.asList(DomainAlias);
	}
	
	private Type type;
	
	private String alias;

	private String refId;	
	
	
	abstract public void detachChildElements();
	
	public void shallowCopy(CommandElement cloned) {
		cloned.setType(getType());
		cloned.setAlias(getAlias());
		cloned.setRefId(getRefId());
	}
	
	public boolean hasRefId() {
		return StringUtils.isNotEmpty(getRefId());
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
			String refId = StringUtils.substring(uri, i+1);
			setAlias(alias);
			setRefId(refId);
		}
	}

}
