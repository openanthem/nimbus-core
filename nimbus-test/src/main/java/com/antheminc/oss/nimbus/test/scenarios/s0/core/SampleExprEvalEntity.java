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
/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import java.time.LocalDate;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Image;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Initialize;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;
import com.antheminc.oss.nimbus.domain.defn.extension.ExpressionConditional;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("sample_expr")
@Getter @Setter
public class SampleExprEvalEntity {

	public static final String K_TRIGGER_URL = "/p/_anotherdomain_${test.url.code}/p1/<!../p1_val!>";
	
	public static final String K_LINK_URL = "${test.url.link}";
	public static final String K_IMAGE_URL = "${test.url.image}";
	public static final String K_INITIALIZE_URL = "${test.url.initialize}";
	
	private String p1_val;
	
	@Config(url=K_TRIGGER_URL)
	private String trigger;
	
	@Link(url=K_LINK_URL)
	private String link;
	
	@Image(imgSrc=K_IMAGE_URL, alias=K_LINK_URL)
	private String image;
	
	@Initialize(alias=K_INITIALIZE_URL)
	@Image(imgSrc=K_IMAGE_URL, alias=K_LINK_URL)
	private String initialize;
	
	/* expression */
	public static final String K_VAL_B_WHEN = "whenB";
	public static final String K_VAL_B_THEN = "thenB";
	
	//@ExpressionConditional(when="state == "+K_VAL_A_WHEN, then="findParamByPath('../exprThen').setState("+K_VAL_A_THEN+")")
	@ExpressionConditional(
			when="state == '"+K_VAL_B_WHEN+"'", 
			then="findParamByPath('../exprThen').setState('"+K_VAL_B_THEN+"')")
	
	private String exprWhenTrigger;
	
	private String exprThen;
	
	
	@ExpressionConditional(when="onLoad() && state==null", then="setState( T(java.time.LocalDate).now() )")
	private LocalDate initDateOnLoad;
	
	private String triggerMessageUpdate;
	
//	@RepoOverride listenerOverride= -- new TransientConditionalParam
//	private SampleCoreEnableEntity vvar;
//	
//	private class SampleCoreEnableEntity optional {
//		
//		@OnChange().
//		private String onchange;
//		
//		@Config("../attachPersistence()")
//		@Config("../update")
//		private String submit;
//	};
//	
//	
//	
}
