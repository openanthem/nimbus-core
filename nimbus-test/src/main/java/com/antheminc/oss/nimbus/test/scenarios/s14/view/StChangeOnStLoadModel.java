package com.antheminc.oss.nimbus.test.scenarios.s14.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.event.EventType;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Script;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;
import com.antheminc.oss.nimbus.test.scenarios.s14.core.StChangeOnStLoadCore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="stchangeonstloadview")
@Repo(Database.rep_none)
@Getter @Setter
@Type(StChangeOnStLoadCore.class)
public class StChangeOnStLoadModel extends IdLong{
	private static final long serialVersionUID = 1L;
	
	private SubModel inline_para;
	

	@Model
	@Getter @Setter
	public static class SubModel{
		@Script(type=Script.Type.SPEL_INLINE, value="findParamByPath('/../parameter3').setState('Value3')", eventType=EventType.OnStateLoad)
		private String parameter2;
		
		@ConfigConditional(
				config= {@Config(url="/.d/inline_para/parameter4/_replace?rawPayload={\"name\":\"Test\"}")}
		)
		private String parameter3;
		
		private SubModel2 parameter4;
	}
	
	@Model
	@Getter @Setter
	public static class SubModel2{
		private String name;
	}
	
}
