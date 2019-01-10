package com.antheminc.oss.nimbus.test.scenarios.s12.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.event.EventType;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Script;
import com.antheminc.oss.nimbus.domain.defn.extension.Script.Type;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="entityinittest", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter
public class EnityInitTestModel extends IdLong{
	private static final long serialVersionUID = 1L;
	
	private Action1 inline_para;
	
	private Action2 file_para;
	
	private Action3 groovy_para;
	
	@Model
	@Script(type=Type.SPEL_INLINE, value="findParamByPath('/parameter2').setState('Value2')")
	@Getter @Setter
	public static class Action1{
//		@ConfigConditional(
//				config= {@Config(url="/inline_para/parameter3/_process?fn=_set&value=Value3")}
//		)
		@Script(type=Type.SPEL_INLINE, value="findParamByPath('/../parameter2').getState() == 'Value2' ? findParamByPath('/../parameter3').setState('Value3'):''", eventType=EventType.OnStateChange)
		private String parameter2;
		
		private String parameter3;
	}
	
	@Model
	@Script(type=Type.SPEL_FILE, value="classpath:scripts/entity_init_test.txt")
	@Getter @Setter
	public static class Action2{
		@ConfigConditional(
				config= {@Config(url="/file_para/parameter3/_process?fn=_set&value=Value3")}
		)
		private String parameter2;
		
		private String parameter3;
	}
	
	@Model
	@Script(type=Type.GROOVY, value="classpath:scripts/entity_init_test.groovy")
	@Getter @Setter
	public static class Action3{
		@ConfigConditional(
				config= {@Config(url="/groovy_para/parameter3/_process?fn=_set&value=Value3")}
		)
		private String parameter2;
		private String parameter3;
		private String parameter4;
	}
	
}
