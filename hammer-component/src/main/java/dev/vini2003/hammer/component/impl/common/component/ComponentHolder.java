package dev.vini2003.hammer.component.impl.common.component;

public interface ComponentHolder {
	ComponentContainer getComponentContainer();
	
	final int ENTITY = 0;
	final int WORLD = 1;
}
