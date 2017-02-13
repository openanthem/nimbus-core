/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.process;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class PageNode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String pageId;
	
	private String pageName;
	
//	private PageNode parent;
//	
//	private PageNode child;
	
	@Override
	public String toString() {
		StringBuilder page = new StringBuilder(id);
		page.append(".").append(pageId).append(".").append(pageName);
		return page.toString();
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PageNode){
			PageNode pn = (PageNode)obj;
			return id.equals(pn.getId());
		}
		return false;
	}
	
}
