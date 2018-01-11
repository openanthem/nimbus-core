/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.access;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public class Permission extends IdString {
	
    private static final long serialVersionUID = 1L;

    
    
	public static final class Access extends Permission {
		
		private static final long serialVersionUID = 1L;
		
		public static final String CODE = "ACCESS";
		
		
		public Access() {
			super("ACCESS");
		}
	}
	
	
	
	public static final class Create extends Permission {
		
		private static final long serialVersionUID = 1L;
		
	    public static final String CODE = "CREATE";
		

		public Create() {
			super("CREATE");
		}
	}
	
	
	
	public static final class Read extends Permission {
		
		private static final long serialVersionUID = 1L;
		
	    public static final String CODE = "READ";
		
	    
		public Read() {
			super("READ");
		}
	}
	

	
	public static final class Update extends Permission {
		
		private static final long serialVersionUID = 1L;

		public static final String CODE = "UPDATE";
		
		
		public Update() {
			super("UPDATE");
		}
	}
	
	
	
	public static final class Delete extends Permission {
        
		private static final long serialVersionUID = 1L;
	
		public static final String CODE = "DELETE";
		

		public Delete() {
			super("DELETE");
		}
	}
	
	
	public Permission() {}
	
	public Permission(String code) {
		this.code = code;
	}
	

	private String code;
	
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Permission)) return false;
		
		Permission other = (Permission) obj;
		
		if(this == other) return true;
		
		if(StringUtils.equals(getCode(), other.getCode())) return true;
		
		return false;
	}
	
}
