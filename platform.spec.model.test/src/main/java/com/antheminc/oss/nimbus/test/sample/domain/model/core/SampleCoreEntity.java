/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.domain.model.core;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Execution.Config;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Modal;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ActivateConditionals;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Audit;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ConfigConditional;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ConfigConditionals;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Content.Label;
import com.antheminc.oss.nimbus.core.domain.definition.extension.EnableConditional;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ParamContext;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Rule;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;

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
	
	@Audit(SampleCoreAuditEntry.class)
	private List<String> attr_list_String;
	
	@Audit(SampleCoreAuditEntry.class)
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
	
	@ConfigConditional(when="state == 'Y'", config=@Config(url="/p/sample_core_audit_history/_new?fn=_initEntity&target=/domainRootRefId&json=\"<!/id!>\""))
	private String conditional_config_attr;
	
	@ConfigConditional(config=@Config(url="/p/sample_core_audit_history/_new?fn=_initEntity&target=/domainRootRefId&json=\"<!/id!>\""))
	private List<String> conditional_config_attr_list_String;

	private String for_mapped_state_change_attr;
	
	@Label("Test Label A")
	private String label_a_en;
	
	@Label(value="Test Label B in French", locale="fr")
	private String label_b_fr;
	
	@Label(value="Test Label C in English")
	@Label(value="Test Label A in French", locale="fr")
	private String label_c_multiple;
	
	@ActivateConditional(when="state != null && state.nested2_attr_String_1 == 'Y' && state.nested2_attr_String_2 == 'Y'",targetPath={
			"/../q4Level1", "/../q4Level2"
	})
	private SampleCoreNested2_Entity q4;	
	private SampleCoreNested2_Entity q4Level1;
	private SampleCoreNested2_Entity q4Level2;
	
	@ConfigConditionals({
			@ConfigConditional(when="state == 'Y'", config=@Config(url="/p/sample_core_audit_history/_new?fn=_initEntity&target=/domainRootRefId&json=\"<!/id!>\"")),
			@ConfigConditional(when="state == 'N'", config=@Config(url="/p/sample_coreassociatedentity/_new?fn=_initEntity&target=/entityId&json=\"<!/id!>\""))
	})
	private String conditionals_config_attr;
	
	private SampleForm nc_form;
	
	@Model
	@Getter @Setter
	public static class SampleForm {
//		@ActivateConditional(when="state=='Y'", targetPath="../nc_nested_level1")
//		private String nc_attr1;
		
		private SampleNoConversionEntity nc_nested0_Details;
	}
	
	private SampleCoreValuesEntity sampleCoreValuesEntity;
	
	@Rule("rules/sample_increment")
	private String rule_param;
	
	@Rule("rules/sample_increment")
	private String rule_param2;
	
	private int rule_param_affectState;
	

	@EnableConditional(when="state == 'Y'", targetPath="../attr_enable_nested")
	private String attr_enable_trigger;
	
	private SampleCoreEnableEntity attr_enable_nested;
	
	@ParamContext(enabled = false, visible = false)
	private String for_set_param_context;
	
	@Modal(context = @ParamContext(enabled = true, visible = true))
	private MyModal myModal1;
	
	@Modal
	private MyModal myModal2;
	
	public static class MyModal {}
}