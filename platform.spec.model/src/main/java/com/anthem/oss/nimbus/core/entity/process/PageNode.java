/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.process;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.entity.Findable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Model
@Getter @Setter
public class PageNode implements Findable<PageNode>, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String pageId;
	
	private String pageName;
		
	@Override
	public String toString() {
		return new StringBuilder(id)
				.append(".").append(getPageId()).append(".").append(getPageName())
				.toString();
	}
	
	@Override
	public boolean isFound(PageNode by) {
		return equals(by);
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
