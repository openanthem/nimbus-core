/**
 * 
 */
package com.antheminc.oss.nimbus.platform.web.flow.login;

import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Hints;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Hints.AlignOptions;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Link;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.PageHeader;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.PageHeader.Property;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Paragraph;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Content.Label;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar Meda
 *
 */
@Model @Getter @Setter
public class VSLoginHeader {
	
	@Link(url="/ui/", imgSrc="anthem-rev.svg", cssClass="bruce") @Hints(AlignOptions.Left) @PageHeader(Property.LOGO)
	private String linkHomeLogo;
	
	@Paragraph @Label(value = "") @PageHeader(Property.APPTITLE)
	private String linkHomeTitle;
	
}
