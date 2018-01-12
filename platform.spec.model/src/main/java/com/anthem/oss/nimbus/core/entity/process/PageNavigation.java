/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.process;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jayant Chaudhuri
 *
 */
@Model
@Getter @Setter @ToString
public class PageNavigation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<PageNode> pageNodes;
	
	@JsonIgnore @Transient
	final private CollectionsTemplate<List<PageNode>, PageNode> template = CollectionsTemplate.array(()->getPageNodes(), s->setPageNodes(s));
	
	private String currentPageId;
	
//	public void addPage(String id, String pageId, String pageName){
//		PageNode page = new PageNode();
//		page.setPageName(pageName);
//		page.setPageId(pageId);
//		page.setId(id);
//		
//		for(int i = 0; i < pageReferences.size(); i++){
//			PageNode pn = pageReferences.get(i);
//			if(pn.getPageName().equals(pageName)){
//				pageReferences.remove(i);
//				break;
//			}
//			
//		}
//		pageReferences.add(page);
//	}
	
	public PageNode getRoot() {
		if(template.size() > 0)
			return template.getElem(0);
		return null;		
	}
	
	public PageNode getLeaf(){
		int size = template.size();
		if(size > 0)
			return template.getElem(size-1);
		return null;		
	}	
	
	public PageNode getPreviousPage(String currentPageId){
		if(currentPageId == null)
			return getRoot();
		
		PageNode currentPage = new PageNode();
		currentPage.setId(currentPageId);
		
		int index = template.lastIndexOf(currentPage);
		if(index != -1 && index > 0){
			return template.getElem(index-1);
		}
		return getRoot();
	}
	
	public PageNode getNextPage(String currentPageId){
		if(currentPageId == null)
			return getLeaf();
		
		PageNode currentPage = new PageNode();
		currentPage.setId(currentPageId);
		
		int index = template.lastIndexOf(currentPage);
		int listSize = template.size();
		if(index != -1 && index < (listSize-1)){
			return template.getElem(index+1);
		}
		return getLeaf();
	}
	
	public PageNode getPageById(String id){
		if(id == null)
			return null;
		
		PageNode currentPage = new PageNode();
		currentPage.setId(id);	
		
		int index = template.lastIndexOf(currentPage);
		if(index != -1){
			return template.getElem(index);
		}
		return null;
	}
}
