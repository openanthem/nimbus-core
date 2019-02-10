package com.antheminc.oss.nimbus.test.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.RepeatContainer;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.EventNoOverride;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
@RepeatContainer(EventNoOverride.class)
public @interface EventNoOverrides {

	EventNoOverride[]  value();
}
