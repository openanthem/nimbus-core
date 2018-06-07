/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s5.core;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ExpressionConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.VisibleConditional;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample core entity to illustrate bulk processing of:<br />
 * <pre>Collections</pre>
 * 
 * @author Soham Chakravarti
 */
@SuppressWarnings("serial")
@Domain(value="s5c_main")
@Getter @Setter @ToString
public class S5C_CoreMain extends IdLong {

	@VisibleConditional(when="state=='Y'", targetPath="../tA", order=1)
	@ExpressionConditional(when="state=='Y'", then="findParamByPath('../tB').setState('triggered')", order=2)
	@VisibleConditional(when="findStateByPath('../tB')=='triggered'", targetPath="../tC")
	private String triggerOrder;
	
	@ConfigConditional(when="state='T'", config=@Config(url="/p2/_replace?rawPayload=\"T2\""))
	@ActivateConditional(when="findStateByPath('../p2')=='T2'", targetPath="../p3/someParam")
	private String trigger2;
	
	private String p2;
	
	private _Nested p3;
	
	private String tA;
	
	private String tB;
	
	private String tC;
	
	@Model @Getter @Setter
	public static class _Nested {
		private String someParam;
	} 
	
	
	public static void main(String args[]) {
		Field f = FieldUtils.getDeclaredField(S5C_CoreMain.class, "triggerOrder", true);
		Set<OnStateLoad> set = AnnotatedElementUtils.findAllMergedAnnotations(f, OnStateLoad.class);
		System.out.println(set);
		
		System.out.println(f.getAnnotations()[0] +" ##  " +AnnotationUtils.getAnnotationAttributes(f.getAnnotations()[0]).get("order"));
	}
	
}
