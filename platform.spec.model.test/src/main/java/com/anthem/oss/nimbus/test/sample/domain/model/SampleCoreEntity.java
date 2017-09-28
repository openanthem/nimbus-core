/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.domain.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="sample_core", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter
public class SampleCoreEntity extends IdString {

	private static final long serialVersionUID = 1L;

	private String attr_String;
	
	private int attr_int;
	private Integer attr_Integer;
	
	private Date attr_Date;
	private LocalDate attr_LocalDate;
	
	private List<String> attr_list_String;
	
	private String[] attr_array_String;
	
	private SampleCoreNestedEntity attr_NestedEntity;
	
	private List<SampleCoreNestedEntity> attr_list_1_NestedEntity;
	private List<SampleCoreNestedEntity> attr_list_2_NestedEntity;
	
	
	private String q1;
	private SampleCoreNestedEntity q1Level1;
	
	@ActivateConditional(when="state == 'Y'", targetPath="/../q2Level1")
	private String q2;
	private SampleCoreNestedEntity q2Level1;
}
