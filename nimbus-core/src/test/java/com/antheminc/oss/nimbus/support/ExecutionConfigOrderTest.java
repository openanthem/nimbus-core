/**
 * 
 */
package com.antheminc.oss.nimbus.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;

import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.DetourConfig;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.ExecutionConfigFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
public class ExecutionConfigOrderTest {

	@Test
	public void t00() {
		ExecutionConfigFactory factory = new ExecutionConfigFactory();
		
		ReflectionUtils.doWithFields(TestContainer.class, f -> {
			ExecutionConfig execCfg = factory.build(f);
			
			execCfg.get().forEach(c -> System.out.println(c));
		});
	}
	
	@Test
	public void t01() {
		ExecutionConfigFactory factory = new ExecutionConfigFactory();
		
		ReflectionUtils.doWithFields(TestContainer.class, f -> {
			if(f.getName().contains("fieldWithBothConfigs")) {
				ExecutionConfig execCfg = factory.build(f);
				Annotation a0 = execCfg.get().get(0);
				assertThat(AnnotationUtils.getAnnotationAttributes(a0).get("url")).isEqualTo("2");
				
				Annotation a1 = execCfg.get().get(1);
				assertThat(AnnotationUtils.getAnnotationAttributes(a1).get("url")).isEqualTo("3");
				
				Annotation a2 = execCfg.get().get(2);
				assertThat(a2.annotationType().equals(DetourConfig.class));
			}
			if(f.getName().contains("fieldWithJustConfigs_defaultOrder")) {
				ExecutionConfig execCfg = factory.build(f);
				Annotation a0 = execCfg.get().get(0);
				assertThat(AnnotationUtils.getAnnotationAttributes(a0).get("url")).isEqualTo("1");
				
				Annotation a1 = execCfg.get().get(1);
				assertThat(AnnotationUtils.getAnnotationAttributes(a1).get("url")).isEqualTo("2");
				
				Annotation a2 = execCfg.get().get(2);
				assertThat(AnnotationUtils.getAnnotationAttributes(a2).get("url")).isEqualTo("3");
				
				Annotation a3 = execCfg.get().get(3);
				assertThat(AnnotationUtils.getAnnotationAttributes(a3).get("url")).isEqualTo("4");
			}
			if(f.getName().contains("fieldWithJustConfigs_WithOrder")) {
				ExecutionConfig execCfg = factory.build(f);
				Annotation a0 = execCfg.get().get(0);
				assertThat(AnnotationUtils.getAnnotationAttributes(a0).get("url")).isEqualTo("1");
				
				Annotation a1 = execCfg.get().get(1);
				assertThat(AnnotationUtils.getAnnotationAttributes(a1).get("url")).isEqualTo("2");
				
				Annotation a2 = execCfg.get().get(2);
				assertThat(AnnotationUtils.getAnnotationAttributes(a2).get("url")).isEqualTo("3");
				
				Annotation a3 = execCfg.get().get(3);
				assertThat(AnnotationUtils.getAnnotationAttributes(a3).get("url")).isEqualTo("4");
			}
		});
	}
	
	@Setter @Getter
	public static class TestContainer {
		
		@Config(url="1")
		@Config(url="2", order = 1)
		@Config(url="4")
		@DetourConfig(main= @Config(url="3"), order = 3)
		@Config(url="3", order = 2)
		private String fieldWithBothConfigs;
		
		@Config(url="1")
		@Config(url="2")
		@Config(url="3")
		@Config(url="4")
		private String fieldWithJustConfigs_defaultOrder;
		
		@Config(url="3")
		@Config(url="1", order = 1)
		@Config(url="4")
		@Config(url="2", order = 2)
		private String fieldWithJustConfigs_WithOrder;

	}

}
