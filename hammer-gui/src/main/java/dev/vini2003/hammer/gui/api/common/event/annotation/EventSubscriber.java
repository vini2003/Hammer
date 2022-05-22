package dev.vini2003.hammer.gui.api.common.event.annotation;

import dev.vini2003.hammer.gui.api.common.event.type.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscriber {
	public EventType type() default EventType.NONE;
}