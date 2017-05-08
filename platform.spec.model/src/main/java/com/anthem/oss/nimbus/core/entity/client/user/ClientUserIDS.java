/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client.user;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
class ClientUserIDS extends AbstractEntity.IdString {
	
	private static final long serialVersionUID = 1L;
	
	
	private String loginName;
	
	private String idsGuid;
	
	private String source;
	
	
	public ClientUserIDS(){
		
	}
	
	public ClientUserIDS(String source, String loginName, String guid){
		this.source = source;
		this.loginName = loginName;
		this.idsGuid = guid;
	}
	
}
