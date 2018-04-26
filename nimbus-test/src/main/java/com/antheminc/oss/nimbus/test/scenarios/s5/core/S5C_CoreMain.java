/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s5.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample core entity to illustrate bulk processing of:<br />
 * <pre>Collections</pre>
 * 
 * @author Soham Chakravarti
 */
@SuppressWarnings("serial")
@Domain(value="s5c_main")
@Getter @Setter @ToString
public class S5C_CoreMain extends IdString {

}
