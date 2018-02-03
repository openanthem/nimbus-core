/**
 * 
 */
package com.antheminc.oss.nimbus.test.entity.sample.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Image;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Initialize;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;

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
}
