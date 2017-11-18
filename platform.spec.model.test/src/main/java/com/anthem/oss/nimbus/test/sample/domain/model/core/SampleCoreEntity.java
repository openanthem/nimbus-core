/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.domain.model.core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Modal;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Radio;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.TextBox;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditionals;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;
import com.anthem.oss.nimbus.core.domain.definition.extension.ConfigConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.ConfigConditionals;
import com.anthem.oss.nimbus.core.domain.definition.extension.Content.Label;
import com.anthem.oss.nimbus.core.domain.definition.extension.ParamContext;
import com.anthem.oss.nimbus.core.domain.definition.extension.Rule;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional.Condition;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditionals;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
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
	
	private StatusForm statusForm;
	
	@Model
	@Getter @Setter 
	public static class StatusForm {
		
		@ValuesConditionals({
			@ValuesConditional(target = "../statusReason", condition = { 
					@Condition(when = "state=='A'", then = @Values(SR_A.class)),
					@Condition(when = "state=='B'", then = @Values(SR_B.class)),
				}
			),
			@ValuesConditional(target = "../statusReason2", condition = { 
					@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				}
			)
		})
		@TextBox(postEventOnChange = true)
		private String status;
		
		@ValuesConditional(target = "../statusReason", condition = { 
				@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				@Condition(when = "state=='A'", then = @Values(SR_B.class)),
			},
			exclusive = false	
		)
		@TextBox(postEventOnChange = true)
		private String allowOverrideStatus;
		
		@ValuesConditional(target = "../statusReason", condition = { 
				@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				@Condition(when = "state=='A'", then = @Values(SR_B.class)),
			}
		)
		@TextBox(postEventOnChange = true)
		private String disallowOverrideStatus;
		
		@Radio
		@Values(SR_DEFAULT.class)
		private String statusReason;
		
		@Radio
		private String statusReason2;
		
		public static class SR_DEFAULT implements Source {
			@Override
			public List<ParamValue> getValues(String paramCode) {
				final List<ParamValue> values = new ArrayList<>();
				values.add(new ParamValue("A1", "A1"));
				values.add(new ParamValue("B1", "B1"));
				return values;
			}
		}
		public static class SR_A implements Source {
			@Override
			public List<ParamValue> getValues(String paramCode) {
				final List<ParamValue> values = new ArrayList<>();
				values.add(new ParamValue("A1", "A1"));
				return values;
			}
		}
		public static class SR_B implements Source {
			@Override
			public List<ParamValue> getValues(String paramCode) {
				final List<ParamValue> values = new ArrayList<>();
				values.add(new ParamValue("B1", "B1"));
				return values;
			}
		}
	}
	
	@Rule("rules/sample_increment")
	private String rule_param;
	
	@Rule("rules/sample_increment")
	private String rule_param2;
	
	private int rule_param_affectState;
	
	@ParamContext(enabled = false, visible = false)
	private String for_set_param_context;
	
	@Modal
	@ParamContext(enabled = true, visible = true)
	private MyModal myModal1;
	
	@Modal
	private MyModal myModal2;
	
	public static class MyModal {}
}