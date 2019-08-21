/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.antheminc.oss.nimbus.domain.defn.Model;

import lombok.Data;

/**
 * @author Soham.Chakravarti
 *
 */

@Model
@Embeddable
@Data
public class RefIdMapMainEmbeddedPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key1;
	
	private int key2;
}
