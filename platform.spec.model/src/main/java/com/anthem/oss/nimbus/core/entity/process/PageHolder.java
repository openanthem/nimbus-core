/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class PageHolder implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<PageNode> pageReferences = new ArrayList<PageNode>();
	
//	private PageNode root;
//	
//	private PageNode lastPageAdded;
	
	private String currentPageId;
	
	
	/**
	 * 
	 * @param pageId
	 */
	public void addPage(String id, String pageId, String pageName){
		PageNode page = new PageNode();
		page.setPageName(pageName);
		page.setPageId(pageId);
		page.setId(id);
		
		for(int i = 0; i < pageReferences.size(); i++){
			PageNode pn = pageReferences.get(i);
			if(pn.getPageName().equals(pageName)){
				pageReferences.remove(i);
				break;
			}
			
		}
		pageReferences.add(page);
	}
	
	/**
	 * 
	 * @return
	 */
	public PageNode getRoot(){
		if(pageReferences.size() > 0)
			return pageReferences.get(0);
		return null;		
	}
	
	/**
	 * 
	 * @return
	 */
	public PageNode getLeaf(){
		int size = pageReferences.size();
		if(size > 0)
			return pageReferences.get(size-1);
		return null;		
	}	
	
	/**
	 * 
	 * @return
	 */
	public PageNode getPreviousPage(String currentPageId){
		if(currentPageId == null)
			return getRoot();
		PageNode currentPage = new PageNode();
		currentPage.setId(currentPageId);
		int index = pageReferences.indexOf(currentPage);
		if(index != -1 && index > 0){
			return pageReferences.get(index-1);
		}
		return getRoot();
	}
	
	/**
	 * 
	 * @return
	 */
	public PageNode getNextPage(String currentPageId){
		if(currentPageId == null)
			return getLeaf();		
		PageNode currentPage = new PageNode();
		currentPage.setId(currentPageId);
		int index = pageReferences.indexOf(currentPage);
		int listSize = pageReferences.size();
		if(index != -1 && index < (listSize-1)){
			return pageReferences.get(index+1);
		}
		return getLeaf();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public PageNode getPageById(String id){
		if(id == null)
			return null;
		PageNode currentPage = new PageNode();
		currentPage.setId(id);	
		int index = pageReferences.indexOf(currentPage);
		if(index != -1){
			return pageReferences.get(index);
		}
		return null;
	}
}
