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
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.ConfigPlaceholder;
import com.antheminc.oss.nimbus.domain.defn.Execution.DetourConfig;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@Model
@Getter @Setter
public class SampleConfigPlaceholders {

	private String p0;
	
	@ConfigPlaceholder(name = "foo", value = "<!#this!>/../p0")
	@Config(url = "<!foo!>/_process?fn=_set&value=p1-did-it")
	private String p1;
	
	@ConfigPlaceholder(name = "bar", value = "<!#this!>/../p0")
	@ConfigPlaceholder(name = "baz", value = "p2-did-it")
	@Config(url = "<!bar!>/_process?fn=_set&value=<!baz!>")
	private String p2;
	
	@Config(url = "<!foo!>/_process?fn=_set&value=p3-did-it")
	private String p3;
	
	@ConfigPlaceholder(name = "foo", value = "<!#this!>/../p0")
	@DetourConfig(main = @Config(url = "break things"), onException = @Config(url = "<!foo!>/_process?fn=_set&value=p4-did-it"))
	private String p4;
	
	@ConfigPlaceholder(name = "foo", value = "<!#this!>/../p0")
	@ConfigConditional(when = "true", config = {
		@Config(url = "<!foo!>/_process?fn=_set&value=p5-did-it")
	})
	private String p5;
	
	private String p6;
	
	private String[] p7;
	
	@ConfigPlaceholder(name = "foo", value = "<!#this!>/../p<!col!>")
	@Config(url = "<!#this!>/../p7/_replace?rawPayload=[\"0\"]")
	@Config(url = "<!foo!>/_process?fn=_set&value=p8-did-it", col="<!/../p7!>")
	private String p8;
}
