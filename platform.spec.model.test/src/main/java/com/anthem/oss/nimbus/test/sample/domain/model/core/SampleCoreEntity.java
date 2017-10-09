/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.domain.model.core;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditionals;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;
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

	@Audit(SampleCoreAuditEntry.class)
	private String audit_String;

	@Audit(SampleCoreAuditEntry.class)
	private Integer audit_Integer;
	
	@Audit(SampleCoreAuditEntry.class)
	private String unmapped_String;
	
	private Date attr_Date;
	private LocalDate attr_LocalDate;
	
	private List<String> attr_list_String;
	
	private String[] attr_array_String;
	
	private SampleCoreNestedEntity attr_NestedEntity;
	
	private List<SampleCoreNestedEntity> attr_list_1_NestedEntity;
	private List<SampleCoreNestedEntity> attr_list_2_NestedEntity;
	
	private String unmapped_attr;
	
	private String q1;
	private SampleCoreNestedEntity q1Level1;
	
	@ActivateConditional(when="state == 'Y'", targetPath="/../q2Level1")
	private String q2;
	private SampleCoreNestedEntity q2Level1;
	
	@ActivateConditionals({
		@ActivateConditional(when="state == 'A'", targetPath="/../q3Level1"),
		@ActivateConditional(when="state == 'B'", targetPath="/../q3Level2")
	})
	private String q3;
	private SampleCoreNestedEntity q3Level1;
	private SampleCoreNestedEntity q3Level2;
	
	private SampleCoreLevel1_Entity level1;
}