/**
 * 
 */
package com.anthem.nimbus.platform.web.flow.login;

import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Link;
import com.anthem.oss.nimbus.core.domain.definition.extension.Content.Label;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Paragraph;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar.Meda
 *
 */
@Model @Getter @Setter
public class VSLoginFooter {

	private String appVersion;
	
	private String appCopyright;

	private String appPrivacy;
	
	private String appTou;
	
	private String appSslCert;
	
}
