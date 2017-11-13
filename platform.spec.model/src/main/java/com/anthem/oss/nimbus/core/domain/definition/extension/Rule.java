/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;

/**
 * <p>Rule allows its decorated field a mechanism for triggering one or more rule definitions 
 * during its OnStateLoad and OnStateChange events.</p> 
 * <pre>
 * &#64;Domain(value="sample_rule_entity", includeListeners={ListenerType.persistence})
 * &#64;Repo(Database.rep_mongodb)
 * &#64;Getter &#64;Setter
 * public class SampleRuleEntity {
 * 
 *   // Execute the rule at "rules/sample_increment" during the OnStateLoad and 
 *   // OnStateChange events of ruleParam.
 *   &#64;Rule("rules/sample_increment")
 *   private String rule_param;
 * }
 * </pre>
 * <br>
 * <p>By default, the framework provides support for firing all rules for a given domain entity. 
 * That is, for the <b>SampleRuleEntity.java</b> above we might have a rule file defined as 
 * <b>sample_rule_entity.drl</b> which will be automatically fired by naming convention.</p>
 * <p>For cases where additional configuration for other rules is needed, @Rule can be used.</p>
 *
 * @author Soham Chakravarti, Tony Lopez (AF42192)
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@OnStateLoad @OnStateChange
public @interface Rule {

	/**
	 * rule file path(s) to execute 
	 */
	String[] value();
	
}
