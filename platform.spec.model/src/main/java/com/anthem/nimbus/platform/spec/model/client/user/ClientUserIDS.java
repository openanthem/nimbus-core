/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.client.user;

import com.anthem.nimbus.platform.spec.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
class ClientUserIDS extends AbstractModel.IdLong{
	
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
