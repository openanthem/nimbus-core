/**
 * 
 */
package com.antheminc.oss.nimbus.platform.web.flow.login;

import com.antheminc.oss.nimbus.core.domain.definition.Model;

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