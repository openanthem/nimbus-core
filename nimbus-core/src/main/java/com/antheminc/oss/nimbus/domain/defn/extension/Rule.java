/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;

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
 * @author Soham Chakravarti, Tony Lopez
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

	int order() default Event.DEFAULT_ORDER_NUMBER;
}
