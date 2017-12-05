/**
 * 
 */
package com.anthem.nimbus.platform.web.flow.login;

import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Link;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.PageFooter;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.PageFooter.Property;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Content.Label;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Paragraph;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar.Meda
 *
 */
@Model @Getter @Setter
public class VSLoginFooter {

	@Paragraph @Label(value = "") @PageFooter(Property.VERSION)
	private String appVersion;
	
	@Paragraph @Label(value = "") @PageFooter(Property.COPYRIGHT)
	private String appCopyright;

	@Paragraph @Label(value = "") @PageFooter(Property.PRIVACY)
	private String appPrivacy;
	
	@Paragraph @Label(value = "") @PageFooter(Property.TOU)
	private String appTou;
	
	@Link(url="#", imgSrc="verisign.png") @PageFooter(Property.SSLCERT)
	private String appSslCert;
	
}
