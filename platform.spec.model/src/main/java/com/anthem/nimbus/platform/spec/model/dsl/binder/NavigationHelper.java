/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class NavigationHelper {
	
	private String currentPageId;
	
	private String nextPageId;
	
	private boolean navigateNext = true;
	
	private boolean currentPageLoaded = false;
	
	private List<String> activePages = new ArrayList<String>();
	
	
	/**
	 * 
	 * @param pageId
	 */
	public void addActivePage(String pageId){
		if(currentPageId != null && currentPageId.equals(pageId)){
			currentPageLoaded = true;
			if(!navigateNext && activePages.size() > 0){
				nextPageId = activePages.get(activePages.size() - 1);
				return;
			}
		}
		
		if(currentPageLoaded){
			if(navigateNext){
				nextPageId = pageId;
				return;
			}
		}
		activePages.add(pageId);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean navigationComplete(){
		return (nextPageId != null);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPageToReturn(){
		if(nextPageId != null){
			return nextPageId;
		}
		else if(activePages.size() > 0){
			return activePages.get(activePages.size() - 1);
		}
		return null;
	}
	
}
